package globaz.apg.util;

import ch.admin.cdc.rapg.core.dto.generated._1.RapgAnnoncesRequestType;
import ch.admin.cdc.seodor.core.dto.generated._1.GetServicePeriodsRequestType;
import ch.admin.cdc.seodor.core.dto.generated._1.ServicePeriodsRequestType;
import ch.eahv.rapg.common._4.DeliveryOfficeType;
import ch.admin.cdc.seodor.core.dto.generated._1.HeaderType;
import ch.globaz.common.properties.CommonProperties;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeUUIDGenerator;
import ch.ech.ech0058._5.SendingApplicationType;


import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class APGSeodorServiceMappingUtil {

    public static final String SOURCE_LOCATION = "";
    public static final String TEST_PREFIX = "T";
    public static final String MANUFACTURER = "Globaz SA";
    public static final String PRODUCT = "Webavs";
    public static final String PRODUCT_VERSION = "1.24.0";
    public static final String RECIPIENT_ID = "6-600024-1";
    public static final String SEDEX_PREFIX = "sedex://";
    public static final String DECLARATION_LOCAL_REFERENCE = "REF_SEARCH";
    public static final String ACTION = "1";
    public static final String MESSAGE_TYPE = "2017";
    public static final String MESSAGE_SUBTYPE = "000101";
    public static final GetServicePeriodsRequestType convertSeodorDataBeanToRequestDelivery(APGSeodorDataBean seodorDataBean) throws Exception {
        String senderId = CommonProperties.SEODOR_WEBSERVICE_SEDEX_SENDER_ID.getValue();
        String recipientId;
        ServicePeriodsRequestType servicePeriodsRequestType = new ServicePeriodsRequestType();
        servicePeriodsRequestType.setVn(Long.parseLong(seodorDataBean.getNss()));
        servicePeriodsRequestType.setStartDate(seodorDataBean.getStartDate());
        SendingApplicationType sendingApplicationType = new SendingApplicationType();
        sendingApplicationType.setManufacturer(MANUFACTURER);
        sendingApplicationType.setProduct(PRODUCT);
        sendingApplicationType.setProductVersion(PRODUCT_VERSION);
        HeaderType header = new HeaderType();
        header.setSenderId(CommonProperties.SEODOR_WEBSERVICE_SEDEX_SENDER_ID.getValue());

        if (senderId.startsWith(TEST_PREFIX)) {
            header.setTestDeliveryFlag(true);
            recipientId = TEST_PREFIX + RECIPIENT_ID;
        } else {
            header.setTestDeliveryFlag(false);
            recipientId = RECIPIENT_ID;
        }
        header.setSenderId(senderId);
        header.setRecipientId(recipientId);
        header.setDeclarationLocalReference(DECLARATION_LOCAL_REFERENCE);
        header.setMessageId(JadeUUIDGenerator.createStringUUID());
        header.setMessageType(MESSAGE_TYPE);
        header.setSubMessageType(MESSAGE_SUBTYPE);
        header.setMessageDate(convertDateJJMMAAAAtoXMLDateGregorian(JACalendar.todayJJsMMsAAAA()));
        header.setSendingApplication(sendingApplicationType);
        header.setAction(ACTION);



        GetServicePeriodsRequestType request = new GetServicePeriodsRequestType();
        GetServicePeriodsRequestType.Message message = new GetServicePeriodsRequestType.Message();
        GetServicePeriodsRequestType.Message.Content content= new  GetServicePeriodsRequestType.Message.Content();
        content.setRequest(servicePeriodsRequestType);
        message.setContent(content);
        message.setHeader(header);
        request.setMessage(message);
        return request;
    }
    public static XMLGregorianCalendar convertDateJJMMAAAAtoXMLDateGregorian(String date) throws ParseException,
            DatatypeConfigurationException {

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(format.parse(date));
        DatatypeFactory dataTypeFac = DatatypeFactory.newInstance();

        return dataTypeFac.newXMLGregorianCalendar(gCalendar);

    }
}
