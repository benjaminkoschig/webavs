package ch.globaz.pegasus.businessimpl.checkers.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleCotisationsPsal;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleCotisationsPsalChecker extends PegasusAbstractChecker {
    /**
     * @param simpleCotisationsPsal
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws CotisationsPsalException
     */
    public static void checkForCreate(SimpleCotisationsPsal simpleCotisationsPsal) throws CotisationsPsalException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleCotisationsPsalChecker.checkMandatory(simpleCotisationsPsal);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleCotisationsPsalChecker.checkIntegrity(simpleCotisationsPsal);
        }
    }

    /**
     * @param simpleCotisationsPsal
     */
    public static void checkForDelete(SimpleCotisationsPsal simpleCotisationsPsal) {
    }

    /**
     * @param simpleCotisationsPsal
     */
    public static void checkForUpdate(SimpleCotisationsPsal simpleCotisationsPsal) {
        SimpleCotisationsPsalChecker.checkMandatory(simpleCotisationsPsal);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleCotisationsPsal
     * @throws CotisationsPsalException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleCotisationsPsal simpleCotisationsPsal) throws CotisationsPsalException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simpleCotisationsPsal ait un montantCotisationAnnuelle Vérifie que le simpleCotisationsPsal ait un
     * idCaisseCompensation
     * 
     * @param simpleCotisationsPsal
     */
    private static void checkMandatory(SimpleCotisationsPsal simpleCotisationsPsal) {

        // Vérifie que le simpleCotisationsPsal ait un montant mensuel
        if (JadeStringUtil.isEmpty(simpleCotisationsPsal.getMontantCotisationsAnnuelles())) {
            JadeThread.logError(simpleCotisationsPsal.getClass().getName(),
                    "pegasus.simpleCotisationsPsal.montantCotisationsAnnuelles.mandatory");
        }

        // Vérifie que le simpleCotisationsPsal ait un id_caisse_af
        if (JadeStringUtil.isEmpty(simpleCotisationsPsal.getIdCaisseCompensation())) {
            JadeThread.logError(simpleCotisationsPsal.getClass().getName(),
                    "pegasus.simpleCotisationsPsal.idCaisseCompensation.mandatory");
        }
    }
}
