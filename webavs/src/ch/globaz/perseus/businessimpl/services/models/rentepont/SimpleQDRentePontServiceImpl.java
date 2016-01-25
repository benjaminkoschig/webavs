/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.rentepont;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.rentepont.QDRentePontException;
import ch.globaz.perseus.business.models.rentepont.SimpleQDRentePont;
import ch.globaz.perseus.business.services.models.rentepont.SimpleQDRentePontService;

/**
 * @author JSI
 * 
 */
public class SimpleQDRentePontServiceImpl implements SimpleQDRentePontService {

    @Override
    public SimpleQDRentePont create(SimpleQDRentePont qdRentePont) throws JadePersistenceException,
            QDRentePontException {
        if (qdRentePont == null) {
            throw new QDRentePontException("Unable to create a simple QDRentePont, the model passed is null");
        }
        return (SimpleQDRentePont) JadePersistenceManager.add(qdRentePont);
    }

    @Override
    public SimpleQDRentePont delete(SimpleQDRentePont qdRentePont) throws JadePersistenceException,
            QDRentePontException {
        if (qdRentePont == null) {
            throw new QDRentePontException("Unable to delete a simple QDRentePont, the model passed is null");
        }
        return (SimpleQDRentePont) JadePersistenceManager.delete(qdRentePont);
    }

    @Override
    public SimpleQDRentePont read(String idQDRentePont) throws JadePersistenceException, QDRentePontException {
        if (idQDRentePont == null) {
            throw new QDRentePontException("Unable to read a simple QDRentePont, the id passed is null");
        }
        SimpleQDRentePont simpleQDRentePont = new SimpleQDRentePont();
        simpleQDRentePont.setId(idQDRentePont);
        return (SimpleQDRentePont) JadePersistenceManager.read(simpleQDRentePont);
    }

    @Override
    public SimpleQDRentePont update(SimpleQDRentePont qdRentePont) throws JadePersistenceException,
            QDRentePontException {
        if (qdRentePont == null) {
            throw new QDRentePontException("Unable to update a simple QDRentePont, the model passed is null");
        }
        return (SimpleQDRentePont) JadePersistenceManager.update(qdRentePont);
    }

}
