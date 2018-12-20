/**
 * 
 */
package globaz.apg.businessimpl.service;

import globaz.apg.ApgServiceLocator;
import globaz.apg.business.service.APAnnoncesRapgService;
import globaz.apg.db.annonces.APAnnonceAPG;
import globaz.apg.exceptions.APAnnoncesException;
import globaz.apg.exceptions.APPlausibilitesException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.rapg.messages.Message101;
import globaz.apg.rapg.messages.Message301;
import globaz.apg.rapg.messages.Message401;
import globaz.apg.rapg.messages.MessageRAPG;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBUtil;
import globaz.jade.jaxb.JAXBValidationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.sedex.message.JadeSedexMessageNotSentException;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.webavs.common.WebavsDocumentionLocator;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.ValidationEvent;
import ch.eahv_iv.xmlns.eahv_iv_2015_000101._4.Message;
import ch.eahv_iv.xmlns.eahv_iv_2015_000101._4.ObjectFactory;
import ch.eahv_iv.xmlns.eahv_iv_2015_common._4.InsurantDomicileType;
import ch.ech.xmlns.ech_0044._2.PersonIdentificationType;
import ch.ech.xmlns.ech_0058._4.ReportType;
import ch.ech.xmlns.ech_0090._1.EnvelopeType;

/**
 * @author dde
 */
public class APAnnoncesRapgServiceImpl implements APAnnoncesRapgService {

