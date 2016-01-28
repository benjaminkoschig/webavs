/**
 * 
 */
package ch.globaz.perseus.business.services.models.rentepont;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.models.rentepont.SimpleCreancierRentePont;

/**
 * @author MBO
 * 
 */
public interface SimpleCreancierRentePontService extends JadeApplicationService {

    public SimpleCreancierRentePont create(SimpleCreancierRentePont simpleCreancierRentePont)
            throws JadePersistenceException, RentePontException;

    public SimpleCreancierRentePont delete(SimpleCreancierRentePont simpleCreancierRentePont)
            throws JadePersistenceException, RentePontException;

    public SimpleCreancierRentePont read(String idCreancierRentePont) throws JadePersistenceException,
            RentePontException;

    public SimpleCreancierRentePont update(SimpleCreancierRentePont simpleCreancierRentePont)
            throws JadePersistenceException, RentePontException;

}
