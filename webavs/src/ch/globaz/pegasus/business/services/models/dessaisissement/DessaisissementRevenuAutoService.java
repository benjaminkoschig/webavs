/**
 * 
 */
package ch.globaz.pegasus.business.services.models.dessaisissement;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.dessaisissement.DessaisissementRevenuException;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuAuto;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuAutoSearch;

/**
 * @author BSC
 * 
 */
public interface DessaisissementRevenuAutoService extends JadeApplicationService {

    public int count(DessaisissementRevenuAutoSearch search) throws DessaisissementRevenuException,
            JadePersistenceException;

    public DessaisissementRevenuAuto read(String idDessaisissementRevenu) throws JadePersistenceException,
            DessaisissementRevenuException;

    public DessaisissementRevenuAutoSearch search(DessaisissementRevenuAutoSearch dessaisissementRevenuSearch)
            throws JadePersistenceException, DessaisissementRevenuException;

}
