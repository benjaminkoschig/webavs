/**
 * 
 */
package ch.globaz.perseus.business.services.models.rentepont;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.rentepont.QDRentePontException;
import ch.globaz.perseus.business.models.rentepont.SimpleQDRentePont;

/**
 * @author JSI
 * 
 */
public interface SimpleQDRentePontService extends JadeApplicationService {

    public SimpleQDRentePont create(SimpleQDRentePont qdRentePont) throws JadePersistenceException,
            QDRentePontException;

    public SimpleQDRentePont delete(SimpleQDRentePont qdRentePont) throws JadePersistenceException,
            QDRentePontException;

    public SimpleQDRentePont read(String idQDRentePont) throws JadePersistenceException, QDRentePontException;

    public SimpleQDRentePont update(SimpleQDRentePont qdRentePont) throws JadePersistenceException,
            QDRentePontException;
}
