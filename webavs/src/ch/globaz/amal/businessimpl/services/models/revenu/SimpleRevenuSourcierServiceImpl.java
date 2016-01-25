/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.revenu;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSourcier;
import ch.globaz.amal.business.models.revenu.SimpleRevenuSourcierSearch;
import ch.globaz.amal.business.services.models.revenu.SimpleRevenuSourcierService;
import ch.globaz.amal.businessimpl.checkers.revenu.SimpleRevenuSourcierChecker;

/**
 * @author CBU
 * 
 */
public class SimpleRevenuSourcierServiceImpl implements SimpleRevenuSourcierService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuSourcierService#create(ch.globaz.amal.business.models
     * .revenu.SimpleRevenuSourcier)
     */
    @Override
    public SimpleRevenuSourcier create(SimpleRevenuSourcier simpleRevenuSourcier) throws JadePersistenceException,
            RevenuException, JadeApplicationServiceNotAvailableException {
        if (simpleRevenuSourcier == null) {
            throw new RevenuException("Unable to create simpleRevenuSourcier, the model passed is null!");
        }
        SimpleRevenuSourcierChecker.checkForCreate(simpleRevenuSourcier);
        return (SimpleRevenuSourcier) JadePersistenceManager.add(simpleRevenuSourcier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuSourcierService#delete(ch.globaz.amal.business.models
     * .revenu.SimpleRevenuSourcier)
     */
    @Override
    public SimpleRevenuSourcier delete(SimpleRevenuSourcier simpleRevenuSourcier) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (simpleRevenuSourcier == null) {
            throw new RevenuException("Unable to delete simpleRevenuSourcier, the model passed is null!");
        }
        SimpleRevenuSourcierChecker.checkForDelete(simpleRevenuSourcier);
        return (SimpleRevenuSourcier) JadePersistenceManager.delete(simpleRevenuSourcier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.SimpleRevenuSourcierService#read(java.lang.String)
     */
    @Override
    public SimpleRevenuSourcier read(String idRevenuSourcier) throws RevenuException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idRevenuSourcier)) {
            throw new RevenuException("Unable to read simplerevenu, the id passed is null!");
        }

        SimpleRevenuSourcier simpleRevenuSourcier = new SimpleRevenuSourcier();
        simpleRevenuSourcier.setId(idRevenuSourcier);
        return (SimpleRevenuSourcier) JadePersistenceManager.read(simpleRevenuSourcier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuSourcierService#search(ch.globaz.amal.business.models
     * .revenu.SimpleRevenuSourcierSearch)
     */
    @Override
    public SimpleRevenuSourcierSearch search(SimpleRevenuSourcierSearch search) throws JadePersistenceException,
            RevenuException {
        if (search == null) {
            throw new RevenuException("Unable to search simpleRevenuSourcier, the model passed is null!");
        }
        return (SimpleRevenuSourcierSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuSourcierService#update(ch.globaz.amal.business.models
     * .revenu.SimpleRevenuSourcier)
     */
    @Override
    public SimpleRevenuSourcier update(SimpleRevenuSourcier simpleRevenuSourcier) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (simpleRevenuSourcier == null) {
            throw new RevenuException("Unable to update simpleRevenuSourcier, the model passed is null!");
        }
        SimpleRevenuSourcierChecker.checkForUpdate(simpleRevenuSourcier);
        return (SimpleRevenuSourcier) JadePersistenceManager.update(simpleRevenuSourcier);
    }

}
