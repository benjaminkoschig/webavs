package ch.globaz.perseus.business.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.PrestationRentePontSearchModel;

public interface PrestationRentePontService extends JadeApplicationService {

    public JadeAbstractSearchModel search(JadeAbstractSearchModel searchModel) throws JadePersistenceException,
            LotException;

    public PrestationRentePontSearchModel search(PrestationRentePontSearchModel searchModel)
            throws JadePersistenceException, LotException;

}
