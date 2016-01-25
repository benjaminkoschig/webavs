package ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierNonHabitableException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierNonHabitable;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public class SimpleBienImmobilierNonHabitableChecker extends PegasusAbstractChecker {
    /**
     * @param simpleBienImmobilierNonHabitable
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws BienImmobilierNonHabitableException
     */
    public static void checkForCreate(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable)
            throws BienImmobilierNonHabitableException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleBienImmobilierNonHabitableChecker.checkMandatory(simpleBienImmobilierNonHabitable);
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleBienImmobilierNonHabitableChecker.checkIntegrity(simpleBienImmobilierNonHabitable);
        }
    }

    /**
     * @param simpleBienImmobilierNonHabitable
     */
    public static void checkForDelete(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable) {
    }

    /**
     * @param simpleBienImmobilierNonHabitable
     */
    public static void checkForUpdate(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable) {
        SimpleBienImmobilierNonHabitableChecker.checkMandatory(simpleBienImmobilierNonHabitable);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleBienImmobilierNonHabitable
     * @throws BienImmobilierNonHabitableException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable)
            throws BienImmobilierNonHabitableException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * Vérifie que le simpleBienImmobilierNonHabitable ait un csTypePropriete Vérifie que le
     * simpleBienImmobilierNonHabitable ait un csTypeBien Vérifie que le simpleBienImmobilierNonHabitable ait un
     * valeurVenale Vérifie que le simpleBienImmobilierNonHabitable ait un noFeuillet Vérifie que le
     * simpleBienImmobilierNonHabitable ait un idCommuneDuBien Vérifie que le simpleBienImmobilierNonHabitable ait un
     * montantRendement
     * 
     * @param simpleBienImmobilierNonHabitable
     */
    private static void checkMandatory(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable) {

        // Vérifie que le simpleBienImmobilierNonHabitable ait un
        // csTypePropriete
        if (JadeStringUtil.isEmpty(simpleBienImmobilierNonHabitable.getCsTypePropriete())) {
            JadeThread.logError(simpleBienImmobilierNonHabitable.getClass().getName(),
                    "pegasus.simpleBienImmobilierNonHabitable.csTypePropriete.mandatory");
        }

        // Vérifie que le simpleBienImmobilierNonHabitable ait un csTypeBien
        if (JadeStringUtil.isEmpty(simpleBienImmobilierNonHabitable.getCsTypeBien())) {
            JadeThread.logError(simpleBienImmobilierNonHabitable.getClass().getName(),
                    "pegasus.simpleBienImmobilierNonHabitable.csTypeBien.mandatory");
        }

        // Vérifie que le simpleBienImmobilierNonHabitable ait un valeurVenale
        if (JadeStringUtil.isEmpty(simpleBienImmobilierNonHabitable.getValeurVenale())) {
            JadeThread.logError(simpleBienImmobilierNonHabitable.getClass().getName(),
                    "pegasus.simpleBienImmobilierNonHabitable.valeurVenale.mandatory");
        }

        // Vérifie que le simpleBienImmobilierNonHabitable ait un noFeuillet
        // if (JadeStringUtil.isEmpty(simpleBienImmobilierNonHabitable.getNoFeuillet())) {
        // JadeThread.logError(simpleBienImmobilierNonHabitable.getClass().getName(),
        // "pegasus.simpleBienImmobilierNonHabitable.noFeuillet.mandatory");
        // }

        // Vérifie que le simpleBienImmobilierNonHabitable ait un
        // IdCommuneDuBien (seulement si pays = suisse (idPays=100))
        if (PRTiersHelper.ID_PAYS_SUISSE.equalsIgnoreCase(simpleBienImmobilierNonHabitable.getIdPays())) {
            if (JadeStringUtil.isEmpty(simpleBienImmobilierNonHabitable.getIdCommuneDuBien())) {
                JadeThread.logError(simpleBienImmobilierNonHabitable.getClass().getName(),
                        "pegasus.simpleBienImmobilierNonHabitable.idCommuneDuBien.mandatory");
            }
        }

        // Vérifie que le simpleBienImmobilierNonHabitable ait un
        // montantRendement
        if (JadeStringUtil.isEmpty(simpleBienImmobilierNonHabitable.getMontantRendement())) {
            JadeThread.logError(simpleBienImmobilierNonHabitable.getClass().getName(),
                    "pegasus.simpleBienImmobilierNonHabitable.montantRendement.mandatory");
        }

        // La part de propriete est obligatoire
        if (JadeStringUtil.isBlankOrZero(simpleBienImmobilierNonHabitable.getPartProprieteNumerateur())
                || JadeStringUtil.isBlankOrZero(simpleBienImmobilierNonHabitable.getPartProprieteDenominateur())) {
            JadeThread.logError(simpleBienImmobilierNonHabitable.getClass().getName(),
                    "pegasus.simpleBienImmobilierNonHabitable.partPropriete.mandatory");
        }
    }
}
