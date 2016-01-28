package ch.globaz.perseus.business.services.models.rentepont;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.PerseusException;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.models.rentepont.CreancierRentePont;
import ch.globaz.perseus.business.models.rentepont.CreancierRentePontSearchModel;

/**
 * 
 * @author DDE
 * 
 */

public interface CreancierRentePontService extends JadeApplicationService {

    public int count(CreancierRentePontSearchModel search) throws RentePontException, JadePersistenceException;

    public CreancierRentePont create(CreancierRentePont creancierRentePont) throws RentePontException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException, PerseusException;

    public CreancierRentePont delete(CreancierRentePont creancierRentePont) throws RentePontException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    public CreancierRentePont read(String idCreancier) throws RentePontException, JadePersistenceException;

    public CreancierRentePontSearchModel search(CreancierRentePontSearchModel creancierRentePontSearch)
            throws RentePontException, JadePersistenceException;

    public CreancierRentePont update(CreancierRentePont creancierRentePont) throws RentePontException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException, PerseusException;
}
