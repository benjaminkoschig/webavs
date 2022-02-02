package ch.globaz.eform.services.sedex.handlers;

import ch.globaz.eform.services.sedex.model.GFSedexModel;

import eform.ch.eahv_iv.xmlns.eahv_iv_2506_006880._1.*;
import globaz.jade.sedex.message.SimpleSedexMessage;

public class GF2506006880Handler extends GFFormHandler {
    @Override
    public boolean setDataFromFile(SimpleSedexMessage currentSimpleMessage, Object sedexObject, String currentSedexFolder) {
        Message message = (Message)sedexObject;
        if(sedexObject != null){
            model = new GFSedexModel();
            HeaderType header = message.getHeader();
            if(header != null) {
                model.setMessageId(header.getMessageId());
                model.setMessageSubject(header.getSubject());
                model.setMessageDate(header.getMessageDate().toGregorianCalendar().toZonedDateTime().toLocalDate());
                setAttachements(currentSimpleMessage.attachments);
                setBeneficiaireData(message.getContent() != null ? message.getContent().getInsuredPerson() : null);
                model.setZipFile(getZip(currentSedexFolder));
                return true;
            }
        }
        return false;
    }
}
