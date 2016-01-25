/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.revenu;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuContribuable;
import ch.globaz.amal.business.models.revenu.SimpleRevenuContribuableSearch;
import ch.globaz.amal.business.services.models.revenu.SimpleRevenuContribuableService;
import ch.globaz.amal.businessimpl.checkers.revenu.SimpleRevenuContribuableChecker;

/**
 * @author CBU
 * 
 */
public class SimpleRevenuContribuableServiceImpl implements SimpleRevenuContribuableService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuContribuableService#create(ch.globaz.amal.business
     * .models.revenu.SimpleRevenuContribuable)
     */
    @Override
    public SimpleRevenuContribuable create(SimpleRevenuContribuable simpleRevenuContribuable)
            throws JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException {
        if (simpleRevenuContribuable == null) {
            throw new RevenuException("Unable to create simpleRevenuContribuable, the model passed is null!");
        }
        SimpleRevenuContribuableChecker.checkForCreate(simpleRevenuContribuable);
        return (SimpleRevenuContribuable) JadePersistenceManager.add(simpleRevenuContribuable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuContribuableService#delete(ch.globaz.amal.business
     * .models.revenu.SimpleRevenuContribuable)
     */
    @Override
    public SimpleRevenuContribuable delete(SimpleRevenuContribuable simpleRevenuContribuable) throws RevenuException,
            JadePersistenceException {
        if (simpleRevenuContribuable == null) {
            throw new RevenuException("Unable to delete simpleRevenuContribuable, the model passed is null!");
        }
        SimpleRevenuContribuableChecker.checkForDelete(simpleRevenuContribuable);
        return (SimpleRevenuContribuable) JadePersistenceManager.delete(simpleRevenuContribuable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.SimpleRevenuContribuableService#read(java.lang.String)
     */
    @Override
    public SimpleRevenuContribuable read(String idRevenuContribuable) throws RevenuException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idRevenuContribuable)) {
            throw new RevenuException("Unable to read simplerevenu, the id passed is null!");
        }
        SimpleRevenuContribuable simpleRevenuContribuable = new SimpleRevenuContribuable();
        simpleRevenuContribuable.setId(idRevenuContribuable);
        return (SimpleRevenuContribuable) JadePersistenceManager.read(simpleRevenuContribuable);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuContribuableService#search(ch.globaz.amal.business
     * .models.revenu.SimpleRevenuContribuableSearch)
     */
    @Override
    public SimpleRevenuContribuableSearch search(SimpleRevenuContribuableSearch search)
            throws JadePersistenceException, RevenuException {
        if (search == null) {
            throw new RevenuException("Unable to search simpleRevenuContribuable, the model passed is null!");
        }
        return (SimpleRevenuContribuableSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuContribuableService#update(ch.globaz.amal.business
     * .models.revenu.SimpleRevenuContribuable)
     */
    @Override
    public SimpleRevenuContribuable update(SimpleRevenuContribuable simpleRevenuContribuable) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (simpleRevenuContribuable == null) {
            throw new RevenuException("Unable to update simpleRevenuContribuable, the model passed is null!");
        }
        SimpleRevenuContribuableChecker.checkForUpdate(simpleRevenuContribuable);
        return (SimpleRevenuContribuable) JadePersistenceManager.update(simpleRevenuContribuable);
    }

}
