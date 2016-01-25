package ch.globaz.pegasus.businessimpl.services.models.dessaisissement;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementFortuneException;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneAuto;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneAutoSearch;
import ch.globaz.pegasus.business.services.models.dessaisissement.DessaisissementFortuneAutoService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * 
 * @author BSC
 * 
 */
public class DessaisissementFortuneAutoServiceImpl extends PegasusAbstractServiceImpl implements
        DessaisissementFortuneAutoService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementFortuneService #
     * count(ch.globaz.pegasus.business.models.fortuneparticuliere .DessaisissementFortuneAutoSearch)
     */
    @Override
    public int count(DessaisissementFortuneAutoSearch search) throws DessaisissementFortuneException,
            JadePersistenceException {
        if (search == null) {
            throw new DessaisissementFortuneException(
                    "Unable to count dessaisissementFortune, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementFortuneService
     * #read(java.lang.String)
     */
    @Override
    public DessaisissementFortuneAuto read(String idDessaisissementFortune) throws JadePersistenceException,
            DessaisissementFortuneException {
        if (JadeStringUtil.isEmpty(idDessaisissementFortune)) {
            throw new DessaisissementFortuneException("Unable to read dessaisissementFortune, the id passed is null!");
        }
        DessaisissementFortuneAuto dessaisissementFortune = new DessaisissementFortuneAuto();
        dessaisissementFortune.setId(idDessaisissementFortune);
        return (DessaisissementFortuneAuto) JadePersistenceManager.read(dessaisissementFortune);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. DessaisissementFortuneService #
     * search(ch.globaz.pegasus.business.models.fortuneparticuliere .DessaisissementFortuneAutoSearch )
     */
    @Override
    public DessaisissementFortuneAutoSearch search(DessaisissementFortuneAutoSearch dessaisissementFortuneSearch)
            throws JadePersistenceException, DessaisissementFortuneException {
        if (dessaisissementFortuneSearch == null) {
            throw new DessaisissementFortuneException(
                    "Unable to search dessaisissementFortune, the search model passed is null!");
        }
        return (DessaisissementFortuneAutoSearch) JadePersistenceManager.search(dessaisissementFortuneSearch);
    }

}
