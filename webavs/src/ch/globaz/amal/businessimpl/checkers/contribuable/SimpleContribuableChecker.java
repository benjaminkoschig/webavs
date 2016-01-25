/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.contribuable;

import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuableSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;

/**
 * @author CBU
 * 
 */
public class SimpleContribuableChecker extends AmalAbstractChecker {

    public static void checkForCreate(SimpleContribuable simpleContribuable) {
        SimpleContribuableChecker.checkMandatory(simpleContribuable);
        if (!AmalAbstractChecker.threadOnError()) {
            SimpleContribuableChecker.checkIntegrity(simpleContribuable);
        }
    }

    public static void checkForDelete(SimpleContribuable simpleContribuable) throws ContribuableException,
            JadePersistenceException {
        try {
            if (SimpleContribuableChecker.hasFamille(simpleContribuable)) {
                JadeThread.logError(simpleContribuable.getClass().getName(), "amal.contribuable.famille.integrity");
            }
        } catch (FamilleException e) {
            throw new ContribuableException("Unable to check familleContribuable for delete contribuable", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ContribuableException("Unable to get contribuable service", e);
        } catch (JadeNoBusinessLogSessionError e) {
            throw new ContribuableException("Unable to get contribuable service", e);
        }

    }

    public static void checkForUpdate(SimpleContribuable simpleContribuable) {
        SimpleContribuableChecker.checkMandatory(simpleContribuable);
        if (!AmalAbstractChecker.threadOnError()) {
            SimpleContribuableChecker.checkIntegrity(simpleContribuable);
        }
    }

    private static void checkIntegrity(SimpleContribuable simpleContribuable) {
    }

    private static void checkMandatory(SimpleContribuable simpleContribuable) {
    }

    private static boolean hasFamille(SimpleContribuable simpleContribuable) throws FamilleException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        FamilleContribuableSearch familleContribuableSearch = new FamilleContribuableSearch();
        familleContribuableSearch.setForIdContribuable(simpleContribuable.getIdContribuable());

        return AmalServiceLocator.getFamilleContribuableService().count(familleContribuableSearch) > 0;
    }

}
