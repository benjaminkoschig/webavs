package ch.globaz.eform.businessimpl.services.sedex.envoi;

import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.eform.businessimpl.services.sedex.constant.GFManufacturerSedex;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.ContentType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.HeaderType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.Message;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.ObjectFactory;
import eform.ch.ech.xmlns.ech_0058._4.SendingApplicationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;


public class Sedex000102 implements SedexMessageSender<ContentType> {

    private static final Logger LOG = LoggerFactory.getLogger(Sedex000102.class);
    private final ObjectFactory factory = new ObjectFactory();


    public Sedex000102() {
    }

    @Override
    public void validate(ContentType content) throws ValidationException {
        if (content == null) {
            throw new CommonTechnicalException("ContentType is null unable to validate!");
        }

    }

    @Override
    public File generateMessageFile(ContentType content, String nom, String prenom) throws ValidationException {
        return null;
    }

    @Override
    public void sendMessages() {

    }

    @Override
    public SedexInfo getSedexInfo() {
        return null;
    }

    @Override
    public void close() throws IOException {

    }

    public Message createMessage(HeaderType header, ContentType content) {
        Message message = factory.createMessage();
        message.setHeader(header);
        message.setContent(content);
        return message;
    }


    public SendingApplicationType getSendingApplicationType() {
        SendingApplicationType sendingApplicationType = new SendingApplicationType();
        sendingApplicationType.setManufacturer(GFManufacturerSedex.WEBAVS.getManufacturer());
        sendingApplicationType.setProduct(GFManufacturerSedex.WEBAVS.getProduct());
        sendingApplicationType.setProductVersion(GFManufacturerSedex.WEBAVS.getProductVersion());
        return sendingApplicationType;
    }

    private XMLGregorianCalendar getMessageDate() throws DatatypeConfigurationException {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        return xmlGregorianCalendar;
    }




}
