package globaz.cygnus.vb.prestationsaccordees;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointTiers;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Vector;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;

/**
 * @author fha
 */
public class RFPrestationsAccordeesViewBean extends RFPrestationAccordeeJointTiers implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // attributes
    private transient Vector<String[]> csEtatsPrestationsAccordeesData = null;
    private transient Vector<String[]> csGenresPrestationsAccordeesData = null;
    private transient Vector<String[]> csTypesPrestationsAccordeesData = null;

    private transient String likeNumeroAVS = "";
    private transient Vector<String[]> orderBy = null;

    // ~ Constructor ------------------------------------------------------------------------------------------------
    public RFPrestationsAccordeesViewBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getAdresse(String idTiers) {

        PRTiersWrapper tier = null;
        try {
            tier = PRTiersHelper.getTiersParId(getSession(), idTiers);

            if (tier == null) {
                tier = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
            }

            if (tier != null) {
                String adresse = PRTiersHelper.getAdresseCourrierFormatee(getSession(), idTiers, "",
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);

                return adresse.replaceAll("\n", "<br>");

            } else {
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public String getBeneficiaire(String idTiers) {

        PRTiersWrapper tier = null;
        try {
            tier = PRTiersHelper.getTiersParId(getSession(), idTiers);

            if (tier == null) {
                tier = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
            }

            if (tier != null) {
                if (JadeStringUtil.isEmpty(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL))) {
                    return tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                } else {
                    return tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                }
            } else {
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public Vector<String[]> getCsEtatsPrestationsAccordeesData() {
        if (csEtatsPrestationsAccordeesData == null) {
            csEtatsPrestationsAccordeesData = PRCodeSystem.getLibellesPourGroupe(
                    IREPrestationAccordee.CS_GROUPE_ETAT_RENTE_ACCORDEE, getSession());

            // ajout des options custom
            csEtatsPrestationsAccordeesData.add(0, new String[] { "", "" });
        }

        return csEtatsPrestationsAccordeesData;
    }

    public Vector<String[]> getCsGenresPrestationsAccordeesData() {
        if (csGenresPrestationsAccordeesData == null) {
            csGenresPrestationsAccordeesData = PRCodeSystem.getLibellesPourGroupe(IPCPCAccordee.GROUPE_TYPE_PC,
                    getSession());

            // ajout des options custom
            csGenresPrestationsAccordeesData.add(0, new String[] { "", "" });
        }

        return csGenresPrestationsAccordeesData;
    }

    public Vector<String[]> getCsTypesPrestationsAccordeesData() {
        if (csTypesPrestationsAccordeesData == null) {
            csTypesPrestationsAccordeesData = PRCodeSystem.getLibellesPourGroupe(IPCPCAccordee.GROUPE_GENRE_PC,
                    getSession());

            // ajout des options custom
            csTypesPrestationsAccordeesData.add(0, new String[] { "", "" });
        }

        return csTypesPrestationsAccordeesData;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailAssure() throws Exception {
        if (!JadeStringUtil.isBlankOrZero(getDateDeces())) {
            return PRNSSUtil.formatDetailRequerantListeDecede(getNss(), getNom() + " " + getPrenom(),
                    getDateNaissance(), getLibelleCourtSexe(), getLibellePays(), getDateDeces());
        } else {
            return PRNSSUtil.formatDetailRequerantListe(getNss(), getNom() + " " + getPrenom(), getDateNaissance(),
                    getLibelleCourtSexe(), getLibellePays());
        }
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe() {

        if (PRACORConst.CS_HOMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays() {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", getCsNationalite())))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getCsNationalite()));
        }

    }

    /**
     * Méthode qui retourne le libellé du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleSexe() {

        if (PRACORConst.CS_HOMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    public Vector<String[]> getOrderBy() {
        return orderBy;
    }

    /**
     * getter pour l'attribut order by data
     * 
     * @return la valeur courante de l'attribut order by data
     */
    public Vector<String[]> getOrderByData() {
        if (orderBy == null) {
            orderBy = new Vector<String[]>(2);
            orderBy.add(new String[] {
                    REPrestationsAccordees.FIELDNAME_DATE_DEBUT_DROIT + " DESC ,"
                            + IPRConstantesExternes.FIELDNAME_TABLE_TIERS_NOM_FOR_SEARCH + ","
                            + IPRConstantesExternes.FIELDNAME_TABLE_TIERS_PRENOM_FOR_SEARCH,
                    getSession().getLabel("JSP_OVE_R_TRIER_PAR_DATE_DEBUT_DROIT") });
        }

        return orderBy;
    }

    /**
     * Méthode qui retourne une string avec true si le NSS passé en paramètre est un NNSS, sinon false
     * 
     * @param noAvs
     * @return String (true ou false)
     */
    public String isNNSS(String noAvs) {

        if (JadeStringUtil.isBlankOrZero(noAvs)) {
            return "";
        }

        if (noAvs.length() > 14) {
            return Boolean.TRUE.toString();
        } else {
            return Boolean.FALSE.toString();
        }
    }

    public void setCsEtatsPrestationsAccordeesData(Vector<String[]> csEtatsPrestationsAccordeesData) {
        this.csEtatsPrestationsAccordeesData = csEtatsPrestationsAccordeesData;
    }

    public void setCsGenresPrestationsAccordeesData(Vector<String[]> csGenresPrestationsAccordeesData) {
        this.csGenresPrestationsAccordeesData = csGenresPrestationsAccordeesData;
    }

    public void setCsTypesPrestationsAccordeesData(Vector<String[]> csTypesPrestationsAccordeesData) {
        this.csTypesPrestationsAccordeesData = csTypesPrestationsAccordeesData;
    }

    public void setOrderBy(Vector<String[]> orderBy) {
        this.orderBy = orderBy;
    }

}
