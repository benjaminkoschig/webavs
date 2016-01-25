/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.parametreapplication;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.amal.business.exceptions.models.parametreapplication.ParametreApplicationException;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplication;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplicationSearch;
import ch.globaz.amal.business.services.models.parametreapplication.SimpleParametreApplicationService;
import ch.globaz.amal.businessimpl.checkers.parametreapplication.SimpleParametreApplicationChecker;

/**
 * @author dhi
 * 
 */
public class SimpleParametreApplicationServiceImpl implements SimpleParametreApplicationService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.parametreapplication.SimpleParametreApplicationService#count(ch.globaz
     * .amal.business.models.parametreapplication.SimpleParametreApplicationSearch)
     */
    @Override
    public int count(SimpleParametreApplicationSearch search) throws ParametreApplicationException,
            JadePersistenceException {
        if (search == null) {
            throw new ParametreApplicationException("Unable to count parametreapplication, the model passed is null");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.parametreapplication.SimpleParametreApplicationService#create(ch.globaz
     * .amal.business.models.parametreapplication.SimpleParametreApplication)
     */
    @Override
    public SimpleParametreApplication create(SimpleParametreApplication simpleParametreApplication)
            throws ParametreApplicationException, JadePersistenceException {
        if (simpleParametreApplication == null) {
            throw new ParametreApplicationException(
                    "Unable to create simpleparametreapplication, the model passe is null");
        }
        SimpleParametreApplicationChecker.checkForCreate(simpleParametreApplication);
        return (SimpleParametreApplication) JadePersistenceManager.add(simpleParametreApplication);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.parametreapplication.SimpleParametreApplicationService#delete(ch.globaz
     * .amal.business.models.parametreapplication.SimpleParametreApplication)
     */
    @Override
    public SimpleParametreApplication delete(SimpleParametreApplication simpleParametreApplication)
            throws JadePersistenceException, ParametreApplicationException {
        if (simpleParametreApplication == null) {
            throw new ParametreApplicationException(
                    "Unable to delete simpleparametreapplication, the model passe is null");
        }
        SimpleParametreApplicationChecker.checkForDelete(simpleParametreApplication);
        return (SimpleParametreApplication) JadePersistenceManager.delete(simpleParametreApplication);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.parametreapplication.SimpleParametreApplicationService#read(java.lang
     * .String)
     */
    @Override
    public SimpleParametreApplication read(String idParametreApplication) throws JadePersistenceException,
            ParametreApplicationException {
        if (JadeStringUtil.isEmpty(idParametreApplication)) {
            throw new ParametreApplicationException("Unable to read simpleparametreapplication, the id passed is null");
        }
        SimpleParametreApplication parametreApplication = new SimpleParametreApplication();
        parametreApplication.setId(idParametreApplication);
        return (SimpleParametreApplication) JadePersistenceManager.read(parametreApplication);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.parametreapplication.SimpleParametreApplicationService#search(ch.globaz
     * .amal.business.models.parametreapplication.SimpleParametreApplicationSearch)
     */
    @Override
    public SimpleParametreApplicationSearch search(SimpleParametreApplicationSearch parametreApplicationSearch)
            throws JadePersistenceException, ParametreApplicationException {
        if (parametreApplicationSearch == null) {
            throw new ParametreApplicationException(
                    "Unable to search simpleparametreapplication, the search model passed is null");
        }
        return (SimpleParametreApplicationSearch) JadePersistenceManager.search(parametreApplicationSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.parametreapplication.SimpleParametreApplicationService#update(ch.globaz
     * .amal.business.models.parametreapplication.SimpleParametreApplication)
     */
    @Override
    public SimpleParametreApplication update(SimpleParametreApplication simpleParametreApplication)
            throws ParametreApplicationException, JadePersistenceException {
        if (simpleParametreApplication == null) {
            throw new ParametreApplicationException(
                    "Unable to update simpleparametreapplication, the model passed is null");
        }
        return (SimpleParametreApplication) JadePersistenceManager.update(simpleParametreApplication);
    }

}
