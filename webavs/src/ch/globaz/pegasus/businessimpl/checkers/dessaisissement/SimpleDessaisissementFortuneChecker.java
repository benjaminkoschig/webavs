package ch.globaz.pegasus.businessimpl.checkers.dessaisissement;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementFortuneException;
import ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementFortune;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class SimpleDessaisissementFortuneChecker extends PegasusAbstractChecker {
    /**
     * @param simpleDessaisissementFortune
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DessaisissementFortuneException
     */
    public static void checkForCreate(SimpleDessaisissementFortune simpleDessaisissementFortune)
            throws DessaisissementFortuneException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleDessaisissementFortuneChecker.checkMandatory(simpleDessaisissementFortune);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDessaisissementFortuneChecker.checkIntegrity(simpleDessaisissementFortune);
        }
    }

    /**
     * @param simpleDessaisissementFortune
     */
    public static void checkForDelete(SimpleDessaisissementFortune simpleDessaisissementFortune) {
    }

    /**
     * @param simpleDessaisissementFortune
     */
    public static void checkForUpdate(SimpleDessaisissementFortune simpleDessaisissementFortune) {
        SimpleDessaisissementFortuneChecker.checkMandatory(simpleDessaisissementFortune);
    }

    /*
     * public static void checkHasFacteurByConversionRente( ConversionRenteSearch cRente) { if (cRente.getSize() == 0) {
     * JadeThread.logError(cRente.getClass().getName(), "pegasus.dessaisissementFortune.facteurTableAFC.notFound"); }
     * 
     * }/*
     * 
     * 
     * 
     * 
     * 
     * /** Verification de l'integrite des donnees
     * 
     * @param simpleDessaisissementFortune
     * 
     * @throws DessaisissementFortuneException
     * 
     * @throws JadePersistenceException
     * 
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleDessaisissementFortune simpleDessaisissementFortune)
            throws DessaisissementFortuneException, JadePersistenceException, JadeNoBusinessLogSessionError {
        // TODO implementer checker
    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * <li>Vérifie que le simpleDessaisissementFortune ait un type de propriete</li> <li>
     * Vérifie que le simpleDessaisissementFortune ait une part de propriete</li> <li>Vérifie que le
     * simpleDessaisissementFortune ait un montant</li>
     * 
     * @param simpleDessaisissementFortune
     */
    private static void checkMandatory(SimpleDessaisissementFortune simpleDessaisissementFortune) {

        if (JadeStringUtil.isEmpty(simpleDessaisissementFortune.getMontantBrut())) {
            JadeThread.logError(simpleDessaisissementFortune.getClass().getName(),
                    "pegasus.simpleDessaisissementFortune.montantbrutdessaisi.mandatory");
        }

        if (JadeStringUtil.isEmpty(simpleDessaisissementFortune.getDeductionMontantDessaisi())) {
            JadeThread.logError(simpleDessaisissementFortune.getClass().getName(),
                    "pegasus.simpleDessaisissementFortune.montantdeduction.mandatory");
        }

    }
}
