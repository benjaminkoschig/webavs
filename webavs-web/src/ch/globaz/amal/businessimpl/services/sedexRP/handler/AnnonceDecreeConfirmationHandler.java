package ch.globaz.amal.businessimpl.services.sedexRP.handler;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import ch.gdk_cds.xmlns.pv_5211_000102._3.Message;
import ch.gdk_cds.xmlns.pv_5211_000102._3.ObjectFactory;
import ch.gdk_cds.xmlns.pv_common._3.InsuranceDataType;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.AMMessagesTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;

/**
 * Gestionnaire de réception pour les annonces 'Confirmation décision'
 * 
 * @author cbu
 * @version XSD _1
 */
public class AnnonceDecreeConfirmationHandler extends AnnonceHandlerAbstract {
    private Message decreeConfirmation;

    public AnnonceDecreeConfirmationHandler(Message decreeConfirmation) {
        this.decreeConfirmation = decreeConfirmation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.businessimpl.services.sedexRP.handler.AnnonceHandler#execute(java.util.HashMap)
     */
    @Override
    public SimpleAnnonceSedex execute() {
        try {
            // Enregistrement des données génériques
            super.recordAnnonce(decreeConfirmation.getHeader().getMessageDate());

            // Enregistrement des données spécifiques à l'annonce
            annonceSedex.setMessageEmetteur(decreeConfirmation.getHeader().getSenderId());
            annonceSedex.setMessageRecepteur(decreeConfirmation.getHeader().getRecipientId());
            annonceSedex.setMessageId(decreeConfirmation.getHeader().getMessageId());
            annonceSedex.setMessageContent(getContentMessage(decreeConfirmation).toString());
            annonceSedex.setMessageHeader(getHeaderMessage(decreeConfirmation).toString());
            annonceSedex.setMessageSubType(AMMessagesSubTypesAnnonceSedex.CONFIRMATION_DECISION.getValue());
            annonceSedex.setMessageType(AMMessagesTypesAnnonceSedex.CONFIRMATION_DECISION.getValue());
            annonceSedex.setNumeroCourant(decreeConfirmation.getHeader().getBusinessProcessId().toString());
            annonceSedex.setNumeroDecision(decreeConfirmation.getContent().getDecreeConfirmation().getDecreeId());
            annonceSedex.setTraitement(AMTraitementsAnnonceSedex.TRAITE_AUTO.getValue());

            SimpleAnnonceSedex parentAnnonceSedex = getRelatedDecree(decreeConfirmation.getContent()
                    .getDecreeConfirmation().getDecreeId(), decreeConfirmation.getHeader().getReferenceMessageId());

            annonceSedex.setIdDetailFamille(parentAnnonceSedex.getIdDetailFamille());
            annonceSedex.setIdContribuable(parentAnnonceSedex.getIdContribuable());

            String idTiers = AMSedexRPUtil.getIdTiersFromSedexId(decreeConfirmation.getHeader().getSenderId());
            annonceSedex.setIdTiersCM(idTiers);

            annonceSedex = AmalImplServiceLocator.getSimpleAnnonceSedexService().create(annonceSedex);

            try {
                SimpleDetailFamille subside = AmalImplServiceLocator.getSimpleDetailFamilleService().read(
                        annonceSedex.getIdDetailFamille());
                BigDecimal contrib = new BigDecimal(subside.getMontantContributionAvecSupplExtra());
                BigDecimal prime = decreeConfirmation.getContent().getDecreeConfirmation().getInsuranceData()
                        .getPremium();
                BigDecimal primeExact = contrib.subtract(prime);

                subside.setMontantPrimeAssurance(prime.setScale(2).toString());
                subside.setMontantExact(primeExact.setScale(2).toString());

                subside = AmalImplServiceLocator.getSimpleDetailFamilleService().update(subside);
            } catch (Exception e) {
                // On ne remonte pas d'erreur mais la prime n'est pas enregistrée. Etant donnée que ce n'est qu'un but
                // information, on ne fait rien d'autre
            }

        } catch (Exception e) {
            _setErrorOnReception(annonceSedex, e, decreeConfirmation);
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
        stringBufferInfos.append("Annonce : 'Confirmation de décision'</br></br>");

        InsuranceDataType insuranceDataType = decreeConfirmation.getContent().getDecreeConfirmation()
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
