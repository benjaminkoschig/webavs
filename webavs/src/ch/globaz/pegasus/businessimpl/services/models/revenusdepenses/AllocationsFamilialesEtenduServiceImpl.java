package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AllocationsFamilialesException;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamilialesEtendu;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamilialesEtenduSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.AllocationsFamilialesEtenduService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class AllocationsFamilialesEtenduServiceImpl extends PegasusAbstractServiceImpl implements
        AllocationsFamilialesEtenduService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AllocationsFamilialesService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .AllocationsFamilialesSearch)
     */
    @Override
    public int count(AllocationsFamilialesEtenduSearch search) throws AllocationsFamilialesException,
            JadePersistenceException {
        if (search == null) {
            throw new AllocationsFamilialesException(
                    "Unable to count AllocationsFamilialesEtendu, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses.
     * AllocationsFamilialesService#read(java.lang.String)
     */
    @Override
    public AllocationsFamilialesEtendu read(String idAllocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException {
        if (JadeStringUtil.isEmpty(idAllocationsFamiliales)) {
            throw new AllocationsFamilialesException("Unable to read AllocationsFamiliales, the id passed is null!");
        }
        AllocationsFamilialesEtendu allocationsFamiliales = new AllocationsFamilialesEtendu();
        allocationsFamiliales.setId(idAllocationsFamiliales);
        return (AllocationsFamilialesEtendu) JadePersistenceManager.read(allocationsFamiliales);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. AllocationsFamilialesService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .AllocationsFamilialesSearch)
     */
    @Override
    public AllocationsFamilialesEtenduSearch search(AllocationsFamilialesEtenduSearch allocationsFamilialesSearch)
            throws JadePersistenceException, AllocationsFamilialesException {
        if (allocationsFamilialesSearch == null) {
            throw new AllocationsFamilialesException(
                    "Unable to search AllocationsFamilialesEtendu, the search model passed is null!");
        }
        return (AllocationsFamilialesEtenduSearch) JadePersistenceManager.search(allocationsFamilialesSearch);
    }

}
