package globaz.osiris.db.ordres.sepa;

import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.common.Jade;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
import globaz.osiris.db.ordres.format.CAOrdreFormateur;
import globaz.osiris.db.ordres.sepa.AbstractSepa.SepaException;
import globaz.osiris.db.ordres.sepa.utils.CASepaCommonUtils;
import globaz.osiris.db.ordres.sepa.utils.CASepaGroupeOGKey;
import globaz.osiris.db.ordres.sepa.utils.CASepaOGConverterUtils;
import globaz.osiris.db.ordres.sepa.utils.CASepaOVConverterUtils;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.AccountIdentification4ChoiceCH;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.ActiveOrHistoricCurrencyAndAmount;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.AmountType3Choice;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.BranchAndFinancialInstitutionIdentification4CH;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.BranchAndFinancialInstitutionIdentification4CHBicOrClrId;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.CashAccount16CHId;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.CashAccount16CHIdAndCurrency;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.CashAccount16CHIdTpCcy;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.CashAccountType2;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.ContactDetails2CH;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.CreditTransferTransactionInformation10CH;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.CreditorReferenceInformation2;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.Document;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.FinancialInstitutionIdentification7CH;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.FinancialInstitutionIdentification7CHBicOrClrId;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.LocalInstrument2Choice;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.ObjectFactory;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.PartyIdentification32CH;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.PartyIdentification32CHName;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.PartyIdentification32CHNameAndId;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.PaymentIdentification1;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.PaymentInstructionInformation3CH;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.PaymentTypeInformation19CH;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.PostalAddress6CH;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.Priority2Code;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.RemittanceInformation5CH;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.StructuredRemittanceInformation7;

/**
 * Formateur du xml, encapsule les objet de la xml et les marshalle lors de la demande de EOF
 * 
 * @author cel
 * 
 */
public class CAProcessFormatOrdreSEPA extends CAOrdreFormateur {

    private static final String XSD_FOLDER = "/schema/com/six_interbank_clearing/de/pain.001.001.03.ch.02.xsd/";
    private static final String XSD_FILE_NAME = "pain.001.001.03.ch.02.xsd";
    private final static String MARSHALLED_XML = "marshalled.xml";
    private final static Logger logger = LoggerFactory.getLogger(CAProcessFormatOrdreSEPA.class);

    private com.six_interbank_clearing.de.pain_001_001_03_ch_02.ObjectFactory factory = new ObjectFactory();

    private com.six_interbank_clearing.de.pain_001_001_03_ch_02.Document root;
    private com.six_interbank_clearing.de.pain_001_001_03_ch_02.CustomerCreditTransferInitiationV03CH cstrmCdt;
    private com.six_interbank_clearing.de.pain_001_001_03_ch_02.GroupHeader32CH grpHeader;

    private Priority2Code instrPrty;

    private HashMap<CASepaGroupeOGKey, List<CreditTransferTransactionInformation10CH>> bLevels = new HashMap<CASepaGroupeOGKey, List<CreditTransferTransactionInformation10CH>>();

