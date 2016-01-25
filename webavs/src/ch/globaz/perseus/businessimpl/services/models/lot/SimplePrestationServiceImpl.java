package ch.globaz.perseus.businessimpl.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.SimplePrestation;
import ch.globaz.perseus.business.models.lot.SimplePrestationSearchModel;
import ch.globaz.perseus.business.services.models.lot.SimplePrestationService;
import ch.globaz.perseus.businessimpl.checkers.lot.SimplePrestationChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * 
 * @author MBO
 * 
 */
public class SimplePrestationServiceImpl extends PerseusAbstractServiceImpl implements SimplePrestationService {

    @Override
    public SimplePrestation create(SimplePrestation simplePrestation) throws JadePersistenceException, LotException {
        if (simplePrestation == null) {
            throw new LotException("Unable to create a simplePrestation, the model passed is null !");
        }
        SimplePrestationChecker.checkForCreate(simplePrestation);
        return (SimplePrestation) JadePersistenceManager.add(simplePrestation);
    }

    @Override
    public SimplePrestation delete(SimplePrestation simplePrestation) throws JadePersistenceException, LotException {
        if (simplePrestation == null) {
            throw new LotException("Unable to delete a simplePrestation, the model passed is null!");
        }
        if (simplePrestation.isNew()) {
            throw new LotException("Unable to delete a simplePrestation, the model passed is new!");
        }
        SimplePrestationChecker.checkForDelete(simplePrestation);
        return (SimplePrestation) JadePersistenceManager.delete(simplePrestation);
    }

    @Override
    public SimplePrestation read(String idSimplePrestation) throws JadePersistenceException, LotException {
        if (idSimplePrestation == null) {
            throw new LotException("Unable to read a simplePrestation, the model passed is null!");
        }
        SimplePrestation simplePrestation = new SimplePrestation();
        simplePrestation.setId(idSimplePrestation);
        return (SimplePrestation) JadePersistenceManager.read(simplePrestation);
    }

    @Override
    public SimplePrestationSearchModel search(SimplePrestationSearchModel searchModel) throws JadePersistenceException,
            LotException {
        if (searchModel == null) {
            throw new LotException("Unable to search a simplePrestation, the search model passed is null!");
        }
        return (SimplePrestationSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public SimplePrestation update(SimplePrestation simplePrestation) throws JadePersistenceException, LotException {
        if (simplePrestation == null) {
            throw new LotException("Unable to update a simplePrestation, the model passed is null!");
        }
        if (simplePrestation.isNew()) {
            throw new LotException("Unable to update a simplePrestation, the model passed is new!");
        }
        SimplePrestationChecker.checkForUpdate(simplePrestation);
        return (SimplePrestation) JadePersistenceManager.update(simplePrestation);
    }

}
