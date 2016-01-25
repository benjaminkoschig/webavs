package ch.globaz.pegasus.businessimpl.services.models.fortuneparticuliere;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AssuranceRenteViagereException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagereSearch;
import ch.globaz.pegasus.business.services.models.fortuneparticuliere.AssuranceRenteViagereService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * 
 * @author BSC
 * 
 */
public class AssuranceRenteViagereServiceImpl extends PegasusAbstractServiceImpl implements
        AssuranceRenteViagereService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. AssuranceRenteViagereService
     * #count(ch.globaz.pegasus.business.models.fortuneparticuliere .AssuranceRenteViagereSearch)
     */
    @Override
    public int count(AssuranceRenteViagereSearch search) throws AssuranceRenteViagereException,
            JadePersistenceException {
        if (search == null) {
            throw new AssuranceRenteViagereException(
                    "Unable to count assuranceRenteViagere, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. AssuranceRenteViagereService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .AssuranceRenteViagere)
     */
    @Override
    public AssuranceRenteViagere create(AssuranceRenteViagere assuranceRenteViagere) throws JadePersistenceException,
            AssuranceRenteViagereException, DonneeFinanciereException {
        if (assuranceRenteViagere == null) {
            throw new AssuranceRenteViagereException(
                    "Unable to create assuranceRenteViagere, the model passed is null!");
        }

        try {
            assuranceRenteViagere.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            assuranceRenteViagere.getSimpleDonneeFinanciereHeader()));

            PegasusImplServiceLocator.getSimpleAssuranceRenteViagereService().create(
                    assuranceRenteViagere.getSimpleAssuranceRenteViagere());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AssuranceRenteViagereException("Service not available - " + e.getMessage());
        }

        return assuranceRenteViagere;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. AssuranceRenteViagereService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .AssuranceRenteViagere)
     */
    @Override
    public AssuranceRenteViagere delete(AssuranceRenteViagere assuranceRenteViagere)
            throws AssuranceRenteViagereException, JadePersistenceException {
        if (assuranceRenteViagere == null) {
            throw new AssuranceRenteViagereException(
                    "Unable to delete assuranceRenteViagere, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleAssuranceRenteViagereService().delete(
                    assuranceRenteViagere.getSimpleAssuranceRenteViagere());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AssuranceRenteViagereException("Service not available - " + e.getMessage());
        }

        return assuranceRenteViagere;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * AssuranceRenteViagereService#read(java.lang.String)
     */
    @Override
    public AssuranceRenteViagere read(String idAssuranceRenteViagere) throws JadePersistenceException,
            AssuranceRenteViagereException {
        if (JadeStringUtil.isEmpty(idAssuranceRenteViagere)) {
            throw new AssuranceRenteViagereException("Unable to read assuranceRenteViagere, the id passed is null!");
        }
        AssuranceRenteViagere assuranceRenteViagere = new AssuranceRenteViagere();
        assuranceRenteViagere.setId(idAssuranceRenteViagere);
        return (AssuranceRenteViagere) JadePersistenceManager.read(assuranceRenteViagere);
    }

    /**
     * Chargement d'un AssuranceRenteViagere via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AssuranceRenteViagereException
     * @throws JadePersistenceException
     */
    @Override
    public AssuranceRenteViagere readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws AssuranceRenteViagereException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new AssuranceRenteViagereException(
                    "Unable to find AssuranceRenteViagere the idDonneeFinanciereHeader passed si null!");
        }

        AssuranceRenteViagereSearch search = new AssuranceRenteViagereSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (AssuranceRenteViagereSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new AssuranceRenteViagereException("More than one AssuranceRenteViagere find, one was exepcted!");
        }

        return (AssuranceRenteViagere) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((AssuranceRenteViagereSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. AssuranceRenteViagereService
     * #search(ch.globaz.pegasus.business.models.fortuneparticuliere .AssuranceRenteViagereSearch)
     */
    @Override
    public AssuranceRenteViagereSearch search(AssuranceRenteViagereSearch assuranceRenteViagereSearch)
            throws JadePersistenceException, AssuranceRenteViagereException {
        if (assuranceRenteViagereSearch == null) {
            throw new AssuranceRenteViagereException(
                    "Unable to search assuranceRenteViagere, the search model passed is null!");
        }
        return (AssuranceRenteViagereSearch) JadePersistenceManager.search(assuranceRenteViagereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. AssuranceRenteViagereService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .AssuranceRenteViagere)
     */
    @Override
    public AssuranceRenteViagere update(AssuranceRenteViagere assuranceRenteViagere) throws JadePersistenceException,
            AssuranceRenteViagereException, DonneeFinanciereException {
        if (assuranceRenteViagere == null) {
            throw new AssuranceRenteViagereException(
                    "Unable to update assuranceRenteViagere, the model passed is null!");
        }

        try {
            assuranceRenteViagere.setSimpleAssuranceRenteViagere(PegasusImplServiceLocator
                    .getSimpleAssuranceRenteViagereService().update(
                            assuranceRenteViagere.getSimpleAssuranceRenteViagere()));
            assuranceRenteViagere.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            assuranceRenteViagere.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AssuranceRenteViagereException("Service not available - " + e.getMessage());
        }

        return assuranceRenteViagere;
    }

}
