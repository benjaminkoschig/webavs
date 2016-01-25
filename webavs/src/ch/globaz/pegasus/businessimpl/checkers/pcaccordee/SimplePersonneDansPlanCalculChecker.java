package ch.globaz.pegasus.businessimpl.checkers.pcaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePersonneDansPlanCalcul;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimplePersonneDansPlanCalculChecker extends PegasusAbstractChecker {
    /**
     * @param simpleEnfantDansCalcul
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws PCAccordeeException
     */
    public static void checkForCreate(SimplePersonneDansPlanCalcul simpleEnfantDansCalcul) throws PCAccordeeException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimplePersonneDansPlanCalculChecker.checkMandatory(simpleEnfantDansCalcul);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimplePersonneDansPlanCalculChecker.checkIntegrity(simpleEnfantDansCalcul);
        }
    }

    /**
     * @param simpleEnfantDansCalcul
     */
    public static void checkForDelete(SimplePersonneDansPlanCalcul simpleEnfantDansCalcul) {
    }

    /**
     * @param simpleEnfantDansCalcul
     */
    public static void checkForUpdate(SimplePersonneDansPlanCalcul simpleEnfantDansCalcul) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleEnfantDansCalcul
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimplePersonneDansPlanCalcul simpleEnfantDansCalcul) throws PCAccordeeException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simpleEnfantDansCalcul ait un idPlanDeCalcul. Vérifie que le simpleEnfantDansCalcul ait un
     * idDroitMembreFamille. Vérifie que le simpleEnfantDansCalcul ait un isPlanRetenu non null.
     * 
     * @param simplePlanDeCalcul
     */
    private static void checkMandatory(SimplePersonneDansPlanCalcul simpleEnfantDansCalcul) {

        // Vérifie que le simpleEnfantDansCalcul ait un idPlanDeCalcul
        if (JadeStringUtil.isIntegerEmpty(simpleEnfantDansCalcul.getIdPlanDeCalcul())) {
            JadeThread.logError(simpleEnfantDansCalcul.getClass().getName(),
                    "pegasus.simpleEnfantDansCalcul.idPlanDeCalcul.mandatory");
        }

        // Vérifie que le simpleEnfantDansCalcul ait un idDroitMembreFamille
        if (JadeStringUtil.isIntegerEmpty(simpleEnfantDansCalcul.getIdDroitMembreFamille())) {
            JadeThread.logError(simpleEnfantDansCalcul.getClass().getName(),
                    "pegasus.simpleEnfantDansCalcul.idDroitMembreFamille.mandatory");
        }

        // Vérifie que le simpleEnfantDansCalcul ait un isComprisDansCalcul
        if (simpleEnfantDansCalcul.getIsComprisDansCalcul() == null) {
            JadeThread.logError(simpleEnfantDansCalcul.getClass().getName(),
                    "pegasus.simpleEnfantDansCalcul.isComprisDansCalcul.mandatory");
        }

    }
}
