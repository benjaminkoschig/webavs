package ch.globaz.pegasus.businessimpl.services.models.parametre;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaitsSearch;
import ch.globaz.pegasus.business.services.models.parametre.SimpleZoneForfaitsService;
import ch.globaz.pegasus.businessimpl.checkers.parametre.SimpleZoneForfaitsChecker;

/**
 * @author DMA
 * @date 12 nov. 2010
 */
public class SimpleZoneForfaitsServiceImpl implements SimpleZoneForfaitsService {

    @Override
    public int count(SimpleZoneForfaitsSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException {
        if (search == null) {
            throw new ForfaitsPrimesAssuranceMaladieException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleZoneForfaits create(SimpleZoneForfaits simpleZoneForfaits)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError {
        if (simpleZoneForfaits == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to create simpleZoneForfaits, the model passed is null!");
        }

        SimpleZoneForfaitsChecker.checkForCreate(simpleZoneForfaits);

        return (SimpleZoneForfaits) JadePersistenceManager.add(simpleZoneForfaits);
    }

    @Override
    public SimpleZoneForfaits delete(SimpleZoneForfaits simpleZoneForfaits)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError {
        if (simpleZoneForfaits == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to delete simpleZoneForfaits, the model passed is null!");
        }
        SimpleZoneForfaitsChecker.checkForDelete(simpleZoneForfaits);
        return (SimpleZoneForfaits) JadePersistenceManager.delete(simpleZoneForfaits);
    }

    @Override
    public SimpleZoneForfaits read(String idSimpleZoneForfaits) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException {
        if (idSimpleZoneForfaits == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to read idSimpleZoneForfaits, the model passed is null!");
        }
        SimpleZoneForfaits simpleZoneForfaits = new SimpleZoneForfaits();
        simpleZoneForfaits.setId(idSimpleZoneForfaits);
        return (SimpleZoneForfaits) JadePersistenceManager.read(simpleZoneForfaits);

    }

    @Override
    public SimpleZoneForfaitsSearch search(SimpleZoneForfaitsSearch simpleZoneForfaitsSearch)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException {
        if (simpleZoneForfaitsSearch == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to search simpleZoneForfaitsSearch, the model passed is null!");
        }
        return (SimpleZoneForfaitsSearch) JadePersistenceManager.search(simpleZoneForfaitsSearch);
    }

    @Override
    public SimpleZoneForfaits update(SimpleZoneForfaits simpleZoneForfaits)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        if (simpleZoneForfaits == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to update simpleZoneForfaits, the model passed is null!");
        }
        SimpleZoneForfaitsChecker.checkForUpdate(simpleZoneForfaits);
        return (SimpleZoneForfaits) JadePersistenceManager.update(simpleZoneForfaits);
    }

}
