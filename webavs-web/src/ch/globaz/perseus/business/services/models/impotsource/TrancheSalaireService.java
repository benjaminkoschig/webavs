package ch.globaz.perseus.business.services.models.impotsource;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.impotsource.TauxException;
import ch.globaz.perseus.business.models.impotsource.TrancheSalaireSearchModel;

public interface TrancheSalaireService extends JadeApplicationService {

    public int count(TrancheSalaireSearchModel searchModel) throws JadePersistenceException, TauxException;

    public TrancheSalaireSearchModel search(TrancheSalaireSearchModel searchModel) throws JadePersistenceException,
            TauxException;

}
