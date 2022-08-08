package globaz.musca.api.musca;

import ch.globaz.common.document.reference.ReferenceEBill;
import ch.globaz.common.properties.CommonProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAModuleImpression;
import globaz.osiris.api.APISection;
import globaz.osiris.process.ebill.EBillTypeDocument;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import osiris.ch.ebill.send.invoice.*;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe permettant de générer la facture eBill au format xml.
 */
public class FAImpressionFactureEBillXml {

    private static final Logger LOG = LoggerFactory.getLogger(FAImpressionFactureEBillXml.class);
    // Constante de l'entête et du body
    private static final String CREATEYB_INVOICE = "CreateybInvoice";
    private static final String IPE_CE_BILL_SERVER = "IPECeBILLServer";
    private static final String SESSION_ID = "1";
    private static final BigDecimal VERSION = new BigDecimal("2.0");
    private static final Short STATUS = 0;
    private static final String TYPE = "string";
    private static final String BILL_DETAILS_TYPE = "PDFAppendix";
    public static final String APPLICATION_PDF = "x-application/pdfappendix";

    private String eBillAccountID;
    private FAEnteteFacture entete;
    private FAEnteteFacture enteteReference;
    private String montantFacture;
    private List<Map> lignes;
    private Map<PaireIdEcheanceParDateExigibiliteEBill, List<Map>> lignesSursis;
    private String reference;
    private List<JadePublishDocument> attachedDocuments;
    private String dateImprOuFactu;
    private String dateEcheance;
    private String dateOctroiSursis;
    private String titreSursis;
    private BSession session;

    private String billerId;
    private EBillTypeDocument typeDocument;
    private ReferenceEBill eBillFacture;

    public FAImpressionFactureEBillXml() throws Exception {
    }

    /**
     * Méthode permettant de marshall l'objet généré dans le fichier passé en paramètre.
     *
     * @param content : l'objet à marshall
     * @param localFile : le fichier à remplir
     * @return le fichier rempli
     */
    public File marshallDansFichier(InvoiceEnvelope content, File localFile) throws Exception {
        try {
            Marshaller marshaller = createMarshall();
            marshaller.setEventHandler(new ValidationEventHandler() {
                @Override
                public boolean handleEvent(ValidationEvent event) {
                    LOG.info("JAXB validation error : " + event.getMessage());
                    return false;
                }
            });

            try {
                marshaller.marshal(content, localFile);
            } catch (MarshalException e) {
                LOG.error("Une erreur s'est produite lors du marshalling du fichier XML d'après les schémas XSD!", e);
                throw e;
            }

            LOG.info("Fichier XML rempli et formatté d'après les schémas XSD!");

            return localFile;

        } catch (JAXBException e) {
            LOG.error("Une erreur s'est produite lors de la création du fichier XML d'après les schémas XSD!", e);
            throw e;
        }
    }

    public void initFactureEBill() throws Exception {
        eBillFacture = new ReferenceEBill();
        eBillFacture.setSession(getSession());

        // init du type de facture
        if (CommonProperties.QR_FACTURE.getBooleanValue()) {
            eBillFacture.setIsQR(true);
        } else {
            eBillFacture.setIsBVR(true);
        }

        // init du type de document
        if (typeDocument == EBillTypeDocument.BULLETIN_DE_SOLDES && enteteReference != null) {
            eBillFacture.setIsBulletinsDeSoldes(true);
        }
        if (typeDocument == EBillTypeDocument.SURSIS && lignesSursis != null) {
            eBillFacture.setIsSursis(true);
        }
        if (typeDocument == EBillTypeDocument.SOMMATION) {
            eBillFacture.setIsSommation(true);
        }
        if (typeDocument == EBillTypeDocument.RECLAMATION) {
            eBillFacture.setIsReclamation(true);
        }
        if (typeDocument == EBillTypeDocument.DECISION) {
            eBillFacture.setIsDecision(true);
        }

        // init du sous-type de document
        // si eBillTransactionID de l'enteteReference est vide, la facture originale a été généré sur papier et entre-temps eBill a été activé alors bulletinsDeSoldesAvecFactureEBill(false) et il faut générer un bulletin de soldes de type factures (sans FixedReference et DOCUMENT_TYPE_BILL au lien de DOCUMENT_TYPE_CREDITADVICE);
        // autrement on est sur un bulletinsDeSoldesAvecFactureEBill(true)
        if (enteteReference != null && StringUtils.isNotEmpty(enteteReference.getEBillTransactionID())) {
            eBillFacture.setBulletinsDeSoldesAvecFactureEBill(true);
        }
        if(entete.getTotalFactureCurrency().getBigDecimalValue().compareTo(BigDecimal.valueOf(0)) < 0) {
            eBillFacture.setIsNotesCredit(true);
        }
        if (FAModuleImpression.CS_RECOUVREMENT_DIRECT.equals(entete.getIdModeRecouvrement())) {
            eBillFacture.setIsLSV(true);
        }
        if (APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(entete.getIdTypeFacture())) {
            eBillFacture.setIsBulletinNeutre(true);
        }

        // init de la référence eBill
        eBillFacture.initReferenceEBill(entete, dateImprOuFactu);
    }

