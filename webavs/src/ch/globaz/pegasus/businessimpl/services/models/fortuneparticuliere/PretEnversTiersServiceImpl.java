package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.PretEnversTiersException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiers;
import ch.globaz.pegasus.business.models.fortuneparticuliere.PretEnversTiersSearch;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.PretEnversTiersService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * 
 * @author BSC
 * 
 */
public class PretEnversTiersServiceImpl extends PegasusAbstractServiceImpl implements PretEnversTiersService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. PretEnversTiersService
     * #count(ch.globaz.pegasus.business.models.fortuneparticuliere .PretEnversTiersSearch)
     */
    @Override
    public int count(PretEnversTiersSearch search) throws PretEnversTiersException, JadePersistenceException {
        if (search == null) {
            throw new PretEnversTiersException("Unable to count pretEnversTiers, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. PretEnversTiersService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .PretEnversTiers)
     */
    @Override
    public PretEnversTiers create(PretEnversTiers pretEnversTiers) throws JadePersistenceException,
            PretEnversTiersException, DonneeFinanciereException {
        if (pretEnversTiers == null) {
            throw new PretEnversTiersException("Unable to create pretEnversTiers, the model passed is null!");
        }

        try {
            pretEnversTiers
                    .setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                            .create(pretEnversTiers.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimplePretEnversTiersService().create(
                    pretEnversTiers.getSimplePretEnversTiers());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PretEnversTiersException("Service not available - " + e.getMessage());
        }

        return pretEnversTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. PretEnversTiersService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .PretEnversTiers)
     */
    @Override
    public PretEnversTiers delete(PretEnversTiers pretEnversTiers) throws PretEnversTiersException,
            JadePersistenceException {
        if (pretEnversTiers == null) {
            throw new PretEnversTiersException("Unable to delete pretEnversTiers, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimplePretEnversTiersService().delete(
                    pretEnversTiers.getSimplePretEnversTiers());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PretEnversTiersException("Service not available - " + e.getMessage());
        }

        return pretEnversTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. PretEnversTiersService#read(java.lang.String)
     */
    @Override
    public PretEnversTiers read(String idPretEnversTiers) throws JadePersistenceException, PretEnversTiersException {
        if (JadeStringUtil.isEmpty(idPretEnversTiers)) {
            throw new PretEnversTiersException("Unable to read pretEnversTiers, the id passed is null!");
        }
        PretEnversTiers pretEnversTiers = new PretEnversTiers();
        pretEnversTiers.setId(idPretEnversTiers);
        return (PretEnversTiers) JadePersistenceManager.read(pretEnversTiers);
    }

    /**
     * Chargement d'un PretEnversTiers via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws PretEnversTiersException
     * @throws JadePersistenceException
     */
    @Override
    public PretEnversTiers readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws PretEnversTiersException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new PretEnversTiersException(
                    "Unable to find PretEnversTiers the idDonneeFinanciereHeader passed si null!");
        }

        PretEnversTiersSearch search = new PretEnversTiersSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (PretEnversTiersSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new PretEnversTiersException("More than one PretEnversTiers find, one was exepcted!");
        }

        return (PretEnversTiers) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((PretEnversTiersSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. PretEnversTiersService
     * #search(ch.globaz.pegasus.business.models.fortuneparticuliere .PretEnversTiersSearch)
     */
    @Override
    public PretEnversTiersSearch search(PretEnversTiersSearch pretEnversTiersSearch) throws JadePersistenceException,
            PretEnversTiersException {
        if (pretEnversTiersSearch == null) {
            throw new PretEnversTiersException("Unable to search pretEnversTiers, the search model passed is null!");
        }
        return (PretEnversTiersSearch) JadePersistenceManager.search(pretEnversTiersSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. PretEnversTiersService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .PretEnversTiers)
     */
    @Override
    public PretEnversTiers update(PretEnversTiers pretEnversTiers) throws JadePersistenceException,
            PretEnversTiersException, DonneeFinanciereException {
        if (pretEnversTiers == null) {
            throw new PretEnversTiersException("Unable to update pretEnversTiers, the model passed is null!");
        }

        try {
            pretEnversTiers.setSimplePretEnversTiers(PegasusImplServiceLocator.getSimplePretEnversTiersService()
                    .update(pretEnversTiers.getSimplePretEnversTiers()));
            pretEnversTiers
                    .setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                            .update(pretEnversTiers.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PretEnversTiersException("Service not available - " + e.getMessage());
        }

        return pretEnversTiers;
    }

}
