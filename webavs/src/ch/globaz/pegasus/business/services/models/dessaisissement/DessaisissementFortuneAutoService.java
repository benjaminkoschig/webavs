/**
 * 
 */
package ch.globaz.pegasus.business.services.models.dessaisissement;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementFortuneException;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneAuto;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneAutoSearch;

/**
 * @author BSC
 * 
 */
public interface DessaisissementFortuneAutoService extends JadeApplicationService {

    public int count(DessaisissementFortuneAutoSearch search) throws DessaisissementFortuneException,
            JadePersistenceException;

    public DessaisissementFortuneAuto read(String idDessaisissementFortune) throws JadePersistenceException,
            DessaisissementFortuneException;

    public DessaisissementFortuneAutoSearch search(DessaisissementFortuneAutoSearch dessaisissementFortuneSearch)
            throws JadePersistenceException, DessaisissementFortuneException;

}