    /**
     * Création du marshaller pour générer le fichier xml.
     *
     * @return le marshaller pour générer le fichier xml.
     * @throws SAXException
     * @throws JAXBException
     */
    private Marshaller createMarshall() throws SAXException, JAXBException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL url = getClass().getResource("/xsd/ebill/invoice/ybInvoice_V2.0.3.xsd");
        Schema schema = sf.newSchema(url);
        JAXBContext jc = JAXBContext.newInstance(InvoiceEnvelope.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setSchema(schema);
        return marshaller;
    }

    public InvoiceEnvelope createFileContent() throws Exception {
        ObjectFactory of = new ObjectFactory();
        InvoiceEnvelope invoice = of.createInvoiceEnvelope();
        invoice.setHeader(createHeader());
        invoice.setBody(createBody());
        invoice.setType(TYPE);
        return invoice;
    }

    /**
     * Création du header de la facture eBill
     *
     * @return le header de la facture eBill
     */
    private HeaderType createHeader() {
        ObjectFactory of = new ObjectFactory();
        HeaderType header = of.createHeaderType();

        header.setFrom(eBillFacture.getNomCaisse());
        header.setTo(IPE_CE_BILL_SERVER);
        header.setUseCase(CREATEYB_INVOICE);
        header.setSessionID(SESSION_ID);
        header.setVersion(VERSION);
        header.setStatus(STATUS);

        return header;
    }

    /**
     * Création du body de la facture eBill
     *
     * @return le body de la facture eBill
     */
    private YbInvoiceType createBody() throws Exception {
        ObjectFactory of = new ObjectFactory();
        YbInvoiceType body = of.createYbInvoiceType();

        body.setDeliveryInfo(createDeliveryInfo());
        body.setBill(createBill());
        body.setAppendix(createAppendix());

        return body;
    }

    /**
     * Création de la balise qui vas contenir le ou les fichiers originaux générés
     * par le processus d'impression classique encodés en base64.
     *
     * @return l'objet de type AppendixType contenant le fichier encodé en base 64
     */
    private AppendixType createAppendix() throws IOException {
        ObjectFactory of = new ObjectFactory();
        AppendixType appendixType = of.createAppendixType();
        for (JadePublishDocument  attachedDocument : getAttachedDocuments()) {
            String pathFile = attachedDocument.getDocumentLocation();
            byte[] inFileBytes = Files.readAllBytes(Paths.get(pathFile));
            String encoded = Base64.getEncoder().encodeToString(inFileBytes);
            AppendixType.Document appendixTypeDocument = of.createAppendixTypeDocument();
            appendixTypeDocument.setMimeType(APPLICATION_PDF);
            appendixTypeDocument.setValue(encoded);
            appendixType.getDocument().add(appendixTypeDocument);
        }
        if (!appendixType.getDocument().isEmpty()) {
            return appendixType;
        } else {
            return null;
        }
    }

    /**
     * Création du deliveryInfo de la facture eBill
     *
     * @return le deliveryInfo de la facture eBill
     */
    private DeliveryType createDeliveryInfo() {
        ObjectFactory of = new ObjectFactory();
        DeliveryType deliveryInfo = of.createDeliveryType();

        if (StringUtils.isNotEmpty(billerId)) {
            deliveryInfo.setBillerID(Long.parseLong(billerId));
        }
        deliveryInfo.setEBillAccountID(Long.parseLong(eBillAccountID));
        deliveryInfo.setDeliveryDate(convertStringDateToXmlCalendarDate(dateImprOuFactu));
        deliveryInfo.setTransactionID(entete.getEBillTransactionID());

        // Dans notre cas, on génère systématiquement une facture PDF Appendix : on ajoute le pdf en Base64 en annexe.
        deliveryInfo.setBillDetailsType(BILL_DETAILS_TYPE);

        return deliveryInfo;
    }


