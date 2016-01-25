package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AllocationsFamilialesException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamiliales;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamilialesSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.AllocationsFamilialesService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class AllocationsFamilialesServiceImpl extends PegasusAbstractServiceImpl implements
        AllocationsFamilialesService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AllocationsFamilialesService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .AllocationsFamilialesSearch)
     */
    @Override
    public int count(AllocationsFamilialesSearch search) throws AllocationsFamilialesException,
            JadePersistenceException {
        if (search == null) {
            throw new AllocationsFamilialesException(
                    "Unable to count AllocationsFamiliales, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AllocationsFamilialesService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .AllocationsFamiliales)
     */
    @Override
    public AllocationsFamiliales create(AllocationsFamiliales allocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException, DonneeFinanciereException {
        if (allocationsFamiliales == null) {
            throw new AllocationsFamilialesException(
                    "Unable to create AllocationsFamiliales, the model passed is null!");
        }

        try {
            allocationsFamiliales.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            allocationsFamiliales.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleAllocationsFamilialesService().create(
                    allocationsFamiliales.getSimpleAllocationsFamiliales());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AllocationsFamilialesException("Service not available - " + e.getMessage());
        }

        return allocationsFamiliales;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AllocationsFamilialesService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .AllocationsFamiliales)
     */
    @Override
    public AllocationsFamiliales delete(AllocationsFamiliales allocationsFamiliales)
            throws AllocationsFamilialesException, JadePersistenceException {
        if (allocationsFamiliales == null) {
            throw new AllocationsFamilialesException(
                    "Unable to delete AllocationsFamiliales, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleAllocationsFamilialesService().delete(
                    allocationsFamiliales.getSimpleAllocationsFamiliales());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AllocationsFamilialesException("Service not available - " + e.getMessage());
        }

        return allocationsFamiliales;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses.
     * AllocationsFamilialesService#read(java.lang.String)
     */
    @Override
    public AllocationsFamiliales read(String idAllocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException {
        if (JadeStringUtil.isEmpty(idAllocationsFamiliales)) {
            throw new AllocationsFamilialesException("Unable to read AllocationsFamiliales, the id passed is null!");
        }
        AllocationsFamiliales AllocationsFamiliales = new AllocationsFamiliales();
        AllocationsFamiliales.setId(idAllocationsFamiliales);
        return (AllocationsFamiliales) JadePersistenceManager.read(AllocationsFamiliales);
    }

    /**
     * Chargement d'une AllocationsFamiliales via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AllocationsFamilialesException
     * @throws JadePersistenceException
     */
    @Override
    public AllocationsFamiliales readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws AllocationsFamilialesException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new AllocationsFamilialesException(
                    "Unable to find AllocationsFamiliales the idDonneeFinanciereHeader passed si null!");
        }

        AllocationsFamilialesSearch search = new AllocationsFamilialesSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (AllocationsFamilialesSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new AllocationsFamilialesException("More than one AllocationsFamiliales find, one was exepcted!");
        }

        return (AllocationsFamiliales) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((AllocationsFamilialesSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AllocationsFamilialesService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .AllocationsFamilialesSearch)
     */
    @Override
    public AllocationsFamilialesSearch search(AllocationsFamilialesSearch allocationsFamilialesSearch)
            throws JadePersistenceException, AllocationsFamilialesException {
        if (allocationsFamilialesSearch == null) {
            throw new AllocationsFamilialesException(
                    "Unable to search AllocationsFamiliales, the search model passed is null!");
        }
        return (AllocationsFamilialesSearch) JadePersistenceManager.search(allocationsFamilialesSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AllocationsFamilialesService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .AllocationsFamiliales)
     */
    @Override
    public AllocationsFamiliales update(AllocationsFamiliales allocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException, DonneeFinanciereException {
        if (allocationsFamiliales == null) {
            throw new AllocationsFamilialesException(
                    "Unable to update AllocationsFamiliales, the model passed is null!");
        }

        try {
            allocationsFamiliales.setSimpleAllocationsFamiliales(PegasusImplServiceLocator
                    .getSimpleAllocationsFamilialesService().update(
                            allocationsFamiliales.getSimpleAllocationsFamiliales()));
            allocationsFamiliales.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            allocationsFamiliales.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AllocationsFamilialesException("Service not available - " + e.getMessage());
        }

        return allocationsFamiliales;
    }

}
