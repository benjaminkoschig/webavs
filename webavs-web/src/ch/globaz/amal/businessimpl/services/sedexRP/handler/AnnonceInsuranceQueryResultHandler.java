package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import java.io.ByteArrayOutputStream;
import ch.gdk_cds.xmlns.pv_5212_000402._3.Message;
import ch.gdk_cds.xmlns.pv_5212_000402._3.ObjectFactory;
import ch.gdk_cds.xmlns.pv_common._3.InsuranceDataType;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.AMMessagesTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedexSearch;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;

/**
 * Gestionnaire de réception pour les annonces 'Réponse du rapport d'assurance'
 * 
 * @author cbu
 * @version XSD _1
 */
public class AnnonceInsuranceQueryResultHandler extends AnnonceHandlerAbstract {
    private Message insuranceQueryResult;

    public AnnonceInsuranceQueryResultHandler(Message insuranceQueryResult) {
        this.insuranceQueryResult = insuranceQueryResult;
    }

    @Override
    public SimpleAnnonceSedex execute() {
        try {
            // Enregistrement des données génériques
            super.recordAnnonce(insuranceQueryResult.getHeader().getMessageDate());

            // Enregistrement des données spécifiques à l'annonce
            annonceSedex.setMessageEmetteur(insuranceQueryResult.getHeader().getSenderId());
            annonceSedex.setMessageRecepteur(insuranceQueryResult.getHeader().getRecipientId());
            annonceSedex.setMessageId(insuranceQueryResult.getHeader().getMessageId());
            annonceSedex.setMessageContent(getContentMessage(insuranceQueryResult).toString());
            annonceSedex.setMessageHeader(getHeaderMessage(insuranceQueryResult).toString());
            annonceSedex.setMessageSubType(AMMessagesSubTypesAnnonceSedex.REPONSE_RAPPORT_ASSURANCE.getValue());
            annonceSedex.setMessageType(AMMessagesTypesAnnonceSedex.REPONSE_RAPPORT_ASSURANCE.getValue());
            annonceSedex.setNumeroCourant(insuranceQueryResult.getHeader().getBusinessProcessId().toString());
            annonceSedex.setNumeroDecision(insuranceQueryResult.getHeader().getMessageId());
            annonceSedex.setTraitement(AMTraitementsAnnonceSedex.A_TRAITER.getValue());

            String idTiers = AMSedexRPUtil.getIdTiersFromSedexId(insuranceQueryResult.getHeader().getSenderId());
            annonceSedex.setIdTiersCM(idTiers);

            SimpleAnnonceSedexSearch annonceSedexSearch = new SimpleAnnonceSedexSearch();
            annonceSedexSearch.setForNumeroDecision(insuranceQueryResult.getHeader().getReferenceMessageId());
            annonceSedexSearch = AmalImplServiceLocator.getSimpleAnnonceSedexService().search(annonceSedexSearch);

            if (annonceSedexSearch.getSize() > 0) {
                SimpleAnnonceSedex annonceSedex = (SimpleAnnonceSedex) annonceSedexSearch.getSearchResults()[0];
                this.annonceSedex.setIdContribuable(annonceSedex.getIdContribuable());
                this.annonceSedex.setIdDetailFamille(annonceSedex.getIdDetailFamille());
            } else {
                throw new Exception("Original InsuranceQuery not found for messageId : "
                        + insuranceQueryResult.getHeader().getMessageId());
            }

            annonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().create(annonceSedex);

        } catch (Exception e) {
            _setErrorOnReception(annonceSedex, e, insuranceQueryResult);
        }

        return annonceSedex;
    }

    public String getContentMessage(Object message) {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setContent(((Message) message).getContent());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
            e.printStackTrace();
        }
        return JadeStringUtil.decodeUTF8(os.toString());
    }

    @Override
    public StringBuffer getDetailsAnnonce() {
        StringBuffer stringBufferInfos = new StringBuffer();

        InsuranceDataType insuranceDataType = insuranceQueryResult.getContent().getInsuranceQueryResult()
                .getInsuranceData();

        stringBufferInfos.append(getInsuranceDataTypeInfos(insuranceDataType));

        return stringBufferInfos;
    }

    public String getHeaderMessage(Object message) {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setHeader(((Message) message).getHeader());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});
        } catch (Exception e) {
            JadeLogger.info(this, e.getMessage());
            e.printStackTrace();
        }
        return JadeStringUtil.decodeUTF8(os.toString());
    }

}
