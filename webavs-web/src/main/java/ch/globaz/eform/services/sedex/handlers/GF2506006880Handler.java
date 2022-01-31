package ch.globaz.eform.services.sedex.handlers;

import ch.eahv_iv.xmlns.eahv_iv_common._4.NaturalPersonsOASIDIType;
import ch.globaz.eform.services.sedex.model.GFSedexModel;

import eform.ch.eahv_iv.xmlns.eahv_iv_2506_006880._1.*;

public class GF2506006880Handler extends GFFormHandler {
    @Override
    public boolean getDataFromFile(Object sedexObject, String zipFileLocation) {
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
                    //model.setZipFileLocation(zipFileLocation);
                    return true;
                }
            }
        }
        return false;
    }
}
