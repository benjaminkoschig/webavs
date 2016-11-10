package ch.globaz.pegasus.businessimpl.services.models.home;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.models.home.SimpleHome;
import ch.globaz.pegasus.business.models.home.SimpleHomeSearch;
import ch.globaz.pegasus.business.services.models.home.SimpleHomeService;
import ch.globaz.pegasus.businessimpl.checkers.home.SimpleHomeChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author BSC
 */
public class SimpleHomeServiceImpl extends PegasusAbstractServiceImpl implements SimpleHomeService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimpleHomeService
     * #count(ch.globaz.pegasus.business.models.home.SimpleHomeSearch)
     */
    @Override
    public int count(SimpleHomeSearch search) throws HomeException, JadePersistenceException {
        if (search == null) {
            throw new HomeException("Unable to count homes, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleHomeSearch search(SimpleHomeSearch search) throws HomeException, JadePersistenceException {

        if (search == null) {
            throw new HomeException("Unable to search simpleDroit, the search model passed is null!");
        }

        return (SimpleHomeSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimpleHomeService
     * #create(ch.globaz.pegasus.business.models.home.SimpleHome)
     */
    @Override
    public SimpleHome create(SimpleHome home) throws HomeException, JadePersistenceException {
        if (home == null) {
            throw new HomeException("Unable to create home, the model passed is null!");
        }

        SimpleHomeChecker.checkForCreate(home);
        return (SimpleHome) JadePersistenceManager.add(home);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimpleHomeService
     * #delete(ch.globaz.pegasus.business.models.home.SimpleHome)
     */
    @Override
    public SimpleHome delete(SimpleHome home) throws HomeException, JadePersistenceException {
        if (home == null) {
            throw new HomeException("Unable to delete home, the model passed is null!");
        }
        if (home.isNew()) {
            throw new HomeException("Unable to delete home, the model passed is new!");
        }

        SimpleHomeChecker.checkForDelete(home);
        return (SimpleHome) JadePersistenceManager.delete(home);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimpleHomeService #read(java.lang.String)
     */
    @Override
    public SimpleHome read(String idHome) throws HomeException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idHome)) {
            throw new HomeException("Unable to read home, the id passed is not defined!");
        }
        SimpleHome home = new SimpleHome();
        home.setId(idHome);
        return (SimpleHome) JadePersistenceManager.read(home);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimpleHomeService
     * #update(ch.globaz.pegasus.business.models.home.SimpleHome)
     */
    @Override
    public SimpleHome update(SimpleHome home) throws HomeException, JadePersistenceException {
        if (home == null) {
            throw new HomeException("Unable to update home, the model passed is null!");
        }
        if (home.isNew()) {
            throw new HomeException("Unable to update home, the model passed is new!");
        }

        SimpleHomeChecker.checkForUpdate(home);
        return (SimpleHome) JadePersistenceManager.update(home);
    }

}
