package ch.globaz.pegasus.businessimpl.services.models.dessaisissement;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementRevenuException;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuAuto;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuAutoSearch;
import ch.globaz.pegasus.business.services.models.dessaisissement.DessaisissementRevenuAutoService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * 
 * @author BSC
 * 
 */
public class DessaisissementRevenuAutoServiceImpl extends PegasusAbstractServiceImpl implements
        DessaisissementRevenuAutoService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementRevenuService #
     * count(ch.globaz.pegasus.business.models.fortuneparticuliere .DessaisissementRevenuAutoSearch)
     */
    @Override
    public int count(DessaisissementRevenuAutoSearch search) throws DessaisissementRevenuException,
            JadePersistenceException {
        if (search == null) {
            throw new DessaisissementRevenuException(
                    "Unable to count dessaisissementRevenu, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementRevenuService
     * #read(java.lang.String)
     */
    @Override
    public DessaisissementRevenuAuto read(String idDessaisissementRevenu) throws JadePersistenceException,
            DessaisissementRevenuException {
        if (JadeStringUtil.isEmpty(idDessaisissementRevenu)) {
            throw new DessaisissementRevenuException("Unable to read dessaisissementRevenu, the id passed is null!");
        }
        DessaisissementRevenuAuto dessaisissementRevenu = new DessaisissementRevenuAuto();
        dessaisissementRevenu.setId(idDessaisissementRevenu);
        return (DessaisissementRevenuAuto) JadePersistenceManager.read(dessaisissementRevenu);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementRevenuService #
     * search(ch.globaz.pegasus.business.models.fortuneparticuliere .DessaisissementRevenuAutoSearch )
     */
    @Override
    public DessaisissementRevenuAutoSearch search(DessaisissementRevenuAutoSearch betailSearch)
            throws JadePersistenceException, DessaisissementRevenuException {
        if (betailSearch == null) {
            throw new DessaisissementRevenuException(
                    "Unable to search dessaisissementRevenu, the search model passed is null!");
        }
        return (DessaisissementRevenuAutoSearch) JadePersistenceManager.search(betailSearch);
    }

}
