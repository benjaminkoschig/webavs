package ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleBienImmobilierServantHabitationPrincipaleChecker extends PegasusAbstractChecker {
    /**
     * @param simpleBienImmobilierServantHabitationPrincipale
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws BienImmobilierServantHabitationPrincipaleException
     */
    public static void checkForCreate(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        SimpleBienImmobilierServantHabitationPrincipaleChecker
                .checkMandatory(simpleBienImmobilierServantHabitationPrincipale);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleBienImmobilierServantHabitationPrincipaleChecker
                    .checkIntegrity(simpleBienImmobilierServantHabitationPrincipale);
        }
    }

    /**
     * @param simpleBienImmobilierServantHabitationPrincipale
     */
    public static void checkForDelete(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale) {
    }

    /**
     * @param simpleBienImmobilierServantHabitationPrincipale
     */
    public static void checkForUpdate(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale) {
        SimpleBienImmobilierServantHabitationPrincipaleChecker
                .checkMandatory(simpleBienImmobilierServantHabitationPrincipale);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleBienImmobilierServantHabitationPrincipale
     * @throws BienImmobilierServantHabitationPrincipaleException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simpleBienImmobilierServantHabitationPrincipale ait un csTypePropriete Vérifie que le
     * simpleBienImmobilierServantHabitationPrincipale ait un csTypeBien Vérifie que le
     * simpleBienImmobilierServantHabitationPrincipale ait un idCommuneDuBien Vérifie que le
     * simpleBienImmobilierServantHabitationPrincipale ait un montantValeurLocative Vérifie que le
     * simpleBienImmobilierServantHabitationPrincipale ait un montantValeurFiscale
     * 
     * @param simpleBienImmobilierServantHabitationPrincipale
     */
    private static void checkMandatory(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale) {

        // Vérifie que le simpleBienImmobilierServantHabitationPrincipale ait un
        // csTypePropriete
        if (JadeStringUtil.isEmpty(simpleBienImmobilierServantHabitationPrincipale.getCsTypePropriete())) {
            JadeThread.logError(simpleBienImmobilierServantHabitationPrincipale.getClass().getName(),
                    "pegasus.simpleBienImmobilierServantHabitationPrincipale.csTypePropriete.mandatory");
        }

        // Vérifie que le simpleBienImmobilierServantHabitationPrincipale ait un
        // csTypeBien
        if (JadeStringUtil.isEmpty(simpleBienImmobilierServantHabitationPrincipale.getCsTypeBien())) {
            JadeThread.logError(simpleBienImmobilierServantHabitationPrincipale.getClass().getName(),
                    "pegasus.simpleBienImmobilierServantHabitationPrincipale.csTypeBien.mandatory");
        }

        // Vérifie que le simpleBienImmobilierServantHabitationPrincipale ait un
        // idCommuneDuBien
        if (JadeStringUtil.isEmpty(simpleBienImmobilierServantHabitationPrincipale.getIdCommuneDuBien())) {
            JadeThread.logError(simpleBienImmobilierServantHabitationPrincipale.getClass().getName(),
                    "pegasus.simpleBienImmobilierServantHabitationPrincipale.idCommuneDuBien.mandatory");
        }

        // Vérifie que le simpleBienImmobilierServantHabitationPrincipale ait un
        // montantValeurLocative
        if (JadeStringUtil.isEmpty(simpleBienImmobilierServantHabitationPrincipale.getMontantValeurLocative())) {
            JadeThread.logError(simpleBienImmobilierServantHabitationPrincipale.getClass().getName(),
                    "pegasus.simpleBienImmobilierServantHabitationPrincipale.montantValeurLocative.mandatory");
        }

        // Vérifie que le simpleBienImmobilierServantHabitationPrincipale ait un
        // montantValeurFiscale
        if (JadeStringUtil.isEmpty(simpleBienImmobilierServantHabitationPrincipale.getMontantValeurFiscale())) {
            JadeThread.logError(simpleBienImmobilierServantHabitationPrincipale.getClass().getName(),
                    "pegasus.simpleBienImmobilierServantHabitationPrincipale.montantValeurFiscale.mandatory");
        }

        // La part de propriete est obligatoire
        if (JadeStringUtil.isBlankOrZero(simpleBienImmobilierServantHabitationPrincipale.getPartProprieteNumerateur())
                || JadeStringUtil.isBlankOrZero(simpleBienImmobilierServantHabitationPrincipale
                        .getPartProprieteDenominateur())) {
            JadeThread.logError(simpleBienImmobilierServantHabitationPrincipale.getClass().getName(),
                    "pegasus.simpleBienImmobilierServantHabitationPrincipale.partPropriete.mandatory");
        }

        // Nombre de personnes
        if (JadeStringUtil.isBlankOrZero(simpleBienImmobilierServantHabitationPrincipale.getNombrePersonnes())) {
            JadeThread.logError(simpleBienImmobilierServantHabitationPrincipale.getClass().getName(),
                    "pegasus.simpleBienImmobilierServantHabitationPrincipale.nombrepersonne.mandatory");
        }

    }
}
