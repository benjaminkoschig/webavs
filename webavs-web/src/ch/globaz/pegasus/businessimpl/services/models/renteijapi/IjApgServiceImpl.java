package ch.globaz.pegasus.businessimpl.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IjApgException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.renteijapi.IjApg;
import ch.globaz.pegasus.business.models.renteijapi.IjApgSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.renteijapi.IjApgService;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class IjApgServiceImpl extends PegasusServiceLocator implements IjApgService {

    @Override
    public int count(IjApgSearch search) throws IjApgException, JadePersistenceException {
        if (search == null) {
            throw new IjApgException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public IjApg create(IjApg ijApg) throws IjApgException, JadePersistenceException, DonneeFinanciereException {
        if (ijApg == null) {
            throw new IjApgException("Unable to create ijApg, the model passed is null!");
        }
        try {
            // creation du donneeFinanciereHeader
            ijApg.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .create(ijApg.getSimpleDonneeFinanciereHeader()));
            // creation du simpleAutreRente
            ijApg.setSimpleIjApg(PegasusImplServiceLocator.getSimpleIjApgService().create(ijApg.getSimpleIjApg()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new IjApgException("Service not available - " + e.getMessage());
        }

        return ijApg;
    }

    @Override
    public IjApg delete(IjApg ijApg) throws IjApgException, JadePersistenceException, DonneeFinanciereException,
            JadeApplicationServiceNotAvailableException {
        if (ijApg == null) {
            throw new IjApgException("Unable to delete ijApg, the model passed is null!");
        }
        // effacement du donneeFinanciereHeader
        ijApg.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                .delete(ijApg.getSimpleDonneeFinanciereHeader()));

        // effacement du simpleAureRente
        ijApg.setSimpleIjApg(PegasusImplServiceLocator.getSimpleIjApgService().delete(ijApg.getSimpleIjApg()));

        return ijApg;
    }

    @Override
    public IjApg read(String idIjApg) throws IjApgException, JadePersistenceException {
        if (idIjApg == null) {
            throw new IjApgException("Unable to read idIjApg, the model passed is null!");
        }
        IjApg ijApg = new IjApg();
        ijApg.setId(idIjApg);
        return (IjApg) JadePersistenceManager.read(ijApg);
    }

    /**
     * Chargement d'une IjAPG via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws IjApgException
     * @throws JadePersistenceException
     */
    @Override
    public IjApg readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws IjApgException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new IjApgException("Unable to find IjAPG the idDonneeFinanciereHeader passed si null!");
        }

        IjApgSearch search = new IjApgSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (IjApgSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new IjApgException("More than one IjApg find, one was exepcted!");
        }

        return (IjApg) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((IjApgSearch) donneeFinanciereSearch);
    }

    @Override
    public IjApgSearch search(IjApgSearch ijApgSearch) throws JadePersistenceException, IjApgException {
        if (ijApgSearch == null) {
            throw new IjApgException("Unable to search ijApgSearch, the model passed is null!");
        }
        return (IjApgSearch) JadePersistenceManager.search(ijApgSearch);
    }

    @Override
    public IjApg update(IjApg ijApg) throws IjApgException, JadePersistenceException, DonneeFinanciereException {
        if (ijApg == null) {
            throw new IjApgException("Unable to update ijApg, the model passed is null!");
        }
        try {
            // mise a jour du donneeFinanciereHeader
            ijApg.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .update(ijApg.getSimpleDonneeFinanciereHeader()));
            // mise a jour du simpleAureRente
            ijApg.setSimpleIjApg(PegasusImplServiceLocator.getSimpleIjApgService().update(ijApg.getSimpleIjApg()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new IjApgException("Service not available - " + e.getMessage());
        }
        return ijApg;
    }

}
