package ch.globaz.perseus.business.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.SimpleLot;

public interface SimpleLotService extends JadeApplicationService {

    public SimpleLot create(SimpleLot lot) throws JadePersistenceException, LotException;

    public SimpleLot delete(SimpleLot lot) throws JadePersistenceException, LotException;

    public SimpleLot read(String idLot) throws JadePersistenceException, LotException;

    public SimpleLot update(SimpleLot lot) throws JadePersistenceException, LotException;

}
