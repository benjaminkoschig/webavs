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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.xml.sax.SAXException;
import ch.gdk_cds.xmlns.pv_5201_000201._3.ContentType;
import ch.gdk_cds.xmlns.pv_5201_000201._3.HeaderType;
import ch.gdk_cds.xmlns.pv_5201_000201._3.Message;
import ch.gdk_cds.xmlns.pv_5201_000201._3.ObjectFactory;
import ch.gdk_cds.xmlns.pv_5201_000201._3.StopType;
import ch.gdk_cds.xmlns.pv_common._3.DecreeType;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.IAMSedex;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.models.annonce.SimpleAnnonce;
import ch.globaz.amal.business.models.annonce.SimpleAnnonceSearch;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedexSearch;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.AnnonceSedexTransformation;
import ch.globaz.amal.businessimpl.services.sedexRP.utils.AMSedexRPUtil;

/**
 * Fabrique pour les annonces 'Interruption'
 * 
 * @author cbu
 * @version XSD _1
 * 
 */
public class AnnonceStopBuilder extends AnnonceBuilderAbstract {
    private String currentDecreeId = null;

    public AnnonceStopBuilder(SimpleAnnonceSedex simpleAnnonceSedex) {
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

        // BZ 9481 21.05.2014 : Suppression du test
        // Si on a un decreeId à 0 c'est que l'on est dans un cas ou l'on fait une interruption sans avoir eu une
        // nouvelle décision. Dans ce cas il faut mettre le numéro courant à 0 également (CF pt 3.2.16 documentation
        // SEDEX)
        // if (JadeStringUtil.isBlankOrZero(((ContentType) annonceContent).getStop().getDecree().getDecreeId())) {
        // ((HeaderType) annonceHeader).setBusinessProcessId(new BigInteger("0"));
        // }

        message.setMinorVersion(new BigInteger("0"));
        message.setContent((ContentType) annonceContent);
        message.setHeader((HeaderType) annonceHeader);

        return message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.amal.businessimpl.services.sedexRP.builder.AnnonceBuilderAbstract#generateContent()
     */
    @Override
    public Object generateContent() throws AnnonceSedexException {
        try {
            /**
             * Récupération du subside
             */
            simpleDetailFamille = getSubside();

            String s_endMonth = simpleDetailFamille.getFinDroit();

            if (!JadeDateUtil.isGlobazDateMonthYear(s_endMonth)) {
                int year = JACalendar.getYear("01." + simpleDetailFamille.getDebutDroit());
                s_endMonth = "12." + year;
            }

            // Récupération des informations annoncées par COSAMA
            SimpleAnnonceSearch simpleAnnonceSearch = new SimpleAnnonceSearch();
            simpleAnnonceSearch.setForIdDetailFamille(simpleDetailFamille.getIdDetailFamille());
            simpleAnnonceSearch.setOrderKey("dateAnnonceRIP");
            simpleAnnonceSearch = AmalImplServiceLocator.getSimpleAnnonceService().search(simpleAnnonceSearch);

            SimpleAnnonce simpleAnnonceCosama = new SimpleAnnonce();
            if (simpleAnnonceSearch.getSize() > 0) {
                simpleAnnonceCosama = (SimpleAnnonce) simpleAnnonceSearch.getSearchResults()[0];
            }

            ch.gdk_cds.xmlns.pv_5201_000101._3.Message originalDecree = (ch.gdk_cds.xmlns.pv_5201_000101._3.Message) retrieveOriginalDecree(simpleAnnonceCosama);
            DecreeType decreeTypeOriginal = originalDecree.getContent().getDecree();

            currentDecreeId = decreeTypeOriginal.getDecreeId();

            // BZ9220 : Erreur si on est en train de créer une interruption pour une nouvelle décision qui a un montant
            // de 0
            if (decreeTypeOriginal.getAmount().compareTo(new BigDecimal("0.05")) == -1) {
                throw new AnnonceSedexException(
                        "Le montant de la décision doit être de 5cts minimum ! AnnonceSedexId : "
                                + simpleAnnonceSedex.getIdAnnonceSedex());
            }

            // Construction de l'objet
            ObjectFactory of = new ObjectFactory();
            ContentType contentType = of.createContentType();
            StopType stopType = of.createStopType();

            // On met tel quel les données de la décision initiale
            stopType.setDecree(originalDecree.getContent().getDecree());

            String s_endMonthOriginal = "";
            if (decreeTypeOriginal.getEndMonth().getMonth() < 10) {
                s_endMonthOriginal = "0" + decreeTypeOriginal.getEndMonth().getMonth() + "."
                        + decreeTypeOriginal.getEndMonth().getYear();
            } else {
                s_endMonthOriginal = decreeTypeOriginal.getEndMonth().getMonth() + "."
                        + decreeTypeOriginal.getEndMonth().getYear();
            }

            String newCM = simpleDetailFamille.getNoCaisseMaladie();
            String oldCM = simpleAnnonceSedex.getIdTiersCM();

            String newDateEnd = "";
            if (JadeStringUtil.isBlankOrZero(currentDecreeId) && !simpleAnnonceCosama.isNew()) {
                // On a trouvé une annonce COSAMA, on annule le subside complètement
                // On prend une date antérieur à la date de début
                newDateEnd = JadeDateUtil.addMonths(
                        "01."
                                + JadeStringUtil.fillWithZeroes(
                                        String.valueOf(decreeTypeOriginal.getBeginMonth().getMonth()), 2) + "."
                                + decreeTypeOriginal.getBeginMonth().getYear(), -1);
            } else if (!simpleDetailFamille.getCodeActif() || !newCM.equals(oldCM)) {
                // Subside annulé ou changement de caisse ?
                // On prend une date antérieur à la date de début
                newDateEnd = JadeDateUtil.addMonths(
                        "01."
                                + JadeStringUtil.fillWithZeroes(
                                        String.valueOf(decreeTypeOriginal.getBeginMonth().getMonth()), 2) + "."
                                + decreeTypeOriginal.getBeginMonth().getYear(), -1);
            } else if (JadeDateUtil.isDateMonthYearBefore(s_endMonth, s_endMonthOriginal)) {
                // Subside écourté ?
                newDateEnd = s_endMonth;
            } else if (JadeDateUtil.areDatesEquals("01." + s_endMonth, "01." + s_endMonthOriginal)) {
                // Subside annulé ?
                newDateEnd = JadeDateUtil.addMonths(
                        "01."
                                + JadeStringUtil.fillWithZeroes(
                                        String.valueOf(decreeTypeOriginal.getBeginMonth().getMonth()), 2) + "."
                                + decreeTypeOriginal.getBeginMonth().getYear(), -1);

            } else {
                // Erreur
                throw new AnnonceSedexException(
                        "Stop endMonth can't be newest than original endMonth ! (Stop endMonth : " + s_endMonth
                                + " / Original endMonth : " + s_endMonthOriginal + ")");
            }
            stopType.setStopMonth(toXmlDate(newDateEnd, true));
            contentType.setStop(stopType);

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
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DatatypeConfigurationException
     *             Exception levée si l'en-tête ne peut être initialisée
     * @throws ALAnnonceRafamException
     *             Exception levée si une erreur métier se produit
     */
    @Override
    public Object generateHeader() throws AnnonceSedexException, JadeSedexDirectoryInitializationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, DatatypeConfigurationException {

        String messageType = simpleAnnonceSedex.getMessageType();
        String messageSubType = JadeStringUtil.fillWithZeroes(simpleAnnonceSedex.getMessageSubType(), 6);

        String recipientId = AMSedexRPUtil.getSedexIdFromIdTiers(simpleAnnonceSedex.getIdTiersCM());

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
    public ByteArrayOutputStream getContentMessage(Object message) {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setContent(((Message) message).getContent());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return os;
    }

    @Override
    public String getDecreeId(Object message) {
        return ((Message) message).getContent().getStop().getDecree().getDecreeId();
    }

    @Override
    public ByteArrayOutputStream getHeaderMessage(Object message) {
        ObjectFactory objF = new ObjectFactory();
        Message m = objF.createMessage();

        m.setHeader(((Message) message).getHeader());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            JAXBServices.getInstance().marshal(m, os, false, false, new Class[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    protected Object retrieveOriginalDecree(SimpleAnnonce simpleAnnonceCosama) throws JadePersistenceException,
            JAXBException, SAXException, MalformedURLException, IOException, JAXBValidationError,
            JAXBValidationWarning, JadeApplicationException {

        // Recherche de la dernière confirmation arrivée sur le subside
        ComplexAnnonceSedexSearch lastConfirmationFoundSearch = new ComplexAnnonceSedexSearch();
        lastConfirmationFoundSearch.setForSDXIdDetailFamille(simpleDetailFamille.getIdDetailFamille());
        ArrayList<String> arrayInSubType = new ArrayList<String>();
        // Sélection de toutes les annonces de type 'Confirmation décision' liées au subside
        arrayInSubType.add(AMMessagesSubTypesAnnonceSedex.CONFIRMATION_DECISION.getValue());
        lastConfirmationFoundSearch.setInSDXMessageSubType(arrayInSubType);
        // On utilise l'id caisse pour être sur de ne pas envoyer l'annonce a une autre caisse
        lastConfirmationFoundSearch.setForCMIdTiersCaisse(simpleAnnonceSedex.getIdTiersCM());
        lastConfirmationFoundSearch.setOrderKey("orderByIdSedexMsg");
        lastConfirmationFoundSearch = AmalServiceLocator.getComplexAnnonceSedexService().search(
                lastConfirmationFoundSearch);

        boolean decreeConfirmed = false;
        ComplexAnnonceSedex complexAnnonceSedex = new ComplexAnnonceSedex();
        // Si on a trouvé une confirmation
        if (lastConfirmationFoundSearch.getSize() > 0) {
            ComplexAnnonceSedex lastConfirmationFound = (ComplexAnnonceSedex) lastConfirmationFoundSearch
                    .getSearchResults()[0];

            // On a trouvé la confirmation, on recherche maintenant l'annonce DECREE qui lui est rattachée
            ComplexAnnonceSedexSearch originalDecreeSearch = new ComplexAnnonceSedexSearch();
            arrayInSubType = new ArrayList<String>();
            arrayInSubType.add(AMMessagesSubTypesAnnonceSedex.NOUVELLE_DECISION.getValue());
            originalDecreeSearch.setInSDXMessageSubType(arrayInSubType);
            originalDecreeSearch.setForSDXNoDecision(lastConfirmationFound.getSimpleAnnonceSedex().getNumeroDecision());
            originalDecreeSearch = AmalServiceLocator.getComplexAnnonceSedexService().search(originalDecreeSearch);

            if (originalDecreeSearch.getSize() > 0) {
                // On a trouvé le decree original
                decreeConfirmed = true;
                complexAnnonceSedex = (ComplexAnnonceSedex) originalDecreeSearch.getSearchResults()[0];
            }
        }

        // Si on a pu trouver une confirmation
        if (decreeConfirmed) {
            Class<?>[] addClasses = new Class[] { ch.gdk_cds.xmlns.pv_5201_000101._3.Message.class };

            // Construction de l'objet JAXB
            // il est possible que l'annonce Sedex lue en DB soit en version 2.2, hors seule la version 2.3 est
            // supportée lors de l'unmarshal. Il faut donc convertir le fichier en 2.3 si nécessaire
            SimpleAnnonceSedex simpleAnnonceSedex = AnnonceSedexTransformation
                    .transformationCurrentVersion(complexAnnonceSedex.getSimpleAnnonceSedex());
            complexAnnonceSedex.setSimpleAnnonceSedex(simpleAnnonceSedex);

            ByteArrayInputStream input = new ByteArrayInputStream(complexAnnonceSedex.getSimpleAnnonceSedex()
                    .getMessageContent().getBytes("UTF-8"));

            // Instancie les services JAXB
            JAXBServices jaxb = JAXBServices.getInstance();
            Object o_content = jaxb.unmarshal(input, false, false, addClasses);

            return o_content;
        } else {
            ch.gdk_cds.xmlns.pv_5201_000101._3.ObjectFactory objFactoryDecreeFactice = new ch.gdk_cds.xmlns.pv_5201_000101._3.ObjectFactory();
            ch.gdk_cds.xmlns.pv_5201_000101._3.Message msgDecreeFactice = objFactoryDecreeFactice.createMessage();
            ch.gdk_cds.xmlns.pv_5201_000101._3.ContentType content = objFactoryDecreeFactice.createContentType();

            BigDecimal bd_Amount = new BigDecimal(simpleDetailFamille.getMontantContributionAvecSupplExtra());
            String s_beginMonth = simpleDetailFamille.getDebutDroit();
            String s_endMonth = simpleDetailFamille.getFinDroit();
            if (JadeStringUtil.isBlankOrZero(s_endMonth)) {
                s_endMonth = "12." + simpleDetailFamille.getAnneeHistorique();
            }
            XMLGregorianCalendar beginMonth = toXmlDate(s_beginMonth, true);
            XMLGregorianCalendar endMonth = toXmlDate(s_endMonth, true);

            // Si on a une annonce, on utilise les données annoncées à la caisse
            if (!simpleAnnonceCosama.isNew()) {
                bd_Amount = new BigDecimal(simpleAnnonceCosama.getMontantContribution());
                s_beginMonth = simpleAnnonceCosama.getDebutDroit();
                s_endMonth = simpleAnnonceCosama.getFinDroit();
                if (JadeStringUtil.isBlankOrZero(s_endMonth)) {
                    s_endMonth = "12." + simpleAnnonceCosama.getAnneeHistorique();
                }
                beginMonth = toXmlDate(s_beginMonth, true);
                endMonth = toXmlDate(s_endMonth, true);
            }

            DecreeType decreeType = new DecreeType();
            decreeType.setAmount(bd_Amount.setScale(2));
            decreeType.setBeginMonth(beginMonth);
            decreeType.setEndMonth(endMonth);
            decreeType.setDecreeId("0");
            int anneeHistoriqueSubside = 0;
            try {
                anneeHistoriqueSubside = Integer.valueOf(simpleDetailFamille.getAnneeHistorique());
            } catch (Exception e) {
                // On laisse anneeHistorique à 0
            }
            // Si le subside est un cas PC année historique > 2011 alors limitation = FALSE sinon TRUE
            if (IAMCodeSysteme.AMTypeDemandeSubside.PC.getValue().equals(simpleDetailFamille.getTypeDemande())
                    && (anneeHistoriqueSubside > 2011)) {
                decreeType.setLimitation(false);
            } else {
                decreeType.setLimitation(true);
            }

            decreeType.setPerson(getPersonType());

            content.setDecree(decreeType);
            msgDecreeFactice.setContent(content);

            return msgDecreeFactice;
        }
    }

    @Override
    public Object send(Object header, Object content) throws JadeApplicationException, JadePersistenceException {
        return null;
    }

    @Override
    public Class whichClass() {
        return Message.class;
    }

}
