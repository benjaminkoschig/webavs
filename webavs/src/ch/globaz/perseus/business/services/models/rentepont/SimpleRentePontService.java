/**
 * 
 */
package ch.globaz.perseus.business.services.models.rentepont;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.models.rentepont.SimpleRentePont;

/**
 * @author DDE
 * 
 */
public interface SimpleRentePontService extends JadeApplicationService {

    public SimpleRentePont create(SimpleRentePont rentePont) throws JadePersistenceException, RentePontException;

    public SimpleRentePont delete(SimpleRentePont rentePont) throws JadePersistenceException, RentePontException;

    public SimpleRentePont read(String idRentePont) throws JadePersistenceException, RentePontException;

    public SimpleRentePont update(SimpleRentePont rentePont) throws JadePersistenceException, RentePontException;
}
