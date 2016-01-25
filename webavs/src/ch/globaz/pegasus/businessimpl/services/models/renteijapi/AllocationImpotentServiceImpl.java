package ch.globaz.pegasus.businessimpl.services.models.renteijapi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AllocationImpotentException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotent;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotentSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.renteijapi.AllocationImpotentService;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * service pour la gestion des allocations impotents
 * 
 * @author SCE
 * 
 */
public class AllocationImpotentServiceImpl extends PegasusServiceLocator implements AllocationImpotentService {

    /**
     * Compte les entités en bd
     * 
     * @param serach
     *            le modèle de recherche
     * @throws AllocationImpotenetException
     *             , JadePersistenceException
     * @return le nombre d'entité en bd
     */
    @Override
    public int count(AllocationImpotentSearch search) throws AllocationImpotentException, JadePersistenceException {
        if (search == null) {
            throw new AllocationImpotentException("Unable to count, the search model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    /**
     * création d'une entité allocation impotent
     */
    @Override
    public AllocationImpotent create(AllocationImpotent allocationImpotent) throws AllocationImpotentException,
            DonneeFinanciereException, JadePersistenceException {
        if (allocationImpotent == null) {
            throw new AllocationImpotentException("Unable to create allocationImpotent, the model passed is null");
        }

        try {
            // Création allocationImpotent
            allocationImpotent.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            allocationImpotent.getSimpleDonneeFinanciereHeader()));
            // Creation simpleRenteAvsAi
            allocationImpotent.setSimpleAllocationImpotent(PegasusImplServiceLocator
                    .getSimpleAllocationImpotentService().create(allocationImpotent.getSimpleAllocationImpotent()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AllocationImpotentException("Service not available - " + e.getMessage());
        }
        return allocationImpotent;
    }

    /**
     * Supression d'une entité allocation Impotenet
     * 
     * @param allocationImpotent
     *            l'allocation à supprimer
     * @return l'allocation impotent supprimé
     * @throws AllocationImpotentException
     *             , JadePersistenceException
     * 
     */
    @Override
    public AllocationImpotent delete(AllocationImpotent allocationImpotent) throws AllocationImpotentException,
            JadePersistenceException {
        // check si pas null
        if (allocationImpotent == null) {
            throw new AllocationImpotentException("unable to delete allocationImpotent, the model passed is null");
        }

        try {
            PegasusImplServiceLocator.getSimpleAllocationImpotentService().delete(
                    allocationImpotent.getSimpleAllocationImpotent());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AllocationImpotentException("Service not available - " + e.getMessage());
        }
        return allocationImpotent;
    }

    /**
     * Charge une entité allocationImpotent
     * 
     * @param idAllocationImpotent
     * @return l'allocation chargé
     * @throws AllocationImpotentException
     *             , JadePersistenceException
     * 
     */
    @Override
    public AllocationImpotent read(String idAllocationImpotent) throws AllocationImpotentException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idAllocationImpotent)) {
            throw new AllocationImpotentException("Unable to read the model, the id passed is null");
        }

        AllocationImpotent allocationImpotent = new AllocationImpotent();
        allocationImpotent.setId(idAllocationImpotent);
        return (AllocationImpotent) JadePersistenceManager.read(allocationImpotent);
    }

    /**
     * Chargement d'une allocation impotenet via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AllocationImpotentException
     * @throws JadePersistenceException
     */
    @Override
    public AllocationImpotent readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws AllocationImpotentException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new AllocationImpotentException(
                    "Unable to find AllocationImpotent the idDonneeFinanciereHeader passed si null!");
        }

        AllocationImpotentSearch search = new AllocationImpotentSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (AllocationImpotentSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new AllocationImpotentException("More than one AllocationImpotent find, one was exepcted!");
        }

        return (AllocationImpotent) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((AllocationImpotentSearch) donneeFinanciereSearch);
    }

    /**
     * Recherche en fonction des paramètres de recherche
     * 
     * @param allocationImpotentSearch
     * @return search
     * @throws JadePersistenceException
     * @throws AllocationImpotentException
     * 
     */
    @Override
    public AllocationImpotentSearch search(AllocationImpotentSearch allocationImpotentSearch)
            throws JadePersistenceException, AllocationImpotentException {
        if (allocationImpotentSearch == null) {
            throw new AllocationImpotentException(
                    "Unable to search allocationImpotent, the search model passed is null!");
        }
        return (AllocationImpotentSearch) JadePersistenceManager.search(allocationImpotentSearch);
    }

    /**
     * Mise à jour d'une entité
     * 
     * @param allocationImpotent
     * @return l'allocation mise à jour
     * @throws AllocationImpotentException
     *             , JadePersistenceException, DonneeFinanciereException
     */
    @Override
    public AllocationImpotent update(AllocationImpotent allocationImpotent) throws AllocationImpotentException,
            DonneeFinanciereException, JadePersistenceException {
        if (allocationImpotent == null) {
            throw new AllocationImpotentException("Unable to update the allocationImpotent, the model passed is null");
        }

        try {
            // Update simpleDonneesFinancieresHeader
            allocationImpotent.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            allocationImpotent.getSimpleDonneeFinanciereHeader()));
            // Update allocationImpotent
            allocationImpotent.setSimpleAllocationImpotent(PegasusImplServiceLocator
                    .getSimpleAllocationImpotentService().update(allocationImpotent.getSimpleAllocationImpotent()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AllocationImpotentException("Service not available - " + e.getMessage());
        }

        return allocationImpotent;
    }

}
