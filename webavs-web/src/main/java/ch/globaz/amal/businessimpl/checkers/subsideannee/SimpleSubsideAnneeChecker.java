/**
 *
 */
package ch.globaz.amal.businessimpl.checkers.subsideannee;

import ch.globaz.amal.business.exceptions.models.subsideannee.SubsideAnneeException;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.properties.JadePropertiesService;

/**
 * @author CBU
 *
 */
public class SimpleSubsideAnneeChecker extends AmalAbstractChecker {

    public static void checkForCreate(SimpleSubsideAnnee simpleSubsideAnnee)
            throws SubsideAnneeException, JadePersistenceException {
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

    public static boolean checkIsSubsidePCFKind(SimpleDetailFamille subside) {
        return checkIsSubsidePCFKind(subside.getDebutDroit());
    }

    public static boolean checkIsSubsidePCFKind(String dateSubside) {
        String suppPCFamilleDateDebut = JadePropertiesService.getInstance()
                .getProperty("amal.subsidesPartiels.startDate");
        boolean isSubsidePCFamille = false;

        int monthSubside = 1;
        int yearSubside;
        // date subside avec mois
        if (dateSubside.length() > 4) {
            monthSubside = Integer.parseInt(dateSubside.substring(0, 2));
            yearSubside = Integer.parseInt(dateSubside.substring(3, 7));
        } else {
            yearSubside = Integer.parseInt(dateSubside);
        }
        if (suppPCFamilleDateDebut != null) {
            int monthDebut = Integer.parseInt(suppPCFamilleDateDebut.substring(0, 2));
            int yearDebut = Integer.parseInt(suppPCFamilleDateDebut.substring(3, 7));

            if (yearSubside > yearDebut || (yearSubside >= yearDebut && monthSubside >= monthDebut)) {
                isSubsidePCFamille = true;
            }
        }
        return isSubsidePCFamille;
    }

}
