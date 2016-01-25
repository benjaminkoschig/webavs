package ch.globaz.pegasus.businessimpl.services.models.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneesPersonnellesException;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnellesSearch;
import ch.globaz.pegasus.business.services.models.droit.SimpleDonneesPersonnellesService;
import ch.globaz.pegasus.businessimpl.checkers.droit.SimpleDonneesPersonnellesChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author BSC
 */
public class SimpleDonneesPersonnellesServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleDonneesPersonnellesService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDonneesPersonnellesService
     * #count(ch.globaz.pegasus.business.models.droit .SimpleDonneesPersonnellesSearch)
     */
    @Override
    public int count(SimpleDonneesPersonnellesSearch search) throws DonneesPersonnellesException,
            JadePersistenceException {
        if (search == null) {
            throw new DonneesPersonnellesException(
                    "Unable to count donneesPersonnelles, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDonneesPersonnellesService
     * #create(ch.globaz.pegasus.business.models. droit.SimpleDonneesPersonnelles)
     */
    @Override
    public SimpleDonneesPersonnelles create(SimpleDonneesPersonnelles donneesPersonnelles)
            throws DonneesPersonnellesException, JadePersistenceException {
        if (donneesPersonnelles == null) {
            throw new DonneesPersonnellesException("Unable to create donneesPersonnelles, the model passed is null!");
        }
        SimpleDonneesPersonnellesChecker.checkForCreate(donneesPersonnelles);
        return (SimpleDonneesPersonnelles) JadePersistenceManager.add(donneesPersonnelles);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDonneesPersonnellesService
     * #delete(ch.globaz.pegasus.business.models. droit.SimpleDonneesPersonnelles)
     */
    @Override
    public SimpleDonneesPersonnelles delete(SimpleDonneesPersonnelles donneesPersonnelles)
            throws DonneesPersonnellesException, JadePersistenceException {
        if (donneesPersonnelles == null) {
            throw new DonneesPersonnellesException("Unable to delete donneesPersonnelles, the model passed is null!");
        }
        if (donneesPersonnelles.isNew()) {
            throw new DonneesPersonnellesException("Unable to delete donneesPersonnelles, the model passed is new!");
        }
        SimpleDonneesPersonnellesChecker.checkForDelete(donneesPersonnelles);
        return (SimpleDonneesPersonnelles) JadePersistenceManager.delete(donneesPersonnelles);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDonneesPersonnellesService#read(java.lang.String)
     */
    @Override
    public SimpleDonneesPersonnelles read(String idDonneesPersonnelles) throws DonneesPersonnellesException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idDonneesPersonnelles)) {
            throw new DonneesPersonnellesException("Unable to read donneesPersonnelles, the id passed is not defined!");
        }
        SimpleDonneesPersonnelles donneesPersonnelles = new SimpleDonneesPersonnelles();
        donneesPersonnelles.setId(idDonneesPersonnelles);
        return (SimpleDonneesPersonnelles) JadePersistenceManager.read(donneesPersonnelles);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.droit. SimpleDonneesPersonnellesService
     * #update(ch.globaz.pegasus.business.models. droit.SimpleDonneesPersonnelles)
     */
    @Override
    public SimpleDonneesPersonnelles update(SimpleDonneesPersonnelles donneesPersonnelles)
            throws DonneesPersonnellesException, JadePersistenceException {
        if (donneesPersonnelles == null) {
            throw new DonneesPersonnellesException("Unable to update donneesPersonnelles, the model passed is null!");
        }
        if (donneesPersonnelles.isNew()) {
            throw new DonneesPersonnellesException("Unable to update donneesPersonnelles, the model passed is new!");
        }
        SimpleDonneesPersonnellesChecker.checkForUpdate(donneesPersonnelles);
        return (SimpleDonneesPersonnelles) JadePersistenceManager.update(donneesPersonnelles);
    }

}
