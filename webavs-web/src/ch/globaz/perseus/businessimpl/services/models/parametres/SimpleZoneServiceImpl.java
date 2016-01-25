/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.parametres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.parametres.ParametresException;
import ch.globaz.perseus.business.models.parametres.SimpleZone;
import ch.globaz.perseus.business.models.parametres.SimpleZoneSearchModel;
import ch.globaz.perseus.business.services.models.parametres.SimpleZoneService;
import ch.globaz.perseus.businessimpl.checkers.parametres.SimpleZoneChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * @author DDE
 * 
 */
public class SimpleZoneServiceImpl extends PerseusAbstractServiceImpl implements SimpleZoneService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.parametres.SimpleZoneService#count(ch.globaz.perseus.business.models
     * .parametres.SimpleZoneSearchModel)
     */
    @Override
    public int count(SimpleZoneSearchModel search) throws ParametresException, JadePersistenceException {
        if (search == null) {
            throw new ParametresException("Unable to count simples Zone, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.parametres.SimpleZoneService#create(ch.globaz.perseus.business.models
     * .parametres.SimpleZone)
     */
    @Override
    public SimpleZone create(SimpleZone simpleZone) throws JadePersistenceException, ParametresException {
        if (simpleZone == null) {
            throw new ParametresException("Unable to create a simple Zone, the model passed is null!");
        }
        SimpleZoneChecker.checkForCreate(simpleZone);
        return (SimpleZone) JadePersistenceManager.add(simpleZone);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.parametres.SimpleZoneService#delete(ch.globaz.perseus.business.models
     * .parametres.SimpleZone)
     */
    @Override
    public SimpleZone delete(SimpleZone simpleZone) throws JadePersistenceException, ParametresException {
        if (simpleZone == null) {
            throw new ParametresException("Unable to delete a simple Zone, the model passed is null!");
        }
        if (simpleZone.isNew()) {
            throw new ParametresException("Unable to delete a simple Zone, the model passed is new!");
        }
        SimpleZoneChecker.checkForDelete(simpleZone);
        return (SimpleZone) JadePersistenceManager.delete(simpleZone);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.perseus.business.services.models.parametres.SimpleZoneService#read(java.lang.String)
     */
    @Override
    public SimpleZone read(String idSimpleZone) throws JadePersistenceException, ParametresException {
        if (idSimpleZone == null) {
            throw new ParametresException("Unable to read a simple Zone, the model passed is null!");
        }
        SimpleZone simpleZone = new SimpleZone();
        simpleZone.setId(idSimpleZone);
        return (SimpleZone) JadePersistenceManager.read(simpleZone);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.parametres.SimpleZoneService#search(ch.globaz.perseus.business.models
     * .parametres.SimpleZoneSearchModel)
     */
    @Override
    public SimpleZoneSearchModel search(SimpleZoneSearchModel searchModel) throws JadePersistenceException,
            ParametresException {
        if (searchModel == null) {
            throw new ParametresException("Unable to search a simple Zone, the search model passed is null!");
        }
        return (SimpleZoneSearchModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.parametres.SimpleZoneService#update(ch.globaz.perseus.business.models
     * .parametres.SimpleZone)
     */
    @Override
    public SimpleZone update(SimpleZone simpleZone) throws JadePersistenceException, ParametresException {
        if (simpleZone == null) {
            throw new ParametresException("Unable to update a simple Zone, the model passed is null!");
        }
        if (simpleZone.isNew()) {
            throw new ParametresException("Unable to update a simple Zone, the model passed is new!");
        }
        SimpleZoneChecker.checkForUpdate(simpleZone);
        return (SimpleZone) JadePersistenceManager.update(simpleZone);
    }

}
