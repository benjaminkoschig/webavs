/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.services.models.droit.DroitBusinessService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.prestation.business.exceptions.models.DemandePrestationException;

/**
 * @author ECO
 * 
 */
public class DroitBusinessServiceImpl extends PegasusAbstractServiceImpl implements DroitBusinessService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.DroitService#count(ch
     * .globaz.pegasus.business.models.droit.DroitSearch)
     */
    @Override
    public int count(DroitSearch search) throws DroitException, JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to count dossiers, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.DroitService#create(
     * ch.globaz.pegasus.business.models.droit.Droit)
     */
    @Override
    public Droit create(Droit droit) throws JadePersistenceException, DroitException, DemandePrestationException,
            DossierException {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.droit.DroitService#delete(
     * ch.globaz.pegasus.business.models.droit.Droit)
     */
    @Override
    public Droit delete(Droit droit) throws DroitException, JadePersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

}
