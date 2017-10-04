/**
 * 
 */
package ch.globaz.corvus.business.services.models.ventilation;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.corvus.business.exceptions.CorvusException;
import ch.globaz.corvus.business.models.lots.SimpleLotSearch;
import ch.globaz.corvus.business.models.ventilation.SimpleVentilation;
import ch.globaz.corvus.business.models.ventilation.SimpleVentilationSearch;

/**
 * @author est
 * 
 */
public interface SimpleVentilationService extends JadeApplicationService {

    public SimpleVentilation create(SimpleVentilation simpleVentilation) throws CorvusException,
            JadePersistenceException;

    public SimpleVentilation delete(SimpleVentilation simpleVentilation) throws CorvusException,
            JadePersistenceException;

    public SimpleVentilation read(String idSimpleLot) throws CorvusException, JadePersistenceException;

    public SimpleVentilationSearch search(SimpleLotSearch search) throws CorvusException, JadePersistenceException;

    public SimpleVentilation update(SimpleVentilation simpleVentilation) throws CorvusException,
            JadePersistenceException;

}
