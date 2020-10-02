package ch.globaz.pegasus.businessimpl.checkers.home;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.SimpleHome;
import ch.globaz.pegasus.business.models.home.TypeChambreSearch;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public abstract class SimpleHomeChecker extends PegasusAbstractChecker {
    /**
     * @param simpleHome
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws HomeException
     */
    public static void checkForCreate(SimpleHome simpleHome) throws HomeException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        SimpleHomeChecker.checkMandatory(simpleHome);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleHomeChecker.checkIntegrity(simpleHome);
        }
    }

    /**
     * Validation lors de l'effacement d'un home
     * 
     * <li>Un home ne peut pas etre efface si il existe un type de chambre pour ce home</li>
     * 
     * @param simpleHome
     * @throws TypeChambreException
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     */
    public static void checkForDelete(SimpleHome simpleHome) throws HomeException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        // Un home ne peut pas etre efface si il existe un type de chambre pour
        // ce home
        TypeChambreSearch tChSearch = new TypeChambreSearch();
        tChSearch.setForIdHome(simpleHome.getIdHome());
        try {
            if (!PegasusAbstractChecker.threadOnError()) {
                if (PegasusImplServiceLocator.getTypeChambreService().count(tChSearch) > 0) {
                    JadeThread.logError(simpleHome.getClass().getName(), "pegasus.home.homeAvecTypeChambre.delete");
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Unable to check home", e);
        } catch (TypeChambreException e) {
            throw new HomeException("Unable to check home", e);
        }

    }

    /**
     * @param simpleHome
     */
    public static void checkForUpdate(SimpleHome simpleHome) {
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * <li>Le tiersHome doit exister dans Pyxis</li>
     * 
     * @param simpleHome
     * @throws HomeException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleHome simpleHome) throws HomeException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        // l'idTiersHome doit exister dans pyxis
        // PersonneEtendueSearchComplexModel peSearch = new PersonneEtendueSearchComplexModel();
        // peSearch.setForIdTiers(simpleHome.getIdTiersHome());
        // try {
        // if (TIBusinessServiceLocator.getPersonneEtendueService().count(peSearch) < 1) {
        // JadeThread.logError(simpleHome.getClass().getName(), "pegasus.home.tiersHome.integrity");
        //
        // }
        // } catch (JadeApplicationServiceNotAvailableException e1) {
        // throw new HomeException("Unable to get PersonneEtendueService service", e1);
        // } catch (JadeApplicationException e) {
        // throw new HomeException("Unable to get PersonneEtendueService service", e);
        // }

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * <li>Un home doit avoir une reference sur un tiers</li>
     * 
     * @param home
     */
    private static void checkMandatory(SimpleHome home) {

        // Un home doit avoir une reference sur un tiers
        if (JadeStringUtil.isEmpty(home.getIdTiersHome())) {
            JadeThread.logError(home.getClass().getName(), "pegasus.home.idTiersHome.mandatory");
        }



    }
}
