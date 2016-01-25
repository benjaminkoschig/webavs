package ch.globaz.pegasus.businessimpl.services.models.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamilleSearch;
import ch.globaz.pegasus.business.services.models.droit.SimpleDroitMembreFamilleService;
import ch.globaz.pegasus.businessimpl.checkers.droit.SimpleDroitMembreFamilleChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author BSC
 */
public class SimpleDroitMembreFamilleServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleDroitMembreFamilleService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDroitMembreFamilleService
     * #count(ch.globaz.pegasus.business.models.droit .SimpleDroitMembreFamilleSearch)
     */
    @Override
    public int count(SimpleDroitMembreFamilleSearch search) throws DroitException, JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to count droitMembreFamille, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDroitMembreFamilleService
     * #create(ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille)
     */
    @Override
    public SimpleDroitMembreFamille create(SimpleDroitMembreFamille droitMembreFamille) throws DroitException,
            JadePersistenceException {
        if (droitMembreFamille == null) {
            throw new DroitException("Unable to create droitMembreFamille, the model passed is null!");
        }
        SimpleDroitMembreFamilleChecker.checkForCreate(droitMembreFamille);
        return (SimpleDroitMembreFamille) JadePersistenceManager.add(droitMembreFamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDroitMembreFamilleService
     * #delete(ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille)
     */
    @Override
    public SimpleDroitMembreFamille delete(SimpleDroitMembreFamille droitMembreFamille) throws DroitException,
            JadePersistenceException {
        if (droitMembreFamille == null) {
            throw new DroitException("Unable to delete droitMembreFamille, the model passed is null!");
        }
        // mis en commantaire car il n'y a pas de spy sur la table donc le test
        // ne sert a rien
        /*
         * if (droitMembreFamille.isNew()) { throw new DroitException(
         * "Unable to delete droitMembreFamille, the model passed is new!"); }
         */
        SimpleDroitMembreFamilleChecker.checkForDelete(droitMembreFamille);
        return (SimpleDroitMembreFamille) JadePersistenceManager.delete(droitMembreFamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDroitMembreFamilleService#read(java.lang.String)
     */
    @Override
    public SimpleDroitMembreFamille read(String idDroitMembreFamille) throws DroitException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idDroitMembreFamille)) {
            throw new DroitException("Unable to read droitMembreFamille, the id passed is not defined!");
        }
        SimpleDroitMembreFamille droitMembreFamille = new SimpleDroitMembreFamille();
        droitMembreFamille.setId(idDroitMembreFamille);
        return (SimpleDroitMembreFamille) JadePersistenceManager.read(droitMembreFamille);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDroitMembreFamilleService
     * #search(ch.globaz.pegasus.business.models.droit .SimpleDroitMembreFamilleSearch)
     */
    @Override
    public SimpleDroitMembreFamilleSearch search(SimpleDroitMembreFamilleSearch droitMembreFamilleSearch)
            throws JadePersistenceException, DroitException {
        if (droitMembreFamilleSearch == null) {
            throw new DroitException("Unable to search droitMembreFamille, the search model passed is null!");
        }
        return (SimpleDroitMembreFamilleSearch) JadePersistenceManager.search(droitMembreFamilleSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDroitMembreFamilleService
     * #update(ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille)
     */
    @Override
    public SimpleDroitMembreFamille update(SimpleDroitMembreFamille droitMembreFamille) throws DroitException,
            JadePersistenceException {
        if (droitMembreFamille == null) {
            throw new DroitException("Unable to update droitMembreFamille, the model passed is null!");
        }
        if (droitMembreFamille.isNew()) {
            throw new DroitException("Unable to update droitMembreFamille, the model passed is new!");
        }
        SimpleDroitMembreFamilleChecker.checkForUpdate(droitMembreFamille);
        return (SimpleDroitMembreFamille) JadePersistenceManager.update(droitMembreFamille);
    }

}
