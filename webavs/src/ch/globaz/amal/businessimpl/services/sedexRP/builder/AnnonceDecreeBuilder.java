package ch.globaz.amal.businessimpl.services.sedexRP.builder;

import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.xml.sax.SAXException;
import ch.gdk_cds.xmlns.pv_5201_000101._3.ContentType;
import ch.gdk_cds.xmlns.pv_5201_000101._3.HeaderType;
import ch.gdk_cds.xmlns.pv_5201_000101._3.Message;
import ch.gdk_cds.xmlns.pv_5201_000101._3.ObjectFactory;
import ch.gdk_cds.xmlns.pv_common._3.DecreeType;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.IAMSedex;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;

/**
 * Fabrique pour les annonces 'Nouvelle décision'
 * 
 * @author cbu
 * @version XSD _1
 * 
 */
public class AnnonceDecreeBuilder extends AnnonceBuilderAbstract {
    private String currentDecreeId = null;

    public AnnonceDecreeBuilder(SimpleAnnonceSedex simpleAnnonceSedex) {
        this.simpleAnnonceSedex = simpleAnnonceSedex;
    }

    @Override
    public Object createMessage(Object annonceHeader, Object annonceContent) {
        if (annonceHeader instanceof Message) {
            annonceHeader = ((Message) annonceHeader).getHeader();
        }

        if (annonceContent instanceof Message) {
            annonceContent = ((Message) annonceContent).getContent();
        }

        ObjectFactory of = new ObjectFactory();
        Message message = of.createMessage();

        message.setMinorVersion(IAMSedex.MESSAGE_MINOR_VERSION);
        message.setContent((ContentType) annonceContent);
        message.setHeader((HeaderType) annonceHeader);

        return message;
    }

    @Override
    public Object generateContent() throws AnnonceSedexException {
        try {
            /**
             * Récupération du subside
             */
            simpleDetailFamille = getSubside();

            BigDecimal bd_Amount = new BigDecimal(simpleDetailFamille.getMontantContributionAvecSupplExtra());
            String s_beginMonth = simpleDetailFamille.getDebutDroit();
            String s_beginMonth_complet = "01." + s_beginMonth;
            String s_endMonth = simpleDetailFamille.getFinDroit();
            if (JadeStringUtil.isBlankOrZero(s_endMonth)) {
                s_endMonth = "12." + simpleDetailFamille.getAnneeHistorique();
            }
            String s_endMonth_complet = "30." + s_endMonth;
            String dateToday = JACalendar.todayJJsMMsAAAA();

            if (JadeDateUtil.isDateAfter(s_beginMonth_complet, dateToday)) {
                int nbMonthBetween = JadeDateUtil.getNbMonthsBetween(dateToday, s_beginMonth_complet);
                if (nbMonthBetween > 915) {
                    throw new AnnonceSedexException("Begin date can't be over 15 month from today ! AnnonceSedexId : "
                            + simpleAnnonceSedex.getIdAnnonceSedex());
                }
            }

            int nbYears = JadeDateUtil.getNbYearsBetween(s_beginMonth_complet, dateToday, JadeDateUtil.YEAR_COMPARISON);
            if (nbYears > 94) {
                throw new AnnonceSedexException(
                        "Periode must be applied during this year and during the four last years ! AnnonceSedexId : "
                                + simpleAnnonceSedex.getIdAnnonceSedex());
            }

            if (bd_Amount.compareTo(new BigDecimal("0.05")) == -1) {
                throw new AnnonceSedexException("Le montant doit être de 5cts minimum ! AnnonceSedexId : "
                        + simpleAnnonceSedex.getIdAnnonceSedex());
            }

            if (s_beginMonth == null) {
                throw new AnnonceSedexException("Begin of periode can't be null ! AnnonceSedexId : "
                        + simpleAnnonceSedex.getIdAnnonceSedex());
            }

            // if ((s_endMonth == null) || JadeStringUtil.isBlankOrZero(s_endMonth)) {
            // throw new AnnonceSedexException("End of periode can't be null ! AnnonceSedexId : "
            // + this.simpleAnnonceSedex.getIdAnnonceSedex());
            // }

            XMLGregorianCalendar beginMonth = toXmlDate(s_beginMonth, true);
            XMLGregorianCalendar endMonth = toXmlDate(s_endMonth, true);

            ObjectFactory of = new ObjectFactory();
            ContentType contentType = of.createContentType();

            DecreeType decreeType = new DecreeType();
            decreeType.setAmount(bd_Amount.setScale(2));
            decreeType.setBeginMonth(beginMonth);
            decreeType.setEndMonth(endMonth);

            int anneeHistoriqueSubside = 0;
            try {
                anneeHistoriqueSubside = Integer.valueOf(simpleDetailFamille.getAnneeHistorique());
            } catch (Exception e) {
                // On laisse anneeHistorique à 0
            }
            // Si le subside est un cas PC année historique compris entre 2012 et 2014 inclus alors limitation = FALSE
            // sinon TRUE
            boolean anneeHistoriqueComprisEntre2012et2014 = (anneeHistoriqueSubside >= 2012 && anneeHistoriqueSubside <= 2014);

            if (IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue().equals(simpleDetailFamille.getTypeDemande())
                    && (anneeHistoriqueComprisEntre2012et2014)) {
                decreeType.setLimitation(false);
            } else {
                decreeType.setLimitation(true);
            }

            decreeType.setPerson(getPersonType());

            currentDecreeId = generateDecreeId();
            decreeType.setDecreeId(currentDecreeId);

            contentType.setDecree(decreeType);

            return contentType;
        } catch (Exception e) {
            throw new AnnonceSedexException(e.getMessage());
        }

    }

