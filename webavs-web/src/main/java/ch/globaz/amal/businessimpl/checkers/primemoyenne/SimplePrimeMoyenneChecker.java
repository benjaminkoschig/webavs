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
     * Contr�le de la cr�ation d'un param�tre de prime moyenne
     * 
     * L'ensemble des champs doit �tre renseign�s et l'ann�e choisie disponible
     * 
     * @param primeMoyenne
     * @throws PrimeMoyenneException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForCreate(SimplePrimeMoyenne primeMoyenne) throws PrimeMoyenneException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // contr�le de l'ensemble des champs (pr�sence)
        SimplePrimeMoyenneChecker.checkMandatory(primeMoyenne);
        // contr�le que pour la m�me ann�e un enregistrement ne soit pas existant
        SimplePrimeMoyenneChecker.checkIntegrity(primeMoyenne);
    }

    /**
     * Contr�le de la suppression d'un param�tre de prime moyenne
     * 
     * Pas de contr�le particulier
     * 
     * @param primeMoyenne
     */
    public static void checkForDelete(SimplePrimeMoyenne primeMoyenne) {
        // pas de contr�le particulier
    }

    /**
     * Contr�le de la mise � jour d'un param�tre de prime moyenne
     * 
     * L'ensemble des champs doit �tre renseign�s et l'ann�e choisie disponible
     * 
     * @param primeMoyenne
     * @throws PrimeMoyenneException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public static void checkForUpdate(SimplePrimeMoyenne primeMoyenne) throws PrimeMoyenneException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // contr�le de l'ensemble des champs (pr�sence)
        SimplePrimeMoyenneChecker.checkMandatory(primeMoyenne);
        // contr�le que pour la m�me ann�e un enregistrement ne soit pas existant
        // SimplePrimeMoyenneChecker.checkIntegrity(primeMoyenne);
    }

    /**
     * Contr�le de l'int�grit� de la prime moyenne : ann�e doit �tre disponible
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
     * Contr�le que les param�tres de prime moyenne soient tous renseign�s
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
