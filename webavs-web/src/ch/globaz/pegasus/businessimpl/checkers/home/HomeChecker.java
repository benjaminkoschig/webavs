package ch.globaz.pegasus.businessimpl.checkers.home;

import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.PeriodeServiceEtatException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.home.HomeSearch;
import ch.globaz.pegasus.business.models.home.PeriodeServiceEtatSearch;
import ch.globaz.pegasus.business.models.home.TypeChambreSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseSearch;
import ch.globaz.pyxis.business.model.PersonneEtendueAdresseSearchComplexModel;

public abstract class HomeChecker extends PegasusAbstractChecker {
    /**
     * Validation d'un home lors de la creation
     * 
     * @param home
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws HomeException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static void checkForCreate(Home home) throws HomeException, JadePersistenceException,
            JadeNoBusinessLogSessionError {
        HomeChecker.checkMandatory(home);
        if (!PegasusAbstractChecker.threadOnError()) {
            HomeChecker.checkIntegrity(home);
        }
    }

    /**
     * Validation lors de l'effacement d'un home
     * 
     * @param home
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws TypeChambreException
     * @throws HomeException
     * @throws PeriodeServiceEtatException
     */
    public static void checkForDelete(Home home) throws TypeChambreException, JadePersistenceException,
            JadeNoBusinessLogSessionError, HomeException, PeriodeServiceEtatException {

        try {
            TypeChambreSearch tcSearch = new TypeChambreSearch();
            tcSearch.setForIdHome(home.getId());
            if (PegasusImplServiceLocator.getTypeChambreService().count(tcSearch) > 0) {
                JadeThread.logError(home.getClass().getName(), "pegasus.home.typeChambreExistant.integrity");
            }

            PeriodeServiceEtatSearch periodeSearch = new PeriodeServiceEtatSearch();
            periodeSearch.setForIdHome(home.getId());
            if (PegasusImplServiceLocator.getPeriodeServiceEtatService().count(periodeSearch) > 0) {
                JadeThread.logError(home.getClass().getName(), "pegasus.home.periodeExistant.integrity");
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Unable to check home", e);
        }

    }

    /**
     * Validation d'un home lors d'une mise a jours
     * 
     * @param home
     * @throws HomeException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    public static void checkForUpdate(Home home) throws HomeException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        HomeChecker.checkMandatory(home);
        if (!PegasusAbstractChecker.threadOnError()) {
            HomeChecker.checkIntegrity(home);
        }
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * <li>Il ne peut exister qu'un seul home pour un tiersHome et un nom de batiment donne</li>
     * 
     * @param home
     * @throws HomeException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(Home home) throws HomeException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

        // Il ne peut exister qu'un seul home pour un tiersHome et un nom de
        // batiment donne
        HomeSearch hSearch = new HomeSearch();
        hSearch.setForIdTiersHome(home.getSimpleHome().getIdTiersHome());
        hSearch.setForNomBatiment(home.getSimpleHome().getNomBatiment());
        hSearch.setNotForIdHome(home.getSimpleHome().getIdHome());

        try {

            PersonneEtendueAdresseSearchComplexModel adresseSearchComplexModel = new PersonneEtendueAdresseSearchComplexModel();
            adresseSearchComplexModel.setForIdTiers(home.getSimpleHome().getIdTiersHome());
            AdresseSearch adresseSearch = new AdresseSearch();
            adresseSearch.setForIdTier(home.getSimpleHome().getIdTiersHome());

            // if (TIBusinessServiceLocator.getAdresseService().searchAdresseWithSimpleTiers(adresseSearch).getSize() ==
            // 0) {
            // JadeThread.logError(home.getClass().getName(), "pegasus.home.noAdressseFounded.integrity");
            // }

        } catch (Exception e1) {
            throw new HomeException("Unable to get the session", e1);
        }

        try {
            if (!PegasusAbstractChecker.threadOnError()) {
                if (PegasusServiceLocator.getHomeService().count(hSearch) > 0) {
                    JadeThread.logError(home.getClass().getName(), "pegasus.home.tierHomeEtBatimentUnique.integrity");
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new HomeException("Unable to check home", e);
        }
    }

    /**
     * @param home
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws HomeException
     */
    private static void checkMandatory(Home home) throws HomeException, JadePersistenceException,
            JadeNoBusinessLogSessionError {

    }
}
