package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordeeSearch;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimplePCAccordeeService;
import ch.globaz.pegasus.businessimpl.checkers.pcaccordee.SimplePCAccordeeChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimplePCAccordeeServiceImpl extends PegasusAbstractServiceImpl implements SimplePCAccordeeService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePCAccordeeService
     * #count(ch.globaz.pegasus.business.models.pcaccordee .SimplePCAccordeeSearch)
     */
    @Override
    public int count(SimplePCAccordeeSearch search) throws PCAccordeeException, JadePersistenceException {
        if (search == null) {
            throw new PCAccordeeException("Unable to count simplePCAccordee, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePCAccordeeService
     * #create(ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee)
     */
    @Override
    public SimplePCAccordee create(SimplePCAccordee simplePCAccordee) throws PCAccordeeException,
            JadePersistenceException {
        if (simplePCAccordee == null) {
            throw new PCAccordeeException("Unable to create simplePCAccordee, the model passed is null!");
        }
        SimplePCAccordeeChecker.checkForCreate(simplePCAccordee);
        return (SimplePCAccordee) JadePersistenceManager.add(simplePCAccordee);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePCAccordeeService
     * #delete(ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee)
     */
    @Override
    public SimplePCAccordee delete(SimplePCAccordee simplePCAccordee) throws PCAccordeeException,
            JadePersistenceException {
        if (simplePCAccordee == null) {
            throw new PCAccordeeException("Unable to delete simplePCAccordee, the model passed is null!");
        }
        if (simplePCAccordee.isNew()) {
            throw new PCAccordeeException("Unable to delete simplePCAccordee, the model passed is new!");
        }
        SimplePCAccordeeChecker.checkForDelete(simplePCAccordee);
        return (SimplePCAccordee) JadePersistenceManager.delete(simplePCAccordee);
    }

    @Override
    public int delete(SimplePCAccordeeSearch simplePCAccordeeSearch) throws JadePersistenceException,
            PCAccordeeException {
        if (simplePCAccordeeSearch == null) {
            throw new PCAccordeeException("Unable to remove simplePCAccordee, the search model passed is null!");
        }
        return JadePersistenceManager.delete(simplePCAccordeeSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePCAccordeeService#read(java.lang.String)
     */
    @Override
    public SimplePCAccordee read(String idSimplePCAccordee) throws PCAccordeeException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idSimplePCAccordee)) {
            throw new PCAccordeeException("Unable to read simplePCAccordee, the id passed is not defined!");
        }
        SimplePCAccordee simplePCAccordee = new SimplePCAccordee();
        simplePCAccordee.setId(idSimplePCAccordee);
        return (SimplePCAccordee) JadePersistenceManager.read(simplePCAccordee);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePCAccordeeService
     * #search(ch.globaz.pegasus.business.models.pcaccordee .SimplePCAccordeeSearch)
     */
    @Override
    public SimplePCAccordeeSearch search(SimplePCAccordeeSearch simplePCAccordeeSearch)
            throws JadePersistenceException, PCAccordeeException {
        if (simplePCAccordeeSearch == null) {
            throw new PCAccordeeException("Unable to search simplePCAccordee, the search model passed is null!");
        }
        return (SimplePCAccordeeSearch) JadePersistenceManager.search(simplePCAccordeeSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.pcaccordee. SimplePCAccordeeService
     * #update(ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee)
     */
    @Override
    public SimplePCAccordee update(SimplePCAccordee simplePCAccordee) throws PCAccordeeException,
            JadePersistenceException {
        if (simplePCAccordee == null) {
            throw new PCAccordeeException("Unable to update simplePCAccordee, the model passed is null!");
        }
        if (simplePCAccordee.isNew()) {
            throw new PCAccordeeException("Unable to update simplePCAccordee, the model passed is new!");
        }
        SimplePCAccordeeChecker.checkForUpdate(simplePCAccordee);
        return (SimplePCAccordee) JadePersistenceManager.update(simplePCAccordee);
    }

}
