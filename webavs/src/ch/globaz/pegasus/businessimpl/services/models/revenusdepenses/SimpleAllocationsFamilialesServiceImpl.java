package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AllocationsFamilialesException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleAllocationsFamiliales;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleAllocationsFamilialesSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.SimpleAllocationsFamilialesService;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.SimpleAllocationsFamilialesChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleAllocationsFamilialesServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleAllocationsFamilialesService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleAllocationsFamilialesService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleAllocationsFamiliales)
     */
    @Override
    public SimpleAllocationsFamiliales create(SimpleAllocationsFamiliales simpleAllocationsFamiliales)
            throws JadePersistenceException, AllocationsFamilialesException {
        if (simpleAllocationsFamiliales == null) {
            throw new AllocationsFamilialesException(
                    "Unable to create simpleAllocationsFamiliales, the model passed is null!");
        }
        SimpleAllocationsFamilialesChecker.checkForCreate(simpleAllocationsFamiliales);
        return (SimpleAllocationsFamiliales) JadePersistenceManager.add(simpleAllocationsFamiliales);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleAllocationsFamilialesService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleAllocationsFamiliales)
     */
    @Override
    public SimpleAllocationsFamiliales delete(SimpleAllocationsFamiliales simpleAllocationsFamiliales)
            throws AllocationsFamilialesException, JadePersistenceException {
        if (simpleAllocationsFamiliales == null) {
            throw new AllocationsFamilialesException(
                    "Unable to delete simpleAllocationsFamiliales, the model passed is null!");
        }
        if (simpleAllocationsFamiliales.isNew()) {
            throw new AllocationsFamilialesException(
                    "Unable to delete simpleAllocationsFamiliales, the model passed is new!");
        }
        SimpleAllocationsFamilialesChecker.checkForDelete(simpleAllocationsFamiliales);
        return (SimpleAllocationsFamiliales) JadePersistenceManager.delete(simpleAllocationsFamiliales);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleAllocationsFamilialesSearch search = new SimpleAllocationsFamilialesSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleAllocationsFamilialesService#read(java.lang.String)
     */
    @Override
    public SimpleAllocationsFamiliales read(String idAllocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException {
        if (JadeStringUtil.isEmpty(idAllocationsFamiliales)) {
            throw new AllocationsFamilialesException(
                    "Unable to read simpleAllocationsFamiliales, the id passed is not defined!");
        }
        SimpleAllocationsFamiliales simpleAllocationsFamiliales = new SimpleAllocationsFamiliales();
        simpleAllocationsFamiliales.setId(idAllocationsFamiliales);
        return (SimpleAllocationsFamiliales) JadePersistenceManager.read(simpleAllocationsFamiliales);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleAllocationsFamilialesService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleAllocationsFamiliales)
     */
    @Override
    public SimpleAllocationsFamiliales update(SimpleAllocationsFamiliales simpleAllocationsFamiliales)
            throws JadePersistenceException, AllocationsFamilialesException {
        if (simpleAllocationsFamiliales == null) {
            throw new AllocationsFamilialesException(
                    "Unable to update simpleAllocationsFamiliales, the model passed is null!");
        }
        if (simpleAllocationsFamiliales.isNew()) {
            throw new AllocationsFamilialesException(
                    "Unable to update simpleAllocationsFamiliales, the model passed is new!");
        }
        SimpleAllocationsFamilialesChecker.checkForUpdate(simpleAllocationsFamiliales);
        return (SimpleAllocationsFamiliales) JadePersistenceManager.update(simpleAllocationsFamiliales);
    }

}