    private MessageRAPG addContent(MessageRAPG message, APChampsAnnonce champsAnnonce) throws Exception {
        if (!JadeStringUtil.isEmpty(champsAnnonce.getTimeStamp())) {
            message.setTimeStamp(JAXBUtil.getXmlCalendarTimestamp(Long.parseLong(champsAnnonce.getTimeStamp())));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getDeliveryOfficeOfficeIdentifier())) {
            message.getDeliveryOffice().setOfficeIdentifier(
                    new Integer(champsAnnonce.getDeliveryOfficeOfficeIdentifier()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getDeliveryOfficeBranch())) {
            message.getDeliveryOffice().setBranch(new Integer(champsAnnonce.getDeliveryOfficeBranch()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getInsurant())) {
            message.getInsurant().setVn(new Long(champsAnnonce.getInsurant().replace(".", "")));
        }
        if (!JadeStringUtil.isBlankOrZero(champsAnnonce.getInsurantMaritalStatus())) {
            message.setInsurantMaritalStatus(new BigInteger(champsAnnonce.getInsurantMaritalStatus()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getNumberOfChildren())
                && !"90".equals(champsAnnonce.getServiceType())) {
            message.setNumberOfChildren(new BigInteger(champsAnnonce.getNumberOfChildren()));
        }
        if (!JadeStringUtil.isBlankOrZero(champsAnnonce.getInsurantDomicileCanton())) {
            message.getInsurantDomicile().setCanton(new BigInteger(champsAnnonce.getInsurantDomicileCanton()));
        }
        if (!JadeStringUtil.isBlankOrZero(champsAnnonce.getInsurantDomicileCountry())) {
            message.getInsurantDomicile().setCountry(Integer.parseInt(champsAnnonce.getInsurantDomicileCountry()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getAccountingMonth())) {
            message.setAccountingMonth(JAXBUtil.getXmlCalendarDateMonthYear(JadeDateUtil.getGlobazDate("01."
                    + champsAnnonce.getAccountingMonth())));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getServiceType())) {
            message.setServiceType(new BigInteger(champsAnnonce.getServiceType()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getReferenceNumber())) {
            message.setReferenceNumber(new BigInteger(champsAnnonce.getReferenceNumber()));
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getControlNumber())) {
            message.setControlNumber(new Long(champsAnnonce.getControlNumber()));
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
            message.setNumberOfDays(new BigInteger(champsAnnonce.getNumberOfDays()));
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

        return message;
    }

    private MessageRAPG addHeader(MessageRAPG message, APChampsAnnonce champsAnnonce) throws Exception {

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
    public APChampsAnnonce convertToChampsAnnonce(APAnnonceAPG annonce) throws APAnnoncesException {
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
            ch.eahv_iv.xmlns.eahv_iv_2015_000301._4.ObjectFactory of000301 = new ch.eahv_iv.xmlns.eahv_iv_2015_000301._4.ObjectFactory();
            ((Message301) message).setContent(of000301.createContentType());
            ((Message301) message).setHeader(of000301.createHeaderType());
        } else if (APAnnoncesRapgService.subMessageType4.equals(champsAnnonce.getSubMessageType())) {
            message = new Message401();
            ch.eahv_iv.xmlns.eahv_iv_2015_000401._4.ObjectFactory of000401 = new ch.eahv_iv.xmlns.eahv_iv_2015_000401._4.ObjectFactory();
            ((Message401) message).setContent(of000401.createContentType());
            ((Message401) message).setHeader(of000401.createHeaderType());
        } else {
            // Action non définie
            throw new APAnnoncesException("Action non définie");
        }
        return message;
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

        if (listChampsAnnonces.size() < 1) {
            throw new APAnnoncesException("Aucune annonce à envoyer");
        }

        String dateMessage = JadeDateUtil.getGlobazFormattedDate(new Date());
        String idAnnonce = "";
        String envelopeMessageId = JadeUUIDGenerator.createLongUID().toString();
        ArrayList<SimpleSedexMessage> messagesToSend = new ArrayList<SimpleSedexMessage>();

        try {
            List<String> errorsXSD = new ArrayList<String>();
            List<String> errorsValidation = new ArrayList<String>();
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

                    try {
                        /**
                         * LGA 10.2014
                         * IMPORTANT ET BON A SAVOIR
                         * L'ordre des classes fournie en arguments est important pour JAXB :
                         * ->> InsurantDomicileType.class, PersonIdentificationType.class, ReportType.class
                         * Si 2 classes sont inversées dans les déclaration, il se peut que
                         * JAXB n'arrive pas à recréer son context de validation ou avoir des effets de bord très
                         * particuliers..
                         * 
                         */
                        String fileName = JAXBServices.getInstance().marshal(
                                m,
                                true,
                                false,
                                new Class<?>[] { InsurantDomicileType.class, PersonIdentificationType.class,
                                        ReportType.class });

                        // Ajout du message dans la liste de messages à envoyer
                        SimpleSedexMessage ssm = new SimpleSedexMessage();
                        ssm.fileLocation = fileName;
                        messagesToSend.add(ssm);

                    } catch (JAXBValidationException e) {
                        e.printStackTrace();
                        StringBuffer sb = new StringBuffer();
                        sb.append(e.toString());
                        for (ValidationEvent event : e.getEvents()) {
                            sb.append(event.getMessage());
                        }
                        errorsValidation.add("[Annonce n°" + idAnnonce + "]" + sb.toString());
                    }
                } else {
                    // Mettre les erreurs dans le fichier d'erreur
                    for (String s : errors) {
                        errorsXSD.add("Erreur annonce n°" + champsAnnonce.getMessageId() + " : "
                                + memoryLog.getSession().getLabel(s));
                    }
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
            String envelopeFile = JAXBServices.getInstance().marshal(element, true, false, new Class<?>[] {});

            JadeSedexService.getInstance().sendGroupedMessage(envelopeFile, messagesToSend);

        } catch (APPlausibilitesException e) {
            for (String s : e.getErrorsList()) {
                memoryLog.logMessage(s, FWMessage.ERREUR, "Check XSD / Validation");
            }
            throw e;
        } catch (JAXBValidationException e) {
            e.printStackTrace();
            StringBuffer sb = new StringBuffer();
            sb.append(e.toString());
            for (ValidationEvent event : e.getEvents()) {
                sb.append(event.getMessage());
            }
            throw new APAnnoncesException("[Annonce n°" + idAnnonce + "]" + sb.toString());
        } catch (JadeSedexMessageNotSentException e) {
            e.printStackTrace();
            throw new APAnnoncesException("[Annonce n°" + idAnnonce + "]" + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new APAnnoncesException("[Annonce n°" + idAnnonce + "]" + e.toString());
        }

        return listChampsAnnonces;
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
            // champsAnnonce.setSenderId("T6-111000-1");
            // champsAnnonce.setRecipientId("T6-6000000-1");
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
            ch.eahv_iv.xmlns.eahv_iv_2015_common._4.ObjectFactory ofCommon = new ch.eahv_iv.xmlns.eahv_iv_2015_common._4.ObjectFactory();
            ch.ech.xmlns.ech_0058._4.ObjectFactory ofEch0058 = new ch.ech.xmlns.ech_0058._4.ObjectFactory();
            ch.ech.xmlns.ech_0044._2.ObjectFactory ofEch0048 = new ch.ech.xmlns.ech_0044._2.ObjectFactory();

            MessageRAPG message = createMessage(champsAnnonce);

            message.setMinorVersion(new BigInteger("3"));
            message.setDeliveryOffice(ofCommon.createDeliveryOfficeType());
            message.setSendingApplication(ofEch0058.createSendingApplicationType());
            if (!JadeStringUtil.isBlankOrZero(champsAnnonce.getInsurantDomicileCanton())
                    || !JadeStringUtil.isBlankOrZero(champsAnnonce.getInsurantDomicileCountry())) {
                message.setInsurantDomicile(ofCommon.createInsurantDomicileType());
            }
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
            obj = (new ch.eahv_iv.xmlns.eahv_iv_2015_000301._4.ObjectFactory()).createMessage();
            ((ch.eahv_iv.xmlns.eahv_iv_2015_000301._4.Message) obj)
                    .setContent(((ch.eahv_iv.xmlns.eahv_iv_2015_000301._4.Message) message).getContent());
            ((ch.eahv_iv.xmlns.eahv_iv_2015_000301._4.Message) obj)
                    .setHeader(((ch.eahv_iv.xmlns.eahv_iv_2015_000301._4.Message) message).getHeader());
            ((ch.eahv_iv.xmlns.eahv_iv_2015_000301._4.Message) obj)
                    .setMinorVersion(((ch.eahv_iv.xmlns.eahv_iv_2015_000301._4.Message) message).getMinorVersion());
        } else if (APAnnoncesRapgService.subMessageType4.equals(champsAnnonce.getSubMessageType())) {
            obj = (new ch.eahv_iv.xmlns.eahv_iv_2015_000401._4.ObjectFactory()).createMessage();
            ((ch.eahv_iv.xmlns.eahv_iv_2015_000401._4.Message) obj)
                    .setContent(((ch.eahv_iv.xmlns.eahv_iv_2015_000401._4.Message) message).getContent());
            ((ch.eahv_iv.xmlns.eahv_iv_2015_000401._4.Message) obj)
                    .setHeader(((ch.eahv_iv.xmlns.eahv_iv_2015_000401._4.Message) message).getHeader());
            ((ch.eahv_iv.xmlns.eahv_iv_2015_000401._4.Message) obj)
                    .setMinorVersion(((ch.eahv_iv.xmlns.eahv_iv_2015_000401._4.Message) message).getMinorVersion());
        }
        return obj;
    }

}
