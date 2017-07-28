package globaz.osiris.db.ordres.sepa;

import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.common.Jade;
import globaz.osiris.api.ordre.APICommonOdreVersement;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
import globaz.osiris.db.ordres.format.CAOrdreFormateur;
import globaz.osiris.db.ordres.sepa.AbstractSepa.SepaException;
import globaz.osiris.db.ordres.sepa.utils.CASepaCommonUtils;
import globaz.osiris.db.ordres.sepa.utils.CASepaOGConverterUtils;
import globaz.osiris.db.ordres.sepa.utils.CASepaORConverterUtils;
import globaz.osiris.db.ordres.sepa.utils.CASepaPain008GroupeOGKey;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdresseCourrier;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import ch.globaz.common.properties.PropertiesException;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.AccountIdentification4Choice;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.ActiveOrHistoricCurrencyAndAmount;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.BranchAndFinancialInstitutionIdentification4;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.CashAccount16;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.ContactDetails2;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.CustomerDirectDebitInitiationV02;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.DirectDebitTransactionInformation9;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.DocumentCHPain008;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.GenericOrganisationIdentification1;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.GenericPersonIdentification1;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.GroupHeader39;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.ObjectFactory;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.OrganisationIdentification4;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.Party6Choice;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.PartyIdentification32;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.PaymentIdentification1;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.PaymentInstructionInformation4;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.PaymentMethod2Code;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.PaymentTypeInformation20;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.PersonIdentification5;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.PostalAddress6;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.RemittanceInformation5;

/**
 * Insérez la description du type ici. Date de création : (08.04.2002 09:05:57)
 * 
 * @author: Administrator
 */
public abstract class CAFormatRecouvrementISOAbstract extends CAOrdreFormateur {
    private static final Logger logger = LoggerFactory.getLogger(CAFormatRecouvrementISOAbstract.class);

    protected static final String CODE_PAYS_REGEX = "[A-Z]{2,2}";
    protected static final String XSD_FOLDER = "/schema/com/six_interbank_clearing/de/pain/_008/_001/_02/ch/_03/xsd/";
    protected static final String XSD_FILE_NAME = "pain.008.001.02.ch.03.xsd";
    protected static final String MARSHALLED_XML = "marshalled.xml";

    protected ObjectFactory factory = new ObjectFactory();
    protected DocumentCHPain008 root;
    protected CustomerDirectDebitInitiationV02 cstrmDbt;
    protected GroupHeader39 grpHeader;
    protected HashMap<CASepaPain008GroupeOGKey, List<DirectDebitTransactionInformation9>> bLevels = new HashMap<CASepaPain008GroupeOGKey, List<DirectDebitTransactionInformation9>>();
    protected APIOrganeExecution organeExecution;

    @Override
    public StringBuffer formatHeader(APIOrdreGroupe og) throws Exception {
        root = factory.createDocumentCHPain008();
        cstrmDbt = factory.createCustomerDirectDebitInitiationV02();
        grpHeader = factory.createGroupHeader39();
        cstrmDbt.setGrpHdr(grpHeader);
        root.setCstmrDrctDbtInitn(cstrmDbt);

        organeExecution = og.getOrganeExecution();

        return null;
    }

    @Override
    public StringBuffer format(CAOperationOrdreRecouvrement or) throws Exception {
        logger.debug("[Format] started for OrdreRecouvrement with id {}", or.getId());

        final CAAdressePaiementFormatter adpf = new CAAdressePaiementFormatter();
        adpf.setAdressePaiement(or.getAdressePaiement());
        checkFormat(adpf);
        adpf.checkAdressePaiement(getSession());

        final DirectDebitTransactionInformation9 cLevel = factory.createDirectDebitTransactionInformation9();

        cLevel.setPmtId(createPaymentId(or));
        cLevel.setInstdAmt(createInstructedAmount(or));
        cLevel.setDbtrAgt(giveDebitorAgentCLevel(adpf));
        cLevel.setDbtr(createDebitorPartyIdentification(or, adpf));
        cLevel.setDbtrAcct(createDebtrAcct(or));
        cLevel.setRmtInf(giveRemittanceInfoCLevel(or, organeExecution));

        final CASepaPain008GroupeOGKey key = new CASepaPain008GroupeOGKey(or.getCodeISOMonnaieBonification(),
                CASepaORConverterUtils.getTypeVersement(), CASepaORConverterUtils.getPaysDestination(adpf),
                giveTypeTransfert());

        logger.debug("[Format] key created with name {}", key.getKeyString());

        if (!bLevels.containsKey(key)) {

            final List<DirectDebitTransactionInformation9> cLevels = new ArrayList<DirectDebitTransactionInformation9>();
            cLevels.add(cLevel);

            final PaymentInstructionInformation4 bLevel = factory.createPaymentInstructionInformation4();
            bLevel.setPmtInfId(key.getKeyString());
            key.setbLevel(bLevel);
            bLevels.put(key, cLevels);

            logger.debug("[Format] Blevel created for group key : [{}] and clevel attached", key.getKeyString());
        } else {
            bLevels.get(key).add(cLevel);

            logger.debug("[Format] Clevel attached to blevel [{}] contain now {}", key.getKeyString(), bLevels.get(key)
                    .size());
        }

        logger.debug("[Format] ended for OrdreRecouvrement with id {}", or.getId());
        return null;
    }

