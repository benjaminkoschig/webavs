package globaz.pavo.process;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.google.common.base.Splitter;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaire;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireType;
import ch.globaz.orion.service.EBEbusinessInterface;
import ch.globaz.orion.service.EBPucsFileService;
import ch.swissdec.schema.sd._20130514.salarydeclarationconsumercontainer.DeclareSalaryConsumerType;
import ch.swissdec.schema.sd._20130514.salarydeclarationcontainer.SalaryDeclarationRequestType;
import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.inscriptions.DSDeclarationListeManager;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuelles;
import globaz.draco.db.inscriptions.DSInscriptionsIndividuellesListeViewBean;
import globaz.draco.db.inscriptions.DSValideMontantDeclarationProcess;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.releve.AFApercuReleve;
import globaz.naos.db.releve.AFApercuReleveManager;
import globaz.naos.db.releve.AFApercuReleveMontant;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.inscriptions.CIJournal;
import globaz.pavo.db.inscriptions.CIJournalManager;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationHTMLOutput;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationPUCSIterator;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationParametreIterator;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationRecord;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationSummaryHTMLOutput;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationSummaryHTMLOutputCotPers;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationTextIterator;
import globaz.pavo.db.inscriptions.declaration.CIDeclarationsAnciensClientsIterator;
import globaz.pavo.db.inscriptions.declaration.ICIDeclarationIterator;
import globaz.pavo.db.inscriptions.declaration.ICIDeclarationOutput;
import globaz.pavo.util.CIUtil;
import globaz.webavs.common.CommonExcelmlContainer;

public class CIDeclaration extends BProcess {

    private static final String PUCS4_NAMESPACE = "http://www.swissdec.ch/schema/sd/20130514";

    private static final long serialVersionUID = 8208343321214530414L;
    public static String CS_AC = "327003";
    public static final String CS_AC_XML = "327019";
    public static String CS_AMI = "327002";
    public static String CS_CLIENTS_GLOBAZ = "327001";
    public static String CS_COT_PERS = "327017";
    public static String CS_DATEN_TRAGER = "327000";
    public static String CS_PUCS = "327004";
    public static String CS_PUCS_CCJU = "327015";
    public static String CS_PUCS_II = "327007";
    private static EBEbusinessInterface ebusinessAccessInstance = null;
    private boolean traitementAFSeul = false;
    private DSValideMontantDeclarationProcess theCalculMasseProcess;

    public static String getNomFormatCI(String nomPrenom) {
        if (JadeStringUtil.isEmpty(nomPrenom)) {
            // tel quel
            return nomPrenom;
        }

        nomPrenom = JadeStringUtil.change(nomPrenom, "ä", "AE");
        nomPrenom = JadeStringUtil.change(nomPrenom, "ë", "E");
        nomPrenom = JadeStringUtil.change(nomPrenom, "ï", "I");
        nomPrenom = JadeStringUtil.change(nomPrenom, "ö", "OE");
        nomPrenom = JadeStringUtil.change(nomPrenom, "ü", "UE");

        nomPrenom = JadeStringUtil.change(nomPrenom, "Ä", "AE");
        nomPrenom = JadeStringUtil.change(nomPrenom, "Ë", "E");
        nomPrenom = JadeStringUtil.change(nomPrenom, "Ï", "I");
        nomPrenom = JadeStringUtil.change(nomPrenom, "Ö", "OE");
        nomPrenom = JadeStringUtil.change(nomPrenom, "Ü", "UE");

        nomPrenom = JadeStringUtil.change(nomPrenom, "é", "E");
        nomPrenom = JadeStringUtil.change(nomPrenom, "è", "E");
        nomPrenom = JadeStringUtil.change(nomPrenom, "ô", "O");
        nomPrenom = JadeStringUtil.change(nomPrenom, "à", "A");

        nomPrenom = nomPrenom.trim().toUpperCase();

        return nomPrenom;
    }

    public static void initEbusinessAccessInstance(EBEbusinessInterface instance) {
        if (CIDeclaration.ebusinessAccessInstance == null) {
            CIDeclaration.ebusinessAccessInstance = instance;
        }
    }

