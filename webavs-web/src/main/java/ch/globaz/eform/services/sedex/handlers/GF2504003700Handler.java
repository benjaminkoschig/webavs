package ch.globaz.eform.services.sedex.handlers;

import ch.eahv_iv.xmlns.eahv_iv_common._4.AttachmentFileType;
import ch.eahv_iv.xmlns.eahv_iv_common._4.NaturalPersonsOASIDIType;
import ch.globaz.eform.services.sedex.model.GFSedexModel;

import eform.ch.eahv_iv.xmlns.eahv_iv_2504_003700._1.*;
import eform.eahv_iv.afv_common.AttachmentType;
import globaz.jade.common.Jade;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class GF2504003700Handler extends GFFormHandler {
    @Override
    public boolean getDataFromFile(Object sedexObject, String xmlFileLocation) {
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
                    model.setZipFile(createZip(message, xmlFileLocation));
                    return true;
                }
            }
        }
        return false;
    }

    protected File createZip(Message message, String fileLocation) {
        File f = new File(Jade.getInstance().getPersistenceDir() + message.getHeader().getMessageId() + ".zip");

        try (FileOutputStream fileOutputStream = new FileOutputStream(f);
             ZipOutputStream out = new ZipOutputStream(fileOutputStream)) {

            zipEntryFile(fileLocation, out);
            for (AttachmentType attachement :
                    message.getHeader().getAttachment()) {
                for (AttachmentFileType attachement2 :
                        attachement.getFile()) {
                    ZipEntry entry = new ZipEntry(attachement2.getPathFileName());
                    out.putNextEntry(entry);
                }
            }
            return f;
        }catch(NullPointerException | IllegalArgumentException | IOException e){
            LOG.error("");
        }
        return null;
    }
}
