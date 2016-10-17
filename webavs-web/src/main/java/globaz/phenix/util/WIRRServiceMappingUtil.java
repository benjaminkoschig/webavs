package globaz.phenix.util;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.pavo.util.CIUtil;
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
import ch.globaz.common.properties.CommonProperties;

public class WIRRServiceMappingUtil {

    public static final int SEARCH_DEPTH_VN = 0;
    public static final int SEARCH_DEPTH_PENSION = 1;
    public static final String MANUFACTURER = "Globaz SA";
    public static final String PRODUCT = "Webavs";
    public static final String PRODUCT_VERSION = "1.17.01";
    public static final String RECIPIENT_ID = "sedex://T6-599000-1";
    public static final String MESSAGE_TYPE = "2028";
    public static final String MESSAGE_SUBTYPE = "000101";
    public static final String ACTION = "5";
    public static final String DECLARATION_LOCAL_REFERENCE = "REF_SEARCH";

    public static final String ERROR_REASON_DETAILED_DONT_MATCH_XSD = "101";
    public static final String ERROR_REASON_DETAILED_WRONG_NSS = "131";
    public static final String ERROR_REASON_DETAILED_PRODUCTION_REQUEST_IN_TEST_ENVIRONMENT = "141";
    public static final String ERROR_REASON_DETAILED_TEST_REQUEST_IN_PRODUCTION_ENVIRONMENT = "142";
    public static final String ERROR_REASON_DETAILED_TOO_OLD_DATE_IN_SUPPRESSEDAFTER_TAG = "151";
    public static final String ERROR_REASON_DETAILED_NSS_MISSING_IN_UPI = "302";
    public static final String ERROR_REASON_DETAILED_WRONG_VALUE_IN_PARTNERID_TAG = "310";
    public static final String ERROR_REASON_DETAILED_SENDERID_DONT_MATCH_PARTNERID = "320";
    public static final String ERROR_REASON_DETAILED_UNEXCEPTED_ERROR = "10";

    public static final Delivery convertWirrDataBeanToRequestDelivery(WIRRDataBean wirrDataBean) throws Exception {

        SendingApplicationType sendingApplicationType = new SendingApplicationType();
        sendingApplicationType.setManufacturer(MANUFACTURER);
        sendingApplicationType.setProduct(PRODUCT);
        sendingApplicationType.setProductVersion(PRODUCT_VERSION);

        HeaderRequestType header = new HeaderRequestType();
        header.setSenderId("sedex://" + CommonProperties.WIRRWEBSERVICE_SEDEX_SENDER_ID.getValue());
        header.getRecipientId().add(RECIPIENT_ID);
        header.setDeclarationLocalReference(DECLARATION_LOCAL_REFERENCE);
        header.setMessageId(JadeUUIDGenerator.createStringUUID());
        header.setMessageType(MESSAGE_TYPE);
        header.setSubMessageType(MESSAGE_SUBTYPE);

        header.setMessageDate(convertDateJJMMAAAAtoXMLDateGregorian(JACalendar.todayJJsMMsAAAA()));

        header.setSendingApplication(sendingApplicationType);
        header.setAction(ACTION);

        SearchPerPersonAndPensionTypeType searchPerPersonAndPensionTypeType = new SearchPerPersonAndPensionTypeType();
        searchPerPersonAndPensionTypeType.setSearchDepthPension(SEARCH_DEPTH_PENSION);
        searchPerPersonAndPensionTypeType.setSearchDepthVn(SEARCH_DEPTH_VN);

        String numAVSNonFormate = CIUtil.unFormatAVS(wirrDataBean.getNss());
        searchPerPersonAndPensionTypeType.setVn(Long.valueOf(numAVSNonFormate));

        RequestType requestType = new RequestType();
        requestType.setPensionsInRegister(searchPerPersonAndPensionTypeType);

        Delivery requestDelivery = new Delivery();
        requestDelivery.setHeader(header);

        Delivery.Content deliveryContent = new Delivery.Content();
        deliveryContent.setRequest(requestType);

        requestDelivery.setContent(deliveryContent);

        return requestDelivery;

    }