    @Override
    public StringBuffer formatEOF(APIOrdreGroupe og) throws Exception {
        logger.debug("[FormatEOF] started for OrdreGroupe with number {}", og.getNumeroOG());

        final String nomCaisse = CASepaOGConverterUtils.getNomCaisse70(organeExecution);

        final CAAdressePaiementFormatter adpf = new CAAdressePaiementFormatter();
        adpf.setAdressePaiement(organeExecution.getAdressePaiement());
        checkFormatEOF(adpf);
        adpf.checkAdressePaiement(getSession());

        if (bLevels.isEmpty()) {
            throw new SepaException(getSession().getLabel("ISO20022_ERROR_EOF_NO_TRANSACTION"));
        }

        initALevel(og, organeExecution, nomCaisse);

        for (Entry<CASepaPain008GroupeOGKey, List<DirectDebitTransactionInformation9>> entry : bLevels.entrySet()) {

            final PaymentInstructionInformation4 bLevel = initBLevel(og, adpf, organeExecution, nomCaisse, entry);

            cstrmDbt.getPmtInf().add(bLevel);
        }

        final File xmlMarshaled = marshallCompleteMessage();

        logger.debug("[FormatEOF] ended for OrdreGroupe with number {}", og.getNumeroOG());

        return new StringBuffer(xmlMarshaled.getAbsolutePath());
    }

    @Override
    public StringBuffer format(APICommonOdreVersement ov) throws Exception {
        throw new SepaException();
    }

    /**
     * Validation du formateur par ordre de recouvrement.
     * 
     * @param adpf Le formater d'adresse avec une adresse initié.
     * @return True si l'adresse est valide.
     */
    protected abstract void checkFormat(CAAdressePaiementFormatter adpf);

    /**
     * Validation du formateur par ordre groupé.
     * 
     * @param adpf Le formater d'adresse avec une adresse initié.
     * @return True si l'adresse est valide.
     */
    protected abstract void checkFormatEOF(CAAdressePaiementFormatter adpf);

    /**
     * Récupération de l'information du numéro d'adhérent ou DTA (A Level).
     * 
     * @param organeExecution L'organe d'exécution.
     * @return L'information du numéro d'adhérent ou DTA.
     */
    protected abstract GenericOrganisationIdentification1 giveInformationIdALevel(
            final APIOrganeExecution organeExecution);

    /**
     * Récupération de l'information du paiement (CHDD, CHTA, ...) (B Level).
     * 
     * @return L'information du paiement.
     */
    protected abstract PaymentTypeInformation20 givePaymentInformationBLevel() throws PropertiesException;

    /**
     * Récupération du type de transfert (CHDD, CHTA) (B Level).
     * 
     * @return Le type de transfert.
     */
    protected abstract String giveTypeTransfert();

    /**
     * Récupération des informations du compte créditeur (IBAN,...) (B Level).
     * 
     * @param og Un ordre groupé.
     * @return L'identification du compte créditeur.
     * @throws Exception Une exception non gérée.
     */
    protected abstract AccountIdentification4Choice giveCreditorAccountBLevel(final APIOrdreGroupe og) throws Exception;

    /**
     * Récupération de l'identification du créditeur agent (clearing,...) (B Level).
     * 
     * @param organeExecution L'organe d'exécution.
     * @param adpf Le formater d'adresse avec une adresse initié.
     * @return L'identification du créditeur agent.
     */
    protected abstract BranchAndFinancialInstitutionIdentification4 giveCreditorAgentBLevel(
            final APIOrganeExecution organeExecution, final CAAdressePaiementFormatter adpf);

    /**
     * Récupération de l'information du numéro d'adhérent ou DTA (CHDD, CHTA...) (B Level).
     * 
     * @param organeExecution L'organe d'exécution.
     * @return L'information du numéro d'adhérent ou DTA.
     */
    protected abstract GenericPersonIdentification1 giveInformationIdBLevel(final APIOrganeExecution organeExecution);

    /**
     * Récupération de l'information de remittance d'un ordre de recouvrement (C Level).
     * 
     * @param or Ordre de recouvrement.
     * @return La remittance selon l'implémentation choisie.
     */
    protected abstract RemittanceInformation5 giveRemittanceInfoCLevel(CAOperationOrdreRecouvrement or,
            APIOrganeExecution organeExecution) throws Exception;

