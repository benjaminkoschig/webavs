package globaz.naos.db.avisMutation;

import globaz.commons.sedex.Utils;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.client.util.JadeUtil;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBUtil;
import globaz.jade.job.JadeJob;
import globaz.jade.log.JadeLogger;
import globaz.jade.sedex.JadeSedexDirectory;
import globaz.jade.sedex.JadeSedexDirectory.Entry;
import globaz.jade.sedex.JadeSedexService;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.db.adhesion.AFAdhesion;
import globaz.naos.db.adhesion.AFAdhesionManager;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.avisMutation.avisAddress.AFAvisAddress;
import globaz.naos.db.avisMutation.avisAddress.AFAvisAddressFactory;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.api.ITITiers;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.CommonNSSFormater;
import globaz.webavs.common.ICommonConstantes;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.eahv_iv.xmlns.eahv_iv_2010_000101._4.CommunicationCodeType;
import ch.eahv_iv.xmlns.eahv_iv_2010_000101._4.ContentType;
import ch.eahv_iv.xmlns.eahv_iv_2010_000101._4.HeaderType;
import ch.eahv_iv.xmlns.eahv_iv_2010_000101._4.Message;
import ch.eahv_iv.xmlns.eahv_iv_2010_000101._4.MutationType;
import ch.eahv_iv.xmlns.eahv_iv_2010_000101._4.ObjectFactory;
import ch.eahv_iv.xmlns.eahv_iv_common._1.DeclarationLocalReferenceType;
import ch.ech.xmlns.ech_0010._3.AddressInformationType;
import ch.ech.xmlns.ech_0044._1.DatePartiallyKnownType;

/**
 * <H1>Description</H1>
 * 
 * @author SBR, SCO, VCH
 */
