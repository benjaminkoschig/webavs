package ch.globaz.perseus.business.services.models.rentepont;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.rentepont.FactureRentePontException;
import ch.globaz.perseus.business.models.rentepont.SimpleFactureRentePont;

/**
 * @author jsi
 * 
 */
public interface SimpleFactureRentePontService extends JadeApplicationService {
    public SimpleFactureRentePont create(SimpleFactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException;

    public SimpleFactureRentePont delete(SimpleFactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException;

    public SimpleFactureRentePont read(String idFactureRentePont) throws JadePersistenceException,
            FactureRentePontException;

    public SimpleFactureRentePont update(SimpleFactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException;
}
