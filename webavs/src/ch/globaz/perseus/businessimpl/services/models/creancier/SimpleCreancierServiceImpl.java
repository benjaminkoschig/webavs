/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.creancier;

import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.creancier.CreancierException;
import ch.globaz.perseus.business.models.creancier.SimpleCreancier;
import ch.globaz.perseus.business.services.models.creancier.SimpleCreancierService;
import ch.globaz.perseus.businessimpl.checkers.creancier.SimpleCreancierChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * @author MBO
 * 
 */
public class SimpleCreancierServiceImpl extends PerseusAbstractServiceImpl implements SimpleCreancierService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancier.SimpleCreancierService#create(ch.globaz.perseus.business
     * .models.creancier.SimpleCreancier)
     */
    @Override
    public SimpleCreancier create(SimpleCreancier simpleCreancier) throws JadePersistenceException, CreancierException {
        if (simpleCreancier == null) {
            throw new CreancierException("Unable to create a SimpleCreancier, the model passed is null");
        }
        try {
            SimpleCreancierChecker.checkForCreate(simpleCreancier);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CreancierException("Service not available in SimpleCreancierChecker " + e.toString(), e);
        } catch (JadeNoBusinessLogSessionError e) {
            throw new CreancierException("JadeNoBusinessLogSessionError in SimpleCreancierChecker " + e.toString(), e);
        }

        return (SimpleCreancier) JadePersistenceManager.add(simpleCreancier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancier.SimpleCreancierService#delete(ch.globaz.perseus.business
     * .models.creancier.SimpleCreancier)
     */
    @Override
    public SimpleCreancier delete(SimpleCreancier simpleCreancier) throws JadePersistenceException, CreancierException {
        if (simpleCreancier == null) {
            throw new CreancierException("Unable to delete a simpleCreancier, the model passed is null");
        }
        if (simpleCreancier.isNew()) {
            throw new CreancierException("Unable to delete a simpleCreancier, the model passed is new!");
        }
        SimpleCreancierChecker.checkForDelete(simpleCreancier);
        return (SimpleCreancier) JadePersistenceManager.delete(simpleCreancier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.creancier.SimpleCreancierService#read(java.lang.String)
     */
    @Override
    public SimpleCreancier read(String idCreancier) throws JadePersistenceException, CreancierException {
        if (idCreancier == null) {
            throw new CreancierException("Unable to read a idCreancier, the model passed is null!");
        }
        SimpleCreancier simpleCreancier = new SimpleCreancier();
        simpleCreancier.setId(idCreancier);
        return (SimpleCreancier) JadePersistenceManager.read(simpleCreancier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.creancier.SimpleCreancierService#update(ch.globaz.perseus.business
     * .models.creancier.SimpleCreancier)
     */
    @Override
    public SimpleCreancier update(SimpleCreancier simpleCreancier) throws JadePersistenceException, CreancierException {
        if (simpleCreancier == null) {
            throw new CreancierException("Unable to update a simpleCreantier, the model passed is null !");
        }
        if (simpleCreancier.isNew()) {
            throw new CreancierException("Unable to update a simpleCreantier, the model passed is new!");
        }
        try {
            SimpleCreancierChecker.checkForUpdate(simpleCreancier);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CreancierException("Service not available in SimpleCreancierChecker " + e.toString(), e);
        } catch (JadeNoBusinessLogSessionError e) {
            throw new CreancierException("JadeNoBusinessLogSessionError in SimpleCreancierChecker " + e.toString(), e);
        }
        return (SimpleCreancier) JadePersistenceManager.update(simpleCreancier);
    }

}
