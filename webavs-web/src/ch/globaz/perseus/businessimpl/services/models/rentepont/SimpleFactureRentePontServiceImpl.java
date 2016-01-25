package ch.globaz.perseus.businessimpl.services.models.rentepont;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.rentepont.FactureRentePontException;
import ch.globaz.perseus.business.models.rentepont.SimpleFactureRentePont;
import ch.globaz.perseus.business.services.models.rentepont.SimpleFactureRentePontService;
import ch.globaz.perseus.businessimpl.checkers.rentepont.SimpleFactureRentePontChecker;

/**
 * @author jsi
 * 
 */
public class SimpleFactureRentePontServiceImpl implements SimpleFactureRentePontService {

    @Override
    public SimpleFactureRentePont create(SimpleFactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException {
        if (factureRentePont == null) {
            throw new FactureRentePontException("Unable to create a simple FactureRentePont, the model passed is null");
        }
        SimpleFactureRentePontChecker.checkForCreate(factureRentePont);
        return (SimpleFactureRentePont) JadePersistenceManager.add(factureRentePont);
    }

    @Override
    public SimpleFactureRentePont delete(SimpleFactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException {
        if (factureRentePont == null) {
            throw new FactureRentePontException("Unable to delete a simple FactureRentePont, the model passed is null");
        }
        return (SimpleFactureRentePont) JadePersistenceManager.delete(factureRentePont);
    }

    @Override
    public SimpleFactureRentePont read(String idFactureRentePont) throws JadePersistenceException,
            FactureRentePontException {
        if (idFactureRentePont == null) {
            throw new FactureRentePontException("Unable to read a simple FactureRentePont, the id passed is null");
        }
        SimpleFactureRentePont simpleFactureRentePont = new SimpleFactureRentePont();
        simpleFactureRentePont.setId(idFactureRentePont);
        return (SimpleFactureRentePont) JadePersistenceManager.read(simpleFactureRentePont);
    }

    @Override
    public SimpleFactureRentePont update(SimpleFactureRentePont factureRentePont) throws JadePersistenceException,
            FactureRentePontException {
        if (factureRentePont == null) {
            throw new FactureRentePontException("Unable to update a simple FactureRentePont, the model passed is null");
        }
        return (SimpleFactureRentePont) JadePersistenceManager.update(factureRentePont);
    }
}
