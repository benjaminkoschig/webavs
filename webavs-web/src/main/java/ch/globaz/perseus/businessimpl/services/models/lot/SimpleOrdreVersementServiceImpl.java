package ch.globaz.perseus.businessimpl.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.OrdreVersementSearchModel;
import ch.globaz.perseus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.perseus.business.models.lot.SimpleOrdreVersementSearch;
import ch.globaz.perseus.business.services.models.lot.SimpleOrdreVersementService;
import ch.globaz.perseus.businessimpl.checkers.lot.SimpleOrdreVersementChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class SimpleOrdreVersementServiceImpl extends PerseusAbstractServiceImpl implements SimpleOrdreVersementService {

    @Override
    public SimpleOrdreVersement create(SimpleOrdreVersement simpleOrdreVersement) throws JadePersistenceException,
            LotException {
        if (simpleOrdreVersement == null) {
            throw new LotException("Unable to create a simpleOrdreVersement, the model passed is null !");
        }
        SimpleOrdreVersementChecker.checkForCreate(simpleOrdreVersement);
        return (SimpleOrdreVersement) JadePersistenceManager.add(simpleOrdreVersement);
    }

    @Override
    public SimpleOrdreVersement delete(SimpleOrdreVersement simpleOrdreVersement) throws JadePersistenceException,
            LotException {
        if (simpleOrdreVersement == null) {
            throw new LotException("Unable to delete a simpleOrdreVersement, the model passed is null!");
        }
        if (simpleOrdreVersement.isNew()) {
            throw new LotException("Unable to delete a simpleOrdreVersement, the model passed is new!");
        }
        SimpleOrdreVersementChecker.checkForDelete(simpleOrdreVersement);
        return (SimpleOrdreVersement) JadePersistenceManager.delete(simpleOrdreVersement);
    }

    @Override
    public int delete(SimpleOrdreVersementSearch search) throws JadePersistenceException, LotException {
        if (search == null) {
            throw new LotException("Unable to delete a simpleOrdreVersement, the search model passed is null!");
        }
        return JadePersistenceManager.delete(search);
    }

    @Override
    public SimpleOrdreVersement read(String idOrdreVersement) throws JadePersistenceException, LotException {
        if (idOrdreVersement == null) {
            throw new LotException("Unable to read a simpleOrdreVersement, the model passed is null!");
        }
        SimpleOrdreVersement simpleOrdreVersement = new SimpleOrdreVersement();
        simpleOrdreVersement.setId(idOrdreVersement);
        return (SimpleOrdreVersement) JadePersistenceManager.read(simpleOrdreVersement);
    }

    public OrdreVersementSearchModel search(OrdreVersementSearchModel searchModel) throws JadePersistenceException,
            LotException {
        if (searchModel == null) {
            throw new LotException("Unable to search a simpleOrdreVersement, the search model passed is null!");
        }
        return (OrdreVersementSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public SimpleOrdreVersement update(SimpleOrdreVersement simpleOrdreVersement) throws JadePersistenceException,
            LotException {
        if (simpleOrdreVersement == null) {
            throw new LotException("Unable to update a simpleOrdreVersement, the model passed is null!");
        }
        if (simpleOrdreVersement.isNew()) {
            throw new LotException("Unable to update a simpleOrdreVersement, the model passed is new!");
        }
        SimpleOrdreVersementChecker.checkForUpdate(simpleOrdreVersement);
        return (SimpleOrdreVersement) JadePersistenceManager.update(simpleOrdreVersement);
    }

}
