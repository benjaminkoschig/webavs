package ch.globaz.pegasus.businessimpl.services.models.creancier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.Creancier;
import ch.globaz.pegasus.business.models.creancier.CreancierSearch;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordeeSearch;
import ch.globaz.pegasus.business.services.models.creancier.CreancierService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class CreancierServiceImpl extends PegasusAbstractServiceImpl implements CreancierService {

    @Override
    public int count(CreancierSearch search) throws CreancierException, JadePersistenceException {
        if (search == null) {
            throw new CreancierException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public Creancier create(Creancier creancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        if (creancier == null) {
            throw new CreancierException("Unable to create creancier, the model passed is null!");
        }
        creancier.setSimpleCreancier(PegasusImplServiceLocator.getSimpleCreancierService().create(
                creancier.getSimpleCreancier()));
        return creancier;
    }

    @Override
    public Creancier delete(Creancier creancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        creancier.setSimpleCreancier(PegasusImplServiceLocator.getSimpleCreancierService().delete(
                creancier.getSimpleCreancier()));
        return creancier;
    }

    @Override
    public boolean hasCreanceAccordee(String idCreancier) throws CreancierException, JadePersistenceException {
        SimpleCreanceAccordeeSearch search = new SimpleCreanceAccordeeSearch();
        search.setForIdCreancier(idCreancier);
        int nb = 0;
        try {
            nb = PegasusImplServiceLocator.getSimpleCreanceAccordeeService().count(search);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CreancierException("Unable to check the crancier", e);
        }
        return nb > 0;
    }

    @Override
    public Creancier read(String idCreancier) throws CreancierException, JadePersistenceException {
        if (idCreancier == null) {
            throw new CreancierException("Unable to read idCreancier, the model passed is null!");
        }
        Creancier creancier = new Creancier();
        creancier.setId(idCreancier);
        return (Creancier) JadePersistenceManager.read(creancier);
    }

    @Override
    public CreancierSearch search(CreancierSearch creancierSearch) throws CreancierException, JadePersistenceException {
        if (creancierSearch == null) {
            throw new CreancierException("Unable to search creancierSearch, the model passed is null!");
        }
        return (CreancierSearch) JadePersistenceManager.search(creancierSearch);
    }

    @Override
    public Creancier update(Creancier creancier) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        creancier.setSimpleCreancier(PegasusImplServiceLocator.getSimpleCreancierService().update(
                creancier.getSimpleCreancier()));
        return creancier;
    }

}
