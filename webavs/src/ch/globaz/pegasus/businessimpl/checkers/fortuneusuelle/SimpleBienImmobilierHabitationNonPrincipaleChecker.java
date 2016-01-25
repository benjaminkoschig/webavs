package ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierHabitationNonPrincipale;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleBienImmobilierHabitationNonPrincipaleChecker extends PegasusAbstractChecker {
    /**
     * @param simpleBienImmobilierHabitationNonPrincipale
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws BienImmobilierHabitationNonPrincipaleException
     */
    public static void checkForCreate(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        SimpleBienImmobilierHabitationNonPrincipaleChecker.checkMandatory(simpleBienImmobilierHabitationNonPrincipale);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleBienImmobilierHabitationNonPrincipaleChecker
                    .checkIntegrity(simpleBienImmobilierHabitationNonPrincipale);
        }
    }

    /**
     * @param simpleBienImmobilierHabitationNonPrincipale
     */
    public static void checkForDelete(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale) {
    }

    /**
     * @param simpleBienImmobilierHabitationNonPrincipale
     */
    public static void checkForUpdate(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale) {
        SimpleBienImmobilierHabitationNonPrincipaleChecker.checkMandatory(simpleBienImmobilierHabitationNonPrincipale);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleBienImmobilierHabitationNonPrincipale
     * @throws BienImmobilierHabitationNonPrincipaleException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * V�rifie que le simpleBienImmobilierHabitationNonPrincipale ait un typePropriete V�rifie que le
     * simpleBienImmobilierHabitationNonPrincipale ait un typeBien V�rifie que le
     * simpleBienImmobilierHabitationNonPrincipale ait un noFeuillet V�rifie que le
     * simpleBienImmobilierHabitationNonPrincipale ait un idCommuneDuBien V�rifie que le
     * simpleBienImmobilierHabitationNonPrincipale ait un montantValeurLocative
     * 
     * @param simpleBienImmobilierHabitationNonPrincipale
     */
    private static void checkMandatory(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale) {

        // V�rifie que le simpleBienImmobilierHabitationNonPrincipale ait un
        // typePropriete
        if (JadeStringUtil.isEmpty(simpleBienImmobilierHabitationNonPrincipale.getCsTypePropriete())) {
            JadeThread.logError(simpleBienImmobilierHabitationNonPrincipale.getClass().getName(),
                    "pegasus.simpleBienImmobilierHabitationNonPrincipale.csTypePropriete.mandatory");
        }

        // V�rifie que le simpleBienImmobilierHabitationNonPrincipale ait un
        // typeBien
        if (JadeStringUtil.isEmpty(simpleBienImmobilierHabitationNonPrincipale.getCsTypeBien())) {
            JadeThread.logError(simpleBienImmobilierHabitationNonPrincipale.getClass().getName(),
                    "pegasus.simpleBienImmobilierHabitationNonPrincipale.csTypeBien.mandatory");
        }

        // V�rifie que le simpleBienImmobilierHabitationNonPrincipale ait un
        // noFeuillet
        // if (JadeStringUtil.isEmpty(simpleBienImmobilierHabitationNonPrincipale.getNoFeuillet())) {
        // JadeThread.logError(simpleBienImmobilierHabitationNonPrincipale.getClass().getName(),
        // "pegasus.simpleBienImmobilierHabitationNonPrincipale.noFeuillet.mandatory");
        // }

        // V�rifie que le simpleBienImmobilierHabitationNonPrincipale ait un
        // IdCommuneDuBien (seulement si pays = suisse (idPays=100))

        if (PRTiersHelper.ID_PAYS_SUISSE.equalsIgnoreCase(simpleBienImmobilierHabitationNonPrincipale.getIdPays())) {
            if (JadeStringUtil.isEmpty(simpleBienImmobilierHabitationNonPrincipale.getIdCommuneDuBien())) {
                JadeThread.logError(simpleBienImmobilierHabitationNonPrincipale.getClass().getName(),
                        "pegasus.simpleBienImmobilierHabitationNonPrincipale.idCommuneDuBien.mandatory");
            }
        }

        // V�rifie que le simpleBienImmobilierHabitationNonPrincipale ait un
        // id_caisse_af
        if (JadeStringUtil.isEmpty(simpleBienImmobilierHabitationNonPrincipale.getMontantValeurLocative())) {
            JadeThread.logError(simpleBienImmobilierHabitationNonPrincipale.getClass().getName(),
                    "pegasus.simpleBienImmobilierHabitationNonPrincipale.montantValeurLocative.mandatory");
        }

        // La part de propriete est obligatoire
        if (JadeStringUtil.isBlankOrZero(simpleBienImmobilierHabitationNonPrincipale.getPartProprieteNumerateur())
                || JadeStringUtil.isBlankOrZero(simpleBienImmobilierHabitationNonPrincipale
                        .getPartProprieteDenominateur())) {
            JadeThread.logError(simpleBienImmobilierHabitationNonPrincipale.getClass().getName(),
                    "pegasus.simpleBienImmobilierHabitationNonPrincipale.partPropriete.mandatory");
        }

    }
}
