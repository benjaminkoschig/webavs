package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.BetailException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Betail;
import ch.globaz.pegasus.business.models.fortuneparticuliere.BetailSearch;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.BetailService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * 
 * @author BSC
 * 
 */
public class BetailServiceImpl extends PegasusAbstractServiceImpl implements BetailService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.fortuneparticuliere.BetailService #
     * count(ch.globaz.pegasus.business.models.fortuneparticuliere.BetailSearch)
     */
    @Override
    public int count(BetailSearch search) throws BetailException, JadePersistenceException {
        if (search == null) {
            throw new BetailException("Unable to count betail, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.fortuneparticuliere.BetailService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere.Betail)
     */
    @Override
    public Betail create(Betail betail) throws JadePersistenceException, BetailException, DonneeFinanciereException {
        if (betail == null) {
            throw new BetailException("Unable to create betail, the model passed is null!");
        }

        try {
            betail.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .create(betail.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleBetailService().create(betail.getSimpleBetail());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BetailException("Service not available - " + e.getMessage());
        }

        return betail;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.fortuneparticuliere.BetailService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere.Betail)
     */
    @Override
    public Betail delete(Betail betail) throws BetailException, JadePersistenceException {
        if (betail == null) {
            throw new BetailException("Unable to delete numeraire, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleBetailService().delete(betail.getSimpleBetail());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BetailException("Service not available - " + e.getMessage());
        }

        return betail;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.fortuneparticuliere.BetailService #read(java.lang.String)
     */
    @Override
    public Betail read(String idBetail) throws JadePersistenceException, BetailException {
        if (JadeStringUtil.isEmpty(idBetail)) {
            throw new BetailException("Unable to read betail, the id passed is null!");
        }
        Betail betail = new Betail();
        betail.setId(idBetail);
        return (Betail) JadePersistenceManager.read(betail);
    }

    /**
     * Chargement d'un Betail via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws BetailException
     * @throws JadePersistenceException
     */
    @Override
    public Betail readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws BetailException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new BetailException("Unable to find Betail the idDonneeFinanciereHeader passed si null!");
        }

        BetailSearch search = new BetailSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (BetailSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new BetailException("More than one Betail find, one was exepcted!");
        }

        return (Betail) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((BetailSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.fortuneparticuliere.BetailService #
     * search(ch.globaz.pegasus.business.models.fortuneparticuliere.BetailSearch )
     */
    @Override
    public BetailSearch search(BetailSearch betailSearch) throws JadePersistenceException, BetailException {
        if (betailSearch == null) {
            throw new BetailException("Unable to search betail, the search model passed is null!");
        }
        return (BetailSearch) JadePersistenceManager.search(betailSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.fortuneparticuliere.BetailService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere.Betail)
     */
    @Override
    public Betail update(Betail betail) throws JadePersistenceException, BetailException, DonneeFinanciereException {
        if (betail == null) {
            throw new BetailException("Unable to update numeraire, the model passed is null!");
        }

        try {
            betail.setSimpleBetail(PegasusImplServiceLocator.getSimpleBetailService().update(betail.getSimpleBetail()));
            betail.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .update(betail.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BetailException("Service not available - " + e.getMessage());
        }

        return betail;
    }

}
