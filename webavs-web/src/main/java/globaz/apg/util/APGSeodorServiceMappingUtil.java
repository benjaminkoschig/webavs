package globaz.apg.util;

import ch.globaz.common.properties.CommonProperties;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.prestation.beans.PRPeriode;
import seodor.ch.eahv_iv.xmlns.eavh_iv_2014_000101._1.AddressInformationType;
import seodor.ch.eahv_iv.xmlns.eavh_iv_2014_000101._1.ContentType;
import seodor.ch.eahv_iv.xmlns.eavh_iv_2014_000101._1.InsurantDomicileType;
import seodor.ch.eahv_iv.xmlns.eavh_iv_2014_000101._1.InsurantType;
import seodor.ch.ech.xmlns.ech_0058._5.SendingApplicationType;
import seodor.ws.GetServicePeriodsRequestType;
import seodor.ws.GetServicePeriodsResponseType;
import seodor.ws.HeaderType;
import seodor.ws.ServicePeriodsRequestType;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class APGSeodorServiceMappingUtil {

    public static final String SOURCE_LOCATION = "";
    public static final String TEST_PREFIX = "T";
    public static final String MANUFACTURER = "Globaz SA";
    public static final String PRODUCT = "Webavs";
    public static final String PRODUCT_VERSION = "1-24-00";
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
        XMLGregorianCalendar dateDebut = seodorDataBean.getStartDate();
        dateDebut.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        servicePeriodsRequestType.setStartDate(dateDebut);
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
//        header.setMessageId(JadeUUIDGenerator.createStringUUID());
        // TODO Recup l'info sur label ou properties
        header.setMessageId("simple-request");
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

        XMLGregorianCalendar dateXML = dataTypeFac.newXMLGregorianCalendar(gCalendar);
        dateXML.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        return dateXML ;

    }

    public static String convertXMLDateGregorianToDateJJMMAAAAto(XMLGregorianCalendar date){

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        GregorianCalendar gCalendar = date.toGregorianCalendar();
        format.setTimeZone(gCalendar.getTimeZone());
        String dateString = format.format(gCalendar.getTime());
        return dateString;

    }

    public static final List<APGSeodorDataBean> putResponseDeliveryResultInApgSeodorDataBean(BSession session, GetServicePeriodsResponseType responseDelivery, APGSeodorDataBean seodorDataBean) {
        List<APGSeodorDataBean> seodorDataBeans = new ArrayList<>();

        if (Objects.nonNull(responseDelivery.getMessage().getContent()) && Objects.nonNull(responseDelivery.getMessage().getContent().getResponse().getPeriod())) {
            for (ContentType content : responseDelivery.getMessage().getContent().getResponse().getPeriod()) {
                APGSeodorDataBean seodorDataBeanTemp = new APGSeodorDataBean(seodorDataBean);
                seodorDataBeanTemp.setAddressInformation(Objects.nonNull(content.getAddress()) ? content.getAddress() : new AddressInformationType());
                seodorDataBeanTemp.setAnnotation(Objects.nonNull(content.getAnnotation()) ? content.getAnnotation() : "");
                seodorDataBeanTemp.setControlNumber(Objects.nonNull(content.getControlNumber()) ? content.getControlNumber() : 0);
                seodorDataBeanTemp.setDepartmentId(Objects.nonNull(content.getDepartmentId()) ? content.getDepartmentId() : "" );
                seodorDataBeanTemp.setEmailAddress(Objects.nonNull(content.getEmailAddress()) ? content.getEmailAddress() : "" );
                seodorDataBeanTemp.setInsurantDomicileType(Objects.nonNull(content.getInsurantDomicile()) ? content.getInsurantDomicile() : new InsurantDomicileType());
                seodorDataBeanTemp.setInsurantType(Objects.nonNull(content.getInsurant()) ? content.getInsurant() : new InsurantType());
                seodorDataBeanTemp.setMobilePhone(Objects.nonNull(content.getMobilePhone()) ? content.getMobilePhone() : "");
                seodorDataBeanTemp.setNumberOfDays(Objects.nonNull(content.getNumberOfDays()) ? content.getNumberOfDays() : 0 );
                seodorDataBeanTemp.setPersonalNumber(Objects.nonNull(content.getPersonalNumber()) ? content.getPersonalNumber() : "" );
                seodorDataBeanTemp.setReferenceNumber(Objects.nonNull(content.getReferenceNumber()) ? content.getReferenceNumber() : "" );
                seodorDataBeanTemp.setServiceEntryDate(Objects.nonNull(content.getServiceEntryDate()) ? content.getServiceEntryDate() : null);
                seodorDataBeanTemp.setServiceType(Objects.nonNull(content.getServiceType()) ? content.getServiceType() : 0);
                seodorDataBeanTemp.setStartOfPeriod(Objects.nonNull(content.getStartOfPeriod()) ? content.getStartOfPeriod() : null);
                seodorDataBeanTemp.setEndOfPeriod(Objects.nonNull(content.getEndOfPeriod()) ? content.getEndOfPeriod() : null);
                seodorDataBeanTemp.setUserId(Objects.nonNull(content.getUserId()) ? content.getUserId() : "" );
                seodorDataBeans.add(seodorDataBeanTemp);
            }
        }
        return seodorDataBeans;
    }

    /**
     * Méthode qui controle si une liste de periodes est bien défini sur SEODOR
     *
     * @param apgSeodorDataBeans
     * @param periodesAControler
     * @param nombreJoursAControler
     * @param messageErreur
     * @return
     * @throws ParseException
     * @throws DatatypeConfigurationException
     */
    public static APGSeodorErreurListEntities controlePeriodesSeodor(List<APGSeodorDataBean> apgSeodorDataBeans, List<PRPeriode> periodesAControler, Long nombreJoursAControler, String messageErreur
            , int genreService, int nombrePeriodeGenreService)
            throws ParseException, DatatypeConfigurationException {
        APGSeodorErreurListEntities periodesEnErreur = new APGSeodorErreurListEntities();
        boolean dejaAjouteErreur = false;
        Long nombreJoursSoldesCalcules = new Long(0);

        for (PRPeriode periodeTemp : periodesAControler) {
            boolean enErreur = true;
            XMLGregorianCalendar dateDeDebutPeriode = convertDateJJMMAAAAtoXMLDateGregorian(periodeTemp.getDateDeDebut().toString());
            XMLGregorianCalendar dateDeFinPeriode = convertDateJJMMAAAAtoXMLDateGregorian(periodeTemp.getDateDeFin().toString());
            for (APGSeodorDataBean apgSeodorDataBeanTemp : apgSeodorDataBeans) {
                if (Objects.equals(apgSeodorDataBeanTemp.getServiceType(), genreService)){
                    if (hasSamePeriodeXMLGregorian(apgSeodorDataBeanTemp.getStartOfPeriod(), dateDeDebutPeriode) &&
                            hasSamePeriodeXMLGregorian(apgSeodorDataBeanTemp.getEndOfPeriod(), dateDeFinPeriode)) {
                        enErreur = false;
                        nombreJoursSoldesCalcules = nombreJoursSoldesCalcules + apgSeodorDataBeanTemp.getNumberOfDays();
                    }
                } else {
                    // Si l'on est pas sur la même année, la période ne doit pas être comparée
                    // Il ne peut pas y avoir d'erreurs
                    enErreur = false;
                }
            }
            if (enErreur && !dejaAjouteErreur) {
                dejaAjouteErreur = true;
                periodesEnErreur = listPeriodeSeodorWithErrorMessage(apgSeodorDataBeans, messageErreur);
            }
        }
        if (!dejaAjouteErreur && !Objects.equals(nombreJoursSoldesCalcules, nombreJoursAControler)) {
            periodesEnErreur = listPeriodeSeodorWithErrorMessage(apgSeodorDataBeans, messageErreur);
        }


        return periodesEnErreur;
    }

    public static int calcNombrePeriodeGenreService(List<APGSeodorDataBean> apgSeodorDataBeans, int genreService) {
        int nombrePeriodeGenreService = 0;
        for(APGSeodorDataBean apgSeodorDataBeanTemp : apgSeodorDataBeans) {
            if (Objects.equals(apgSeodorDataBeanTemp.getServiceType(), genreService)) {
                nombrePeriodeGenreService++;
            }
        }
        return nombrePeriodeGenreService;
    }

    /**
     * Méthode qui liste les périodes récupérées par le WS Seodor
     *
     * @param apgSeodorDataBeans
     * @param messageErreur
     * @return APGSeodorErreurListEntities
     */
    private static APGSeodorErreurListEntities listPeriodeSeodorWithErrorMessage(List<APGSeodorDataBean> apgSeodorDataBeans, String messageErreur){
        APGSeodorErreurListEntities periodesEnErreur = new APGSeodorErreurListEntities();
        periodesEnErreur.setMessageErreur(messageErreur);
        for (APGSeodorDataBean apgSeodorTemps : apgSeodorDataBeans) {
            APGSeodorErreurEntity periodeApgTemp = new APGSeodorErreurEntity();
            periodeApgTemp.setDateDebutPeriode(convertXMLDateGregorianToDateJJMMAAAAto(apgSeodorTemps.getStartOfPeriod()));
            periodeApgTemp.setDateFinPeriode(convertXMLDateGregorianToDateJJMMAAAAto(apgSeodorTemps.getEndOfPeriod()));
            periodeApgTemp.setCodeService(apgSeodorTemps.getServiceType());
            periodeApgTemp.setNombreJours(apgSeodorTemps.getNumberOfDays());
            periodesEnErreur.getApgSeodorErreurEntityList().add(periodeApgTemp);
        }
        return periodesEnErreur;
    }

    /**
     * Méthode qui compare le jour / mois / année entre deux date au format XMLGregorianCalendar
     *
     * @param dateXmlGregorian1
     * @param dateXmlGregorian2
     * @return true si égale, false sinon
     * @throws ParseException
     * @throws DatatypeConfigurationException
     */
    private static boolean hasSamePeriodeXMLGregorian(XMLGregorianCalendar dateXmlGregorian1, XMLGregorianCalendar dateXmlGregorian2) throws ParseException, DatatypeConfigurationException {
        if (Objects.equals(dateXmlGregorian1.getYear(), dateXmlGregorian2.getYear())
                && Objects.equals(dateXmlGregorian1.getMonth(), dateXmlGregorian2.getMonth())
                && Objects.equals(dateXmlGregorian1.getDay(), dateXmlGregorian2.getDay())) {
            return true;
        }
        return false;
    }
}
