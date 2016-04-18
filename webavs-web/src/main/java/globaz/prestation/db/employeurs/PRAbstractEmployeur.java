package globaz.prestation.db.employeurs;

import globaz.globall.api.BISession;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.webavs.common.CommonProperties;

/**
 * <H1>Description</H1>
 *
 * <p>
 * Classe de base pour les employeurs, fournit des methodes pour la gestion des employeurs avec ou sans numero
 * d'affilie.
 * </p>
 *
 * @author vre
 */
public abstract class PRAbstractEmployeur extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String SUFFIXE_NUM_BIDON = "?";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * dans le cas ou un numero d'affilie a ete genere par la methode {@link #loadNumero() loadNumero()}, extrait
     * l'identifiant du tiers de ce numero.
     *
     * @param numAffilie
     *            un numero bidon (pas de test)
     *
     * @return DOCUMENT ME!
     */
    public static final String extractIdTiers(String numAffilie) {
        String classFormatNoAffilie = "";
        String retValue = "";

        try {
            classFormatNoAffilie = PRAbstractApplication.getApplication("APG")
                    .getProperty(CommonProperties.KEY_FORMAT_NUM_AFFILIE);
        } catch (Exception e) {
            e.printStackTrace();
            // on utilise la classe de formattage standard
            classFormatNoAffilie = "globaz.cicicam.format.CICICAMNumAffilie";
        }

        try {
            // on cherche la classe de formattage de la caisse
            IFormatData noAffilieFormater = (IFormatData) Class.forName(classFormatNoAffilie).newInstance();
            retValue = noAffilieFormater.unformat(numAffilie);

        } catch (Exception e) {
            e.printStackTrace();
            // un unformat a l'ancienne qui marche dans 99% des cas
            while (numAffilie.indexOf(".") > 0) {
                numAffilie = numAffilie.substring(0, numAffilie.indexOf("."))
                        + numAffilie.substring(numAffilie.indexOf(".") + 1, numAffilie.length());
            }
            retValue = numAffilie;
        }

        // suppression du suffixe
        retValue = retValue.substring(0, retValue.indexOf(PRAbstractEmployeur.SUFFIXE_NUM_BIDON));

        if (retValue.startsWith("0")) {
            int lastZero = 0;

            do {
                ++lastZero;
            } while (retValue.charAt(lastZero) == '0');

            retValue = retValue.substring(lastZero);
        }

        return retValue;
    }

    /**
     * retourne vrai si le numero passe en argument semble avoir ete genere au par la methode {@link #loadNumero()
     * loadNumero}.
     *
     * @param numAffilie
     *            DOCUMENT ME!
     *
     * @return la valeur courante de l'attribut numero bidon
     */
    public static final boolean isNumeroBidon(String numAffilie) {
        return (numAffilie != null) && numAffilie.endsWith(PRAbstractEmployeur.SUFFIXE_NUM_BIDON);
    }

    private transient IPRAffilie affilie;
    private transient PRDepartement departement;

    private String idAffilie = "";
    private String idEmployeur = "";
    private String idParticularite = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    private String idTiers = "";

    private transient PRTiersWrapper tiers;

    /**
     * cree une cle unique qui identifie cet employeur en se basant sur son idTiers et son idAffilie (s'il existe).
     *
     * @return la valeur courante de l'attribut cle unique
     */
    public String getCleUnique() {
        return idTiers + "_" + (JadeStringUtil.isIntegerEmpty(idAffilie) ? "?" : idAffilie);
    }

    /**
     * getter pour l'attribut id affilie.
     *
     * @return la valeur courante de l'attribut id affilie
     */
    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * getter pour l'attribut id employeur.
     *
     * @return la valeur courante de l'attribut id employeur
     */
    public String getIdEmployeur() {
        return idEmployeur;
    }

    /**
     * getter pour l'attribut id particularite.
     *
     * @return la valeur courante de l'attribut id particularite
     */
    public String getIdParticularite() {
        return idParticularite;
    }

    /**
     * getter pour l'attribut id tiers.
     *
     * @return la valeur courante de l'attribut id tiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * charge cet employeur en tant qu'affilie.
     *
     * @return une instance de IPRAffilie ou null si idAffilie est null ou 0
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IPRAffilie loadAffilie() throws Exception {
        if ((affilie == null) && !JadeStringUtil.isIntegerEmpty(idAffilie)) {
            affilie = PRAffiliationHelper.getEmployeurParIdAffilie(getSession(),
                    getSession().getCurrentThreadTransaction(), idAffilie, idTiers);
        }

        return affilie;
    }

    /**
     * @return DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public PRDepartement loadDepartement() throws Exception {
        if ((departement == null) && !JadeStringUtil.isIntegerEmpty(idParticularite)) {
            departement = new PRDepartement();
            departement.setIdParticularite(idParticularite);
            departement.setSession(getSession());
            departement.retrieve();
        }

        return departement;
    }

    /**
     * retourne le nom de cet employeur.
     *
     * <p>
     * S'il s'agit d'un affilie, retourne le nom de l'entreprise, sinon, retourne le nom du tiers.
     * </p>
     *
     * @return retourne le nom de l'entreprise ou le nom du tiers ou chaine vide si idTiers est null ou 0.
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String loadNom() throws Exception {
        return loadNomEmployeur(idTiers, idAffilie, getISession());
    }

    public static String loadNomEmployeur(String idTiers, String idAffilie, BISession session) throws Exception {
        String result = "";
        if (JadeStringUtil.isIntegerEmpty(idTiers)) {
            return result;
        }

        if (JadeStringUtil.isIntegerEmpty(idAffilie)) {

            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, idTiers);
            if (tiers != null) {
                result = tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                result += " " + tiers.getProperty(PRTiersWrapper.PROPERTY_NOM);
                result = result.trim();

            } else {
                tiers = PRTiersHelper.getAdministrationParId(session, idTiers);
                if (tiers != null) {
                    String temp = tiers.getProperty(PRTiersWrapper.PROPERTY_NOM);
                    if (!JadeStringUtil.isBlank(temp)) {
                        result = temp.trim();
                    }
                    temp = tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                    if (!JadeStringUtil.isBlank(temp)) {
                        result += " " + temp.trim();
                    }
                    temp = tiers.getProperty(PRTiersWrapper.PROPERTY_DESIGNATION_3);
                    if (!JadeStringUtil.isBlank(temp)) {
                        result += " " + temp.trim();
                    }
                }
            }

        } else {
            IPRAffilie affilie = PRAffiliationHelper.getEmployeurParIdAffilie(session,
                    ((BSession) session).getCurrentThreadTransaction(), idAffilie, idTiers);

            if (affilie != null) {
                result = affilie.getNom();

                // Dans le cas d'un independant, il y a aussi un prenom
                if (!JadeStringUtil.isEmpty(result) && !JadeStringUtil.isEmpty(affilie.getNoAVS())) {

                    PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, idTiers);

                    if (tiers != null && !JadeStringUtil.isEmpty(tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM))) {
                        result += " " + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                    }
                }
            }
        }

        // Supression des caractères spéciaux dans les noms des employeurs, car
        // si existant le fichier batch généré va s'interrompre, car non supporté par la commande DOS : ECHO
        result = cleanNomEmployeur(result);
        return result;
    }

    /**
     * élimine les caractère spéciaux pouvant créer des problèmes</br>
     * Supression des caractères spéciaux dans les noms des employeurs, car
     * si existant
     * le fichier batch généré va s'interrompre, car non supporté par la
     * commande DOS : ECHO
     *
     * @param result
     * @return
     */
    private static String cleanNomEmployeur(String result) {
        result = result.replace('&', ' ');
        result = result.replace('<', ' ');
        result = result.replace('>', ' ');
        result = result.replace('\'', ' ');
        result = result.replace('"', ' ');
        return result;
    }

    /**
     * s'il est affilie, retourne le numero d'affilie de cet employeur, sinon retourne un no bidon mais unique (base sur
     * son idTiers).
     *
     * <p>
     * Note: Il n'y a pas de verification du idTiers, il peut etre vide ou 0.
     * </p>
     *
     * <p>
     * Note: il est possible de determiner si un numero d'affilie bidon a ete cree avec cette methode en utilisant la
     * methode {@link #isNumeroBidon(String) isNumeroBidon}
     * </p>
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String loadNumero() throws Exception {
        if (JadeStringUtil.isIntegerEmpty(idAffilie)) {
            return JadeStringUtil.rightJustify(getIdTiers(), 8, '0') + PRAbstractEmployeur.SUFFIXE_NUM_BIDON;
        } else {
            return loadAffilie().getNumAffilie();
        }
    }

    /**
     * charge cet employeur en tant que tiers.
     *
     * @return une instance PRTiersWrapper ou null si idTiers est null ou 0.
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public PRTiersWrapper loadTiers() throws Exception {
        if ((tiers == null) && !JadeStringUtil.isIntegerEmpty(idTiers)) {
            tiers = PRTiersHelper.getTiersParId(getISession(), idTiers);
        }

        return tiers;
    }

    /**
     * charge cet employeur en tant qu'administration.
     *
     * @return une instance PRTiersWrapper ou null si idTiers est null ou 0.
     *
     * @throws Exception
     */
    public PRTiersWrapper loadAdministration() throws Exception {
        if ((tiers == null) && !JadeStringUtil.isIntegerEmpty(idTiers)) {
            tiers = PRTiersHelper.getAdministrationParId(getISession(), idTiers);
        }

        return tiers;
    }

    /**
     * setter pour l'attribut id affilie.
     *
     * @param idAffilie
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
        affilie = null;
    }

    /**
     * setter pour l'attribut id employeur.
     *
     * @param idEmployeur
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    /**
     * setter pour l'attribut id particularite.
     *
     * @param idParticularite
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdParticularite(String idParticularite) {
        this.idParticularite = idParticularite;
    }

    /**
     * setter pour l'attribut id tiers.
     *
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
        tiers = null;
    }
}
