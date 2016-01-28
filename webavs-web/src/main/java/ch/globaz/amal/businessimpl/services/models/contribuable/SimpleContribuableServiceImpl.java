/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.contribuable;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.models.contribuable.SimpleContribuable;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableSearch;
import ch.globaz.amal.business.services.models.contribuable.SimpleContribuableService;
import ch.globaz.amal.businessimpl.checkers.contribuable.SimpleContribuableChecker;

/**
 * @author CBU
 * 
 */
public class SimpleContribuableServiceImpl implements SimpleContribuableService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.contribuable.SimpleContribuableService#create(ch.globaz.amal.business
     * .models.contribuable.SimpleContribuable)
     */
    @Override
    public SimpleContribuable create(SimpleContribuable contribuable) throws JadePersistenceException,
            ContribuableException {
        if (contribuable == null) {
            throw new ContribuableException("Unable to create contribuable, the model passed is null!");
        }
        SimpleContribuableChecker.checkForCreate(contribuable);
        return (SimpleContribuable) JadePersistenceManager.add(contribuable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.contribuable.SimpleContribuableService#delete(ch.globaz.amal.business
     * .models.contribuable.SimpleContribuable)
     */
    @Override
    public SimpleContribuable delete(SimpleContribuable contribuable) throws ContribuableException,
            JadePersistenceException {
        if (contribuable == null) {
            throw new ContribuableException("Unable to delete contribuable, the model passed is null!");
        }
        if (contribuable.isNew()) {
            throw new ContribuableException("Unable to delete contribuable, the model passed is new!");
        }
        SimpleContribuableChecker.checkForDelete(contribuable);
        return (SimpleContribuable) JadePersistenceManager.delete(contribuable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.contribuable.SimpleContribuableService#read(java.lang.String)
     */
    @Override
    public SimpleContribuable read(String idContribuable) throws ContribuableException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idContribuable)) {
            throw new ContribuableException("Unable to read contribuable, the id passed is not defined!");
        }
        SimpleContribuable contribuable = new SimpleContribuable();
        contribuable.setId(idContribuable);
        return (SimpleContribuable) JadePersistenceManager.read(contribuable);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.contribuable.SimpleContribuableService#search(ch.globaz.amal.business
     * .models.contribuable.SimpleContribuableSearch)
     */
    @Override
    public SimpleContribuableSearch search(SimpleContribuableSearch search) throws ContribuableException,
            JadePersistenceException {
        if (search == null) {
            throw new ContribuableException("Unable to search simpleContribuable, the model passed is null!");
        }

        return (SimpleContribuableSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.contribuable.SimpleContribuableService#update(ch.globaz.amal.business
     * .models.contribuable.SimpleContribuable)
     */
    @Override
    public SimpleContribuable update(SimpleContribuable contribuable) throws ContribuableException,
            JadePersistenceException {
        if (contribuable == null) {
            throw new ContribuableException("Unable to update contribuable, the model passed is null!");
        }
        if (contribuable.isNew()) {
            throw new ContribuableException("Unable to update contribuable, the model passed is new!");
        }
        SimpleContribuableChecker.checkForUpdate(contribuable);
        return (SimpleContribuable) JadePersistenceManager.update(contribuable);
    }

}