    public static final WIRRDataBean putResponseDeliveryResultInWirrDataBean(BSession session,
            ch.admin.bsv.xmlns.ebsv_2028_000102._1.Delivery responseDelivery, WIRRDataBean wirrDataBean) {

        ch.admin.bsv.xmlns.ebsv_2028_000102._1.Delivery.Content.PositiveResponse positiveResponse = responseDelivery
                .getContent().getPositiveResponse();

        ch.admin.bsv.xmlns.ebsv_2028_000102._1.Delivery.Content.NegativeReport negativeResponse = responseDelivery
                .getContent().getNegativeReport();

        if (positiveResponse != null) {
            if (positiveResponse.getListOfPensions().getPension().size() >= 1) {
                wirrDataBean.setHasRenteWIRRFounded(true);
                wirrDataBean.setMessageForUser(session.getLabel("MESSAGE_WEBSERVICE_RENTE_EXISTANTE"));
            } else {
                wirrDataBean.setHasRenteWIRRFounded(false);
                wirrDataBean.setMessageForUser(session.getLabel("MESSAGE_WEBSERVICE_AUCUNE_RENTE_EXISTANTE"));
            }

        }

        if (negativeResponse != null) {
            wirrDataBean.setHasTechnicalError(true);
            wirrDataBean.setErrorReason(String.valueOf(negativeResponse.getReason()));
            wirrDataBean.setErrorDetailedReason(negativeResponse.getDetailedReason());
            wirrDataBean.setErrorComment(negativeResponse.getComment());

            if (ERROR_REASON_DETAILED_DONT_MATCH_XSD.equalsIgnoreCase(negativeResponse.getDetailedReason())) {
                wirrDataBean.setMessageForUser(session.getLabel("ERROR_REASON_DETAILED_DONT_MATCH_XSD"));

            } else if (ERROR_REASON_DETAILED_WRONG_NSS.equalsIgnoreCase(negativeResponse.getDetailedReason())) {
                wirrDataBean.setMessageForUser(session.getLabel("ERROR_REASON_DETAILED_WRONG_NSS"));
            } else if (ERROR_REASON_DETAILED_PRODUCTION_REQUEST_IN_TEST_ENVIRONMENT.equalsIgnoreCase(negativeResponse
                    .getDetailedReason())) {
                wirrDataBean.setMessageForUser(session
                        .getLabel("ERROR_REASON_DETAILED_PRODUCTION_REQUEST_IN_TEST_ENVIRONMENT"));
            } else if (ERROR_REASON_DETAILED_TEST_REQUEST_IN_PRODUCTION_ENVIRONMENT.equalsIgnoreCase(negativeResponse
                    .getDetailedReason())) {
                wirrDataBean.setMessageForUser(session
                        .getLabel("ERROR_REASON_DETAILED_TEST_REQUEST_IN_PRODUCTION_ENVIRONMENT"));
            } else if (ERROR_REASON_DETAILED_TOO_OLD_DATE_IN_SUPPRESSEDAFTER_TAG.equalsIgnoreCase(negativeResponse
                    .getDetailedReason())) {
                wirrDataBean.setMessageForUser(session
                        .getLabel("ERROR_REASON_DETAILED_TOO_OLD_DATE_IN_SUPPRESSEDAFTER_TAG"));
            } else if (ERROR_REASON_DETAILED_NSS_MISSING_IN_UPI.equalsIgnoreCase(negativeResponse.getDetailedReason())) {
                wirrDataBean.setMessageForUser(session.getLabel("ERROR_REASON_DETAILED_NSS_MISSING_IN_UPI"));
            } else if (ERROR_REASON_DETAILED_WRONG_VALUE_IN_PARTNERID_TAG.equalsIgnoreCase(negativeResponse
                    .getDetailedReason())) {
                wirrDataBean.setMessageForUser(session.getLabel("ERROR_REASON_DETAILED_WRONG_VALUE_IN_PARTNERID_TAG"));
            } else if (ERROR_REASON_DETAILED_SENDERID_DONT_MATCH_PARTNERID.equalsIgnoreCase(negativeResponse
                    .getDetailedReason())) {
                wirrDataBean.setMessageForUser(session.getLabel("ERROR_REASON_DETAILED_SENDERID_DONT_MATCH_PARTNERID"));
            } else if (ERROR_REASON_DETAILED_UNEXCEPTED_ERROR.equalsIgnoreCase(negativeResponse.getDetailedReason())) {
                wirrDataBean.setMessageForUser(session.getLabel("ERROR_REASON_DETAILED_UNEXCEPTED_ERROR"));
            }
        }

        return wirrDataBean;

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