package ch.globaz.pegasus.businessimpl.checkers.habitat;

import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.SejourMoisPartielHomeException;
import ch.globaz.pegasus.business.models.habitat.SimpleSejourMoisPartielHome;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;

public class SimpleSejourMoisPartielHomeChecker extends PegasusAbstractChecker {

    public static void checkForCreate(SimpleSejourMoisPartielHome sejourMoisPartielHome)
            throws SejourMoisPartielHomeException, JadePersistenceException {
        SimpleSejourMoisPartielHomeChecker.checkMandatory(sejourMoisPartielHome);
    }

    /**
     * @param sejourMoisPartielHome
     */
    public static void checkForDelete(SimpleSejourMoisPartielHome sejourMoisPartielHome) {

    }

    /**
     * @param sejourMoisPartielHome
     * @throws JadePersistenceException
     * @throws SejourMoisPartielHomeException
     */
    public static void checkForUpdate(SimpleSejourMoisPartielHome sejourMoisPartielHome)
            throws SejourMoisPartielHomeException, JadePersistenceException {
        SimpleSejourMoisPartielHomeChecker.checkMandatory(sejourMoisPartielHome);
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * @param sejourMoisPartielHome
     * @throws LoyerException
     * @throws JadePersistenceException
     */
    private static void checkIntegrity(SimpleSejourMoisPartielHome sejourMoisPartielHome)
            throws SejourMoisPartielHomeException, JadePersistenceException {

    }

    /**
     * Verification des donnees obligatoires:
     * 
     * 
     * @param sejourMoisPartielHome
     */

    private static void checkMandatory(SimpleSejourMoisPartielHome sejourMoisPartielHome) {
        if (JadeStringUtil.isEmpty(sejourMoisPartielHome.getPrixJournalier())) {
            JadeThread.logError(sejourMoisPartielHome.getClass().getName(),
                    "pegasus.sejourMoisPartielHome.prixJournalier.mandatory");
        }
        if (JadeStringUtil.isEmpty(sejourMoisPartielHome.getNbJours())) {
            JadeThread.logError(sejourMoisPartielHome.getClass().getName(),
                    "pegasus.sejourMoisPartielHome.nbJours.mandatory");
        }
    }

}
