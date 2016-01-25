package ch.globaz.perseus.business.services.models.impotsource;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.impotsource.TauxException;
import ch.globaz.perseus.business.models.impotsource.SimpleTrancheSalaire;
import ch.globaz.perseus.business.models.impotsource.SimpleTrancheSalaireSearchModel;

public interface SimpleTrancheSalaireService extends JadeApplicationService {

    public SimpleTrancheSalaire create(SimpleTrancheSalaire simpleTrancheSalaire) throws JadePersistenceException,
            TauxException;

    public SimpleTrancheSalaire delete(SimpleTrancheSalaire simpleTrancheSalaire) throws JadePersistenceException,
            TauxException;

    public SimpleTrancheSalaire read(String idTrancheSalaire) throws JadePersistenceException, TauxException;

    public SimpleTrancheSalaireSearchModel search(SimpleTrancheSalaireSearchModel searchModel)
            throws JadePersistenceException, TauxException;

    public SimpleTrancheSalaire update(SimpleTrancheSalaire simpleTrancheSalaire) throws JadePersistenceException,
            TauxException;

}
