/**
 * 
 */
package ch.globaz.perseus.business.services.models.creancier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.creancier.CreancierException;
import ch.globaz.perseus.business.models.creancier.SimpleCreancier;

/**
 * @author MBO
 * 
 */
public interface SimpleCreancierService extends JadeApplicationService {

    public SimpleCreancier create(SimpleCreancier simpleCreantier) throws JadePersistenceException, CreancierException;

    public SimpleCreancier delete(SimpleCreancier simpleCreantier) throws JadePersistenceException, CreancierException;

    public SimpleCreancier read(String idCreancier) throws JadePersistenceException, CreancierException;

    public SimpleCreancier update(SimpleCreancier simpleCreantier) throws JadePersistenceException, CreancierException;

}
