package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import java.io.ByteArrayOutputStream;
import ch.gdk_cds.xmlns.pv_5211_000202._3.Message;
import ch.gdk_cds.xmlns.pv_5211_000202._3.ObjectFactory;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.AMMessagesTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;

/**
 * Gestionnaire de réception pour les annonces 'Confirmation d'une interruption'
 * 
 * @author cbu
 * @version XSD _1
 */
public class AnnonceStopConfirmationHandler extends AnnonceHandlerAbstract {
    private Message stopConfirmation;

    public AnnonceStopConfirmationHandler(Message stopConfirmation) {
        this.stopConfirmation = stopConfirmation;
    }

    @Override
    public SimpleAnnonceSedex execute() {
        try {
            // Enregistrement des données génériques
            super.recordAnnonce(stopConfirmation.getHeader().getMessageDate());

            // Enregistrement des données spécifiques à l'annonce
            annonceSedex.setMessageEmetteur(stopConfirmation.getHeader().getSenderId());
            annonceSedex.setMessageRecepteur(stopConfirmation.getHeader().getRecipientId());
            annonceSedex.setMessageId(stopConfirmation.getHeader().getMessageId());
            annonceSedex.setMessageContent(getContentMessage(stopConfirmation).toString());
            annonceSedex.setMessageHeader(getHeaderMessage(stopConfirmation).toString());
            annonceSedex.setMessageSubType(AMMessagesSubTypesAnnonceSedex.CONFIRMATION_INTERRUPTION.getValue());
            annonceSedex.setMessageType(AMMessagesTypesAnnonceSedex.CONFIRMATION_INTERRUPTION.getValue());
            annonceSedex.setNumeroCourant(stopConfirmation.getHeader().getBusinessProcessId().toString());
            annonceSedex.setNumeroDecision(stopConfirmation.getContent().getStopConfirmation().getDecreeId());
            annonceSedex.setTraitement(AMTraitementsAnnonceSedex.TRAITE_AUTO.getValue());

            SimpleAnnonceSedex parentAnnonceSedex = getRelatedDecree(stopConfirmation.getContent()
                    .getStopConfirmation().getDecreeId(), stopConfirmation.getHeader().getReferenceMessageId());

            annonceSedex.setIdDetailFamille(parentAnnonceSedex.getIdDetailFamille());
            annonceSedex.setIdContribuable(parentAnnonceSedex.getIdContribuable());
            String idTiers = AMSedexRPUtil.getIdTiersFromSedexId(stopConfirmation.getHeader().getSenderId());
            annonceSedex.setIdTiersCM(idTiers);

            annonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().create(annonceSedex);

        } catch (Exception e) {
            _setErrorOnReception(annonceSedex, e, stopConfirmation);
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

        stringBufferInfos.append("<strong>Données de la confirmation d'interruption :</strong></br>");
        stringBufferInfos.append("DecreeId : " + stopConfirmation.getContent().getStopConfirmation().getDecreeId());
        stringBufferInfos.append("***********************************************</br>");
        stringBufferInfos.append("</br>");

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