    @Override
    public StringBuffer format(APICommonOdreVersement ov) throws Exception {

        CASepaGroupeOGKey key = new CASepaGroupeOGKey(ov.getCodeISOMonnaieBonification(),
                CASepaOVConverterUtils.getTypeVersement(ov), CASepaOVConverterUtils.getTypeVirement(ov),
                CASepaOVConverterUtils.getPaysDestination(ov));
        // ids
        CreditTransferTransactionInformation10CH cLevelData = factory.createCreditTransferTransactionInformation10CH();
        PaymentIdentification1 pmtId = factory.createPaymentIdentification1();
        pmtId.setInstrId(CASepaOVConverterUtils.getInstrId(ov));
        pmtId.setEndToEndId(CASepaOVConverterUtils.getEndToEndId(ov));
        cLevelData.setPmtId(pmtId);

        // paiement info (if BVR in Clevel, instead Blevel)
        if (CASepaOVConverterUtils.getTypeVersement(ov).equals(CASepaOVConverterUtils.ORDRE_VERSEMENT_BVR)) {
            PaymentTypeInformation19CH pmtTpInfC = factory.createPaymentTypeInformation19CH();
            pmtTpInfC.setInstrPrty(instrPrty);
            LocalInstrument2Choice lclInstrm = factory.createLocalInstrument2Choice();
            lclInstrm.setPrtry(CASepaOVConverterUtils.ExternalLocalInstrument1_CH01);
            pmtTpInfC.setLclInstrm(lclInstrm);
            cLevelData.setPmtTpInf(pmtTpInfC);
        }
        // Amount
        AmountType3Choice amt = new AmountType3Choice();
        ActiveOrHistoricCurrencyAndAmount instdAmt = factory.createActiveOrHistoricCurrencyAndAmount();
        instdAmt.setCcy(ov.getCodeISOMonnaieBonification());
        instdAmt.setValue(new BigDecimal(ov.getMontant()));
        amt.setInstdAmt(instdAmt);
        cLevelData.setAmt(amt);

        // BIC
        if (CASepaOVConverterUtils.isCLevelBicRequired(ov)) {
            BranchAndFinancialInstitutionIdentification4CH cbtrAgt = factory
                    .createBranchAndFinancialInstitutionIdentification4CH();
            FinancialInstitutionIdentification7CH finInstnId = factory.createFinancialInstitutionIdentification7CH();
            finInstnId.setBIC(CASepaOVConverterUtils.getCbtrAgtBIC(ov));
            cbtrAgt.setFinInstnId(finInstnId);
            cLevelData.setCdtrAgt(cbtrAgt);
        }
        // Creditor
        PartyIdentification32CHName creditor = factory.createPartyIdentification32CHName();
        // +Name
        creditor.setNm(CASepaOVConverterUtils.getCreditorName70(ov));
        // +Postal Adr
        PostalAddress6CH postalAdr = factory.createPostalAddress6CH();
        postalAdr.setStrtNm(ov.getAdressePaiement().getAdresseCourrier().getRue());
        postalAdr.setBldgNb(null);
        postalAdr.setPstCd(ov.getAdressePaiement().getAdresseCourrier().getNumPostal());
        postalAdr.setTwnNm(ov.getAdressePaiement().getAdresseCourrier().getLocalite());
        postalAdr.setCtry(ov.getAdressePaiement().getAdresseCourrier().getPaysISO());
        logger.debug("clevel adress : {} {}, NPA : {} {} {}", postalAdr.getStrtNm(), postalAdr.getBldgNb(),
                postalAdr.getPstCd(), postalAdr.getTwnNm(), postalAdr.getCtry());
        creditor.setPstlAdr(postalAdr);

        cLevelData.setCdtr(creditor);

        // Creditor Account
        if (!CASepaOVConverterUtils.isMandat(ov)) {
            CashAccount16CHId cdtrAcct = factory.createCashAccount16CHId();
            AccountIdentification4ChoiceCH id = factory.createAccountIdentification4ChoiceCH();
            id.setIBAN(CASepaOVConverterUtils.getCbtrIBAN(ov));
            id.setOthr(CASepaOVConverterUtils.getCbtrNotIBAN(ov));
            cdtrAcct.setId(id);
            cLevelData.setCdtrAcct(cdtrAcct);
        }
        RemittanceInformation5CH rmtInf = factory.createRemittanceInformation5CH();
        if (!CASepaOVConverterUtils.isBVR(ov)) {

            rmtInf.setUstrd(CASepaOVConverterUtils.getMotif140(ov));

        } else {
            StructuredRemittanceInformation7 strd = factory.createStructuredRemittanceInformation7();
            CreditorReferenceInformation2 cdtrRefInf = factory.createCreditorReferenceInformation2();
            cdtrRefInf.setRef(ov.getReferenceBVR());
            strd.setCdtrRefInf(cdtrRefInf);
            rmtInf.setStrd(strd);
        }
        cLevelData.setRmtInf(rmtInf);

        if (!bLevels.containsKey(key)) {

            // init Blevel
            PaymentInstructionInformation3CH bLevelData = factory.createPaymentInstructionInformation3CH();
            bLevelData.setPmtInfId(CASepaOVConverterUtils.getPmtInfId(ov));
            bLevelData.setPmtMtd(CASepaOVConverterUtils.getPmtMtd(ov));
            bLevelData.setBtchBookg(null); // from OG --> will be set at aggregate
            PaymentTypeInformation19CH pmtTpInfB = factory.createPaymentTypeInformation19CH();
            if (!CASepaOVConverterUtils.getTypeVersement(ov).equals(CASepaOVConverterUtils.ORDRE_VERSEMENT_BVR)) {
                pmtTpInfB.setSvcLvl(CASepaOVConverterUtils.getSvcLvl(ov));
                pmtTpInfB.setLclInstrm(CASepaOVConverterUtils.getLclInstrm(ov));
            }
            pmtTpInfB.setCtgyPurp(CASepaOVConverterUtils.getCtgyPurp(ov));
            bLevelData.setPmtTpInf(pmtTpInfB);

            key.setbLevel(bLevelData);
            List<CreditTransferTransactionInformation10CH> cLevels = new ArrayList<CreditTransferTransactionInformation10CH>();
            cLevels.add(cLevelData);
            bLevels.put(key, cLevels);
            logger.debug("New blevel group key : [{}]", key.toString());
        } else {
            bLevels.get(key).add(cLevelData);
            logger.debug("added clevel to blevel [{}] contain now {}", key.toString(), bLevels.get(key).size());
        }
        return null;
    }

    @Override
    public StringBuffer format(CAOperationOrdreRecouvrement or) throws Exception {
        throw new SepaException();
    }

