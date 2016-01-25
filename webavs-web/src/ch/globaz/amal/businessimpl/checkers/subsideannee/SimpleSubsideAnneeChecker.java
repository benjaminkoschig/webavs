/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.subsideannee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.amal.business.exceptions.models.subsideannee.SubsideAnneeException;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;

/**
 * @author CBU
 * 
 */
public class SimpleSubsideAnneeChecker extends AmalAbstractChecker {

    public static void checkForCreate(SimpleSubsideAnnee simpleSubsideAnnee) throws SubsideAnneeException,
            JadePersistenceException {
        SimpleSubsideAnneeChecker.checkMandatory(simpleSubsideAnnee);
        SimpleSubsideAnneeChecker.checkIntegrity(simpleSubsideAnnee);
    }

    public static void checkForDelete(SimpleSubsideAnnee simpleSubsideAnnee) {

    }

    public static void checkForUpdate(SimpleSubsideAnnee simpleSubsideAnnee) {
        SimpleSubsideAnneeChecker.checkMandatory(simpleSubsideAnnee);
        SimpleSubsideAnneeChecker.checkIntegrity(simpleSubsideAnnee);
    }

    private static void checkIntegrity(SimpleSubsideAnnee simpleSubsideAnnee) {

    }

    private static void checkMandatory(SimpleSubsideAnnee simpleSubsideAnnee) {
        if (JadeStringUtil.isBlank(simpleSubsideAnnee.getAnneeSubside())) {
            JadeThread.logError(simpleSubsideAnnee.getClass().getName(), "amal.subsideAnnee.annee.mandatory");
        }
    }

}
