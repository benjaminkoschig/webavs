package ch.globaz.pegasus.businessimpl.checkers.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.PensionAlimentaireException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimplePensionAlimentaire;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimplePensionAlimentaireChecker extends PegasusAbstractChecker {
    /**
     * @param simplePensionAlimentaire
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws PensionAlimentaireException
     */
    public static void checkForCreate(SimplePensionAlimentaire simplePensionAlimentaire)
            throws PensionAlimentaireException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimplePensionAlimentaireChecker.checkMandatory(simplePensionAlimentaire);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimplePensionAlimentaireChecker.checkIntegrity(simplePensionAlimentaire);
        }
    }

    /**
     * @param simplePensionAlimentaire
     */
    public static void checkForDelete(SimplePensionAlimentaire simplePensionAlimentaire) {
    }

    /**
     * @param simplePensionAlimentaire
     */
    public static void checkForUpdate(SimplePensionAlimentaire simplePensionAlimentaire) {
        SimplePensionAlimentaireChecker.checkMandatory(simplePensionAlimentaire);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simplePensionAlimentaire
     * @throws PensionAlimentaireException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimplePensionAlimentaire simplePensionAlimentaire)
            throws PensionAlimentaireException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simplePensionAlimentaire ait un montant pension
     * 
     * @param simplePensionAlimentaire
     */
    private static void checkMandatory(SimplePensionAlimentaire simplePensionAlimentaire) {

        // Vérifie que le simplePensionAlimentaire ait un montant mensuel
        if (JadeStringUtil.isEmpty(simplePensionAlimentaire.getMontantPensionAlimentaire())) {
            JadeThread.logError(simplePensionAlimentaire.getClass().getName(),
                    "pegasus.simplePensionAlimentaire.montantPensionAlimentaire.mandatory");
        }

        // si la pension est due alors le motif et le lien est obligatoire
        if (simplePensionAlimentaire.getCsTypePension().equals("64031001")) {
            if (JadeStringUtil.isEmpty(simplePensionAlimentaire.getCsMotif())) {
                JadeThread.logError(simplePensionAlimentaire.getClass().getName(),
                        "pegasus.simplePensionAlimentaire.motifPensionAlimentaire.mandatory");
            }
            if (JadeStringUtil.isEmpty(simplePensionAlimentaire.getCsLienAvecRequerantPC())) {
                JadeThread.logError(simplePensionAlimentaire.getClass().getName(),
                        "pegasus.simplePensionAlimentaire.lienPensionAlimentaire.mandatory");
            }
        }

        // si la pension est due alors le lien est obligatoire

    }
}
