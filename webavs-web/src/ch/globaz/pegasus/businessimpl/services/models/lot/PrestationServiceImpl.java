package ch.globaz.pegasus.businessimpl.services.models.lot;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.lot.Prestation;
import ch.globaz.pegasus.business.models.lot.PrestationSearch;
import ch.globaz.pegasus.business.services.models.lot.PrestationService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class PrestationServiceImpl extends PegasusAbstractServiceImpl implements PrestationService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.lot.PrestationService#count
     * (ch.globaz.pegasus.business.models.lot.PrestationSearch)
     */
    @Override
    public int count(PrestationSearch search) throws PrestationException, JadePersistenceException {
        if (search == null) {
            throw new PrestationException("Unable to count prestation, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.lot.PrestationService#read (java.lang.String)
     */
    @Override
    public Prestation read(String idPrestation) throws JadePersistenceException, PrestationException {
        if (JadeStringUtil.isEmpty(idPrestation)) {
            throw new PrestationException("Unable to read prestation, the id passed is null!");
        }
        Prestation prestation = new Prestation();
        prestation.setId(idPrestation);
        return (Prestation) JadePersistenceManager.read(prestation);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.lot.PrestationService#search
     * (ch.globaz.pegasus.business.models.lot.PrestationSearch)
     */
    @Override
    public PrestationSearch search(PrestationSearch search) throws JadePersistenceException, PrestationException {
        if (search == null) {
            throw new PrestationException("Unable to search prestation, the search model passed is null!");
        }
        return (PrestationSearch) JadePersistenceManager.search(search);
    }

}
