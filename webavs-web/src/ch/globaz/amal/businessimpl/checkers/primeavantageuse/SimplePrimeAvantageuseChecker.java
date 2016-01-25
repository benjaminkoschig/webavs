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
     * Contr�le de la cr�ation d'un param�tre de prime avantageuse
     * 
     * L'ensemble des champs doit �tre renseign�s et l'ann�e choisie disponible
     * 
     * @param primeAvantageuse
     * @throws PrimeAvantageuseException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForCreate(SimplePrimeAvantageuse primeAvantageuse) throws PrimeAvantageuseException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // contr�le de l'ensemble des champs (pr�sence)
        SimplePrimeAvantageuseChecker.checkMandatory(primeAvantageuse);
        // contr�le que pour la m�me ann�e un enregistrement ne soit pas existant
        SimplePrimeAvantageuseChecker.checkIntegrity(primeAvantageuse);
    }

    /**
     * Contr�le de la suppression d'un param�tre de prime avantageuse
     * 
     * Pas de contr�le particulier
     * 
     * @param primeAvantageuse
     */
    public static void checkForDelete(SimplePrimeAvantageuse primeAvantageuse) {
        // pas de contr�le particulier
    }

    /**
     * Contr�le de la mise � jour d'un param�tre de prime avantageuse
     * 
     * L'ensemble des champs doit �tre renseign�s et l'ann�e choisie disponible
     * 
     * @param primeAvantageuse
     * @throws PrimeAvantageuseException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForUpdate(SimplePrimeAvantageuse primeAvantageuse) throws PrimeAvantageuseException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // contr�le de l'ensemble des champs (pr�sence)
        SimplePrimeAvantageuseChecker.checkMandatory(primeAvantageuse);
        // contr�le que pour la m�me ann�e un enregistrement ne soit pas existant
        // SimplePrimeAvantageuseChecker.checkIntegrity(primeAvantageuse);
    }

    /**
     * Contr�le de l'int�grit� de la prime avantageuse : ann�e doit �tre disponible
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
     * Contr�le que les param�tres de prime avantageuse soient tous renseign�s
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
