package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import ch.gdk_cds.xmlns.pv_5215_000802._3.Message;
import ch.gdk_cds.xmlns.pv_5215_000802._3.ObjectFactory;
import ch.gdk_cds.xmlns.pv_5215_000802._3.PremiumInsuredPersonType;
import ch.gdk_cds.xmlns.pv_5215_000802._3.PremiumQueryResultType;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.AMMessagesTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedexSearch;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuableSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.webavs.common.CommonNSSFormater;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Gestionnaire de réception pour les annonces 'Reponse prime tarifaire'
 *
 * @author sco
 */
public class AnnoncePremiumQueryResultHandler extends AnnonceHandlerAbstract {

    private Message premiumQueryResult;
    private CommonNSSFormater nnssFormater = new CommonNSSFormater();
    private String annee;

    /**
     * Cosntructeur de l'handler de réponse de l'annonce
     *
     * @param premiumQueryResult
     */
    public AnnoncePremiumQueryResultHandler(Message premiumQueryResult) {
        this.premiumQueryResult = premiumQueryResult;
    }

    @Override
    public SimpleAnnonceSedex execute() throws JadeApplicationException, JadePersistenceException {
        String idTiersCM = null;
        try {
            // Enregistrement des données génériques
            super.recordAnnonce(premiumQueryResult.getHeader().getMessageDate());

            // Enregistrement des données spécifiques ŕ l'annonce
            // On utilise une annonceSedex de class pour stocker les infos
            // On créera une annonce pour chaque réponse
            annonceSedex.setMessageEmetteur(premiumQueryResult.getHeader().getSenderId());
            annonceSedex.setMessageRecepteur(premiumQueryResult.getHeader().getRecipientId());
            annonceSedex.setMessageId(premiumQueryResult.getHeader().getMessageId());
            annonceSedex.setMessageHeader(getHeaderMessage(premiumQueryResult));
            annonceSedex.setNumeroCourant(premiumQueryResult.getHeader().getBusinessProcessId().toString());
            annonceSedex.setNumeroDecision(premiumQueryResult.getHeader().getMessageId());

            idTiersCM = AMSedexRPUtil.getIdTiersFromSedexId(premiumQueryResult.getHeader().getSenderId());
            annonceSedex.setIdTiersCM(idTiersCM);


            // Année traité
            annee = String.valueOf(premiumQueryResult.getContent().getPremiumQueryResult().getYear().getYear());
        } catch (Exception e) {
            _setErrorOnReception(annonceSedex, e, premiumQueryResult);
        }

        // On boucle sur chacune des réponses
        List<PremiumInsuredPersonType> premiumInsuredPersonTypes = premiumQueryResult.getContent().getPremiumQueryResult().getPremiumInsuredPerson();
        for (PremiumInsuredPersonType insuredPerson : premiumInsuredPersonTypes) {
            // Pour chaque personne dans la réponse, on crée une annonce.
            SimpleAnnonceSedex annonceSedexRetour = new SimpleAnnonceSedex();

            try {

                // On copy les infos des annonces setté plus haut.
                copyInfosCommunes(annonceSedexRetour);

                // On stocke la valeur du montant de prime tarifaire. (seuelement si pas de rejet)
                if(insuredPerson.getPremiumRejectReason() == null) {
                    try {
                        annonceSedexRetour.setMontantPrimeTarifaire(insuredPerson.getQueryDatePremium().setScale(2).toString());
                        annonceSedexRetour.setMessageContent(insuredPerson.getQueryDatePremium().setScale(2).toString());
                    } catch (Exception e) {
                        throw new Exception("Problčme avec la récupération prime pour le cas "
                                + insuredPerson.getPerson().getVn() + " / Annee = " + annee);
                    }
                }

                // Recherche des infos de la personne
                FamilleContribuableSearch familleContribuableSearch = new FamilleContribuableSearch();
                familleContribuableSearch.setForAnneeHistorique(annee);
                familleContribuableSearch.setForNNSS(nnssFormater.format(Long.toString(insuredPerson.getPerson().getVn())));
                familleContribuableSearch.setForDroitActifFromToday("0");
                familleContribuableSearch =  AmalImplServiceLocator.getFamilleContribuableService().search(familleContribuableSearch);

               if (familleContribuableSearch.getSize() == 1) {
                   // Une seule famille, on créé l'annonce sedex de réponse
                   FamilleContribuable familleContribuable = (FamilleContribuable) familleContribuableSearch.getSearchResults()[0];
                   saveAnnonceSedex(annonceSedexRetour, familleContribuable);

               } else if(familleContribuableSearch.getSize() > 1) {
                   // On a plusieurs dossier. Il faut ajouter la réponse a tous les dossiers qui ont une demande
                   // pour cette caisse et pour cette année la.
                   boolean aucuneDemandeTrouve = true;
                   for (JadeAbstractModel model : familleContribuableSearch.getSearchResults()){
                       FamilleContribuable familleContribuable = (FamilleContribuable) model;
                       SimpleAnnonceSedexSearch simpleAnnonceSedexSearch = new SimpleAnnonceSedexSearch();
                       simpleAnnonceSedexSearch.setForIdDetailFamille(familleContribuable.getSimpleDetailFamille().getIdDetailFamille());
                       simpleAnnonceSedexSearch.setForIdContribuable(familleContribuable.getSimpleContribuable().getIdContribuable());
                       simpleAnnonceSedexSearch.setForIdTiersCM(idTiersCM);
                       simpleAnnonceSedexSearch.setForMessageType(AMMessagesTypesAnnonceSedex.DEMANDE_PRIME_TARIFAIRE.getValue());
                       simpleAnnonceSedexSearch.setForMessageSubType(AMMessagesSubTypesAnnonceSedex.DEMANDE_PRIME_TARIFAIRE.getValue());

                       simpleAnnonceSedexSearch = AmalImplServiceLocator.getSimpleAnnonceSedexService().search(simpleAnnonceSedexSearch);

                       // On a bien une demande pour cette année, ce dossier et cette caisse, on ajoute l'annonce de réponse.
                       if(simpleAnnonceSedexSearch.getSize() > 0) {
                           aucuneDemandeTrouve = false; //On a bien au moins une demande parmis tous les dossiers de ce NSS et de cette année
                           saveAnnonceSedex(annonceSedexRetour, familleContribuable);
                       }
                   }

                   if(aucuneDemandeTrouve) {
                       // Si toujours rien trouvé, on lčve une erreur
                       throw new Exception("Pas de demande de prime tarifaire correspondante pour ce NSS = "
                               + insuredPerson.getPerson().getVn() + " / Annee = " + annee);
                   }
               } else {
                   // Cas ou l'on a aucune correspondance pour une réponse
                    throw new Exception("Aucun dossier correspondant pour le retour de demande tarifaire : NSS = "
                            + insuredPerson.getPerson().getVn() + " / Annee = " + annee);
                }

            } catch (Exception e) {
                _setErrorOnReception(annonceSedexRetour, e, premiumQueryResult);
            }
        }

        return annonceSedex;
    }

