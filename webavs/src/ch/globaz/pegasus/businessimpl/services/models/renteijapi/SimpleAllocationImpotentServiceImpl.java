package ch.globaz.pegasus.businessimpl.services.models.renteijapi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AllocationImpotentException;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotent;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAllocationImpotent;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAllocationImpotentSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.renteijapi.SimpleAllocationImpotentService;
import ch.globaz.pegasus.businessimpl.checkers.renteijapi.SimpleAllocationImpotentChecker;

public class SimpleAllocationImpotentServiceImpl extends PegasusServiceLocator implements
        SimpleAllocationImpotentService {

    /**
     * Création d'une rente allocation impotent
     * 
     * @param allocationImpotent
     *            l' allocationImpotent à créer
     * @throws AllocationImpotentException
     * @return allocationImpotent a crée
     */
    @Override
    public SimpleAllocationImpotent create(SimpleAllocationImpotent allocationImpotent)
            throws AllocationImpotentException, JadePersistenceException {
        if (allocationImpotent == null) {
            throw new AllocationImpotentException("Unable to create allocationImpotent, the model passed is null!");
        }
        SimpleAllocationImpotentChecker.checkForCreate(allocationImpotent);

        return (SimpleAllocationImpotent) JadePersistenceManager.add(allocationImpotent);
    }

    /**
     * Suppression d'une allocation impotent
     * 
     * @param allocationImpotent
     *            l'allocation impotent à supprimer
     * @throws AllocationImpotent
     *             Exception
     * @return allocationImpotent a supprimé
     */
    @Override
    public SimpleAllocationImpotent delete(SimpleAllocationImpotent allocationImpotent)
            throws AllocationImpotentException, JadePersistenceException {
        // Check si model pas null
        if (allocationImpotent == null) {
            throw new AllocationImpotentException("Unable to delete allocationImpotent, the model passed is null!");
        }
        // check si model pas new
        if (allocationImpotent.isNew()) {
            throw new AllocationImpotentException("Unable to delelte allocationImpotent, the model passed is new!");
        }
        SimpleAllocationImpotentChecker.checkForDelete(allocationImpotent);
        return (SimpleAllocationImpotent) JadePersistenceManager.delete(allocationImpotent);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.renteijapi.
     * SimpleAllocationImpotentService#deleteParListeIdDoFinH(java.lang.String)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleAllocationImpotentSearch search = new SimpleAllocationImpotentSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);

    }

    /**
     * Chargement d'une allocation impotent
     * 
     * @param idAllocationImpotent
     *            l'identifiant d'allocation à charger
     * @throws AllocationImpotentException
     * @return allocationImpotent a charger
     */
    @Override
    public SimpleAllocationImpotent read(String idAllocationImpotent) throws AllocationImpotentException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idAllocationImpotent)) {
            throw new AllocationImpotentException("Unable to read the model, the id passed is null!");
        }
        SimpleAllocationImpotent simpleAllocationImpotent = new SimpleAllocationImpotent();
        simpleAllocationImpotent.setId(idAllocationImpotent);

        return (SimpleAllocationImpotent) JadePersistenceManager.read(simpleAllocationImpotent);
    }

/**
	 * Mise à jour de l'allocation impotent
	 * 
	 * @throws AllocationImpotentException, {@link JadePersistenceException
	 * @param allocationImpotent la rente à mettre à jour
	 * @return AllocationImpotentException la rente mise à jour
	 * 
	 */
    @Override
    public SimpleAllocationImpotent update(SimpleAllocationImpotent allocationImpotent)
            throws AllocationImpotentException, JadePersistenceException {
        // check si model pas null
        if (allocationImpotent == null) {
            throw new AllocationImpotentException("Unable to update the allocationImpotent, the model passed is null!");
        }
        // check si model pas new
        if (allocationImpotent.isNew()) {
            throw new AllocationImpotentException("Unable to update allocationImpotent, the model passed is new!");
        }
        SimpleAllocationImpotentChecker.checkForUpdate(allocationImpotent);
        return (SimpleAllocationImpotent) JadePersistenceManager.update(allocationImpotent);
    }

}
