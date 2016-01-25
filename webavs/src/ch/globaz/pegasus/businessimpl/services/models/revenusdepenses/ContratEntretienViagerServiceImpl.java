package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.ContratEntretienViagerException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViagerSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.ContratEntretienViagerService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class ContratEntretienViagerServiceImpl extends PegasusAbstractServiceImpl implements
        ContratEntretienViagerService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. ContratEntretienViagerService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .ContratEntretienViagerSearch)
     */
    @Override
    public int count(ContratEntretienViagerSearch search) throws ContratEntretienViagerException,
            JadePersistenceException {
        if (search == null) {
            throw new ContratEntretienViagerException(
                    "Unable to count ContratEntretienViager, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. ContratEntretienViagerService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .ContratEntretienViager)
     */
    @Override
    public ContratEntretienViager create(ContratEntretienViager contratEntretienViager)
            throws JadePersistenceException, ContratEntretienViagerException, DonneeFinanciereException {
        if (contratEntretienViager == null) {
            throw new ContratEntretienViagerException(
                    "Unable to create ContratEntretienViager, the model passed is null!");
        }

        try {
            contratEntretienViager.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            contratEntretienViager.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleContratEntretienViagerService().create(
                    contratEntretienViager.getSimpleContratEntretienViager());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ContratEntretienViagerException("Service not available - " + e.getMessage());
        }

        return contratEntretienViager;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. ContratEntretienViagerService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .ContratEntretienViager)
     */
    @Override
    public ContratEntretienViager delete(ContratEntretienViager contratEntretienViager)
            throws ContratEntretienViagerException, JadePersistenceException {
        if (contratEntretienViager == null) {
            throw new ContratEntretienViagerException(
                    "Unable to delete ContratEntretienViager, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleContratEntretienViagerService().delete(
                    contratEntretienViager.getSimpleContratEntretienViager());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ContratEntretienViagerException("Service not available - " + e.getMessage());
        }

        return contratEntretienViager;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses.
     * ContratEntretienViagerService#read(java.lang.String)
     */
    @Override
    public ContratEntretienViager read(String idContratEntretienViager) throws JadePersistenceException,
            ContratEntretienViagerException {
        if (JadeStringUtil.isEmpty(idContratEntretienViager)) {
            throw new ContratEntretienViagerException("Unable to read ContratEntretienViager, the id passed is null!");
        }
        ContratEntretienViager ContratEntretienViager = new ContratEntretienViager();
        ContratEntretienViager.setId(idContratEntretienViager);
        return (ContratEntretienViager) JadePersistenceManager.read(ContratEntretienViager);
    }

    /**
     * Chargement d'un ContratEntretienViager via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws ContratEntretienViagerException
     * @throws JadePersistenceException
     */
    @Override
    public ContratEntretienViager readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws ContratEntretienViagerException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new ContratEntretienViagerException(
                    "Unable to find ContratEntretienViager the idDonneeFinanciereHeader passed si null!");
        }

        ContratEntretienViagerSearch search = new ContratEntretienViagerSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (ContratEntretienViagerSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new ContratEntretienViagerException("More than one ContratEntretienViager find, one was exepcted!");
        }

        return (ContratEntretienViager) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((ContratEntretienViagerSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. ContratEntretienViagerService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .ContratEntretienViagerSearch)
     */
    @Override
    public ContratEntretienViagerSearch search(ContratEntretienViagerSearch contratEntretienViagerSearch)
            throws JadePersistenceException, ContratEntretienViagerException {
        if (contratEntretienViagerSearch == null) {
            throw new ContratEntretienViagerException(
                    "Unable to search ContratEntretienViager, the search model passed is null!");
        }
        return (ContratEntretienViagerSearch) JadePersistenceManager.search(contratEntretienViagerSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. ContratEntretienViagerService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .ContratEntretienViager)
     */
    @Override
    public ContratEntretienViager update(ContratEntretienViager contratEntretienViager)
            throws JadePersistenceException, ContratEntretienViagerException, DonneeFinanciereException {
        if (contratEntretienViager == null) {
            throw new ContratEntretienViagerException(
                    "Unable to update ContratEntretienViager, the model passed is null!");
        }

        try {
            contratEntretienViager.setSimpleContratEntretienViager(PegasusImplServiceLocator
                    .getSimpleContratEntretienViagerService().update(
                            contratEntretienViager.getSimpleContratEntretienViager()));
            contratEntretienViager.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            contratEntretienViager.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new ContratEntretienViagerException("Service not available - " + e.getMessage());
        }

        return contratEntretienViager;
    }

}
