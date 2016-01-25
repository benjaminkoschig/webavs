/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.rentepont;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.models.rentepont.SimpleCreancierRentePont;
import ch.globaz.perseus.business.services.models.rentepont.SimpleCreancierRentePontService;
import ch.globaz.perseus.businessimpl.checkers.rentepont.SimpleCreancierRentePontChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * @author DDE
 * 
 */
public class SimpleCreancierRentePontServiceImpl extends PerseusAbstractServiceImpl implements
        SimpleCreancierRentePontService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancier.SimpleCreancierRentePontService#create(ch.globaz.perseus
     * .business .models.creancier.SimpleCreancierRentePont)
     */
    @Override
    public SimpleCreancierRentePont create(SimpleCreancierRentePont simpleCreancierRentePont)
            throws JadePersistenceException, RentePontException {
        if (simpleCreancierRentePont == null) {
            throw new RentePontException("Unable to create a SimpleCreancierRentePont, the model passed is null");
        }
        try {
            SimpleCreancierRentePontChecker.checkForCreate(simpleCreancierRentePont);
        } catch (JadeNoBusinessLogSessionError e) {
            throw new RentePontException("JadeNoBusinessLogSessionError in SimpleCreancierRentePontChecker "
                    + e.toString(), e);
        }

        return (SimpleCreancierRentePont) JadePersistenceManager.add(simpleCreancierRentePont);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancier.SimpleCreancierRentePontService#delete(ch.globaz.perseus
     * .business .models.creancier.SimpleCreancierRentePont)
     */
    @Override
    public SimpleCreancierRentePont delete(SimpleCreancierRentePont simpleCreancierRentePont)
            throws JadePersistenceException, RentePontException {
        if (simpleCreancierRentePont == null) {
            throw new RentePontException("Unable to delete a simpleCreancierRentePont, the model passed is null");
        }
        if (simpleCreancierRentePont.isNew()) {
            throw new RentePontException("Unable to delete a simpleCreancierRentePont, the model passed is new!");
        }
        SimpleCreancierRentePontChecker.checkForDelete(simpleCreancierRentePont);
        return (SimpleCreancierRentePont) JadePersistenceManager.delete(simpleCreancierRentePont);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.creancier.SimpleCreancierRentePontService#read(java.lang.String)
     */
    @Override
    public SimpleCreancierRentePont read(String idCreancier) throws JadePersistenceException, RentePontException {
        if (idCreancier == null) {
            throw new RentePontException("Unable to read a idCreancier, the model passed is null!");
        }
        SimpleCreancierRentePont simpleCreancierRentePont = new SimpleCreancierRentePont();
        simpleCreancierRentePont.setId(idCreancier);
        return (SimpleCreancierRentePont) JadePersistenceManager.read(simpleCreancierRentePont);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancier.SimpleCreancierRentePontService#update(ch.globaz.perseus
     * .business .models.creancier.SimpleCreancierRentePont)
     */
    @Override
    public SimpleCreancierRentePont update(SimpleCreancierRentePont simpleCreancierRentePont)
            throws JadePersistenceException, RentePontException {
        if (simpleCreancierRentePont == null) {
            throw new RentePontException("Unable to update a simpleCreantier, the model passed is null !");
        }
        if (simpleCreancierRentePont.isNew()) {
            throw new RentePontException("Unable to update a simpleCreantier, the model passed is new!");
        }
        try {
            SimpleCreancierRentePontChecker.checkForUpdate(simpleCreancierRentePont);
        } catch (JadeNoBusinessLogSessionError e) {
            throw new RentePontException("JadeNoBusinessLogSessionError in SimpleCreancierRentePontChecker "
                    + e.toString(), e);
        }
        return (SimpleCreancierRentePont) JadePersistenceManager.update(simpleCreancierRentePont);
    }

}
