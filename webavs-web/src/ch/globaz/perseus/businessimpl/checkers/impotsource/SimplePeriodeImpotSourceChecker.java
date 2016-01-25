package ch.globaz.perseus.businessimpl.checkers.impotsource;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.impotsource.PeriodeImpotSourceException;
import ch.globaz.perseus.business.models.impotsource.SimplePeriodeImpotSource;
import ch.globaz.perseus.business.models.impotsource.SimplePeriodeImpotSourceSearchModel;
import ch.globaz.perseus.businessimpl.checkers.PerseusAbstractChecker;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * 
 * @author PCA
 * 
 */

public class SimplePeriodeImpotSourceChecker extends PerseusAbstractChecker {

    public static void checkForCreate(SimplePeriodeImpotSource simplePeriodeImpotSource)
            throws PeriodeImpotSourceException, JadePersistenceException {
        SimplePeriodeImpotSourceChecker.checkMandatory(simplePeriodeImpotSource);
        SimplePeriodeImpotSourceChecker.checkIntegrity(simplePeriodeImpotSource);
    }

    public static void checkForDelete(SimplePeriodeImpotSource simplePeriodeImpotSource)
            throws PeriodeImpotSourceException, JadePersistenceException {
        // TODO Auto-generated method stub

    }

    public static void checkForUpdate(SimplePeriodeImpotSource simplePeriodeImpotSource)
            throws PeriodeImpotSourceException, JadePersistenceException {
        SimplePeriodeImpotSourceChecker.checkMandatory(simplePeriodeImpotSource);
        SimplePeriodeImpotSourceChecker.checkIntegrity(simplePeriodeImpotSource);
    }

    private static void checkIntegrity(SimplePeriodeImpotSource simplePeriodeImpotSource)
            throws PeriodeImpotSourceException, JadePersistenceException, JadeNoBusinessLogSessionError {

        if (JadeDateUtil.isDateMonthYearAfter(simplePeriodeImpotSource.getDateDebut(),
                simplePeriodeImpotSource.getDateFin())) {
            JadeThread.logError(simplePeriodeImpotSource.getClass().getName(),
                    "perseus.periodeImpotSource.dateDebutPlusPetite.integrity");
        }

        try {
            if (!PerseusAbstractChecker.threadOnError()) {
                // SimplePeriodeImpotSourceChecker.checkPeriodeSuperieur(simplePeriodeImpotSource);
                SimplePeriodeImpotSourceChecker.checkSuperPositionPeriode(simplePeriodeImpotSource);
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PeriodeImpotSourceException("Unable to check impotSourcePeriode", e);
        }
    }

    private static void checkMandatory(SimplePeriodeImpotSource simplePeriodeImpotSource) {

        // Test le champ date de début
        if (JadeStringUtil.isEmpty(simplePeriodeImpotSource.getDateDebut())) {
            JadeThread.logError(simplePeriodeImpotSource.getClass().getName(),
                    "perseus.periodeImpotSource.datedebut.mandatory");
        }

        // Test le champ date de fin
        if (JadeStringUtil.isEmpty(simplePeriodeImpotSource.getDateFin())) {
            JadeThread.logError(simplePeriodeImpotSource.getClass().getName(),
                    "perseus.periodeImpotSource.datefin.mandatory");
        }

    }

    private static void checkSuperPositionPeriode(SimplePeriodeImpotSource simplePeriodeImpotSource)
            throws PeriodeImpotSourceException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
        SimplePeriodeImpotSourceSearchModel perioderSearch = SimplePeriodeImpotSourceChecker
                .convertToSearchModel(simplePeriodeImpotSource);
        perioderSearch.setWhereKey("checkSuperpositionDates");
        if (PerseusImplServiceLocator.getSimplePeriodeImpotSourceService().count(perioderSearch) > 0) {
            JadeThread.logError(simplePeriodeImpotSource.getClass().getName(),
                    "perseus.periodeImpotSource.superpositionDates.integrity");
        }
    }

    private static SimplePeriodeImpotSourceSearchModel convertToSearchModel(
            SimplePeriodeImpotSource simplePeriodeImpotSource) {
        SimplePeriodeImpotSourceSearchModel perdiodeSearch = new SimplePeriodeImpotSourceSearchModel();
        perdiodeSearch.setForDateDebutCheckPeriode(simplePeriodeImpotSource.getDateDebut());
        perdiodeSearch.setForDateFinCheckPeriode(simplePeriodeImpotSource.getDateFin());
        perdiodeSearch.setForIdPeriodeImpotSource(simplePeriodeImpotSource.getIdPeriode());
        return perdiodeSearch;
    }
}