    public static boolean isAvs0(String avs) {
        if (avs == null) {
            return true;
        }
        try {
            if (Integer.parseInt(avs.trim()) == 0) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean isPUCS4 = false;

    public boolean isPUCS4() {
        return isPUCS4;
    }

    public void setPUCS4(boolean isPUCS4) {
        this.isPUCS4 = isPUCS4;
    }

    private String accepteAnneeEnCours = "";

    private String accepteEcrituresNegatives = "";

    private String accepteLienDraco = "";
    private String anneeCotisation = "";
    private String dateReceptionForced = "";
    private String filename = "";
    private String forNumeroAffilie = "";
    private boolean hasChild = false;
    private TreeMap<String, String> hJournalExisteDeja = new TreeMap<String, String>();
    private TreeMap<String, Object> hMontantInscriptionsCI = new TreeMap<String, Object>();
    private TreeMap<String, Object> hMontantInscriptionsErreur = new TreeMap<String, Object>();
    private TreeMap<String, Object> hMontantInscriptionsNegatives = new TreeMap<String, Object>();
    private TreeMap<String, Object> hMontantInscriptionsSuspens = new TreeMap<String, Object>();
    private TreeMap<String, Object> hMontantInscritionsTraites = new TreeMap<String, Object>();
    private TreeMap<String, Object> hMontantTotalControle = new TreeMap<String, Object>();
    private TreeMap<String, Object> hNbrInscriptionsCI = new TreeMap<String, Object>();
    private TreeMap<String, Object> hNbrInscriptionsErreur = new TreeMap<String, Object>();
    private TreeMap<String, Object> hNbrInscriptionsNegatives = new TreeMap<String, Object>();
    private TreeMap<String, Object> hNbrInscriptionsSuspens = new TreeMap<String, Object>();
    private TreeMap<String, Object> hNbrInscriptionsTotalControle = new TreeMap<String, Object>();
    private TreeMap<String, Object> hNbrInscriptionsTraites = new TreeMap<String, Object>();
    private String idsPucsFile = null;
    private Boolean isBatch = new Boolean(false);
    private boolean isErrorMontant = false;

    private boolean isErrorNbInscriptions = false;
    private CIImportPucsFileProcess launcherImportPucsFileProcess = null;
    private TreeMap<String, Object> nbInsc = null;
    private boolean hasDifferenceAc = false;

    private String nombreInscriptions = "";
    private String numAffilieBase = "";
    private String provenance = "";
    private String simulation = "";
    private String source = "";
    // Cette table contient les journaux déjà crée
    // si le journal existait déjà avant le traitement, la clé est quand même mise dans cette table et la valeur sera
    // null.
    private TreeMap<String, Object> totauxJournaux = null;
    private final TreeMap<String, Object> tableJournaux = new TreeMap<String, Object>();
    // table pour stocké les erreurs/info au niveau des assuré (detail)
    private final TreeMap<String, CIDeclarationRecord> tableLogAssure = new TreeMap<String, CIDeclarationRecord>();
    private String titreLog = "";

    private long totalAvertissement = 0;
    private String totalControle = "";

    private long totalErreur = 0;

    private long totalTraite = 0;

    private String Type = "";
    private boolean validationAutomatique = false;

    private DSDeclarationViewBean declaration = null;

    private DeclarationSalaireType declarationSalaireType = DeclarationSalaireType.PRINCIPALE;
    private String anneeVersement;

    private CIImportPucs4Process importPucs4Process;

    public CIDeclaration() {
        super();
    }

    public CIDeclaration(BProcess parent) {
        super(parent);
    }

    public CIDeclaration(globaz.globall.db.BSession session) {
        super(session);
    }

    private void _doLogJournauxExistant(TreeMap<String, String> hJournalExisteDeja) {
        Set<String> journalSet = hJournalExisteDeja.keySet();
        Iterator<String> it = journalSet.iterator();
        while (it.hasNext()) {
            String str = it.next();
            getMemoryLog().logMessage(hJournalExisteDeja.get(str), FWMessage.ERREUR, "Déclaration");
        }
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    private void setIsPUCS4AttributeReadingFileNamespace() {

        try {

            Element element = getSoapBodyPayloadElement(resolveFileName());

            isPUCS4 = StringUtils.startsWith(element.getNamespaceURI(), PUCS4_NAMESPACE);

        } catch (Exception e) {
            isPUCS4 = false;
        }

    }

    protected Element getSoapBodyPayloadElement(String filePath)
            throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            Document doc = dbf.newDocumentBuilder().parse(fileInputStream);

            NodeList nodes = doc.getElementsByTagNameNS(
                    "http://www.swissdec.ch/schema/sd/20130514/SalaryDeclarationConsumerServiceTypes", "*");

            if (nodes == null || nodes.getLength() == 0) {
                nodes = doc.getElementsByTagNameNS(
                        "http://www.swissdec.ch/schema/sd/20130514/SalaryDeclarationServiceTypes", "*");
            }

            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE
                        && ("DeclareSalaryConsumer".equalsIgnoreCase(nodes.item(i).getLocalName())
                                || "DeclareSalary".equalsIgnoreCase(nodes.item(i).getLocalName()))) {
                    return (Element) nodes.item(i);
                }
            }
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }

        }

        return null;
    }

    private DeclareSalaryConsumerType unmarshallDeclareSalaryConsumerTypeFromSoapBody(String path)
            throws SAXException, IOException, ParserConfigurationException, JAXBException {
        Element element = getSoapBodyPayloadElement(path);

        JAXBContext jc = JAXBContext.newInstance(DeclareSalaryConsumerType.class);
        DeclareSalaryConsumerType valueDeclareSalaryConsumerType = jc.createUnmarshaller()
                .unmarshal(element, DeclareSalaryConsumerType.class).getValue();

        if (valueDeclareSalaryConsumerType.getDeclareSalary() == null) {
            addDeclareSalary(element, valueDeclareSalaryConsumerType);
        }
        return valueDeclareSalaryConsumerType;
    }

    /***
     * Ajout les informations de la balise DeclareSalary si le fichier SwissDec ne contient pas de balise
     * DeclareSalaryConsumer
     *
     * @param element
     * @param valueDeclareSalaryConsumerType
     * @throws JAXBException
     */
    private void addDeclareSalary(Element element, DeclareSalaryConsumerType valueDeclareSalaryConsumerType)
            throws JAXBException {
        JAXBContext jcSalaryDeclarationType = JAXBContext.newInstance(SalaryDeclarationRequestType.class);
        SalaryDeclarationRequestType valueSalaryDeclarationRequestType = jcSalaryDeclarationType.createUnmarshaller()
                .unmarshal(element, SalaryDeclarationRequestType.class).getValue();

        valueDeclareSalaryConsumerType.setDeclareSalary(valueSalaryDeclarationRequestType);
    }

    private DeclarationSalaire convertPucs4FileToDeclarationSalaire()
            throws SAXException, IOException, ParserConfigurationException, JAXBException {

        DeclareSalaryConsumerType value = unmarshallDeclareSalaryConsumerTypeFromSoapBody(resolveFileName());
        PUCS4SalaryConverter salaryConverterPUCS4 = new PUCS4SalaryConverter();

        return salaryConverterPUCS4.convert(value);

    }

    private String resolveFileName() {

        if (!isBatch) {
            return Jade.getInstance().getHomeDir() + "work/" + getFilename();
        }
        return getFilename();

    }

    private boolean executeImportPucs4Process() throws Exception {

        importPucs4Process = new CIImportPucs4Process();
        importPucs4Process.setSession(getSession());
        importPucs4Process.setEMailAddress(getEMailAddress());
        importPucs4Process.setDeclarationSalaire(convertPucs4FileToDeclarationSalaire());

        importPucs4Process.setFilename(resolveFileName());

        importPucs4Process.setDateReceptionForced(getDateReceptionForced());
        importPucs4Process.setSimulation(getSimulation());
        importPucs4Process.setProvenance(getProvenance());
        importPucs4Process.setAccepteAnneeEnCours(getAccepteAnneeEnCours());
        importPucs4Process.setAccepteEcrituresNegatives(getAccepteEcrituresNegatives());
        importPucs4Process.setAccepteLienDraco(getAccepteLienDraco());
        importPucs4Process.setLauncherImportPucsFileProcess(getLauncherImportPucsFileProcess());
        importPucs4Process.setTotalControle(getTotalControle());
        importPucs4Process.setNombreInscriptions(getNombreInscriptions());
        importPucs4Process.setNumAffilieBase(getNumAffilieBase());
        importPucs4Process.setType(getType());
        importPucs4Process.setIdsPucsFile(getIdsPucsFile());

        CIImportPucs4Process.initEbusinessAccessInstance(ebusinessAccessInstance);

        importPucs4Process.executeProcess();

        hasDifferenceAc = importPucs4Process.hasDifferenceAc();
        declaration = importPucs4Process.getDeclaration();
        setNumAffilieBase(importPucs4Process.getNumAffilieBase());

        setSendCompletionMail(false);
        setSendMailOnError(false);

        if (isAborted()) {
            return executeAnnulationTraitement(JadeStringUtil.isBlankOrZero(getSimulation()),
                    importPucs4Process.getTableJournaux());
        }

        return true;
    }

    public boolean isImportPucs4OnError() {
        return importPucs4Process != null && importPucs4Process.isMemoryLogEnErreur();
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        CIJournal journalCotPersRetro = null;
        CIJournal journalCotPersAnneeEnCours = null;

        if (!isBatch.booleanValue()) {
            JadeFsFacade.copyFile("jdbc://" + Jade.getInstance().getDefaultJdbcSchema() + "/" + getFilename(),
                    Jade.getInstance().getHomeDir() + "work/" + getFilename());
        }

        if (CIDeclaration.CS_COT_PERS.equals(getType())) {
            titreLog = "CI - Cotisations personnelles";
        } else {
            titreLog = "Déclaration";
        }
        if (CIDeclaration.CS_AC.equals(getType()) || CIDeclaration.CS_AMI.equals(getType())
                || CIDeclaration.CS_AC_XML.equals(getType())) {
            return declarationCentrale();
        } else {

            boolean modeInscription = JadeStringUtil.isBlankOrZero(getSimulation());
            boolean result = true;
            int line = 0;
            if (!modeInscription) {
                getMemoryLog().logMessage(getSession().getLabel("DT_MODE_SIMULATION"), FWMessage.INFORMATION, titreLog);
            }
            try {

                CIApplication app = (CIApplication) GlobazServer.getCurrentSystem()
                        .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO);

                if ("true".equalsIgnoreCase(app.getPropertyActiverParsingPUCS4())
                        && CIDeclaration.CS_PUCS_II.equalsIgnoreCase(getType())) {

                    setIsPUCS4AttributeReadingFileNamespace();
                    if (isPUCS4) {
                        return executeImportPucs4Process();
                    }

                }

                String forNumeroAffilieSansPoint = CIUtil.UnFormatNumeroAffilie(getSession(), getForNumeroAffilie());
                ICIDeclarationIterator itDec = null;
                if (CIDeclaration.CS_PUCS.equals(Type) || CIDeclaration.CS_PUCS_II.equals(Type)
                        || CIDeclaration.CS_PUCS_CCJU.equals(Type)) {
                    itDec = new CIDeclarationPUCSIterator();
                    itDec.setSession(getSession());
                    itDec.setTypeImport(Type);
                } else if (CIDeclaration.CS_CLIENTS_GLOBAZ.equals(Type)) {
                    itDec = new CIDeclarationsAnciensClientsIterator();
                    itDec.setSession(getSession());
                } else if (CIDeclaration.CS_DATEN_TRAGER.equals(Type)) {
                    itDec = new CIDeclarationTextIterator();
                    itDec.setSession(getSession());
                } else {
                    itDec = new CIDeclarationParametreIterator();
                    itDec.setSession(getSession());
                    itDec.setTypeImport(Type);
                }
                if (!isBatch.booleanValue()) {
                    itDec.setFilename(Jade.getInstance().getHomeDir() + "work/" + getFilename());
                } else {
                    itDec.setFilename(getFilename());
                }
                itDec.setProvenance(provenance);
                itDec.setTransaction(getTransaction());
                int nbRecords = itDec.size();
                if (JadeStringUtil.isBlankOrZero(dateReceptionForced)) {

                    dateReceptionForced = itDec.getDateReception();

                }
                // Pour le type SwissDec, prendre la date du jour
                if (JadeStringUtil.isBlankOrZero(dateReceptionForced)) {
                    dateReceptionForced = JACalendar.todayJJsMMsAAAA();
                }
                // SI size = 0 et ccju, on fait la réception du fichier
                if (CIDeclaration.CS_PUCS_CCJU.equals(Type) && (nbRecords == 0) && modeInscription) {
                    TreeMap<?, ?> mapRec = itDec.getNoAffiliePourReception();
                    receptionDS(mapRec, tableJournaux, false);

                    modeInscription = false;

                }
                setState("");
                setProgressScaleValue(nbRecords);
                CIDeclarationRecord rec = null;
                Map<String, String> totalParCanton = new HashMap<String, String>();
                AFAffiliation affilie = null;
                while (itDec.hasNext() && !getTransaction().hasErrors()) {

                    line++;
                    setProgressCounter(line);

                    rec = itDec.next();
                    traitementAFSeul = false;
                    ArrayList<String> errors = new ArrayList<String>();
                    ArrayList<String> info = new ArrayList<String>();
                    ArrayList<String> ciAdd = new ArrayList<String>();

                    affilie = app.getAffilieByNo(getSession(),
                            CIUtil.formatNumeroAffilie(getSession(), rec.getNumeroAffilie()), true, false,
                            String.valueOf(rec.getMoisDebut()), String.valueOf(rec.getMoisFin()), rec.getAnnee(),
                            String.valueOf(rec.getJourDebut()), String.valueOf(rec.getJourFin()));
                    if (rec.getCategorieAffilie().equalsIgnoreCase("AF")) {
                        if (launcherImportPucsFileProcess != null) {
                            launcherImportPucsFileProcess.setTraitementAFSeul(true);
                        }

                        traitementAFSeul = true;
                        if (affilie != null) {
                            // Cumul par canton
                            if (totalParCanton.containsKey(rec.getCodeCanton())) {
                                // Cumul du montant
                                FWCurrency cumul = new FWCurrency(totalParCanton.get(rec.getCodeCanton()));
                                cumul.add(rec.getMontantAf());
                                totalParCanton.put(rec.getCodeCanton(), cumul.toString());
                            } else {
                                totalParCanton.put(rec.getCodeCanton(), rec.getMontantAf());
                            }
                        } else {
                            if (launcherImportPucsFileProcess != null) {
                                launcherImportPucsFileProcess.getMemoryLog().logMessage(
                                        getSession().getLabel("MSG_AFFILIE_NON_VALIDE") + " - Affilié  "
                                                + rec.getNumeroAffilie() + " - Année " + rec.getAnnee(),
                                        FWMessage.ERREUR, "");
                            }

                        }
                    }
                    if (CIDeclaration.CS_PUCS_II.equals(Type) || CIDeclaration.CS_PUCS_CCJU.equals(Type)) {
                        if (JadeStringUtil.isBlank(numAffilieBase)) {
                            numAffilieBase = CIUtil.formatNumeroAffilie(getSession(), rec.getNumeroAffilie());
                        }
                    }

                    setProgressDescription(rec.getNumeroAvs() + "<br>" + line + "/" + nbRecords + "<br>");
                    if (isAborted()) {
                        return executeAnnulationTraitement(modeInscription);
                    }
                    // si l'année de cotisation n'est pas précisé, ou quelle correspond a celle de ce record
                    if ((JadeStringUtil.isEmpty(getAnneeCotisation()))
                            || (rec.getAnnee().equals(getAnneeCotisation()))) {
                        // si le n°d'affilie n'est pas précisé, ou qu'il correspond a celui de ce record
                        if ((JadeStringUtil.isEmpty(forNumeroAffilieSansPoint))
                                || (rec.getNumeroAffilie().equals(forNumeroAffilieSansPoint))) {
                            // initialisations
                            CIEcriture ecriture = new CIEcriture();

                            // BZ 9227 : Workaround pour faire passer écritures CCB
                            if (!CIDeclaration.CS_COT_PERS.equals(getType())) {
                                ecriture.setForAffilieParitaire(true);
                            }
                            if (!"true".equals(getAccepteLienDraco())) {
                                ecriture.setSession((BSession) getSessionCI(getSession()));
                            } else {
                                ecriture.setSession(getSession());
                            }
                            // pour stockage intermédiaire
                            long nbrInscriptionsTraites = 0;
                            FWCurrency montantInscritionsTraites = new FWCurrency();
                            long nbrInscriptionsErreur = 0;
                            FWCurrency montantInscriptionsErreur = new FWCurrency();
                            long nbrInscriptionsSuspens = 0;
                            FWCurrency montantInscriptionsSuspens = new FWCurrency();
                            long nbrInscriptionsCI = 0;
                            FWCurrency montantInscriptionsCI = new FWCurrency();
                            long nbrInscriptionsNegatives = 0;
                            FWCurrency montantInscriptionsNegatives = new FWCurrency();
                            long nbrInscriptionsTotalControle = 0;
                            FWCurrency montantTotalControle = new FWCurrency();

                            if (traitementAFSeul && rec.getNomAffilie().equalsIgnoreCase(
                                    getTransaction().getSession().getLabel("DT_AUNCUNE_PERIODE_AFFILIE"))) {
                                errors.add(getSession().getLabel("MSG_AFFILIE_NON_VALIDE") + "  - Affilié "
                                        + rec.getNumeroAffilie() + " - année " + rec.getAnnee());
                            }

                            // Pour les déclarations de salaire:
                            // trouver le journal à utiliser pour ce record.
                            // il y a un journal par année/affilié.
                            // si le journal n'existe pas on le crée et on le garde dans une table car
                            // il peut être utilisé par plusieurs ligne du fichier.
                            // si le journal existe préalablement au traitement du fichier, on génère une erreur.
                            // findJournal retourne le journal à utiliser, ou null si le jounal à utilisé existait déjà
                            // avant ce traitement,
                            // ce qui n'est pas autorisé.
                            // Pour les cot. pers: les inscriptions qui concernent l'année en cours vont dans le journal
                            // de l'année en cours, les autres sont dans le même journal.
                            CIJournal journal = null;
                            String key = "";
                            if (!traitementAFSeul) {
                                if (!CIDeclaration.CS_COT_PERS.equals(Type)) {
                                    journal = this._findJournal(modeInscription, rec, tableJournaux);
                                } else {
                                    // Si année = année en cours
                                    if (Integer.parseInt(rec.getAnnee()) == JACalendar
                                            .getYear(JACalendar.todayJJsMMsAAAA())) {
                                        if (journalCotPersAnneeEnCours == null) {
                                            journalCotPersAnneeEnCours = this._findJournal(true, modeInscription);
                                        }
                                        journal = journalCotPersAnneeEnCours;
                                    } else {
                                        if (journalCotPersRetro == null) {
                                            journalCotPersRetro = this._findJournal(false, modeInscription);
                                        }
                                        journal = journalCotPersRetro;
                                    }
                                }
                                if (!CIDeclaration.CS_COT_PERS.equals(Type)) {
                                    key = _getKey(rec);
                                } else {
                                    key = journal.getIdJournal();
                                }
                            }
                            if (journal == null && !traitementAFSeul) {
                                // Erreur, ce journal existe déjà avant ce traitement
                                /*
                                 * hJournalExisteDeja.put(key,
                                 * getSession().getLabel("DT_JOURNAL_EXISTANT") + " " + rec.getNumeroAffilie()
                                 * + "/" + rec.getAnnee());
                                 */
                                hJournalExisteDeja.put(key, getSession().getLabel("DT_JOURNAL_NON_CREE") + " "
                                        + rec.getNumeroAffilie() + "/" + rec.getAnnee());
                            } else {
                                ecriture.setAnnee(rec.getAnnee());
                                boolean breakTests = false;
                                // Plausi période
                                testPeriode(rec, ecriture, errors, rec.getNumeroAffilie());
                                // Plausi montant
                                boolean montantPositif = rec.isMontantPositif();

                                String montantEcr = "";
                                if (traitementAFSeul) {
                                    montantEcr = rec.getMontantAf();
                                } else {
                                    montantEcr = rec.getMontantEcr();
                                }

                                if (montantPositif) {
                                    montantEcr = testAndSetPourMontantPositif(rec, ecriture, errors, montantEcr);
                                } else {
                                    nbrInscriptionsNegatives++;
                                    montantInscriptionsNegatives.sub(montantEcr);
                                    montantEcr = testAndSetPourMontantNegatif(rec, ecriture, errors, montantEcr);
                                }
                                // Période affiliation
                                if (!app.isInAffiliation(getSession(), rec.getDebutAffiliation(),
                                        rec.getFinAffiliation(), String.valueOf(rec.getJourDebut()),
                                        String.valueOf(rec.getJourFin()), String.valueOf(rec.getMoisDebut()),
                                        String.valueOf(rec.getMoisFin()), rec.getAnnee())) {
                                    // Les dates ne correspondent pas avec la période d'affiliation
                                    if (!CIDeclaration.CS_COT_PERS.equals(getType())) {
                                        errors.add(getSession().getLabel("DT_ERR_DATE_AFFILIATION"));
                                    }

                                }
                                // Plausi no avs
                                String noAvs = testEndSetInfoPourNumAvs(rec, ecriture, errors);

                                ecriture.setNomPrenom(CIDeclaration.getNomFormatCI(rec.getNomPrenom()));

                                /************************************************************
                                 * Modif. 03.05.2006 Plus de plausi sur le nom, car on insère plus la virgule
                                 * automatiquement
                                 */
                                int anneeNaissance = determineAnneeNaissance(rec, ecriture);

                                if (!JadeStringUtil.isBlankOrZero(rec.getMontantAf()) && !traitementAFSeul) {
                                    BigDecimal mntAf = new BigDecimal(rec.getMontantAf());
                                    BigDecimal mntAvs = new BigDecimal(rec.getMontantEcr());
                                    if (mntAf.compareTo(mntAvs) != 0) {
                                        errors.add(getSession().getLabel("DT_AF_DIFF_AVS"));
                                    }

                                }
                                //
                                // !!! Attention test basé sur le numéro AVS pour calculé l'age
                                //
                                if ((anneeNaissance + 1918) > Integer.parseInt(rec.getAnnee())) {
                                    // test de l'age à partir du n°AVS
                                    errors.add(getSession().getLabel("DT_ERR_AGE_MIN"));
                                    breakTests = true;
                                } else {
                                    if (CIDeclaration.isAvs0(noAvs)) {
                                        ecriture.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE_SUSPENS);
                                        info.add(getSession().getLabel("DT_AVS_0"));
                                    } else {
                                        if (noAvs.length() < 11) {
                                            // avs trop court
                                            ecriture.setIdTypeCompte(CIEcriture.CS_TEMPORAIRE_SUSPENS);
                                            info.add(getSession().getLabel("DT_ERR_AVS_11"));
                                        }
                                    }
                                    if (!ecriture.rechercheCI(getTransaction(), null, false, false)
                                            || getTransaction().hasErrors()) {
                                        // erreur de création de CI
                                        info.add(getSession().getLabel("DT_NUM_AVS_INVALIDE"));
                                        breakTests = true;
                                    }
                                }
                                // test sur le total des inscriptions pour l'affiliation et l'année en cours
                                if (traitementAFSeul) {
                                    breakTests = true;
                                } else if ("true".equalsIgnoreCase(accepteEcrituresNegatives) && !montantPositif) {
                                    if (CICompteIndividuel.CS_REGISTRE_PROVISOIRE
                                            .equals(ecriture.getCI(getTransaction(), false).getRegistre())) {
                                        errors.add(getSession().getLabel("MSG_DT_INCONNU_ET_NEG"));
                                    } else {

                                        BigDecimal totalPourAff = new BigDecimal("0");
                                        try {
                                            totalPourAff = ecriture.getWrapperUtil().rechercheEcritureEmpResult(
                                                    getTransaction(),
                                                    CIUtil.formatNumeroAffilie(getSession(), rec.getNumeroAffilie()));
                                            totalPourAff = totalPourAff.subtract(new BigDecimal(montantEcr.trim()));
                                        } catch (Exception e) {
                                            totalPourAff = new BigDecimal("0");

                                        }
                                        int res = totalPourAff.compareTo(new BigDecimal("0"));
                                        if (res < 0) {
                                            errors.add(getSession().getLabel("MSG_ECRITURE_SUMEMP"));
                                        }
                                    }
                                }
                                if (!breakTests) {
                                    if (CICompteIndividuel.CS_REGISTRE_PROVISOIRE
                                            .equals(ecriture.getCI(getTransaction(), false).getRegistre())) {
                                        // CI Provisoire
                                        traitementSiRegistreProvisoire(rec, ecriture, errors, info,
                                                rec.getNumeroAffilie(), noAvs);
                                    } else {
                                        // ci ok, recherche des écritures identiques
                                        CIEcritureManager ecrMgr = new CIEcritureManager();
                                        ecrMgr.setSession(getSession());
                                        ecrMgr.setForAnnee(rec.getAnnee());
                                        ecrMgr.setForCompteIndividuelId(
                                                ecriture.getCI(getTransaction(), false).getCompteIndividuelId());
                                        ecrMgr.setForAffilie(
                                                CIUtil.formatNumeroAffilie(getSession(), rec.getNumeroAffilie()));
                                        ecrMgr.find(getTransaction());
                                        for (int i = 0; i < ecrMgr.size(); i++) {
                                            CIEcriture ecr = (CIEcriture) ecrMgr.getEntity(i);
                                            if (CIEcriture.CS_CODE_PROVISOIRE.equals(ecr.getCode())) {
                                                // erreur: écriture provisoire déjà présente
                                                errors.add(getSession().getLabel("DT_INSCR_PROV"));
                                                break;
                                            }
                                            if ((ecr.getMoisDebut().equals(rec.getMoisDebut() + ""))
                                                    && (ecr.getMoisFin().equals(rec.getMoisFin() + ""))
                                                    && ("01".equals(ecr.getGreFormat())
                                                            || "11".equals(ecr.getGreFormat())
                                                            || "07".equals(ecr.getGreFormat())
                                                            || "17".equals(ecr.getGreFormat()))
                                                    && ((ecr.getMontant().substring(0, ecr.getMontant().length() - 3))
                                                            .equals(ecriture.getMontant().substring(0,
                                                                    ecriture.getMontant().length() - 3)))) {

                                                if ((JadeStringUtil.isEmpty(ecriture.getExtourne())
                                                        && "0".equals(ecr.getExtourne()))
                                                        || ecr.getExtourne().equals(ecriture.getExtourne())) {
                                                    if (!CIDeclaration.CS_COT_PERS.equals(getType())) {
                                                        // erreur: écriture identique
                                                        errors.add(getSession().getLabel("DT_INSCR_IDENTIQUE"));
                                                    }

                                                    break;
                                                }

                                            }
                                        }
                                        // au CI, tester clôture
                                        String clo = ecriture.getCI(getTransaction(), false).getDerniereCloture(true);
                                        if (ecriture.aCloturer(new JADate(clo))) {
                                            // écriture avant clôture
                                            ciAdd.add(getSession().getLabel("DT_CI_ADDITIONEL"));
                                            if ((errors.size() == 0) && (info.size() == 0)) {
                                                nbrInscriptionsCI++;

                                                if (montantPositif) {
                                                    montantInscriptionsCI.add(montantEcr);
                                                } else {
                                                    montantInscriptionsCI.sub(montantEcr);
                                                }
                                            }
                                        } else {
                                            if (!ecriture.getCI(getTransaction(), false).isCiOuvert().booleanValue()) {
                                                if (ecriture.isPeriodeDeCotisationACheval(getTransaction(),
                                                        new JADate(clo))) {
                                                    errors.add(getSession().getLabel("MSG_IK_PERIODE_A_CHEVAL"));
                                                } else {
                                                    info.add(getSession().getLabel("DT_ECR_APRES_CLOTURE"));
                                                }
                                            } else {
                                                if (ecriture.isPeriodeDeCotisationACheval(getTransaction(),
                                                        new JADate(clo))) {
                                                    errors.add(getSession().getLabel("MSG_IK_PERIODE_A_CHEVAL"));
                                                }
                                                if ((errors.size() == 0) && (info.size() == 0)) {
                                                    nbrInscriptionsCI++;
                                                    if (montantPositif) {
                                                        montantInscriptionsCI.add(montantEcr);
                                                    } else {
                                                        montantInscriptionsCI.sub(montantEcr);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (errors.size() != 0) {
                                    // si il y a eu des erreurs
                                    nbrInscriptionsErreur++;
                                    if (montantPositif) {
                                        montantInscriptionsErreur.add(montantEcr);
                                    } else {
                                        montantInscriptionsErreur.sub(montantEcr);
                                    }
                                    // result = false;
                                } else {
                                    // pas d'erreur
                                    if (info.size() != 0) {
                                        nbrInscriptionsSuspens++;
                                        if (montantPositif) {
                                            montantInscriptionsSuspens.add(montantEcr);
                                        } else {
                                            montantInscriptionsSuspens.sub(montantEcr);
                                        }
                                    }
                                    // -------------------------------------------------------------------------------
                                    // Ajout écriture et mis a jour du journal
                                    // -------------------------------------------------------------------------------
                                    if (modeInscription && !traitementAFSeul) {
                                        // journal trouvé
                                        ecriture.setIdJournal(journal.getIdJournal());
                                        // Si on est en mode linkDraco, on ne passe pas par l'écriture, mais par
                                        // l'inscription DRACO
                                        if ("true".equalsIgnoreCase(accepteLienDraco)) {
                                            DSInscriptionsIndividuelles insc = new DSInscriptionsIndividuelles();
                                            // recherche de la déclaration en question
                                            DSDeclarationListeManager decMgr = new DSDeclarationListeManager();
                                            decMgr.setForIdJournal(journal.getIdJournal());
                                            decMgr.setSession((BSession) getSessionDS(getSession()));
                                            decMgr.wantCallMethodAfter(false);
                                            decMgr.find(getTransaction());
                                            if (decMgr.size() > 0) {
                                                DSInscriptionsIndividuellesListeViewBean declarationDraco = (DSInscriptionsIndividuellesListeViewBean) decMgr
                                                        .getFirstEntity();
                                                insc.setDeclaration(declarationDraco);
                                                insc.setSession((BSession) getSessionDS(getSession()));
                                                insc.setIdDeclaration(declarationDraco.getIdDeclaration());
                                                insc.setGenreEcriture(ecriture.getGre());
                                                if (!JadeStringUtil
                                                        .isIntegerEmpty(String.valueOf(rec.getJourDebut()))) {
                                                    insc.setPeriodeDebut(rec.getJourDebut() + "." + rec.getMoisDebut());
                                                } else {
                                                    insc.setPeriodeDebut(ecriture.getMoisDebutPad());
                                                }
                                                if (!JadeStringUtil.isIntegerEmpty(String.valueOf(rec.getJourFin()))) {
                                                    insc.setPeriodeFin(rec.getJourFin() + "." + ecriture.getMoisFin());
                                                } else {
                                                    insc.setPeriodeFin(ecriture.getMoisFinPad());
                                                }
                                                if (!JadeStringUtil.isIntegerEmpty(rec.getCodeCanton())) {
                                                    try {
                                                        String codeCanton = CIUtil.codeUtilisateurToCodeSysteme(
                                                                getTransaction(), rec.getCodeCanton(), "PYCANTON",
                                                                getSession());
                                                        insc.setCodeCanton(codeCanton);
                                                    } catch (Exception e) {
                                                    }
                                                } else {
                                                    String codeCanton = AFAffiliationUtil.getCantonAFForDS(affilie,
                                                            JACalendar.todayJJsMMsAAAA());
                                                    insc.setCodeCanton(codeCanton);

                                                }
                                                insc.setFromPucs(true);
                                                insc.setMontant(JANumberFormatter.deQuote(ecriture.getMontant()));
                                                insc.setNumeroAvs(CIUtil.unFormatAVS(ecriture.getAvs()));
                                                insc.setNomPrenom(ecriture.getNomPrenom());
                                                insc.setAnneeInsc(ecriture.getAnnee());
                                                if (CIDeclaration.CS_PUCS_CCJU.equals(Type)
                                                        || DeclarationSalaireProvenance
                                                                .fromValueWithOutException(provenance).isDan()) {
                                                    if (!JadeStringUtil.isIntegerEmpty(rec.getCategoriePers())) {
                                                        try {
                                                            String categoriePers = CIUtil.codeUtilisateurToCodeSysteme(
                                                                    getTransaction(), rec.getCategoriePers(),
                                                                    "CICATPER", getSession());
                                                            insc.setCategoriePerso(categoriePers);
                                                        } catch (Exception e) {
                                                            // Ne pas faire planter le processus si la catégorie est mal
                                                            // renseignée
                                                        }
                                                    }
                                                }
                                                if (!JadeStringUtil.isBlankOrZero(rec.getMontantAf())) {
                                                    insc.setMontantAf(JANumberFormatter.deQuote(rec.getMontantAf()));
                                                    BigDecimal mntAf = new BigDecimal(rec.getMontantAf());
                                                    BigDecimal mntAvs = new BigDecimal(rec.getMontantEcr());
                                                    if (mntAf.compareTo(mntAvs) != 0) {
                                                        errors.add(getSession().getLabel("DT_AF_DIFF_AVS"));
                                                    }

                                                }
                                                insc.add(getTransaction());
                                                boolean differenceAc = false;
                                                if (!DeclarationSalaireProvenance.fromValueWithOutException(provenance)
                                                        .isDan() && !JadeStringUtil.isBlank(rec.getMontantAc())) {
                                                    BigDecimal montantCommunique = new BigDecimal(rec.getMontantAc());
                                                    BigDecimal montantEcriture = null;
                                                    if (!JadeStringUtil.isBlank(insc.getACI())) {
                                                        montantEcriture = new BigDecimal(insc.getACI());
                                                    } else {
                                                        montantEcriture = new BigDecimal("0");
                                                    }
                                                    if (montantCommunique.compareTo(montantEcriture) != 0) {
                                                        info.add(getSession().getLabel("MSG_MONTANT_AC") + " "
                                                                + new FWCurrency(montantCommunique.toString())
                                                                        .toStringFormat()
                                                                + " / " + new FWCurrency(montantEcriture.toString())
                                                                        .toStringFormat());
                                                        if ((nbrInscriptionsSuspens == 0)
                                                                && (nbrInscriptionsSuspens == 0)) {
                                                            totalAvertissement = totalAvertissement + 1;
                                                        }
                                                        differenceAc = true;
                                                        hasDifferenceAc = true;
                                                    }

                                                }
                                                if (!DeclarationSalaireProvenance.fromValueWithOutException(provenance)
                                                        .isDan() && !JadeStringUtil.isBlank(rec.getMontantAc2())) {
                                                    BigDecimal montantCommunique = new BigDecimal(rec.getMontantAc2());
                                                    BigDecimal montantEcriture = null;
                                                    if (!JadeStringUtil.isBlank(insc.getACII())) {
                                                        montantEcriture = new BigDecimal(insc.getACII());
                                                    } else {
                                                        montantEcriture = new BigDecimal("0");
                                                    }
                                                    if (montantCommunique.compareTo(montantEcriture) != 0) {
                                                        info.add(getSession().getLabel("MSG_MONTANT_AC2") + " "
                                                                + new FWCurrency(montantCommunique.toString())
                                                                        .toStringFormat()
                                                                + " / " + new FWCurrency(montantEcriture.toString())
                                                                        .toStringFormat());
                                                        if ((nbrInscriptionsSuspens == 0)
                                                                && (nbrInscriptionsSuspens == 0)) {
                                                            totalAvertissement = totalAvertissement + 1;
                                                        }
                                                        differenceAc = true;
                                                        hasDifferenceAc = true;
                                                    }

                                                }
                                                // Si différence AC ou AC2 pour SwissDec, il faut remettre les montants
                                                // communiqués via SwissDec
                                                // car le montant ne doit pas être recalculé (période fausse)
                                                if (differenceAc
                                                        && DSDeclarationViewBean.PROVENANCE_SWISSDEC.equals(provenance)
                                                        && !insc.isNew() && !insc.hasErrors()) {
                                                    insc.setACI(rec.getMontantAc());
                                                    insc.setACII(rec.getMontantAc2());
                                                    insc.wantCallValidate(false);
                                                    insc.update(getTransaction());
                                                }

                                                if (CIUtil.isRetraite(ecriture.getAvs(),
                                                        Integer.parseInt(ecriture.getAnnee()), ecriture.getAvsNNSS(),
                                                        getSession())) {
                                                    info.add(getSession().getLabel("MSG_PUCS_ASSURE_RETRAITE"));
                                                }
                                            } else {
                                                getTransaction().addErrors(getSession().getLabel("DECL_NON_EXISTANTE"));
                                            }
                                        } else {
                                            ecriture.setNoSumNeeded(true);
                                            // Modif. mettre le kbbatt à 2 pour concordance nnss
                                            ecriture.setWantForDeclaration(new Boolean(false));
                                            ecriture.setEmployeur(rec.getIdAffiliation());
                                            if (CIDeclaration.CS_COT_PERS.equals(getType()) && !montantPositif) {
                                                // mode sans plausi
                                                ecriture.simpleAdd(getTransaction());
                                            } else {
                                                ecriture.add(getTransaction());
                                            }
                                        }
                                        getTransaction().disableSpy();
                                        // journal.updateInscription(getTransaction());
                                        // getTransaction().enableSpy();
                                        if (!getTransaction().hasErrors()) {
                                            getTransaction().commit();
                                        }
                                    }
                                }
                                // -------------------------------------------------------------------------------
                                // log assuré
                                // -------------------------------------------------------------------------------
                                rec.setCiAdd(ciAdd);
                                rec.setInfo(info);
                                rec.setErrors(errors);
                                tableLogAssure.put(_getFullKey(rec, line), rec);
                                if (getTransaction().hasErrors()) {
                                    if (traitementAFSeul) {
                                        getTransaction().rollback();
                                    }
                                    errors.add(getTransaction().getErrors().toString());
                                    nbrInscriptionsErreur++;
                                    getMemoryLog().logStringBuffer(getTransaction().getErrors(), titreLog);
                                    getMemoryLog().logMessage(ecriture.getNomPrenom(), FWMessage.ERREUR, titreLog);
                                    getTransaction().clearErrorBuffer();
                                }
                                nbrInscriptionsTraites++;
                                if (montantPositif) {
                                    montantInscritionsTraites.add(montantEcr);
                                } else {
                                    montantInscritionsTraites.sub(montantEcr);
                                }
                                if (!modeInscription) {
                                    // simulation
                                    // faire un rollback permettant d'effacer le CI temporaire éventuellement créés
                                    getTransaction().rollback();
                                }
                                // -------------------------------------------------------------------------------
                                // met a jour les TreeMaps pour les summaries
                                // -------------------------------------------------------------------------------
                                nbrInscriptionsTotalControle = nbrInscriptionsCI + nbrInscriptionsSuspens;
                                montantTotalControle.add(montantInscriptionsCI);
                                montantTotalControle.add(montantInscriptionsSuspens);

                                _updateSummary(hNbrInscriptionsTraites, hMontantInscritionsTraites,
                                        hNbrInscriptionsErreur, hMontantInscriptionsErreur, hNbrInscriptionsSuspens,
                                        hMontantInscriptionsSuspens, hNbrInscriptionsCI, hMontantInscriptionsCI,
                                        hNbrInscriptionsNegatives, hMontantInscriptionsNegatives,
                                        nbrInscriptionsTraites, montantInscritionsTraites, nbrInscriptionsErreur,
                                        montantInscriptionsErreur, nbrInscriptionsSuspens, montantInscriptionsSuspens,
                                        nbrInscriptionsCI, montantInscriptionsCI, nbrInscriptionsNegatives,
                                        montantInscriptionsNegatives, hNbrInscriptionsTotalControle,
                                        nbrInscriptionsTotalControle, hMontantTotalControle, montantTotalControle, key);
                            } // fin du else 'journal trouvé'
                        } // fin du if 'numéro affilie'
                    } // fin du if 'annee'
                } // fin du while principale

                if (traitementAFSeul) {
                    // Création du relevé à l'état saisi
                    if (affilie != null) {
                        creationReleve(rec, affilie, totalParCanton);
                    }
                    if (launcherImportPucsFileProcess != null) {
                        preparerDonnneeRapportExcelMlAFSeule(rec, totalParCanton);
                    }
                }
                try {
                    totauxJournaux = itDec.getTotauxJournaux();

                } catch (Exception err) {
                    JadeLogger.error(this, err);
                }
                if (modeInscription && !traitementAFSeul && !isAborted()) {
                    // maj des totaux des journaux

                    Set<String> keys = tableJournaux.keySet();
                    Iterator<String> iter = keys.iterator();

                    getTransaction().disableSpy();
                    while (iter.hasNext()) {

                        String key = iter.next();
                        CIJournal journal = (CIJournal) tableJournaux.get(key);

                        // Mettre à jour les inscriptions
                        if (journal != null && !JadeStringUtil.isIntegerEmpty(journal.getIdJournal())) {
                            journal.updateInscription(getTransaction());
                        }

                        if ("true".equalsIgnoreCase(accepteLienDraco) && journal != null
                                && !JadeStringUtil.isBlankOrZero(journal.getIdJournal())) {
                            DSDeclarationListeManager decMgr = new DSDeclarationListeManager();
                            decMgr.setForIdJournal(journal.getIdJournal());
                            decMgr.setSession((BSession) getSessionDS(getSession()));
                            decMgr.find(getTransaction());
                            if (decMgr.size() > 0) {
                                DSInscriptionsIndividuellesListeViewBean declarationDraco = (DSInscriptionsIndividuellesListeViewBean) decMgr
                                        .getFirstEntity();
                                declarationDraco.calculeTotauxAcAf();
                                String numAffilieNonFormate = CIUtil.UnFormatNumeroAffilie(getSession(),
                                        declarationDraco.getAffiliation().getAffilieNumero());
                                String keyDec = declarationDraco.getAnnee() + numAffilieNonFormate;

                                // Pour la FPV, il est possible que le n° d'affilié dans la DS soit sans les premiers 0
                                // => supprimer les 0 en début du n° d'affilié pour qu'il soit retrouvé
                                boolean totalTrouve = false;
                                if (totauxJournaux != null) {
                                    totalTrouve = totauxJournaux.containsKey(keyDec);
                                    if (!totalTrouve) {
                                        // Recherche num caisse
                                        CIApplication applicationCI = (CIApplication) GlobazServer.getCurrentSystem()
                                                .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO);
                                        String numCaisse = applicationCI.getProperty(CIApplication.CODE_CAISSE) + "."
                                                + applicationCI.getProperty(CIApplication.CODE_AGENCE);
                                        // Traitement spécifique FPV
                                        if ("110.000".equals(numCaisse)) {
                                            String numAffilieFPV = JadeStringUtil.stripLeading(numAffilieNonFormate,
                                                    "0");
                                            keyDec = declarationDraco.getAnnee() + numAffilieFPV;
                                            totalTrouve = totauxJournaux.containsKey(keyDec);
                                        }
                                    }
                                }

                                if (totalTrouve) {
                                    String montantControleDec = (String) totauxJournaux.get(keyDec);
                                    declarationDraco.setTotalControleDS(montantControleDec);
                                    journal.setTotalControle(montantControleDec);
                                }
                                declarationDraco.update(getTransaction());
                                importeInscriptionsProvisoires(declarationDraco);

                                // InfoRom363 calcul automatique des masses
                                try {
                                    theCalculMasseProcess = new DSValideMontantDeclarationProcess();
                                    // Tant que la transaction n'est pas commitée, certaines tables (par exemple
                                    // DSDECLP) sont lockées
                                    // et le calcul des masses ne peut pas s'effectuer
                                    if (getTransaction().hasErrors()) {
                                        getTransaction().rollback();
                                    } else {
                                        getTransaction().commit();
                                    }

                                    theCalculMasseProcess.setSession((BSession) getSessionDS(getSession()));
                                    theCalculMasseProcess.setEMailAddress(getEMailAddress());
                                    theCalculMasseProcess.setIdDeclaration(declarationDraco.getIdDeclaration());
                                    theCalculMasseProcess.executeProcess();

                                    if (theCalculMasseProcess.isAborted() || theCalculMasseProcess.isOnError()) {
                                        if (launcherImportPucsFileProcess != null) {
                                            launcherImportPucsFileProcess
                                                    .setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
                                        }

                                        // le calcul des masses doit envoyer un mail qu'en cas d'erreurs
                                        // le mode synchrone ne gère pas l'envoi de mails
                                        // c'est pourquoi l'envoi est fait manuellement dans ce process
                                        JadeSmtpClient.getInstance().sendMail(theCalculMasseProcess.getEMailAddress(),
                                                declarationDraco.getNumeroAffilie() + " - "
                                                        + declarationDraco.getAnnee() + " - "
                                                        + theCalculMasseProcess.getSubject(),
                                                theCalculMasseProcess.getSubjectDetail(), new String[0]);
                                    }

                                } catch (Exception e) {
                                    if (launcherImportPucsFileProcess != null) {

                                        String infoDeclaration = "";
                                        if (declarationDraco != null) {
                                            infoDeclaration = declarationDraco.getNumeroAffilie() + " - "
                                                    + declarationDraco.getAnnee() + " - ";
                                        }

                                        launcherImportPucsFileProcess.getMemoryLog().logMessage(
                                                infoDeclaration + e.toString(), FWMessage.INFORMATION,
                                                this.getClass().getName());
                                        launcherImportPucsFileProcess
                                                .setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
                                    }
                                }

                                if (launcherImportPucsFileProcess != null) {
                                    preparerDonnneeRapportExcelMl(rec, declarationDraco);
                                }
                            }
                        }

                        hMontantTotalControle.keySet();

                        // if (iter1.hasNext()) {
                        FWCurrency montantTotal = (FWCurrency) hMontantTotalControle.get(key);
                        // }

                        // Mettre à jour le journal
                        if (journal != null && !JadeStringUtil.isEmpty(journal.getIdJournal())) {
                            journal.setIdJournal(journal.getIdJournal());
                            journal.setSession(getSession());
                            journal.retrieve();
                            if (!JadeStringUtil.isDecimalEmpty(montantTotal.toString())) {
                                journal.setTotalControle(montantTotal.toString());
                            }
                            journal.update();
                        }

                    }

                    // notifiyFinished pour e-Business
                    if (!"true".equals(getAccepteLienDraco())) {
                        if (!JadeStringUtil.isBlankOrZero(idsPucsFile)
                                && (CIDeclaration.ebusinessAccessInstance != null)) {
                            try {

                                List<String> ids = Splitter.on(";").trimResults().splitToList(idsPucsFile);
                                for (String id : ids) {
                                    CIDeclaration.ebusinessAccessInstance.notifyFinishedPucsFile(id, provenance,
                                            getSession());
                                    EBPucsFileService.comptabiliserByFilename(id, getSession());
                                }

                            } catch (Exception e) {
                                JadeLogger.error(this, e);
                                getMemoryLog().logMessage("unable to change status", FWMessage.ERREUR, titreLog);
                            }
                        }
                    }

                    getTransaction().enableSpy();
                }

                // ------------------------------------------------------------------------------
                // affichage / génération des rapports
                // ------------------------------------------------------------------------------

                _doLogJournauxExistant(hJournalExisteDeja); // des Journaux qui existait dejà
                // CIDeclarationTextOutput doc = new CIDeclarationTextOutput();
                ICIDeclarationOutput doc = new CIDeclarationHTMLOutput();
                doc.setSession(getSession());
                doc.setSimulation(!modeInscription);
                doc.setData(tableLogAssure);
                // Modif jmc 1-5-8, si aucun avertissement et aucune erreur => pas de mail
                if (!traitementAFSeul) {
                    if ((0 != totalErreur) || (0 != totalAvertissement)) {
                        if (doc.getOutputFile() != null) {
                            JadePublishDocumentInfo docInfo = createDocumentInfo();
                            docInfo.setDocumentType("0161CCI");
                            docInfo.setDocumentTypeNumber("");
                            this.registerAttachedDocument(docInfo, doc.getOutputFile());
                        }
                    }
                    if (!traitementAFSeul) {
                        impressionResumeFormatXML(modeInscription, itDec);
                    }
                }
                // ------------------------------------------------------------------------------
                // Comparaison avec somme de control (si renseigné )
                // ------------------------------------------------------------------------------
                if (!JadeStringUtil.isEmpty(totalControle)) {
                    FWCurrency totalCalcule = new FWCurrency(0);
                    Collection<Object> values = hMontantInscritionsTraites.values();
                    Iterator<Object> it = values.iterator();
                    while (it.hasNext()) {
                        FWCurrency cur = (FWCurrency) it.next();
                        totalCalcule.add(cur);
                    }
                    FWCurrency totalControleFormate = new FWCurrency(totalControle);
                    if (!totalCalcule.toStringFormat().equals(totalControleFormate.toStringFormat())) {
                        getMemoryLog().logMessage(getSession().getLabel("DT_LOG_TOTALE_COR_PAS"), FWMessage.ERREUR,
                                titreLog);
                        getMemoryLog().logMessage(getSession().getLabel("DT_LOG_TOTALE_CTRL") + " : "
                                + totalControleFormate.toStringFormat(), FWMessage.INFORMATION, titreLog);
                        getMemoryLog().logMessage(
                                getSession().getLabel("DT_LOG_TOTALE_CAL") + " : " + totalCalcule.toStringFormat(),
                                FWMessage.INFORMATION, titreLog);
                        isErrorMontant = true;
                        result = false;
                    }
                } // fin total contrôle
                  // ------------------------------------------------------------------------------
                  // Comparaison avec nombre d'inscriptions (si renseigné )
                  // ------------------------------------------------------------------------------
                if (!JadeStringUtil.isEmpty(nombreInscriptions)) {
                    long nombreInscritionsCalcule = 0;
                    Collection<Object> values = hNbrInscriptionsTraites.values();
                    Iterator<Object> it = values.iterator();
                    while (it.hasNext()) {
                        long tmp = ((Long) it.next()).longValue();
                        nombreInscritionsCalcule += tmp;
                    }
                    if (!(nombreInscritionsCalcule + "").equals(nombreInscriptions)) {
                        getMemoryLog().logMessage(getSession().getLabel("DT_LOG_NB_INS_COR_PAS"), FWMessage.ERREUR,
                                titreLog);
                        getMemoryLog().logMessage(getSession().getLabel("DT_LOG_NB_INS") + " : " + nombreInscriptions,
                                FWMessage.INFORMATION, titreLog);
                        getMemoryLog().logMessage(
                                getSession().getLabel("DT_LOG_NB_INS_FICH") + " : " + nombreInscritionsCalcule + "",
                                FWMessage.INFORMATION, titreLog);
                        isErrorNbInscriptions = true;
                        result = false;
                    }
                } // fin nombre d'inscriptions

            } catch (Exception ioe) {
                JadeLogger.error(this, ioe);
                result = false;
                getMemoryLog().logMessage(ioe.toString(), FWMessage.FATAL, titreLog);
                if (launcherImportPucsFileProcess != null) {
                    launcherImportPucsFileProcess.getMemoryLog().logMessage(ioe.toString(), FWMessage.INFORMATION,
                            this.getClass().getName());
                    launcherImportPucsFileProcess.setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
                }
            }

            return result;
        }
    }

    private void impressionResumeFormatXML(boolean modeInscription, ICIDeclarationIterator itDec) throws IOException {
        ICIDeclarationOutput docSummary = null;
        if (CIDeclaration.CS_COT_PERS.equals(Type)) {
            docSummary = new CIDeclarationSummaryHTMLOutputCotPers();
        } else {
            docSummary = new CIDeclarationSummaryHTMLOutput();
        }
        docSummary.setSession(getSession());
        docSummary.setSimulation(!modeInscription);
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(hNbrInscriptionsTraites);
        params.add(hMontantInscritionsTraites);
        params.add(hNbrInscriptionsErreur);
        params.add(hMontantInscriptionsErreur);
        params.add(hNbrInscriptionsSuspens);
        params.add(hMontantInscriptionsSuspens);
        params.add(hNbrInscriptionsCI);
        params.add(hMontantInscriptionsCI);
        params.add(tableJournaux);
        // ajout totaux journaux pour PUCS
        if (JadeStringUtil.isEmpty(totalControle)) {
            params.add(new FWCurrency(0L));
        } else {
            params.add(new FWCurrency(totalControle));
        }
        if (JadeStringUtil.isEmpty(nombreInscriptions)) {
            params.add(new Long(0L));
        } else {
            params.add(new Long(nombreInscriptions));
        }

        try {
            nbInsc = itDec.getNbSalaires();

        } catch (Exception err) {
            err.printStackTrace();
        }

        params.add(totauxJournaux);
        params.add(nbInsc);
        params.add(hNbrInscriptionsNegatives);
        params.add(hMontantInscriptionsNegatives);
        params.add(hMontantTotalControle);
        params.add(hNbrInscriptionsTotalControle);
        docSummary.setData(params);
        if (docSummary.getOutputFile() != null) {
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentType("0161CCI");
            docInfo.setDocumentTypeNumber("");
            this.registerAttachedDocument(docInfo, docSummary.getOutputFile());
        }
    }

    private void preparerDonnneeRapportExcelMl(CIDeclarationRecord rec,
            DSInscriptionsIndividuellesListeViewBean declarationDraco) {
        CommonExcelmlContainer theContainerRapportExcelmlImportedPucsFile = null;
        try {
            // Préparation des données pour le rapport Excelml basé sur le modèle
            // (RapportImportedPucsFileModele.xml)
            theContainerRapportExcelmlImportedPucsFile = launcherImportPucsFileProcess
                    .getContainerRapportExcelmlImportedPucsFile();

            declarationDraco.retrieve(getTransaction());

            theContainerRapportExcelmlImportedPucsFile.put("COL_NO_AFFILIE", declarationDraco.getNumeroAffilie());
            theContainerRapportExcelmlImportedPucsFile.put("COL_NOM_AFFILIE", declarationDraco.getDesignation1());
            // info si swissDec
            if (DSDeclarationViewBean.PROVENANCE_SWISSDEC.equalsIgnoreCase(getProvenance()) && rec != null) {
                theContainerRapportExcelmlImportedPucsFile.put("COL_INFO_AFFILIE_TRANSMIS",
                        rec.getAffilieFichierNom() + "\n" + rec.getAffilieFichierStreet() + "\n"
                                + rec.getAffilieFichierNpa() + " " + rec.getAffilieFichierCity());
                theContainerRapportExcelmlImportedPucsFile.put("COL_INFO_CONTACT_TRANSMIS",
                        rec.getAffilieContactPersonName() + "\n" + rec.getAffilieContactPersonEmail() + "\n"
                                + rec.getAffilieContactPersonPhone());
            }
            theContainerRapportExcelmlImportedPucsFile.put("COL_DATE_RECEPTION", declarationDraco.getDateRetourEff());
            theContainerRapportExcelmlImportedPucsFile.put("COL_NOMBRE_INSCRIPTION",
                    JANumberFormatter.deQuote(String.valueOf(totalTraite)));
            theContainerRapportExcelmlImportedPucsFile.put("COL_ANNEE",
                    JANumberFormatter.deQuote(declarationDraco.getAnnee()));
            theContainerRapportExcelmlImportedPucsFile.put("COL_MASSE_AVS",
                    JANumberFormatter.deQuote(declarationDraco.getMasseSalTotal()));
            theContainerRapportExcelmlImportedPucsFile.put("COL_MONTANT_FACTURE",
                    JANumberFormatter.deQuote(declarationDraco.getMontantFacture().toString()));
            theContainerRapportExcelmlImportedPucsFile.put("COL_NOMBRE_ERREUR",
                    JANumberFormatter.deQuote(String.valueOf(totalErreur)));

            if (isOnError() || isAborted()) {
                launcherImportPucsFileProcess.setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
            }
            theContainerRapportExcelmlImportedPucsFile.put("COL_STATUT",
                    launcherImportPucsFileProcess.getImportStatutAFile());

        } catch (Exception e) {
            String infoDeclaration = "";
            if (declarationDraco != null) {
                infoDeclaration = declarationDraco.getNumeroAffilie() + " - " + declarationDraco.getAnnee() + " - ";
            }
            launcherImportPucsFileProcess.getMemoryLog().logMessage(infoDeclaration + e.toString(),
                    FWMessage.INFORMATION, this.getClass().getName());
            launcherImportPucsFileProcess.setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
        }
    }

    private void preparerDonnneeRapportExcelMlAFSeule(CIDeclarationRecord rec, Map<String, String> sommeParCanton) {
        for (String mapKey : sommeParCanton.keySet()) {
            CommonExcelmlContainer theContainerRapportExcelmlImportedFileAFSeule = null;
            try {
                // Préparation des données pour le rapport Excelml basé sur le modèle
                // (RapportImportedSwissDecFileModeleAFSeule.xml)
                theContainerRapportExcelmlImportedFileAFSeule = launcherImportPucsFileProcess
                        .getContainerRapportExcelmlImportedAFSeule();

                theContainerRapportExcelmlImportedFileAFSeule.put("COL_NO_AFFILIE", rec.getNumeroAffilie());
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_NOM_AFFILIE", rec.getNomAffilie());
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_INFO_AFFILIE_TRANSMIS",
                        rec.getAffilieFichierNom() + "\n" + rec.getAffilieFichierStreet() + "\n"
                                + rec.getAffilieFichierNpa() + " " + rec.getAffilieFichierCity());
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_INFO_CONTACT_TRANSMIS",
                        rec.getAffilieContactPersonName() + "\n" + rec.getAffilieContactPersonEmail() + "\n"
                                + rec.getAffilieContactPersonPhone());
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_ANNEE",
                        JANumberFormatter.deQuote(rec.getAnnee()));
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_MASSE_AF",
                        JANumberFormatter.deQuote(sommeParCanton.get(mapKey)));
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_CANTON", mapKey);
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_DATE_RECEPTION", dateReceptionForced);
                if (isOnError() || isAborted() || totalErreur > 0) {
                    launcherImportPucsFileProcess.setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
                }
                theContainerRapportExcelmlImportedFileAFSeule.put("COL_STATUT",
                        launcherImportPucsFileProcess.getImportStatutAFile());
            } catch (Exception e) {
                launcherImportPucsFileProcess.getMemoryLog().logMessage(e.toString(), FWMessage.INFORMATION,
                        this.getClass().getName());
                launcherImportPucsFileProcess.setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
            }
        }
    }

    private void traitementSiRegistreProvisoire(CIDeclarationRecord rec, CIEcriture ecriture, ArrayList<String> errors,
            ArrayList<String> info, String numeroAffilie, String noAvs) throws Exception {
        // assuré inconnu
        info.add(getSession().getLabel("DT_ASSURE_INCONNU"));
        CICompteIndividuel ci = new CICompteIndividuel();
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        if ((!JadeStringUtil.isEmpty(ecriture.getAvs()) || !JadeStringUtil.isEmpty(ecriture.getNomPrenom()))
                && !"00000000".equals(ecriture.getAvs())) {

            if (!JadeStringUtil.isEmpty(ecriture.getAvs())) {
                ciMgr.setForNumeroAvs(ecriture.getAvs());
            } else {
                ciMgr.setForNomPrenom(ecriture.getNomPrenom());
            }
        } else if ("00000000".equals(ecriture.getAvs())) {
            ciMgr.setForNumeroAvs(noAvs);
        }
        if (!JadeStringUtil.isEmpty(ciMgr.getForNumeroAvs()) || !JadeStringUtil.isEmpty(ciMgr.getForNomPrenom())) {
            ciMgr.setSession(getSession());
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
            ciMgr.find(getTransaction());
            if (ciMgr.size() != 0) {
                ci = (CICompteIndividuel) ciMgr.getFirstEntity();
                CIEcritureManager ecrMgr = new CIEcritureManager();
                ecrMgr.setSession(getSession());
                ecrMgr.setForAnnee(rec.getAnnee());
                ecrMgr.setForCompteIndividuelId(ci.getCompteIndividuelId());
                ecrMgr.setForAffilie(CIUtil.formatNumeroAffilie(getSession(), numeroAffilie));
                ecrMgr.find(getTransaction());
                for (int i = 0; i < ecrMgr.size(); i++) {
                    CIEcriture ecr = (CIEcriture) ecrMgr.getEntity(i);
                    if ((ecr.getMoisDebut().equals(rec.getMoisDebut() + ""))
                            && (ecr.getMoisFin().equals(rec.getMoisFin() + ""))
                            && ("01".equals(ecr.getGreFormat()) || "11".equals(ecr.getGreFormat())
                                    || "07".equals(ecr.getGreFormat()) || "17".equals(ecr.getGreFormat()))

                            // && (ecr.getMontant().equals(ecriture.getMontant()))) {
                            && ((ecr.getMontant().substring(0, ecr.getMontant().length() - 3))
                                    .equals(ecriture.getMontant().substring(0, ecriture.getMontant().length() - 3)))) {

                        if ((JadeStringUtil.isEmpty(ecriture.getExtourne()) && "0".equals(ecr.getExtourne()))
                                || ecr.getExtourne().equals(ecriture.getExtourne())) {
                            if (!CIDeclaration.CS_COT_PERS.equals(getType())) {
                                // erreur: écriture identique
                                errors.add(getSession().getLabel("DT_INSCR_IDENTIQUE"));
                            }

                            break;
                        }
                    }
                }
            }
        }
    }

    private int determineAnneeNaissance(CIDeclarationRecord rec, CIEcriture ecriture) throws JAException {
        int anneeNaissance = 60;
        if (rec.getNumeroAvs().trim().length() < 13) {
            try {
                anneeNaissance = Integer.parseInt(rec.getNumeroAvs().substring(3, 5));
            } catch (Exception e) {
                System.out.println("Erreur : " + rec.getNumeroAvs());
            }
        } else {
            CICompteIndividuel ci = ecriture.getForcedCi(getTransaction());
            if (ci != null) {
                JADate dateNaiss = new JADate(ci.getDateNaissance());
                anneeNaissance = dateNaiss.getYear();
                String anneeString = String.valueOf(anneeNaissance);
                if (anneeString.length() == 4) {
                    anneeString = anneeString.substring(2, 4);
                    anneeNaissance = Integer.parseInt(anneeString);
                }

            } else {
                anneeNaissance = 70;
            }

        }
        return anneeNaissance;
    }

    private String testEndSetInfoPourNumAvs(CIDeclarationRecord rec, CIEcriture ecriture, ArrayList<String> errors) {
        String noAvs = rec.getNumeroAvs().trim();
        // Modif v4.12 => dans pucs, le no peut être vide, pour avoir un identiant, on set le no
        if (JadeStringUtil.isBlank(noAvs)) {
            noAvs = "00000000000";
        }
        if (noAvs.endsWith("000") && (noAvs.trim().length() != 13)) {
            noAvs = rec.getNumeroAvs().substring(0, rec.getNumeroAvs().lastIndexOf("000")); // A
            // controler
        }
        // pour les cas ersam catherine
        if (noAvs.trim().startsWith("000")) {
            if (!JadeStringUtil.isEmpty(rec.getNomPrenom().trim())) {
                ecriture.setAvs("");
            } else {
                ecriture.setAvs(noAvs);
            }
        } else {
            ecriture.setAvs(noAvs);
        }
        if (CIUtil.isNNSSlengthOrNegate(noAvs)) {
            ecriture.setNumeroavsNNSS("true");
            ecriture.setAvsNNSS("true");
        }
        if ("true".equals(ecriture.getNumeroavsNNSS()) && !NSUtil.nssCheckDigit(ecriture.getAvs())) {
            errors.add(getSession().getLabel("MSG_CI_VAL_AVS"));

        }
        return noAvs;
    }

    private String testAndSetPourMontantNegatif(CIDeclarationRecord rec, CIEcriture ecriture, ArrayList<String> errors,
            String montantEcr) {
        if (!"true".equalsIgnoreCase(getAccepteEcrituresNegatives())) {
            FWCurrency cur = new FWCurrency(montantEcr);
            errors.add(getSession().getLabel("DT_ECRITURE_NEGATIVE"));
            if (!CIDeclaration.CS_COT_PERS.equals(Type)) {
                ecriture.setGre("11");
            } else {
                ecriture.setGre("1" + rec.getGenreEcriture());
            }
            ecriture.setExtourne(CIEcriture.CS_EXTOURNE_1);
            ecriture.setMontant(cur.toStringFormat());

        } else {
            try {
                FWCurrency cur = new FWCurrency(montantEcr);
                if (cur.compareTo(new FWCurrency("1")) == -1) {
                    errors.add(getSession().getLabel("DT_MONTANT_INF_1CHF"));
                    // breakTests = true;
                } else {
                    if (!CIDeclaration.CS_COT_PERS.equals(Type)) {
                        ecriture.setGre("11");
                    } else {
                        ecriture.setGre("1" + rec.getGenreEcriture());
                    }
                    ecriture.setExtourne(CIEcriture.CS_EXTOURNE_1);
                    ecriture.setMontant(cur.toStringFormat());
                }
            } catch (Exception inex) {
                errors.add(getSession().getLabel("DT_MONTANT_INVALIDE"));
                montantEcr = "0.00";
                // breakTests = true;
            }
        }
        return montantEcr;
    }

    private String testAndSetPourMontantPositif(CIDeclarationRecord rec, CIEcriture ecriture, ArrayList<String> errors,
            String montantEcr) {
        try {
            FWCurrency cur = new FWCurrency(montantEcr);
            if (cur.compareTo(new FWCurrency("1")) == -1) {
                errors.add(getSession().getLabel("DT_MONTANT_INF_1CHF"));
                ecriture.setMontant(montantEcr);
                // breakTests = true;
            } else {
                if (!CIDeclaration.CS_COT_PERS.equals(Type)) {
                    ecriture.setGre("01");
                } else {
                    ecriture.setGre("0" + rec.getGenreEcriture());
                }
                ecriture.setMontant(cur.toStringFormat());
            }
        } catch (Exception inex) {
            errors.add(getSession().getLabel("DT_MONTANT_INVALIDE"));
            montantEcr = "        0.00";
            // breakTests = true;
        }
        return montantEcr;
    }

    private void testPeriode(CIDeclarationRecord rec, CIEcriture ecriture, ArrayList<String> errors,
            String numeroAffilie) {
        // Plausi période
        try {
            int jourDebut = rec.getJourDebut();
            if (jourDebut < 0 || jourDebut > 31) {
                errors.add(getSession().getLabel("ERREUR_DATE_DEBUT"));
            } else {
                ecriture.setJourDebut("" + rec.getJourDebut());
            }

            int jourFin = rec.getJourFin();
            if (jourFin < 0 || jourFin > 31) {
                errors.add(getSession().getLabel("ERREUR_DATE_FIN"));
            } else {
                ecriture.setJourFin("" + rec.getJourFin());
            }

            int moisDebut = rec.getMoisDebut();
            if (((moisDebut < 1) || (moisDebut > 12)) && (99 != moisDebut) && (66 != moisDebut)) {
                errors.add(getSession().getLabel("DT_MOIS_DEBUT_INVALIDE"));
            } else {
                ecriture.setMoisDebut("" + rec.getMoisDebut());
            }
            int moisFin = rec.getMoisFin();
            if ((moisFin < 1) || ((moisFin > 12) && (99 != moisFin) && (66 != moisFin))) {
                errors.add(getSession().getLabel("DT_MOIS_FIN_INVALIDE"));
            } else {

                ecriture.setMoisFin("" + rec.getMoisFin());
            }
            if (moisDebut > moisFin) {
                errors.add(getSession().getLabel("DT_MOIS_DEBUT_PLUS_GRAND"));
            }
            if ((99 == moisDebut) && (99 == moisFin)) {
                if (!ecriture.getWrapperUtil().rechercheEcritureSemblablesDt(getTransaction(),
                        CIUtil.formatNumeroAffilie(getSession(), numeroAffilie), rec.getNumeroAvs())) {
                    errors.add(getSession().getLabel("MSG_ECRITURE_99"));
                }
            }

            // année en cours et futre sont interdites
            int annee = JACalendar.today().getYear();

            if (!"true".equalsIgnoreCase(accepteAnneeEnCours)) {
                if (Integer.parseInt(rec.getAnnee()) >= annee) {
                    errors.add(getSession().getLabel("DT_ANNEE_TROP_GRANDE"));
                }
            } else {
                if (Integer.parseInt(rec.getAnnee()) > annee) {
                    errors.add(getSession().getLabel("DT_ANNEE_TROP_GRANDE"));
                }
            }
        } catch (Exception ex) {
            errors.add(getSession().getLabel("DT_MOIS_INVALIDE"));
        }
    }

    private boolean executeAnnulationTraitement(boolean modeInscription) throws Exception {
        return executeAnnulationTraitement(modeInscription, tableJournaux);
    }

    private boolean executeAnnulationTraitement(boolean modeInscription, TreeMap<String, Object> mapJournaux)
            throws Exception {

        if (modeInscription) {
            Iterator<Object> jourIt = mapJournaux.values().iterator();

            while (jourIt.hasNext()) {
                if (!"true".equalsIgnoreCase(accepteLienDraco)) {
                    ((CIJournal) jourIt.next()).delete(getTransaction());
                } else {
                    CIJournal jour = new CIJournal();
                    jour = (CIJournal) jourIt.next();
                    DSDeclarationListViewBean dsMgr = new DSDeclarationListViewBean();
                    dsMgr.setSession((BSession) getSessionDS(getSession()));
                    dsMgr.setForIdJournal(jour.getIdJournal());
                    dsMgr.find(getTransaction());
                    if ((dsMgr.size() > 0) && !JadeStringUtil.isBlankOrZero(jour.getIdJournal())) {
                        DSDeclarationViewBean ds = (DSDeclarationViewBean) dsMgr.getFirstEntity();
                        ds.delete(getTransaction());
                    }

                }
            }
        }
        if (!getTransaction().hasErrors()) {
            getTransaction().commit();
        }
        getMemoryLog().logMessage(getSession().getLabel("MSG_PROCESSUS_ANNULE"), FWMessage.ERREUR, titreLog);
        return false;
    }

    private boolean declarationCentrale() {
        CIDeclarationCentrale process = null;

        try {
            process = new CIDeclarationCentrale(getSession());
            process.setType(getType());
            process.setEMailAddress(getEMailAddress());
            process.setEchoToConsole(false);
            process.setSimulation(simulation);
            process.setAccepteEcrituresNegatives(getAccepteEcrituresNegatives());
            process.setTotalControle(getTotalControle());
            process.setNombreInscriptions(getNombreInscriptions());
            process.setParent(this);
            if (!JadeStringUtil.isEmpty(getFilename()) && !isBatch.booleanValue()) {
                process.setFilename(Jade.getInstance().getHomeDir() + "work/" + getFilename());
            } else {
                process.setFilename(getFilename());
            }
            process.executeProcess();
            return true;
        } catch (Exception e) {
            JadeLogger.error(this, e);
            abort();
            return false;
        }
    }

    private CIJournal _findJournal(boolean anneeEncours, boolean modeInscription) throws Exception {
        try {
            CIJournal journal = null;
            if (anneeEncours) {
                String annee = Integer.toString(JACalendar.getYear(JACalendar.todayJJsMMsAAAA()));
                // recherche si le journal de l'année en cours est existant.
                CIJournalManager jourManager = new CIJournalManager();
                jourManager.setSession(getSession());
                // jourManager.getSession().connectSession(this.getSession());
                jourManager.setForAnneeCotisation(annee);
                jourManager.setForIdTypeInscription(CIJournal.CS_COTISATIONS_PERSONNELLES);
                jourManager.find(getTransaction());
                // prendre le premier journal du manager
                if (jourManager.getSize() > 0) {
                    journal = (CIJournal) jourManager.getEntity(0);
                } else {
                    // On créer un journal d'inscription CI
                    journal = new CIJournal();
                    journal.setSession(getSession());
                    journal.setAnneeCotisation(annee);
                    journal.setIdTypeInscription(CIJournal.CS_COTISATIONS_PERSONNELLES);
                    journal.setDateReception(dateReceptionForced);
                    if (modeInscription) {
                        journal.add(getTransaction());
                    }
                }
            } else {
                journal = new CIJournal();
                journal.setSession(getSession());
                journal.setIdTypeInscription(CIJournal.CS_DECISION_COT_PERS);
                journal.setRefExterneFacturation("");
                journal.setLibelle(getSession().getLabel("MSG_LIBELLE_JOURNAL_DECLARATION"));
                journal.setDateReception(dateReceptionForced);
                if (modeInscription) {
                    journal.add(getTransaction());

                }
            }
            if (getTransaction().hasErrors()) {
                getMemoryLog().logMessage("Erreur transaction :" + getTransaction().getErrors().toString(),
                        FWMessage.INFORMATION, "Importation CI");
                // Pour éviter de logger dans le mail et d'ajouter une erreur dans la trans alors que le cas est
                // géré
                getTransaction().clearErrorBuffer();
            }
            // Moide simulation : key vide => mnettre un id pour la récap
            if (!modeInscription && JadeStringUtil.isEmpty(journal.getIdJournal())) {
                journal.setIdJournal("1");
            }
            tableJournaux.put(journal.getIdJournal(), journal);
            return journal;
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return null;
        }
    }

    /*
     * Cherche le journal à utiliser pour ce record Si le journal a déjà été ouvert dans ce process, on le réutilise
     * sinon on le crée (si modeInscription)
     */
    private CIJournal _findJournal(boolean modeInscription, CIDeclarationRecord rec,
            TreeMap<String, Object> tableJournaux) throws Exception {
        // cle pour pouvoir stock un journal par affilié/année
        String key = _getKey(rec);
        CIJournal journal = null;
        String numAffFormate = CIUtil.formatNumeroAffilie(getSession(), rec.getNumeroAffilie());
        if (tableJournaux.containsKey(key)) {
            journal = (CIJournal) tableJournaux.get(key);
        } else {
            // on a pas encore eu à traité ce journal.
            // si il existe dejà dans la DB, on génère une erreur
            // BTransaction transactionJournalDS;
            CIJournalManager jrnMgr = new CIJournalManager();
            jrnMgr.setSession(getSession());
            jrnMgr.setForAnneeCotisation(rec.getAnnee());
            jrnMgr.setForIdTypeInscription(CIJournal.CS_DECLARATION_SALAIRES);
            jrnMgr.setForIdAffiliation(numAffFormate);
            int size = jrnMgr.getCount(getTransaction());
            journal = new CIJournal();
            journal.setSession(getSession());
            journal.setAnneeCotisation(rec.getAnnee());
            journal.setIdAffiliation(numAffFormate, true, false);
            journal.setTotalControle(getTotalControle().equals("") ? "200.00" : getTotalControle());
            journal.setLibelle(getSession().getLabel("MSG_LIBELLE_JOURNAL_DECLARATION"));
            journal.setIdTypeCompte(CIJournal.CS_PROVISOIRE);
            journal.setDateReception(dateReceptionForced);
            boolean wantCreatePrincipale = true;
            if (size == 0 && declarationSalaireType.isPrincipale()) {
                // si il n'existe pas encore dans la DB, on le crée (sauf en mode simulation)
                journal.setIdTypeInscription(CIJournal.CS_DECLARATION_SALAIRES);
            } else {
                wantCreatePrincipale = false;
                journal.setIdTypeInscription(CIJournal.CS_DECLARATION_COMPLEMENTAIRE);
            }
            try {
                if (modeInscription) {

                    // transactionJournalDS = new BTransaction(getSession());
                    // transactionJournalDS.openTransaction();

                    // mode inscription
                    journal.add(getTransaction());
                    if (!getTransaction().hasErrors()) {
                        if ("true".equalsIgnoreCase((accepteLienDraco))) {
                            declaration = null;
                            DSDeclarationListViewBean dsMgr = new DSDeclarationListViewBean();
                            dsMgr.setSession((BSession) getSessionDS(getSession()));
                            dsMgr.setForAffiliationId(journal.getIdAffiliation());
                            dsMgr.setForAnnee(journal.getAnneeCotisation());
                            dsMgr.setForEtat(DSDeclarationViewBean.CS_OUVERT);
                            if (wantCreatePrincipale) {
                                dsMgr.setForTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
                            } else {
                                dsMgr.setForTypeDeclaration(DSDeclarationViewBean.CS_COMPLEMENTAIRE);
                                if (!journal.getAnneeCotisation().equals(anneeVersement)) {
                                    dsMgr.setForTypeDeclaration(DSDeclarationViewBean.CS_SALAIRE_DIFFERES);
                                }

                            }
                            dsMgr.find(getTransaction(), BManager.SIZE_USEDEFAULT);
                            if (dsMgr.size() > 0) {
                                declaration = (DSDeclarationViewBean) dsMgr.getFirstEntity();
                                declaration.setIdJournal(journal.getIdJournal());
                                if (!JadeStringUtil.isIntegerEmpty(totalControle)) {
                                    declaration.setTotalControleDS(totalControle);
                                }
                                declaration.setProvenance(getProvenance());

                                declaration.setIdPucsFile(idsPucsFile);
                                declaration.update(getTransaction());
                            } else {
                                declaration = new DSDeclarationViewBean();
                                declaration.setAffiliationId(journal.getIdAffiliation());
                                declaration.setSession((BSession) getSessionDS(getSession()));
                                declaration.setNumeroAffilie(getNumAffilieBase());
                                declaration.setAnnee(journal.getAnneeCotisation());
                                declaration.setProvenance(getProvenance());
                                declaration.setIdPucsFile(idsPucsFile);
                                if (!JadeStringUtil.isIntegerEmpty(totalControle)) {
                                    declaration.setTotalControleDS(totalControle);
                                }
                                if (wantCreatePrincipale) {
                                    declaration.setTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
                                } else {
                                    declaration.setTypeDeclaration(DSDeclarationViewBean.CS_COMPLEMENTAIRE);
                                    if (!declaration.getAnnee().equals(anneeVersement)) {
                                        declaration.setTypeDeclaration(DSDeclarationViewBean.CS_SALAIRE_DIFFERES);
                                        // pour les salaires différés les intérêts moratoires sont directement exemptés
                                        declaration.setSoumisInteret(CAInteretMoratoire.CS_EXEMPTE);
                                        declaration.setAnneeTaux(anneeVersement);
                                    }
                                }
                                declaration.setEtat(DSDeclarationViewBean.CS_OUVERT);
                                declaration.setIdJournal(journal.getIdJournal());
                                // Modif FER 1-5-6-1
                                if (!JadeStringUtil.isBlankOrZero(dateReceptionForced)) {
                                    declaration.setDateRetourEff(dateReceptionForced);
                                }
                                declaration.add(getTransaction());

                                if (getTransaction().isRollbackOnly()) {

                                    getMemoryLog().logMessage(
                                            "Erreur transaction:" + getTransaction().getErrors().toString(),
                                            FWMessage.INFORMATION, "Importation CI");
                                    getTransaction().rollback();
                                    return null;

                                }
                            }

                        }
                    }
                    // Maj type de déclaration de salaire swissdec
                    if (!getTransaction().hasErrors() && declaration != null
                            && DSDeclarationViewBean.PROVENANCE_SWISSDEC.equalsIgnoreCase(getProvenance())
                            && !JadeStringUtil.isBlankOrZero(declaration.getAffiliationId())) {
                        AFAffiliation affiliation = declaration.getAffiliation();
                        if (affiliation != null && !affiliation.isNew()) {
                            affiliation.setDeclarationSalaire(CodeSystem.DS_SWISSDEC);
                            affiliation.setWantGenerationSuiviLAALPP(false);
                            affiliation.update(getTransaction());
                        }
                    }

                    if (getTransaction().hasErrors()) {
                        journal = null;
                        if (launcherImportPucsFileProcess != null) {
                            launcherImportPucsFileProcess
                                    .setImportStatutAFile(CIImportPucsFileProcess.IMPORT_STATUT_KO);
                        }
                        getMemoryLog().logMessage("Erreur transaction:" + getTransaction().getErrors().toString(),
                                FWMessage.INFORMATION, "Importation CI");
                        // Pour éviter de logger dans le mail et d'ajouter une erreur dans la transaction alors que le
                        // cas est géré
                        getTransaction().clearErrorBuffer();

                    }
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
                journal = null;
            }
            tableJournaux.put(key, journal);
        }
        return journal;
    }

    /*
     * Retourne un clé qui sera utilisée par pour trier les treemaps du process
     *
     * cette clé est composée comme suit : du n° affilié de l'année du n°AVS du mois de deb du mois de fin
     */
    private String _getFullKey(CIDeclarationRecord rec, int num) {
        String numAvs = rec.getNumeroAvs();
        String moisDebut = (rec.getMoisDebut() < 10) ? "0" + rec.getMoisDebut() : "" + rec.getMoisDebut();
        String moisFin = (rec.getMoisFin() < 10) ? "0" + rec.getMoisFin() : "" + rec.getMoisFin();
        return CIUtil.unFormatAVS(rec.getNumeroAffilie()) + rec.getAnnee() + numAvs + moisDebut + moisFin + num;
    }

    private void creationReleve(CIDeclarationRecord rec, AFAffiliation affilie, Map<String, String> sommeParCanton)
            throws Exception {

        String typeReleve = CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA;

        // Détermination du type (Si relevé déjà existant => complément sinon final)
        // Il faut tester dans les relevés et en compta
        AFApercuReleveManager manager = new AFApercuReleveManager();
        manager.setSession(getSession());
        manager.setForIdTiers(affilie.getIdTiers());
        manager.setForAffilieNumero(affilie.getAffilieNumero());
        manager.setFromDateDebut("01.01." + rec.getAnnee());
        manager.setUntilDateFin("31.12." + rec.getAnnee());
        manager.find();
        for (int i = 0; i < manager.size(); i++) {
            AFApercuReleve releve = (AFApercuReleve) manager.getEntity(i);
            // Détermination du type (Si relevé déjà existant => complément sinon final)
            if (CodeSystem.TYPE_RELEVE_DECOMP_FINAL.equalsIgnoreCase(releve.getType())
                    || CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equalsIgnoreCase(releve.getType())) {
                typeReleve = CodeSystem.TYPE_RELEVE_RECTIF;
            }
            // Si relevé en cours de facturation pour l'année => erreur
            if (CodeSystem.ETATS_RELEVE_SAISIE.equalsIgnoreCase(releve.getEtat())
                    || CodeSystem.ETATS_RELEVE_FACTURER.equalsIgnoreCase(releve.getEtat())) {

                _addError(getTransaction(),
                        getSession().getLabel("AFSEUL_RELEVE_EXISTANT") + " - " + getSession().getLabel("DEC_AFFILIE")
                                + " " + affilie.getAffilieNumero() + " - " + getSession().getLabel("DEC_ANNEE") + " "
                                + rec.getAnnee());
                totalErreur++;
                break;
            }
        }
        if (totalErreur == 0) {
            // Vérifier en compta s'il n'existe pas déjà un décompte => cas du traitement manuel
            if (CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equalsIgnoreCase(typeReleve)) {
                String role = (CaisseHelperFactory.getInstance()
                        .getRoleForAffilieParitaire(getSession().getApplication()));
                // Récupérer le compte annexe
                CACompteAnnexe ca = new CACompteAnnexe();
                ca.setISession(getSession());
                ca.setIdRole(role);
                ca.setIdExterneRole(rec.getNumeroAffilie());
                ca.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                ca.retrieve();
                if (!ca.isNew()) {
                    // Si le compte existe
                    CASectionManager mgr = new CASectionManager();
                    mgr.setISession(getSession());
                    mgr.setForIdCompteAnnexe(ca.getIdCompteAnnexe());
                    mgr.setLikeIdExterne(rec.getAnnee() + "13");
                    mgr.find();
                    if (mgr.getSize() > 0) {
                        typeReleve = CodeSystem.TYPE_RELEVE_RECTIF;
                    }
                }
            }

            // Pas de DS (car ne gère pas les AF seul et met en CI) => Génération d'un relevé
            AFApercuReleve releve = new AFApercuReleve();
            releve.setSession(getSession());
            releve.setAffilieNumero(rec.getNumeroAffilie());
            // Recherche id tiers
            releve.setIdTiers(affilie.getIdTiers());
            releve.setType(typeReleve);

            releve.setDateDebut(
                    CIUtil.giveDateDebutGreater(getSession(), "01.01." + rec.getAnnee(), affilie.getDateDebut()));
            releve.setDateFin(CIUtil.giveDateFinLower(getSession(), "31.12." + rec.getAnnee(), affilie.getDateFin()));

            releve.setInterets(CodeSystem.INTERET_MORATOIRE_AUTOMATIQUE);
            releve.setDateReception(dateReceptionForced);
            releve.setNewEtat(CodeSystem.ETATS_RELEVE_SAISIE);
            releve.setTotalCalculer(rec.getMontantAf());
            // releve.setTotalControl(rec.getMontantAf());
            releve.retrieveIdPassage();

            String dateJour = JACalendar.todayJJsMMsAAAA();
            String moisCourant = dateJour.substring(3, 5);
            releve.setIdExterneFacture(dateJour.substring(6, 10) + moisCourant + "000");
            if (CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA.equalsIgnoreCase(typeReleve)) {
                releve.setIdSousTypeFacture("227013");
            } else {
                releve.setIdSousTypeFacture("2270" + moisCourant);
            }
            releve.setWantControleCotisation(false);
            releve.add(getTransaction());
            if (!getTransaction().hasErrors()) {
                // Parcours de la map pour insérer les totaux par canton
                for (String mapKey : sommeParCanton.keySet()) {
                    String csCanton = CIUtil.codeUtilisateurToCodeSysteme(getTransaction(), mapKey, "PYCANTON",
                            getSession());
                    if (JadeStringUtil.isEmpty(csCanton)) {
                        _addError(getTransaction(),
                                getSession().getLabel("AFSEUL_CANTON_ERRONE") + " (" + rec.getCodeCanton() + ")" + " - "
                                        + getSession().getLabel("DEC_AFFILIE") + " " + affilie.getAffilieNumero()
                                        + " - " + getSession().getLabel("DEC_ANNEE") + " " + rec.getAnnee());
                        return;
                    }
                    // Recherche coti AF pour le canton concerné
                    AFCotisationManager cotisationManager = new AFCotisationManager();
                    cotisationManager.setForAffiliationId(affilie.getAffiliationId());
                    cotisationManager.setSession(getSession());
                    cotisationManager.setForAssuranceCanton(csCanton);
                    cotisationManager.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
                    cotisationManager.changeManagerSize(BManager.SIZE_NOLIMIT);
                    cotisationManager.find(getTransaction());
                    if (cotisationManager.getSize() == 0) {
                        _addError(getTransaction(),
                                getSession().getLabel("ERREUR_AUCUNE_COTISATION_AF") + " " + affilie.getAffilieNumero()
                                        + " - " + getSession().getLabel("CANTON") + " " + rec.getCodeCanton());
                        totalErreur++;
                        return;
                    }
                    for (int i = 0; i < cotisationManager.size(); i++) {
                        AFCotisation cotisation = (AFCotisation) cotisationManager.getEntity(i);
                        if ((BSessionUtil.compareDateFirstLowerOrEqual(getSession(), releve.getDateDebut(),
                                cotisation.getDateFin()) || JadeStringUtil.isBlankOrZero(cotisation.getDateFin()))
                                && BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), releve.getDateFin(),
                                        cotisation.getDateDebut())) {
                            if (!cotisation.getDateDebut().equals(cotisation.getDateFin())) {
                                creationLigneReleve(sommeParCanton.get(mapKey), releve, cotisation);
                            }
                        }
                    }
                }
            }
        }
    }

    private void creationLigneReleve(String totalParCanton, AFApercuReleve releve, AFCotisation cotisation)
            throws Exception {
        AFApercuReleveMontant montant = new AFApercuReleveMontant();
        montant.setSession(getSession());
        montant.setIdReleve(releve.getIdReleve());
        montant.setAssuranceId(cotisation.getAssuranceId());
        montant.setCotisationId(cotisation.getCotisationId());
        montant.setDateDebut(releve.getDateDebut());
        montant.setMasse(totalParCanton);
        //
        montant.setMasse(AFUtil.plafonneMasse(totalParCanton, getType(), cotisation.getAssuranceId(),
                releve.getDateDebut(), getSession(), ""));
        float taux = Float
                .parseFloat(JANumberFormatter.deQuote(cotisation.getTaux(releve.getDateFin(), montant.getMasse())));
        float cotiAnnuelleBrut = (Float.parseFloat(montant.getMasse()) * taux) / 100;
        cotiAnnuelleBrut = JANumberFormatter.round(cotiAnnuelleBrut, 0.05, 2, JANumberFormatter.NEAR);

        montant.setMontantCalculer(Float.toString(cotiAnnuelleBrut));
        montant.add(getTransaction());
    }

    /*
     * Rretourne un clé qui sera utilisée pour trier les treemaps du process cette clé est composée du n° affilié et de
     * l'année
     */
    private String _getKey(CIDeclarationRecord rec) throws Exception {
        if (CIDeclaration.CS_PUCS_CCJU.equals(Type)) {
            return CIUtil.UnFormatNumeroAffilie(getSession(), rec.getNumeroAffilie()) + "/" + rec.getAnnee() + "#"
                    + rec.getNomAffilie() + "*" + rec.getReturnCode();
        } else {
            return CIUtil.UnFormatNumeroAffilie(getSession(), rec.getNumeroAffilie()) + "/" + rec.getAnnee() + "#"
                    + rec.getNomAffilie();
        }

    }

    /*
     * Met à jour les TreeMap utilisés pour les summary par affilié/Année
     */
    private void _updateSummary(TreeMap<String, Object> hNbrInscriptionsTraites,
            TreeMap<String, Object> hMontantInscritionsTraites, TreeMap<String, Object> hNbrInscriptionsErreur,
            TreeMap<String, Object> hMontantInscriptionsErreur, TreeMap<String, Object> hNbrInscriptionsSuspens,
            TreeMap<String, Object> hMontantInscriptionsSuspens, TreeMap<String, Object> hNbrInscriptionsCI,
            TreeMap<String, Object> hMontantInscriptionsCI, TreeMap<String, Object> hNbrInscriptionsNegatives,
            TreeMap<String, Object> hMontantInscriptionsNegatives, long nbrInscriptionsTraites,
            FWCurrency montantInscritionsTraites, long nbrInscriptionsErreur, FWCurrency montantInscriptionsErreur,
            long nbrInscriptionsSuspens, FWCurrency montantInscriptionsSuspens, long nbrInscriptionsCI,
            FWCurrency montantInscriptionsCI, long nbrInscritptionsNegatives, FWCurrency montantInscriptionsNegatives,
            TreeMap<String, Object> hNbrInscriptionsTotalControle, long nbrInscriptionsTotalControle,
            TreeMap<String, Object> hMontantTotalControle, FWCurrency montantTotalControle, String key) {
        // compteur
        Long value = new Long(0L);
        if (hNbrInscriptionsTraites.get(key) == null) {
            hNbrInscriptionsTraites.put(key, new Long(0L));
        }
        if (hNbrInscriptionsErreur.get(key) == null) {
            hNbrInscriptionsErreur.put(key, new Long(0L));
        }
        if (hNbrInscriptionsSuspens.get(key) == null) {
            hNbrInscriptionsSuspens.put(key, new Long(0L));
        }
        if (hNbrInscriptionsCI.get(key) == null) {
            hNbrInscriptionsCI.put(key, new Long(0L));
        }
        if (hNbrInscriptionsNegatives.get(key) == null) {
            hNbrInscriptionsNegatives.put(key, new Long(0L));
        }
        if (hNbrInscriptionsTotalControle.get(key) == null) {
            hNbrInscriptionsTotalControle.put(key, new Long(0L));
        }
        value = (Long) hNbrInscriptionsTraites.get(key);
        hNbrInscriptionsTraites.put(key, new Long(value.longValue() + nbrInscriptionsTraites));
        totalTraite = totalTraite + nbrInscriptionsTraites;
        value = (Long) hNbrInscriptionsErreur.get(key);
        hNbrInscriptionsErreur.put(key, new Long(value.longValue() + nbrInscriptionsErreur));
        totalErreur = totalErreur + nbrInscriptionsErreur;
        value = (Long) hNbrInscriptionsSuspens.get(key);
        hNbrInscriptionsSuspens.put(key, new Long(value.longValue() + nbrInscriptionsSuspens));
        totalAvertissement = totalAvertissement + nbrInscriptionsSuspens;
        value = (Long) hNbrInscriptionsCI.get(key);
        hNbrInscriptionsCI.put(key, new Long(value.longValue() + nbrInscriptionsCI));
        value = (Long) hNbrInscriptionsNegatives.get(key);
        hNbrInscriptionsNegatives.put(key, new Long(value.longValue() + +nbrInscritptionsNegatives));
        value = (Long) hNbrInscriptionsTotalControle.get(key);
        hNbrInscriptionsTotalControle.put(key, new Long(value.longValue() + nbrInscriptionsTotalControle));
        // montants
        FWCurrency montant = new FWCurrency(0L);
        if (hMontantInscritionsTraites.get(key) == null) {
            hMontantInscritionsTraites.put(key, new FWCurrency("0"));
        }
        if (hMontantInscriptionsErreur.get(key) == null) {
            hMontantInscriptionsErreur.put(key, new FWCurrency("0"));
        }
        if (hMontantInscriptionsSuspens.get(key) == null) {
            hMontantInscriptionsSuspens.put(key, new FWCurrency("0"));
        }
        if (hMontantInscriptionsCI.get(key) == null) {
            hMontantInscriptionsCI.put(key, new FWCurrency("0"));
        }
        if (hMontantInscriptionsNegatives.get(key) == null) {
            hMontantInscriptionsNegatives.put(key, new FWCurrency("0"));
        }
        if (hMontantTotalControle.get(key) == null) {
            hMontantTotalControle.put(key, new FWCurrency("0"));
        }
        montant = (FWCurrency) hMontantInscritionsTraites.get(key);
        montant.add(montantInscritionsTraites);
        hMontantInscritionsTraites.put(key, montant);
        montant = (FWCurrency) hMontantInscriptionsErreur.get(key);
        montant.add(montantInscriptionsErreur);
        hMontantInscriptionsErreur.put(key, montant);
        montant = (FWCurrency) hMontantInscriptionsSuspens.get(key);
        montant.add(montantInscriptionsSuspens);
        hMontantInscriptionsSuspens.put(key, montant);
        montant = (FWCurrency) hMontantInscriptionsCI.get(key);
        montant.add(montantInscriptionsCI);
        hMontantInscriptionsCI.put(key, montant);
        montant = (FWCurrency) hMontantInscriptionsNegatives.get(key);
        montant.add(montantInscriptionsNegatives);
        hMontantInscriptionsNegatives.put(key, montant);
        montant = (FWCurrency) hMontantTotalControle.get(key);
        montant.add(montantTotalControle);
        hMontantTotalControle.put(key, montant);
    }

    @Override
    protected void _validate() throws Exception {
        // Nom de fichier : obligatoire
        if (JadeStringUtil.isEmpty(filename)) {
            this._addError(getSession().getLabel("FICHIER_DEC_ETRE_RENSEIGNE"));
        }
        // 5) adresse email obligatoire
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("EMAIL_ETRE_RENSEIGNE"));
        }
        // divers :

        setControleTransaction(true);
        if (!CIDeclaration.CS_AC.equals(Type) && !CIDeclaration.CS_AMI.equals(Type)) {
            setSendCompletionMail(true);
            setSendMailOnError(true);
        } else {
            setSendCompletionMail(false);
            setSendMailOnError(false);
        }
    }

    /**
     * @return
     */
    public String getAccepteAnneeEnCours() {
        return accepteAnneeEnCours;

    }

    /**
     * Returns the accepteEcrituresNegatives.
     *
     * @return String
     */
    public String getAccepteEcrituresNegatives() {
        return accepteEcrituresNegatives;
    }

    /**
     * @return
     */
    public String getAccepteLienDraco() {
        return accepteLienDraco;
    }

    /**
     * Returns the fromAnnee.
     *
     * @return String
     */
    public String getAnneeCotisation() {
        return anneeCotisation;
    }

    public String getDateReceptionForced() {
        return dateReceptionForced;
    }

    @Override
    public java.lang.String getEMailObject() {
        String mailObject = source + " " + numAffilieBase + " " + getSession().getLabel("DECLARATION_NB_ERREUR") + " "
                + String.valueOf(totalErreur) + " " + getSession().getLabel("DECLARATION_NB_AVERT") + " "
                + String.valueOf(totalAvertissement) + " " + getSession().getLabel("DECLARATION_NB_TOTAL")
                + String.valueOf(totalTraite);
        if (isOnError() || isErrorMontant || isErrorNbInscriptions || isAborted()) {
            if (traitementAFSeul) {
                return mailObject + " " + getSession().getLabel("EMAIL_IMPORTATION_AFSEULE_ECHEC");
            } else {
                return mailObject + " " + getSession().getLabel("EMAIL_IMPORTATION_ECHEC");
            }
        } else {
            if (traitementAFSeul) {
                return mailObject + " " + getSession().getLabel("EMAIL_IMPORTATION_AFSEULE_SUCCES");
            } else {
                return mailObject + " " + getSession().getLabel("EMAIL_IMPORTATION_SUCCES");
            }
        }
    }

    /**
     * Returns the fileName.
     *
     * @return String
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Returns the forNumeroAffilie.
     *
     * @return String
     */
    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    public Boolean getIsBatch() {
        return isBatch;
    }

    public CIImportPucsFileProcess getLauncherImportPucsFileProcess() {
        return launcherImportPucsFileProcess;
    }

    /**
     * Returns the nombreInscriptions.
     *
     * @return String
     */
    public String getNombreInscriptions() {
        return nombreInscriptions;
    }

    public String getNumAffilieBase() {
        return numAffilieBase;
    }

    public String getProvenance() {
        return provenance;
    }

    public BISession getSessionCI(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionPavo");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("PAVO").newSession(local);
            local.setAttribute("sessionPavo", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    public BISession getSessionDS(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionDraco");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("DRACO").newSession(local);
            local.setAttribute("sessionDraco", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /**
     * Returns the simulation.
     *
     * @return String
     */
    public String getSimulation() {
        return simulation;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Returns the totalControle.
     *
     * @return String
     */
    public String getTotalControle() {
        return totalControle;
    }

    /**
     * Returns the type.
     *
     * @return String
     */
    public String getType() {
        return Type;
    }

    /**
     * Méthode qui insére les écritures provisoires de l'année dans la déclaration
     *
     * @throws Exception
     */
    private void importeInscriptionsProvisoires(DSInscriptionsIndividuellesListeViewBean declaration) throws Exception {
        if (JadeStringUtil.isBlankOrZero(declaration.getAffiliationId())) {
            return;
        }
        CIEcritureManager mgrEcrCI = new CIEcritureManager();
        mgrEcrCI.setSession(getSession());
        mgrEcrCI.setForAffilie(declaration.getNumeroAffilie());
        mgrEcrCI.changeManagerSize(BManager.SIZE_NOLIMIT);
        mgrEcrCI.setForAnnee(declaration.getAnnee());
        mgrEcrCI.setForCode(CIEcriture.CS_CODE_PROVISOIRE);
        mgrEcrCI.find(getTransaction());

        for (int i = 0; i < mgrEcrCI.size(); i++) {
            CIEcriture ecrCI = (CIEcriture) mgrEcrCI.getEntity(i);
            DSInscriptionsIndividuelles insc = new DSInscriptionsIndividuelles();
            insc.setSession(getSession());
            insc.setIdEcrtirueCI(ecrCI.getEcritureId());
            insc.setCompteIndividuelId(ecrCI.getCompteIndividuelId());
            insc.setMontant(ecrCI.getMontant());
            insc.setIdDeclaration(declaration.getIdDeclaration());
            insc.setMontantAf(ecrCI.getMontantSigne());
            // insc.setNotWantCI(true);
            insc.setDeclaration(declaration);
            insc.setNumeroAvs(ecrCI.getAvs());
            insc.setCategoriePerso(ecrCI.getCategoriePersonnel());
            insc.setAnneeInsc(ecrCI.getAnnee());
            insc.setProvisoire(true);
            try {
                insc.add(getTransaction());
                getMemoryLog().logMessage(
                        "Le numéro : " + JAStringFormatter.formatAVS(ecrCI.getAvs())
                                + " a été réaffecté à la déclaration",
                        FWMessage.INFORMATION, "Pré-remplissage de la déclaration");
                if (!getTransaction().hasErrors()) {
                    declaration.retrieve(getTransaction());
                    declaration.setIdJournal(insc.donneIdJournal(getTransaction()));
                    declaration.wantCallMethodAfter(false);
                    declaration.wantCallMethodBefore(false);
                    declaration.update(getTransaction());
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
            if (!getTransaction().hasErrors()) {
                getTransaction().commit();
            } else {
                getTransaction().rollback();
            }
        }

    }

    /**
     * Returns the hasChild.
     *
     * @return boolean
     */
    public boolean isHasChild() {
        return hasChild;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    private void receptionDS(TreeMap<?, ?> map, TreeMap<String, Object> journal, boolean modeInscription)
            throws Exception {

        Iterator<?> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String chaine = (String) iter.next();
            if (chaine.length() > 5) {
                CIApplication application = (CIApplication) GlobazServer.getCurrentSystem()
                        .getApplication(CIApplication.DEFAULT_APPLICATION_PAVO);
                String returnCode = chaine.substring(0, 1);
                String annee = chaine.substring(1, 5);
                String numeroAffilie = chaine.substring(5);

                AFAffiliation affilie = application.getAffilieByNo(getSession(),
                        CIUtil.formatNumeroAffilie(getSession(), numeroAffilie), true, false, "", "", annee, "", "");
                String idAffiliation = affilie.getAffiliationId();
                DSDeclarationViewBean declaration = new DSDeclarationViewBean();
                declaration = new DSDeclarationViewBean();
                declaration.setAffiliationId(idAffiliation);
                declaration.setSession((BSession) getSessionDS(getSession()));
                declaration.setAnnee(annee);
                // On ne gère pas les compléments = > aucune raison d'avoir un complément sans salarié
                declaration.setTypeDeclaration(DSDeclarationViewBean.CS_PRINCIPALE);
                declaration.setEtat(DSDeclarationViewBean.CS_OUVERT);
                if (!JadeStringUtil.isBlankOrZero(dateReceptionForced)) {
                    declaration.setDateRetourEff(dateReceptionForced);
                }

                declaration.add(getTransaction());
                CIDeclarationRecord rec = new CIDeclarationRecord();
                rec.setNomAffilie("Réception effectuée avec fichier vide");
                rec.setReturnCode(returnCode);
                rec.setAnnee(annee);
                rec.setNumeroAffilie(numeroAffilie);
                this._findJournal(false, rec, journal);

                _updateSummary(hNbrInscriptionsTraites, hMontantInscritionsTraites, hNbrInscriptionsErreur,
                        hMontantInscriptionsErreur, hNbrInscriptionsSuspens, hMontantInscriptionsSuspens,
                        hNbrInscriptionsCI, hMontantInscriptionsCI, hNbrInscriptionsNegatives,
                        hMontantInscriptionsNegatives, 0, new FWCurrency(0), 0, new FWCurrency(0), 0, new FWCurrency(0),
                        0, new FWCurrency(0), 0, new FWCurrency(0), hNbrInscriptionsTotalControle, (0),
                        hMontantTotalControle, new FWCurrency(0), _getKey(rec));

            }
        }
    }

    /**
     * @param string
     */
    public void setAccepteAnneeEnCours(String accepteAnneeEnCours) {
        this.accepteAnneeEnCours = accepteAnneeEnCours;
    }

    /**
     * Sets the accepteEcrituresNegatives.
     *
     * @param accepteEcrituresNegatives
     *            The accepteEcrituresNegatives to set
     */
    public void setAccepteEcrituresNegatives(String accepteEcrituresNegatives) {
        this.accepteEcrituresNegatives = accepteEcrituresNegatives;
    }

    /**
     * @param string
     */
    public void setAccepteLienDraco(String string) {
        accepteLienDraco = string;
    }

    /**
     * Sets the fromAnnee.
     *
     * @param fromAnnee
     *            The fromAnnee to set
     */
    public void setAnneeCotisation(String fromAnnee) {
        anneeCotisation = fromAnnee;
    }

    public void setDateReceptionForced(String dateReceptionForced) {
        this.dateReceptionForced = dateReceptionForced;
    }

    /**
     * Ajoute ou non des infomations de traitement dans la console. Date de création : (25.11.2002 10:27:48)
     *
     * @param newEchoToConsole
     *            mettre à true si ces informations doivent apparaître dans la console.
     */
    public void setEchoToConsole(boolean newEchoToConsole) {
    }

    /**
     * Sets the fileName.
     *
     * @param fileName
     *            The fileName to set
     */
    public void setFilename(String fileName) {
        filename = fileName;
    }

    /**
     * Sets the forNumeroAffilie.
     *
     * @param forNumeroAffilie
     *            The forNumeroAffilie to set
     */
    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    /**
     * Sets the hasChild.
     *
     * @param hasChild
     *            The hasChild to set
     */
    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public void setIsBatch(Boolean isBatch) {
        this.isBatch = isBatch;
    }

    public void setLauncherImportPucsFileProcess(CIImportPucsFileProcess launcherImportPucsFileProcess) {
        this.launcherImportPucsFileProcess = launcherImportPucsFileProcess;
    }

    /**
     * Sets the nombreInscriptions.
     *
     * @param nombreInscriptions
     *            The nombreInscriptions to set
     */
    public void setNombreInscriptions(String nombreInscriptions) {
        this.nombreInscriptions = nombreInscriptions;
    }

    public void setNumAffilieBase(String numAffilieBase) {
        this.numAffilieBase = numAffilieBase;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    /**
     * Sets the simulation.
     *
     * @param simulation
     *            The simulation to set
     */
    public void setSimulation(String simulation) {
        this.simulation = simulation;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Sets the totalControle.
     *
     * @param totalControle
     *            The totalControle to set
     */
    public void setTotalControle(String totalControle) {
        this.totalControle = JANumberFormatter.deQuote(totalControle);
    }

    /**
     * Sets the type.
     *
     * @param type
     *            The type to set
     */
    public void setType(String type) {
        Type = type;
    }

    public DSDeclarationViewBean getDeclaration() {
        return declaration;
    }

    public DSValideMontantDeclarationProcess getTheCalculMasseProcess() {
        return theCalculMasseProcess;
    }

    public boolean hasDifferenceAc() {
        return hasDifferenceAc;
    }

    public boolean hasCasRejete() {
        return totalErreur > 0;
    }

    public boolean getValidationAutomatique() {
        return validationAutomatique;
    }

    public void setValidationAutomatique(boolean validationAutomatique) {
        this.validationAutomatique = validationAutomatique;
    }

    public String getIdsPucsFile() {
        return idsPucsFile;
    }

    public void setIdsPucsFile(String idsPucsFile) {
        this.idsPucsFile = idsPucsFile;
    }

    public DeclarationSalaireType getDeclarationSalaireType() {
        return declarationSalaireType;
    }

    public void setDeclarationSalaireType(DeclarationSalaireType declarationSalaireType) {
        this.declarationSalaireType = declarationSalaireType;
    }

    public String getAnneeVersement() {
        return anneeVersement;
    }

    public void setAnneeVersement(String anneeVersement) {
        this.anneeVersement = anneeVersement;
    }

}
