package ch.globaz.perseus.businessimpl.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.LotSearchModel;
import ch.globaz.perseus.business.models.lot.SimpleLot;
import ch.globaz.perseus.business.services.models.lot.SimpleLotService;
import ch.globaz.perseus.businessimpl.checkers.lot.SimpleLotChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class SimpleLotServiceImpl extends PerseusAbstractServiceImpl implements SimpleLotService {

    public Lot create(Lot lot) throws JadePersistenceException, LotException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SimpleLot create(SimpleLot simpleLot) throws JadePersistenceException, LotException {
        if (simpleLot == null) {
            throw new LotException("Unable to create a simple Lot, the model passed is null !");
        }
        SimpleLotChecker.checkForCreate(simpleLot);
        return (SimpleLot) JadePersistenceManager.add(simpleLot);
    }

    @Override
    public SimpleLot delete(SimpleLot simpleLot) throws JadePersistenceException, LotException {
        if (simpleLot == null) {
            throw new LotException("Unable to delete a simple Lot, the model passed is null!");
        }
        if (simpleLot.isNew()) {
            throw new LotException("Unable to delete a simple Lot, the model passed is new!");
        }
        SimpleLotChecker.checkForDelete(simpleLot);
        return (SimpleLot) JadePersistenceManager.delete(simpleLot);
    }

    @Override
    public SimpleLot read(String idLot) throws JadePersistenceException, LotException {
        if (idLot == null) {
            throw new LotException("Unable to read a simple Lot, the model passed is null!");
        }
        SimpleLot simpleLot = new SimpleLot();
        simpleLot.setId(idLot);
        return (SimpleLot) JadePersistenceManager.read(simpleLot);
    }

    public LotSearchModel search(LotSearchModel searchModel) throws JadePersistenceException, LotException {
        if (searchModel == null) {
            throw new LotException("Unable to search a simple Lot, the search model passed is null!");
        }
        return (LotSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public SimpleLot update(SimpleLot simpleLot) throws JadePersistenceException, LotException {
        if (simpleLot == null) {
            throw new LotException("Unable to update a simple Lot, the model passed is null!");
        }
        if (simpleLot.isNew()) {
            throw new LotException("Unable to update a simple Lot, the model passed is new!");
        }
        SimpleLotChecker.checkForUpdate(simpleLot);
        return (SimpleLot) JadePersistenceManager.update(simpleLot);
    }

}
