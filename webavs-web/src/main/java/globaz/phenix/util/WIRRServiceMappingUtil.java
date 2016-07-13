package globaz.phenix.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.admin.bsv.xmlns.ebsv_2028_000000._1.RequestType;
import ch.admin.bsv.xmlns.ebsv_2028_000000._1.SearchPerPersonAndPensionTypeType;
import ch.admin.bsv.xmlns.ebsv_2028_000101._1.Delivery;
import ch.ech.xmlns.ech_0058._4.HeaderRequestType;
import ch.ech.xmlns.ech_0058._4.SendingApplicationType;

public class WIRRServiceMappingUtil {

    public static final Delivery convertWirrDataBeanToRequestDelivery(WIRRDataBean wirrDataBean) {

        SendingApplicationType sendingApplicationType = new SendingApplicationType();
        sendingApplicationType.setManufacturer("todo");
        sendingApplicationType.setProduct("todo");
        sendingApplicationType.setProductVersion("todo");

        HeaderRequestType header = new HeaderRequestType();
        header.setSenderId("todo");
        header.setMessageId("todo");
        header.setMessageType("todo");

        try {
            header.setMessageDate(convertDateJJMMAAAAtoXMLDateGregorian("todo"));
        } catch (Exception e) {
            // todo
        }

        header.setSendingApplication(sendingApplicationType);
        header.setAction("todo");

        SearchPerPersonAndPensionTypeType searchPerPersonAndPensionTypeType = new SearchPerPersonAndPensionTypeType();
        searchPerPersonAndPensionTypeType.setSearchDepthPension(1);
        searchPerPersonAndPensionTypeType.setSearchDepthVn(0);
        searchPerPersonAndPensionTypeType.setVn(123456789);

        RequestType requestType = new RequestType();
        requestType.setPensionsInRegister(searchPerPersonAndPensionTypeType);

        Delivery requestDelivery = new Delivery();
        requestDelivery.setHeader(header);

        Delivery.Content deliveryContent = new Delivery.Content();
        deliveryContent.setRequest(requestType);

        requestDelivery.setContent(deliveryContent);

        return requestDelivery;

    }

    public static final WIRRDataBean convertResponseDeliveryToWirrDataBean(
            ch.admin.bsv.xmlns.ebsv_2028_000102._1.Delivery responseDelivery) {
        return new WIRRDataBean();
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