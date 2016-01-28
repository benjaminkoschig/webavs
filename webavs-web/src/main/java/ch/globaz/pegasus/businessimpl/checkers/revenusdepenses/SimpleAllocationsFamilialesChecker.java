package ch.globaz.pegasus.businessimpl.checkers.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AllocationsFamilialesException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleAllocationsFamiliales;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleAllocationsFamilialesChecker extends PegasusAbstractChecker {
    /**
     * @param simpleAllocationsFamiliales
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws AllocationsFamilialesException
     */
    public static void checkForCreate(SimpleAllocationsFamiliales simpleAllocationsFamiliales)
            throws AllocationsFamilialesException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleAllocationsFamilialesChecker.checkMandatory(simpleAllocationsFamiliales);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleAllocationsFamilialesChecker.checkIntegrity(simpleAllocationsFamiliales);
        }
    }

    /**
     * @param simpleAllocationsFamiliales
     */
    public static void checkForDelete(SimpleAllocationsFamiliales simpleAllocationsFamiliales) {
    }

    /**
     * @param simpleAllocationsFamiliales
     */
    public static void checkForUpdate(SimpleAllocationsFamiliales simpleAllocationsFamiliales) {
        SimpleAllocationsFamilialesChecker.checkMandatory(simpleAllocationsFamiliales);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleAllocationsFamiliales
     * @throws AllocationsFamilialesException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleAllocationsFamiliales simpleAllocationsFamiliales)
            throws AllocationsFamilialesException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * <li>Vérifie que le simpleAllocationsFamiliales ait un montant</li> <li>
     * Vérifie que le simpleAllocationsFamiliales ait un id_caisse_af</li>
     * 
     * @param simpleAllocationsFamiliales
     */
    private static void checkMandatory(SimpleAllocationsFamiliales simpleAllocationsFamiliales) {

        // Vérifie que le simpleAllocationsFamiliales ait un montant mensuel
        if (JadeStringUtil.isEmpty(simpleAllocationsFamiliales.getMontantMensuel())) {
            JadeThread.logError(simpleAllocationsFamiliales.getClass().getName(),
                    "pegasus.simpleAllocationsFamiliales.montantMensuel.mandatory");
        }

        // Vérifie que le simpleAllocationsFamiliales ait un id_caisse_af
        if (JadeStringUtil.isEmpty(simpleAllocationsFamiliales.getIdCaisseAf())) {
            JadeThread.logError(simpleAllocationsFamiliales.getClass().getName(),
                    "pegasus.simpleAllocationsFamiliales.idCaisseAf.mandatory");
        }
    }
}
