package ch.globaz.pegasus.businessimpl.checkers.home;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat;
import ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtatSearch;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public abstract class SimplePeriodeServiceEtatChecker extends PegasusAbstractChecker {
    /**
     * @param simplePeriodeServiceEtat
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws PeriodeServiceEtatException
     */
    public static void checkForCreate(SimplePeriodeServiceEtat simplePeriodeServiceEtat)
            throws PeriodeServiceEtatException, JadePersistenceException, JadeNoBusinessLogSessionError {

        SimplePeriodeServiceEtatChecker.checkMandatory(simplePeriodeServiceEtat);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimplePeriodeServiceEtatChecker.checkIntegrity(simplePeriodeServiceEtat, false);
        }
    }

    /**
     * 
     * @param simplePeriodeServiceEtat
     */
    public static void checkForDelete(SimplePeriodeServiceEtat simplePeriodeServiceEtat) {
    }

    /**
     * @param simplePeriodeServiceEtat
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws PeriodeServiceEtatException
     */
    public static void checkForUpdate(SimplePeriodeServiceEtat simplePeriodeServiceEtat)
            throws PeriodeServiceEtatException, JadePersistenceException, JadeNoBusinessLogSessionError {
        SimplePeriodeServiceEtatChecker.checkMandatory(simplePeriodeServiceEtat);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimplePeriodeServiceEtatChecker.checkIntegrity(simplePeriodeServiceEtat, true);
        }
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * <li>pour un home, il ne doit pas y avoir de superposition de periode de service de l'etat</li>
     * 
     * @param simplePeriodeServiceEtat
     * @throws TypeChambreException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimplePeriodeServiceEtat simplePeriodeServiceEtat, boolean isUpdating)
            throws PeriodeServiceEtatException, JadePersistenceException, JadeNoBusinessLogSessionError {

        // pour un home, il ne doit pas y avoir de superposition de periode de
        // service de l'etat

        SimplePeriodeServiceEtatSearch pchSearch = new SimplePeriodeServiceEtatSearch();
        pchSearch.setForIdHome(simplePeriodeServiceEtat.getIdHome());
        pchSearch.setForIdPeriodeServiceEtat(simplePeriodeServiceEtat.getIdSimplePeriodeServiceEtat());
        pchSearch.setForDateDebutCheckPeriode(simplePeriodeServiceEtat.getDateDebut());
        pchSearch.setForDateFinCheckPeriode(simplePeriodeServiceEtat.getDateFin());
        pchSearch.setWhereKey("checkSuperpositionPeriode");

        try {
            if (!PegasusAbstractChecker.threadOnError()) {
                int expectedCount = 0;
                if (isUpdating) {
                    expectedCount = 1;
                }
                if (PegasusImplServiceLocator.getSimplePeriodeServiceEtatService().count(pchSearch) > expectedCount) {
                    JadeThread.logError(simplePeriodeServiceEtat.getClass().getName(),
                            "pegasus.periodeServiceEtat.superpositionPeriodePourHome.integrity");
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PeriodeServiceEtatException("Unable to check periodeServiceEtat", e);
        }

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * <li>une periode de service de l'etat doit avoir une reference sur un home</li> <li>la date de debut d'une periode
     * de service de l'etat est obligatoire</li>
     * 
     * @param simplePeriodeServiceEtat
     */
    private static void checkMandatory(SimplePeriodeServiceEtat simplePeriodeServiceEtat) {

        // une periode de service de l'etat doit avoir une reference sur un home
        if (JadeStringUtil.isIntegerEmpty(simplePeriodeServiceEtat.getIdHome())) {
            JadeThread.logError(simplePeriodeServiceEtat.getClass().getName(),
                    "pegasus.periodeServiceEtat.idHome.mandatory");
        }
        // la date de debut d'une periode de service de l'etat est obligatoire
        if (JadeStringUtil.isEmpty(simplePeriodeServiceEtat.getDateDebut())) {
            JadeThread.logError(simplePeriodeServiceEtat.getClass().getName(),
                    "pegasus.periodeServiceEtat.dateDebut.mandatory");
        } else {

            // la date de debut d'une periode de service de l'etat est
            // obligatoire
            if (!JadeDateUtil.isGlobazDate("01." + simplePeriodeServiceEtat.getDateDebut())) {
                JadeThread.logError(simplePeriodeServiceEtat.getClass().getName(),
                        "pegasus.periodeServiceEtat.dateDebut.invalid");
            }
        }

        // la date de debut d'une periode de service de l'etat est obligatoire
        if (!JadeStringUtil.isEmpty(simplePeriodeServiceEtat.getDateFin())
                && !JadeDateUtil.isGlobazDate("01." + simplePeriodeServiceEtat.getDateFin())) {
            JadeThread.logError(simplePeriodeServiceEtat.getClass().getName(),
                    "pegasus.periodeServiceEtat.dateFin.invalid");
        }
    }
}
