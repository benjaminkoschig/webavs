package ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AssuranceVieException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleAssuranceVie;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleAssuranceVieChecker extends PegasusAbstractChecker {
    /**
     * @param simpleAssuranceVie
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws AssuranceVieException
     */
    public static void checkForCreate(SimpleAssuranceVie simpleAssuranceVie) throws AssuranceVieException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleAssuranceVieChecker.checkMandatory(simpleAssuranceVie);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleAssuranceVieChecker.checkIntegrity(simpleAssuranceVie);
        }
    }

    /**
     * @param simpleAssuranceVie
     */
    public static void checkForDelete(SimpleAssuranceVie simpleAssuranceVie) {
    }

    /**
     * @param simpleAssuranceVie
     */
    public static void checkForUpdate(SimpleAssuranceVie simpleAssuranceVie) {
        SimpleAssuranceVieChecker.checkMandatory(simpleAssuranceVie);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleAssuranceVie
     * @throws AssuranceVieException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleAssuranceVie simpleAssuranceVie) throws AssuranceVieException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * V�rifie que le simpleAssuranceVie ait un montantValeurRachat V�rifie que le simpleAssuranceVie ait un
     * numeroPolice V�rifie que le simpleAssuranceVie ait un nomCompagnie V�rifie que le simpleAssuranceVie ait une
     * dateEcheance
     * 
     * @param simpleAssuranceVie
     */
    private static void checkMandatory(SimpleAssuranceVie simpleAssuranceVie) {

        // V�rifie que le simpleAssuranceVie ait un montant mensuel
        if (JadeStringUtil.isEmpty(simpleAssuranceVie.getMontantValeurRachat())) {
            JadeThread.logError(simpleAssuranceVie.getClass().getName(),
                    "pegasus.simpleAssuranceVie.montantValeurRachat.mandatory");
        }

        // V�rifie que le simpleAssuranceVie ait un id_caisse_af
        if (JadeStringUtil.isEmpty(simpleAssuranceVie.getNumeroPolice())) {
            JadeThread.logError(simpleAssuranceVie.getClass().getName(),
                    "pegasus.simpleAssuranceVie.numeroPolice.mandatory");
        }
        // V�rifie que le simpleAssuranceVie ait un montant mensuel
        if (JadeStringUtil.isEmpty(simpleAssuranceVie.getNomCompagnie())) {
            JadeThread.logError(simpleAssuranceVie.getClass().getName(),
                    "pegasus.simpleAssuranceVie.nomCompagnie.mandatory");
        }

        // V�rifie que le simpleAssuranceVie ait un id_caisse_af
        // if (JadeStringUtil.isEmpty(simpleAssuranceVie.getDateEcheance())) {
        // JadeThread.logError(simpleAssuranceVie.getClass().getName(),
        // "pegasus.simpleAssuranceVie.dateEcheance.mandatory");
        // }
    }
}
