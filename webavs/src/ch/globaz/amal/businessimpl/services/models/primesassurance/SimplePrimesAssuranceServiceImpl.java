/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.primesassurance;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.primesassurance.PrimesAssuranceException;
import ch.globaz.amal.business.models.primesassurance.SimplePrimesAssurance;
import ch.globaz.amal.business.models.primesassurance.SimplePrimesAssuranceSearch;
import ch.globaz.amal.business.services.models.primesassurance.SimplePrimesAssuranceService;

/**
 * @author cbu
 * 
 */
public class SimplePrimesAssuranceServiceImpl implements SimplePrimesAssuranceService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.primesassurance.SimplePrimesAssuranceService#create(ch.globaz.amal.business
     * .models.primesassurance.SimplePrimesAssurance)
     */
    @Override
    public SimplePrimesAssurance create(SimplePrimesAssurance simplePrimesAssurance) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PrimesAssuranceException, DetailFamilleException {
        if (simplePrimesAssurance == null) {
            throw new PrimesAssuranceException("Unable to create simplePrimesAssurance, the model passed is null!");
        }
        // SimpleAnnonceChecker.checkForCreate(simplePrimesAssurance);
        return (SimplePrimesAssurance) JadePersistenceManager.add(simplePrimesAssurance);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.primesassurance.SimplePrimesAssuranceService#delete(ch.globaz.amal.business
     * .models.primesassurance.SimplePrimesAssurance)
     */
    @Override
    public SimplePrimesAssurance delete(SimplePrimesAssurance simplePrimesAssurance) throws JadePersistenceException,
            PrimesAssuranceException, DetailFamilleException, JadeApplicationServiceNotAvailableException {
        if (simplePrimesAssurance == null) {
            throw new PrimesAssuranceException("Unable to delete simplePrimesAssurance, the model passed is null!");
        }
        // SimpleAnnonceChecker.checkForCreate(simplePrimesAssurance);
        return (SimplePrimesAssurance) JadePersistenceManager.delete(simplePrimesAssurance);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.business.services.models.primesassurance.SimplePrimesAssuranceService#read(java.lang.String)
     */
    @Override
    public SimplePrimesAssurance read(String idPrimesAssurance) throws JadePersistenceException,
            PrimesAssuranceException {
        if (JadeStringUtil.isEmpty(idPrimesAssurance)) {
            throw new PrimesAssuranceException("Unable to read the prime assurance, id passed is empty");
        }
        SimplePrimesAssurance simplePrimesAssurance = new SimplePrimesAssurance();
        simplePrimesAssurance.setId(idPrimesAssurance);
        return (SimplePrimesAssurance) JadePersistenceManager.read(simplePrimesAssurance);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.primesassurance.SimplePrimesAssuranceService#search(ch.globaz.amal.business
     * .models.primesassurance.SimplePrimesAssuranceSearch)
     */
    @Override
    public SimplePrimesAssuranceSearch search(SimplePrimesAssuranceSearch search) throws JadePersistenceException,
            PrimesAssuranceException {
        if (search == null) {
            throw new PrimesAssuranceException("Unable to search simplePrimesAssurance, the model passed is null!");
        }
        // SimpleAnnonceChecker.checkForCreate(simplePrimesAssurance);
        return (SimplePrimesAssuranceSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.primesassurance.SimplePrimesAssuranceService#update(ch.globaz.amal.business
     * .models.primesassurance.SimplePrimesAssurance)
     */
    @Override
    public SimplePrimesAssurance update(SimplePrimesAssurance simplePrimesAssurance) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PrimesAssuranceException, DetailFamilleException {
        if (simplePrimesAssurance == null) {
            throw new PrimesAssuranceException("Unable to update simplePrimesAssurance, the model passed is null!");
        }
        // SimpleAnnonceChecker.checkForCreate(simplePrimesAssurance);
        return (SimplePrimesAssurance) JadePersistenceManager.update(simplePrimesAssurance);
    }

}
