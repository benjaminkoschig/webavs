package globaz.hercule.service;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.globall.db.BSession;
import globaz.hercule.db.controleEmployeur.CEAffilie;
import globaz.hercule.db.controleEmployeur.CEAffilieManager;
import globaz.hercule.exception.HerculeException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pyxis.db.tiers.TITiers;

/**
 * @author SCO
 * @since SCO 2 juin 2010
 */
public class CEAffiliationService {

    /**
     * Recherche d'un affilié par son numéro d'affilié.<br>
     * Retourne la premiere occurence trouvée.
     * 
     * @param session
     * @param affilieNumero
     * @return AFAffiliation
     * @throws Exception
     */
    public static AFAffiliation findAffilie(BSession session, String affilieNumero, String dateDebut, String dateFin)
            throws Exception {

        if (session == null) {
            throw new HerculeException("Unabled to retrieve AFAffiliation, session is null");
        }

        if (JadeStringUtil.isBlankOrZero(affilieNumero)) {
            throw new HerculeException("Unabled to retrieve AFAffiliation, affilieNumero is null or empty");
        }

        AFAffiliationManager manager = new AFAffiliationManager();
        manager.setSession(session);
        manager.setForAffilieNumero(affilieNumero);
        manager.setFromDateFin(dateFin);
        manager.setForDateDebutAffLowerOrEqualTo(dateDebut);
        String[] typeAffiliation = { "804002", "804005", "804012" };
        manager.setForTypeAffiliation(typeAffiliation);

        try {
            manager.find();
        } catch (Exception e) {
            throw new HerculeException("Technical exception, Unabled to find AFAffiliation (affilieNumero : "
                    + affilieNumero + ")", e);
        }

        if (manager.size() > 0) {
            return (AFAffiliation) manager.getFirstEntity();
        }

        return null;
    }

    /**
     * Recherche le nom d'un affilié
     * 
     * @param numAffilie
     *            Le numéro d'un affilié
     * @param session
     *            La session
     * @return
     */
    public static String findNomAffilie(String numAffilie, BSession session) {

        String nomPrenom = "";

        try {
            if (!JadeStringUtil.isEmpty(numAffilie)) {
                AFAffiliation affiliation = CEAffiliationService.findAffilie(session, numAffilie, null, null);
                if (affiliation != null) {
                    TITiers tiers = CETiersService.retrieveTiers(session, affiliation.getIdTiers());
                    if (!tiers.isNew()) {
                        nomPrenom = tiers.getNomPrenom();
                    }
                }
            }

        } catch (Exception ex) {
            JadeLogger.warn(CEAffiliationService.class, ex);
        }

        return nomPrenom;
    }

    /**
     * Récupération d'un numéro d'affilié. Si non trouvé, renvoit blanc.
     * 
     * @param session
     *            Une session
     * @param affiliationId
     *            Un id d'afficialit
     * @return
     */
    public static String findNumeroAffilie(BSession session, String affiliationId) {

        AFAffiliation aff = null;

        try {
            aff = CEAffiliationService.retrieveAffilie(session, affiliationId);
        } catch (Exception e) {
            JadeLogger.warn(CEAffiliationService.class, e);
            return "";
        }

        return aff.getAffilieNumero();
    }

    /**
     * Permet la récupération de l'information d'un affilie (Nom et date d'affiliation)
     * 
     * @param session
     * @param idAffiliation
     * @return
     */
    public static String getNomEtDateAffiliation(BSession session, String idAffiliation) {
        String infoTiers = "";

        CEAffilieManager affilieManager = new CEAffilieManager();
        affilieManager.setSession(session);
        affilieManager.setForIdAffiliation(idAffiliation);

        try {
            affilieManager.find();

            if (affilieManager.size() == 1) {
                CEAffilie affilie = (CEAffilie) affilieManager.getFirstEntity();
                if (affilie != null) {

                    String dateFinAffiliation = JadeStringUtil.isEmpty(affilie.getDateFinAffiliation()) ? "*" : affilie
                            .getDateFinAffiliation();

                    infoTiers = affilie.getNom() + '\n' + affilie.getDateDebutAffiliation() + " - "
                            + dateFinAffiliation;
                }
            }
        } catch (Exception e) {
            JadeLogger.error(CEAffiliationService.class, e);
        }

        return infoTiers;
    }

    /**
     * Retourne l'id role de l'affilié paritaire
     * 
     * @param session
     * @return
     */
    public static String getRoleForAffilieParitaire(BSession session) {

        String roleForAffilie = "";

        try {
            roleForAffilie = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(session.getApplication());
            if (JadeStringUtil.isEmpty(roleForAffilie)) {
                throw new Exception();
            }
        } catch (Exception e) {
            session.addError("unable to load propertie roleForAffilie");
        }

        return roleForAffilie;
    }

    /**
     * Récupération d'un affilié
     * 
     * @param session
     *            Une session
     * @param affiliationId
     *            Un identifiant d'affilié
     * @return Un affilié de type AFAffiliation
     * @throws Exception
     */
    public static AFAffiliation retrieveAffilie(BSession session, String affiliationId) throws Exception {

        if (JadeStringUtil.isIntegerEmpty(affiliationId)) {
            throw new HerculeException("Unabled to retrieve AFAffiliation, affiliationID is null or empty");
        }

        AFAffiliation aff = new AFAffiliation();
        aff.setSession(session);
        aff.setAffiliationId(affiliationId);
        try {
            aff.retrieve();
        } catch (Exception e) {
            throw new HerculeException("Technical exception, Unabled to retrieve AFAffiliation (affiliationID : "
                    + affiliationId + ")", e);
        }

        if (!aff.hasErrors()) {
            return aff;
        }

        return null;
    }

    /**
     * Constructeur de CEAffiliationService
     */
    protected CEAffiliationService() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
