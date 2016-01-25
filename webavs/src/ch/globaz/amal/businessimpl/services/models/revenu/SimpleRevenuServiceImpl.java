/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.revenu;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenu;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSearch;
import ch.globaz.amal.business.services.models.revenu.SimpleRevenuService;
import ch.globaz.amal.businessimpl.checkers.revenu.SimpleRevenuChecker;

/**
 * @author CBU
 * 
 */
public class SimpleRevenuServiceImpl implements SimpleRevenuService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuService#create(ch.globaz.amal.business.models.revenu
     * .SimpleRevenu)
     */
    @Override
    public SimpleRevenu create(SimpleRevenu simpleRevenu) throws JadePersistenceException, RevenuException,
            JadeApplicationServiceNotAvailableException {
        if (simpleRevenu == null) {
            throw new RevenuException("Unable to create simpleRevenu, the model passed is null!");
        }
        SimpleRevenuChecker.checkForCreate(simpleRevenu);
        return (SimpleRevenu) JadePersistenceManager.add(simpleRevenu);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuService#delete(ch.globaz.amal.business.models.revenu
     * .SimpleRevenu)
     */
    @Override
    public SimpleRevenu delete(SimpleRevenu simpleRevenu) throws RevenuException, JadePersistenceException {
        if (simpleRevenu == null) {
            throw new RevenuException("Unable to delete simpleRevenu, the model passed is null!");
        }
        SimpleRevenuChecker.checkForDelete(simpleRevenu);
        return (SimpleRevenu) JadePersistenceManager.delete(simpleRevenu);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.SimpleRevenuService#read(java.lang.String)
     */
    @Override
    public SimpleRevenu read(String idRevenu) throws RevenuException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idRevenu)) {
            throw new RevenuException("Unable to read simplerevenu, the id passed is null!");
        }
        SimpleRevenu simpleRevenu = new SimpleRevenu();
        simpleRevenu.setId(idRevenu);
        return (SimpleRevenu) JadePersistenceManager.read(simpleRevenu);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuService#search(ch.globaz.amal.business.models.revenu
     * .SimpleRevenu)
     */
    @Override
    public SimpleRevenuSearch search(SimpleRevenuSearch search) throws RevenuException, JadePersistenceException {
        if (search == null) {
            throw new RevenuException("Unable to search simpleRevenu, the model passed is null!");
        }
        return (SimpleRevenuSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuService#update(ch.globaz.amal.business.models.revenu
     * .SimpleRevenu)
     */
    @Override
    public SimpleRevenu update(SimpleRevenu simpleRevenu) throws RevenuException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        if (simpleRevenu == null) {
            throw new RevenuException("Unable to update simpleRevenu, the model passed is null!");
        }
        if (simpleRevenu.isNew()) {
            throw new RevenuException("Unable to update simpleRevenu, the model passed is new!");
        }
        SimpleRevenuChecker.checkForUpdate(simpleRevenu);

        return (SimpleRevenu) JadePersistenceManager.update(simpleRevenu);
    }

}
