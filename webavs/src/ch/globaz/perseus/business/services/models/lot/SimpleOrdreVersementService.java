package ch.globaz.perseus.business.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.perseus.business.models.lot.SimpleOrdreVersementSearch;

public interface SimpleOrdreVersementService extends JadeApplicationService {

    public SimpleOrdreVersement create(SimpleOrdreVersement ordreVersement) throws JadePersistenceException,
            LotException;

    public SimpleOrdreVersement delete(SimpleOrdreVersement ordreVersement) throws JadePersistenceException,
            LotException;

    public int delete(SimpleOrdreVersementSearch search) throws JadePersistenceException, LotException;

    public SimpleOrdreVersement read(String idOrdreVersement) throws JadePersistenceException, LotException;

    public SimpleOrdreVersement update(SimpleOrdreVersement ordreVersement) throws JadePersistenceException,
            LotException;

}