    private void saveAnnonceSedex(SimpleAnnonceSedex annonceSedexRetour, FamilleContribuable familleContribuable) throws JadePersistenceException, JadeApplicationServiceNotAvailableException, AnnonceSedexException, DetailFamilleException {
        annonceSedexRetour.setIdContribuable(familleContribuable.getSimpleContribuable().getIdContribuable());
        annonceSedexRetour.setIdDetailFamille(familleContribuable.getSimpleDetailFamille().getIdDetailFamille());
        AmalImplServiceLocator.getSimpleAnnonceSedexService().create(annonceSedexRetour);
    }

    @Override
    public StringBuffer getDetailsAnnonce() {
        StringBuffer stringBufferInfos = new StringBuffer();

        PremiumQueryResultType premiumQueryResultType = premiumQueryResult.getContent().getPremiumQueryResult();

        stringBufferInfos.append(getPremiumQueryResultTypeeInfos(premiumQueryResultType));

        return stringBufferInfos;
    }

    public String getContentMessage(Object message) {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setContent(((Message) message).getContent());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            JAXBServices.getInstance().marshal(m, os, false, false, new Class[]{});
        } catch (Exception e) {
            JadeLogger.error(this, e.getMessage());
        }
        return JadeStringUtil.decodeUTF8(os.toString());
    }

    public String getHeaderMessage(Object message) {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setHeader(((Message) message).getHeader());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            JAXBServices.getInstance().marshal(m, os, false, false, new Class[]{});
        } catch (Exception e) {
            JadeLogger.error(this, e.getMessage());
        }
        return JadeStringUtil.decodeUTF8(os.toString());
    }

    public StringBuffer getPremiumQueryResultTypeeInfos(PremiumQueryResultType premiumQueryResultType) {
        StringBuffer mapInfos = new StringBuffer();
        mapInfos.append("<strong>Données de l'assureur maladie :</strong></br>");

        // Jour de l'inventaire
        String jourInventaire = AMSedexRPUtil.getDateXMLToString(premiumQueryResultType.getInventoryDate());
        mapInfos.append("Jour inventaire : " + jourInventaire + "</br>");

        // Gestion de l'erreur
        if (StringUtils.isNotEmpty(premiumQueryResultType.getQueryRejectReason())) {
            int queryRejectReason = 0;
            try {
                queryRejectReason = Integer.valueOf(premiumQueryResultType.getQueryRejectReason());
            } catch (NumberFormatException e) {
                JadeLogger.error(this, "Code de QueryRejectReason non prévu :" + premiumQueryResultType.getQueryRejectReason());
            }
            mapInfos.append("Rejeté : Oui</br>");
            switch (queryRejectReason) {
                case 1:
                    mapInfos.append("Raison : l'année demandée est située trop loin dans le passé</br>");
                    break;
                case 2:
                    mapInfos.append("Raison : l'année demandée est située trop loin dans l’avenir</br>");
                    break;
                case 3:
                    mapInfos.append("Raison : la demande ne correspond pas ŕ l'OPC</br>");
                    break;
                default:
                    mapInfos.append("Raison : inconnu. L'erreur n'est pas prévu (" + premiumQueryResultType.getQueryRejectReason() + ")</br>");
            }
        }

        List<PremiumInsuredPersonType> premiumInsuredPersonTypes = premiumQueryResultType.getPremiumInsuredPerson();
        for (PremiumInsuredPersonType premiumInsuredPersonType : premiumInsuredPersonTypes) {

            // Prime tarifaire en francs et centimes le jour de l'inventaire
            mapInfos.append("Prime tarifaire :" + premiumInsuredPersonType.getInventoryDatePremium() + "</br>");

            // Prime tarifaire en francs et centimes pour le mois de janvier (resp. le début de l'assurance) de l'année demandée
            mapInfos.append("Prime tarifaire mois de janvier :" + premiumInsuredPersonType.getQueryDatePremium() + "</br>");

            // Raison pour laquelle aucune prime tarifaire n'est fournie pour l'année demandée
            // 1 = la personne est inconnue
            // 2 = la prime tarifaire est inconnue
            // 3 = la personne ne dispose d’aucune assurance AOS auprčs de l’assureur-maladie
            if (StringUtils.isNotEmpty(premiumInsuredPersonType.getPremiumRejectReason())) {
                int premiumRejectReason = 0;
                try {
                    premiumRejectReason = Integer.valueOf(premiumInsuredPersonType.getPremiumRejectReason());
                } catch (NumberFormatException e) {
                    JadeLogger.error(this, "Code de PremiumRejectReason non prévu :" + premiumInsuredPersonType.getPremiumRejectReason());
                }
                mapInfos.append("Rejeté : Oui</br>");
                switch (premiumRejectReason) {
                    case 1:
                        mapInfos.append("Raison : la personne est inconnue</br>");
                        break;
                    case 2:
                        mapInfos.append("Raison : la prime tarifaire est inconnue</br>");
                        break;
                    case 3:
                        mapInfos.append("la personne ne dispose d’aucune assurance AOS auprčs de l’assureur-maladie</br>");
                        break;
                    default:
                        mapInfos.append("Raison : inconnu. L'erreur n'est pas prévu (" + premiumInsuredPersonType.getPremiumRejectReason() + ")</br>");
                }
            }
        }
        ;

        mapInfos.append("***********************************************</br>");

        return mapInfos;
    }

    private void copyInfosCommunes(SimpleAnnonceSedex annonceSedex) {
        annonceSedex.setDateMessage(this.annonceSedex.getDateMessage());
        annonceSedex.setStatus(this.annonceSedex.getStatus());
        annonceSedex.setMessageEmetteur(this.annonceSedex.getMessageEmetteur());
        annonceSedex.setMessageRecepteur(this.annonceSedex.getMessageRecepteur());
        annonceSedex.setMessageId(this.annonceSedex.getMessageId());
        annonceSedex.setMessageHeader(this.annonceSedex.getMessageHeader());
        annonceSedex.setNumeroCourant(this.annonceSedex.getNumeroCourant());
        annonceSedex.setNumeroDecision(this.annonceSedex.getNumeroDecision());
        annonceSedex.setIdTiersCM(this.annonceSedex.getIdTiersCM());

        annonceSedex.setMessageSubType(AMMessagesSubTypesAnnonceSedex.REPONSE_PRIME_TARIFAIRE.getValue());
        annonceSedex.setMessageType(AMMessagesTypesAnnonceSedex.REPONSE_PRIME_TARIFAIRE.getValue());
        annonceSedex.setTraitement(IAMCodeSysteme.AMTraitementsAnnonceSedex.TRAITE_AUTO.getValue());
    }
}
