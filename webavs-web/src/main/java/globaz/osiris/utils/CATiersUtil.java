/**
 *
 */
package globaz.osiris.utils;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntTiers;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.divers.TIApplication;
import globaz.pyxis.db.tiers.TITiers;

/**
 * @author SEL
 */
public class CATiersUtil {

    /**
     * @param session
     * @param tiers
     * @param idExterne
     * @return TIAvoirAdresse selon la cascade pour le contentieux : Adresse contentieux, adresse domicile, adresse
     *         courrier.
     * @throws Exception
     */
    private static TIAvoirAdresse getAvoirAdresseCascadeContentieux(BSession session, IntTiers tiers, String idExterne)
            throws Exception {
        TIAvoirAdresse avoirAdresse = null;

        avoirAdresse = TITiers.getAvoirAdresse(idExterne, IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                IConstantes.CS_APPLICATION_CONTENTIEUX, JACalendar.today().toStr("."), tiers.getIdTiers(), session);

        if (avoirAdresse == null) {
            avoirAdresse = TITiers.getAvoirAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    IConstantes.CS_APPLICATION_CONTENTIEUX, JACalendar.today().toStr("."), tiers.getIdTiers(), session);
        }
        if (avoirAdresse == null) {
            avoirAdresse = TITiers.getAvoirAdresse(idExterne, IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, JACalendar.today().toStr("."), tiers.getIdTiers(), session);
        }
        if (avoirAdresse == null) {
            avoirAdresse = TITiers.getAvoirAdresse(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    IConstantes.CS_APPLICATION_DEFAUT, JACalendar.today().toStr("."), tiers.getIdTiers(), session);
        }
        if (avoirAdresse == null) {
            avoirAdresse = TITiers.getAvoirAdresse(idExterne, IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    IConstantes.CS_APPLICATION_DEFAUT, JACalendar.today().toStr("."), tiers.getIdTiers(), session);
        }
        if (avoirAdresse == null) {
            avoirAdresse = TITiers.getAvoirAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER,
                    IConstantes.CS_APPLICATION_DEFAUT, JACalendar.today().toStr("."), tiers.getIdTiers(), session);
        }
        return avoirAdresse;
    }

    /**
     * Retourne le domaine d'adresse standard à utiliser par tous les documents.
     * <p>
     * Note: lors de la recherche d'une adresse, il faut utiliser une recherche sur tous les domaines (c'est-à-dire avec
     * le boolean {@link TITiers#getAdresseAsDataSource(String, String, String, boolean) hérité} à vrai) pour le cas où
     * l'adresse pour le domaine d'application retourné par cette méthode ne serait pas renseigné pour un tiers.
     * </p>
     * 
     * @return
     */
    public static final String getIdApplicationAdresse() {
        return TIApplication.CS_CONTENTIEUX;
    }

    /**
     * Retourne la designation de l'office des poursuites correspondant à la localité de l'affilié
     * 
     * @param session
     * @param tiers
     *            Affilié
     * @param idExterneRole
     * @return la designation de l'office des poursuites correspondant à la localité de l'affilié
     * @throws Exception
     */
    public static String getLibelleOfficeDesPoursuitesCourt(BSession session, IntTiers tiers, String idExterneRole)
            throws Exception {
        String description = null;

        if ((tiers != null) && !JadeStringUtil.isIntegerEmpty(tiers.getIdTiers())) {
            tiers = CATiersUtil.getOfficePoursuite(session, tiers, idExterneRole);
            if (tiers != null) {
                description = tiers.getDesignation1();
                if (!JadeStringUtil.isBlank(tiers.getDesignation2())) {
                    // Ecrase la desription avec la designation2
                    description = tiers.getDesignation2();
                }
            }
        }
        if (JadeStringUtil.isBlank(description)) {
            description = session.getLabel("CONT_POU_OFF_NON_DET");
        }

        return description;
    }

    /**
     * Retourne le nom de l'office des poursuites correspondant à la localité de l'affilié
     * 
     * @param session
     * @param tiers
     *            Affilié
     * @param idExterneRole
     * @return la designation de l'office des poursuites correspondant à la localité de l'affilié
     * @throws Exception
     */
    public static String getLibelleOfficeDesPoursuitesLong(BSession session, IntTiers tiers, String idExterneRole)
            throws Exception {
        String description = null;

        if ((tiers != null) && !JadeStringUtil.isIntegerEmpty(tiers.getIdTiers())) {
            tiers = CATiersUtil.getOfficePoursuite(session, tiers, idExterneRole);
            if (tiers != null) {
                description = tiers.getNom();
            }
        }

        if (JadeStringUtil.isBlank(description)) {
            description = session.getLabel("CONT_POU_OFF_NON_DET");
        }

        return description;
    }

    /**
     * Retourne l'office des poursuites pour le tiers donné en se basant sur les liens.
     * 
     * @param session
     * @param tiers
     *            le tiers dont on veut trouver l'OP.
     * @param idExterne
     * @return l'OP lié directement au tiers s'il existe ou l'OP lié à la localité de l'adresse du tiers sinon.
     * @throws Exception
     */
    public static IntTiers getOfficePoursuite(BSession session, IntTiers tiers, String idExterne) throws Exception {
        TIAvoirAdresse avoirAdresse = CATiersUtil.getAvoirAdresseCascadeContentieux(session, tiers, idExterne);
        return tiers.getOfficePoursuitesSelonLien(avoirAdresse.getIdLocalite());
    }

    /**
     * Retourne le canton (sous forme courte, GE, NE, etc...) de l'office des poursuites relatif au tiers passé en
     * paramètre en se basant sur les liens et sur la localité le cas échéant
     * 
     * @param session
     * @param tiers
     * @param idExterne
     * @param idExterneRole
     * @return le canton ou null si non trouvé
     * @throws Exception
     */
    public static String getCantonOfficePoursuite(BSession session, IntTiers tiers, String idExterne,
            String idExterneRole) throws Exception {

        // recherche de l'office des poursuites relatifs au tiers
        IntTiers officePoursuite = CATiersUtil.getOfficePoursuite(session, tiers, idExterne);
        TITiers officePoursuiteTiers = new TITiers();
        officePoursuiteTiers.setIdTiers(officePoursuite.getIdTiers());
        officePoursuiteTiers.setSession(session);
        officePoursuiteTiers.retrieve();

        // détermination du canton de l'office des poursuites
        String cantonOfficePoursuite = null;
        if (!officePoursuiteTiers.isNew()) {
            TIAdresseDataSource adresseDataSource = officePoursuiteTiers.getAdresseAsDataSource(
                    IConstantes.CS_AVOIR_ADRESSE_COURRIER, IConstantes.CS_APPLICATION_DEFAUT, idExterneRole,
                    JACalendar.todayJJsMMsAAAA(), true, officePoursuiteTiers.getLangueIso());
            cantonOfficePoursuite = adresseDataSource.canton_court;
        }

        return cantonOfficePoursuite;
    }
}