    /**
     * Génère l'en-tête de l'annonce
     * 
     * @return l'en-tête initialisée
     * @throws AnnonceSedexException
     * 
     * @throws JadeSedexDirectoryInitializationException
     *             Exception levée si dépôt Sedex ne peut être trouvé
     * @throws DatatypeConfigurationException
     *             Exception levée si l'en-tête ne peut être initialisée
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws ALAnnonceRafamException
     *             Exception levée si une erreur métier se produit
     */
    @Override
    public Object generateHeader() throws AnnonceSedexException, JadeSedexDirectoryInitializationException,
            DatatypeConfigurationException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        String messageType = simpleAnnonceSedex.getMessageType();
        String messageSubType = JadeStringUtil.fillWithZeroes(simpleAnnonceSedex.getMessageSubType(), 6);

        String recipientId = AMSedexRPUtil.getSedexIdFromIdTiers(simpleDetailFamille.getNoCaisseMaladie());

        ObjectFactory of = new ObjectFactory();
        HeaderType header = of.createHeaderType();

        header.setSenderId(JadeSedexService.getInstance().getSedexDirectory().getLocalEntry().getId());
        header.setRecipientId(recipientId);
        header.setMessageId(JadeUUIDGenerator.createStringUUID());

        header.setBusinessProcessId(getNextBusinessProcessId());

        header.setMessageType(messageType);
        header.setSubMessageType(messageSubType);

        header.setSendingApplication(getSendingApplicationType());

        GregorianCalendar cal = new GregorianCalendar();
        XMLGregorianCalendar nowDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

        header.setMessageDate(nowDateTime);
        header.setAction(IAMSedex.MESSAGE_ACTION_DEMANDE);
        header.setTestDeliveryFlag(JadeSedexService.getInstance().getTestDeliveryFlag());

        header.setDeclarationLocalReference(getDeclarationLocalReferenceType());

        return header;
    }

    @Override
    public String getAction(Object message) {
        return ((Message) message).getHeader().getAction();
    }

    @Override
    public String getBusinessProcessId(Object message) {
        return ((Message) message).getHeader().getBusinessProcessId().toString();
    }

    @Override
    public ByteArrayOutputStream getContentMessage(Object message) throws MalformedURLException, JAXBValidationError,
            JAXBValidationWarning, JAXBException, SAXException, IOException {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setContent(((Message) message).getContent());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});
        return os;
    }

    @Override
    public String getDecreeId(Object message) {
        return ((Message) message).getContent().getDecree().getDecreeId();
    }

    @Override
    public ByteArrayOutputStream getHeaderMessage(Object message) throws MalformedURLException, JAXBValidationError,
            JAXBValidationWarning, JAXBException, SAXException, IOException {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setHeader(((Message) message).getHeader());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        // try {
        JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});
        // } catch (Exception e) {
        // this.setErrorOnCreation(e);
        // }
        return os;
    }

    @Override
    public String getMessageId(Object message) {
        return ((Message) message).getHeader().getMessageId();
    }

    @Override
    public String getMinorVersion(Object message) {
        return ((Message) message).getMinorVersion().toString();
    }

    @Override
    public String getRecipientId(Object message) {
        return ((Message) message).getHeader().getRecipientId();
    }

    @Override
    public String getSenderId(Object message) {
        return ((Message) message).getHeader().getSenderId();
    }

    @Override
    public Object send(Object header, Object content) throws JadeApplicationException, JadePersistenceException {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();
        HeaderType h = (HeaderType) header;
        ContentType c = (ContentType) content;

        m.setHeader(h);
        m.setContent(c);

        return m;

    }

    @Override
    public Class whichClass() {
        return Message.class;
    }

}