    /**
     * Méthode permettant de créer la date au format xmlCalendar.
     *
     * @param dateFacturation : la date à convertir
     * @return la date au format xmlCalendar
     */
    private XMLGregorianCalendar convertStringDateToXmlCalendarDate(String dateFacturation) {
        XMLGregorianCalendar xmlGregCal = null;
        DateFormat inputFormat = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (StringUtils.isNotBlank(dateFacturation)) {
                if (dateFacturation.length() == 4) {
                    inputFormat = new SimpleDateFormat("yyyy");
                }
                Date date = inputFormat.parse(dateFacturation);
                xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(outputFormat.format(date));

            }
        } catch (ParseException | DatatypeConfigurationException e) {
            LOG.error("Erreur lors du parsing de la date", e);
        }

        return xmlGregCal;
    }

    /**
     * Création du bill de la facture eBill
     *
     * @return le bill de la facture eBill
     */
    private InvoiceBillType createBill() throws Exception {
        ObjectFactory of = new ObjectFactory();
        InvoiceBillType bill = of.createInvoiceBillType();

        bill.setHeader(createBillHeader());
        bill.setLineItems(createLineItems());
        bill.setSummary(createSummary());

        return bill;
    }

    /**
     * Création du headerBill de la facture eBill
     *
     * @return le headerBill de la facture eBill
     */
    private InvoiceBillType.Header createBillHeader() throws Exception {
        ObjectFactory of = new ObjectFactory();
        InvoiceBillType.Header header = of.createInvoiceBillTypeHeader();

        header.setDocumentType(eBillFacture.getSubTypeDocument());
        header.setDocumentID(entete.getEBillTransactionID());

        header.setDocumentDate(convertStringDateToXmlCalendarDate(dateImprOuFactu));

        header.setSenderParty(createSenderParty());
        header.setReceiverParty(createReceiverParty());

        header.setAchievementDate(createAchievementDate(dateImprOuFactu, dateImprOuFactu));
        header.setCurrency(eBillFacture.getDevise());
        // header.setAccountAssignment(createAccountAssignment());

        // Dans le cas d'un bulletin de soldes avec factures eBill, ajoute le transactionID de l'entete de reference dans une balise de type FixedReference
        if (eBillFacture.isBulletinsDeSoldesAvecFactureEBill()) {
            header.getFixedReference().add(createFixedReference(of, enteteReference.getEBillTransactionID()));
        // Dans le cas d'une notes de crédit, ajoute le transactionID de l'entete dans une balise de type FixedReference
        } else if (eBillFacture.isNotesCredit()) {
            header.getFixedReference().add(createFixedReference(of, entete.getEBillTransactionID()));
        }

        header.setLanguage(entete.getISOLangueTiers().toLowerCase(Locale.ROOT));

        header.setPaymentInformation(createPaymentInformation());

        header.getFreeText().add(entete.getDescriptionDecompte());

        return header;
    }

    private FixedReference createFixedReference(ObjectFactory of, String eBillTransactionID) {
        FixedReference fixedReference = of.createFixedReference();
        fixedReference.setReferenceType(ReferenceEBill.REFERENCE_TYPE_BILLNUMBER);
        fixedReference.setReferenceValue(eBillTransactionID);
        fixedReference.setReferencePosition("1");
        return fixedReference;
    }

    private BillHeaderType.SenderParty createSenderParty() {
        ObjectFactory of = new ObjectFactory();
        BillHeaderType.SenderParty senderParty = of.createBillHeaderTypeSenderParty();

        senderParty.setNetwork(createSenderNetwork());
        senderParty.setTaxLiability(eBillFacture.getTaxLiability());
        senderParty.setPartyType(createSenderPartyType());
        return senderParty;
    }

    private NetworkType createSenderNetwork() {
        ObjectFactory of = new ObjectFactory();
        NetworkType networkType = of.createNetworkType();

        networkType.setNetworkName(eBillFacture.getNetworkName());
        networkType.setNetworkID(billerId);

        return networkType;
    }

    private PartyType createSenderPartyType() {
        ObjectFactory of = new ObjectFactory();
        PartyType partyType = of.createPartyType();

        AddressType addressType = of.createAddressType();

        // TODO identifier le cas d'une entreprise ou d'un particulier.
        addressType.setCompanyName(eBillFacture.getNomCaisse());

        StringBuffer addresse1Buffer = new StringBuffer();
        eBillFacture.addIfNotEmpty(eBillFacture.getCreRueAdresse(), addresse1Buffer, null);
        eBillFacture.addIfNotEmpty(eBillFacture.getCreNumMaisonAdresse(), addresse1Buffer, " ");
        addressType.setAddress1(addresse1Buffer.toString());
        addressType.setPOBox(eBillFacture.getCreCasePostale());
        addressType.setZIP(eBillFacture.getCreCodePostal());
        addressType.setCity(eBillFacture.getCreLieu());
        addressType.setCountry(eBillFacture.getCrePays());

        partyType.setAddress(addressType);

        return partyType;
    }

    private BillHeaderType.ReceiverParty createReceiverParty() {
        ObjectFactory of = new ObjectFactory();
        BillHeaderType.ReceiverParty receiverParty = of.createBillHeaderTypeReceiverParty();

        receiverParty.setNetwork(createReceiverNetwork());
        receiverParty.setPartyType(createReceiverPartyType());

        return receiverParty;
    }

    private NetworkType createReceiverNetwork() {
        ObjectFactory of = new ObjectFactory();
        NetworkType networkType = of.createNetworkType();

        networkType.setNetworkName(eBillFacture.getNetworkName());
        networkType.setNetworkID(eBillAccountID);

        return networkType;
    }

    private PartyType createReceiverPartyType() {
        ObjectFactory of = new ObjectFactory();
        PartyType partyType = of.createPartyType();

        AddressType addressType = of.createAddressType();

        // TODO identifier le cas d'une entreprise ou d'un particulier.
        addressType.setCompanyName(eBillFacture.getDebNom());

        StringBuffer addresse1Buffer = new StringBuffer();
        eBillFacture.addIfNotEmpty(eBillFacture.getDebRueAdresse(), addresse1Buffer, null);
        eBillFacture.addIfNotEmpty(eBillFacture.getDebNumMaisonAdresse(), addresse1Buffer, " ");
        addressType.setAddress1(addresse1Buffer.toString());
        addressType.setPOBox(eBillFacture.getDebCasePostale());
        addressType.setZIP(eBillFacture.getDebCodePostal());
        addressType.setCity(eBillFacture.getDebLieu());
        addressType.setCountry(eBillFacture.getDebPays());

        String numeroAffilie = entete.getIdExterneRole().replaceAll("[^\\d]", "");
        String role = entete.getIdRole().substring(Math.max(entete.getIdRole().length() - 2, 0));

        StringBuilder customerId = new StringBuilder(numeroAffilie).append("/").append(role);
        partyType.setCustomerID(customerId.toString());

        partyType.setAddress(addressType);

        return partyType;
    }


    private AchievementDateType createAchievementDate(String startDate, String endDate) {
        ObjectFactory of = new ObjectFactory();
        AchievementDateType achievementDateType = of.createAchievementDateType();

        achievementDateType.setStartDateAchievement(convertStringDateToXmlCalendarDate(startDate));
        achievementDateType.setEndDateAchievement(convertStringDateToXmlCalendarDate(endDate));

        return achievementDateType;
    }

    private AccountAssignmentType createAccountAssignment() {
        ObjectFactory of = new ObjectFactory();
        AccountAssignmentType accountAssignmentType = of.createAccountAssignmentType();

        // TODO : clarifier la référence
        Reference reference = of.createReference();
        reference.setReferenceType(ReferenceEBill.REFERENCE_TYPE_BILLNUMBER);
        reference.setReferenceValue(entete.getIdExterneFacture());
        reference.setReferencePosition("1");
        accountAssignmentType.setOrderReference(reference);

        return accountAssignmentType;
    }

    private BillHeaderType.PaymentInformation createPaymentInformation() throws Exception {
        ObjectFactory of = new ObjectFactory();
        BillHeaderType.PaymentInformation paymentInformation = of.createBillHeaderTypePaymentInformation();

        paymentInformation.setPaymentDueDate(convertStringDateToXmlCalendarDate(getDateEcheance()));
        paymentInformation.setPaymentType(eBillFacture.getPaymentType());

        if (eBillFacture.isBulletinNeutre()) {
            paymentInformation.setFixAmount("No");
        } else {
            paymentInformation.setFixAmount("Yes");
        }

        if (eBillFacture.isBVR()) {
            BillHeaderType.PaymentInformation.ESR esr = of.createBillHeaderTypePaymentInformationESR();
            esr.setESRCustomerNumber(eBillFacture.getNumeroCC().replaceAll("\\s", ""));
            esr.setESRReferenceNumber(reference.replaceAll("\\s", ""));
            paymentInformation.setESR(esr);
        } else if (eBillFacture.isQR()) {
            BillHeaderType.PaymentInformation.IBAN iban = of.createBillHeaderTypePaymentInformationIBAN();
            iban.setIBAN(eBillFacture.getNumeroCC().replaceAll("\\s", ""));
            iban.setCreditorReference(reference.replaceAll("\\s", ""));
            paymentInformation.setIBAN(iban);
        }

        if (eBillFacture.isSursis()) {
            BillHeaderType.PaymentInformation.Instalments instalments = createInstalments(of);
            paymentInformation.getInstalments().add(instalments);
        }

        return paymentInformation;
    }

    /**
     * Création du Instalments de la facture eBill
     *
     * @return le Instalments de la facture eBill
     */
    private BillHeaderType.PaymentInformation.Instalments createInstalments(ObjectFactory of) {
        BillHeaderType.PaymentInformation.Instalments instalments = of.createBillHeaderTypePaymentInformationInstalments();
        int instalmentCount = 1;
        for (Map.Entry<PaireIdEcheanceParDateExigibiliteEBill, List<Map>> ligneSursis : lignesSursis.entrySet()) {
            instalments.getInstalment().add(createInstalment(of, instalments, ligneSursis, instalmentCount));
            instalmentCount++;
        }
        return instalments;
    }

    /**
     * Création du Instalment de la facture eBill
     *
     * @return le Instalment de la facture eBill
     */
    private InstalmentType createInstalment(ObjectFactory of, BillHeaderType.PaymentInformation.Instalments instalments, Map.Entry<PaireIdEcheanceParDateExigibiliteEBill, List<Map>> ligneSursis, int counter) {
        InstalmentType instalment = of.createInstalmentType();
        instalment.setDescription(ligneSursis.getValue().get(0).get("COL_1") != null ? String.valueOf(ligneSursis.getValue().get(0).get("COL_1")) : String.valueOf(counter));
        instalment.setAmount(ligneSursis.getValue().get(0).get("COL_6") != null ? BigDecimal.valueOf((Double) ligneSursis.getValue().get(0).get("COL_6")) : null);
        instalment.setPaymentDueDate(convertStringDateToXmlCalendarDate(ligneSursis.getKey().getDateExigibilite()));
        instalment.setESRReferenceNr(reference.replaceAll("\\s", ""));
        return instalment;
    }

    /**
     * Création du Line Items de la facture eBill
     *
     * @return le Line Items de la facture eBill
     */
    private InvoiceBillType.LineItems createLineItems() {
        ObjectFactory of = new ObjectFactory();
        InvoiceBillType.LineItems lineItems = of.createInvoiceBillTypeLineItems();
        AtomicInteger ligneId = new AtomicInteger(1);

        // Création des LineItems pour les Bulletins de Soldes
        if(eBillFacture.isBulletinsDeSoldes() && lignes != null) {
            lignes.stream().forEach(ligne -> lineItems.getLineItem().add(createLineItemBulletinsDeSoldes(ligne, ligneId.getAndIncrement())));
        // Création des LineItems pour les Sursis au Paiement
        } else if (eBillFacture.isSursis()) {
            lineItems.getLineItem().add(createLineItemSursis(lignesSursis.entrySet().stream().findFirst().get().getValue().get(0), ligneId.getAndIncrement()));
        // Création des LineItems pour les Sommations
        } else if (eBillFacture.isSommation()) {
            // filtre la ligne de TOTAL qui se distingue par la présence de la colonne F4
            lignes.stream().filter(ligne -> ligne.get("F4") == null).forEach(ligne -> lineItems.getLineItem().add(createLineItemSommation(ligne, ligneId.getAndIncrement())));
        // Création des LineItems pour les Réclamations de frais et intérêts
        } else if (eBillFacture.isReclamation()) {
            // filtre la ligne de TOTAL qui se distingue par la présence de la colonne F4
            lignes.stream().filter(ligne -> ligne.get("F4") == null).forEach(ligne -> lineItems.getLineItem().add(createLineItemReclamation(ligne, ligneId.getAndIncrement())));
        // Création des LineItems pour les Décisions
        } else if (eBillFacture.isDecision()) {
            lignes.stream().forEach(ligne -> lineItems.getLineItem().add(createLineItemDecision(ligne, ligneId.getAndIncrement())));
        // Création des LineItems pour les Factures qui ne sont d'aucun des autres type de document spécifique
        } else if ((eBillFacture.isQR() || eBillFacture.isBVR()) && lignes != null) {
            lignes.forEach(ligne -> lineItems.getLineItem().add(createLineItemFactures(ligne, ligneId.getAndIncrement())));
        }
        return lineItems;
    }

    private LineItemType createLineItemFactures(Map ligne, int ligneId) {
        LineItemType lineItem = createLineItemWithIdAndType(ligne, ligneId);

        String dateDebut = ((String) ligne.get("COL_7_DEBUT"));
        String dateFin = ((String) ligne.get("COL_7_FIN"));
        AchievementDateType achievementDate = null;
        if (StringUtils.isNotBlank(dateDebut) && StringUtils.isNotBlank(dateFin)) {
            achievementDate = createAchievementDate(dateDebut, dateFin);
        }
        lineItem.setAchievementDate(achievementDate);

        lineItem.setProductDescription((String) ligne.get("COL_1"));

        String taux = ((String) ligne.get("COL_5"));
        lineItem.setQuantity(new BigDecimal(StringUtils.isEmpty(taux) ? "0.00" : taux));
        lineItem.setQuantityDescription("1I");
        Double masse = ((Double) ligne.get("COL_4"));
        lineItem.setPriceUnit(BigDecimal.valueOf(masse != null ? masse : 1.00));
        BigDecimal montant = ligne.get("COL_6") != null ? BigDecimal.valueOf((Double) ligne.get("COL_6")) : null;
        lineItem.setAmountInclusiveTax(montant);
        lineItem.setAmountExclusiveTax(montant);

        // lineItem.setAccountAssignment(createAccountAssignment());

        return lineItem;
    }

    private LineItemType createLineItemSommation(Map ligne, int ligneId) {
        LineItemType lineItem = createLineItemWithIdAndType(ligne, ligneId);

        String dateDebut = (dateImprOuFactu);
        String dateFin = (dateEcheance);
        AchievementDateType achievementDate = null;
        if (StringUtils.isNotBlank(dateDebut) && StringUtils.isNotBlank(dateFin)) {
            achievementDate = createAchievementDate(dateDebut, dateFin);
        }
        lineItem.setAchievementDate(achievementDate);

        lineItem.setProductDescription(ligne.get("F1") != null ? (String) ligne.get("F1") : "");

        lineItem.setQuantity(BigDecimal.valueOf(0.00));
        lineItem.setQuantityDescription("1I");
        lineItem.setPriceUnit(BigDecimal.valueOf(1.00));
        BigDecimal montant = ligne.get("F3") != null ? new FWCurrency((String) ligne.get("F3")).getBigDecimalValue() : null;
        lineItem.setAmountInclusiveTax(montant);
        lineItem.setAmountExclusiveTax(montant);

        // lineItem.setAccountAssignment(createAccountAssignment());

        return lineItem;
    }

    private LineItemType createLineItemReclamation(Map ligne, int ligneId) {
        LineItemType lineItem = createLineItemWithIdAndType(ligne, ligneId);

        String dateDebut = (dateImprOuFactu);
        String dateFin = (dateEcheance);
        AchievementDateType achievementDate = null;
        if (StringUtils.isNotBlank(dateDebut) && StringUtils.isNotBlank(dateFin)) {
            achievementDate = createAchievementDate(dateDebut, dateFin);
        }
        lineItem.setAchievementDate(achievementDate);

        lineItem.setProductDescription(ligne.get("F1") != null ? (String) ligne.get("F1") : "");

        lineItem.setQuantity(BigDecimal.valueOf(0.00));
        lineItem.setQuantityDescription("1I");
        lineItem.setPriceUnit(BigDecimal.valueOf(1.00));
        BigDecimal montant = ligne.get("F3") != null ? new FWCurrency((String) ligne.get("F3")).getBigDecimalValue() : null;
        lineItem.setAmountInclusiveTax(montant);
        lineItem.setAmountExclusiveTax(montant);

        // lineItem.setAccountAssignment(createAccountAssignment());

        return lineItem;
    }

    private LineItemType createLineItemDecision(Map ligne, int ligneId) {
        LineItemType lineItem = createLineItemWithIdAndType(ligne, ligneId);

        String dateDebut = (dateImprOuFactu);
        String dateFin = (dateEcheance);
        AchievementDateType achievementDate = null;
        if (StringUtils.isNotBlank(dateDebut) && StringUtils.isNotBlank(dateFin)) {
            achievementDate = createAchievementDate(dateDebut, dateFin);
        }
        lineItem.setAchievementDate(achievementDate);

        lineItem.setProductDescription(ligne.get("F1") != null ? (String) ligne.get("F1") : "");

        lineItem.setQuantity(BigDecimal.valueOf(0.00));
        lineItem.setQuantityDescription("1I");
        lineItem.setPriceUnit(BigDecimal.valueOf(1.00));
        BigDecimal montant = ligne.get("F3") != null ? new FWCurrency((String) ligne.get("F3")).getBigDecimalValue() : null;
        lineItem.setAmountInclusiveTax(montant);
        lineItem.setAmountExclusiveTax(montant);

        // lineItem.setAccountAssignment(createAccountAssignment());

        return lineItem;
    }


    private LineItemType createLineItemSursis(Map ligne, int ligneId) {
        LineItemType lineItem = createLineItemWithIdAndType(ligne, ligneId);

        String dateDebut = (dateOctroiSursis);
        String dateFin = (dateEcheance);
        AchievementDateType achievementDate = null;
        if (StringUtils.isNotBlank(dateDebut) && StringUtils.isNotBlank(dateFin)) {
            achievementDate = createAchievementDate(dateDebut, dateFin);
        }
        lineItem.setAchievementDate(achievementDate);

        lineItem.setProductDescription(titreSursis);

        lineItem.setQuantity(BigDecimal.valueOf(0.00));
        lineItem.setQuantityDescription("1I");
        lineItem.setPriceUnit(BigDecimal.valueOf(1.00));
        lineItem.setAmountInclusiveTax(new FWCurrency(montantFacture).getBigDecimalValue());
        lineItem.setAmountExclusiveTax(new FWCurrency(montantFacture).getBigDecimalValue());

        // lineItem.setAccountAssignment(createAccountAssignment());

        return lineItem;
    }

    private LineItemType createLineItemBulletinsDeSoldes(Map ligne, int ligneId) {
        LineItemType lineItem = createLineItemWithIdAndType(ligne, ligneId);

        String dateDebut = (String) ligne.get("COL_1");
        String dateFin = (String) ligne.get("COL_2");
        AchievementDateType achievementDate = null;
        if (StringUtils.isNotBlank(dateDebut) && StringUtils.isNotBlank(dateFin)) {
            achievementDate = createAchievementDate(dateDebut, dateFin);
        }
        lineItem.setAchievementDate(achievementDate);

        lineItem.setProductDescription((String) ligne.get("COL_3"));

        lineItem.setQuantity(BigDecimal.valueOf(0.00));
        lineItem.setQuantityDescription("1I");
        lineItem.setPriceUnit(BigDecimal.valueOf(1.00));
        BigDecimal montant = ligne.get("COL_4") != null ? BigDecimal.valueOf((Double) ligne.get("COL_4")) : null;
        lineItem.setAmountInclusiveTax(montant);
        lineItem.setAmountExclusiveTax(montant);

        // lineItem.setAccountAssignment(createAccountAssignment());

        return lineItem;
    }

    /**
     * Création du LineItem de la facture eBill
     *
     * @return le LineItem de la facture eBill
     */
    private LineItemType createLineItemWithIdAndType(Map ligne, int ligneId) {
        ObjectFactory of = new ObjectFactory();
        LineItemType lineItem = of.createLineItemType();
        lineItem.setLineItemType(eBillFacture.getLineItemType());
        lineItem.setLineItemID(ligne.get("COL_ID") != null ? String.valueOf(ligne.get("COL_ID")) : String.valueOf(ligneId));
        return lineItem;
    }

    /**
     * Création du Summary de la facture eBill
     *
     * @return le Summary de la facture eBill
     */
    private SummaryType createSummary() {
        ObjectFactory of = new ObjectFactory();
        SummaryType summaryType = of.createSummaryType();

        summaryType.setTax(createTaxType());

        summaryType.setTotalAmountPaid(BigDecimal.valueOf(0.00));

        summaryType.setTotalAmountExclusiveTax(new FWCurrency(montantFacture).getBigDecimalValue());
        summaryType.setTotalAmountInclusiveTax(new FWCurrency(montantFacture).getBigDecimalValue());
        summaryType.setTotalAmountDue(new FWCurrency(montantFacture).getBigDecimalValue());

        return summaryType;
    }

    /**
     * Création du TaxType de la facture eBill
     *
     * @return le TaxType de la facture eBill
     */
    private TaxType createTaxType() {
        ObjectFactory of = new ObjectFactory();

        TaxType taxType = of.createTaxType();
        taxType.getTaxDetail().add(createTaxDetailType());
        taxType.setTotalTax(BigDecimal.valueOf(0.00));

        return taxType;
    }

    /**
     * Création du TaxDetailType de la facture eBill
     *
     * @return le TaxDetailType de la facture eBill
     */
    private TaxDetailType createTaxDetailType() {
        ObjectFactory of = new ObjectFactory();

        TaxDetailType taxDetailType = of.createTaxDetailType();
        taxDetailType.setRate(BigDecimal.valueOf(0.00));
        taxDetailType.setAmount(BigDecimal.valueOf(0.00));

        taxDetailType.setBaseAmountInclusiveTax(new FWCurrency(montantFacture).getBigDecimalValue());
        taxDetailType.setBaseAmountExclusiveTax(new FWCurrency(montantFacture).getBigDecimalValue());

        return taxDetailType;
    }

    public FAEnteteFacture getEntete() {
        return entete;
    }

    public void setEntete(FAEnteteFacture entete) {
        this.entete = entete;
    }

    public FAEnteteFacture getEnteteReference() {
        return enteteReference;
    }

    public void setEnteteReference(FAEnteteFacture enteteReference) {
        this.enteteReference = enteteReference;
    }

    public String getDateImprOuFactu() {
        return dateImprOuFactu;
    }

    public void setDateImprOuFactu(String dateImprOuFactu) {
        this.dateImprOuFactu = dateImprOuFactu;
    }

    public String getBillerId() {
        return billerId;
    }

    public void setBillerId(String billerId) {
        this.billerId = billerId;
    }

    public EBillTypeDocument getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(EBillTypeDocument typeDocument) {
        this.typeDocument = typeDocument;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public String getDateOctroiSursis() {
        return dateOctroiSursis;
    }

    public void setDateOctroiSursis(String dateOctroiSursis) {
        this.dateOctroiSursis = dateOctroiSursis;
    }

    public List<JadePublishDocument> getAttachedDocuments() {
        return attachedDocuments;
    }

    public void setAttachedDocuments(List<JadePublishDocument> attachedDocuments) {
        this.attachedDocuments = attachedDocuments;
    }

    public String getEBillAccountID() {
        return eBillAccountID;
    }

    public void setEBillAccountID(String eBillAccountID) {
        this.eBillAccountID = eBillAccountID;
    }

    public String getMontantFacture() {
        return montantFacture;
    }

    public void setMontantFacture(String montantFacture) {
        this.montantFacture = montantFacture;
    }

    public List<Map> getLignes() {
        return lignes;
    }

    public void setLignes(List<Map> lignes) {
        this.lignes = lignes;
    }

    public Map<PaireIdEcheanceParDateExigibiliteEBill, List<Map>> getLignesSursis() {
        return lignesSursis;
    }

    public void setLignesSursis(Map<PaireIdEcheanceParDateExigibiliteEBill, List<Map>> lignesSursis) {
        this.lignesSursis = lignesSursis;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTitreSursis() {
        return titreSursis;
    }

    public void setTitreSursis(String titreSursis) {
        this.titreSursis = titreSursis;
    }

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
