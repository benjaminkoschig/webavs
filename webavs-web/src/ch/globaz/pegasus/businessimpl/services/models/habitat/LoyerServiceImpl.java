package ch.globaz.pegasus.businessimpl.services.models.habitat;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.habitat.Loyer;
import ch.globaz.pegasus.business.models.habitat.LoyerSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.habitat.LoyerService;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class LoyerServiceImpl extends PegasusServiceLocator implements LoyerService {

    @Override
    public int count(AbstractDonneeFinanciereSearchModel search) throws LoyerException, JadePersistenceException {
        if (search == null) {
            throw new LoyerException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public Loyer create(Loyer loyer) throws LoyerException, JadePersistenceException, DonneeFinanciereException {
        if (loyer == null) {
            throw new LoyerException("Unable to create loyer, the model passed is null!");
        }
        try {
            // creation du donneeFinanciereHeader
            loyer.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .create(loyer.getSimpleDonneeFinanciereHeader()));

            // creation du simpleLoyer
            loyer.setSimpleLoyer((PegasusImplServiceLocator.getSimpleLoyerService().create(loyer.getSimpleLoyer())));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new LoyerException("Service not available - " + e.getMessage());
        }
        return loyer;
    }

    @Override
    public Loyer delete(Loyer loyer) throws LoyerException, JadePersistenceException, DonneeFinanciereException {
        try {
            // effacement du donneeFinanciereHeader
            loyer.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .delete(loyer.getSimpleDonneeFinanciereHeader()));

            // effacement du simpleLoyer
            loyer.setSimpleLoyer(PegasusImplServiceLocator.getSimpleLoyerService().delete(loyer.getSimpleLoyer()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new LoyerException("Service not available - " + e.getMessage());
        }
        return loyer;
    }

    @Override
    public Loyer read(String idLoyer) throws LoyerException, JadePersistenceException {
        if (idLoyer == null) {
            throw new LoyerException("Unable to read idLoyer, the model passed is null!");
        }
        Loyer loyer = new Loyer();
        loyer.setId(idLoyer);
        return (Loyer) JadePersistenceManager.read(loyer);
    }

    /**
     * Chargement d'un Loyer via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws LoyerException
     * @throws JadePersistenceException
     */
    @Override
    public Loyer readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws LoyerException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new LoyerException("Unable to find Loyer the idDonneeFinanciereHeader passed si null!");
        }

        LoyerSearch search = new LoyerSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (LoyerSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new LoyerException("More than one Loyer find, one was exepcted!");
        }

        return (Loyer) search.getSearchResults()[0];
    }

    @Override
    public LoyerSearch search(AbstractDonneeFinanciereSearchModel loyerSearch) throws JadePersistenceException,
            DonneeFinanciereException {
        if (loyerSearch == null) {
            throw new DonneeFinanciereException("Unable to search loyerSearch, the model passed is null!");
        }
        return (LoyerSearch) JadePersistenceManager.search(loyerSearch);
    }

    @Override
    public Loyer update(Loyer loyer) throws LoyerException, JadePersistenceException, DonneeFinanciereException {
        if (loyer == null) {
            throw new LoyerException("Unable to update loyer, the model passed is null!");
        }

        try {
            // mise a jour du donneeFinanciereHeader
            loyer.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .update(loyer.getSimpleDonneeFinanciereHeader()));

            // mise a jour du simpleLoyer
            loyer.setSimpleLoyer((PegasusImplServiceLocator.getSimpleLoyerService().update(loyer.getSimpleLoyer())));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new LoyerException("Service not available - " + e.getMessage());
        }
        return loyer;
    }

}
