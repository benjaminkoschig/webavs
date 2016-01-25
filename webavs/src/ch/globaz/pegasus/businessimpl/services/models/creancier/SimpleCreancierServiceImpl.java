package ch.globaz.pegasus.businessimpl.services.models.creancier;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.SimpleCreancier;
import ch.globaz.pegasus.business.models.creancier.SimpleCreancierSearch;
import ch.globaz.pegasus.business.services.models.creancier.SimpleCreancierService;
import ch.globaz.pegasus.businessimpl.checkers.creancier.SimpleCreancierChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleCreancierServiceImpl extends PegasusAbstractServiceImpl implements SimpleCreancierService {

    @Override
    public int count(SimpleCreancierSearch search) throws CreancierException, JadePersistenceException {
        if (search == null) {
            throw new CreancierException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleCreancier create(SimpleCreancier simpleCreancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError {
        if (simpleCreancier == null) {
            throw new CreancierException("Unable to create simpleCreancier, the model passed is null!");
        }
        SimpleCreancierChecker.checkForCreate(simpleCreancier);
        return (SimpleCreancier) JadePersistenceManager.add(simpleCreancier);
    }

    @Override
    public SimpleCreancier delete(SimpleCreancier simpleCreancier) throws CreancierException, JadePersistenceException {
        if (simpleCreancier == null) {
            throw new CreancierException("Unable to delete simpleCreancier, the model passed is null!");
        }
        SimpleCreancierChecker.checkForDelete(simpleCreancier);
        return (SimpleCreancier) JadePersistenceManager.delete(simpleCreancier);
    }

    @Override
    public SimpleCreancier read(String idSimpleCreancier) throws CreancierException, JadePersistenceException {
        if (idSimpleCreancier == null) {
            throw new CreancierException("Unable to read idSimpleCreancier, the model passed is null!");
        }
        SimpleCreancier simpleCreancier = new SimpleCreancier();
        simpleCreancier.setId(idSimpleCreancier);
        return (SimpleCreancier) JadePersistenceManager.read(simpleCreancier);
    }

    @Override
    public SimpleCreancierSearch search(SimpleCreancierSearch simpleCreancierSearch) throws CreancierException,
            JadePersistenceException {
        if (simpleCreancierSearch == null) {
            throw new CreancierException("Unable to search simpleCreancierSearch, the model passed is null!");
        }
        return (SimpleCreancierSearch) JadePersistenceManager.search(simpleCreancierSearch);
    }

    @Override
    public SimpleCreancier update(SimpleCreancier simpleCreancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {

        if (simpleCreancier == null) {
            throw new CreancierException("Unable to update simpleCreancier, the model passed is null!");
        }
        SimpleCreancierChecker.checkForUpdate(simpleCreancier);
        return (SimpleCreancier) JadePersistenceManager.update(simpleCreancier);
    }

}
