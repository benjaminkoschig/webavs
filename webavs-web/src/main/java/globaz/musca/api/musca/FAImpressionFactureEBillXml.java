package globaz.musca.api.musca;

import ch.globaz.common.document.reference.ReferenceEBill;
import ch.globaz.common.properties.CommonProperties;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAModuleImpression;
import globaz.musca.db.facturation.FAPassage;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APISectionDescriptor;
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
import java.util.*;

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

    private JadePublishDocument attachedDocument;
    private String montantBulletinSoldes;
    private ReferenceEBill eBillFacture;
    private FAEnteteFacture entete;
    private FAEnteteFacture enteteReference;
    private FAAfact afact;
    private List<Map> lignesParPaireIdExterne;
    private FAPassage passage;
    private String billerId;
    private BSession session;
    private String eBillAccountID;
    private String reference;

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

            marshaller.marshal(content, localFile);
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

        if (CommonProperties.QR_FACTURE.getBooleanValue()) {
            eBillFacture.setIsQR(true);
        } else {
            eBillFacture.setIsBVR(true);
        }
        if (enteteReference != null) {
            eBillFacture.setIsBulletinsDeSoldes(true);
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
        eBillFacture.initReferenceEBill(entete.getIdTiers());
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
     * Création de la balise qui vas contenir le fichier original généré
     * par le processus d'impression classique encodé en base64.
     *
     * @return l'objet de type AppendixType contenant le fichier encodé en base 64
     */
    private AppendixType createAppendix() throws IOException {
        if (getAttachedDocument() != null) {
            ObjectFactory of = new ObjectFactory();
            String pathFile = getAttachedDocument().getDocumentLocation();
            byte[] inFileBytes = Files.readAllBytes(Paths.get(pathFile));
            String encoded = Base64.getEncoder().encodeToString(inFileBytes);
            AppendixType appendixType = of.createAppendixType();
            AppendixType.Document appendixTypeDocument = of.createAppendixTypeDocument();
            appendixTypeDocument.setMimeType(APPLICATION_PDF);
            appendixTypeDocument.setValue(encoded);
            appendixType.getDocument().add(appendixTypeDocument);
            return appendixType;
        }
        return null;
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
        XMLGregorianCalendar deliveryDate = convertStringDateToXmlCalendarDate(passage.getDateFacturation());
        deliveryInfo.setDeliveryDate(deliveryDate);
        deliveryInfo.setTransactionID(entete.geteBillTransactionID());

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

        header.setDocumentType(eBillFacture.getDocumentType());
        header.setDocumentID(entete.geteBillTransactionID());

        XMLGregorianCalendar documentDate = convertStringDateToXmlCalendarDate(passage.getDateFacturation());
        header.setDocumentDate(documentDate);

        header.setSenderParty(createSenderParty());
        header.setReceiverParty(createReceiverParty());

        // TODO : determiner les dates dans le header.
        header.setAchievementDate(createAchievementDate(passage.getDateFacturation(), passage.getDateFacturation()));
        header.setCurrency(eBillFacture.getDevise());
        // header.setAccountAssignment(createAccountAssignment());

        // Dans le cas d'un bulletin de soldes, ajoute le transactionID de l'entete de reference dans une balise de type FixedReference
        if (eBillFacture.isBulletinsDeSoldes()) {
            header.getFixedReference().add(createFixedReference(of, enteteReference.geteBillTransactionID()));
        // Dans le cas d'une notes de crédit, ajoute le transactionID de l'entete dans une balise de type FixedReference
        } else if (eBillFacture.isNotesCredit()) {
            header.getFixedReference().add(createFixedReference(of, entete.geteBillTransactionID()));
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

        XMLGregorianCalendar paymentDueDate = convertStringDateToXmlCalendarDate(getDateEcheance());
        paymentInformation.setPaymentDueDate(paymentDueDate);
        paymentInformation.setPaymentType(eBillFacture.getPaymentType());

        if (eBillFacture.isBulletinNeutre()) {
            paymentInformation.setFixAmount("No");
        } else {
            paymentInformation.setFixAmount("Yes");
        }

        if (eBillFacture.isBVR()) {
            BillHeaderType.PaymentInformation.ESR esr = of.createBillHeaderTypePaymentInformationESR();
            esr.setESRCustomerNumber(eBillFacture.getNumeroCC());
            esr.setESRReferenceNumber(reference);
            paymentInformation.setESR(esr);
        } else if (eBillFacture.isQR()) {
            BillHeaderType.PaymentInformation.IBAN iban = of.createBillHeaderTypePaymentInformationIBAN();
            iban.setIBAN(eBillFacture.getNumeroCC().replaceAll("\\s", ""));
            iban.setCreditorReference(reference);
            paymentInformation.setIBAN(iban);
        }

        //TODO : voir comment on peut gérer les accomptes.
//        BillHeaderType.PaymentInformation.Instalments instalments = of.createBillHeaderTypePaymentInformationInstalments();
//        InstalmentType instalment = of.createInstalmentType();
//        instalment.setAmount(new BigDecimal(""));
//        instalment.setPaymentDueDate("");
//        instalments.getInstalment().add(instalment);
//        paymentInformation.set(instalments);

        return paymentInformation;
    }

    /**
     * Création du Line Items de la facture eBill
     *
     * @return le Line Items de la facture eBill
     */
    private InvoiceBillType.LineItems createLineItems() {
        ObjectFactory of = new ObjectFactory();
        InvoiceBillType.LineItems lineItems = of.createInvoiceBillTypeLineItems();

        // Création des LineItems pour les bulletins de soldes
        if(eBillFacture.isBulletinsDeSoldes() && lignesParPaireIdExterne != null) {
            for (int i = 0; i < lignesParPaireIdExterne.size(); i++) {
                LineItemType lineItemType = createLineItemBulletinsDeSoldes(lignesParPaireIdExterne.get(i));
                lineItems.getLineItem().add(lineItemType);
            }
        // Création des LineItems pour les factures
        } else if ((eBillFacture.isQR() || eBillFacture.isBVR()) && lignesParPaireIdExterne != null) {
            for (int i = 0; i < lignesParPaireIdExterne.size(); i++) {
                LineItemType lineItemType = createLineItemFactures(lignesParPaireIdExterne.get(i));
                lineItems.getLineItem().add(lineItemType);
            }
        }
        return lineItems;
    }

    private LineItemType createLineItemFactures(Map lignes) {
        ObjectFactory of = new ObjectFactory();
        LineItemType lineItem = of.createLineItemType();

        lineItem.setLineItemType(eBillFacture.getLineItemType());
        lineItem.setLineItemID(((Integer) lignes.get("COL_ID")).toString());

        String dateDebut = ((String) lignes.get("COL_7_DEBUT"));
        String dateFin = ((String) lignes.get("COL_7_FIN"));
        AchievementDateType achievementDate = null;
        if (StringUtils.isNotBlank(dateDebut) && StringUtils.isNotBlank(dateFin)) {
            achievementDate = createAchievementDate(dateDebut, dateFin);
        }
        lineItem.setAchievementDate(achievementDate);

        lineItem.setProductDescription((String) lignes.get("COL_1"));

        String taux = ((String) lignes.get("COL_5"));
        lineItem.setQuantity(new BigDecimal(StringUtils.isEmpty(taux) ? "0.00" : taux));
        lineItem.setQuantityDescription("1I");

        Double masse = ((Double) lignes.get("COL_4"));
        lineItem.setPriceUnit(new BigDecimal(masse == null ? 0.00 : masse));

        // lineItem.setTax(createTaxLineItem());

        BigDecimal montant = BigDecimal.valueOf((Double) lignes.get("COL_6"));
        lineItem.setAmountInclusiveTax(montant);
        lineItem.setAmountExclusiveTax(montant);

        // lineItem.setAccountAssignment(createAccountAssignment());

        return lineItem;
    }

    private LineItemType createLineItemBulletinsDeSoldes(Map lignes) {
        ObjectFactory of = new ObjectFactory();
        LineItemType lineItem = of.createLineItemType();

        lineItem.setLineItemType(eBillFacture.getLineItemType());
        lineItem.setLineItemID(((Integer) lignes.get("COL_ID")).toString());

        String dateDebut = (String) lignes.get("COL_1");
        String dateFin = (String) lignes.get("COL_2");
        AchievementDateType achievementDate = null;
        if (StringUtils.isNotBlank(dateDebut) && StringUtils.isNotBlank(dateFin)) {
            achievementDate = createAchievementDate(dateDebut, dateFin);
        }
        lineItem.setAchievementDate(achievementDate);

        lineItem.setProductDescription((String) lignes.get("COL_3"));

        lineItem.setQuantity(new BigDecimal(0));
        lineItem.setQuantityDescription("1I");
        lineItem.setPriceUnit(new BigDecimal(1));

        // lineItem.setTax(createTaxLineItem());

        BigDecimal montant = BigDecimal.valueOf((Double) lignes.get("COL_4"));
        lineItem.setAmountInclusiveTax(montant);
        lineItem.setAmountExclusiveTax(montant);

        // lineItem.setAccountAssignment(createAccountAssignment());

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

        // TODO : mettre en place montant déjà payé (bulletin de solde ??)
        summaryType.setTotalAmountPaid(new BigDecimal(0.0));

        if (eBillFacture.isBulletinsDeSoldes()) {
            summaryType.setTotalAmountExclusiveTax(new FWCurrency(montantBulletinSoldes).getBigDecimalValue());
            summaryType.setTotalAmountInclusiveTax(new FWCurrency(montantBulletinSoldes).getBigDecimalValue());
            summaryType.setTotalAmountDue(new FWCurrency(montantBulletinSoldes).getBigDecimalValue());
        } else {
            summaryType.setTotalAmountExclusiveTax(entete.getTotalFactureCurrency().getBigDecimalValue());
            summaryType.setTotalAmountInclusiveTax(entete.getTotalFactureCurrency().getBigDecimalValue());
            summaryType.setTotalAmountDue(entete.getTotalFactureCurrency().getBigDecimalValue());
        }

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
        taxType.setTotalTax(new BigDecimal(0.0));

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
        taxDetailType.setRate(new BigDecimal(0.0));
        taxDetailType.setAmount(new BigDecimal(0.0));

        if (eBillFacture.isBulletinsDeSoldes()) {
            taxDetailType.setBaseAmountInclusiveTax(new FWCurrency(montantBulletinSoldes).getBigDecimalValue());
            taxDetailType.setBaseAmountExclusiveTax(new FWCurrency(montantBulletinSoldes).getBigDecimalValue());
        } else {
            taxDetailType.setBaseAmountInclusiveTax(entete.getTotalFactureCurrency().getBigDecimalValue());
            taxDetailType.setBaseAmountExclusiveTax(entete.getTotalFactureCurrency().getBigDecimalValue());
        }

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

    public FAPassage getPassage() {
        return passage;
    }

    public void setPassage(FAPassage passage) {
        this.passage = passage;
    }

    public String getBillerId() {
        return billerId;
    }

    public void setBillerId(String billerId) {
        this.billerId = billerId;
    }

    public BSession getSession() {
        return session;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public String getDateEcheance() throws Exception {
        APISectionDescriptor sectionDescriptor = ((FAApplication) getSession().getApplication()).getSectionDescriptor(getSession());
        sectionDescriptor.setSection(entete.getIdExterneFacture(), entete.getIdTypeFacture(), entete.getIdSousType(), passage
                .getDateFacturation(), "", "");
        return sectionDescriptor.getDateEcheanceFacturation();
    }

    public FAAfact getAfact() {
        return afact;
    }

    public void setAfact(FAAfact afact) {
        this.afact = afact;
    }

    public JadePublishDocument getAttachedDocument() {
        return attachedDocument;
    }

    public void setAttachedDocument(JadePublishDocument attachedDocument) {
        this.attachedDocument = attachedDocument;
    }

    public String geteBillAccountID() {
        return eBillAccountID;
    }

    public void seteBillAccountID(String eBillAccountID) {
        this.eBillAccountID = eBillAccountID;
    }

    public String getMontantBulletinSoldes() {
        return montantBulletinSoldes;
    }

    public void setMontantBulletinSoldes(String montantBulletinSoldes) {
        this.montantBulletinSoldes = montantBulletinSoldes;
    }

    public List<Map> getLignesParPaireIdExterne() {
        return lignesParPaireIdExterne;
    }

    public void setLignesParPaireIdExterne(List<Map> lignesParPaireIdExterne) {
        this.lignesParPaireIdExterne = lignesParPaireIdExterne;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
