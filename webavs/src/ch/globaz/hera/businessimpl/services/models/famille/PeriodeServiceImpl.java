package ch.globaz.hera.businessimpl.services.models.famille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.hera.business.exceptions.models.PeriodeException;
import ch.globaz.hera.business.models.famille.SimplePeriode;
import ch.globaz.hera.business.models.famille.SimplePeriodeSearch;
import ch.globaz.hera.business.services.models.famille.PeriodeService;
import ch.globaz.hera.businessimpl.services.HeraAbstractServiceImpl;

public class PeriodeServiceImpl extends HeraAbstractServiceImpl implements PeriodeService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.hera.business.services.models.famille.PeriodeService#count(
     * ch.globaz.hera.business.models.famille.SimplePeriodeSearch)
     */
    @Override
    public int count(SimplePeriodeSearch search) throws PeriodeException, JadePersistenceException {
        if (search == null) {
            throw new PeriodeException("Unable to count simplePeriode, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.hera.business.services.models.famille.PeriodeService#read(java .lang.String)
     */
    @Override
    public SimplePeriode read(String idSimplePeriode) throws JadePersistenceException, PeriodeException {
        if (JadeStringUtil.isEmpty(idSimplePeriode)) {
            throw new PeriodeException("Unable to read simplePeriode, the id passed is null!");
        }
        SimplePeriode simplePeriode = new SimplePeriode();
        simplePeriode.setId(idSimplePeriode);
        return (SimplePeriode) JadePersistenceManager.read(simplePeriode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.hera.business.services.models.famille.PeriodeService#search
     * (ch.globaz.hera.business.models.famille.SimplePeriodeSearch)
     */
    @Override
    public SimplePeriodeSearch search(SimplePeriodeSearch search) throws JadePersistenceException, PeriodeException {
        if (search == null) {
            throw new PeriodeException("Unable to search simplePeriode, the search model passed is null!");
        }
        return (SimplePeriodeSearch) JadePersistenceManager.search(search);
    }

}
