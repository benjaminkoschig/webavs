package ch.globaz.perseus.businessimpl.services.models.impotsource;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.impotsource.TauxException;
import ch.globaz.perseus.business.models.impotsource.SimpleTrancheSalaire;
import ch.globaz.perseus.business.models.impotsource.SimpleTrancheSalaireSearchModel;
import ch.globaz.perseus.business.services.models.impotsource.SimpleTrancheSalaireService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class SimpleTrancheSalaireServiceImpl extends PerseusAbstractServiceImpl implements SimpleTrancheSalaireService {

    @Override
    public SimpleTrancheSalaire create(SimpleTrancheSalaire simpleTrancheSalaire) throws JadePersistenceException,
            TauxException {
        if (simpleTrancheSalaire == null) {
            throw new TauxException("Unable to create a simpleTrancheSalaire, the model passed is null");
        }

        return (SimpleTrancheSalaire) JadePersistenceManager.add(simpleTrancheSalaire);
    }

    @Override
    public SimpleTrancheSalaire delete(SimpleTrancheSalaire simpleTrancheSalaire) throws JadePersistenceException,
            TauxException {
        if (simpleTrancheSalaire == null) {
            throw new TauxException("Unable to delete a simpleTrancheSalaire, the model passed is null");
        }
        if (simpleTrancheSalaire.isNew()) {
            throw new TauxException("Unable to delete a simpleTrancheSalaire, the model passed is null");
        }

        return (SimpleTrancheSalaire) JadePersistenceManager.add(simpleTrancheSalaire);
    }

    @Override
    public SimpleTrancheSalaire read(String idTrancheSalaire) throws JadePersistenceException, TauxException {
        if (JadeStringUtil.isEmpty(idTrancheSalaire)) {
            throw new TauxException("Unable to read a simpleTrancheSalaire, the id passed is null!");
        }
        SimpleTrancheSalaire trancheSalaire = new SimpleTrancheSalaire();
        trancheSalaire.setId(idTrancheSalaire);
        return (SimpleTrancheSalaire) JadePersistenceManager.read(trancheSalaire);
    }

    @Override
    public SimpleTrancheSalaireSearchModel search(SimpleTrancheSalaireSearchModel searchModel)
            throws JadePersistenceException, TauxException {
        if (searchModel == null) {
            throw new TauxException("Unable to search a trancheSalaire, the search model passed is null!");
        }

        return (SimpleTrancheSalaireSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public SimpleTrancheSalaire update(SimpleTrancheSalaire simpleTrancheSalaire) throws JadePersistenceException,
            TauxException {
        if (simpleTrancheSalaire == null) {
            throw new TauxException("Unable to update a simpleTrancheSalaire, the model passed is null");
        }
        if (simpleTrancheSalaire.isNew()) {
            throw new TauxException("Unable to update a simpleTrancheSalaire, the model passed is null");
        }

        return (SimpleTrancheSalaire) JadePersistenceManager.add(simpleTrancheSalaire);
    }

}
