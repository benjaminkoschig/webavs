package ch.globaz.perseus.businessimpl.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.PrestationDecision;
import ch.globaz.perseus.business.models.lot.PrestationRP;
import ch.globaz.perseus.business.models.lot.PrestationRentePontSearchModel;
import ch.globaz.perseus.business.services.models.lot.PrestationRentePontService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class PrestationRentePontServiceImpl extends PerseusAbstractServiceImpl implements PrestationRentePontService {

    @Override
    public JadeAbstractSearchModel search(JadeAbstractSearchModel searchModel) throws JadePersistenceException,
            LotException {
        if (searchModel == null) {
            throw new LotException("Unable to search Prestation, the search model passed is null !");
        }
        return JadePersistenceManager.search(searchModel);
    }

    @Override
    public PrestationRentePontSearchModel search(PrestationRentePontSearchModel searchModel)
            throws JadePersistenceException, LotException {
        if (searchModel == null) {
            throw new LotException("Unable to search PrestationRentePontSearchModel, the search model passed is null !");
        }
        // Dans le cas où on cherche des prestations pour un type de lot précis, optimisation de la recherche
        if (searchModel.getInTypeLot().size() == 1) {
            if (CSTypeLot.LOT_DECISION_RP.getCodeSystem().equals(searchModel.getInTypeLot().get(0))) {
                searchModel.setModelClass(PrestationDecision.class);
            }
            if (CSTypeLot.LOT_FACTURES_RP.getCodeSystem().equals(searchModel.getInTypeLot().get(0))) {
                searchModel.setModelClass(PrestationRP.class);
            }
        }
        return (PrestationRentePontSearchModel) JadePersistenceManager.search(searchModel);
    }

}