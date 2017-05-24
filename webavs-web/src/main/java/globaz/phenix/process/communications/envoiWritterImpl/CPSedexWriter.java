package globaz.phenix.process.communications.envoiWritterImpl;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.jaxb.JAXBValidationError;
import globaz.jade.jaxb.JAXBValidationWarning;
import globaz.jade.log.JadeLogger;
import globaz.jade.sedex.JadeSedexDirectory;
import globaz.jade.sedex.JadeSedexDirectoryInitializationException;
import globaz.jade.sedex.JadeSedexService;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichage;
import globaz.phenix.db.communications.CPCommunicationFiscaleAffichageManager;
import globaz.phenix.db.communications.CPLienSedexCommunicationFiscale;
import globaz.phenix.db.communications.CPLienSedexCommunicationFiscaleManager;
import globaz.phenix.db.communications.CPRejets;
import globaz.phenix.db.communications.CPRejetsManager;
import globaz.phenix.db.divers.CPParametreCanton;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.listes.excel.CPXmlmlMappingCommunicationEnvoiProcess;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.CPUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.xml.sax.SAXException;
import ch.eahv_iv.xmlns.eahv_iv_2011_000101._3.ContentType;
import ch.eahv_iv.xmlns.eahv_iv_2011_000101._3.HeaderType;
import ch.eahv_iv.xmlns.eahv_iv_2011_000101._3.Message;
import ch.eahv_iv.xmlns.eahv_iv_2011_000101._3.ObjectFactory;
import ch.eahv_iv.xmlns.eahv_iv_common._1.DeclarationLocalReferenceType;
import ch.eahv_iv.xmlns.eahv_iv_common._1.NaturalPersonsTaxReturnsOASIType;
import ch.eahv_iv.xmlns.eahv_iv_common._1.ReportHeader;
import ch.ech.xmlns.ech_0006._2.PermitRoot;
import ch.ech.xmlns.ech_0007._3.MunicipalityRoot;
import ch.ech.xmlns.ech_0008._2.CountryRoot;
import ch.ech.xmlns.ech_0010._3.AddressInformationType;
import ch.ech.xmlns.ech_0010._3.MailAddressType;
import ch.ech.xmlns.ech_0010._3.OrganisationMailAddressInfoType;
import ch.ech.xmlns.ech_0010._3.OrganisationMailAddressType;
import ch.ech.xmlns.ech_0011._3.PersonRoot;
import ch.ech.xmlns.ech_0044._1.DatePartiallyKnownType;
import ch.ech.xmlns.ech_0044._1.NamedPersonIdType;
import ch.ech.xmlns.ech_0044._1.PersonIdentificationRoot;
import ch.ech.xmlns.ech_0046._1.ContactRoot;
import ch.ech.xmlns.ech_0058._2.EventReport;
import ch.ech.xmlns.ech_0058._2.SendingApplicationType;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPSedexWriter {

    private final static String ACTION_CODE_DEMANDE = "5";
    private final static String ACTION_CODE_RAPPEL = "12";
    private final static String CH_AHV = "CH.AHV";

    private final static String CS_CELIBATAIRE = "1";

    private final static String CS_FEMME = "2";

    private final static String CS_HOMME = "1";
    private final static String CS_MARIE = "2";

    private final static String CS_SEXE_HOMME = "516001";

    private static boolean isCasTest = false;

    private final static String LOC = "LOC";

    // private final static String CS_SEXE_FEMME="516002";
    private final static String MESSAGE_PRIORITY_HIGH = "1";

    private final static String MESSAGE_PRIORITY_NORMAL = "0";
    private final static int ORDERSCOPE_DONNEES_COMMERCIALES = 8;

    private final static int ORDERSCOPE_DONNEES_PRIVEES = 16;
    private final static int ORDERSCOPE_IND = 1;
    private final static int ORDERSCOPE_NA = 2;

    private final static int ORDERSCOPE_TSE = 4;
    public static final List<Integer> orderScopeAuthorizedValues = Arrays.asList(-1, 1, 2, 3, 4, 5, 6, 7, 9, 11, 13,
            14, 15, 18, 19, 21, 22, 23, 25, 26, 27, 29, 30, 31);

    private static String addPhone(String phone) {
        // On controle que le téléphone ne comporte pas d'espaces ni de symboles et qu'il soit compris entre 10 et 20
        // caractères.
        // on supprime les espaces :
        phone = JadeStringUtil.removeChar(phone, ' ');
        // On supprime les +
        phone = JadeStringUtil.removeChar(phone, '+');
        // On regarde que le numéro de téléphone ne contient pas de symboles
        try {
            Long.parseLong(phone);
        } catch (Exception e) {
            return "0000000000";
        }
        if ((phone.length() >= 10) && (phone.length() <= 20)) {
            return phone;
        } else {
            return "0000000000";
        }
    }

    private static void addZipCode(AddressInformationType address, String replaceECommercial, Hashtable<?, ?> data) {
        try {
            int npa = Integer.valueOf(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA).toString());
            if ((npa >= 1000) && (npa <= 9999)) {
                // Il s'agit d'un NPA Suisse
                address.setSwissZipCode(Long.valueOf(String.valueOf(npa)));
            } else {
                // C'est un NPA étranger
                address.setForeignZipCode(String.valueOf(npa));
            }
        } catch (Exception e) {
            address.setForeignZipCode(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA).toString());
        }
    }

    protected static void createLinkBetweenSedexAndComFis(BTransaction transaction,
            CPCommunicationFiscaleAffichage entity, String messageId) throws Exception {
        CPLienSedexCommunicationFiscaleManager mng = new CPLienSedexCommunicationFiscaleManager();
        mng.setSession(entity.getSession());
        mng.setForIdMessageSedex(messageId);
        mng.setForIdCommunication(entity.getIdCommunication());
        mng.find();
        if (mng.size() == 0) {
            CPLienSedexCommunicationFiscale lien = new CPLienSedexCommunicationFiscale();
            lien.setSession(entity.getSession());
            lien.setIdCommunication(entity.getIdCommunication());
            lien.setIdMessageSedex(messageId);
            lien.add(transaction);
        }
    }

    private static XMLGregorianCalendar formatDateGregorian(BSession session, BTransaction transaction,
            String dateNonFormatee, String numAffilie) {
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
        Date dateFormate = null;
        try {
            dateFormate = formater.parse(dateNonFormatee);
            XMLGregorianCalendar xmlCalendar = null;
            GregorianCalendar gCalendar = new GregorianCalendar();
            gCalendar.setTime(dateFormate);
            DatatypeFactory dataTypeFac = DatatypeFactory.newInstance();
            xmlCalendar = dataTypeFac.newXMLGregorianCalendar(gCalendar);
            return xmlCalendar;
        } catch (Exception e) {
            if (!JadeStringUtil.isEmpty(numAffilie)) {
                // Erreur lors du parcours de la date :
                transaction.addErrors(session.getLabel("CP_SEDEX_MSG4") + dateNonFormatee);
            } else {
                transaction.addErrors(session.getLabel("CP_SEDEX_MSG4") + dateNonFormatee + " " + numAffilie);
            }
            return null;
        }
    }

    private static String formatNumAVS(TITiersViewBean tiers, BTransaction transaction) throws IOException {
        String numero = "";
        String numAvs;
        try {
            numAvs = tiers.getNumAvsActuel();
            if (tiers != null) {
                int tailleMin = 11;
                if (NSUtil.unFormatAVS(numAvs).length() < tailleMin) {
                    int difference = tailleMin - numAvs.length();
                    numero = numAvs;
                    for (int i = 0; i < difference; i++) {
                        numero += "0";
                    }
                } else {
                    numero = NSUtil.unFormatAVS(numAvs);
                }
                if (numero.length() == 13) {
                    // Recherche du N° AVS
                    String varNumAvs = NSUtil.unFormatAVS(CPUtil.getNssOrNavs(numero, tiers.getSession()));
                    if (!JadeStringUtil.isEmpty(varNumAvs)) {
                        numero = varNumAvs;
                    }
                }
            }
            return numero;
        } catch (Exception e) {
            return "";
        }
    }

    private static Hashtable<?, ?> getAdresseDataSource(TITiers tiers, BTransaction transaction) throws Exception {
        Hashtable<?, ?> data = null;
        TIAdresseDataSource adresse = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY), true);
        if (adresse == null) {
            try {
                adresse = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                        JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY), true);
            } catch (NullPointerException e) {
                e.printStackTrace();
                throw new NullPointerException("CPSedexWriter.getAdresseDataSource() : Exception with id tiers ["
                        + tiers.getIdTiers() + "] : " + e.toString());

            }
        }
        if (adresse != null) {
            data = adresse.getData();
            if (data != null) {
                String codeCanton = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_CODE_OFAS);
                if (codeCanton.equalsIgnoreCase("030") || JadeStringUtil.isEmpty(codeCanton) || (data == null)) { // 30
                    // =
                    // Etranger
                    adresse = tiers.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                            JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY), true);
                    if (adresse != null) {
                        data = adresse.getData();
                    }
                }
            } else {
                if (!transaction.hasErrors()) {
                    // Problème dans la recherche d'adresse :
                    transaction.addErrors(tiers.getSession().getLabel("CP_SEDEX_MSG5") + " " + tiers.getNomPrenom()
                            + " Id. Tiers: " + tiers.getIdTiers());
                }
            }
        } else {
            // Problème dans la recherche d'adresse :
            if (!transaction.hasErrors()) {
                transaction.addErrors(tiers.getSession().getLabel("CP_SEDEX_MSG5") + " " + tiers.getNomPrenom()
                        + " - Id. Tiers: " + tiers.getIdTiers());
            }
        }
        return data;
    }

    private static XMLGregorianCalendar getAnneeGregorian(String annee) {
        XMLGregorianCalendar xmlCalendar = null;
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.set(Calendar.YEAR, Integer.valueOf(annee));
        try {
            DatatypeFactory dataTypeFac = DatatypeFactory.newInstance();
            xmlCalendar = dataTypeFac.newXMLGregorianCalendar(gCalendar);
        } catch (Exception e) {
            return null;
        }
        return xmlCalendar;
    }

    private static XMLGregorianCalendar getDateActuelleGregorian() {
        XMLGregorianCalendar xmlCalendar = null;
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(new Date());
        try {
            DatatypeFactory dataTypeFac = DatatypeFactory.newInstance();
            xmlCalendar = dataTypeFac.newXMLGregorianCalendar(gCalendar);
        } catch (Exception e) {
            return null;
        }
        return xmlCalendar;
    }

    private static XMLGregorianCalendar getDateGregorian(BSession session, BTransaction transaction, String date,
            String numAffilie) {
        XMLGregorianCalendar xmlCalendar = null;
        GregorianCalendar gCalendar = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        try {
            gCalendar.setTime(format.parse(date));
            DatatypeFactory dataTypeFac = DatatypeFactory.newInstance();
            xmlCalendar = dataTypeFac.newXMLGregorianCalendar(gCalendar);
        } catch (Exception e) {
            if (!JadeStringUtil.isEmpty(numAffilie)) {
                // Erreur lors du traitement de l'année
                transaction.addErrors(session.getLabel("CP_SEDEX_MSG6") + " " + numAffilie);
            } else {
                // Erreur lors du traitement de l'année
                transaction.addErrors(session.getLabel("CP_SEDEX_MSG6"));
            }
            return null;
        }
        return xmlCalendar;
    }

    /* Recherche de la date de mariage */
    private static String getDateOfMaritalStatus(TITiersViewBean tiers, TITiersViewBean conjoint) throws Exception {
        if ((tiers != null) && (conjoint != null)) {
            // Recherche du tiers en tant qu'enfant dans la relation
            TICompositionTiersManager tiersManager = new TICompositionTiersManager();
            tiersManager.setSession(tiers.getSession());
            tiersManager.setForIdTiersEnfant(tiers.getIdTiers());
            tiersManager.setForIdTiersParent(conjoint.getIdTiers());
            tiersManager.find();
            if (tiersManager.size() > 0) {
                TICompositionTiers tiers1 = (TICompositionTiers) tiersManager.getFirstEntity();
                return tiers1.getDebutRelation();
            } else {
                // Recherche du tiers en tant que parent dans la relation
                tiersManager = new TICompositionTiersManager();
                tiersManager.setSession(tiers.getSession());
                tiersManager.setForIdTiersEnfant(conjoint.getIdTiers());
                tiersManager.setForIdTiersParent(tiers.getIdTiers());
                tiersManager.find();
                if (tiersManager.size() > 0) {
                    TICompositionTiers tiers1 = (TICompositionTiers) tiersManager.getFirstEntity();
                    return tiers1.getDebutRelation();
                }
            }
        }
        return "";
    }

    private static XMLGregorianCalendar getDateOfMaritalStatusGregorian(String dateMariage) throws Exception {
        GregorianCalendar gCalendar = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        gCalendar.setTime(format.parse(dateMariage));
        try {
            DatatypeFactory dataTypeFac = DatatypeFactory.newInstance();
            return dataTypeFac.newXMLGregorianCalendar(gCalendar);
        } catch (Exception e) {
            return null;
        }
    }

    private static String getMessageType() {
        return "2011";
    }

    private static String getSex(String numAffilie, TITiersViewBean tiers, BTransaction transaction) throws Exception {
        String sexe = tiers.getSexe();
        if (!JadeStringUtil.isEmpty(sexe) && !sexe.equals("0")) {
            if (CPSedexWriter.CS_SEXE_HOMME.equalsIgnoreCase(sexe)) {
                return CPSedexWriter.CS_HOMME;
            } else {
                return CPSedexWriter.CS_FEMME;
            }
        } else {
            // Le sexe n'est pas renseigné pour cet affilié
            if (!JadeStringUtil.isEmpty(numAffilie)) {
                transaction.addErrors(tiers.getSession().getLabel("CP_SEDEX_MSG10") + " " + numAffilie);
            } else {
                transaction.addErrors(tiers.getSession().getLabel("CP_SEDEX_MSG10"));
            }
            return "";
        }

    }

    private static String getSubject() {
        return "Demande de Communications Fiscales";
    }

    private static String getSubMessageType() {
        return "000101";
    }

    private static String getSubMessageType102() {
        return "000102";
    }

    private static String getUserEmail(BSession session) {
        return session.getUserEMail();
    }

    private static String getUserName(BSession session) {
        return session.getUserFullName();
    }

    protected static void insertInfoNumAvs(BTransaction transaction, CPCommunicationFiscaleAffichage comFis,
            NaturalPersonsTaxReturnsOASIType object101, ch.ech.xmlns.ech_0044._1.ObjectFactory objFac44)
            throws IOException, Exception {
        String numAvs = CPSedexWriter.formatNumAVS(comFis.getTiers(), transaction);
        NamedPersonIdType otherPersonId = objFac44.createNamedPersonIdType();
        if (NSUtil.unFormatAVS(numAvs).length() == 11) {
            otherPersonId.setPersonIdCategory(CPSedexWriter.CH_AHV);
            otherPersonId.setPersonId(numAvs);
            object101.getOtherPersonId().add(otherPersonId);
        }
    }

    protected static AFAffiliation loadAffiliation(CPCommunicationFiscaleAffichage entity) throws Exception {
        AFAffiliation affiliation;
        affiliation = new AFAffiliation();
        affiliation.setSession(entity.getSession());
        affiliation.setAffiliationId(entity.getIdAffiliation());
        affiliation.retrieve();
        return affiliation;
    }

    protected static TITiersViewBean loadTiers(String idTiers, BSession session) throws Exception {
        TITiersViewBean tiers;
        tiers = new TITiersViewBean();
        tiers.setSession(session);
        tiers.setIdTiers(idTiers);
        tiers.retrieve();
        return tiers;
    }

    private static String replaceECommercial(String officialName) {
        return officialName.replace('&', 'E');
    }

    private static String replaceECommercial12(String houseNumber) {
        if (!JadeStringUtil.isEmpty(houseNumber)) {
            houseNumber = houseNumber.replace('&', 'E');
            houseNumber = houseNumber.replace('/', ' ');
            if (houseNumber.length() > 12) {
                houseNumber = houseNumber.substring(0, 12);
            }
            return houseNumber;
        } else {
            return "";
        }

    }

    protected static ch.ech.xmlns.ech_0010._3.ObjectFactory setChampCommunHeader(BSession session,
            TITiersViewBean entity, TITiersViewBean conjoint, AFAffiliation affiliation, BTransaction transaction,
            NaturalPersonsTaxReturnsOASIType object101, ch.ech.xmlns.ech_0044._1.ObjectFactory objFacEch044,
            boolean isMarie) throws Exception {
        // Infotmation du Vn (= NSS pour Sedex)
        CPSedexWriter.setInfoVn(session, entity, affiliation, transaction, object101);
        // Official Name
        object101.setOfficialName(CPSedexWriter.replaceECommercial(entity.getDesignation1()));
        // First Name
        object101.setFirstName(CPSedexWriter.replaceECommercial(entity.getDesignation2()));
        // Sex
        String numAffilie = "";
        if (affiliation != null) {
            numAffilie = affiliation.getAffilieNumero();
        }
        object101.setSex(CPSedexWriter.getSex(numAffilie, entity, transaction));
        // DateOfBirth
        // Year Month Day
        DatePartiallyKnownType dob = objFacEch044.createDatePartiallyKnownType();
        object101.setDateOfBirth(dob);
        DatePartiallyKnownType dateOfBirth = object101.getDateOfBirth();
        dateOfBirth.setYearMonthDay(CPSedexWriter.formatDateGregorian(session, transaction, entity.getDateNaissance(),
                numAffilie));
        // adresse du cas a demandé pour l'header
        ch.ech.xmlns.ech_0010._3.ObjectFactory objFacEch10 = CPSedexWriter.setInfoAdresseForHeader(session, entity,
                affiliation, transaction, object101);
        // MaritalStatus
        if (!isMarie) {
            object101.setMaritalStatus(CPSedexWriter.CS_CELIBATAIRE);
        } else {
            object101.setMaritalStatus(CPSedexWriter.CS_MARIE);
            String dateMariage = CPSedexWriter.getDateOfMaritalStatus(entity, conjoint);
            if (!JadeStringUtil.isEmpty(dateMariage)) {
                object101.setDateOfMaritalStatus(CPSedexWriter.getDateOfMaritalStatusGregorian(dateMariage));
            }
        }
        // dateOfEntry
        if ((affiliation != null) && !JadeStringUtil.isEmpty(affiliation.getDateDebut())) {
            object101.setDateOfEntry(CPSedexWriter.formatDateGregorian(session, transaction,
                    affiliation.getDateDebut(), numAffilie));
        }
        return objFacEch10;
    }

    protected static ch.ech.xmlns.ech_0010._3.ObjectFactory setInfoAdresseForHeader(BSession session,
            TITiersViewBean entity, AFAffiliation affiliation, BTransaction transaction,
            NaturalPersonsTaxReturnsOASIType object101) throws Exception {
        ch.ech.xmlns.ech_0010._3.ObjectFactory objFacEch10 = new ch.ech.xmlns.ech_0010._3.ObjectFactory();
        AddressInformationType add = objFacEch10.createAddressInformationType();
        object101.setAddress(add);
        AddressInformationType address = object101.getAddress();

        Hashtable<?, ?> adresse = CPSedexWriter.getAdresseDataSource(entity, transaction);

        if (adresse != null) {
            CPSedexWriter.setInfoAdresseForSedex(address, adresse);
        } else {
            if (!transaction.hasErrors()) {
                // Aucune adresse trouvée pour l'affilié
                if (affiliation != null && !JadeStringUtil.isEmpty(affiliation.getAffilieNumero())) {
                    transaction.addErrors(session.getLabel("CP_SEDEX_MSG13") + " - " + affiliation.getAffilieNumero());
                } else {
                    transaction.addErrors(session.getLabel("CP_SEDEX_MSG13") + " - id Tiers : " + entity.getIdTiers());
                }
            }
        }
        return objFacEch10;
    }

    /* Ajout de l'information concernant une adresse */
    protected static void setInfoAdresseForSedex(AddressInformationType address, Hashtable<?, ?> data) {
        address.setStreet(CPSedexWriter.replaceECommercial(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE)
                .toString()));
        address.setHouseNumber(CPSedexWriter.replaceECommercial12(data.get(
                TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO).toString()));
        address.setTown(CPSedexWriter.replaceECommercial(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE)
                .toString()));
        CPSedexWriter.addZipCode(address,
                CPSedexWriter.replaceECommercial(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA).toString()),
                data);
        address.setCountry(CPSedexWriter.replaceECommercial(data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_PAYS_ISO)
                .toString()));
    }

    /* Ajout de l'information concernant l'adresse du contribuable */
    protected static void setInfoAdresseTaxPayer(BSession session, TITiersViewBean tiers, BTransaction transaction,
            String numAffilie, AddressInformationType addressTP) throws Exception {
        Hashtable<?, ?> adresse = CPSedexWriter.getAdresseDataSource(tiers, transaction);
        if (adresse != null) {
            CPSedexWriter.setInfoAdresseForSedex(addressTP, adresse);
        } else {
            if (!transaction.hasErrors()) {
                if (!JadeStringUtil.isEmpty(numAffilie)) {
                    // Aucune adresse trouvée pour l'affilié.
                    transaction.addErrors(session.getLabel("CP_SEDEX_MSG17") + " " + numAffilie);
                } else {
                    transaction.addErrors(session.getLabel("CP_SEDEX_MSG17"));
                }

            }
        }
    }

    /* Ajout de l'information concernant l'adresse de l'épouse */
    protected static void setInfoAdressSpouse(BSession session, TITiersViewBean conjoint, TITiersViewBean tiers,
            String numAffilie, BTransaction transaction, ch.ech.xmlns.ech_0010._3.ObjectFactory objFacEch10,
            NaturalPersonsTaxReturnsOASIType spouse) throws Exception {
        Hashtable<?, ?> adresse = null;
        AddressInformationType addSP = objFacEch10.createAddressInformationType();
        spouse.setAddress(addSP);
        AddressInformationType addressSP = spouse.getAddress();
        if (!session.getCurrentThreadTransaction().hasErrors()) {
            // Recherche adresse du conjoint
            adresse = CPSedexWriter.getAdresseDataSource(conjoint, transaction);
            if (adresse == null) {
                // Recherche adresse du contribuable (cas ou le mari n'appartient pas à la caisse)
                adresse = CPSedexWriter.getAdresseDataSource(tiers, transaction);
            }
            if (adresse != null) {
                CPSedexWriter.setInfoAdresseForSedex(addressSP, adresse);
            } else {
                if (!transaction.hasErrors()) {
                    if (!JadeStringUtil.isEmpty(numAffilie)) {
                        // Aucune adresse trouvée pour le conjoint.
                        transaction.addErrors(session.getLabel("CP_SEDEX_MSG16") + " - " + numAffilie);
                    } else {
                        transaction.addErrors(session.getLabel("CP_SEDEX_MSG16"));
                    }
                }
            }
        }
    }

    protected static void setInfoOtherPersonId(TITiersViewBean conjoint, AFAffiliation affConjoint,
            BTransaction transaction, ch.ech.xmlns.ech_0044._1.ObjectFactory objFacEch044,
            NaturalPersonsTaxReturnsOASIType spouse) throws IOException {

        String numAvs = CPSedexWriter.formatNumAVS(conjoint, transaction);
        // Insertion info du numéro avs
        CPSedexWriter.setInfoOtherPersonIdNumAvs(objFacEch044, spouse, numAvs);
        // Insertion info du numéro d'affilié
        CPSedexWriter.setInfoOtherPersonIdNumAffilie(affConjoint, objFacEch044, spouse);
    }

    protected static void setInfoOtherPersonIdNumAffilie(AFAffiliation affConjoint,
            ch.ech.xmlns.ech_0044._1.ObjectFactory objFacEch044, NaturalPersonsTaxReturnsOASIType spouse) {
        if ((affConjoint != null) && !JadeStringUtil.isEmpty(affConjoint.getAffilieNumero())) {
            NamedPersonIdType otherPersonId = objFacEch044.createNamedPersonIdType();
            otherPersonId.setPersonIdCategory(CPSedexWriter.LOC);
            otherPersonId.setPersonId(affConjoint.getAffilieNumero());
            spouse.getOtherPersonId().add(otherPersonId);
        }
    }

    /*
     * Ajout de l'information concernant le numéro AVS
     */
    protected static void setInfoOtherPersonIdNumAvs(ch.ech.xmlns.ech_0044._1.ObjectFactory objFacEch044,
            NaturalPersonsTaxReturnsOASIType spouse, String numAvs) {
        if (numAvs.length() == 11) { // PersonIdCategory
            NamedPersonIdType otherPersonId = objFacEch044.createNamedPersonIdType();
            otherPersonId.setPersonIdCategory(CPSedexWriter.CH_AHV);
            otherPersonId.setPersonId(numAvs);
            spouse.getOtherPersonId().add(otherPersonId);
        }
    }

    /*
     * Ajout de l'information concernant le NSS
     */
    protected static void setInfoVn(BSession session, TITiersViewBean entity, AFAffiliation affiliation,
            BTransaction transaction, NaturalPersonsTaxReturnsOASIType object101) throws NumberFormatException {
        String numAvs = "";
        try {
            if (!JadeStringUtil.isEmpty(entity.getNumAvsActuel())) {
                numAvs = NSUtil.unFormatAVS(entity.getNumAvsActuel());
                if (numAvs.length() < 13) {
                    // Il n'existe pas de NAVS13 pour cet affilié
                    if (affiliation != null && !JadeStringUtil.isEmpty(affiliation.getAffilieNumero())) {
                        transaction.addErrors(session.getLabel("CP_SEDEX_MSG12") + " - "
                                + affiliation.getAffilieNumero());
                    } else {
                        transaction.addErrors(session.getLabel("CP_SEDEX_MSG12") + " - id Tiers : "
                                + entity.getIdTiers());
                    }
                } else {
                    object101.setVn(Long.valueOf(numAvs));
                }
            } else {
                // Il n'existe pas de NAVS13 et de num avs pour cet affilié
                if (affiliation != null && !JadeStringUtil.isEmpty(affiliation.getAffilieNumero())) {
                    transaction.addErrors(session.getLabel("CP_SEDEX_MSG18") + " - " + affiliation.getAffilieNumero());
                } else {
                    transaction.addErrors(session.getLabel("CP_SEDEX_MSG18") + " - id Tiers : " + entity.getIdTiers());
                }
            }

        } catch (Exception e) {
            String numInformation = "";
            if (entity != null) {
                numInformation = " - id Tiers : " + entity.getIdTiers();
            }

            if (affiliation != null) {
                numInformation = " - " + affiliation.getAffilieNumero();
            }

            // Problème lors de la recherche du n°avs
            transaction.addErrors(session.getLabel("CP_SEDEX_MSG11") + " " + e.toString() + numInformation);
        }

    }

    /* Ajout des informations concernant l'épouse */
    protected static void setSpouse(BSession session, TITiersViewBean conjoint, TITiersViewBean tiers,
            AFAffiliation affConjoint, String numAffilie, BTransaction transaction,
            ch.eahv_iv.xmlns.eahv_iv_common._1.ObjectFactory objFacEahvIvCommon,
            ch.ech.xmlns.ech_0044._1.ObjectFactory objFacEch044, ch.ech.xmlns.ech_0010._3.ObjectFactory objFacEch10,
            ContentType content101) throws Exception {
        NaturalPersonsTaxReturnsOASIType sp = objFacEahvIvCommon.createNaturalPersonsTaxReturnsOASIType();
        content101.setSpouse(sp);
        NaturalPersonsTaxReturnsOASIType spouse = content101.getSpouse();
        // Vn
        if (JadeStringUtil.isEmpty(conjoint.getNumAvsActuel())) {
            // Il n'existe pas de NAV et de num avs pour cet affilié
            if (affConjoint != null && !JadeStringUtil.isEmpty(affConjoint.getAffilieNumero())) {
                transaction.addErrors(session.getLabel("CP_SEDEX_MSG18") + " - " + affConjoint.getAffilieNumero());
            } else {
                transaction.addErrors(session.getLabel("CP_SEDEX_MSG18") + " - id Tiers : " + conjoint.getIdTiers());
            }
        } else {
            String nss = NSUtil.unFormatAVS(conjoint.getNumAvsActuel());
            if (!(nss.length() < 13)) {// Il s'agit alors d'un
                // NAVS13
                spouse.setVn(Long.valueOf(nss));
            }
        }

        // OtherPersonId
        // Person Id
        CPSedexWriter.setInfoOtherPersonId(conjoint, affConjoint, transaction, objFacEch044, spouse);
        // official name
        spouse.setOfficialName(CPSedexWriter.replaceECommercial(conjoint.getDesignation1()));
        // first name
        spouse.setFirstName(CPSedexWriter.replaceECommercial(conjoint.getDesignation2()));
        // Sex
        spouse.setSex(CPSedexWriter.getSex(numAffilie, conjoint, transaction));
        // dateNaissance
        DatePartiallyKnownType dobSP = objFacEch044.createDatePartiallyKnownType();
        spouse.setDateOfBirth(dobSP);
        DatePartiallyKnownType dateOfBirthSP = spouse.getDateOfBirth();
        dateOfBirthSP.setYearMonthDay(CPSedexWriter.formatDateGregorian(session, transaction,
                conjoint.getDateNaissance(), numAffilie));
        // adresse
        CPSedexWriter.setInfoAdressSpouse(session, conjoint, tiers, numAffilie, transaction, objFacEch10, spouse);
        // marital status
        spouse.setMaritalStatus(CPSedexWriter.CS_MARIE);
        String dateMariage = CPSedexWriter.getDateOfMaritalStatus(tiers, conjoint);
        if (!JadeStringUtil.isEmpty(dateMariage)) {
            spouse.setDateOfMaritalStatus(CPSedexWriter.getDateOfMaritalStatusGregorian(dateMariage));
        }
        // dateOfEntry
        if ((affConjoint != null) && (!affConjoint.isNew())) {
            if (!JadeStringUtil.isEmpty(affConjoint.getDateDebut())) {
                spouse.setDateOfEntry(CPSedexWriter.getDateGregorian(session, transaction, affConjoint.getDateDebut(),
                        numAffilie));
            }
        }
    }

    protected static void setTaxPayer(BSession session, TITiersViewBean tiers, TITiersViewBean conjoint,
            AFAffiliation affiliation, BTransaction transaction, boolean isMarie, String numAffilie,
            ch.ech.xmlns.ech_0044._1.ObjectFactory objFac44, ch.ech.xmlns.ech_0010._3.ObjectFactory objFac10,
            ContentType content) throws Exception {
        NaturalPersonsTaxReturnsOASIType taxPayer = content.getTaxpayer();

        // Vn
        if (JadeStringUtil.isEmpty(tiers.getNumAvsActuel())) {
            // Il n'existe pas de NAV et de num avs pour cet affilié
            if ((affiliation != null) && !JadeStringUtil.isEmpty(affiliation.getAffilieNumero())) {
                transaction.addErrors(session.getLabel("CP_SEDEX_MSG18") + " - " + affiliation.getAffilieNumero());
            } else {
                transaction.addErrors(session.getLabel("CP_SEDEX_MSG18") + " - id Tiers : " + tiers.getIdTiers());
            }
        } else {
            taxPayer.setVn(Long.valueOf(NSUtil.unFormatAVS(tiers.getNumAvsActuel())));
        }
        // Insertion info du numréo d'affilié
        CPSedexWriter.setInfoOtherPersonIdNumAffilie(affiliation, objFac44, taxPayer);
        // official name
        taxPayer.setOfficialName(CPSedexWriter.replaceECommercial(tiers.getDesignation1()));
        // first name
        taxPayer.setFirstName(CPSedexWriter.replaceECommercial(tiers.getDesignation2()));
        // sex
        taxPayer.setSex(CPSedexWriter.getSex(numAffilie, tiers, transaction));
        // Date Of Birth
        DatePartiallyKnownType dobTP = objFac44.createDatePartiallyKnownType();
        taxPayer.setDateOfBirth(dobTP);
        DatePartiallyKnownType dateOfBirthTP = taxPayer.getDateOfBirth();
        dateOfBirthTP.setYearMonthDay(CPSedexWriter.formatDateGregorian(session, transaction, tiers.getDateNaissance(),
                numAffilie));
        // adresse
        AddressInformationType addTP = objFac10.createAddressInformationType();
        taxPayer.setAddress(addTP);
        AddressInformationType addressTP = taxPayer.getAddress();
        // Inormation sur l'adresse du contribuable (taxPayer)
        CPSedexWriter.setInfoAdresseTaxPayer(session, tiers, transaction, numAffilie, addressTP);
        // MaritalStatus
        if (!isMarie) {
            taxPayer.setMaritalStatus(CPSedexWriter.CS_CELIBATAIRE);
        } else {
            taxPayer.setMaritalStatus(CPSedexWriter.CS_MARIE);
            String dateMariage = CPSedexWriter.getDateOfMaritalStatus(tiers, conjoint);
            if (!JadeStringUtil.isEmpty(dateMariage)) {
                taxPayer.setDateOfMaritalStatus(CPSedexWriter.getDateOfMaritalStatusGregorian(dateMariage));
            }
        }
        // dateOfEntry
        if ((affiliation != null) && !JadeStringUtil.isEmpty(affiliation.getDateDebut())) {
            taxPayer.setDateOfEntry(CPSedexWriter.formatDateGregorian(session, transaction, affiliation.getDateDebut(),
                    numAffilie));
        }
    }

    protected static CPCommunicationFiscale updateDateCommunicationEtLienSedex(BTransaction transaction,
            Message message101, CPCommunicationFiscaleAffichage entity, String messageId) throws Exception {
        CPCommunicationFiscale comFis;
        comFis = new CPCommunicationFiscale();
        comFis.setSession(entity.getSession());
        comFis.setIdCommunication(entity.getIdCommunication());
        comFis.retrieve(transaction);
        if (!comFis.isNew()) {
            comFis.setDateEnvoi(JACalendar.todayJJsMMsAAAA());
            comFis.setOrderscope(message101.getContent().getOrderScope().toString());
            // On génère un message ID
            comFis.setIdMessageSedex(messageId);
            comFis.update(transaction);
            // Mise à jour des rejets
            // lecture du fichier de lien entre la com.fis et les envois Sedex
            CPSedexWriter.updateStatusForRejext(transaction, comFis);
            // Création dans la table historique -> utile pour les rejets
            CPSedexWriter.createLinkBetweenSedexAndComFis(transaction, entity, messageId);
        }
        return comFis;
    }

    /**
     * Met à jour létat des éventuels rejets à TRAITER
     * 
     * @author hna
     * @param transaction
     * @param entity
     * @param comFis
     * @throws Exception
     */
    protected static void updateStatusForRejext(BTransaction transaction, CPCommunicationFiscale entity)
            throws Exception {
        CPRejetsManager rejetMng = new CPRejetsManager();
        rejetMng.setSession(entity.getSession());
        rejetMng.setForIdCommunication(entity.getIdCommunication());
        rejetMng.find();
        for (int j = 0; j < rejetMng.getSize(); j++) {
            CPRejets rejet = new CPRejets();
            rejet.setSession(entity.getSession());
            rejet.setIdRejets(((CPRejets) rejetMng.getEntity(j)).getIdRejets());
            rejet.retrieve();
            if ((rejet.isNew() == false) && (CPRejets.CS_ETAT_TRAITE.equalsIgnoreCase(rejet.getEtat()) == false)) {
                rejet.setEtat(CPRejets.CS_ETAT_TRAITE);
                rejet.update(transaction);
            }
            if ((rejet.isNew() == false) && (CPRejets.CS_ETAT_TRAITE.equalsIgnoreCase(rejet.getEtat()) == true)) {
                rejet.setEtat(CPRejets.CS_ETAT_ENVOYE);
                rejet.update(transaction);
            }
        }
    }

    // On renseigne postalAddress : adresse de la caisse de compensation
    private TIAdministrationViewBean caisse = null;

    private ArrayList<String> communicationEnErreur = new ArrayList<String>();

    private String date_impression = "";

    private boolean donneesCommerciales = false;

    private boolean donneesPrivees = false;

    private boolean envoiImmediat = false;

    public String filename = "";

    private boolean lifd = false;

    private BProcess processAppelant = null;

    private BSession session;

    private String user = "";

    public CPSedexWriter(BSession session) {
        setUser(CPSedexWriter.getUserName(session));
    }

    public AFAffiliation _retourAffiliation(String idTiers, String dateDebutCP, String dateFinCP) throws Exception {

        AFAffiliation affiliationRetour = new AFAffiliation();
        try {
            AFAffiliationManager affiliationManager = new AFAffiliationManager();
            affiliationManager.setForIdTiers(idTiers);
            affiliationManager.setSession(getSession());
            affiliationManager.find();

            for (int i = 0; i < affiliationManager.size(); i++) {

                AFAffiliation affiliation = (AFAffiliation) affiliationManager.getEntity(i);

                if (JadeStringUtil.isBlankOrZero(affiliation.getDateFin())) {

                    if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateDebutCP,
                            affiliation.getDateDebut())) {

                        affiliationRetour = affiliation;
                    }
                } else {
                    if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateDebutCP,
                            affiliation.getDateDebut())
                            && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateFinCP,
                                    affiliation.getDateFin())) {

                        affiliationRetour = affiliation;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }

        return affiliationRetour;

    }

    public String calculOrderScopeUnique(CPCommunicationFiscaleAffichage comFiscale, BTransaction transaction)
            throws Exception {
        int returnValue = 1;
        int value = 0;
        if (lifd) {
            return "-1";
        } else {

            if (isDonneesCommerciales() || isDonneesPrivees()) {
                if (isDonneesCommerciales()) {
                    value += CPSedexWriter.ORDERSCOPE_DONNEES_COMMERCIALES;
                }
                if (isDonneesPrivees()) {
                    value += CPSedexWriter.ORDERSCOPE_DONNEES_PRIVEES;
                }
            }
            AFAffiliation aff = new AFAffiliation();
            aff.setSession(comFiscale.getSession());
            aff.setAffiliationId(comFiscale.getIdAffiliation());
            aff.retrieve();
            if (!aff.isNew() && (aff != null)) {
                if (aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_INDEP)
                        || aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_INDEP_EMPLOY)) {
                    returnValue = CPSedexWriter.ORDERSCOPE_IND + value;
                } else if (aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)) {
                    returnValue = CPSedexWriter.ORDERSCOPE_NA + value;
                } else if (aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_TSE)
                        || aff.getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE)) {
                    // pour les TSE, il n'est pas possible de demander les données commerciales et/ou privées
                    returnValue = CPSedexWriter.ORDERSCOPE_TSE;
                } else {
                    returnValue = CPSedexWriter.ORDERSCOPE_NA + value;
                }
            }

            if (CPSedexWriter.orderScopeAuthorizedValues.contains(returnValue)) {
                return String.valueOf(returnValue);
            } else {
                StringBuffer erreurMessage = new StringBuffer();
                erreurMessage.append(comFiscale.getSession().getLabel("CP_SEDEX_MSG1"));
                erreurMessage.append(" " + comFiscale.getSession().getLabel("CP_ENVOI_SEDEX"));

                if (!JadeStringUtil.isEmpty(comFiscale.getNumAffilie())) {
                    erreurMessage.append(" - " + comFiscale.getNumAffilie());
                }
                if (aff != null && !aff.isNew() && !JadeStringUtil.isEmpty(aff.getTypeAffiliation())) {
                    erreurMessage.append(" - "
                            + CodeSystem.getLibelle(comFiscale.getSession(), aff.getTypeAffiliation()));
                }
                if (isDonneesCommerciales()) {
                    erreurMessage.append(" - " + comFiscale.getSession().getLabel("DONNEE_COMM"));
                }

                if (isDonneesPrivees()) {
                    erreurMessage.append(" - " + comFiscale.getSession().getLabel("DONNEE_PRIVEE"));
                }
                transaction.addErrors(erreurMessage.toString());

            }
        }
        return "";
    }

    protected void createDeclarationLocalReferenceForHeader101(BSession session, HeaderType header101) {
        ch.eahv_iv.xmlns.eahv_iv_common._1.ObjectFactory objFacEahvIvCommon = new ch.eahv_iv.xmlns.eahv_iv_common._1.ObjectFactory();
        DeclarationLocalReferenceType declarationLocalReference = objFacEahvIvCommon
                .createDeclarationLocalReferenceType();
        declarationLocalReference.setName(CPSedexWriter.replaceECommercial(CPSedexWriter.getUserName(session)));
        declarationLocalReference.setDepartment("");
        if (!JadeStringUtil.isEmpty(session.getUserInfo().getPhone())) {
            declarationLocalReference.setPhone(CPSedexWriter.addPhone(session.getUserInfo().getPhone()));
        } else {
            // Le téléphone ne peut être vide
            declarationLocalReference.setPhone("0000000000");
        }
        declarationLocalReference.setEmail(CPSedexWriter.getUserEmail(session));
        header101.setDeclarationLocalReference(declarationLocalReference);
    }

    /*
     * méthode pour la création du style de la feuille pour la liste des décisions avec mise en compte entêtes, des
     * bordures...
     */
    public SedexResult createFileSedexXMLEnvoiGroupe(CPCommunicationFiscaleAffichageManager manager,
            BTransaction transaction, BProcess process, int debutATraiter, int finATraiter,
            boolean wantDonneesCommerciales, boolean wantDonneesPrivees) {

        SedexResult result = new SedexResult();
        ch.eahv_iv.xmlns.eahv_iv_2011_000102._3.ObjectFactory objFac102 = new ch.eahv_iv.xmlns.eahv_iv_2011_000102._3.ObjectFactory();
        ch.eahv_iv.xmlns.eahv_iv_2011_000102._3.Message message102 = objFac102.createMessage();

        ObjectFactory objFac101 = new ObjectFactory();
        Message message101 = objFac101.createMessage();

        CPCommunicationFiscale comFis = null;
        ArrayList<String> communicationTraitee = new ArrayList<String>();
        CPCommunicationFiscaleAffichage entity = null;
        try {

            int nbreCommunications = manager.getSize();
            setDonneesCommerciales(wantDonneesCommerciales);
            setDonneesPrivees(wantDonneesPrivees);

            for (int j = debutATraiter; j < finATraiter; j++) {
                CPDecision decision = null;
                String idConjoint = "";
                String idTiers = "";
                TITiersViewBean conjoint = null;
                AFAffiliation affiliationConjoint = null;
                AFAffiliation affiliation = null;
                boolean isMaried = false;
                if (process.isAborted()) {
                    return null;
                }
                process.incProgressCounter();
                entity = (CPCommunicationFiscaleAffichage) manager.getEntity(j);

                // [EST] D0202 - Controle de l'état du SEDEX, si 'non traité' ou 'abandonné' on ne le traite pas
                boolean isEtatRejetNonTraiteAbandonne = isEtatRejetSedexNonTraiteAbandonne(process.getSession(),
                        entity.getIdCommunication());
                if (!isEtatRejetNonTraiteAbandonne) {

                    // Sauvegarde du cas traité pour éventuellement le mettre en erreur
                    // si la validation du fichier xml a échoué
                    if (entity.isNew() != false) {
                        continue;
                    }

                    communicationTraitee.add(entity.getNumAffilie());
                    process.incProgressCounter();
                    message101 = objFac101.createMessage();

                    if (message102.getContent() == null) {
                        ch.eahv_iv.xmlns.eahv_iv_2011_000102._3.ContentType con = objFac102.createContentType();
                        message102.setContent(con);
                    }

                    idTiers = entity.getIdTiers();
                    affiliation = CPSedexWriter.loadAffiliation(entity);

                    // On recherche la décision qui a généré la demande pour retrouver l'idConjoint

                    CPDecisionManager decM = new CPDecisionManager();
                    decM.setSession(process.getSession());
                    decM.setForIdAffiliation(entity.getIdAffiliation());
                    decM.setForAnneeDecision(entity.getAnneeDecision());
                    decM.setForIsActive(Boolean.TRUE);
                    decM.find();
                    if (decM.size() == 0) {
                        if (JadeStringUtil.isEmpty(affiliation.getAffilieNumero()) == false) {
                            transaction.addErrors(entity.getSession().getLabel("CP_SEDEX_MSG2")
                                    + entity.getIdCommunication() + " - " + affiliation.getAffilieNumero() + " - "
                                    + entity.getAnneeDecision());
                        } else {
                            transaction.addErrors(process.getSession().getLabel("CP_SEDEX_MSG2")
                                    + entity.getIdCommunication());
                        }

                    } else {
                        decision = (CPDecision) decM.getFirstEntity();
                        idConjoint = decision.getIdConjoint();
                        if ((JadeStringUtil.isBlankOrZero(idConjoint) == false)
                                || decision.getDivision2().equals(Boolean.TRUE)) {
                            isMaried = true;
                        }
                        // Selon les directives Sedex:
                        // Le contribuable est toujours le mari ==> si une communication a un tiers
                        // de sexe féminin et qui a un conjoint, on inverse les rôles, le contribuable passe conjoint
                        // (même si l'homme est inconnu à la caisse)
                        // Selon le mode VD -> Le tiers (homme ou femme) est le contribuable
                        String modeEnvoiSedex = CPParametreCanton
                                .findCodeWhitTypeAndCanton(process.getSession(), entity.getCanton(),
                                        CPParametreCanton.CS_MODE_ENVOI_SEDEX, JACalendar.todayJJsMMsAAAA());

                        boolean changeAffilie = false;
                        if (!CPParametreCanton.CS_SEDEXENVOI_MODE_VD.equalsIgnoreCase(modeEnvoiSedex)
                                && entity.getSexe().equalsIgnoreCase(TITiersViewBean.CS_FEMME)
                                && (!JadeStringUtil.isBlankOrZero(idConjoint) || decision.getDivision2().equals(
                                        Boolean.TRUE))) {
                            idTiers = idConjoint;
                            idConjoint = entity.getIdTiers();
                            affiliationConjoint = CPSedexWriter.loadAffiliation(entity);
                            affiliation = null;
                            changeAffilie = true;
                        }

                        // Rechercher du tiers
                        TITiersViewBean tiers = null;
                        if ((JadeStringUtil.isBlankOrZero(idTiers) == false)
                                && ("100".equalsIgnoreCase(idTiers) == false)) {
                            tiers = CPSedexWriter.loadTiers(idTiers, process.getSession());
                        }
                        if ((JadeStringUtil.isBlankOrZero(idConjoint) == false)
                                && ("100".equalsIgnoreCase(idConjoint) == false)) {
                            conjoint = CPSedexWriter.loadTiers(idConjoint, process.getSession());
                            // On va rechercher l'affiliation du conjoint si elle n'est pas renseignée (cas ou le tiers
                            // de la décision est une femme)
                            if (conjoint != null) {
                                if (!changeAffilie) {
                                    affiliationConjoint = CPToolBox.returnAffiliation(entity.getSession(), transaction,
                                            conjoint.getIdTiers(), entity.getAnneeDecision(), "", 1);
                                }
                            }
                        }

                        String messageId = creationDemande(objFac101, objFac102, message101, message102, transaction,
                                entity, tiers, affiliation, conjoint, affiliationConjoint, isMaried);
                        // Mise à jour de la date d'envoi
                        if (!transaction.hasErrors()) {
                            // Si il n'y a pas d'erreurs on ajoute le message 101 au 102
                            message102.getContent().getMessage().add(message101);
                            // sauvegarde de la date d'envoi et création du lien entre la communication et le message
                            // Sedex (utile pour les rejets)
                            comFis = CPSedexWriter.updateDateCommunicationEtLienSedex(transaction, message101, entity,
                                    messageId);
                        }
                    }
                    // Ce ne sont pas des erreurs mais des warnings
                    if (transaction.hasErrors()) {
                        // Parfois des messages sont remonté à double, on va donc tenter de les filtrer

                        String message = transaction.getErrors().toString();
                        List<String> fileredMessage = new ArrayList<String>();

                        // On try/catch afin d'assurer qu'une erreur à ce niveau (formattage des messages) ne casse pas
                        // l'exécution du processus
                        try {
                            // On recherche le caractère de saut de ligne pour voir si plusieurs messages sont présents
                            if (message.contains("\n")) {
                                String[] values = message.split("\n");
                                for (String val : values) {
                                    if (!JadeStringUtil.isBlank(val)) {
                                        if (!fileredMessage.contains(val)) {
                                            fileredMessage.add(val);
                                        }
                                    }
                                }
                            } else {
                                fileredMessage.add(message);
                            }

                            StringBuilder formatedMessage = new StringBuilder();
                            formatedMessage.append(process.getSession().getLabel("CP_WARNING") + " "
                                    + entity.getNumAffilie() + " : ");
                            for (int ctr = 0; ctr < fileredMessage.size(); ctr++) {
                                formatedMessage.append(fileredMessage.get(ctr));
                                if ((ctr + 1) < fileredMessage.size()) {
                                    formatedMessage.append(". ");
                                }
                            }
                            // Attention au factoring de l'append de l id communication, sans vérifié ou est utiliser la
                            // constante car on l'utilise avec une recherche contains.
                            result.addWarning(formatedMessage.toString() + ", "
                                    + CPXmlmlMappingCommunicationEnvoiProcess.CONCAT_ID_COMMUNICATION
                                    + entity.getIdCommunication());
                        } catch (Exception e) {
                            JadeLogger.error(this, e);
                        }

                        transaction.clearErrorBuffer();
                        communicationEnErreur.add(entity.getIdCommunication());
                    }
                }
                // Ajout des demandes en erreur pour avoir l'état "non envoyé" dans la liste
                else {
                    communicationEnErreur.add(entity.getIdCommunication());
                }
            }

            /*
             * Arrivé à ce stade la transaction est clean
             * Seul les erreurs de validations doivent dévalider le lot
             * Validation en envoi du fichier XML généré
             */
            // ---------------------------------------

            // Controle de l'état du SEDEX
            CPCommunicationFiscale commi = new CPCommunicationFiscale();
            commi.setSession(process.getSession());
            commi.setIdCommunication(entity.getIdCommunication());
            commi.retrieve(transaction);

            if (!commi.getIdMessageSedex().isEmpty()) {
                CPRejets rejet = new CPRejets();
                rejet.setSession(process.getSession());
                rejet.setIdDemande(commi.getId());
                rejet.setMessageId(commi.getIdMessageSedex());
                rejet.retrieve(transaction);

                if (CPRejets.CS_ETAT_TRAITE.equals(rejet.getEtat())) {
                    rejet.setEtat(CPRejets.CS_ETAT_ENVOYE);
                }
            }

            ArrayList<String> errorBuffer = new ArrayList<String>();
            CPSedexWriter myFile = new CPSedexWriter(comFis.getSession());
            myFile.validateAndSendFileXML(transaction, errorBuffer, message102, message101, comFis,
                    communicationTraitee, nbreCommunications);

            if (errorBuffer.size() > 0) {
                result.setDoRollback(true);
                result.addError(errorBuffer);
            }

            if (transaction.hasErrors()) {
                transaction.setRollbackOnly();
                result.setDoRollback(true);
                result.addError(transaction.getErrors().toString());
            }
            // ------------------------------------------

        } catch (Exception e) {
            StringBuilder infoSupplementaire = new StringBuilder();
            if (entity != null) {
                infoSupplementaire.append(process.getSession().getLabel("DETAIL_FISC_NE_TIERS_DESCRIPTION"));
                infoSupplementaire.append(" [");
                infoSupplementaire.append(entity.getIdTiers());
                infoSupplementaire.append("]. ");
                infoSupplementaire.append(process.getSession().getLabel("CP_MSG_0110"));
                infoSupplementaire.append(" [");
                infoSupplementaire.append(entity.getNumAffilie());
                infoSupplementaire.append("] : ");
                infoSupplementaire.append(e.toString());
                infoSupplementaire.append(", ");
                // Attention au factoring de l'append de l id communication, sans vérifié ou est utiliser la
                // constante car on l'utilise avec une recherche contains.
                infoSupplementaire.append(CPXmlmlMappingCommunicationEnvoiProcess.CONCAT_ID_COMMUNICATION
                        + entity.getIdCommunication());
                JadeLogger.error(this, new Exception(infoSupplementaire.toString(), e));

                // On met toutes les entités du lot en erreur, quand une erreur global a peter.
                for (int i = debutATraiter; i < finATraiter; i++) {
                    CPCommunicationFiscaleAffichage entityLocal = (CPCommunicationFiscaleAffichage) manager
                            .getEntity(i);
                    communicationEnErreur.add(entityLocal.getIdCommunication());
                }
            } else {
                JadeLogger.error(this, e);
            }
            transaction.setRollbackOnly();
            result.setDoRollback(true);
            result.addError(process.getSession().getLabel("CP_ERROR") + " " + e.toString() + " - " + infoSupplementaire);
        }
        return result;
    }

    public StringBuffer createFileXMLSedex(BTransaction transaction,
            CPCommunicationFiscaleAffichage communicationAffichage) {
        StringBuffer errorBuffer = new StringBuffer();
        ObjectFactory objFac101 = new ObjectFactory();
        try {
            CPDecision decision = null;
            String idConjoint = "";
            String idTiers = "";
            TITiersViewBean conjoint = null;
            TITiersViewBean tiers = null;
            AFAffiliation affiliationConjoint = null;
            AFAffiliation affiliation = null;
            boolean isMaried = false;
            String messageId = "" + JadeUUIDGenerator.createLongUID();

            idTiers = communicationAffichage.getIdTiers();
            affiliation = CPSedexWriter.loadAffiliation(communicationAffichage);
            // On recherche la décision qui a généré la demande
            // Utile pour retrouver l'idConjoint
            CPDecisionManager decM = findDecision(communicationAffichage);
            if (decM.size() == 0) {
                errorBuffer.append(communicationAffichage.getSession().getLabel("CP_SEDEX_MSG2")
                        + communicationAffichage.getIdCommunication() + " - " + affiliation.getAffilieNumero() + " - "
                        + communicationAffichage.getAnneeDecision());
                return errorBuffer;
            } else {
                decision = (CPDecision) decM.getFirstEntity();
                idConjoint = decision.getIdConjoint();
            }
            if ((JadeStringUtil.isBlankOrZero(idConjoint) == false) || decision.getDivision2().equals(Boolean.TRUE)) {
                isMaried = true;
            }
            // Selon les directives Sedex:
            // Le contribuable est toujours le mari ==> si une communication a un tiers
            // de sexe féminin et qui a un conjoint, on inverse les rôles, le contribuable passe conjoint (même
            // si l'homme est inconnu à la caisse)
            // Selon le mode VD -> Le tiers (homme ou femme) est le contribuable
            String modeEnvoiSedex = CPParametreCanton.findCodeWhitTypeAndCanton(communicationAffichage.getSession(),
                    communicationAffichage.getCanton(), CPParametreCanton.CS_MODE_ENVOI_SEDEX,
                    JACalendar.todayJJsMMsAAAA());
            boolean changeAffilie = false;
            if (!CPParametreCanton.CS_SEDEXENVOI_MODE_VD.equalsIgnoreCase(modeEnvoiSedex)
                    && communicationAffichage.getSexe().equalsIgnoreCase(TITiersViewBean.CS_FEMME)
                    && (!JadeStringUtil.isBlankOrZero(idConjoint) || decision.getDivision2().equals(Boolean.TRUE))) {
                idTiers = idConjoint;
                idConjoint = communicationAffichage.getIdTiers();
                affiliationConjoint = CPSedexWriter.loadAffiliation(communicationAffichage);
                affiliation = null;
                changeAffilie = true;
            }

            // Rechercher du tiers
            // 100 = id du tiers inconnu
            if (!JadeStringUtil.isBlankOrZero(idTiers) && !"100".equalsIgnoreCase(idTiers)) {
                tiers = CPSedexWriter.loadTiers(idTiers, communicationAffichage.getSession());
            }
            if (!JadeStringUtil.isBlankOrZero(idConjoint) && !"100".equalsIgnoreCase(idConjoint)) {
                conjoint = CPSedexWriter.loadTiers(idConjoint, communicationAffichage.getSession());
                // On va rechercher l'affiliation du conjoint si elle n'est pas renseignée (cas ou le tiers de
                // la décision est une femme)
                if (conjoint != null) {
                    if (!changeAffilie) {
                        affiliationConjoint = CPToolBox.returnAffiliation(communicationAffichage.getSession(),
                                transaction, conjoint.getIdTiers(), communicationAffichage.getAnneeDecision(), "", 1);
                    }
                }
            }

            // Si l'état du rejet n'est PAS 'non traité' ou 'abandonné' on continue le processus
            if (!isEtatRejetSedexNonTraiteAbandonne(communicationAffichage.getSession(),
                    communicationAffichage.getIdCommunication())) {
                // Création du message 101 - Format requis par Sedex pour envoi unitaire
                Message message101 = createMessage101(transaction, communicationAffichage, errorBuffer, objFac101,
                        conjoint, tiers, affiliationConjoint, affiliation, isMaried, messageId);
                // Mise à jour de la date d'envoi et création du lien entre le message Sedex et la communication
                CPSedexWriter.updateDateCommunicationEtLienSedex(transaction, message101, communicationAffichage,
                        messageId);

                if (transaction.hasErrors()) {
                    transaction.rollback();
                    if (!JadeStringUtil.isEmpty(errorBuffer.toString())) {
                        errorBuffer.append("\n");
                    }
                    errorBuffer.append(communicationAffichage.getSession().getLabel("CP_ERROR") + ": "
                            + transaction.getErrors());
                } else {
                    // On a une seule communication, on envoi un message101
                    // On extrait le message
                    managementMessage101(transaction, communicationAffichage, errorBuffer, affiliationConjoint,
                            affiliation, message101);
                }
            }
        } catch (Exception e) {
            if (!JadeStringUtil.isEmpty(errorBuffer.toString())) {
                errorBuffer.append("\n");
            }
            errorBuffer.append(communicationAffichage.getSession().getLabel("CP_ERROR") + ": " + e.toString());
        }
        return errorBuffer;
    }

    /***
     * Retourne un boolean indiquant si l'état d'un rejet est à l'état 'non traité' ou 'abandonné'
     * 
     * @author est
     * @param session
     * @param idCommunication
     * @return
     * @throws Exception
     */
    private boolean isEtatRejetSedexNonTraiteAbandonne(BSession session, String idCommunication) throws Exception {
        boolean isEtatRejetSedexNonTraiteAbandonne = false;

        CPRejetsManager rejetMng = new CPRejetsManager();
        rejetMng.setSession(session);
        rejetMng.setForIdCommunication(idCommunication);
        rejetMng.find(BManager.SIZE_NOLIMIT);

        try {
            for (int j = 0; j < rejetMng.getSize(); j++) {
                CPRejets rejet = new CPRejets();
                rejet.setSession(session);
                rejet.setIdRejets(((CPRejets) rejetMng.getEntity(j)).getIdRejets());
                rejet.retrieve();

                if (CPRejets.CS_ETAT_NON_TRAITE.equals(rejet.getEtat())
                        || CPRejets.CS_ETAT_ABANDONNE.equals(rejet.getEtat())) {
                    isEtatRejetSedexNonTraiteAbandonne = true;
                }

                else {
                    isEtatRejetSedexNonTraiteAbandonne = false;
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return isEtatRejetSedexNonTraiteAbandonne;
    }

    /*
     * Création du message 101 Format requis par Sedex pour envoi unique (deamdne individuelle)
     */
    protected Message createMessage101(BTransaction transaction,
            CPCommunicationFiscaleAffichage communicationAffichage, StringBuffer errorBuffer, ObjectFactory objFac101,
            TITiersViewBean conjoint, TITiersViewBean tiers, AFAffiliation affiliationConjoint,
            AFAffiliation affiliation, boolean isMaried, String messageId) {
        Message message101 = objFac101.createMessage();
        try {
            creationDemandeUnique(objFac101, message101, messageId, transaction, communicationAffichage, tiers,
                    affiliation, conjoint, affiliationConjoint, isMaried);
        } catch (Exception e) {
            String error = e.toString();
            saveErrorInBuffer(communicationAffichage, errorBuffer, affiliationConjoint, affiliation, error);
        }
        return message101;
    }

    private String creationDemande(ObjectFactory objFac101,
            ch.eahv_iv.xmlns.eahv_iv_2011_000102._3.ObjectFactory objFac102, Message message101,
            ch.eahv_iv.xmlns.eahv_iv_2011_000102._3.Message message102, BTransaction transaction,
            CPCommunicationFiscaleAffichage comFis, TITiersViewBean tiers, AFAffiliation affiliation,
            TITiersViewBean conjoint, AFAffiliation affiliationConjoint, boolean isMaried) throws Exception {
        String messageId = "" + JadeUUIDGenerator.createLongUID();
        // Création de l'entête du message Sedex
        setHeaderCommun(comFis.getSession(), transaction, objFac101, objFac102, message101, message102, messageId,
                comFis.getCanton());

        ch.eahv_iv.xmlns.eahv_iv_common._1.ObjectFactory objFacEahvIvCommon = new ch.eahv_iv.xmlns.eahv_iv_common._1.ObjectFactory();
        NaturalPersonsTaxReturnsOASIType obj101 = objFacEahvIvCommon.createNaturalPersonsTaxReturnsOASIType();
        message101.getHeader().setObject(obj101);
        NaturalPersonsTaxReturnsOASIType object101 = message101.getHeader().getObject();
        // ourBusinessReferenceID
        // OtherPersonId
        // PersonIdCategory
        // Person Id

        ch.ech.xmlns.ech_0044._1.ObjectFactory objFac44 = new ch.ech.xmlns.ech_0044._1.ObjectFactory();
        // Insérer l'information sur le numéro avs
        CPSedexWriter.insertInfoNumAvs(transaction, comFis, object101, objFac44);

        ch.ech.xmlns.ech_0010._3.ObjectFactory objFac10 = null;
        if (affiliation == null) {
            objFac10 = CPSedexWriter.setChampCommunHeader(comFis.getSession(), conjoint, tiers, affiliationConjoint,
                    transaction, object101, objFac44, isMaried);
        } else {
            objFac10 = CPSedexWriter.setChampCommunHeader(comFis.getSession(), tiers, conjoint, affiliation,
                    transaction, object101, objFac44, isMaried);
        }
        HeaderType header = message101.getHeader();
        header.setMessagePriority(BigInteger.valueOf(Long.valueOf(CPSedexWriter.MESSAGE_PRIORITY_NORMAL)));
        header.setEventPeriod(CPSedexWriter.getAnneeGregorian(comFis.getAnneeDecision()));
        // OurBusinessReference ID
        header.setOurBusinessReferenceID(comFis.getNumAffilie());
        // ---------------------------
        // Content
        // OderScope
        ContentType con = objFac101.createContentType();
        message101.setContent(con);
        ContentType content = message101.getContent();
        content.setOrderScope(BigInteger.valueOf(Long.valueOf(calculOrderScopeUnique(comFis, transaction))));
        // TaxPayer
        // Vn
        if (tiers != null) {
            NaturalPersonsTaxReturnsOASIType nat = objFacEahvIvCommon.createNaturalPersonsTaxReturnsOASIType();
            content.setTaxpayer(nat);
            CPSedexWriter.setTaxPayer(comFis.getSession(), tiers, conjoint, affiliation, transaction, isMaried,
                    comFis.getNumAffilie(), objFac44, objFac10, content);
        }
        if (conjoint != null) {
            CPSedexWriter.setSpouse(comFis.getSession(), conjoint, tiers, affiliationConjoint, comFis.getNumAffilie(),
                    transaction, objFacEahvIvCommon, objFac44, objFac10, content);
        }
        return messageId;
    }

    private void creationDemandeUnique(ObjectFactory objFac101, Message message101, String messageId,
            BTransaction transaction, CPCommunicationFiscaleAffichage comFis, TITiersViewBean tiers,
            AFAffiliation affiliation, TITiersViewBean conjoint, AFAffiliation affiliationConjoint, boolean isMaried)
            throws Exception {

        // Création de l'entête du message XML
        boolean rappel = !JadeStringUtil.isEmpty(comFis.getDateEnvoi());
        setHeaderMessage101(comFis.getSession(), objFac101, message101, messageId, rappel, comFis.getCanton());

        HeaderType header101 = message101.getHeader();
        ch.eahv_iv.xmlns.eahv_iv_common._1.ObjectFactory objFacEahvIvCommon = new ch.eahv_iv.xmlns.eahv_iv_common._1.ObjectFactory();
        NaturalPersonsTaxReturnsOASIType obj = objFacEahvIvCommon.createNaturalPersonsTaxReturnsOASIType();
        header101.setObject(obj);
        NaturalPersonsTaxReturnsOASIType object101 = header101.getObject();
        // ourBusinessReferenceID
        // OtherPersonId
        // Person Id
        ch.ech.xmlns.ech_0044._1.ObjectFactory objFacEch044 = new ch.ech.xmlns.ech_0044._1.ObjectFactory();

        NamedPersonIdType otherPersonId = objFacEch044.createNamedPersonIdType();

        setInfoNumAvs(transaction, comFis, object101, otherPersonId);

        ch.ech.xmlns.ech_0010._3.ObjectFactory objFacEch10 = null;
        if (affiliation == null) {
            objFacEch10 = CPSedexWriter.setChampCommunHeader(comFis.getSession(), conjoint, tiers, affiliationConjoint,
                    transaction, object101, objFacEch044, isMaried);
        } else {
            objFacEch10 = CPSedexWriter.setChampCommunHeader(comFis.getSession(), tiers, conjoint, affiliation,
                    transaction, object101, objFacEch044, isMaried);
        }
        if (isEnvoiImmediat()) {
            header101.setMessagePriority(BigInteger.valueOf(Long.valueOf(CPSedexWriter.MESSAGE_PRIORITY_HIGH)));
        } else {
            header101.setMessagePriority(BigInteger.valueOf(Long.valueOf(CPSedexWriter.MESSAGE_PRIORITY_NORMAL)));
        }
        header101.setEventPeriod(CPSedexWriter.getAnneeGregorian(comFis.getAnneeDecision()));
        // OurBusinessReference ID
        header101.setOurBusinessReferenceID(comFis.getNumAffilie());
        // ---------------------------
        // Content
        // OderScope
        ContentType con101 = objFac101.createContentType();
        message101.setContent(con101);
        ContentType content101 = message101.getContent();

        try {
            content101.setOrderScope(BigInteger.valueOf(Long.valueOf(calculOrderScopeUnique(comFis, transaction))));
        } catch (Exception e) {
            throw new Exception(transaction.getErrors().toString());
        }

        // TaxPayer
        // Vn
        if (tiers != null) {
            NaturalPersonsTaxReturnsOASIType nat = objFacEahvIvCommon.createNaturalPersonsTaxReturnsOASIType();
            content101.setTaxpayer(nat);
            CPSedexWriter.setTaxPayer(comFis.getSession(), tiers, conjoint, affiliation, transaction, isMaried,
                    comFis.getNumAffilie(), objFacEch044, objFacEch10, content101);
        }
        if (conjoint != null) {
            CPSedexWriter.setSpouse(comFis.getSession(), conjoint, tiers, affiliationConjoint, comFis.getNumAffilie(),
                    transaction, objFacEahvIvCommon, objFacEch044, objFacEch10, content101);
        }
    }

    protected CPDecisionManager findDecision(CPCommunicationFiscaleAffichage communicationAffichage) throws Exception {
        CPDecisionManager decM = new CPDecisionManager();
        decM.setSession(communicationAffichage.getSession());
        decM.setForIdAffiliation(communicationAffichage.getIdAffiliation());
        decM.setForAnneeDecision(communicationAffichage.getAnneeDecision());
        decM.setForIsActive(Boolean.TRUE);
        decM.find();
        return decM;
    }

    protected JadeSedexDirectory findSedexDirectory() {
        JadeSedexDirectory sedexDirectory = null;
        try {
            sedexDirectory = JadeSedexService.getInstance().getSedexDirectory();
        } catch (JadeSedexDirectoryInitializationException e) {
            // impossible de retrouver le recipient ID
            JadeLogger.error(this, e + " " + getSession().getLabel("CP_SEDEX_MSG14"));
        }
        return sedexDirectory;
    }

    public TIAdministrationViewBean getCaisse() {
        return caisse;
    }

    public ArrayList<String> getCommunicationEnErreur() {
        return communicationEnErreur;
    }

    public String getDate_impression() {
        return date_impression;
    }

    public BProcess getProcessAppelant() {
        return processAppelant;
    }

    public BSession getSession() {
        return session;
    }

    public String getUser() {
        return user;
    }

    public boolean isDonneesCommerciales() {
        return donneesCommerciales;
    }

    public boolean isDonneesPrivees() {
        return donneesPrivees;
    }

    public boolean isEnvoiImmediat() {
        return envoiImmediat;
    }

    public boolean isLifd() {
        return lifd;
    }

    protected void managementMessage101(BTransaction transaction,
            CPCommunicationFiscaleAffichage communicationAffichage, StringBuffer errorBuffer,
            AFAffiliation affiliationConjoint, AFAffiliation affiliation, Message message101) throws Exception {
        try {
            Class<?>[] classes = new Class<?>[] { ReportHeader.class, ContactRoot.class, EventReport.class,
                    PersonRoot.class, PermitRoot.class, MunicipalityRoot.class, CountryRoot.class,
                    PersonIdentificationRoot.class, MailAddressType.class, };
            /*
             * namespace="http://www.ech.ch/xmlns/eCH-0011/3"/> <xs:import
             */
            String monFichier = null;
            try {
                monFichier = JAXBServices.getInstance().marshal(message101, true, false, classes);
            } catch (JAXBValidationError e) {
                transaction.addErrors(communicationAffichage.getSession().getLabel("CP_SEDEX_MSG3"));
                if (e.getEvents().isEmpty() == false) {
                    transaction.addErrors(e.getEvents().get(0).getMessage());
                    String error = transaction.getErrors().toString();
                    saveErrorInBuffer(communicationAffichage, errorBuffer, affiliationConjoint, affiliation, error);
                }
            }
            JadeSedexService.getInstance().sendSimpleMessage(monFichier, null);
            transaction.commit();
        } catch (Exception e) {
            String error = transaction.getErrors().toString();
            saveErrorInBuffer(communicationAffichage, errorBuffer, affiliationConjoint, affiliation, error);
            communicationEnErreur.add(communicationAffichage.getIdCommunication());
            transaction.rollback();
        }
    }

    protected void saveErrorInBuffer(CPCommunicationFiscaleAffichage communicationAffichage, StringBuffer errorBuffer,
            AFAffiliation affiliationConjoint, AFAffiliation affiliation, String error) {
        if (!JadeStringUtil.isEmpty(errorBuffer.toString())) {
            errorBuffer.append("\n");
        }
        if (!affiliation.isNew()) {
            errorBuffer.append(communicationAffichage.getSession().getLabel("CP_ERROR") + ": "
                    + affiliation.getAffilieNumero() + " : " + error);
        } else if (!affiliationConjoint.isNew()) {
            errorBuffer.append(communicationAffichage.getSession().getLabel("CP_ERROR") + ": "
                    + affiliationConjoint.getAffilieNumero() + " : " + error);
        } else {
            errorBuffer.append(getSession().getLabel("CP_ERROR") + ": " + communicationAffichage.getIdCommunication()
                    + " : " + error);
        }
    }

    public void setCaisse(TIAdministrationViewBean caisse) {
        this.caisse = caisse;
    }

    public void setDate_impression(String string) {
        date_impression = string;
    }

    public void setDonneesCommerciales(boolean donneesCommerciales) {
        this.donneesCommerciales = donneesCommerciales;
    }

    public void setDonneesPrivees(boolean donneesPrivees) {
        this.donneesPrivees = donneesPrivees;
    }

    public void setEnvoiImmediat(boolean envoiImmediat) {
        this.envoiImmediat = envoiImmediat;
    }

    private void setHeaderCommun(BSession session, BTransaction transaction, ObjectFactory objFac101,
            ch.eahv_iv.xmlns.eahv_iv_2011_000102._3.ObjectFactory objFac102, Message message101,
            ch.eahv_iv.xmlns.eahv_iv_2011_000102._3.Message message102, String messageId, String canton) {

        setHeaderMessage101(session, objFac101, message101, messageId, false, canton);
        message102.setMinorVersion(BigInteger.valueOf(0));
        ch.eahv_iv.xmlns.eahv_iv_2011_000102._3.HeaderType hea102 = objFac102.createHeaderType();

        message102.setHeader(hea102);
        ch.eahv_iv.xmlns.eahv_iv_2011_000102._3.HeaderType headerType102 = message102.getHeader();

        setHeaderMessage102(message101, messageId, headerType102);
    }

    private void setHeaderMessage101(BSession session, ObjectFactory objFac101, Message message101, String messageId,
            boolean isDateEnvoi, String canton) {
        // HEADER
        // Sender Id
        message101.setMinorVersion(BigInteger.valueOf(0));
        HeaderType header = objFac101.createHeaderType();
        message101.setHeader(header);
        HeaderType headerType101 = message101.getHeader();
        // Recherche du répertoire Sedex
        JadeSedexDirectory sedexDirectory = findSedexDirectory();
        if (sedexDirectory != null) {
            headerType101.setSenderId(sedexDirectory.getLocalEntry().getId());
            if (headerType101.getSenderId().startsWith("T")) {
                CPSedexWriter.isCasTest = true;
            }
        }
        // DECLARATIONLOCALREFERENCE
        createDeclarationLocalReferenceForHeader101(session, headerType101);
        // Recipient Id
        if (CPSedexWriter.isCasTest) {
            headerType101.getRecipientId().add("T2-" + session.getCode(canton) + "-5");
        } else {
            headerType101.getRecipientId().add("2-" + session.getCode(canton) + "-5");
        }
        // Message Id
        headerType101.setMessageId(messageId);
        // Message Type
        headerType101.setMessageType(Integer.valueOf(CPSedexWriter.getMessageType()));
        // Sub Message Type
        headerType101.setSubMessageType(CPSedexWriter.getSubMessageType());
        // Sending Application
        // Manufacturer
        ch.ech.xmlns.ech_0058._2.ObjectFactory objFacech58 = new ch.ech.xmlns.ech_0058._2.ObjectFactory();
        SendingApplicationType send = objFacech58.createSendingApplicationType();
        headerType101.setSendingApplication(send);
        SendingApplicationType sendingApplication = headerType101.getSendingApplication();
        sendingApplication.setManufacturer("");
        // Product
        sendingApplication.setProduct("");
        // Product Version
        sendingApplication.setProductVersion("");
        // Subject
        headerType101.setSubject(CPSedexWriter.getSubject());
        // Message Date
        headerType101.setMessageDate(CPSedexWriter.getDateActuelleGregorian());
        if (isDateEnvoi) {
            headerType101.setAction(CPSedexWriter.ACTION_CODE_RAPPEL);
        } else {
            headerType101.setAction(CPSedexWriter.ACTION_CODE_DEMANDE);
        }
        // test Delivery Flag
        headerType101.setTestDeliveryFlag(Boolean.valueOf(JadeSedexService.getInstance().getTestDeliveryFlag())
                .booleanValue());
        // Information sur la caisse de compensation
        setInfoCaisse(session, headerType101);
    }

    protected void setHeaderMessage102(Message message101, String messageId,
            ch.eahv_iv.xmlns.eahv_iv_2011_000102._3.HeaderType headerType102) throws NumberFormatException {
        headerType102.setSenderId(message101.getHeader().getSenderId());
        // DECLARATIONLOCALREFERENCE
        headerType102.setDeclarationLocalReference(message101.getHeader().getDeclarationLocalReference());
        // Recipient Id
        headerType102.getRecipientId().addAll(message101.getHeader().getRecipientId());
        // Message Id
        headerType102.setMessageId(messageId);

        // Message Type
        headerType102.setMessageType(Integer.valueOf(CPSedexWriter.getMessageType()));
        // Sub Message Type
        headerType102.setSubMessageType(CPSedexWriter.getSubMessageType102());
        // Sending Application
        // Manufacturer
        ch.ech.xmlns.ech_0058._2.ObjectFactory objFac58 = new ch.ech.xmlns.ech_0058._2.ObjectFactory();
        SendingApplicationType sen102 = objFac58.createSendingApplicationType();
        headerType102.setSendingApplication(sen102);
        SendingApplicationType sendingApplication102 = headerType102.getSendingApplication();
        sendingApplication102.setManufacturer("");
        // Product
        sendingApplication102.setProduct("");
        // Product Version
        sendingApplication102.setProductVersion("");
        // Subject
        headerType102.setSubject(CPSedexWriter.getSubject());

        headerType102.setMessageDate(CPSedexWriter.getDateActuelleGregorian());

        headerType102.setAction(CPSedexWriter.ACTION_CODE_DEMANDE);

        headerType102.setTestDeliveryFlag(JadeSedexService.getInstance().getTestDeliveryFlag());
    }

    protected void setInfoCaisse(BSession session, HeaderType header) {
        // postalAddress obligatoire
        ch.ech.xmlns.ech_0010._3.ObjectFactory objFacEch10 = new ch.ech.xmlns.ech_0010._3.ObjectFactory();
        OrganisationMailAddressType post = objFacEch10.createOrganisationMailAddressType();
        OrganisationMailAddressInfoType org = objFacEch10.createOrganisationMailAddressInfoType();
        AddressInformationType adr = objFacEch10.createAddressInformationType();
        post.setOrganisation(org);
        post.setAddressInformation(adr);
        header.setPostalAddress(post);

        OrganisationMailAddressInfoType organisation = header.getPostalAddress().getOrganisation();
        if (getCaisse() != null) {
            // Organisation
            organisation.setOrganisationName(CPSedexWriter.replaceECommercial(getCaisse().getDesignation1()));
            organisation.setOrganisationNameAddOn1(CPSedexWriter.replaceECommercial(getCaisse().getDesignation2()));
            organisation.setOrganisationNameAddOn2(CPSedexWriter.replaceECommercial(getCaisse().getDesignation3()));
            // AddressInformation
            OrganisationMailAddressType postalAddress = header.getPostalAddress();
            AddressInformationType addressInformation = postalAddress.getAddressInformation();
            addressInformation.setAddressLine1(CPSedexWriter.replaceECommercial(getCaisse().getDesignation1()));
            addressInformation.setAddressLine2(CPSedexWriter.replaceECommercial(getCaisse().getDesignation2()));
            try {
                TIAdresseDataSource ds = getCaisse().getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                        "519005", JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY), true);
                if (ds == null) {
                    ds = getCaisse().getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_COURRIER, "519005",
                            JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY), true);
                }
                if (ds != null) {
                    Hashtable<?, ?> data = ds.getData();
                    addressInformation.setStreet(CPSedexWriter.replaceECommercial(data.get(
                            TIAbstractAdresseDataSource.ADRESSE_VAR_RUE).toString()));
                    addressInformation.setHouseNumber(CPSedexWriter.replaceECommercial12(data.get(
                            TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO).toString()));
                    addressInformation.setTown(CPSedexWriter.replaceECommercial(data.get(
                            TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE).toString()));
                    addressInformation.setSwissZipCode(Long.valueOf(CPSedexWriter.replaceECommercial(data.get(
                            TIAbstractAdresseDataSource.ADRESSE_VAR_NPA).toString())));
                    addressInformation.setCountry(CPSedexWriter.replaceECommercial(data.get(
                            TIAbstractAdresseDataSource.ADRESSE_VAR_PAYS_ISO).toString()));
                    header.getPostalAddress().setAddressInformation(addressInformation);
                }
            } catch (Exception e) {
                // On ne fait rien ces données ne sont pas obligatoires
            }
        } else {
            organisation.setOrganisationName(" ");
        }
        header.getPostalAddress().setOrganisation(organisation);
    }

    protected void setInfoNumAvs(BTransaction transaction, CPCommunicationFiscaleAffichage comFis,
            NaturalPersonsTaxReturnsOASIType object101, NamedPersonIdType otherPersonId) throws IOException, Exception {
        // Formatage du numéro avs
        String numAvs = CPSedexWriter.formatNumAVS(comFis.getTiers(), transaction);
        if (NSUtil.unFormatAVS(numAvs).length() == 11) {
            otherPersonId.setPersonId(numAvs);
            otherPersonId.setPersonIdCategory(CPSedexWriter.CH_AHV);
            object101.getOtherPersonId().add(otherPersonId);
        }
    }

    public void setLifd(boolean lifd) {
        this.lifd = lifd;
    }

    public void setProcessAppelant(BProcess processAppelant) {
        this.processAppelant = processAppelant;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setUser(String string) {
        user = string;
    }

    protected void validateAndSendFileXML(BTransaction transaction, List<String> errorBuffer,
            ch.eahv_iv.xmlns.eahv_iv_2011_000102._3.Message message102, Message message101,
            CPCommunicationFiscale comFis, ArrayList<String> communicationTraitee, int nbreCommunications) {
        // Si une seule communication : message101 sinon message102
        try {
            CPSedexWriter myFile = new CPSedexWriter(comFis.getSession());
            String monFichier = myFile.validateFileXML(transaction, errorBuffer, message102, message101,
                    communicationTraitee, nbreCommunications);
            // Envoi du fichier
            JadeSedexService.getInstance().sendSimpleMessage(monFichier, null);
        } catch (Exception e) {
            if (!JadeStringUtil.isEmpty(comFis.getNumAffilie())) {
                // Erreur pendant l'envoi !
                transaction.addErrors(comFis.getNumAffilie() + " " + comFis.getSession().getLabel("SEDEX_ENVOI_KO")
                        + e.getMessage() + " " + comFis.getNumAffilie());
            } else {
                transaction.addErrors(comFis.getSession().getLabel("SEDEX_ENVOI_KO") + e.getMessage());
            }
            communicationEnErreur.addAll(communicationTraitee);

        }
    }

    protected String validateFileXML(BTransaction transaction, List<String> errorBuffer,
            ch.eahv_iv.xmlns.eahv_iv_2011_000102._3.Message message102, Message message101,
            ArrayList<String> communicationTraitee, int nbreCommunications) throws JAXBException, SAXException,
            MalformedURLException, IOException, JAXBValidationWarning {
        String monFichier = null;
        try {
            if (nbreCommunications == 1) {
                // Controle du format pour fichier unitaire
                Class<?>[] classes = new Class<?>[] { ReportHeader.class, ContactRoot.class, EventReport.class,
                        PersonRoot.class, PermitRoot.class, MunicipalityRoot.class, CountryRoot.class,
                        PersonIdentificationRoot.class, MailAddressType.class, };
                monFichier = JAXBServices.getInstance().marshal(message101, true, false, classes);
            } else {
                // Controle du format pour fichier de masse
                Class<?>[] classes = new Class<?>[] { Message.class, ReportHeader.class, ContactRoot.class,
                        EventReport.class, PersonRoot.class, PermitRoot.class, MunicipalityRoot.class,
                        CountryRoot.class, PersonIdentificationRoot.class, MailAddressType.class, };
                monFichier = JAXBServices.getInstance().marshal(message102, true, false, classes);
            }
        } catch (JAXBValidationError e) {
            if (e.getEvents().isEmpty() == false) {
                transaction.addErrors(e.getEvents().get(0).getMessage());
                errorBuffer.add(e.getEvents().get(0).getMessage());
            }
            communicationEnErreur.addAll(communicationTraitee);
        }
        return monFichier;
    }
}
