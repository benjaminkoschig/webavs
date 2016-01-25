/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.primeavantageuse;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.primeavantageuse.PrimeAvantageuseException;
import ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuse;
import ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuseSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;

/**
 * @author dhi
 * 
 */
public class SimplePrimeAvantageuseChecker extends AmalAbstractChecker {

    /**
     * Contrôle de la création d'un paramètre de prime avantageuse
     * 
     * L'ensemble des champs doit être renseignés et l'année choisie disponible
     * 
     * @param primeAvantageuse
     * @throws PrimeAvantageuseException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForCreate(SimplePrimeAvantageuse primeAvantageuse) throws PrimeAvantageuseException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // contrôle de l'ensemble des champs (présence)
        SimplePrimeAvantageuseChecker.checkMandatory(primeAvantageuse);
        // contrôle que pour la même année un enregistrement ne soit pas existant
        SimplePrimeAvantageuseChecker.checkIntegrity(primeAvantageuse);
    }

    /**
     * Contrôle de la suppression d'un paramètre de prime avantageuse
     * 
     * Pas de contrôle particulier
     * 
     * @param primeAvantageuse
     */
    public static void checkForDelete(SimplePrimeAvantageuse primeAvantageuse) {
        // pas de contrôle particulier
    }

    /**
     * Contrôle de la mise à jour d'un paramètre de prime avantageuse
     * 
     * L'ensemble des champs doit être renseignés et l'année choisie disponible
     * 
     * @param primeAvantageuse
     * @throws PrimeAvantageuseException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForUpdate(SimplePrimeAvantageuse primeAvantageuse) throws PrimeAvantageuseException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // contrôle de l'ensemble des champs (présence)
        SimplePrimeAvantageuseChecker.checkMandatory(primeAvantageuse);
        // contrôle que pour la même année un enregistrement ne soit pas existant
        // SimplePrimeAvantageuseChecker.checkIntegrity(primeAvantageuse);
    }

    /**
     * Contrôle de l'intégrité de la prime avantageuse : année doit être disponible
     * 
     * @param primeAvantageuse
     * @throws PrimeAvantageuseException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private static void checkIntegrity(SimplePrimeAvantageuse primeAvantageuse) throws PrimeAvantageuseException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (!JadeStringUtil.isBlankOrZero(primeAvantageuse.getAnneeSubside())) {
            SimplePrimeAvantageuseSearch primeSearch = new SimplePrimeAvantageuseSearch();
            primeSearch.setForAnneeSubside(primeAvantageuse.getAnneeSubside());
            primeSearch = AmalServiceLocator.getSimplePrimeAvantageuseService().search(primeSearch);
            if (primeSearch.getSize() > 0) {
                JadeThread.logError(primeAvantageuse.getClass().getName(),
                        "amal.simplePrimeAvantageuse.anneeSubside.integrity");
            }
        }
    }

    /**
     * Contrôle que les paramètres de prime avantageuse soient tous renseignés
     * 
     * @param primeAvantageuse
     */
    private static void checkMandatory(SimplePrimeAvantageuse primeAvantageuse) {
        if (JadeStringUtil.isBlankOrZero(primeAvantageuse.getAnneeSubside())) {
            JadeThread.logError(primeAvantageuse.getClass().getName(),
                    "amal.simplePrimeAvantageuse.anneeSubside.mandatory");
        }
        if (JadeStringUtil.isBlank(primeAvantageuse.getMontantPrimeAdulte())) {
            JadeThread.logError(primeAvantageuse.getClass().getName(),
                    "amal.simplePrimeAvantageuse.montantPrimeAdulte.mandatory");
        }
        if (JadeStringUtil.isBlank(primeAvantageuse.getMontantPrimeEnfant())) {
            JadeThread.logError(primeAvantageuse.getClass().getName(),
                    "amal.simplePrimeAvantageuse.montantPrimeEnfant.mandatory");
        }
        if (JadeStringUtil.isBlank(primeAvantageuse.getMontantPrimeFormation())) {
            JadeThread.logError(primeAvantageuse.getClass().getName(),
                    "amal.simplePrimeAvantageuse.montantPrimeFormation.mandatory");
        }
    }

}
