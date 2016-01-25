/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.revenu;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.SimpleRevenuDeterminant;
import ch.globaz.amal.business.models.revenu.SimpleRevenuDeterminantSearch;
import ch.globaz.amal.business.services.models.revenu.SimpleRevenuDeterminantService;
import ch.globaz.amal.businessimpl.checkers.revenu.SimpleRevenuDeterminantChecker;

/**
 * @author dhi
 * 
 */
public class SimpleRevenuDeterminantServiceImpl implements SimpleRevenuDeterminantService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuDeterminantService#create(ch.globaz.amal.business.
     * models.revenu.SimpleRevenuDeterminant)
     */
    @Override
    public SimpleRevenuDeterminant create(SimpleRevenuDeterminant simpleRevenuDeterminant)
            throws JadePersistenceException, RevenuException, JadeApplicationServiceNotAvailableException {
        if (simpleRevenuDeterminant == null) {
            throw new RevenuException("Unable to create simpleRevenuDeterminant, the model passed is null!");
        }
        SimpleRevenuDeterminantChecker.checkForCreate(simpleRevenuDeterminant);
        return (SimpleRevenuDeterminant) JadePersistenceManager.add(simpleRevenuDeterminant);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuDeterminantService#delete(ch.globaz.amal.business.
     * models.revenu.SimpleRevenuDeterminant)
     */
    @Override
    public SimpleRevenuDeterminant delete(SimpleRevenuDeterminant simpleRevenuDeterminant) throws RevenuException,
            JadePersistenceException {
        if (simpleRevenuDeterminant == null) {
            throw new RevenuException("Unable to delete simpleRevenuDeterminant, the model passed is null!");
        }
        SimpleRevenuDeterminantChecker.checkForDelete(simpleRevenuDeterminant);
        return (SimpleRevenuDeterminant) JadePersistenceManager.update(simpleRevenuDeterminant);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.revenu.SimpleRevenuDeterminantService#read(java.lang.String)
     */
    @Override
    public SimpleRevenuDeterminant read(String idRevenu) throws RevenuException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idRevenu)) {
            throw new RevenuException("Unable to read simplerevenuDeterminant, the id passed is null!");
        }
        SimpleRevenuDeterminant simpleRevenuDeterminant = new SimpleRevenuDeterminant();
        simpleRevenuDeterminant.setId(idRevenu);
        return (SimpleRevenuDeterminant) JadePersistenceManager.read(simpleRevenuDeterminant);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuDeterminantService#search(ch.globaz.amal.business.
     * models.revenu.SimpleRevenuDeterminantSearch)
     */
    @Override
    public SimpleRevenuDeterminantSearch search(SimpleRevenuDeterminantSearch search) throws JadePersistenceException,
            RevenuException {
        if (search == null) {
            throw new RevenuException("Unable to search simpleRevenuDeterminant, the model passed is null!");
        }
        return (SimpleRevenuDeterminantSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.revenu.SimpleRevenuDeterminantService#update(ch.globaz.amal.business.
     * models.revenu.SimpleRevenuDeterminant)
     */
    @Override
    public SimpleRevenuDeterminant update(SimpleRevenuDeterminant simpleRevenuDeterminant) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (simpleRevenuDeterminant == null) {
            throw new RevenuException("Unable to update simpleRevenuDeterminant, the model passed is null!");
        }
        SimpleRevenuDeterminantChecker.checkForUpdate(simpleRevenuDeterminant);
        return (SimpleRevenuDeterminant) JadePersistenceManager.update(simpleRevenuDeterminant);
    }

}
