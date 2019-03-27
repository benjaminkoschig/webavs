package globaz.apg.utils;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.properties.JadePropertiesService;

/**
 * Une simple classe utilitaire. Devrait disparaitre....
 *
 * @author lga
 */
public class APGUtils {

    /**
     * Dans un soucis de cohérence, il serait plus sage d'utiliser la méthode avec la transaction...
     *
     * @param session
     *                         La session à utiliser
     * @param idDroit
     *                         L'id du droit à rechercher
     * @param genreService
     *                         Le genre de service du droit à rechercher
     * @return Le droit dans le bon type; APDroitAPG ou APDroitMaternite sinon une exception sera lancée
     * @throws Exception
     *                       Si le traitement de récupération du droit à échoué
     */
    @Deprecated
    public static final APDroitLAPG loadDroit(BSession session, String idDroit, String genreService) throws Exception {
        return APGUtils.loadDroit(session, null, idDroit, genreService);
    }

    /**
     * Dans un soucis de cohérence, il serait plus sage d'utiliser la méthode avec la transaction...
     *
     * @param session
     *                         La session à utiliser
     * @param transaction
     *                         La transaction en cours à utiliser
     * @param idDroit
     *                         L'id du droit à rechercher
     * @param genreService
     *                         Le genre de service du droit à rechercher
     * @return Le droit dans le bon type; APDroitAPG ou APDroitMaternite sinon une exception sera lancée
     * @throws Exception
     *                       Si le traitement de récupération du droit à échoué
     */
    public static final APDroitLAPG loadDroit(BSession session, BTransaction transaction, String idDroit,
            String genreService) throws Exception {
        APDroitLAPG droit;
        if (session == null) {
            throw new IllegalArgumentException("The provided BSession is null");
        }
        if (JadeStringUtil.isEmpty(idDroit)) {
            throw new Exception("idDroit is null or empty");
        }
        if (JadeStringUtil.isEmpty(genreService)) {
            throw new Exception("genre service inconnu");
        }
        if (IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE.equals(genreService)) {
            droit = new APDroitMaternite();
        } else {
            droit = new APDroitAPG();
        }
        droit.setSession(session);
        droit.setIdDroit(idDroit);
        if (transaction != null) {
            droit.retrieve(transaction);
        } else {
            droit.retrieve();
        }
        return droit;
    }

    public static Boolean isTypeAllocationJourIsole(String csTypeAllocation) {
        String isFerciab = JadePropertiesService.getInstance().getProperty(APApplication.PROPERTY_IS_FERCIAB);
        return (IAPDroitLAPG.CS_DEMENAGEMENT_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_NAISSANCE_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_MARIAGE_LPART_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_DECES_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_JOURNEES_DIVERSES_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_CONGE_JEUNESSE_CIAB.equals(csTypeAllocation)
                || IAPDroitLAPG.CS_SERVICE_ETRANGER_CIAB.equals(csTypeAllocation)) && "true".equals(isFerciab);
    }
}
