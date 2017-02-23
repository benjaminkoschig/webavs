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
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.ClearingSystemIdentification2Choice;
import com.six_interbank_clearing.de.pain_001_001_03_ch_02.ClearingSystemMemberIdentification2;
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

    private static final String CODE_PAYS_REGEX = "[A-Z]{2,2}";
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

        final boolean isBVR = CASepaOVConverterUtils.getTypeVersement(ov).equals(
                CASepaOVConverterUtils.ORDRE_VERSEMENT_BVR);

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
        if (isBVR) {
            PaymentTypeInformation19CH pmtTpInfC = factory.createPaymentTypeInformation19CH();
            pmtTpInfC.setInstrPrty(instrPrty);
            LocalInstrument2Choice lclInstrm = factory.createLocalInstrument2Choice();
            lclInstrm.setPrtry(CASepaOVConverterUtils.ExternalLocalInstrument1_CH01);
            pmtTpInfC.setLclInstrm(lclInstrm);
            cLevelData.setPmtTpInf(pmtTpInfC);
        }
        // Amount
        AmountType3Choice amt = factory.createAmountType3Choice();
        ActiveOrHistoricCurrencyAndAmount instdAmt = factory.createActiveOrHistoricCurrencyAndAmount();
        instdAmt.setCcy(ov.getCodeISOMonnaieBonification());
        instdAmt.setValue(new BigDecimal(ov.getMontant()));
        amt.setInstdAmt(instdAmt);
        cLevelData.setAmt(amt);

        // BIC
        if (!isBVR && CASepaOVConverterUtils.isCLevelBicRequired(key)) {
            BranchAndFinancialInstitutionIdentification4CH cbtrAgt = factory
                    .createBranchAndFinancialInstitutionIdentification4CH();
            FinancialInstitutionIdentification7CH finInstnId = factory.createFinancialInstitutionIdentification7CH();
            if (CASepaOVConverterUtils.isCLevelCCP(ov)) {
                ClearingSystemMemberIdentification2 clrSys = factory.createClearingSystemMemberIdentification2();
                ClearingSystemIdentification2Choice clrSysId = factory.createClearingSystemIdentification2Choice();
                clrSysId.setCd(CASepaOVConverterUtils.CLEARING_SUISSE_BCC_SYS_ID);
                clrSys.setClrSysId(clrSysId);
                clrSys.setMmbId(CASepaOVConverterUtils.CLEARING_POSTFINANCE);
                finInstnId.setClrSysMmbId(clrSys);
            } else {
                if (key.isPaysDestination(CASepaOVConverterUtils.PAYS_DESTINATION_SUISSE)) {
                    ClearingSystemMemberIdentification2 clrSys = factory.createClearingSystemMemberIdentification2();
                    ClearingSystemIdentification2Choice clrSysId = factory.createClearingSystemIdentification2Choice();
                    clrSysId.setCd(CASepaOVConverterUtils.CLEARING_SUISSE_BCC_SYS_ID);
                    clrSys.setClrSysId(clrSysId);
                    clrSys.setMmbId(ov.getAdressePaiement().getBanque().getClearing());
                    finInstnId.setClrSysMmbId(clrSys);
                } else {
                    finInstnId.setBIC(CASepaOVConverterUtils.getCbtrAgtBIC(ov));
                }
            }
            cbtrAgt.setFinInstnId(finInstnId);
            cLevelData.setCdtrAgt(cbtrAgt);
        }
        // Creditor
        PartyIdentification32CHName creditor = factory.createPartyIdentification32CHName();
        // +Name
        creditor.setNm(CASepaOVConverterUtils.getCreditorName70(ov));

        // +Postal Adr
        if (!isBVR) {

            PostalAddress6CH postalAdr = factory.createPostalAddress6CH();
            // certains code pays sont vide en DB (retour de test ISO20022-32)
            String codePays = ov.getAdressePaiement().getAdresseCourrier().getPaysISO();
            if (codePays.matches(CODE_PAYS_REGEX)) {
                postalAdr.setCtry(codePays);
            }

            if (key.isPaysDestination(CASepaOVConverterUtils.PAYS_DESTINATION_SUISSE)) {
                // adrLine interdit pour les type mandat
                String rue = CASepaCommonUtils.limit70(ov.getAdressePaiement().getAdresseCourrier().getRue().trim());
                postalAdr.setStrtNm((rue.isEmpty() ? null : rue));
                // FIXME besoin de modif des interfaces PIXYS
                String postCode = CASepaCommonUtils
                        .limit16(ov.getAdressePaiement().getAdresseCourrier().getNumPostal());
                postalAdr.setPstCd(postCode.isEmpty() ? null : postCode);
                String town = CASepaCommonUtils.limit35(ov.getAdressePaiement().getAdresseCourrier().getLocalite());
                postalAdr.setTwnNm(town.isEmpty() ? null : town);

                // String rue = CASepaCommonUtils.limit70(ov.getAdressePaiement().getAdresseCourrier().getRueSansNum());
                // String numeroRue =
                // CASepaCommonUtils.limit16(ov.getAdressePaiement().getAdresseCourrier().getNumeroRue());
                // postalAdr.setStrtNm((rue.isEmpty() ? null : rue));
                // postalAdr.setBldgNb((numeroRue.isEmpty() ? null : numeroRue));
                // postalAdr.setPstCd(CASepaCommonUtils.limit16(ov.getAdressePaiement().getAdresseCourrier().getNumPostal()));
                // postalAdr.setTwnNm(CASepaCommonUtils.limit35(ov.getAdressePaiement().getAdresseCourrier().getLocalite()));
                // postalAdr.setCtry(ov.getAdressePaiement().getAdresseCourrier().getPaysISO());
            } else {

                if (!ov.getAdressePaiement().getAdresseCourrier().getRue().isEmpty()) {
                    postalAdr.getAdrLine().add(
                            CASepaCommonUtils.limit70(ov.getAdressePaiement().getAdresseCourrier().getRue()));
                }
                postalAdr.getAdrLine().add(
                        CASepaCommonUtils.limit70(ov.getAdressePaiement().getAdresseCourrier().getPaysISO() + "-"
                                + ov.getAdressePaiement().getAdresseCourrier().getNumPostal() + " "
                                + ov.getAdressePaiement().getAdresseCourrier().getLocalite()));

            }
            creditor.setPstlAdr(postalAdr);
        }
        cLevelData.setCdtr(creditor);

        // Creditor Account
        if (!CASepaOVConverterUtils.isMandat(ov)) {

            CashAccount16CHId cdtrAcct = factory.createCashAccount16CHId();

            AccountIdentification4ChoiceCH id = factory.createAccountIdentification4ChoiceCH();
            if (isBVR) {
                try {
                    id.setOthr(CASepaOVConverterUtils.getNumAdherentBVR(ov));
                } catch (SepaException e) {
                    throw new Exception(getSession().getLabel("ISO20022_NUMERO_ADHERENT_BVR_NON_IDENTIFIE")
                            + ov.getNumTransaction(), e);
                }
            } else {
                id.setIBAN(CASepaOVConverterUtils.getCbtrIBAN(ov));
                id.setOthr(CASepaOVConverterUtils.getCbtrNotIBAN(ov));
            }
            cdtrAcct.setId(id);
            cLevelData.setCdtrAcct(cdtrAcct);
        }

        RemittanceInformation5CH rmtInf = factory.createRemittanceInformation5CH();
        if (!CASepaOVConverterUtils.isBVR(key)) {
            String motif140 = CASepaOVConverterUtils.getMotif140(ov);
            if (motif140 != null) {
                rmtInf.setUstrd(motif140);
                cLevelData.setRmtInf(rmtInf);
            }
        } else {
            StructuredRemittanceInformation7 strd = factory.createStructuredRemittanceInformation7();
            CreditorReferenceInformation2 cdtrRefInf = factory.createCreditorReferenceInformation2();
            cdtrRefInf.setRef(CASepaCommonUtils.limit35(ov.getReferenceBVR()));
            strd.setCdtrRefInf(cdtrRefInf);
            rmtInf.setStrd(strd);
            cLevelData.setRmtInf(rmtInf);
        }

        if (!bLevels.containsKey(key)) {

            // init Blevel
            PaymentInstructionInformation3CH bLevelData = factory.createPaymentInstructionInformation3CH();
            bLevelData.setPmtInfId(key.getKeyString());
            bLevelData.setPmtMtd(CASepaOVConverterUtils.getPmtMtd(key));
            bLevelData.setBtchBookg(null); // from OG --> will be set at aggregate
            PaymentTypeInformation19CH pmtTpInfB = factory.createPaymentTypeInformation19CH();
            if (!isBVR) {
                pmtTpInfB.setSvcLvl(CASepaOVConverterUtils.getSvcLvl(ov));
                pmtTpInfB.setLclInstrm(CASepaOVConverterUtils.getLclInstrm(key));
            }
            pmtTpInfB.setCtgyPurp(CASepaOVConverterUtils.getCtgyPurp(ov));
            bLevelData.setPmtTpInf(pmtTpInfB);

            key.setbLevel(bLevelData);
            List<CreditTransferTransactionInformation10CH> cLevels = new ArrayList<CreditTransferTransactionInformation10CH>();
            cLevels.add(cLevelData);
            bLevels.put(key, cLevels);
            logger.debug("New blevel group key : [{}]", key.getKeyString());
        } else {
            bLevels.get(key).add(cLevelData);
            logger.debug("added clevel to blevel [{}] contain now {}", key.getKeyString(), bLevels.get(key).size());
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
        // vérifier qu'on ai au moins une transaction
        if (bLevels.isEmpty()) {
            throw new SepaException(getSession().getLabel("ISO20022_ERROR_EOF_NO_TRANSACTION"));
        }

        String nomCaisse = CASepaOGConverterUtils.getNomCaisse70(og);
        // set validation fields
        grpHeader.setNbOfTxs(og.getNbTransactions());
        grpHeader.setCtrlSum(new BigDecimal(og.getTotal()));
        grpHeader.setMsgId(og.getNumLivraison());
        PartyIdentification32CHNameAndId initPty = factory.createPartyIdentification32CHNameAndId();
        initPty.setNm(CASepaCommonUtils.limit70(nomCaisse));
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
        dbtr.setNm(CASepaCommonUtils.limit70(nomCaisse));
        CashAccount16CHIdTpCcy dbtrAcct = factory.createCashAccount16CHIdTpCcy();
        AccountIdentification4ChoiceCH id = factory.createAccountIdentification4ChoiceCH();
        id.setIBAN(CASepaOGConverterUtils.getDbtrIBAN(og));
        id.setOthr(CASepaOGConverterUtils.getDbtrNotIBAN(og));
        dbtrAcct.setId(id);

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
            // Besoin d'unicité
            bLevelData.setPmtInfId(og.getNumLivraison() + bLevelData.getPmtInfId());
            // Set Blevel info from OG
            bLevelData.setBtchBookg(btchBookg);
            // priority to set here if not a BVR
            if (!bLevelData.getPmtInfId().contains(CASepaOVConverterUtils.ORDRE_VERSEMENT_BVR)) {
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
