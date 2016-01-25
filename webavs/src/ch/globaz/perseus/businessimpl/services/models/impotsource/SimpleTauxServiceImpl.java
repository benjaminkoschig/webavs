package ch.globaz.perseus.businessimpl.services.models.impotsource;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.impotsource.TauxException;
import ch.globaz.perseus.business.models.impotsource.SimpleTaux;
import ch.globaz.perseus.business.models.impotsource.SimpleTauxSearchModel;
import ch.globaz.perseus.business.services.models.impotsource.SimpleTauxService;
import ch.globaz.perseus.businessimpl.checkers.impotsource.SimpleTauxChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class SimpleTauxServiceImpl extends PerseusAbstractServiceImpl implements SimpleTauxService {

    @Override
    public SimpleTaux create(SimpleTaux simpleTaux) throws JadePersistenceException, TauxException {
        if (simpleTaux == null) {
            throw new TauxException("Unable to create a simpleTaux, the model passed is null");
        }
        SimpleTauxChecker.checkForCreate(simpleTaux);
        return (SimpleTaux) JadePersistenceManager.add(simpleTaux);
    }

    @Override
    public SimpleTaux delete(SimpleTaux simpleTaux) throws JadePersistenceException, TauxException {
        if (simpleTaux == null) {
            throw new TauxException("Unable to delete a simpleTaux, the model passed is null");
        }
        if (simpleTaux.isNew()) {
            throw new TauxException("Unable to delete a simpleTaux, the model passed is null");
        }
        SimpleTauxChecker.checkForDelete(simpleTaux);
        return (SimpleTaux) JadePersistenceManager.delete(simpleTaux);
    }

    @Override
    public int delete(SimpleTauxSearchModel simpleTauxSearchModel) throws JadePersistenceException, TauxException {
        if (simpleTauxSearchModel == null) {
            throw new TauxException("Unable to delete a simpleTaux, the search model passed is null");
        }

        return JadePersistenceManager.delete(simpleTauxSearchModel);
    }

    @Override
    public SimpleTaux read(String idTaux) throws JadePersistenceException, TauxException {
        if (idTaux == null) {
            throw new TauxException("Unable to read a simpleTaux, the model passed is null");
        }
        SimpleTaux simpleTaux = new SimpleTaux();
        simpleTaux.setId(idTaux);
        return (SimpleTaux) JadePersistenceManager.read(simpleTaux);
    }

    @Override
    public SimpleTaux update(SimpleTaux simpleTaux) throws JadePersistenceException, TauxException {
        if (simpleTaux == null) {
            throw new TauxException("Unable to update a simpleTaux, the model passed is null");
        }
        if (simpleTaux.isNew()) {
            throw new TauxException("Unable to update a simpleTaux, the model passed is null");
        }
        SimpleTauxChecker.checkForUpdate(simpleTaux);
        return (SimpleTaux) JadePersistenceManager.add(simpleTaux);
    }

}