    /**
     * use it to set validation fields, and marshal object to xml to give to parent process
     */
    @Override
    public StringBuffer formatEOF(APIOrdreGroupe og) throws Exception {
        String nomCaisse = CASepaOGConverterUtils.getNomCaisse70(og);
        // set validation fields
        grpHeader.setNbOfTxs(og.getNbTransactions());
        grpHeader.setCtrlSum(new BigDecimal(og.getTotal()));
        grpHeader.setMsgId("TODO determiner l'id");
        PartyIdentification32CHNameAndId initPty = factory.createPartyIdentification32CHNameAndId();
        initPty.setNm(nomCaisse);
        ContactDetails2CH ctctDtls = factory.createContactDetails2CH();
        ctctDtls.setNm(CASepaCommonUtils.getAppName());
        ctctDtls.setOthr(CASepaCommonUtils.getVersion());
        initPty.setCtctDtls(ctctDtls);
        grpHeader.setInitgPty(initPty);
        grpHeader.setCreDtTm(CASepaCommonUtils.getCurrentTime());

        // aggregate groups
        Boolean btchBookg = CASepaOGConverterUtils.getBtchBook(og);

        XMLGregorianCalendar reqdExctmDt = CASepaOGConverterUtils.getReqdExctnDt(og);
        PartyIdentification32CH dbtr = factory.createPartyIdentification32CH();
        dbtr.setNm(nomCaisse);
        CashAccount16CHIdTpCcy dbtrAcct = factory.createCashAccount16CHIdTpCcy();
        AccountIdentification4ChoiceCH id = factory.createAccountIdentification4ChoiceCH();
        id.setIBAN(CASepaOGConverterUtils.getDbtrIBAN(og));
        id.setOthr(CASepaOGConverterUtils.getDbtrNotIBAN(og));
        dbtrAcct.setId(id);
        CashAccountType2 tp = factory.createCashAccountType2();
        tp.setPrtry(CASepaOGConverterUtils.getTpProprietary(og));
        dbtrAcct.setTp(tp);

        // BIC
        BranchAndFinancialInstitutionIdentification4CHBicOrClrId dbtrAgt = factory
                .createBranchAndFinancialInstitutionIdentification4CHBicOrClrId();
        FinancialInstitutionIdentification7CHBicOrClrId finInstnId = factory
                .createFinancialInstitutionIdentification7CHBicOrClrId();
        finInstnId.setBIC(CASepaOGConverterUtils.getDbtrAgtBIC(og));
        dbtrAgt.setFinInstnId(finInstnId);

        CashAccount16CHIdAndCurrency chrgsAcct = CASepaOGConverterUtils.getChrgsAcct(og);

        for (CASepaGroupeOGKey key : bLevels.keySet()) {
            PaymentInstructionInformation3CH bLevelData = key.getbLevel();
            // Set Blevel info from OG
            bLevelData.setBtchBookg(btchBookg);
            // priority to set here if not a BVR
            if (!bLevelData.getPmtInfId().equals(CASepaOVConverterUtils.B_LEVEL_ID_BVR)) {
                bLevelData.getPmtTpInf().setInstrPrty(instrPrty);
            }
            bLevelData.setReqdExctnDt(reqdExctmDt);
            bLevelData.setDbtr(dbtr);
            bLevelData.setDbtrAcct(dbtrAcct);
            bLevelData.setDbtrAgt(dbtrAgt);
            bLevelData.setChrgsAcct(chrgsAcct);
            // aggregate Clevel
            bLevelData.getCdtTrfTxInf().addAll(bLevels.get(key));
            // add this Blevel to Alevel
            cstrmCdt.getPmtInf().add(bLevelData);
        }

        logger.debug("blevels : {} ", cstrmCdt.getPmtInf().size());
        for (PaymentInstructionInformation3CH blvl : cstrmCdt.getPmtInf()) {
            logger.debug("clevels [{}] ", blvl.getCdtTrfTxInf().size());
        }
        File xmlMarshaled = marshallCompleteMessage(root, XSD_FILE_NAME);
        logger.debug(xmlMarshaled.toString());
        return new StringBuffer(xmlMarshaled.getAbsolutePath());
    }

    @Override
    public StringBuffer formatHeader(APIOrdreGroupe og) throws Exception {
        root = factory.createDocument();
        cstrmCdt = factory.createCustomerCreditTransferInitiationV03CH();
        grpHeader = factory.createGroupHeader32CH();
        cstrmCdt.setGrpHdr(grpHeader);
        root.setCstmrCdtTrfInitn(cstrmCdt);
        instrPrty = CASepaOGConverterUtils.getInstrPrty(og);
        return null;
    }

    protected File marshallCompleteMessage(Document element, String xsdFileName) throws SAXException, JAXBException,
            PropertyException, IOException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        URL url = getClass().getResource(XSD_FOLDER + xsdFileName);

        Schema schema = sf.newSchema(url);
        JAXBContext jc = JAXBContext.newInstance(element.getClass());

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setSchema(schema);

        String filename = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil.addFilenameSuffixUID(MARSHALLED_XML);

        File f = new File(filename);
        f.createNewFile();

        try {
            marshaller.setEventHandler(new ValidationEventHandler() {

                @Override
                public boolean handleEvent(ValidationEvent event) {
                    logger.warn("JAXB validation error : " + event.getMessage(), this);
                    return false;
                }
            });
            marshaller.marshal(factory.createDocument(element), f);

        } catch (JAXBException exception) {
            logger.error("JAXB validation has thrown a JAXBException : " + exception.toString(), exception);
            throw exception;
        }
        return f;
    }

}
