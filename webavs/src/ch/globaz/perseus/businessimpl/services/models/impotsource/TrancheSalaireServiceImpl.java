package ch.globaz.perseus.businessimpl.services.models.impotsource;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.impotsource.TauxException;
import ch.globaz.perseus.business.models.impotsource.TrancheSalaireSearchModel;
import ch.globaz.perseus.business.services.models.impotsource.TrancheSalaireService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class TrancheSalaireServiceImpl extends PerseusAbstractServiceImpl implements TrancheSalaireService {

    @Override
    public int count(TrancheSalaireSearchModel search) throws TauxException, JadePersistenceException {
        if (search == null) {
            throw new TauxException("Unable to count trancheSalaire, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public TrancheSalaireSearchModel search(TrancheSalaireSearchModel searchModel) throws JadePersistenceException,
            TauxException {
        if (searchModel == null) {
            throw new TauxException("Unable to search a trancheSalaire, the search model passed is null!");
        }

        return (TrancheSalaireSearchModel) JadePersistenceManager.search(searchModel);
    }

}