    /**
     * Récupération des informations du compte débiteur (IBAN,...) (C Level).
     * 
     * @param ordreRecouvrement Un ordre de recouvrement.
     * @return L'identification du compte débiteur.
     * @throws Exception Une exception non gérée.
     */
    protected abstract AccountIdentification4Choice giveDebitorAccountCLevel(
            final CAOperationOrdreRecouvrement ordreRecouvrement) throws Exception;

    /**
     * Récupération de l'identification du débiteur agent (clearing,...) (C Level).
     * 
     * @param adpf Le formater d'adresse avec une adresse initié.
     * @return L'identification du débiteur agent.
     */
    protected abstract BranchAndFinancialInstitutionIdentification4 giveDebitorAgentCLevel(
            final CAAdressePaiementFormatter adpf);

    /**
     * Récupération de l'identification technique de l'ordre de recouvrement (C Level).
     * 
     * @param or Un ordre de recouvrement.
     * @return L'identification technique.
     */
    protected abstract String giveEndToEndIdCLevel(CAOperationOrdreRecouvrement or);

    private CashAccount16 createDebtrAcct(CAOperationOrdreRecouvrement or) throws Exception {
        final CashAccount16 cashAccount = factory.createCashAccount16();

        cashAccount.setId(giveDebitorAccountCLevel(or));

        return cashAccount;
    }

    private ActiveOrHistoricCurrencyAndAmount createInstructedAmount(CAOperationOrdreRecouvrement or) {

        final ActiveOrHistoricCurrencyAndAmount currencyAmount = factory.createActiveOrHistoricCurrencyAndAmount();
        currencyAmount.setCcy(or.getCodeISOMonnaieBonification());
        currencyAmount.setValue(new BigDecimal(or.getMontant()));

        return currencyAmount;
    }

    private PaymentIdentification1 createPaymentId(CAOperationOrdreRecouvrement or) {

        final PaymentIdentification1 paymentIdentification = factory.createPaymentIdentification1();
        paymentIdentification.setInstrId(CASepaORConverterUtils.getInstrId(or));
        paymentIdentification.setEndToEndId(giveEndToEndIdCLevel(or));

        return paymentIdentification;
    }

    private PaymentInstructionInformation4 initBLevel(final APIOrdreGroupe og, final CAAdressePaiementFormatter adpf,
            final APIOrganeExecution organeExecution, final String nomCaisse,
            final Entry<CASepaPain008GroupeOGKey, List<DirectDebitTransactionInformation9>> entry) throws Exception {

        final PaymentInstructionInformation4 bLevel = entry.getKey().getbLevel();

        bLevel.setPmtInfId(og.getNumLivraison() + bLevel.getPmtInfId());
        bLevel.setPmtMtd(PaymentMethod2Code.DD);
        bLevel.setPmtTpInf(givePaymentInformationBLevel());
        bLevel.setReqdColltnDt(CASepaOGConverterUtils.getReqdExctnDt(og));
        bLevel.setCdtr(createCreditorPartyIdentification(nomCaisse, adpf));
        bLevel.setCdtrAcct(createCrdtrAcct(og));
        bLevel.setCdtrAgt(giveCreditorAgentBLevel(organeExecution, adpf));
        bLevel.setCdtrSchmeId(createCrdtrSchmeId(organeExecution));

        bLevel.getDrctDbtTxInf().addAll(bLevels.get(entry.getKey()));

        logger.debug("[FormatEOF] Blevel created and attached the number of Clevel {}", bLevels.get(entry.getKey())
                .size());

        return bLevel;
    }

    private void initALevel(APIOrdreGroupe og, final APIOrganeExecution organeExecution, final String nomCaisse)
            throws Exception {

        grpHeader.setMsgId(og.getNumLivraison());
        grpHeader.setCreDtTm(CASepaCommonUtils.getCurrentTime());
        grpHeader.setNbOfTxs(og.getNbTransactions());
        grpHeader.setCtrlSum(new BigDecimal(og.getTotal()));
        grpHeader.setInitgPty(createInitgPtyGrpHeader(nomCaisse, organeExecution));

        logger.debug("[FormatEOF] Alevel has setted");
    }

    private PartyIdentification32 createDebitorPartyIdentification(final CAOperationOrdreRecouvrement or,
            final CAAdressePaiementFormatter adpf) throws Exception {

        final PartyIdentification32 identification = factory.createPartyIdentification32();
        final PostalAddress6 adresse = factory.createPostalAddress6();
        final IntAdresseCourrier adresseCourrier = adpf.getAdresseCourrierBeneficiaire();

        adresse.setStrtNm(CASepaORConverterUtils.getRue(adresseCourrier));
        adresse.setPstCd(adresseCourrier.getNumPostal());
        adresse.setTwnNm(adresseCourrier.getLocalite());

        final String codePays = adresseCourrier.getPaysISO();
        if (codePays.matches(CODE_PAYS_REGEX)) {
            adresse.setCtry(codePays);
        }

        identification.setPstlAdr(adresse);
        identification.setNm(CASepaORConverterUtils.getDebtorName70(or));

        return identification;
    }

