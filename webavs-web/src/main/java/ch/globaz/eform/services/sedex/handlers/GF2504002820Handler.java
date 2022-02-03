package ch.globaz.eform.services.sedex.handlers;

import ch.globaz.eform.services.sedex.model.GFSedexModel;
import eform.ch.eahv_iv.xmlns.eahv_iv_2504_002820._1.*;
import globaz.jade.sedex.message.SimpleSedexMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GF2504002820Handler extends GFFormHandler {
    @Override
    public boolean setDataFromFile(SimpleSedexMessage currentSimpleMessage, String currentSedexFolder) {
        if(message != null){
            try {
                Message messageToTreat = (Message) message;
                model = new GFSedexModel();
                HeaderType header = messageToTreat.getHeader();
                if (header != null) {
                    model.setMessageId(header.getMessageId());
                    model.setMessageSubject(header.getSubject());
                    model.setMessageDate(header.getMessageDate().toGregorianCalendar().toZonedDateTime().toLocalDate());
                    setAttachements(currentSimpleMessage.attachments);
                    setBeneficiaireData(messageToTreat.getContent() != null ? messageToTreat.getContent().getInsuredPerson() : null);
                    model.setZipFile(getZip(currentSedexFolder));
                    return true;
                }
            }catch(ClassCastException e){
                LOG.error("Erreur de type de message.", e);
            }
        }
        return false;
    }
}
