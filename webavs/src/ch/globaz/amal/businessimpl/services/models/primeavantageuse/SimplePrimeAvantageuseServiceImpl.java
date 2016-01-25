/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.primeavantageuse;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.primeavantageuse.PrimeAvantageuseException;
import ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuse;
import ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuseSearch;
import ch.globaz.amal.business.services.models.primeavantageuse.SimplePrimeAvantageuseService;
import ch.globaz.amal.businessimpl.checkers.primeavantageuse.SimplePrimeAvantageuseChecker;

/**
 * @author CBU
 * 
 */
public class SimplePrimeAvantageuseServiceImpl implements SimplePrimeAvantageuseService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.primeavantageuse.SimplePrimeAvantageuseService#create(ch.globaz.amal.business
     * .models.primeavantageuse.SimplePrimeAvantageuse)
     */
    @Override
    public SimplePrimeAvantageuse create(SimplePrimeAvantageuse simplePrimeAvantageuse)
            throws PrimeAvantageuseException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (simplePrimeAvantageuse == null) {
            throw new PrimeAvantageuseException(
                    "Unable to create simplePrimeAvantageuse, the search model passed is null!");
        }
        SimplePrimeAvantageuseChecker.checkForCreate(simplePrimeAvantageuse);
        return (SimplePrimeAvantageuse) JadePersistenceManager.add(simplePrimeAvantageuse);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.primeavantageuse.SimplePrimeAvantageuseService#delete(ch.globaz.amal.business
     * .models.primeavantageuse.SimplePrimeAvantageuse)
     */
    @Override
    public SimplePrimeAvantageuse delete(SimplePrimeAvantageuse simplePrimeAvantageuse)
            throws JadePersistenceException, PrimeAvantageuseException {
        if (simplePrimeAvantageuse == null) {
            throw new PrimeAvantageuseException("Unable to delete simplePrimeAvantageuse, the model passed is null!");
        }
        SimplePrimeAvantageuseChecker.checkForDelete(simplePrimeAvantageuse);
        return (SimplePrimeAvantageuse) JadePersistenceManager.delete(simplePrimeAvantageuse);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.primeavantageuse.SimplePrimeAvantageuseService#read(java.lang.String)
     */
    @Override
    public SimplePrimeAvantageuse read(String idPrimeAvantageuse) throws JadePersistenceException,
            PrimeAvantageuseException {
        if (JadeStringUtil.isEmpty(idPrimeAvantageuse)) {
            throw new PrimeAvantageuseException("Unable to read primeAvantageuse, the id passed is not defined!");
        }
        SimplePrimeAvantageuse primeAvantageuse = new SimplePrimeAvantageuse();
        primeAvantageuse.setId(idPrimeAvantageuse);
        return (SimplePrimeAvantageuse) JadePersistenceManager.read(primeAvantageuse);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.primeavantageuse.SimplePrimeAvantageuseService#search(ch.globaz.amal.business
     * .models.primeavantageuse.SimplePrimeAvantageuseSearch)
     */
    @Override
    public SimplePrimeAvantageuseSearch search(SimplePrimeAvantageuseSearch primeAvantageuseSearch)
            throws JadePersistenceException, PrimeAvantageuseException {
        if (primeAvantageuseSearch == null) {
            throw new PrimeAvantageuseException("Unable to search primeAvantageuse, the search model passed is null!");
        }
        return (SimplePrimeAvantageuseSearch) JadePersistenceManager.search(primeAvantageuseSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.primeavantageuse.SimplePrimeAvantageuseService#update(ch.globaz.amal.business
     * .models.primeavantageuse.SimplePrimeAvantageuse)
     */
    @Override
    public SimplePrimeAvantageuse update(SimplePrimeAvantageuse simplePrimeAvantageuse)
            throws PrimeAvantageuseException, JadePersistenceException, JadeApplicationServiceNotAvailableException {
        if (simplePrimeAvantageuse == null) {
            throw new PrimeAvantageuseException(
                    "Unable to update simplePrimeAvantageuse, the search model passed is null!");
        }
        SimplePrimeAvantageuseChecker.checkForUpdate(simplePrimeAvantageuse);
        return (SimplePrimeAvantageuse) JadePersistenceManager.update(simplePrimeAvantageuse);
    }

}
