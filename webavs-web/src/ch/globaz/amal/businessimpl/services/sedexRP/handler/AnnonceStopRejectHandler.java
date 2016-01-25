package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import java.io.ByteArrayOutputStream;
import ch.gdk_cds.xmlns.pv_5211_000203._3.Message;
import ch.gdk_cds.xmlns.pv_5211_000203._3.ObjectFactory;
import ch.gdk_cds.xmlns.pv_5211_000203._3.StopRejectType;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.AMMessagesTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;

/**
 * Gestionnaire de réception pour les annonces 'Rejet d'une interruption'
 * 
 * @author cbu
 * @version XSD _1
 */
public class AnnonceStopRejectHandler extends AnnonceHandlerAbstract {
    private Message stopReject;

    /**
     * @param stopReject
     */
    public AnnonceStopRejectHandler(Message stopReject) {
        this.stopReject = stopReject;
    }

    @Override
    public SimpleAnnonceSedex execute() {
        try {
            // Enregistrement des données génériques
            super.recordAnnonce(stopReject.getHeader().getMessageDate());

            // Enregistrement des données spécifiques à l'annonce
            annonceSedex.setMessageEmetteur(stopReject.getHeader().getSenderId());
            annonceSedex.setMessageRecepteur(stopReject.getHeader().getRecipientId());
            annonceSedex.setMessageId(stopReject.getHeader().getMessageId());
            annonceSedex.setMessageContent(getContentMessage(stopReject).toString());
            annonceSedex.setMessageHeader(getHeaderMessage(stopReject).toString());
            annonceSedex.setMessageSubType(AMMessagesSubTypesAnnonceSedex.REJET_INTERRUPTION.getValue());
            annonceSedex.setMessageType(AMMessagesTypesAnnonceSedex.REJET_INTERRUPTION.getValue());
            annonceSedex.setNumeroCourant(stopReject.getHeader().getBusinessProcessId().toString());
            annonceSedex.setNumeroDecision(stopReject.getContent().getStopReject().getDecreeId());
            annonceSedex.setTraitement(AMTraitementsAnnonceSedex.A_TRAITER.getValue());

            SimpleAnnonceSedex parentAnnonceSedex = getRelatedDecree(stopReject.getContent().getStopReject()
                    .getDecreeId(), stopReject.getHeader().getReferenceMessageId());

            annonceSedex.setIdDetailFamille(parentAnnonceSedex.getIdDetailFamille());
            annonceSedex.setIdContribuable(parentAnnonceSedex.getIdContribuable());
            String idTiers = AMSedexRPUtil.getIdTiersFromSedexId(stopReject.getHeader().getSenderId());
            annonceSedex.setIdTiersCM(idTiers);

            annonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().create(annonceSedex);

        } catch (Exception e) {
            _setErrorOnReception(annonceSedex, e, stopReject);
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

        StopRejectType stopRejectType = stopReject.getContent().getStopReject();
        stringBufferInfos.append("<strong>Données du rejet de l'interruption :</strong></br>");
        stringBufferInfos.append("DecreeId : " + stopRejectType.getDecreeId());
        stringBufferInfos.append("</br>");

        if (!JadeStringUtil.isBlankOrZero(stopRejectType.getStopRejectReason())) {
            BSession currentSession = BSessionUtil.getSessionFromThreadContext();
            String motif = currentSession
                    .getLabel("SEDEXRP_STOP_REJECT_REASON_" + stopRejectType.getStopRejectReason());
            stringBufferInfos.append("Motif de rejet : " + motif + "</br>");
        }
        stringBufferInfos.append("</br>");

        String stopMonth = AMSedexRPUtil.getDateXMLToString(stopRejectType.getStopMonth());

        if (!JadeStringUtil.isBlankOrZero(stopMonth)) {
            stringBufferInfos.append("Décision déjà interrompue au : " + stopMonth + "</br>");
        }
        stringBufferInfos.append("***********************************************</br>");

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
