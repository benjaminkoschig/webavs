package ch.globaz.pegasus.businessimpl.services.models.creancier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordeeSearch;
import ch.globaz.pegasus.business.services.models.creancier.SimpleCreanceAccordeeService;
import ch.globaz.pegasus.businessimpl.checkers.creancier.SimpleCreanceAccordeeChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class SimpleCreanceAccordeeServiceImpl implements SimpleCreanceAccordeeService {

    @Override
    public int count(SimpleCreanceAccordeeSearch search) throws CreancierException, JadePersistenceException {
        if (search == null) {
            throw new CreancierException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleCreanceAccordee create(SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException,
            JadePersistenceException {
        if (simpleCreanceAccordee == null) {
            throw new CreancierException("Unable to create simpleCreanceAccordee, the model passed is null!");
        }
        SimpleCreanceAccordeeChecker.checkForCreate(simpleCreanceAccordee);
        return (SimpleCreanceAccordee) JadePersistenceManager.add(simpleCreanceAccordee);
    }

    @Override
    public SimpleCreanceAccordee delete(SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException,
            JadePersistenceException {
        if (simpleCreanceAccordee == null) {
            throw new CreancierException("Unable to delete simpleCreanceAccordee, the model passed is null!");
        }

        return (SimpleCreanceAccordee) JadePersistenceManager.delete(simpleCreanceAccordee);
    }

    public void delete(SimpleCreanceAccordeeSearch simpleCreanceAccordeeSearch) throws CreancierException,
            JadePersistenceException {
        if (simpleCreanceAccordeeSearch == null) {
            throw new CreancierException("Unable to delete simpleCreanceAccordeeSearch, the model passed is null!");
        }

        JadePersistenceManager.delete(simpleCreanceAccordeeSearch);
    }

    @Override
    public void deleteByIdVersionDroit(String idVersionDroit) throws CreancierException, JadePersistenceException {
        SimplePCAccordeeSearch simplePCAccordeeSearch = new SimplePCAccordeeSearch();
        simplePCAccordeeSearch.setForIdVersionDroit(idVersionDroit);
        try {
            simplePCAccordeeSearch = PegasusImplServiceLocator.getSimplePCAccordeeService().search(
                    simplePCAccordeeSearch);
        } catch (PCAccordeeException e) {
            throw new CreancierException("Unable to search the simplePCAccordeeSearch for delete", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CreancierException("Unable to search the simplePCAccordeeSearch for delete", e);
        }

        SimpleCreanceAccordeeSearch simpleCreanceAccordeeSearch = new SimpleCreanceAccordeeSearch();
        for (JadeAbstractModel model : simplePCAccordeeSearch.getSearchResults()) {
            SimplePCAccordee simplePCAccordee = (SimplePCAccordee) model;
            simpleCreanceAccordeeSearch.setForIdPcAccordee(simplePCAccordee.getIdPCAccordee());
            this.delete(simpleCreanceAccordeeSearch);
        }
    }

    @Override
    public SimpleCreanceAccordee read(String idSimpleCreanceAccordee) throws CreancierException,
            JadePersistenceException {
        if (idSimpleCreanceAccordee == null) {
            throw new CreancierException("Unable to read idSimpleCreanceAccordee, the model passed is null!");
        }
        SimpleCreanceAccordee simpleCreanceAccordee = new SimpleCreanceAccordee();
        simpleCreanceAccordee.setId(idSimpleCreanceAccordee);
        return (SimpleCreanceAccordee) JadePersistenceManager.read(simpleCreanceAccordee);
    }

    @Override
    public SimpleCreanceAccordeeSearch search(SimpleCreanceAccordeeSearch simpleCreanceAccordeeSearch)
            throws CreancierException, JadePersistenceException {
        if (simpleCreanceAccordeeSearch == null) {
            throw new CreancierException("Unable to search simpleCreanceAccordeeSearch, the model passed is null!");
        }
        return (SimpleCreanceAccordeeSearch) JadePersistenceManager.search(simpleCreanceAccordeeSearch);
    }

    @Override
    public SimpleCreanceAccordee update(SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException,
            JadePersistenceException {
        if (simpleCreanceAccordee == null) {
            throw new CreancierException("Unable to update simpleCreanceAccordee, the model passed is null!");
        }
        SimpleCreanceAccordeeChecker.checkForUpdate(simpleCreanceAccordee);
        return (SimpleCreanceAccordee) JadePersistenceManager.update(simpleCreanceAccordee);
    }

}
