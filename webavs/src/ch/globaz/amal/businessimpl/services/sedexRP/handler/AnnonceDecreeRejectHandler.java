package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import java.io.ByteArrayOutputStream;
import ch.gdk_cds.xmlns.pv_5211_000103._3.DecreeRejectType;
import ch.gdk_cds.xmlns.pv_5211_000103._3.Message;
import ch.gdk_cds.xmlns.pv_5211_000103._3.ObjectFactory;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.AMMessagesTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;

/**
 * Gestionnaire de réception pour les annonces 'Rejet d'une décision'
 * 
 * @author cbu
 * @version XSD _1
 */
public class AnnonceDecreeRejectHandler extends AnnonceHandlerAbstract {
    private Message decreeReject;

    public AnnonceDecreeRejectHandler(Message decreeReject) {
        this.decreeReject = decreeReject;
    }

    @Override
    public SimpleAnnonceSedex execute() {
        try {
            // Enregistrement des données génériques
            super.recordAnnonce(decreeReject.getHeader().getMessageDate());

            // Enregistrement des données spécifiques à l'annonce
            annonceSedex.setMessageEmetteur(decreeReject.getHeader().getSenderId());
            annonceSedex.setMessageRecepteur(decreeReject.getHeader().getRecipientId());
            annonceSedex.setMessageId(decreeReject.getHeader().getMessageId());
            annonceSedex.setMessageContent(getContentMessage(decreeReject).toString());
            annonceSedex.setMessageHeader(getHeaderMessage(decreeReject).toString());
            annonceSedex.setMessageSubType(AMMessagesSubTypesAnnonceSedex.REJET_DECISION.getValue());
            annonceSedex.setMessageType(AMMessagesTypesAnnonceSedex.REJET_DECISION.getValue());
            annonceSedex.setNumeroCourant(decreeReject.getHeader().getBusinessProcessId().toString());
            annonceSedex.setNumeroDecision(decreeReject.getContent().getDecreeReject().getDecreeId());
            annonceSedex.setTraitement(AMTraitementsAnnonceSedex.A_TRAITER.getValue());

            SimpleAnnonceSedex parentAnnonceSedex = getRelatedDecree(decreeReject.getContent().getDecreeReject()
                    .getDecreeId(), decreeReject.getHeader().getReferenceMessageId());

            annonceSedex.setIdDetailFamille(parentAnnonceSedex.getIdDetailFamille());
            annonceSedex.setIdContribuable(parentAnnonceSedex.getIdContribuable());
            String idTiers = AMSedexRPUtil.getIdTiersFromSedexId(decreeReject.getHeader().getSenderId());
            annonceSedex.setIdTiersCM(idTiers);

            annonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().create(annonceSedex);

        } catch (Exception e) {
            _setErrorOnReception(annonceSedex, e, decreeReject);
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

        DecreeRejectType reject = decreeReject.getContent().getDecreeReject();

        stringBufferInfos.append("<strong>Données du rejet de la décision:</strong></br>");
        if (!(reject.getAgency() == null)) {
            stringBufferInfos.append("Canton : " + reject.getAgency() + "</br>");
        }

        if (!(reject.getDecreeRejectReason() == null)) {
            String reasonType = reject.getDecreeRejectReason();
            BSession currentSession = BSessionUtil.getSessionFromThreadContext();
            stringBufferInfos.append("Motif : " + currentSession.getLabel("SEDEXRP_DECREE_REJECT_REASON_" + reasonType)
                    + "</br>");
        }

        if (!(reject.getNewInsurance() == null)) {
            stringBufferInfos.append("Nouvel assureur : " + reject.getNewInsurance() + "</br>");
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
