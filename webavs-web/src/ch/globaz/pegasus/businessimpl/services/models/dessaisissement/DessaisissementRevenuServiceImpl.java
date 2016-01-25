package ch.globaz.pegasus.businessimpl.services.models.dessaisissement;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementRevenuException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenu;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuSearch;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.services.models.dessaisissement.DessaisissementRevenuService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * 
 * @author BSC
 * 
 */
public class DessaisissementRevenuServiceImpl extends PegasusAbstractServiceImpl implements
        DessaisissementRevenuService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementRevenuService #
     * count(ch.globaz.pegasus.business.models.fortuneparticuliere .DessaisissementRevenuSearch)
     */
    @Override
    public int count(DessaisissementRevenuSearch search) throws DessaisissementRevenuException,
            JadePersistenceException {
        if (search == null) {
            throw new DessaisissementRevenuException("Unable to count betail, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementRevenuService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .DessaisissementRevenu)
     */
    @Override
    public DessaisissementRevenu create(DessaisissementRevenu betail) throws JadePersistenceException,
            DessaisissementRevenuException, DonneeFinanciereException {
        if (betail == null) {
            throw new DessaisissementRevenuException("Unable to create betail, the model passed is null!");
        }

        try {
            betail.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .create(betail.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleDessaisissementRevenuService().create(
                    betail.getSimpleDessaisissementRevenu());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DessaisissementRevenuException("Service not available - " + e.getMessage());
        }

        return betail;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementRevenuService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .DessaisissementRevenu)
     */
    @Override
    public DessaisissementRevenu delete(DessaisissementRevenu betail) throws DessaisissementRevenuException,
            JadePersistenceException {
        if (betail == null) {
            throw new DessaisissementRevenuException("Unable to delete numeraire, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleDessaisissementRevenuService().delete(
                    betail.getSimpleDessaisissementRevenu());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DessaisissementRevenuException("Service not available - " + e.getMessage());
        }

        return betail;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementRevenuService
     * #read(java.lang.String)
     */
    @Override
    public DessaisissementRevenu read(String idDessaisissementRevenu) throws JadePersistenceException,
            DessaisissementRevenuException {
        if (JadeStringUtil.isEmpty(idDessaisissementRevenu)) {
            throw new DessaisissementRevenuException("Unable to read betail, the id passed is null!");
        }
        DessaisissementRevenu betail = new DessaisissementRevenu();
        betail.setId(idDessaisissementRevenu);
        return (DessaisissementRevenu) JadePersistenceManager.read(betail);
    }

    /**
     * Chargement d'une DessaisissementRevenu via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws DessaisissementRevenuSearch
     *             Exception
     * @throws JadePersistenceException
     */
    @Override
    public DessaisissementRevenu readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws DessaisissementRevenuException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new DessaisissementRevenuException(
                    "Unable to find DessaisissementRevenu the idDonneeFinanciereHeader passed si null!");
        }

        DessaisissementRevenuSearch search = new DessaisissementRevenuSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (DessaisissementRevenuSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new DessaisissementRevenuException("More than one DessaisissementRevenu find, one was exepcted!");
        }

        return (DessaisissementRevenu) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((DessaisissementRevenuSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementRevenuService #
     * search(ch.globaz.pegasus.business.models.fortuneparticuliere .DessaisissementRevenuSearch )
     */
    @Override
    public DessaisissementRevenuSearch search(DessaisissementRevenuSearch betailSearch)
            throws JadePersistenceException, DessaisissementRevenuException {
        if (betailSearch == null) {
            throw new DessaisissementRevenuException("Unable to search betail, the search model passed is null!");
        }
        return (DessaisissementRevenuSearch) JadePersistenceManager.search(betailSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementRevenuService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .DessaisissementRevenu)
     */
    @Override
    public DessaisissementRevenu update(DessaisissementRevenu betail) throws JadePersistenceException,
            DessaisissementRevenuException, DonneeFinanciereException {
        if (betail == null) {
            throw new DessaisissementRevenuException("Unable to update numeraire, the model passed is null!");
        }

        try {
            betail.setSimpleDessaisissementRevenu(PegasusImplServiceLocator.getSimpleDessaisissementRevenuService()
                    .update(betail.getSimpleDessaisissementRevenu()));
            betail.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .update(betail.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DessaisissementRevenuException("Service not available - " + e.getMessage());
        }

        return betail;
    }

}
