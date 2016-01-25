/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.primemoyenne;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.primemoyenne.PrimeMoyenneException;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenne;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenneSearch;
import ch.globaz.amal.business.services.models.primemoyenne.SimplePrimeMoyenneService;
import ch.globaz.amal.businessimpl.checkers.primemoyenne.SimplePrimeMoyenneChecker;

/**
 * @author CBU
 * 
 */
public class SimplePrimeMoyenneServiceImpl implements SimplePrimeMoyenneService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.primemoyenne.SimplePrimeMoyenneService#create(ch.globaz.amal.business
     * .models.primemoyenne.SimplePrimeMoyenne)
     */
    @Override
    public SimplePrimeMoyenne create(SimplePrimeMoyenne simplePrimeMoyenne) throws PrimeMoyenneException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (simplePrimeMoyenne == null) {
            throw new PrimeMoyenneException("Unable to create simplePrimeMoyenne, the search model passed is null!");
        }
        SimplePrimeMoyenneChecker.checkForCreate(simplePrimeMoyenne);
        return (SimplePrimeMoyenne) JadePersistenceManager.add(simplePrimeMoyenne);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.primemoyenne.SimplePrimeMoyenneService#delete(ch.globaz.amal.business
     * .models.primemoyenne.SimplePrimeMoyenne)
     */
    @Override
    public SimplePrimeMoyenne delete(SimplePrimeMoyenne simplePrimeMoyenne) throws JadePersistenceException,
            PrimeMoyenneException {
        if (simplePrimeMoyenne == null) {
            throw new PrimeMoyenneException("Unable to delete simplePrimeMoyenne, the model passed is null!");
        }
        SimplePrimeMoyenneChecker.checkForDelete(simplePrimeMoyenne);
        return (SimplePrimeMoyenne) JadePersistenceManager.delete(simplePrimeMoyenne);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.primemoyenne.SimplePrimeMoyenneService#read(java.lang.String)
     */
    @Override
    public SimplePrimeMoyenne read(String idPrimeMoyenne) throws JadePersistenceException, PrimeMoyenneException {
        if (JadeStringUtil.isEmpty(idPrimeMoyenne)) {
            throw new PrimeMoyenneException("Unable to read primeMoyenne, the id passed is not defined!");
        }
        SimplePrimeMoyenne primeMoyenne = new SimplePrimeMoyenne();
        primeMoyenne.setId(idPrimeMoyenne);
        return (SimplePrimeMoyenne) JadePersistenceManager.read(primeMoyenne);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.primemoyenne.SimplePrimeMoyenneService#search(ch.globaz.amal.business
     * .models.primemoyenne.SimplePrimeMoyenneSearch)
     */
    @Override
    public SimplePrimeMoyenneSearch search(SimplePrimeMoyenneSearch primeMoyenneSearch)
            throws JadePersistenceException, PrimeMoyenneException {
        if (primeMoyenneSearch == null) {
            throw new PrimeMoyenneException("Unable to search primeMoyenne, the search model passed is null!");
        }
        return (SimplePrimeMoyenneSearch) JadePersistenceManager.search(primeMoyenneSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.primemoyenne.SimplePrimeMoyenneService#update(ch.globaz.amal.business
     * .models.primemoyenne.SimplePrimeMoyenne)
     */
    @Override
    public SimplePrimeMoyenne update(SimplePrimeMoyenne simplePrimeMoyenne) throws PrimeMoyenneException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (simplePrimeMoyenne == null) {
            throw new PrimeMoyenneException("Unable to update simplePrimeMoyenne, the search model passed is null!");
        }
        SimplePrimeMoyenneChecker.checkForUpdate(simplePrimeMoyenne);
        return (SimplePrimeMoyenne) JadePersistenceManager.update(simplePrimeMoyenne);
    }

}
