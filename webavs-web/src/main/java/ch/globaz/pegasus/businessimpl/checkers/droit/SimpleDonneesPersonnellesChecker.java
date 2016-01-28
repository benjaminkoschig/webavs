package ch.globaz.pegasus.businessimpl.checkers.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneesPersonnellesException;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnelles;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;

public abstract class SimpleDonneesPersonnellesChecker extends PegasusAbstractChecker {
    /**
     * @param donneesPersonnelles
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws DonneesPersonnellesException
     */
    public static void checkForCreate(SimpleDonneesPersonnelles donneesPersonnelles)
            throws DonneesPersonnellesException, JadePersistenceException, JadeNoBusinessLogSessionError {
        // pour la creation pas de mandatory...
        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDonneesPersonnellesChecker.checkIntegrity(donneesPersonnelles);
        }
    }

    /**
     * @param donneesPersonnelles
     */
    public static void checkForDelete(SimpleDonneesPersonnelles donneesPersonnelles) {
    }

    /**
     * @param donneesPersonnelles
     */
    public static void checkForUpdate(SimpleDonneesPersonnelles donneesPersonnelles)
            throws DonneesPersonnellesException, JadePersistenceException, JadeNoBusinessLogSessionError {
        SimpleDonneesPersonnellesChecker.checkMandatory(donneesPersonnelles);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleDonneesPersonnellesChecker.checkIntegrity(donneesPersonnelles);
        }
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param donneesPersonnelles
     * @throws DonneesPersonnellesException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleDonneesPersonnelles donneesPersonnelles)
            throws DonneesPersonnellesException, JadePersistenceException, JadeNoBusinessLogSessionError {

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * @param donneesPersonnelles
     */
    private static void checkMandatory(SimpleDonneesPersonnelles donneesPersonnelles) {

        // Le motif doit etre spécifié
        if (JadeStringUtil.isEmpty(donneesPersonnelles.getIdDernierDomicileLegale())) {
            JadeThread.logError(donneesPersonnelles.getClass().getName(),
                    "pegasus.donneepersonnelle.dernierdomicilelegal.mandatory");
        }
    }
}
