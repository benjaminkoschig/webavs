package ch.globaz.perseus.businessimpl.services.models.rentepont;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.models.rentepont.SimpleRentePont;
import ch.globaz.perseus.business.services.models.rentepont.SimpleRentePontService;
import ch.globaz.perseus.businessimpl.checkers.rentepont.SimpleRentePontChecker;

/**
 * @author DDE
 * 
 */
public class SimpleRentePontServiceImpl implements SimpleRentePontService {

    @Override
    public SimpleRentePont create(SimpleRentePont rentePont) throws JadePersistenceException, RentePontException {
        if (rentePont == null) {
            throw new RentePontException("Unable to create a simple RentePont, the model passed is null");
        }
        SimpleRentePontChecker.checkForCreate(rentePont);
        return (SimpleRentePont) JadePersistenceManager.add(rentePont);
    }

    @Override
    public SimpleRentePont delete(SimpleRentePont rentePont) throws JadePersistenceException, RentePontException {
        if (rentePont == null) {
            throw new RentePontException("Unable to delete a simple rentePont, the model passed is null!");
        } else if (rentePont.isNew()) {
            throw new RentePontException("Unable to delete a simple rentePont, the model passed is new!");
        }
        SimpleRentePontChecker.checkForDelete(rentePont);
        return (SimpleRentePont) JadePersistenceManager.delete(rentePont);
    }

    @Override
    public SimpleRentePont read(String idRentePont) throws JadePersistenceException, RentePontException {
        if (JadeStringUtil.isEmpty(idRentePont)) {
            throw new RentePontException("Unable to read a simple rentePont, the id passed is null!");
        }
        SimpleRentePont rentePont = new SimpleRentePont();
        rentePont.setId(idRentePont);
        return (SimpleRentePont) JadePersistenceManager.read(rentePont);
    }

    @Override
    public SimpleRentePont update(SimpleRentePont rentePont) throws JadePersistenceException, RentePontException {
        if (rentePont == null) {
            throw new RentePontException("Unable to update a simple rentePont, the model passed is null!");
        } else if (rentePont.isNew()) {
            throw new RentePontException("Unable to update a simple rentePont, the model passed is new!");
        }
        SimpleRentePontChecker.checkForUpdate(rentePont);
        return (SimpleRentePont) JadePersistenceManager.update(rentePont);
    }

}
