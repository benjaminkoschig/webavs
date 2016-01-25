package ch.globaz.perseus.business.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.OrdreVersement;
import ch.globaz.perseus.business.models.lot.OrdreVersementSearchModel;

public interface OrdreVersementService extends JadeApplicationService {

    public int count(OrdreVersementSearchModel search) throws LotException, JadePersistenceException;

    public OrdreVersement create(OrdreVersement ordreVersement) throws JadePersistenceException, LotException;

    public OrdreVersement delete(OrdreVersement ordreVersement) throws JadePersistenceException, LotException;

    public OrdreVersement read(String idOrdreVersement) throws JadePersistenceException, LotException;

    public OrdreVersementSearchModel search(OrdreVersementSearchModel searchModel) throws JadePersistenceException,
            LotException;

    public OrdreVersement update(OrdreVersement ordreVersement) throws JadePersistenceException, LotException;

}
