package ch.globaz.eform.services.sedex.handlers;

import ch.eahv_iv.xmlns.eahv_iv_common._4.NaturalPersonsOASIDIType;
import ch.eahv_iv.xmlns.eahv_iv_common._4.AttachmentFileType;
import ch.globaz.eform.services.sedex.model.GFSedexModel;
import eform.ch.eahv_iv.xmlns.eahv_iv_2501_001800._1.*;
import eform.eahv_iv.afv_common.AttachmentType;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class GF2501001800Handler extends GFFormHandler {
    @Override
    public boolean getDataFromFile(Object sedexObject, String xmlFileLocation) throws IOException {
        Message message = (Message)sedexObject;
        if(sedexObject != null){
            model = new GFSedexModel();
            HeaderType header = message.getHeader();
            if(header != null) {
                model.setMessageId(header.getMessageId());
                model.setMessageSubject(header.getSubject());
                model.setMessageDate(header.getMessageDate().toGregorianCalendar().toZonedDateTime().toLocalDate());
                NaturalPersonsOASIDIType person = message.getContent() != null ? message.getContent().getInsuredPerson() : null;
                if (person != null){
                    model.setNssBeneficiaire(person.getVn().toString());
                    model.setNomBeneficiaire(person.getOfficialName());
                    model.setPrenomBenefiaicaire(person.getFirstName());
                    model.setNaissanceBeneficiaire(person.getDateOfBirth().getYearMonthDay().toGregorianCalendar().toZonedDateTime().toLocalDate());
                    //model.setZipFile(createZipOutputStream(message, xmlFileLocation));
                    return true;
                }
            }
        }
        return false;
    }


}
