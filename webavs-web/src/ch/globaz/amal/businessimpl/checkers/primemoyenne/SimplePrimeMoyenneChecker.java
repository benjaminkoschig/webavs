/**
 * 
 */
package ch.globaz.amal.businessimpl.checkers.primemoyenne;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.primemoyenne.PrimeMoyenneException;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenne;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenneSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.checkers.AmalAbstractChecker;

/**
 * @author dhi
 * 
 */
public class SimplePrimeMoyenneChecker extends AmalAbstractChecker {

    /**
     * Contrôle de la création d'un paramètre de prime moyenne
     * 
     * L'ensemble des champs doit être renseignés et l'année choisie disponible
     * 
     * @param primeMoyenne
     * @throws PrimeMoyenneException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForCreate(SimplePrimeMoyenne primeMoyenne) throws PrimeMoyenneException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // contrôle de l'ensemble des champs (présence)
        SimplePrimeMoyenneChecker.checkMandatory(primeMoyenne);
        // contrôle que pour la même année un enregistrement ne soit pas existant
        SimplePrimeMoyenneChecker.checkIntegrity(primeMoyenne);
    }

    /**
     * Contrôle de la suppression d'un paramètre de prime moyenne
     * 
     * Pas de contrôle particulier
     * 
     * @param primeMoyenne
     */
    public static void checkForDelete(SimplePrimeMoyenne primeMoyenne) {
        // pas de contrôle particulier
    }

    /**
     * Contrôle de la mise à jour d'un paramètre de prime moyenne
     * 
     * L'ensemble des champs doit être renseignés et l'année choisie disponible
     * 
     * @param primeMoyenne
     * @throws PrimeMoyenneException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForUpdate(SimplePrimeMoyenne primeMoyenne) throws PrimeMoyenneException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // contrôle de l'ensemble des champs (présence)
        SimplePrimeMoyenneChecker.checkMandatory(primeMoyenne);
        // contrôle que pour la même année un enregistrement ne soit pas existant
        // SimplePrimeMoyenneChecker.checkIntegrity(primeMoyenne);
    }

    /**
     * Contrôle de l'intégrité de la prime moyenne : année doit être disponible
     * 
     * @param primeMoyenne
     * @throws PrimeMoyenneException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private static void checkIntegrity(SimplePrimeMoyenne primeMoyenne) throws PrimeMoyenneException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (!JadeStringUtil.isBlankOrZero(primeMoyenne.getAnneeSubside())) {
            SimplePrimeMoyenneSearch primeSearch = new SimplePrimeMoyenneSearch();
            primeSearch.setForAnneeSubside(primeMoyenne.getAnneeSubside());
            primeSearch = AmalServiceLocator.getSimplePrimeMoyenneService().search(primeSearch);
            if (primeSearch.getSize() > 0) {
                JadeThread
                        .logError(primeMoyenne.getClass().getName(), "amal.simplePrimeMoyenne.anneeSubside.integrity");
            }
        }
    }

    /**
     * Contrôle que les paramètres de prime moyenne soient tous renseignés
     * 
     * @param primeMoyenne
     */
    private static void checkMandatory(SimplePrimeMoyenne primeMoyenne) {
        if (JadeStringUtil.isBlankOrZero(primeMoyenne.getAnneeSubside())) {
            JadeThread.logError(primeMoyenne.getClass().getName(), "amal.simplePrimeMoyenne.anneeSubside.mandatory");
        }
        if (JadeStringUtil.isBlank(primeMoyenne.getMontantPrimeAdulte())) {
            JadeThread.logError(primeMoyenne.getClass().getName(),
                    "amal.simplePrimeMoyenne.montantPrimeAdulte.mandatory");
        }
        if (JadeStringUtil.isBlank(primeMoyenne.getMontantPrimeEnfant())) {
            JadeThread.logError(primeMoyenne.getClass().getName(),
                    "amal.simplePrimeMoyenne.montantPrimeEnfant.mandatory");
        }
        if (JadeStringUtil.isBlank(primeMoyenne.getMontantPrimeFormation())) {
            JadeThread.logError(primeMoyenne.getClass().getName(),
                    "amal.simplePrimeMoyenne.montantPrimeFormation.mandatory");
        }
    }

}
