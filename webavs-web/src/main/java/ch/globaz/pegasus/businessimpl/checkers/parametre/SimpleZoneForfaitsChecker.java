package ch.globaz.pegasus.businessimpl.checkers.parametre;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.ForfaitsPrimesAssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaitsSearch;
import ch.globaz.pegasus.business.models.parametre.ZoneLocaliteSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * @author DMA
 * @date 12 nov. 2010
 */
public class SimpleZoneForfaitsChecker {
    public static void checkForCreate(SimpleZoneForfaits simpleZoneForfaits)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException, JadeNoBusinessLogSessionError,
            JadeApplicationServiceNotAvailableException {
        SimpleZoneForfaitsChecker.checkMandatory(simpleZoneForfaits);
        SimpleZoneForfaitsChecker.checkIntegrity(simpleZoneForfaits);
    }

    public static void checkForDelete(SimpleZoneForfaits simpleZoneForfait)
            throws ForfaitsPrimesAssuranceMaladieException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
        if (SimpleZoneForfaitsChecker.isIdUsedInOthersTable(simpleZoneForfait)) {

            JadeThread.logError(simpleZoneForfait.getClass().getName(),
                    "pegasus.simplezoneforfait.foreignkey.integrity");

        }
    }

    /**
     * @param simpleZoneForfait
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ForfaitsPrimesAssuranceMaladieException
     */
    public static void checkForUpdate(SimpleZoneForfaits simpleZoneForfaits)
            throws ForfaitsPrimesAssuranceMaladieException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        SimpleZoneForfaitsChecker.checkMandatory(simpleZoneForfaits);
        SimpleZoneForfaitsChecker.checkIntegrity(simpleZoneForfaits);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param simpleZoneForfait
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws exception
     */
    private static void checkIntegrity(SimpleZoneForfaits simpleZoneForfait)
            throws ForfaitsPrimesAssuranceMaladieException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        if (SimpleZoneForfaitsChecker.isDistinct(simpleZoneForfait)) {
            JadeThread.logError(simpleZoneForfait.getClass().getName(), "pegasus.simplezoneforfait.distinct.integrity");

        }
    }

    /**
     * Verification des donnees obligatoires:
     * 
     * @param simpleZoneForfait
     */
    private static void checkMandatory(SimpleZoneForfaits simpleZoneForfait) {
        if (JadeStringUtil.isEmpty(simpleZoneForfait.getCsCanton())) {
            JadeThread.logError(simpleZoneForfait.getClass().getName(), "pegasus.simplezoneforfait.cscanton.mandatory");

        }
        if (JadeStringUtil.isEmpty(simpleZoneForfait.getDesignation())) {
            JadeThread.logError(simpleZoneForfait.getClass().getName(),
                    "pegasus.simplezoneforfait.designation.mandatory");

        }

    }

    private static boolean isDistinct(SimpleZoneForfaits simpleZoneForfait)
            throws ForfaitsPrimesAssuranceMaladieException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        int count = 0;
        SimpleZoneForfaitsSearch search = new SimpleZoneForfaitsSearch();
        search.setForCsCanton(simpleZoneForfait.getCsCanton());
        search.setForDesignation(simpleZoneForfait.getDesignation());
        search.setForType(simpleZoneForfait.getType());
        count = PegasusImplServiceLocator.getSimpleZoneForfaitsService().count(search);
        return count > 0;
    }

    /**
     * @param simpleZoneForfait
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ForfaitsPrimesAssuranceMaladieException
     */
    public static boolean isIdUsedInOthersTable(SimpleZoneForfaits simpleZoneForfait)
            throws ForfaitsPrimesAssuranceMaladieException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        if (SimpleZoneForfaitsChecker.isIdUsedInTableForfaitPrime(simpleZoneForfait)) {
            return true;
        }
        if (SimpleZoneForfaitsChecker.isIdUsedInTableLienZoneLocalite(simpleZoneForfait)) {
            return true;
        }

        return false;
    }

    public static boolean isIdUsedInOthersTableWithOutException(SimpleZoneForfaits simpleZoneForfait) {
        boolean isUsed = true;
        try {
            isUsed = SimpleZoneForfaitsChecker.isIdUsedInOthersTable(simpleZoneForfait);

        } catch (Exception e) {
            isUsed = true;
        }
        return isUsed;
    }

    private static boolean isIdUsedInTableForfaitPrime(SimpleZoneForfaits simpleZoneForfait)
            throws ForfaitsPrimesAssuranceMaladieException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        boolean isUsed = true;
        int count = 0;

        ForfaitsPrimesAssuranceMaladieSearch forfaitsPrimesAssuranceMaladieSearch = new ForfaitsPrimesAssuranceMaladieSearch();
        forfaitsPrimesAssuranceMaladieSearch.setForIdZoneForfaits(simpleZoneForfait.getId());
        count = PegasusServiceLocator.getParametreServicesLocator().getForfaitsPrimesAssuranceMaladieService()
                .count(forfaitsPrimesAssuranceMaladieSearch);
        if (count == 0) {
            isUsed = false;
        }
        return isUsed;
    }

    private static boolean isIdUsedInTableLienZoneLocalite(SimpleZoneForfaits simpleZoneForfait)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            ForfaitsPrimesAssuranceMaladieException {
        boolean isUsed = true;
        int count = 0;

        ZoneLocaliteSearch search = new ZoneLocaliteSearch();
        search.setForIdZoneForfait(simpleZoneForfait.getIdZoneForfait());
        count = PegasusServiceLocator.getParametreServicesLocator().getZoneLocaliteService().count(search);
        if (count == 0) {
            isUsed = false;
        }
        return isUsed;
    }
}