public class AFProcessAvisSedexWriterNew implements JadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public class ExemplePasCommitter {
        public void doIt() {

            // set header...
            // HeaderType h = of2001.createHeaderType();
            // m.setHeader(h);
            // h.setMessageType(2010);

        }
    }

    private static final String BLANCK = "";;

    private static HashMap<String, String> caisseCanton;

    private final static String DEFAULT_FAO_COMPENSATION_OFFICE = "000";

    private final static String DEFAULT_OASI_COMPENSATION_OFFICE = "000";
    private static final String DESCRIPTION = "Avis de mutation via SEDEX";
    private static final String DONNEE_INCONNUE = "inconnu";
    public static final String GENRE_CAISSE_AF = "509030";

    public static final String GENRE_CAISSE_PROF = "509028";
    public static final String IMPR_CHANGEMENT = "1";

    public static final String IMPR_NOUVELLE_AFFILIATION = "2";
    public static final String IMPR_SORTIE = "4";
    public static final String PAYS = "CH";
    private static final String TYPE_EXPLOITATION = "508021";

    // private TIAdresseDataSource dsAdresse = null;
    // private TIAdresseDataSource dsAdresseExploitation = null;

    static {
        AFProcessAvisSedexWriterNew.caisseCanton = new HashMap<String, String>();
        AFProcessAvisSedexWriterNew.caisseCanton.put("505001", "001"); // CS_ZURICH
        AFProcessAvisSedexWriterNew.caisseCanton.put("505002", "002"); // CS_BERNE
        AFProcessAvisSedexWriterNew.caisseCanton.put("505003", "003"); // CS_LUCERNE
        AFProcessAvisSedexWriterNew.caisseCanton.put("505004", "004"); // CS_URI
        AFProcessAvisSedexWriterNew.caisseCanton.put("505005", "005"); // CS_SCHWYZ
        AFProcessAvisSedexWriterNew.caisseCanton.put("505006", "006"); // CS_OBWALD
        AFProcessAvisSedexWriterNew.caisseCanton.put("505007", "007"); // CS_NIDWALD
        AFProcessAvisSedexWriterNew.caisseCanton.put("505008", "008"); // CS_GLARIS
        AFProcessAvisSedexWriterNew.caisseCanton.put("505009", "009"); // CS_ZOUG
        AFProcessAvisSedexWriterNew.caisseCanton.put("505010", "010"); // CS_FRIBOURG
        AFProcessAvisSedexWriterNew.caisseCanton.put("505011", "011"); // CS_SOLEURE
        AFProcessAvisSedexWriterNew.caisseCanton.put("505012", "012"); // CS_BALE_VILLE
        AFProcessAvisSedexWriterNew.caisseCanton.put("505013", "013"); // CS_BALE_CAMPAGNE
        AFProcessAvisSedexWriterNew.caisseCanton.put("505014", "014"); // CS_SCHAFFOUSE
        AFProcessAvisSedexWriterNew.caisseCanton.put("505015", "015"); // CS_APPENZELL_AR
        AFProcessAvisSedexWriterNew.caisseCanton.put("505016", "016"); // CS_APPENZELL_AI
        AFProcessAvisSedexWriterNew.caisseCanton.put("505017", "017"); // CS_SAINT_GALL
        AFProcessAvisSedexWriterNew.caisseCanton.put("505018", "018"); // CS_GRISONS
        AFProcessAvisSedexWriterNew.caisseCanton.put("505019", "019"); // CS_ARGOVIE
        AFProcessAvisSedexWriterNew.caisseCanton.put("505020", "020"); // CS_THURGOVIE
        AFProcessAvisSedexWriterNew.caisseCanton.put("505021", "021"); // CS_TESSIN
        AFProcessAvisSedexWriterNew.caisseCanton.put("505022", "022"); // CS_VAUD
        AFProcessAvisSedexWriterNew.caisseCanton.put("505023", "023"); // CS_VALAIS
        AFProcessAvisSedexWriterNew.caisseCanton.put("505024", "024"); // CS_NEUCHATEL
        AFProcessAvisSedexWriterNew.caisseCanton.put("505025", "025"); // CS_GENEVE
        AFProcessAvisSedexWriterNew.caisseCanton.put("505026", "150"); // CS_JURA
        System.out.println("Version tests SEDEX 1.05");
    }

    /**
     * Convertit un numéro de téléphone suisse en format tout numérique.
     * 
     * @param phoneNumber
     *            le numéro de téléphone à convertir
     * @return le numéro de téléphone en format numérique
     */
    private static String swissPhoneNumberToNumeric(String phoneNumber) {
        if (JadeStringUtil.isBlank(phoneNumber)) {
            return "";
        }
        phoneNumber = JadeStringUtil.stripBlanks(phoneNumber);
        int startIdx = 0;
        StringBuffer buf = new StringBuffer();
        if (JadeStringUtil.startsWith(phoneNumber, "++41")) {
            buf.append("0");
            startIdx += 4;
        } else if (JadeStringUtil.startsWith(phoneNumber, "+41")) {
            buf.append("0");
            startIdx += 3;
        } else if (JadeStringUtil.startsWith(phoneNumber, "0041")) {
            buf.append("0");
            startIdx += 4;
        }
        int length = phoneNumber.length();
        for (int i = startIdx; i < length; i++) {
            char c = phoneNumber.charAt(i);
            if (Character.isDigit(c)) {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    private boolean adresseChange;

    private List<AFAffiliation> avis = null;

    private boolean avsChange;
    private String caisseAF = AFProcessAvisSedexWriterNew.BLANCK;
    private String caisseAFFromAdhesion = AFProcessAvisSedexWriterNew.BLANCK;
    private String caisseAVS = AFProcessAvisSedexWriterNew.BLANCK;
    private boolean cantonChange;
    private AFAffiliation currentAffiliation;
    // Date et numero Caisse AF (idAffiliationAFControle est la pour eviter de
    // lancer plusieurs fois le code)
    private String dateAF = AFProcessAvisSedexWriterNew.BLANCK;
    private String dateAvis = AFProcessAvisSedexWriterNew.BLANCK;
    // dateAF et caisseAF
    // corresponde bien a
    // idAffiliationAFControle
    // Date et numero Caisse AVS (idAffiliationAVSControle est la pour eviter de
    // lancer plusieurs fois le code)
    private String dateAVS = AFProcessAvisSedexWriterNew.BLANCK;
    private String eMailAddress = AFProcessAvisSedexWriterNew.BLANCK;
    private String idAffiliation = null;
    private String idAffiliationAFControle = AFProcessAvisSedexWriterNew.BLANCK; // Pour controller que la
    private String idAffiliationAVSControle = AFProcessAvisSedexWriterNew.BLANCK; // Pour controller que la
    // dateAVS et caisseAvs
    // corresponde bien a
    // idAffiliationAVSControle
    private String idTiers = null;

    private AFAffiliation maisonMere = null;
    private AFAvisMotif motif = null;
    private boolean nomChange;
    private String observations = AFProcessAvisSedexWriterNew.BLANCK;
    ObjectFactory of2010 = new ObjectFactory();
    ch.eahv_iv.xmlns.eahv_iv_common._1.ObjectFactory ofCommon = new ch.eahv_iv.xmlns.eahv_iv_common._1.ObjectFactory();
    ch.ech.xmlns.ech_0010._3.ObjectFactory ofEch10 = new ch.ech.xmlns.ech_0010._3.ObjectFactory();
    ch.ech.xmlns.ech_0044._1.ObjectFactory ofEch44 = new ch.ech.xmlns.ech_0044._1.ObjectFactory();
    ch.ech.xmlns.ech_0058._2.ObjectFactory ofEch58 = new ch.ech.xmlns.ech_0058._2.ObjectFactory();
    private boolean pmChange;
    private String selectionImpression = null;
    // private TIAvoirAdresse adresseDomicile;
    private BSession session = null;

    private boolean siegeChange;
    private TITiersViewBean tiers = null;

    // Constructeurs

    public AFProcessAvisSedexWriterNew() {
        // vide
    }

    /**
     * Constructeur de AFProcessAvisSedexWriterNew
     */
    public AFProcessAvisSedexWriterNew(BSession session, String selectionImpression) {
        this.session = session;
        this.selectionImpression = selectionImpression;
        setDateAvis(JACalendar.todayJJsMMsAAAA());
        setAvis(new ArrayList<AFAffiliation>());
    }

    private void cleanup() {
        motif = null;
    }

    /**
     * Création du contenu
     * 
     * @param message101
     */
    private void createContent(Message message101) throws Exception {
        ContentType content = of2010.createContentType();
        message101.setContent(content);
        // Chargement de la caisse AF depuis l'adhésion
        loadCaisseFromAdhesion();
        // Récupération des détails de l'adresse
        AFAvisMotif avisMotif = getReason();
        AFAvisAddressFactory adresseFactory = AFAvisAddressFactory.createInstance(getDateAvis(), avisMotif,
                currentAffiliation, tiers);
        // Adresse postale
        AFAvisAddress postalAddress = adresseFactory.createPostalAddress();
        // Mutation
        MutationType mutation = of2010.createMutationType();
        content.getMutation().add(mutation);

        mutation.setReason(new BigInteger(avisMotif.toString()));
        mutation.setAccountNumber(getAccountNumber());

        // BZ 8465, le code a été commenté afin de rendre le fichier xml généré lisible pour sedex même si
        // un numéro fédéral d'entreprise a été renseigné. La structure XML pour cette partie devra être revu
        // pour une version ultérieure.

        // if (!JadeStringUtil.isBlankOrZero(this.getOtherOrganisationID())) {
        //
        // NamedOrganisationIdType otherOrgId = this.ofCommon.createNamedOrganisationIdType();
        // otherOrgId.setOrganisationId(this.getOtherOrganisationID());
        // mutation.getOtherOrganisationId().add(otherOrgId);
        // }

        mutation.setLegalForm(getLegalForm());
        if (!JadeStringUtil.isBlankOrZero(getTitle())) {
            mutation.setTitle(new BigInteger(getTitle().toString()));
        }
        if (!JadeStringUtil.isBlankOrZero(getVn())) {
            mutation.setVn(new Long(getVn().toString()));
        }
        if (!JadeStringUtil.isBlankOrZero(getDateOfBirth())) {
            DatePartiallyKnownType date = ofEch44.createDatePartiallyKnownType();
            mutation.setDateOfBirth(date);
            date.setYearMonthDay(JAXBUtil.getXmlCalendarDate(getDateOfBirth()));
        }
        if (!JadeStringUtil.isBlankOrZero(getSex())) {
            mutation.setSex(getSex());
        }
        String persJuridiqueCode = getSession().getCode(currentAffiliation.getPersonnaliteJuridique());
        boolean persJuridiqueAB = CodeSystem.PERS_JURIDIQUE_CODE_A.equals(persJuridiqueCode)
                || CodeSystem.PERS_JURIDIQUE_CODE_B.equals(persJuridiqueCode);
        if (persJuridiqueAB) {
            mutation.setOfficialName(getTiers().getDesignation1());
            mutation.setFirstName(getTiers().getDesignation2());
        } else {
            // si affiliation en cours est IND ou NON ACTIF, alors organisation1/2 va dans nom/prénom
            if (CodeSystem.TYPE_AFFILI_INDEP.equals(getCurrentAffiliation().getTypeAffiliation())
                    || CodeSystem.TYPE_AFFILI_NON_ACTIF.equals(getCurrentAffiliation().getTypeAffiliation())) {
                if (!JadeStringUtil.isBlankOrZero(getOrganisationName1())) {
                    mutation.setOfficialName(getOrganisationName1());
                }

                if (!JadeStringUtil.isBlankOrZero(getOrganisationName2())) {
                    mutation.setFirstName(getOrganisationName2());
                }
            } else {
                if (!JadeStringUtil.isBlankOrZero(getOfficialName())) {
                    mutation.setOfficialName(getOfficialName());
                }
                if (!JadeStringUtil.isBlankOrZero(getFirstName())) {
                    mutation.setFirstName(getFirstName());
                }
                if (!JadeStringUtil.isBlankOrZero(getOrganisationName1())) {
                    mutation.setOrganisationName1(getOrganisationName1());
                }
                if (!JadeStringUtil.isBlankOrZero(getOrganisationName2())) {
                    mutation.setOrganisationName2(getOrganisationName2());
                }
            }
        }
        // fin nom/prénom
        if (!JadeStringUtil.isBlankOrZero(getKindOfActivity())) {
            mutation.setKindOfActivity(getKindOfActivity());
        }
        if (postalAddress != null) {

            // adresse
            AddressInformationType address = ofEch10.createAddressInformationType();
            mutation.setAddress(address);
            //
            address.setStreet(postalAddress.getStreet());
            // mutation.getAddress().setStreet();
            address.setHouseNumber(postalAddress.getHouseNumber());
            address.setTown(postalAddress.getTown());
            address.setSwissZipCode(new Long(postalAddress.getSwissZipCode()));
            address.setCountry(AFProcessAvisSedexWriterNew.PAYS);
        }
        mutation.setDateOfValidity(JAXBUtil.getXmlCalendarDate(getDateAvis()));

        if (!JadeStringUtil.isBlankOrZero(getDateAVS(getCurrentAffiliation().getAffiliationId()))
                && !AFProcessAvisSedexWriterNew.DONNEE_INCONNUE.equalsIgnoreCase(getDateAVS(getCurrentAffiliation()
                        .getAffiliationId()))) {
            mutation.setOasiDateOfAcceptance(JAXBUtil.getXmlCalendarDate(getDateAVS(getCurrentAffiliation()
                    .getAffiliationId())));
        }

        if (!JadeStringUtil.isBlankOrZero(getCurrentAffiliation().getDateFin())) {
            mutation.setOasiDateOfDeletion(JAXBUtil.getXmlCalendarDate(getCurrentAffiliation().getDateFin()));
        }
        // if (!JadeStringUtil.isBlankOrZero(this.session.getApplication().getProperty(("noCaisse"), null))) {
        // mutation.setOasiCompensationOffice(new BigInteger(this.session.getApplication().getProperty(("noCaisse"),
        // null)));
        // }
        // Caisse AVS, si vide alors on met 000
        String caisseAVS = getOasiCompensationOffice();
        if (!JadeStringUtil.isBlankOrZero(caisseAVS)
                && !AFProcessAvisSedexWriterNew.DONNEE_INCONNUE.equalsIgnoreCase(caisseAVS)) {
            mutation.setOasiCompensationOffice(new BigInteger(caisseAVS));
        } else {
            mutation.setOasiCompensationOffice(new BigInteger(
                    AFProcessAvisSedexWriterNew.DEFAULT_OASI_COMPENSATION_OFFICE));
        }

        if (!JadeStringUtil.isBlankOrZero(getCommunication()) && isValueBetween(avisMotif.codeMotifInt(), 30, 39)) {
            mutation.setOasiCommunication(CommunicationCodeType.fromValue(getCommunication()));
        }
        boolean authorizedToSetCaisseAFFromAdhesion = false;
        if (!JadeStringUtil.isBlankOrZero(getFaoDateOfAcceptance())
                && !AFProcessAvisSedexWriterNew.DONNEE_INCONNUE.equalsIgnoreCase(getFaoDateOfAcceptance())) {
            mutation.setFaoDateOfAcceptance(JAXBUtil.getXmlCalendarDate(getFaoDateOfAcceptance()));
            authorizedToSetCaisseAFFromAdhesion = true;
        }

        if (!JadeStringUtil.isBlankOrZero(getCurrentAffiliation().getDateFin())) {
            mutation.setFaoDateOfDeletion(JAXBUtil.getXmlCalendarDate(getCurrentAffiliation().getDateFin()));
        }
        // Caisse AF. Au final, si vide alors on met 000
        String faoCompensationOffice = null;
        // Caisse AF présent dans le suivi des caisses ?
        if (!JadeStringUtil.isBlankOrZero(getFaoCompensationOffice())
                && !AFProcessAvisSedexWriterNew.DONNEE_INCONNUE.equalsIgnoreCase(getFaoCompensationOffice())) {
            faoCompensationOffice = getFaoCompensationOffice();
            // Caisse AF ou Caisse prof présentes dans l'adhésion ?
            // S'il existe une caisse AF et une Caisse prof, c'est la caisse AF qui est retournée
        } else if (!JadeStringUtil.isBlankOrZero(getCaisseAFFromAdhesion()) && authorizedToSetCaisseAFFromAdhesion) {
            faoCompensationOffice = getCaisseAFFromAdhesion();
        } else {
            faoCompensationOffice = AFProcessAvisSedexWriterNew.DEFAULT_FAO_COMPENSATION_OFFICE;
        }

        mutation.setFaoCompensationOffice(new BigInteger(faoCompensationOffice.toString()));

        //
        if (!JadeStringUtil.isBlankOrZero(getCommunication()) && isValueBetween(avisMotif.codeMotifInt(), 30, 39)) {
            mutation.setFaoCommunication(CommunicationCodeType.fromValue(getCommunication()));
        }
        // Adresse de renvoi
        AFAvisAddress returnAdress = adresseFactory.createReturnAddress(maisonMere);
        String nameReturnAddress1 = returnAdress.getName1();
        if (!JadeStringUtil.isBlankOrZero(nameReturnAddress1)) {
            mutation.setNameReturnAddress1(nameReturnAddress1);
        }
        String nameReturnAddress2 = returnAdress.getName2();
        if (!JadeStringUtil.isBlankOrZero(nameReturnAddress2)) {
            mutation.setNameReturnAddress2(nameReturnAddress2);
        }
        String nameReturnAddress3 = returnAdress.getName3();
        if (!JadeStringUtil.isBlankOrZero(nameReturnAddress3)) {
            mutation.setNameReturnAddress3(nameReturnAddress3);
        }

        // return adresse

        if (!JadeStringUtil.isBlankOrZero(returnAdress.getTown())) {
            AddressInformationType returnAddress1 = ofEch10.createAddressInformationType();
            mutation.setReturnAddress(returnAddress1);

            String street = returnAdress.getStreet();
            if (!JadeStringUtil.isBlankOrZero(street)) {
                returnAddress1.setStreet(street);
            }
            String houseNumber = returnAdress.getHouseNumber();
            if (!JadeStringUtil.isBlankOrZero(houseNumber)) {
                returnAddress1.setHouseNumber(houseNumber);
            }
            String town = returnAdress.getTown();
            if (!JadeStringUtil.isBlankOrZero(town)) {
                returnAddress1.setTown(town);
            }
            String swissZipCode = returnAdress.getSwissZipCode();
            if (!JadeStringUtil.isBlankOrZero(swissZipCode)) {
                returnAddress1.setSwissZipCode(Long.valueOf(swissZipCode));
            }
            String country = returnAdress.getCountry();
            if (!JadeStringUtil.isBlankOrZero(country)) {
                returnAddress1.setCountry(country);
            }
        }
        if (!JadeStringUtil.isBlankOrZero(getAdditionalObservations())) {
            mutation.setAdditionalObservations(getAdditionalObservations());
        }
    }

    /**
     * Création du header
     * 
     * @param message101
     */
    private void createHeader(Message message101) throws Exception {
        JadeSedexDirectory sedexDirectory = JadeSedexService.getInstance().getSedexDirectory();
        HeaderType header = of2010.createHeaderType();
        message101.setHeader(header);
        // header.setTestDeliveryFlag(JadeSedexService.getInstance().getTestDeliveryFlag());
        DeclarationLocalReferenceType declarationLocalReference = ofCommon.createDeclarationLocalReferenceType();
        header.setDeclarationLocalReference(declarationLocalReference);
        String senderId = sedexDirectory.getLocalEntry().getId();
        header.setSenderId(senderId);
        // DECLARATIONLOCALREFERENCEUtils.computeSedexId
        declarationLocalReference.setName(getSession().getUserFullName());
        declarationLocalReference.setDepartment(getSession().getUserInfo().getDepartment());
        String thePhone = getSession().getUserInfo().getPhone();
        if (!JadeStringUtil.isBlankOrZero(thePhone)) {
            declarationLocalReference.setPhone(AFProcessAvisSedexWriterNew.swissPhoneNumberToNumeric(thePhone));
        } else {
            declarationLocalReference.setPhone("0000000000");
        }
        declarationLocalReference.setEmail(getSession().getUserEMail());

        // Recipient Id
        TIAvoirAdresse adresse = findAdresseSender(getCurrentAffiliation(), false);
        TILocalite localite = getLocalite(adresse);
        String canton = localite.getIdCanton();
        String noCaisse = AFProcessAvisSedexWriterNew.caisseCanton.get(canton);
        String recipientSedexId = Utils.computeSedexId(noCaisse, null);
        Entry recipient = sedexDirectory.getEntryWithId(recipientSedexId);
        if (recipient == null) {
            throw new Exception("Impossible d'envoyer l'avis de mutation: la caisse '" + noCaisse + "' (idSedex: "
                    + recipientSedexId + ") n'existe pas dans le repository SEDEX");
        }
        String recipientId = recipient.getId();
        if ((senderId != null) && senderId.toUpperCase().startsWith("T")) {
            recipientId = "T" + recipientId;
        }
        header.getRecipientId().add(recipientId);
        // setRecipientId().setValue();
        // id sedex test 10.17.1.106
        // HACK: forçage du sedex ID si CCVD -> inforom
        /*
         * if ("022".equals(noCaisse)) { message101.getHeader().getRecipientId().setValue("6-900004-1"); }
         */
        // Message Id
        header.setMessageId(AFProcessAvisSedexWriterNew.BLANCK + JadeUUIDGenerator.createLongUID());
        // Message Type
        header.setOurBusinessReferenceID(getCurrentAffiliation().getAffilieNumero() + "-01");
        // Message Type
        header.setMessageType(2010);
        // Sub Message Type
        header.setSubMessageType("000101");
        // Sending Application
        // Manufacturer

        ch.ech.xmlns.ech_0058._2.SendingApplicationType sendingApplication = ofEch58.createSendingApplicationType();
        header.setSendingApplication(sendingApplication);

        sendingApplication.setManufacturer("GLOBAZ SA");
        // Product
        sendingApplication.setProduct("Web@AVS");
        // Product Version
        sendingApplication.setProductVersion("1.8.00");
        // Subject
        header.setSubject("Mutation - " + getReason() + " " + getOfficialName() + getOrganisationName1());
        header.setMessageDate(JAXBUtil.getXmlCalendarTimestamp(JadeDateUtil.getGlobazDate(getDateAvis())));
        header.setEventDate(JAXBUtil.getXmlCalendarTimestamp(JadeDateUtil.getGlobazDate(getDateAvis())));
        header.setAction("1");
        header.setTestDeliveryFlag(new Boolean(JadeSedexService.getInstance().getTestDeliveryFlag()));

    }

    private TIAvoirAdresse findAdresse(TIAvoirAdresse adresse) {
        // adresse = adresse.findCurrentRelation();
        return adresse.findCurrentRelation();
        /*
         * if(adresse!=null) { return adresse.findPreviousRelation(); } else { return null; }
         */
    }

    private TIAvoirAdresse findAdresseSender(AFAffiliation affToUse, boolean saveDomicile) {
        // rechercher l'adresse de l'affilie
        TIAvoirAdresse adresseCriteres = new TIAvoirAdresse();
        adresseCriteres.setIdTiers(affToUse.getIdTiers());
        adresseCriteres.setSession(affToUse.getSession());
        adresseCriteres.setIdExterne(affToUse.getAffilieNumero());
        adresseCriteres.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
        // recherche adresse d'exploitation
        adresseCriteres.setTypeAdresse(AFProcessAvisSedexWriterNew.TYPE_EXPLOITATION);
        TIAvoirAdresse domicile;
        TIAvoirAdresse principale = findAdresse(adresseCriteres);
        if (principale == null) {
            // recherche sans no d'affilié
            adresseCriteres.setIdExterne("");
            principale = findAdresse(adresseCriteres);
            if (principale == null) {
                // recherche adresse courrier
                adresseCriteres.setIdApplication(ICommonConstantes.CS_APPLICATION_COTISATION);
                adresseCriteres.setTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER);
                adresseCriteres.setIdExterne(affToUse.getAffilieNumero());
                principale = findAdresse(adresseCriteres);
                if (principale == null) {
                    // recherche adresse courrier sans no aff.
                    adresseCriteres.setIdExterne("");
                    principale = findAdresse(adresseCriteres);
                }
            }

            if (principale == null) {
                // recherche adresse courrier DEFAUT
                adresseCriteres.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
                adresseCriteres.setTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_COURRIER);
                adresseCriteres.setIdExterne(affToUse.getAffilieNumero());
                principale = findAdresse(adresseCriteres);
                if (principale == null) {
                    // recherche adresse courrier sans no aff.
                    adresseCriteres.setIdExterne("");
                    principale = findAdresse(adresseCriteres);
                }
            }
        }

        // recherche domicile
        adresseCriteres.setIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
        adresseCriteres.setTypeAdresse(IConstantes.CS_AVOIR_ADRESSE_DOMICILE);
        adresseCriteres.setIdExterne(affToUse.getAffilieNumero());
        domicile = findAdresse(adresseCriteres);
        if (domicile == null) {
            // recherche dom sans no d'affilié
            adresseCriteres.setIdExterne("");
            domicile = findAdresse(adresseCriteres);
        }

        if (CodeSystem.TYPE_AFFILI_EMPLOY.equals(affToUse.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_EMPLOY_D_F.equals(affToUse.getTypeAffiliation())
                || CodeSystem.TYPE_AFFILI_LTN.equals(affToUse.getTypeAffiliation())) {
            // si employeur, utiliser domicile si existante, principale sinon
            if (domicile != null) {
                return domicile;
            } else {
                return principale;
            }
        } else {
            // pour IND, NA, etc, utiliser principale, domicile sinon
            if (principale == null) {
                return domicile;
            } else {
                if (saveDomicile) {
                    // sauver adresse domicile (privée)
                    // heu, on ne lit jamais cette variable?
                    // adresseDomicile = domicile;
                }
                return principale;
            }
        }
    }

    /**
     * Récupération du numéro d'affilié de la caisse émetrice
     * 
     * @return
     */
    private String getAccountNumber() {
        return getCurrentAffiliation().getAffilieNumero();
    }

    private String getAdditionalObservations() {
        StringBuffer observationTemp = new StringBuffer();
        try {
            AFAffiliation snc = AFAffiliationUtil.isAssocie(getCurrentAffiliation(), dateAvis);
            if (snc != null) {
                observationTemp.append(getSession().getLabel("AVIS_OFAS_SNC") + ":\n" + snc.getRaisonSocialeCourt());
                observationTemp.append("\n");
            }
        } catch (Exception e) {
            JadeLogger.error(this, AFProcessAvisSedexWriterNew.class.getName() + " - "
                    + "Problème dans la récupération de l'affiliation snc");
        }
        if (nomChange) {
            observationTemp.append(getSession().getLabel("CHANGEMENT_NOM"));
            observationTemp.append("\n");
        }
        if (adresseChange) {
            observationTemp.append(getSession().getLabel("CHANGEMENT_ADRESSE"));
            observationTemp.append("\n");
        }
        if (cantonChange) {
            observationTemp.append(getSession().getLabel("CHANGEMENT_CANTON"));
            observationTemp.append("\n");
        }
        if (pmChange) {
            observationTemp.append(getSession().getLabel("CHANGEMENT_PM"));
            observationTemp.append("\n");
        }
        if (siegeChange) {
            observationTemp.append(getSession().getLabel("CHANGEMENT_SIEGE"));
            observationTemp.append("\n");
        }
        if (avsChange) {
            observationTemp.append(getSession().getLabel("CHANGEMENT_NUMERO_AVS"));
            observationTemp.append("\n");
        }
        if (!JadeStringUtil.isEmpty(getObservations())) {
            observationTemp.append(getObservations());
        }
        return observationTemp.toString();
    }

    public boolean getAdresseChange() {
        return adresseChange;
    }

    public List<AFAffiliation> getAvis() {
        return avis;
    }

    public boolean getAvsChange() {
        return avsChange;
    }

    public String getCaisseAF(String _idAffiliation) {
        if (!getIdAffiliationAFControle().equals(_idAffiliation)) {
            loadCaisseAF();
        }
        return caisseAF;
    }

    public String getCaisseAFFromAdhesion() {
        return caisseAFFromAdhesion;
    }

    public String getCaisseAVS(String _idAffiliation) {
        if (!getIdAffiliationAVSControle().equals(_idAffiliation)) {
            loadCaisseAVS();
        }
        return caisseAVS;
    }

    public boolean getCantonChange() {
        return cantonChange;
    }

    private String getCommunication() {
        String valeursAutorisees = "DEFHKNPQRTX";
        String aValider = getCurrentAffiliation().getSession().getCode(getCurrentAffiliation().getMotifFin());
        if ((aValider != null) && (aValider.length() == 1) && (valeursAutorisees.indexOf(aValider) > -1)) {
            return aValider;
        } else {
            return "X";
        }
    }

    private AFAffiliation getCurrentAffiliation() {
        if (currentAffiliation == null) {
            loadData();
        }
        return currentAffiliation;
    }

    public String getDateAF(String _idAffiliation) {
        if (!getIdAffiliationAFControle().equals(_idAffiliation)) {
            loadCaisseAF();
        }
        return dateAF;
    }

    public String getDateAvis() {
        return dateAvis;
    }

    public String getDateAVS(String _idAffiliation) {
        if (!getIdAffiliationAVSControle().equals(_idAffiliation)) {
            loadCaisseAVS();
        }
        return dateAVS;
    }

    /**
     * Récupération de la date de naissance du tiers
     * 
     * @return
     */
    private String getDateOfBirth() {
        return getTiers().getDateNaissance();
    }

    @Override
    public String getDescription() {
        return AFProcessAvisSedexWriterNew.DESCRIPTION;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * Récupération du numero de la caisse AF dans le canton
     * 
     * @return
     */
    private String getFaoCompensationOffice() {
        return getCaisseAF(getCurrentAffiliation().getAffiliationId());
    }

    /**
     * Récupération de la date d'affiliation à la caisse AF
     * 
     * @return
     */
    private String getFaoDateOfAcceptance() {
        return getDateAF(getCurrentAffiliation().getAffiliationId());
    }

    /**
     * Récupération du prénom du tiers
     * 
     * @return
     */
    private String getFirstName() {
        if (getTiers().getPersonnePhysique().booleanValue()) {
            return getTiers().getDesignation2();
        }
        return AFProcessAvisSedexWriterNew.BLANCK;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdAffiliationAFControle() {
        return idAffiliationAFControle;
    }

    public String getIdAffiliationAVSControle() {
        return idAffiliationAVSControle;
    }

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * Récupération de la date a partir de laquelle la modification est valable
     * 
     * @return
     */
    /*
     * private String getDateOfValidity() { return getDateAvis(); }
     */
    private String getKindOfActivity() {
        if (getTiers().getPersonneMorale().booleanValue()) {
            return getTiers().getDesignation3();
        }
        return AFProcessAvisSedexWriterNew.BLANCK;
    }

    /**
     * Récupération de la date d'affiliation à l'AVS
     * 
     * @return
     */
    /*
     * private String getOasiDateOfAcceptance() { return getDateAVS(getCurrentAffiliation().getDateDebut()); }
     */
    /**
     * Récupération de la forme juridique
     * 
     * @return
     */
    private String getLegalForm() {
        return getSession().getCode(getCurrentAffiliation().getPersonnaliteJuridique());
    }

    private TILocalite getLocalite(TIAvoirAdresse adresse) throws Exception {
        TILocalite localite = new TILocalite();
        localite.setSession(getSession());
        if (adresse != null) {
            localite.setIdLocalite(adresse.getIdLocalite());
            localite.retrieve();
        }
        return localite;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public boolean getNomChange() {
        return nomChange;
    }

    /**
     * Récupération du numero de la caisse AVS
     * 
     * @return
     */

    private String getOasiCompensationOffice() {
        return getCaisseAVS(getCurrentAffiliation().getAffiliationId());
    }

    public String getObservations() {
        return observations;
    }

    /**
     * Récupération du nom du tiers
     * 
     * @return
     */
    private String getOfficialName() {
        if (getTiers().getPersonnePhysique().booleanValue()) {
            return getTiers().getDesignation1();
        }
        return AFProcessAvisSedexWriterNew.BLANCK;
    }

    /**
     * @return : la désignation de l'organisation
     */
    private String getOrganisationName1() {
        if (getTiers().getPersonneMorale().booleanValue()) {
            return getTiers().getDesignation1();
        }
        return AFProcessAvisSedexWriterNew.BLANCK;
    }

    /**
     * @return : le complément de la désignation de l'organisation
     */
    private String getOrganisationName2() {
        if (getTiers().getPersonneMorale().booleanValue()) {
            return getTiers().getDesignation2();
        }
        return AFProcessAvisSedexWriterNew.BLANCK;
    }

    /**
     * @return : le numéro fédéral de l'entreprise (Numéro REE)
     */
    private String getOtherOrganisationID() {
        return getCurrentAffiliation().getNumeroIDE();
    }

    public boolean getPmChange() {
        return pmChange;
    }

    @Override
    public float getProgress() {
        return 0;
    }

    /**
     * Récupération du motif de l'avis
     * 
     * @return+
     */
    private AFAvisMotif getReason() {
        if (motif == null) {
            try {
                maisonMere = AFAffiliationUtil.isSuccursale(currentAffiliation, dateAvis);
                if (selectionImpression != null) {
                    boolean associeCommanditaire = AFAffiliationUtil.isAssocieCommanditaire(currentAffiliation);
                    boolean doDelete = AFProcessAvisSedexWriterNew.IMPR_SORTIE.equals(selectionImpression)
                            || (AFProcessAvisSedexWriterNew.IMPR_CHANGEMENT.equals(selectionImpression) && cantonChange);
                    if (doDelete) {
                        // dans le cas d'un changement de canton, on fait un document pour chaque canton:
                        // d'abord un 31, 32 ou 33 pour l'ancienne relation
                        if (associeCommanditaire) {
                            motif = AFAvisMotif.DELETE_ASSOCIE;
                        } else if (maisonMere != null) {
                            motif = AFAvisMotif.DELETE_SUCCURSALE;
                        } else {
                            motif = AFAvisMotif.DELETE_RI_SOC;
                        }
                    } else if (selectionImpression.equals(AFProcessAvisSedexWriterNew.IMPR_NOUVELLE_AFFILIATION)) {
                        if (associeCommanditaire) {
                            motif = AFAvisMotif.CREATE_ASSOCIE;
                        } else if (maisonMere != null) {
                            motif = AFAvisMotif.CREATE_SUCCURSALE;
                        } else {
                            motif = AFAvisMotif.CREATE_RI_SOC;
                        }

                    } else if (selectionImpression.equals(AFProcessAvisSedexWriterNew.IMPR_CHANGEMENT) && !cantonChange) {
                        // dans le cas d'un changement d'adresse ou de nom ou de no avs on met les infos nouvelles a
                        // droite dans
                        // un 20 et les infos anciennes a gauche dans un 21, 22 ou 23
                        if (associeCommanditaire) {
                            motif = AFAvisMotif.UPDATE_ASSOCIE;
                        } else if (maisonMere != null) {
                            motif = AFAvisMotif.UPDATE_SUCCURSALE;
                        } else {
                            motif = AFAvisMotif.UPDATE_RI_SOC;
                        }
                    } else {
                        motif = AFAvisMotif.MOTIF_55;
                    }
                } else {
                    motif = AFAvisMotif.CREATE_RI_SOC;
                }
            } catch (Exception e) {
                motif = AFAvisMotif.MOTIF_55;
            }
        }
        return motif;
    }

    public String getSelectionImpression() {
        return selectionImpression;
    }

    @Override
    public BSession getSession() {
        return session;
    }

    /**
     * Récupération du sexe du tiers
     * 
     * @return
     */
    private String getSex() {
        if (ITITiers.CS_MONSIEUR.equals(currentAffiliation.getTiers().getTitreTiers())) {
            return "1";
        } else if (ITITiers.CS_MADAME.equals(currentAffiliation.getTiers().getTitreTiers())) {
            return "2";
        } else {
            return "0";
        }
    }

    public boolean getSiegeChange() {
        return siegeChange;
    }

    public TITiersViewBean getTiers() {
        if (tiers == null) {
            loadTiers();
        }
        return tiers;
    }

    /**
     * Récupération du titre du tiers
     * 
     * @return
     */
    private String getTitle() {
        if (ITITiers.CS_MONSIEUR.equals(currentAffiliation.getTiers().getTitreTiers())) {
            return "1";
        } else if (ITITiers.CS_MADAME.equals(currentAffiliation.getTiers().getTitreTiers())) {
            return "2";
        } else {
            return "0";
        }
    }

    /**
     * Récupération du numero NSS
     * 
     * @return
     * @throws Exception
     */
    private String getVn() throws Exception {
        IFormatData nssFormater = new CommonNSSFormater();
        return nssFormater.unformat(getCurrentAffiliation().getTiers().getNumAvsActuel());
    }

    private boolean hasPlanCaisseCotiAF(String idPlanCaisse) throws Exception {

        if (JadeStringUtil.isBlankOrZero(idPlanCaisse)) {
            throw new Exception("can't check if plan caisse has a coti AF because idPlanCaisse is blank or zero ");
        }

        AFCotisationManager cotisMgr = new AFCotisationManager();
        cotisMgr.setSession(getSession());
        cotisMgr.setForPlanCaisseId(idPlanCaisse);
        cotisMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AF);

        return cotisMgr.getCount() >= 1;
    }

    /**
     * Indique si la valeur est comprise entre borneMin et borneMax (bornes comprises).
     */
    private boolean isValueBetween(int value, int borneMin, int borneMax) {
        return ((value >= borneMin) && (value <= borneMax));
    }

    @Override
    public String jobQueueName() {
        return null;
    }

    /**
     * Chargement des données de la caisse AF de l'affilié
     */
    protected void loadCaisseAF() {
        String _dateAF = AFProcessAvisSedexWriterNew.BLANCK;
        String dateAFpers = AFProcessAvisSedexWriterNew.BLANCK;
        String _caisseAF = AFProcessAvisSedexWriterNew.BLANCK;

        try {
            _dateAF = AFAffiliationUtil.getDateDebutCotisationsAF(getCurrentAffiliation(), dateAvis,
                    CodeSystem.GENRE_ASS_PARITAIRE);
            dateAFpers = AFAffiliationUtil.getDateDebutCotisationsAF(getCurrentAffiliation(), dateAvis,
                    CodeSystem.GENRE_ASS_PERSONNEL);
            if ((_dateAF == null)
                    || ((dateAFpers != null) && BSessionUtil.compareDateFirstLower(getSession(), dateAFpers, _dateAF))) {
                // si pas d'AF par, utiliser AF pers ou si AF pers est plus
                // ancienne, utilisée cette dernière
                _dateAF = dateAFpers;
            }

            if (_dateAF == null) {
                // pas d'AF, rechercher caisse externe
                AFSuiviCaisseAffiliation caisse = AFAffiliationUtil.getCaissseAF(getCurrentAffiliation(), dateAvis);
                if (caisse != null) {
                    // la caisse existe
                    _dateAF = AFProcessAvisSedexWriterNew.DONNEE_INCONNUE;
                    _caisseAF = AFProcessAvisSedexWriterNew.DONNEE_INCONNUE;
                    if (!JadeStringUtil.isIntegerEmpty(caisse.getDateDebut())) {
                        _dateAF = caisse.getDateDebut();
                    }
                    if (caisse.getAdministration() != null) {
                        _caisseAF = caisse.getAdministration().getCodeInstitution();
                    }
                } else {
                    _dateAF = AFProcessAvisSedexWriterNew.BLANCK;
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        } finally {
            setDateAF(_dateAF);
            setCaisseAF(_caisseAF);
            setIdAffiliationAFControle(getCurrentAffiliation().getAffiliationId());
        }

    }

    /**
     * Chargement des données de la caisse AVS de l'affilié
     */
    protected void loadCaisseAVS() {
        String _dateAVS = AFProcessAvisSedexWriterNew.BLANCK;
        String dateAVSpers = AFProcessAvisSedexWriterNew.BLANCK;
        String _caisseAVS = AFProcessAvisSedexWriterNew.BLANCK;
        try {
            // date AVS, recherche de la coti AVS paritaire (employeur, RI)
            _dateAVS = AFAffiliationUtil.getDateDebutCotisationsAVS(getCurrentAffiliation(), dateAvis,
                    CodeSystem.GENRE_ASS_PARITAIRE);
            dateAVSpers = AFAffiliationUtil.getDateDebutCotisationsAVS(getCurrentAffiliation(), dateAvis,
                    CodeSystem.GENRE_ASS_PERSONNEL);
            if ((_dateAVS == null)
                    || ((dateAVSpers != null) && BSessionUtil
                            .compareDateFirstLower(getSession(), dateAVSpers, _dateAVS))) {
                // si pas d'AVS par, utiliser AVS pers ou si AVS pers est plus
                // ancienne, utilisée cette dernière
                _dateAVS = dateAVSpers;
            }

            if (_dateAVS == null) {
                _dateAVS = AFProcessAvisSedexWriterNew.DONNEE_INCONNUE;
                // si taxé sous, prendre la date de début et de fin de
                // l'affiliation
                if (AFAffiliationUtil.isTaxeSous(getCurrentAffiliation(), dateAvis)) {
                    _dateAVS = getCurrentAffiliation().getDateDebut();
                } else {
                    _caisseAVS = AFProcessAvisSedexWriterNew.DONNEE_INCONNUE;
                    // pas d'AVS, rechercher caisse externe
                    AFSuiviCaisseAffiliation caisse = AFAffiliationUtil
                            .getCaissseAVS(getCurrentAffiliation(), dateAvis);
                    if (caisse != null) {
                        // la caisse existe
                        if (!JadeStringUtil.isIntegerEmpty(caisse.getDateDebut())) {
                            _dateAVS = caisse.getDateDebut();
                        }
                        if (caisse.getAdministration() != null) {
                            _caisseAVS = caisse.getAdministration().getCodeInstitution();
                        }
                    }
                }
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
        } finally {
            setDateAVS(_dateAVS);
            setCaisseAVS(_caisseAVS);
            setIdAffiliationAVSControle(getCurrentAffiliation().getAffiliationId());
        }
    }

    private void loadCaisseFromAdhesion() {
        try {
            AFAdhesionManager mgrAdhesion = new AFAdhesionManager();
            mgrAdhesion.setSession(getSession());
            mgrAdhesion.setForAffiliationId(getCurrentAffiliation().getAffiliationId());
            mgrAdhesion.find(BManager.SIZE_NOLIMIT);
            AFAdhesion adhesion = null;
            TIAdministrationViewBean vbAdministrationTier = null;

            for (int i = 1; i <= mgrAdhesion.size(); i++) {
                adhesion = (AFAdhesion) mgrAdhesion.getEntity(i - 1);
                if (adhesion != null) {
                    vbAdministrationTier = adhesion.getAdministrationCaisse();
                    if (vbAdministrationTier != null) {
                        if (AFProcessAvisSedexWriterNew.GENRE_CAISSE_AF.equalsIgnoreCase(vbAdministrationTier
                                .getGenreAdministration())) {
                            setCaisseAFFromAdhesion(vbAdministrationTier.getCodeInstitution());
                            break;
                        } else if (AFProcessAvisSedexWriterNew.GENRE_CAISSE_PROF.equalsIgnoreCase(vbAdministrationTier
                                .getGenreAdministration()) && hasPlanCaisseCotiAF(adhesion.getPlanCaisseId())) {
                            setCaisseAFFromAdhesion(vbAdministrationTier.getCodeInstitution());
                        }

                    }
                }
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    /**
     * Chargement des données
     */
    private void loadData() {
        avis.clear();
        System.out.println("AF Load data");
        try {
            AFAffiliationManager affiliationManager = new AFAffiliationManager();
            affiliationManager.setSession(session);
            affiliationManager.setForIdTiers(getIdTiers());
            affiliationManager.setForAffiliationId(getIdAffiliation());
            affiliationManager.find();

            System.out.println("Manager size: " + affiliationManager.getCount());

            for (int i = 0; i < affiliationManager.getSize(); i++) {
                AFAffiliation affiliation = (AFAffiliation) affiliationManager.getEntity(i);
                avis.add(affiliation);
            }
            System.out.println("Parcours manager terminé. " + avis.size() + " avis.");
            if (avis.size() > 0) {
                currentAffiliation = avis.get(0);
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    /**
     * Charge l'object tiers
     */
    protected void loadTiers() {
        // Si si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(currentAffiliation.getIdTiers())) {
            return;
        }
        // Si log pas déjà chargé
        if (tiers == null) {
            // Instancier un nouveau LOG
            tiers = new TITiersViewBean();
            tiers.setSession(getSession());

            // Récupérer le log en question
            tiers.setIdTiers(currentAffiliation.getIdTiers());

            try {
                tiers.retrieve();

                if (tiers.getSession().hasErrors()) {
                    tiers = null;
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                tiers = null;
            }
        }
    }

    // *******************************************************
    // Setter
    // *******************************************************

    // Méthodes
    /**
     * Lancement du process
     */
    @Override
    public void run() {
        loadData();
        sendMessages();

    }

    /**
     * Envoi d'un message
     * 
     * @param affiliation
     */
    private void sendMessage(AFAffiliation affiliation) {
        Message message101 = of2010.createMessage();
        message101.setMinorVersion(new BigInteger("0"));
        // Message message101 = new Message();
        try {
            createHeader(message101);
            createContent(message101);
            System.out.println("Sending 1 avis");
            Class[] suppClasses = new Class[] { ch.ech.xmlns.ech_0044._1.PersonIdentificationPartnerType.class,
                    ch.ech.xmlns.ech_0010._3.AddressInformationType.class };
            String messageXmlFile = JAXBServices.getInstance().marshal(message101, false, false, suppClasses);
            JadeSedexService.getInstance().sendSimpleMessage(messageXmlFile, (Map<String, String>) null);
        } catch (Exception e) {
            StringBuffer msgError = new StringBuffer();
            msgError.append("L'avis de mutation pour l'affilié " + affiliation.getAffilieNumero()
                    + " n'a pas pu être envoyé en raison de l'erreur suivante:");
            msgError.append("\n\n");
            msgError.append(e.toString());
            msgError.append("\n\nInformations techniques:\n");
            msgError.append(JadeUtil.getStackTrace(e));
            try {
                JadeSmtpClient.getInstance().sendMail(eMailAddress, null, null,
                        "Avis de mutation pas envoyé pour l'affilié " + affiliation.getAffilieNumero(),
                        msgError.toString(), null);
            } catch (Exception ex) {
                JadeLogger.error(this, msgError.toString());
            }
        }
    }

    /**
     * Envoi des messages
     */
    private void sendMessages() {
        System.out.println("Sending messages: " + avis.size());
        for (int i = 0; i < avis.size(); i++) {
            sendMessage(avis.get(i));
            cleanup();
        }
    }

    public void setAdresseChange(boolean parseBoolean) {
        adresseChange = parseBoolean;
    }

    public void setAvis(List<AFAffiliation> avis) {
        this.avis = avis;
    }

    public void setAvsChange(boolean parseBoolean) {
        avsChange = parseBoolean;
    }

    public void setCaisseAF(String caisseAF) {
        this.caisseAF = caisseAF;
    }

    public void setCaisseAFFromAdhesion(String newCaisseAFFromAdhesion) {
        caisseAFFromAdhesion = newCaisseAFFromAdhesion;
    }

    public void setCaisseAVS(String caisseAVS) {
        this.caisseAVS = caisseAVS;
    }

    public void setCantonChange(boolean parseBoolean) {
        cantonChange = parseBoolean;
    }

    public void setCurrentAffiliation(AFAffiliation currentAffiliation) {
        this.currentAffiliation = currentAffiliation;
    }

    public void setDateAF(String dateAF) {
        this.dateAF = dateAF;
    }

    public void setDateAvis(String parameter) {
        dateAvis = parameter;
    }

    public void setDateAVS(String dateAVS) {
        this.dateAVS = dateAVS;
    }

    public void setEMailAddress(String newEMailAddress) {
        eMailAddress = newEMailAddress;
    }

    public void setIdAffiliation(String newAffId) {
        idAffiliation = newAffId;
    }

    public void setIdAffiliationAFControle(String idAffiliationAF) {
        idAffiliationAFControle = idAffiliationAF;
    }

    public void setIdAffiliationAVSControle(String idAffiliationAVSControle) {
        this.idAffiliationAVSControle = idAffiliationAVSControle;
    }

    public void setIdTiers(String anIdTiers) {
        idTiers = anIdTiers;
    }

    public void setNomChange(boolean parseBoolean) {
        nomChange = parseBoolean;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public void setPmChange(boolean parseBoolean) {
        pmChange = parseBoolean;
    }

    public void setSelectionImpression(String selectionImpression) {
        this.selectionImpression = selectionImpression;
    }

    @Override
    public void setSession(BSession aSession) {
        session = aSession;
    }

    public void setSiegeChange(boolean parseBoolean) {
        siegeChange = parseBoolean;
    }

    public void setTiers(TITiersViewBean tiers) {
        this.tiers = tiers;
    }

    @Override
    public void setTransaction(BTransaction transaction) {
        // on n'en fait rien...
    }

}
