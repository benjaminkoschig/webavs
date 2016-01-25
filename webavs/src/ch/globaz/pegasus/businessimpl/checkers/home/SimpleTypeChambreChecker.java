package ch.globaz.pegasus.businessimpl.checkers.home;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.HomeSearch;
import ch.globaz.pegasus.business.models.home.PrixChambreSearch;
import ch.globaz.pegasus.business.models.home.SimpleTypeChambre;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pyxis.business.model.PersonneEtendueSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public abstract class SimpleTypeChambreChecker extends PegasusAbstractChecker {
    /**
     * @param simpleTypeChambre
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws HomeException
     */
    public static void checkForCreate(SimpleTypeChambre simpleTypeChambre) throws TypeChambreException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        SimpleTypeChambreChecker.checkMandatory(simpleTypeChambre);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleTypeChambreChecker.checkIntegrity(simpleTypeChambre);
        }
    }

    /**
     * Validation lors de l'effacement d'un simpleTypeChambre
     * 
     * <li>Un simpleTypeChambre ne peut pas etre efface si il existe un prix de chambre pour ce simpleTypeChambre</li>
     * 
     * @param simpleTypeChambre
     * @throws TypeChambreException
     */
    public static void checkForDelete(SimpleTypeChambre simpleTypeChambre) throws TypeChambreException {

        // Un simpleTypeChambre ne peut pas etre efface si il existe un prix de
        // chambre pour ce simpleTypeChambre
        PrixChambreSearch tChSearch = new PrixChambreSearch();
        tChSearch.setForIdTypeChambre(simpleTypeChambre.getIdTypeChambre());

        try {
            if (!PegasusAbstractChecker.threadOnError()) {
                if (PegasusImplServiceLocator.getPrixChambreService().count(tChSearch) > 0) {
                    JadeThread.logError(simpleTypeChambre.getClass().getName(),
                            "pegasus.typeChambre.typeChambreAvecPrixChambre.delete");
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TypeChambreException("Unable to check home", e);
        } catch (PrixChambreException e) {
            throw new TypeChambreException("Unable to check home", e);
        } catch (JadePersistenceException e) {
            throw new TypeChambreException("Unable to check home", e);
        } catch (JadeNoBusinessLogSessionError e) {
            throw new TypeChambreException("Unable to check home", e);
        }

    }

    /**
     * @param simpleTypeChambre
     * @throws JadeNoBusinessLogSessionError
     * @throws JadePersistenceException
     * @throws TypeChambreException
     */
    public static void checkForUpdate(SimpleTypeChambre simpleTypeChambre) throws TypeChambreException,
            JadePersistenceException, JadeNoBusinessLogSessionError {
        SimpleTypeChambreChecker.checkMandatory(simpleTypeChambre);

        if (!PegasusAbstractChecker.threadOnError()) {
            SimpleTypeChambreChecker.checkIntegrity(simpleTypeChambre);
        }
    }

    /**
     * Verification de l'integrite des donnees
     * 
     * <li>le home doit exister</li> <li>Si le type de chambre est une particularite, l'idTiersParticularite doit
     * exister dans pyxis</li>
     * 
     * @param simpleTypeChambre
     * @throws TypeChambreException
     * @throws JadePersistenceException
     * @throws JadeNoBusinessLogSessionError
     */
    private static void checkIntegrity(SimpleTypeChambre simpleTypeChambre) throws TypeChambreException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        // le home doit exister
        HomeSearch hSearch = new HomeSearch();
        hSearch.setForIdHome(simpleTypeChambre.getIdHome());
        try {
            if (PegasusServiceLocator.getHomeService().count(hSearch) < 1) {
                JadeThread.logError(simpleTypeChambre.getClass().getName(), "pegasus.typeChambre.home.integrity");

            }
        } catch (JadeApplicationServiceNotAvailableException e1) {
            throw new TypeChambreException("Unable to get PersonneEtendueService service", e1);
        } catch (JadeApplicationException e) {
            throw new TypeChambreException("Unable to get PersonneEtendueService service", e);
        }

        // Si le type de chambre est une particularite
        if (simpleTypeChambre.getIsParticularite().booleanValue()) {

            // l'idTiersParticularite doit exister dans pyxis
            PersonneEtendueSearchComplexModel peSearch = new PersonneEtendueSearchComplexModel();
            peSearch.setForIdTiers(simpleTypeChambre.getIdTiersParticularite());
            try {
                if (TIBusinessServiceLocator.getPersonneEtendueService().count(peSearch) < 1) {
                    JadeThread.logError(simpleTypeChambre.getClass().getName(),
                            "pegasus.typeChambre.tiersParticulariute.integrity");

                }
            } catch (JadeApplicationServiceNotAvailableException e1) {
                throw new TypeChambreException("Unable to get PersonneEtendueService service", e1);
            } catch (JadeApplicationException e) {
                throw new TypeChambreException("Unable to get PersonneEtendueService service", e);
            }
        }

    }

    /**
     * Verifiaction des donnees obligatoires:
     * 
     * <li>un type de chambre doit avoir une reference sur un home</li> <li>un type de chambre doit avoir une
     * designation</li><li>Si le type de chambre est une particularite, il doit avoir une reference sur un tiers</li>
     * 
     * @param simpleTypeChambre
     */
    private static void checkMandatory(SimpleTypeChambre simpleTypeChambre) {

        // un type de chambre doit avoir une reference sur un home
        if (JadeStringUtil.isIntegerEmpty(simpleTypeChambre.getIdHome())) {
            JadeThread.logError(simpleTypeChambre.getClass().getName(), "pegasus.typeChambre.idHome.mandatory");
        }
        // un type de chambre doit avoir une designation
        if (JadeStringUtil.isEmpty(simpleTypeChambre.getDesignation())) {
            JadeThread.logError(simpleTypeChambre.getClass().getName(), "pegasus.typeChambre.designation.mandatory");
        }
        // Si le type de chambre est une particularite, il doit avoir une
        // reference sur un tiers
        if (simpleTypeChambre.getIsParticularite().booleanValue()) {
            if (JadeStringUtil.isIntegerEmpty(simpleTypeChambre.getIdTiersParticularite())) {
                JadeThread.logError(simpleTypeChambre.getClass().getName(),
                        "pegasus.typeChambre.idTiersPourParticularite.mandatory");
            }
        }

    }
}
