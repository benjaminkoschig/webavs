/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.paiement;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.OrdreVersement;
import ch.globaz.perseus.business.models.lot.OrdreVersementSearchModel;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.lot.PrestationSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * @author DDE
 * 
 */
public abstract class AbstractPmtServiceImpl extends PerseusAbstractServiceImpl {

    protected List<Prestation> loadPrestationsWithOrdresVersement(Lot lot) throws LotException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Charger les prestations du lot
        HashMap<String, Prestation> listPrestations = new HashMap<String, Prestation>();
        PrestationSearchModel prestationSearchModel = new PrestationSearchModel();
        prestationSearchModel.setForIdLot(lot.getId());
        prestationSearchModel.getInTypeLot().add(lot.getSimpleLot().getTypeLot());
        prestationSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        prestationSearchModel = PerseusServiceLocator.getPrestationService().search(prestationSearchModel);
        for (JadeAbstractModel model : prestationSearchModel.getSearchResults()) {
            Prestation prestation = (Prestation) model;
            listPrestations.put(prestation.getId(), prestation);
        }
        // Charger les ordres de versement et les mettres dans les prestations
        OrdreVersementSearchModel ordreVersementSearchModel = new OrdreVersementSearchModel();
        ordreVersementSearchModel.setForIdLot(lot.getId());
        ordreVersementSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        ordreVersementSearchModel = PerseusServiceLocator.getOrdreVersementService().search(ordreVersementSearchModel);
        for (JadeAbstractModel model : ordreVersementSearchModel.getSearchResults()) {
            OrdreVersement ordreVersement = (OrdreVersement) model;
            listPrestations.get(ordreVersement.getSimplePrestation().getId()).getListOrdreVersement()
                    .add(ordreVersement);
        }

        return new ArrayList<Prestation>(listPrestations.values());
    }

}
