package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import ch.gdk_cds.xmlns.pv_5215_000802._3.Message;
import ch.gdk_cds.xmlns.pv_5215_000802._3.ObjectFactory;
import ch.gdk_cds.xmlns.pv_5215_000802._3.PremiumInsuredPersonType;
import ch.gdk_cds.xmlns.pv_5215_000802._3.PremiumQueryResultType;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.AMMessagesTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedexSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
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
        try {
            // Enregistrement des données génériques
            super.recordAnnonce(premiumQueryResult.getHeader().getMessageDate());

            // Enregistrement des données spécifiques à l'annonce
            annonceSedex.setMessageEmetteur(premiumQueryResult.getHeader().getSenderId());
            annonceSedex.setMessageRecepteur(premiumQueryResult.getHeader().getRecipientId());
            annonceSedex.setMessageId(premiumQueryResult.getHeader().getMessageId());
            annonceSedex.setMessageContent(getContentMessage(premiumQueryResult).toString());
            annonceSedex.setMessageHeader(getHeaderMessage(premiumQueryResult).toString());
            annonceSedex.setMessageSubType(AMMessagesSubTypesAnnonceSedex.REPONSE_PRIME_TARIFAIRE.getValue());
            annonceSedex.setMessageType(AMMessagesTypesAnnonceSedex.REPONSE_PRIME_TARIFAIRE.getValue());
            annonceSedex.setNumeroCourant(premiumQueryResult.getHeader().getBusinessProcessId().toString());
            annonceSedex.setNumeroDecision(premiumQueryResult.getHeader().getMessageId());
            annonceSedex.setTraitement(IAMCodeSysteme.AMTraitementsAnnonceSedex.TRAITE_AUTO.getValue());

            String idTiers = AMSedexRPUtil.getIdTiersFromSedexId(premiumQueryResult.getHeader().getSenderId());
            annonceSedex.setIdTiersCM(idTiers);

            SimpleAnnonceSedexSearch annonceSedexSearch = new SimpleAnnonceSedexSearch();
            annonceSedexSearch.setForMessageId(premiumQueryResult.getHeader().getReferenceMessageId());
            annonceSedexSearch = AmalImplServiceLocator.getSimpleAnnonceSedexService().search(annonceSedexSearch);

            if (annonceSedexSearch.getSize() > 0) {
                SimpleAnnonceSedex annonceSedex = (SimpleAnnonceSedex) annonceSedexSearch.getSearchResults()[0];
                this.annonceSedex.setIdContribuable(annonceSedex.getIdContribuable());
                this.annonceSedex.setIdDetailFamille(annonceSedex.getIdDetailFamille());
            } else {
                throw new Exception("Annonce sedex not found for ReferenceMessageId : "
                        + premiumQueryResult.getHeader().getMessageId());
            }

            // Si on a une QueryRejectReason, on n'a pas de donnée pour le montant
            List<PremiumInsuredPersonType> premiumInsuredPersonTypes = premiumQueryResult.getContent().getPremiumQueryResult().getPremiumInsuredPerson();
            if (!premiumInsuredPersonTypes.isEmpty()) {
                annonceSedex.setMontantPrimeTarifaire(premiumInsuredPersonTypes.get(0).getQueryDatePremium().setScale(2).toString());
            } else {
                //throw new AnnonceSedexException("Aucune donnée de retour pour cette annonce");
            }

            annonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().create(annonceSedex);

        } catch (Exception e) {
            _setErrorOnReception(annonceSedex, e, premiumQueryResult);
        }

        return annonceSedex;
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
            JadeLogger.info(this, e.getMessage());
            e.printStackTrace();
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
            JadeLogger.info(this, e.getMessage());
            e.printStackTrace();
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
                JadeLogger.info(this, "Code de QueryRejectReason non prévu :" + premiumQueryResultType.getQueryRejectReason());
                e.printStackTrace();
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
                    mapInfos.append("Raison : la demande ne correspond pas à l'OPC</br>");
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
            // 3 = la personne ne dispose d’aucune assurance AOS auprès de l’assureur-maladie
            if (StringUtils.isNotEmpty(premiumInsuredPersonType.getPremiumRejectReason())) {
                int premiumRejectReason = 0;
                try {
                    premiumRejectReason = Integer.valueOf(premiumInsuredPersonType.getPremiumRejectReason());
                } catch (NumberFormatException e) {
                    JadeLogger.info(this, "Code de PremiumRejectReason non prévu :" + premiumInsuredPersonType.getPremiumRejectReason());
                    e.printStackTrace();
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
                        mapInfos.append("la personne ne dispose d’aucune assurance AOS auprès de l’assureur-maladie</br>");
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
}
