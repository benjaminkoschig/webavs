package ch.globaz.perseus.business.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.SimplePrestation;
import ch.globaz.perseus.business.models.lot.SimplePrestationSearchModel;

public interface SimplePrestationService extends JadeApplicationService {

    public SimplePrestation create(SimplePrestation prestation) throws JadePersistenceException, LotException;

    public SimplePrestation delete(SimplePrestation prestation) throws JadePersistenceException, LotException;

    public SimplePrestation read(String idPrestation) throws JadePersistenceException, LotException;

    public SimplePrestationSearchModel search(SimplePrestationSearchModel searchModel) throws JadePersistenceException,
            LotException;

    public SimplePrestation update(SimplePrestation prestation) throws JadePersistenceException, LotException;
}
