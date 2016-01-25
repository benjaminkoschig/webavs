package ch.globaz.perseus.businessimpl.services.models.qd;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import ch.globaz.perseus.business.models.qd.SimpleFacture;
import ch.globaz.perseus.business.models.qd.SimpleFactureSearchModel;
import ch.globaz.perseus.business.services.models.qd.FactureDoublonService;

public class FactureDoublonServiceImpl implements FactureDoublonService {
    /*
     * public FactureDoublonServiceImpl() {
     * 
     * }
     */
    @Override
    public boolean factureSimilaireExiste(String idFacture, String idQD, String dateFacture, String montantFacture)
            throws JadePersistenceException {

        SimpleFactureSearchModel searchModel = new SimpleFactureSearchModel();

        searchModel.setForIdQD(idQD);
        searchModel.setForDateFacture(dateFacture);
        searchModel.setForMontant(montantFacture);

        searchModel = (SimpleFactureSearchModel) JadePersistenceManager.search(searchModel);

        int count = JadePersistenceManager.count(searchModel);

        Boolean isSameId = false;
        // Boucler sur les factures "potentiellement en doublon"
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            SimpleFacture facture = (SimpleFacture) model;
            if (facture.getId().equals(idFacture)) {
                isSameId = true;
            }
        }

        if (isSameId) {
            return (count > 1);
        } else {
            return (count > 0);
        }
    }
}
