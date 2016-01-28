/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.revenu;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenu;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSourcier;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;

/**
 * @author CBU
 * 
 */
public class SimpleRevenuSourcierChecker extends AmalAbstractChecker {

    /**
     * @param simpleRevenuSourcier
     * @throws RevenuException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForCreate(SimpleRevenuSourcier simpleRevenuSourcier) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        SimpleRevenuSourcierChecker.checkMandatory(simpleRevenuSourcier);
        if (!AmalAbstractChecker.threadOnError()) {
            SimpleRevenuSourcierChecker.checkForIntegrity(simpleRevenuSourcier);
        }
    }

    /**
     * @param simpleRevenuSourcier
     * @throws RevenuException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForDelete(SimpleRevenuSourcier simpleRevenuSourcier) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
    }

    /**
     * @param simpleRevenuSourcier
     * @throws RevenuException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private static void checkForIntegrity(SimpleRevenuSourcier simpleRevenuSourcier) throws RevenuException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleRevenu simpleRevenu = AmalImplServiceLocator.getSimpleRevenuService().read(
                simpleRevenuSourcier.getIdRevenu());
        if (simpleRevenu.isNew()) {
            JadeThread.logError(simpleRevenuSourcier.getClass().getName(), "amal.revenu.idRevenuSourcier.integrity");
        }
    }

    /**
     * @param simpleRevenuSourcier
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     */
    public static void checkForUpdate(SimpleRevenuSourcier simpleRevenuSourcier) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        SimpleRevenuSourcierChecker.checkMandatory(simpleRevenuSourcier);
        if (!AmalAbstractChecker.threadOnError()) {
            SimpleRevenuSourcierChecker.checkForIntegrity(simpleRevenuSourcier);
        }
    }

    /**
     * @param simpleRevenuSourcier
     */
    private static void checkMandatory(SimpleRevenuSourcier simpleRevenuSourcier) {
        if (JadeStringUtil.isBlankOrZero(simpleRevenuSourcier.getIdRevenu())) {
            JadeThread.logError(simpleRevenuSourcier.getClass().getName(), "amal.revenu.idRevenuSourcier.mandatory");
        }

        if ((JadeStringUtil.isBlankOrZero(simpleRevenuSourcier.getNombreMois()) && !JadeStringUtil
                .isBlankOrZero(simpleRevenuSourcier.getRevenuEpouseMensuel()))
                || (JadeStringUtil.isBlankOrZero(simpleRevenuSourcier.getNombreMois()) && !JadeStringUtil
                        .isBlankOrZero(simpleRevenuSourcier.getRevenuEpouxMensuel()))) {
            JadeThread.logError(simpleRevenuSourcier.getClass().getName(), "amal.revenu.moisRevenu.mandatory");
        }
    }
}