    private PartyIdentification32 createCreditorPartyIdentification(final String nomCaisse,
            final CAAdressePaiementFormatter adpf) throws Exception {

        final PartyIdentification32 identification = factory.createPartyIdentification32();
        final PostalAddress6 adresse = factory.createPostalAddress6();
        final IntAdresseCourrier adresseCourrier = adpf.getAdresseCourrierBeneficiaire();

        adresse.setStrtNm(CASepaORConverterUtils.getRue(adresseCourrier));
        adresse.setPstCd(adresseCourrier.getNumPostal());
        adresse.setTwnNm(adresseCourrier.getLocalite());

        final String codePays = adresseCourrier.getPaysISO();
        if (codePays.matches(CODE_PAYS_REGEX)) {
            adresse.setCtry(codePays);
        }

        identification.setPstlAdr(adresse);
        identification.setNm(CASepaCommonUtils.limit70(nomCaisse));

        return identification;
    }

    private PartyIdentification32 createInitgPtyGrpHeader(final String nomCaisse,
            final APIOrganeExecution organeExecution) {

        final PartyIdentification32 partyIdentification = factory.createPartyIdentification32();

        partyIdentification.setNm(CASepaCommonUtils.limit70(nomCaisse));
        partyIdentification.setId(createPartyIdentification(organeExecution));
        partyIdentification.setCtctDtls(createContactDetails());

        return partyIdentification;
    }

    private ContactDetails2 createContactDetails() {

        final ContactDetails2 ctctDtls = factory.createContactDetails2();

        ctctDtls.setNm(CASepaCommonUtils.getAppName());
        ctctDtls.setOthr(CASepaCommonUtils.getVersion());

        return ctctDtls;
    }

    private Party6Choice createPartyIdentification(final APIOrganeExecution organeExecution) {

        final Party6Choice partyChoice = factory.createParty6Choice();
        final OrganisationIdentification4 organisationIdentification = factory.createOrganisationIdentification4();

        organisationIdentification.setOthr(giveInformationIdALevel(organeExecution));
        partyChoice.setOrgId(organisationIdentification);

        return partyChoice;
    }

    private PartyIdentification32 createCrdtrSchmeId(final APIOrganeExecution organeExecution) {

        final PartyIdentification32 partyId = factory.createPartyIdentification32();
        final Party6Choice partyChoice = factory.createParty6Choice();
        final PersonIdentification5 persIdentification = factory.createPersonIdentification5();

        persIdentification.setOthr(giveInformationIdBLevel(organeExecution));
        partyChoice.setPrvtId(persIdentification);
        partyId.setId(partyChoice);

        return partyId;
    }

    private CashAccount16 createCrdtrAcct(APIOrdreGroupe og) throws Exception {

        final CashAccount16 cashAccount = factory.createCashAccount16();

        cashAccount.setId(giveCreditorAccountBLevel(og));

        return cashAccount;
    }

    protected File marshallCompleteMessage() throws SAXException, IOException, JAXBException {

        final SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final URL url = getClass().getResource(XSD_FOLDER + XSD_FILE_NAME);
        final Schema schema = sf.newSchema(url);
        final JAXBContext jc = JAXBContext.newInstance(root.getClass());

        final Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setSchema(schema);

        final String filename = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil.addFilenameSuffixUID(MARSHALLED_XML);

        final File f = new File(filename);
        if (!f.createNewFile()) {
            throw new SepaException(getSession().getLabel("ISO20022_ERROR_EOF_CREATE_FILE"));
        }

        doMarshall(marshaller, f);

        return f;
    }

    private void doMarshall(final Marshaller marshaller, final File f) throws JAXBException {
        try {
            marshaller.setEventHandler(new ValidationEventHandler() {

                @Override
                public boolean handleEvent(ValidationEvent event) {
                    logger.warn("JAXB validation error : " + event.getMessage(), this);
                    if (event.getLocator() != null) {
                        logger.warn("JAXB validation error : " + event.getLocator().getNode(), this);
                        logger.warn("JAXB validation error : " + event.getLocator().getObject(), this);
                    }
                    logger.warn("JAXB validation error : " + event.getLinkedException(), this);
                    return false;
                }
            });

            /*** Lancement du MARSHALLER */
            marshaller.marshal(factory.createDocument(root), f);
        } catch (JAXBException exception) {
            logger.error("JAXB validation has thrown a JAXBException : " + exception.toString(), exception);
            throw exception;
        }
    }
}
