/**
 *
 */
package globaz.apg.businessimpl.service;

import ch.ech.xmlns.ech_0058._4.ReportType;
import ch.ech.xmlns.ech_0090._1.EnvelopeType;
import globaz.apg.ApgServiceLocator;
import globaz.apg.business.service.APAnnoncesRapgService;
import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.exceptions.APAnnoncesException;
import globaz.apg.exceptions.APPlausibilitesException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.rapg.messages.v5.Message101;
import globaz.apg.rapg.messages.v5.Message301;
import globaz.apg.rapg.messages.v5.Message401;
import globaz.apg.rapg.messages.v5.MessageRAPG;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.common.Jade;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBUtil;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.webavs.common.WebavsDocumentionLocator;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000101._5.Message;
import rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000101._5.ObjectFactory;
import rapg.ch.eahv_iv.xmlns.eahv_iv_2015_common._5.InsurantDomicileType;
import rapg.ch.ech.xmlns.ech_0044._2.PersonIdentificationType;
import rapg.ch.eahv_iv.xmlns.eahv_iv_2015_common._5.PaternityLeaveDataType;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author dde
 */
public class APAnnoncesRapgServiceV5Impl implements APAnnoncesRapgService {

    private static final String XSD_FOLDER = "/xsd/rapg/xsd/";
    private static final String XSD_101 = "eahv-iv-2015-000101-5-0.xsd";
    private static final String XSD_301 = "eahv-iv-2015-000301-5-0.xsd";
    private static final String XSD_401 = "eahv-iv-2015-000401-5-0.xsd";
    private static final String XSD_COMMON = "eahv-iv-2015-common-5-0.xsd";
    private static final String XSD_44 = "eCH-0044-2-0f.xsd";
    private static final String XSD_58 = "eCH-0058-4-0.xsd";
    private static final String TEMP_FILENAME = "marshalled.xml";
    public static final String ANNONCE_N = "[Annonce n°";

    private static final Logger LOG = LoggerFactory.getLogger(APAnnoncesRapgServiceV5Impl.class);
    private static final String SERVICE_PATERNITE = "91";

