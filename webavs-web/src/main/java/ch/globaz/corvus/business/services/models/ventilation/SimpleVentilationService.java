/**
 * 
 */
package ch.globaz.corvus.business.services.models.ventilation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.corvus.business.exceptions.CorvusException;
import ch.globaz.corvus.business.models.lots.SimpleLotSearch;
import ch.globaz.corvus.business.models.ventilation.SimpleVentilation;
import ch.globaz.corvus.business.models.ventilation.SimpleVentilationSearch;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;

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

    public SimpleVentilationSearch search(SimpleVentilationSearch search) throws JadePersistenceException,
            AdaptationException;

    public SimpleVentilation getMontantVentileFromIdPca(String idPca) throws JadeApplicationException,
            JadePersistenceException;

}
