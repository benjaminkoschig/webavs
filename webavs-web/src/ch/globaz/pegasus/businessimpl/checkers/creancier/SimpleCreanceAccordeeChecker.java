package ch.globaz.pegasus.businessimpl.checkers.creancier;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordee;

public class SimpleCreanceAccordeeChecker {
    public static void checkForCreate(SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
        SimpleCreanceAccordeeChecker.checkIntegrity(simpleCreanceAccordee);
        SimpleCreanceAccordeeChecker.checkMandatory(simpleCreanceAccordee);
    }

    /**
     * @param SimpleCreanceAccordee
     */
    public static void checkForDelete(SimpleCreanceAccordee simpleCreanceAccordee) {
    }

    /**
     * @param SimpleCreanceAccordee
     * @throws CreancierException
     */
    public static void checkForUpdate(SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException {
        SimpleCreanceAccordeeChecker.checkMandatory(simpleCreanceAccordee);
        SimpleCreanceAccordeeChecker.checkIntegrity(simpleCreanceAccordee);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param SimpleCreanceAccordee
     * @throws CreancierException
     */
    private static void checkIntegrity(SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * @param SimpleCreanceAccordee
     */
    private static void checkMandatory(SimpleCreanceAccordee simpleCreanceAccordee) {
        // montant is not null
        if (JadeStringUtil.isEmpty(simpleCreanceAccordee.getMontant())) {
            JadeThread.logError(simpleCreanceAccordee.getClass().getName(),
                    "pegasus.simpleCreanceAccordee.montant.mandatory");
        }
        // idCreancier is not null
        if (JadeStringUtil.isEmpty(simpleCreanceAccordee.getIdCreancier())) {
            JadeThread.logError(simpleCreanceAccordee.getClass().getName(),
                    "pegasus.simpleCreanceAccordee.idCreancier.mandatory");
        }
        // idPCAccordee is not null
        if (JadeStringUtil.isEmpty(simpleCreanceAccordee.getIdPCAccordee())) {
            JadeThread.logError(simpleCreanceAccordee.getClass().getName(),
                    "pegasus.simpleCreanceAccordee.idPCAccordee.mandatory");
        }
    }
}