    private MessageRAPG addContent(MessageRAPG message, APChampsAnnonce champsAnnonce) throws DatatypeConfigurationException {
        if (!JadeStringUtil.isEmpty(champsAnnonce.getTimeStamp())) {
            message.setTimeStamp(JAXBUtil.getXmlCalendarTimestamp(Long.parseLong(champsAnnonce.getTimeStamp())));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getDeliveryOfficeOfficeIdentifier())) {
            message.getDeliveryOffice().setOfficeIdentifier(
                    Integer.valueOf(champsAnnonce.getDeliveryOfficeOfficeIdentifier()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getDeliveryOfficeBranch())) {
            message.getDeliveryOffice().setBranch(Integer.valueOf(champsAnnonce.getDeliveryOfficeBranch()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getInsurant())) {
            message.getInsurant().setVn(Long.valueOf(champsAnnonce.getInsurant().replace(".", "")));
        }
        if (!JadeStringUtil.isBlankOrZero(champsAnnonce.getInsurantMaritalStatus())) {
            message.setInsurantMaritalStatus(new BigInteger(champsAnnonce.getInsurantMaritalStatus()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getNumberOfChildren())
                && !"90".equals(champsAnnonce.getServiceType())) {
            message.setNumberOfChildren(Short.valueOf(champsAnnonce.getNumberOfChildren()));
        }
        if (!JadeStringUtil.isBlankOrZero(champsAnnonce.getInsurantDomicileCanton())) {
            message.getInsurantDomicile().setCanton(new BigInteger(champsAnnonce.getInsurantDomicileCanton()));
        }
        if (!JadeStringUtil.isBlankOrZero(champsAnnonce.getInsurantDomicileCountry())) {
            message.getInsurantDomicile().setCountry(Integer.parseInt(champsAnnonce.getInsurantDomicileCountry()));
        }else{
            message.setInsurantDomicile(null);
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getAccountingMonth())) {
            message.setAccountingMonth(JAXBUtil.getXmlCalendarDateMonthYear(JadeDateUtil.getGlobazDate("01."
                    + champsAnnonce.getAccountingMonth())));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getServiceType())) {
            message.setServiceType(new BigInteger(champsAnnonce.getServiceType()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getReferenceNumber())) {
            message.setReferenceNumber(Integer.valueOf(champsAnnonce.getReferenceNumber()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getControlNumber())) {
            message.setControlNumber(Long.valueOf(champsAnnonce.getControlNumber()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getActivityBeforeService())) {
            message.setActivityBeforeService(new BigInteger(champsAnnonce.getActivityBeforeService()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getAverageDailyIncome())) {
            message.setAverageDailyIncome(new BigDecimal(champsAnnonce.getAverageDailyIncome()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getStartOfPeriod())) {
            message.setStartOfPeriod(JAXBUtil.getXmlCalendarDate(champsAnnonce.getStartOfPeriod()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getEndOfPeriod())) {
            message.setEndOfPeriod(JAXBUtil.getXmlCalendarDate(champsAnnonce.getEndOfPeriod()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getNumberOfDays())) {
            message.setNumberOfDays(Short.valueOf(champsAnnonce.getNumberOfDays()));
        }
        if (!JadeStringUtil.isBlankOrZero(champsAnnonce.getBasicDailyAmount())) {
            message.setBasicDailyAmount(new BigDecimal(champsAnnonce.getBasicDailyAmount()));
        }
        message.setDailyIndemnityGuaranteeAI(champsAnnonce.getDailyIndemnityGuaranteeAI());
        message.setAllowanceFarm(champsAnnonce.getAllowanceFarm());
        if (!JadeStringUtil.isEmpty(champsAnnonce.getAllowanceCareExpenses())) {
            message.setAllowanceCareExpenses(new BigDecimal(champsAnnonce.getAllowanceCareExpenses()));
        }
        if (!JadeStringUtil.isBlankOrZero(champsAnnonce.getTotalAPG())) {
            message.setTotalAPG(new BigDecimal(champsAnnonce.getTotalAPG()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getPaymentMethod())) {
            message.setPaymentMethod(new BigInteger(champsAnnonce.getPaymentMethod()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getBreakRules())) {
            for (String br : champsAnnonce.getBreakRules().split(",")) {
                message.getBreakRules().getBreakRuleCode().add(new BigInteger(br));
            }
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getTimeStamp())) {
            message.setTimeStamp(JAXBUtil.getXmlCalendarTimestamp(Long.parseLong(champsAnnonce.getTimeStamp())));
        }
        //Paternité
        if (champsAnnonce.getServiceType().equals(SERVICE_PATERNITE) && !champsAnnonce.getAction().equals("4")) {
            rapg.ch.eahv_iv.xmlns.eahv_iv_2015_common._5.ObjectFactory factory = new rapg.ch.eahv_iv.xmlns.eahv_iv_2015_common._5.ObjectFactory();
            PaternityLeaveDataType paternityLeaveDataType = factory.createPaternityLeaveDataType();
            if (!JadeStringUtil.isEmpty(champsAnnonce.getNewbornDateOfBirth())) {
                paternityLeaveDataType.setNewbornDateOfBirth(JAXBUtil.getXmlCalendarDate(champsAnnonce.getNewbornDateOfBirth()));
            }
            if (!JadeStringUtil.isEmpty(champsAnnonce.getNumberOfWorkdays())) {
                paternityLeaveDataType.setNumberOfWorkdays(Short.valueOf(champsAnnonce.getNumberOfWorkdays()));
            }
            if (!JadeStringUtil.isEmpty(champsAnnonce.getParternityLeaveType())) {
                paternityLeaveDataType.setPaternityLeaveType(new BigInteger(champsAnnonce.getParternityLeaveType()));
            }
            rapg.ch.eahv_iv.xmlns.eahv_iv_2015_common._5.InsurantDomicileType insurantDomicileType = factory.createInsurantDomicileType();
            if (!JadeStringUtil.isEmpty(champsAnnonce.getChildCantonBorn()) && champsAnnonce.getChildDomicile().equals(PRTiersHelper.ID_PAYS_SUISSE)) {
                insurantDomicileType.setCanton(new BigInteger(champsAnnonce.getChildCantonBorn()));
            }
            if (!JadeStringUtil.isEmpty(champsAnnonce.getChildDomicile())) {
                insurantDomicileType.setCountry(Integer.parseInt(champsAnnonce.getChildDomicile()));
            }

            paternityLeaveDataType.setChildDomicile(insurantDomicileType);
            if (!JadeStringUtil.isEmpty(champsAnnonce.getChildInsurantVn())) {
                paternityLeaveDataType.setChildInsurantVn(Long.valueOf(champsAnnonce.getChildInsurantVn().replace(".", "")));
            }
            message.setPaternityLeaveData(paternityLeaveDataType);
        }
        return message;
    }

    private MessageRAPG addHeader(MessageRAPG message, APChampsAnnonce champsAnnonce) throws DatatypeConfigurationException {

        // Ajouter les informations de l'application
        message.getSendingApplication().setManufacturer("GLOBAZ SA");
        message.getSendingApplication().setProduct("Web@AVS");
        String version = WebavsDocumentionLocator.getVersion();
        if (version.length() > 10) {
            version = version.substring(0, 9);
        }
        message.getSendingApplication().setProductVersion(version);

        message.setSenderId(champsAnnonce.getSenderId());
        message.setRecipientId(champsAnnonce.getRecipientId());
        message.setMessageId(champsAnnonce.getMessageId());
        if (!JadeStringUtil.isBlankOrZero(champsAnnonce.getBusinessProcessId())) {
            message.setBusinessProcessId(champsAnnonce.getBusinessProcessIdFormatted());
        }
        message.setMessageType(champsAnnonce.getMessageType());
        message.setSubMessageType(champsAnnonce.getSubMessageType());
        message.setMessageDate(JAXBUtil.getXmlCalendarTimestamp((JadeDateUtil.getGlobazDate(champsAnnonce
                .getMessageDate()))));
        message.setEventDate(JAXBUtil.getXmlCalendarDate(champsAnnonce.getEventDate()));
        message.setAction(champsAnnonce.getAction());
        message.setTestDeliveryFlag(JadeSedexService.getInstance().getTestDeliveryFlag());
        message.setResponseExpected(true);

        return message;
    }

    /**
     * Conversion d'une annonce de la DB en annonce controlable par les services
     *
     * @param annonce
     * @return
     * @throws APAnnoncesException
     */
    public APChampsAnnonce convertToChampsAnnonce(APAnnonceAPG annonce) {
        return annonce.toChampsAnnonce();
    }

    private MessageRAPG createMessage(APChampsAnnonce champsAnnonce) throws APAnnoncesException {
        MessageRAPG message;
        if (APAnnoncesRapgService.subMessageType1.equals(champsAnnonce.getSubMessageType())) {
            message = new Message101();
            ObjectFactory of000101 = new ObjectFactory();
            ((Message101) message).setContent(of000101.createContentType());
            ((Message101) message).setHeader(of000101.createHeaderType());
        } else if (APAnnoncesRapgService.subMessageType3.equals(champsAnnonce.getSubMessageType())) {
            message = new Message301();
            rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000301._5.ObjectFactory of000301 = new rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000301._5.ObjectFactory();
            ((Message301) message).setContent(of000301.createContentType());
            ((Message301) message).setHeader(of000301.createHeaderType());
        } else if (APAnnoncesRapgService.subMessageType4.equals(champsAnnonce.getSubMessageType())) {
            message = new Message401();
            rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000401._5.ObjectFactory of000401 = new rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000401._5.ObjectFactory();
            ((Message401) message).setContent(of000401.createContentType());
            ((Message401) message).setHeader(of000401.createHeaderType());
        } else {
            // Action non définie
            throw new APAnnoncesException("Action non définie");
        }
        return message;
    }

    private Schema getSchema() throws SAXException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source[] sources = new Source[]{
                new StreamSource(getClass().getResource(XSD_FOLDER + XSD_101).toExternalForm()),
                new StreamSource(getClass().getResource(XSD_FOLDER + XSD_301).toExternalForm()),
                new StreamSource(getClass().getResource(XSD_FOLDER + XSD_401).toExternalForm()),
                new StreamSource(getClass().getResource(XSD_FOLDER + XSD_COMMON).toExternalForm()),
                new StreamSource(getClass().getResource(XSD_FOLDER + XSD_44).toExternalForm()),
                new StreamSource(getClass().getResource(XSD_FOLDER + XSD_58).toExternalForm()),
        };
        return sf.newSchema(sources);
    }

    private class JAXBValidationEventHandler implements ValidationEventHandler {
        List<ValidationEvent> events;
        boolean hasErrors;

        private JAXBValidationEventHandler() {
            this.events = new LinkedList();
            this.hasErrors = false;
        }

        public boolean handleEvent(ValidationEvent event) {
            this.events.add(event);
            if (event.getSeverity() == 1 || event.getSeverity() == 2) {
                this.hasErrors = true;
            }

            return true;
        }
    }

    private static void checkValidationErrors(JAXBValidationEventHandler eventHandler) throws JAXBValidationError {
        if (!eventHandler.events.isEmpty() && eventHandler.hasErrors) {
            throw new JAXBValidationError("XML validation error (see attached validation events)", eventHandler.events);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.apg.business.services.annonces.AnnoncesRapgService#envoyerAnnonces(java.lang.String,
     * java.lang.String)
     */
    @Override
    public List<APChampsAnnonce> envoyerAnnonces(List<APChampsAnnonce> listChampsAnnonces, FWMemoryLog memoryLog,
                                                 BSession session) throws APAnnoncesException, APPlausibilitesException,
            JadeApplicationServiceNotAvailableException {

        if (listChampsAnnonces.isEmpty()) {
            throw new APAnnoncesException("Aucune annonce à envoyer");
        }

        String dateMessage = JadeDateUtil.getGlobazFormattedDate(new Date());
        String idAnnonce = "";
        String envelopeMessageId = JadeUUIDGenerator.createLongUID().toString();
        ArrayList<SimpleSedexMessage> messagesToSend = new ArrayList<>();

        try {
            List<String> errorsXSD = new ArrayList<>();
            List<String> errorsValidation = new ArrayList<>();
            // Parcourir tous les champsAnonnce et les envoyer
            for (APChampsAnnonce champsAnnonce : listChampsAnnonces) {
                idAnnonce = champsAnnonce.getMessageId();
                champsAnnonce = fullWithConstants(champsAnnonce);
                champsAnnonce.setMessageDate(dateMessage);
                champsAnnonce.setEnvelopeMessageId(envelopeMessageId);

                List<String> errors = ApgServiceLocator.getPlausibilitesApgService().checkPlausisXSD(champsAnnonce,
                        session);

                if (errors.isEmpty()) {
                    Object m = preparerAnnonce(champsAnnonce);
                    marshalAnnonce(idAnnonce, messagesToSend, errorsValidation, champsAnnonce, m);

                } else {
                    // Mettre les erreurs dans le fichier d'erreur
                    addErrorXsd(memoryLog, errorsXSD, champsAnnonce, errors);
                }
            }
            if (!errorsXSD.isEmpty()) {
                APPlausibilitesException e = new APPlausibilitesException("Erreur de plausibilités ");
                e.setErrorsList(errorsXSD);
                throw e;
            }
            if (!errorsValidation.isEmpty()) {
                APPlausibilitesException e = new APPlausibilitesException("Erreur de validation ");
                e.setErrorsList(errorsValidation);
                throw e;
            }
            idAnnonce = "enveloppe";

            // Préparer l'enveloppe sedex (ech-0090)
            ch.ech.xmlns.ech_0090._1.ObjectFactory of0090 = new ch.ech.xmlns.ech_0090._1.ObjectFactory();
            EnvelopeType enveloppe = of0090.createEnvelopeType();
            enveloppe.setEventDate(JAXBUtil.getXmlCalendarTimestamp());
            enveloppe.setMessageDate(JAXBUtil.getXmlCalendarTimestamp());
            enveloppe.setSenderId(JadeSedexService.getInstance().getSedexDirectory().getLocalEntry().getId());
            enveloppe.setMessageId(envelopeMessageId);
            enveloppe.setMessageType(Integer.parseInt(APAnnoncesRapgService.messageType));
            enveloppe.setVersion("1.0");
            enveloppe.getRecipientId().add(
                    GlobazServer.getCurrentSystem().getApplication("APG").getProperty("rapg.recipientid"));

            JAXBElement<EnvelopeType> element = of0090.createEnvelope(enveloppe);
            String envelopeFile = JAXBServices.getInstance().marshal(element, true, false, new Class<?>[]{});

            JadeSedexService.getInstance().sendGroupedMessage(envelopeFile, messagesToSend);

        } catch (APPlausibilitesException e) {
            for (String s : e.getErrorsList()) {
                memoryLog.logMessage(s, FWMessage.ERREUR, "Check XSD / Validation");
            }
            throw e;
        } catch (JAXBValidationException e) {
            LOG.error("Erreur de validation XSD :", e);
            StringBuilder sb = new StringBuilder();
            sb.append(e.toString());
            for (ValidationEvent event : e.getEvents()) {
                sb.append(event.getMessage());
            }
            throw new APAnnoncesException(ANNONCE_N + idAnnonce + "]" + sb.toString());
        } catch (Exception e) {
            throw new APAnnoncesException(ANNONCE_N + idAnnonce + "]" + e.toString());
        }

        return listChampsAnnonces;
    }

    private void addErrorXsd(FWMemoryLog memoryLog, List<String> errorsXSD, APChampsAnnonce champsAnnonce, List<String> errors) {
        for (String s : errors) {
            errorsXSD.add("Erreur annonce n°" + champsAnnonce.getMessageId() + " : "
                    + memoryLog.getSession().getLabel(s));
        }
    }

    private void marshalAnnonce(String idAnnonce, ArrayList<SimpleSedexMessage> messagesToSend, List<String> errorsValidation, APChampsAnnonce champsAnnonce, Object m) throws JAXBException, SAXException {
        try {
            Class<?>[] classes = new Class<?>[]{InsurantDomicileType.class, PersonIdentificationType.class,
                    ReportType.class};
            if (APAnnoncesRapgService.subMessageType1.equals(champsAnnonce.getSubMessageType())) {
                classes = (Class<?>[]) ArrayUtils.addAll(classes, new Class[]{ObjectFactory.class, Message.class});
            } else if (APAnnoncesRapgService.subMessageType3.equals(champsAnnonce.getSubMessageType())) {
                classes = (Class<?>[]) ArrayUtils.addAll(classes, new Class[]{rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000301._5.ObjectFactory.class, rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000301._5.Message.class});
            } else if (APAnnoncesRapgService.subMessageType4.equals(champsAnnonce.getSubMessageType())) {
                classes = (Class<?>[]) ArrayUtils.addAll(classes, new Class[]{rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000401._5.ObjectFactory.class, rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000401._5.Message.class});
            }

            JAXBContext context = JAXBContext.newInstance(classes);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setSchema(getSchema());
            marshaller.setEventHandler(new JAXBValidationEventHandler());

            String fileName = Jade.getInstance().getPersistenceDir() + JadeFilenameUtil.addFilenameSuffixUID(TEMP_FILENAME);
            marshaller.marshal(m, new File(fileName));

            checkValidationErrors((JAXBValidationEventHandler) (marshaller.getEventHandler()));

            // Ajout du message dans la liste de messages à envoyer
            SimpleSedexMessage ssm = new SimpleSedexMessage();
            ssm.fileLocation = fileName;
            messagesToSend.add(ssm);

        } catch (JAXBValidationException e) {
            LOG.error("Erreur de validation XSD :", e);
            StringBuilder sb = new StringBuilder();
            sb.append(e.toString());
            for (ValidationEvent event : e.getEvents()) {
                sb.append(event.getMessage());
            }
            errorsValidation.add(ANNONCE_N + idAnnonce + "]" + sb.toString());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.apg.business.services.annonces.AnnoncesRapgService#fullWithConstants(ch.globaz.apg.business.models.
     * annonces.ChampsAnnonce)
     */
    @Override
    public APChampsAnnonce fullWithConstants(APChampsAnnonce champsAnnonce) throws APAnnoncesException {
        if (champsAnnonce == null) {
            throw new APAnnoncesException("ChampsAnnonce is null !!!");
        }
        try {
            champsAnnonce.setSenderId(JadeSedexService.getInstance().getSedexDirectory().getLocalEntry().getId());
            champsAnnonce.setRecipientId(GlobazServer.getCurrentSystem().getApplication("APG")
                    .getProperty("rapg.recipientid"));
        } catch (Exception e) {
            throw new APAnnoncesException("Error while trying to get APG properties or Sedex config : " + e.toString());
        }

        return champsAnnonce;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.apg.business.services.annonces.AnnoncesRapgService#preparerAnnonce(ch.globaz.apg.business.models.annonces
     * .ChampsAnnonce)
     */
    @Override
    public Object preparerAnnonce(APChampsAnnonce champsAnnonce) throws APAnnoncesException {
        if (champsAnnonce == null) {
            throw new APAnnoncesException("champsAnnonce is null !");
        }

        try {

            // Objects communs
            rapg.ch.eahv_iv.xmlns.eahv_iv_2015_common._5.ObjectFactory ofCommon = new rapg.ch.eahv_iv.xmlns.eahv_iv_2015_common._5.ObjectFactory();
            rapg.ch.ech.xmlns.ech_0058._4.ObjectFactory ofEch0058 = new rapg.ch.ech.xmlns.ech_0058._4.ObjectFactory();
            rapg.ch.ech.xmlns.ech_0044._2.ObjectFactory ofEch0048 = new rapg.ch.ech.xmlns.ech_0044._2.ObjectFactory();

            MessageRAPG message = createMessage(champsAnnonce);

            message.setMinorVersion(BigInteger.valueOf(0));
            message.setDeliveryOffice(ofCommon.createDeliveryOfficeType());
            message.setSendingApplication(ofEch0058.createSendingApplicationType());
            message.setInsurantDomicile(ofCommon.createInsurantDomicileType());
            message.setInsurant(ofEch0048.createPersonIdentificationType());
            if (!JadeStringUtil.isEmpty(champsAnnonce.getBreakRules())) {
                message.setBreakRules(ofCommon.createBreakRuleType());
            }
            // Remplissage du header
            message = addHeader(message, champsAnnonce);
            // Remplissage du content
            message = addContent(message, champsAnnonce);

            return toJaxbMessage(champsAnnonce, message);

        } catch (Exception e) {
            throw new APAnnoncesException("Exception pendant la préparation de l'annonce : " + e.toString());
        }

    }

    private Object toJaxbMessage(APChampsAnnonce champsAnnonce, MessageRAPG message) {
        Object obj = null;
        if (APAnnoncesRapgService.subMessageType1.equals(champsAnnonce.getSubMessageType())) {
            obj = (new ObjectFactory()).createMessage();
            ((Message) obj).setContent(((Message) message).getContent());
            ((Message) obj).setHeader(((Message) message).getHeader());
            ((Message) obj).setMinorVersion(((Message) message).getMinorVersion());
        } else if (APAnnoncesRapgService.subMessageType3.equals(champsAnnonce.getSubMessageType())) {
            obj = (new rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000301._5.ObjectFactory()).createMessage();
            ((rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000301._5.Message) obj)
                    .setContent(((rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000301._5.Message) message).getContent());
            ((rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000301._5.Message) obj)
                    .setHeader(((rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000301._5.Message) message).getHeader());
            ((rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000301._5.Message) obj)
                    .setMinorVersion(((rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000301._5.Message) message).getMinorVersion());
        } else if (APAnnoncesRapgService.subMessageType4.equals(champsAnnonce.getSubMessageType())) {
            obj = (new rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000401._5.ObjectFactory()).createMessage();
            ((rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000401._5.Message) obj)
                    .setContent(((rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000401._5.Message) message).getContent());
            ((rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000401._5.Message) obj)
                    .setHeader(((rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000401._5.Message) message).getHeader());
            ((rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000401._5.Message) obj)
                    .setMinorVersion(((rapg.ch.eahv_iv.xmlns.eahv_iv_2015_000401._5.Message) message).getMinorVersion());
        }
        return obj;
    }

}
