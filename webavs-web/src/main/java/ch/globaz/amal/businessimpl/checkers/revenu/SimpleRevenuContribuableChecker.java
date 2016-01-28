package ch.globaz.amal.businessimpl.checkers.revenu;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenu;
import ch.globaz.amal.business.models.revenu.SimpleRevenuContribuable;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author CBU
 * 
 */
public class SimpleRevenuContribuableChecker extends AmalAbstractChecker {

    /**
     * @param simpleRevenuContribuable
     * @throws RevenuException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForCreate(SimpleRevenuContribuable simpleRevenuContribuable) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        SimpleRevenuContribuableChecker.checkMandatory(simpleRevenuContribuable);
        if (!AmalAbstractChecker.threadOnError()) {
            SimpleRevenuContribuableChecker.checkForIntegrity(simpleRevenuContribuable);
        }
    }

    /**
     * @param dossier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     */
    public static void checkForDelete(SimpleRevenuContribuable simpleRevenuContribuableContribuable)
            throws RevenuException, JadePersistenceException {
    }

    /**
     * 
     * @param simpleRevenuContribuable
     * @throws RevenuException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    private static void checkForIntegrity(SimpleRevenuContribuable simpleRevenuContribuable) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        SimpleRevenu simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().read(
                simpleRevenuContribuable.getIdRevenu());
        if (simpleRevenu.isNew()) {
            JadeThread.logError(simpleRevenuContribuable.getClass().getName(),
                    "amal.revenu.idRevenuContribuable.integrity");
        }
    }

    /**
     * @param dossier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     */
    public static void checkForUpdate(SimpleRevenuContribuable simpleRevenuContribuable) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        SimpleRevenuContribuableChecker.checkMandatory(simpleRevenuContribuable);
        if (!AmalAbstractChecker.threadOnError()) {
            SimpleRevenuContribuableChecker.checkForIntegrity(simpleRevenuContribuable);
        }
    }

    /**
     * @param simpleRevenuContribuable
     */
    private static void checkMandatory(SimpleRevenuContribuable simpleRevenuContribuable) {
        if (JadeStringUtil.isBlankOrZero(simpleRevenuContribuable.getIdRevenu())) {
            JadeThread.logError(simpleRevenuContribuable.getClass().getName(),
                    "amal.revenu.idRevenuContribuable.mandatory");
        }
    }

}
