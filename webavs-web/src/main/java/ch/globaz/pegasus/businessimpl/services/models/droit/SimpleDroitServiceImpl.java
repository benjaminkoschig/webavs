package ch.globaz.pegasus.businessimpl.services.models.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.droit.SimpleDroitSearch;
import ch.globaz.pegasus.business.services.models.droit.SimpleDroitService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * 
 * @author BSC
 * 
 */
public class SimpleDroitServiceImpl extends PegasusAbstractServiceImpl implements SimpleDroitService {

    @Override
    public int count(SimpleDroitSearch search) throws DroitException, JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to count SimpleDroit, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.SimpleDroitService#create
     * (ch.globaz.pegasus.business.models.droit.SimpleDroit)
     */
    @Override
    public SimpleDroit create(SimpleDroit droit) throws DroitException, JadePersistenceException {
        if (droit == null) {
            throw new DroitException("Unable to create simpleDroit, the model passed is null!");
        }

        // TODO
        // SimpleDroitChecker.checkForCreate(droit);

        return (SimpleDroit) JadePersistenceManager.add(droit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.SimpleDroitService#delete
     * (ch.globaz.pegasus.business.models.droit.SimpleDroit)
     */
    @Override
    public SimpleDroit delete(SimpleDroit droit) throws DroitException, JadePersistenceException {
        if (droit == null) {
            throw new DroitException("Unable to delete simpleDroit, the model passed is null!");
        }
        // TODO
        // SimpleDroitChecker.checkForDelete(droit);
        return (SimpleDroit) JadePersistenceManager.delete(droit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.SimpleDroitService#read (java.lang.String)
     */
    @Override
    public SimpleDroit read(String idDroit) throws DroitException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idDroit)) {
            throw new DroitException("Unable to read droit, the id passed is not defined!");
        }
        SimpleDroit droit = new SimpleDroit();
        droit.setId(idDroit);
        return (SimpleDroit) JadePersistenceManager.read(droit);
    }

    @Override
    public SimpleDroitSearch search(SimpleDroitSearch search) throws DroitException, JadePersistenceException {

        if (search == null) {
            throw new DroitException("Unable to search simpleDroit, the search model passed is null!");
        }

        return (SimpleDroitSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.SimpleDroitService#update
     * (ch.globaz.pegasus.business.models.droit.SimpleDroit)
     */
    @Override
    public SimpleDroit update(SimpleDroit droit) throws DroitException, JadePersistenceException {
        if (droit == null) {
            throw new DroitException("Unable to update simpleDroit, the model passed is null!");
        }

        // TODO
        // SimpleDroitChecker.checkForUpdate(droit);

        return (SimpleDroit) JadePersistenceManager.update(droit);
    }

}
