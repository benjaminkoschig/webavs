package ch.globaz.pegasus.businessimpl.services.models.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroitSearch;
import ch.globaz.pegasus.business.services.models.droit.SimpleVersionDroitService;
import ch.globaz.pegasus.businessimpl.checkers.droit.SimpleVersionDroitChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * 
 * @author BSC
 * 
 */
public class SimpleVersionDroitServiceImpl extends PegasusAbstractServiceImpl implements SimpleVersionDroitService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.SimpleVersionDroitService
     * #count(ch.globaz.pegasus.business.models.droit.SimpleVersionDroitSearch)
     */
    @Override
    public int count(SimpleVersionDroitSearch search) throws DroitException, JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to count versionDroit, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.SimpleVersionDroitService
     * #create(ch.globaz.pegasus.business.models.droit.SimpleVersionDroit)
     */
    @Override
    public SimpleVersionDroit create(SimpleVersionDroit versionDroit) throws DroitException, JadePersistenceException {
        if (versionDroit == null) {
            throw new DroitException("Unable to create simpleVersionDroit, the model passed is null!");
        }

        SimpleVersionDroitChecker.checkForCreate(versionDroit);

        return (SimpleVersionDroit) JadePersistenceManager.add(versionDroit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.SimpleVersionDroitService
     * #delete(ch.globaz.pegasus.business.models.droit.SimpleVersionDroit)
     */
    @Override
    public SimpleVersionDroit delete(SimpleVersionDroit versionDroit) throws DroitException, JadePersistenceException {
        if (versionDroit == null) {
            throw new DroitException("Unable to delete simpleVersionDroit, the model passed is null!");
        }
        if (versionDroit.isNew()) {
            throw new DroitException("Unable to delete simpleVersionDroit, the model passed is new!");
        }

        SimpleVersionDroitChecker.checkForDelete(versionDroit);
        return (SimpleVersionDroit) JadePersistenceManager.delete(versionDroit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.SimpleVersionDroitService #read(java.lang.String)
     */
    @Override
    public SimpleVersionDroit read(String idVersionDroit) throws DroitException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idVersionDroit)) {
            throw new DroitException("Unable to read version droit, the id passed is not defined!");
        }
        SimpleVersionDroit versionDroit = new SimpleVersionDroit();
        versionDroit.setId(idVersionDroit);
        return (SimpleVersionDroit) JadePersistenceManager.read(versionDroit);
    }

    @Override
    public SimpleVersionDroitSearch search(SimpleVersionDroitSearch search) throws JadePersistenceException,
            DroitException {
        if (search == null) {
            throw new DroitException("Unable to search simpleVersionDroitSearch, the model passed is null!");
        }
        return (SimpleVersionDroitSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.SimpleVersionDroitService
     * #update(ch.globaz.pegasus.business.models.droit.SimpleVersionDroit)
     */
    @Override
    public SimpleVersionDroit update(SimpleVersionDroit versionDroit) throws DroitException, JadePersistenceException {
        if (versionDroit == null) {
            throw new DroitException("Unable to update simpleVersionDroit, the model passed is null!");
        }
        if (versionDroit.isNew()) {
            throw new DroitException("Unable to update simpleVersionDroit, the model passed is new!");
        }

        SimpleVersionDroitChecker.checkForUpdate(versionDroit);

        return (SimpleVersionDroit) JadePersistenceManager.update(versionDroit);
    }

}
