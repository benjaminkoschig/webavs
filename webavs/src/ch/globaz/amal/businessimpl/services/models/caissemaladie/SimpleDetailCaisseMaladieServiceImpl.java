/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.caissemaladie;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.amal.business.exceptions.models.CaisseMaladieException;
import ch.globaz.amal.business.models.caissemaladie.SimpleDetailCaisseMaladie;
import ch.globaz.amal.business.models.caissemaladie.SimpleDetailCaisseMaladieSearch;
import ch.globaz.amal.business.services.models.caissemaladie.SimpleDetailCaisseMaladieService;
import ch.globaz.amal.businessimpl.checkers.caissemaladie.SimpleDetailCaisseMaladieChecker;

/**
 * @author cbu
 * 
 */
public class SimpleDetailCaisseMaladieServiceImpl implements SimpleDetailCaisseMaladieService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.caissemaladie.SimpleDetailCaisseMaladieService#count(ch.globaz.amal.business
     * .models.caissemaladie.SimpleDetailCaisseMaladieSearch)
     */
    @Override
    public int count(SimpleDetailCaisseMaladieSearch simpleDetailCaisseMaladieSearch) throws CaisseMaladieException,
            JadePersistenceException {
        if (simpleDetailCaisseMaladieSearch == null) {
            throw new CaisseMaladieException("Unable to count simpleDetailCaisseMaladie, the model passed is null!");
        }
        return JadePersistenceManager.count(simpleDetailCaisseMaladieSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.caissemaladie.SimpleDetailCaisseMaladieService#create(ch.globaz.amal.
     * business.models.caissemaladie.SimpleDetailCaisseMaladie)
     */
    @Override
    public SimpleDetailCaisseMaladie create(SimpleDetailCaisseMaladie simpleDetailCaisseMaladie)
            throws CaisseMaladieException, JadePersistenceException {
        if (simpleDetailCaisseMaladie == null) {
            throw new CaisseMaladieException("Unable to create simpleDetailCaisseMaladie, the model passed is null!");
        }
        SimpleDetailCaisseMaladieChecker.checkForCreate(simpleDetailCaisseMaladie);
        return (SimpleDetailCaisseMaladie) JadePersistenceManager.add(simpleDetailCaisseMaladie);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.caissemaladie.SimpleDetailCaisseMaladieService#delete(ch.globaz.amal.
     * business.models.caissemaladie.SimpleDetailCaisseMaladie)
     */
    @Override
    public SimpleDetailCaisseMaladie delete(SimpleDetailCaisseMaladie simpleDetailCaisseMaladie)
            throws CaisseMaladieException, JadePersistenceException {
        if (simpleDetailCaisseMaladie == null) {
            throw new CaisseMaladieException("Unable to delete simpleDetailCaisseMaladie, the model passed is null!");
        }
        SimpleDetailCaisseMaladieChecker.checkForDelete(simpleDetailCaisseMaladie);
        return (SimpleDetailCaisseMaladie) JadePersistenceManager.delete(simpleDetailCaisseMaladie);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.caissemaladie.SimpleDetailCaisseMaladieService#read(java.lang.String)
     */
    @Override
    public SimpleDetailCaisseMaladie read(String idSimpleDetailCaisseMaladie) throws CaisseMaladieException,
            JadePersistenceException {
        if (idSimpleDetailCaisseMaladie == null) {
            throw new CaisseMaladieException("Unable to read simpleDetailCaisseMaladie, id passed is null!");
        }
        SimpleDetailCaisseMaladie detailCaisseMaladie = new SimpleDetailCaisseMaladie();
        detailCaisseMaladie.setId(idSimpleDetailCaisseMaladie);
        return (SimpleDetailCaisseMaladie) JadePersistenceManager.read(detailCaisseMaladie);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.caissemaladie.SimpleDetailCaisseMaladieService#search(ch.globaz.amal.
     * business.models.caissemaladie.SimpleDetailCaisseMaladieSearch)
     */
    @Override
    public SimpleDetailCaisseMaladieSearch search(SimpleDetailCaisseMaladieSearch simpleDetailCaisseMaladieSearch)
            throws CaisseMaladieException, JadePersistenceException {
        if (simpleDetailCaisseMaladieSearch == null) {
            throw new CaisseMaladieException("Unable to search simpleDetailCaisseMaladie, the model passed is null!");
        }
        return (SimpleDetailCaisseMaladieSearch) JadePersistenceManager.search(simpleDetailCaisseMaladieSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.caissemaladie.SimpleDetailCaisseMaladieService#update(ch.globaz.amal.
     * business.models.caissemaladie.SimpleDetailCaisseMaladie)
     */
    @Override
    public SimpleDetailCaisseMaladie update(SimpleDetailCaisseMaladie simpleDetailCaisseMaladie)
            throws CaisseMaladieException, JadePersistenceException {
        if (simpleDetailCaisseMaladie == null) {
            throw new CaisseMaladieException("Unable to update simpleDetailCaisseMaladie, the model passed is null!");
        }
        SimpleDetailCaisseMaladieChecker.checkForUpdate(simpleDetailCaisseMaladie);
        return (SimpleDetailCaisseMaladie) JadePersistenceManager.update(simpleDetailCaisseMaladie);
    }
}
