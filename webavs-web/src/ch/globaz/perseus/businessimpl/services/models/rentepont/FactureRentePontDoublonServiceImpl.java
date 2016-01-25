package ch.globaz.perseus.businessimpl.services.models.rentepont;

import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRDateFormater;
import ch.globaz.perseus.business.constantes.CSEtatRentePont;
import ch.globaz.perseus.business.exceptions.models.rentepont.RentePontException;
import ch.globaz.perseus.business.models.rentepont.FactureRentePont;
import ch.globaz.perseus.business.models.rentepont.FactureRentePontSearchModel;
import ch.globaz.perseus.business.models.rentepont.RentePontSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.rentepont.FactureRentePontDoublonService;

public class FactureRentePontDoublonServiceImpl implements FactureRentePontDoublonService {

    @Override
    public boolean factureSimilaireExiste(String idFacture, String idDossier, String dateFacture,
            String montantFacture, String sousTypeQD) throws JadePersistenceException {

        FactureRentePontSearchModel searchModel = new FactureRentePontSearchModel();

        searchModel.setForIdDossier(idDossier);
        searchModel.setForMontant(montantFacture);
        searchModel.setForDateFacture(dateFacture);
        searchModel.setForCsSousTypeSoin(sousTypeQD);

        searchModel = (FactureRentePontSearchModel) JadePersistenceManager.search(searchModel);

        int count = JadePersistenceManager.count(searchModel);

        Boolean isSameId = false;
        // Boucler sur les factures "potentiellement en doublon"
        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            FactureRentePont facture = (FactureRentePont) model;
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

    @Override
    public String checkDemandeExiste(String dateReception, String dateFacture, String datePriseEnCharge,
            String idDossier, String anneeQD) throws RentePontException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeNoBusinessLogSessionError {

        String message = "";
        String dateAVerifier = dateFacture;

        // Si la date de prise en charge est renseigné, c est avec cette date que nous allons voir si une demande
        // PCF etait en cours au moment de la prise en charge.
        if (!datePriseEnCharge.equals("dateNull")) {
            dateAVerifier = datePriseEnCharge;
        }

        // Voir si il existe une demande de rente-pont valide pour la date de la facture
        RentePontSearchModel rpsm = new RentePontSearchModel();

        rpsm.setForDateValable(dateAVerifier);
        rpsm.setForIdDossier(idDossier);
        rpsm.setForCsEtat(CSEtatRentePont.VALIDE.getCodeSystem());

        if (PerseusServiceLocator.getRentePontService().count(rpsm) < 1) {
            message = JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                    "perseus.rentePont.factureRentePont.rentePont.valide");
        } else {
            if (!PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(dateAVerifier).equals(anneeQD)) {
                String[] t = new String[1];
                t[0] = anneeQD;

                message = JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(),
                        "perseus.rentePont.factureRentePont.anneeQd.pasMemeDateFacture", t);
            }
        }

        return message;
    }
}
