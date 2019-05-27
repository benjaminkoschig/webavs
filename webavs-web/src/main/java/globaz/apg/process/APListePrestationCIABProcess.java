/*
 * Créé le 15 juin 05
 */
package globaz.apg.process;

import globaz.apg.application.APApplication;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.excel.APListePrestationCIABExcel;
import globaz.apg.pojo.PrestationCIABAssuranceComplPojo;
import globaz.apg.pojo.PrestationCIABLigneRecapitulationPojo;
import globaz.apg.pojo.PrestationCIABPojo;
import globaz.apg.pojo.PrestationVerseeLigneRecapitulationPojo;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.admin.JadeAdminServiceLocatorProvider;
import globaz.jade.client.util.JadeConversionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.context.JadeThreadContext;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.phenix.db.taxation.definitive.CPTaxationDefinitiveManager;
import globaz.pyxis.util.TIToolBox;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author eniv
 * 
 *         Ce processus fournit au format Excel une liste de toutes les prestations payées par employeur pour une
 *         période donnée
 * 
 *         L'utilisateur peut choisir d'envoyer le fichier Excel généré par ce processus en GED
 * 
 * 
 * 
 */
public class APListePrestationCIABProcess extends BProcess {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public final static String LABEL_CAS = "DOC_LISTE_PRESTATION_VERSEE_CAS";
    public final static String LABEL_DATE_PAIEMENT = "DOC_LISTE_PRESTATION_VERSEE_DATE_PAIEMENT";
    public final static String LABEL_DATE_TRAITEMENT = "DOC_LISTE_PRESTATION_VERSEE_DATE_TRAITEMENT";
    public final static String LABEL_GENRE_SERVICE = "DOC_LISTE_PRESTATION_VERSEE_GENRE_SERVICE";
    public final static String LABEL_MONTANT_BRUT = "DOC_LISTE_PRESTATION_VERSEE_MONTANT_BRUT";
    public final static String LABEL_NOM_PRENOM = "DOC_LISTE_PRESTATION_VERSEE_NOM_PRENOM";

    public final static String LABEL_NOMBRE_CAS = "DOC_LISTE_PRESTATION_VERSEE_NOMBRE_CAS";
    public final static String LABEL_NSS = "DOC_LISTE_PRESTATION_VERSEE_NSS";
    public final static String LABEL_NUMERO_AFFILIE = "DOC_LISTE_PRESTATION_VERSEE_NUMERO_AFFILIE";

    public final static String LABEL_PERIODE = "DOC_LISTE_PRESTATION_VERSEE_PERIODE";
    public final static String LABEL_PERIODE_DETAIL = "DOC_LISTE_PRESTATION_VERSEE_PERIODE_DETAIL";
    public final static String LABEL_RECAPITULATION = "DOC_LISTE_PRESTATION_VERSEE_RECAPITULATION";
    public final static String LABEL_SELECTEUR_PRESTATION = "DOC_LISTE_PRESTATION_VERSEE_SELECTEUR_PRESTATION";
    public final static String LABEL_TITRE = "DOC_LISTE_PRESTATION_CIAB_TITRE";
    public final static String LABEL_TOTAUX = "DOC_LISTE_PRESTATION_VERSEE_TOTAUX";

    public final static String LABEL_ASSURANCE_COMPLEMENT_PARITAIRE_JU = "ASSURANCE_COMPLEMENT_PARITAIRE_JU";
    public final static String LABEL_ASSURANCE_COMPLEMENT_PARITAIRE_BE = "ASSURANCE_COMPLEMENT_PARITAIRE_BE";
    public final static String LABEL_ASSURANCE_COMPLEMENT_PERSONNEL_JU = "ASSURANCE_COMPLEMENT_PERSONNEL_JU";
    public final static String LABEL_ASSURANCE_COMPLEMENT_PERSONNEL_BE = "ASSURANCE_COMPLEMENT_PERSONNEL_BE";
    public final static String LABEL_TOTAUX_JOURS_ISOLES = "TOTAUX_JOURS_ISOLES";
    public final static String LABEL_COMPLEMENT_CIAB = "COMPLEMENT_CIAB";
    public final static String LABEL_TOTAL_COMPLEMENT_CIAB = "TOTAL_COMPLEMENT_CIAB";

    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_DEBUT = "DATE_DEBUT";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_FIN = "DATE_FIN";

    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_PAIEMENT = "DATE_PAIEMENT";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_PRESTATION = "GENRE_PRESTATION";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_PRESTATION_LIBELLE = "GENRE_PRESTATION_LIBELLE";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_SERVICE = "GENRE_SERVICE";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_SERVICE_LIBELLE = "GENRE_SERVICE_LIBELLE";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_ID_TIERS = "ID_TIERS";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_MONTANT_BRUT = "MONTANT_BRUT";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NOM = "NOM";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NSS = "NSS";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NUMERO_AFFILIE = "NUMERO_AFFILIE";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NOM_AFFILIE = "NOM_AFFILIE";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_PRENOM = "PRENOM";
    public static final String REQUETE_LISTE_PRESTATION_VERSEE_COL_CODE_ASSURANCE_COMPL = "CODE_ASSURANCE_COMPLEMENTAIRE";
    public static final String SELECTEUR_PRESTATION_PAR_PAIEMENT = "52025001";
    public static final String SELECTEUR_PRESTATION_PAR_PERIODE = "52025002";

    private String dateDebut;
    private String dateFin;
    private Boolean envoyerGed;

    private PrestationCIABPojo listePrestationCIABPojo;
    private Map<String, PrestationVerseeLigneRecapitulationPojo> ligneRecapitulationPrestationCIAB;
    private String schemaDBWithTablePrefix;
    private String selecteurPrestation;

    private String idAssuranceParitaireJU;
    private String idAssurancePersonnelJU;
    private String idAssuranceParitaireBE;
    private String idAssurancePersonnelBE;

    public APListePrestationCIABProcess() {
        super();
        schemaDBWithTablePrefix = TIToolBox.getCollection();
        dateDebut = "";
        dateFin = "";
        selecteurPrestation = "";
        listePrestationCIABPojo = new PrestationCIABPojo();
        envoyerGed = new Boolean(false);

        idAssuranceParitaireJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
        idAssurancePersonnelJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);

        idAssuranceParitaireBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
        idAssurancePersonnelBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);

    }

    public APListePrestationCIABProcess(BSession session) {
        super(session);
        schemaDBWithTablePrefix = TIToolBox.getCollection();
        dateDebut = "";
        dateFin = "";
        selecteurPrestation = "";
        listePrestationCIABPojo = new PrestationCIABPojo();
        envoyerGed = new Boolean(false);

        idAssuranceParitaireJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID);
        idAssurancePersonnelJU = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID);

        idAssuranceParitaireBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID);
        idAssurancePersonnelBE = JadePropertiesService.getInstance()
                .getProperty(APApplication.PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID);

    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {

        boolean success = true;
        String errorMessage = "";

        try {

            // initialisation du thread context et utilisation du contextjdbc
            JadeThreadContext threadContext = initThreadContext(getSession());
            JadeThreadActivator.startUsingJdbcContext(Thread.currentThread(), threadContext.getContext());

            // voir javadoc de la méthode
            // Création de la ListePrestationCIABPojo
            creerListePrestationCIABPojo();

            if (!listePrestationCIABPojo.getListPrestationCIAB().isEmpty()) {
                // création des documents

                JadePublishDocument documentAPublierExcel = creerListePrestationCIABDocumentsExcel();

                // publication des documents
                publierListePrestationVerseeDocumentsExcel(documentAPublierExcel);
            } else {
                getMemoryLog().logMessage(
                        getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_MAIL_BODY_AUCUNE_DONNEE_A_IMPRIMER"),
                        FWMessage.INFORMATION, this.getClass().getName());
            }

        } catch (Exception e) {
            success = false;
            errorMessage = e.toString();
        } finally {
            // stopper l'utilisation du context
            JadeThreadActivator.stopUsingContext(Thread.currentThread());
        }

        if (!success || isAborted() || isOnError() || getSession().hasErrors()) {

            success = false;

            getMemoryLog().logMessage(errorMessage, FWMessage.FATAL, this.getClass().getName());
            this._addError(getTransaction(), errorMessage);
        }

        return success;
    }

    @Override
    protected void _validate() throws Exception {

        boolean isDateDebutValide = true;
        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            isDateDebutValide = false;
            getSession().addError(getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_DATE_DEBUT_MANDATORY"));
        }

        boolean isDateFinValide = true;
        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            isDateFinValide = false;
            getSession().addError(getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_DATE_FIN_MANDATORY"));
        }

        if (isDateDebutValide && isDateFinValide && JadeDateUtil.isDateAfter(dateDebut, dateFin)) {
            getSession().addError(getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_DATE_DEBUT_APRES_DATE_FIN"));
        }

        if (isDateDebutValide && JadeDateUtil.isDateBefore(dateDebut, "01.01.1948")) {
            getSession().addError(getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_DATE_DEBUT_AVANT_01_01_1948"));
        }

        if (isDateFinValide && JadeDateUtil.isDateAfter(dateFin, JACalendar.todayJJsMMsAAAA())) {
            getSession().addError(getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_DATE_FIN_APRES_DATE_DU_JOUR"));
        }

    }

    private JadePublishDocument creerListePrestationCIABDocumentsExcel() throws IOException {

        APListePrestationCIABExcel listePrestationCIABExcel = new APListePrestationCIABExcel(getSession());
        listePrestationCIABExcel.setListePrestationCIABPojo(listePrestationCIABPojo);
        listePrestationCIABExcel.creerDocument();

        JadePublishDocumentInfo docInfoExcel = JadePublishDocumentInfoProvider.newInstance(this);
        docInfoExcel.setTemplateName("");
        docInfoExcel.setDocumentTypeNumber(APListePrestationCIABExcel.NUM_INFOROM);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);

        return new JadePublishDocument(listePrestationCIABExcel.getOutputFile(), docInfoExcel);

    }

    /**
     * 
     * Cette méthode a pour but de créer une liste de PrestationVerseePojo
     * 
     * Un PrestationVerseePojo se rapporte à un affilié et contient pour ce dernier une liste des prestations versées De
     * plus, il contient une map récapitulant le nombre de prestations versées et le montant de ces dernières par genre
     * de service
     * 
     * La liste de PrestationVerseePojo est donc un container de données qui contient toutes les informations
     * nécessaires pour créer la liste des prestations versées demandée dans le mandat InfoRom457 (peu importe le format
     * de sortie souhaité PDF, Excel...)
     */
    private void creerListePrestationCIABPojo() throws Exception {

        String selecteurPrestationLibelle = getSession().getCodeLibelle(selecteurPrestation);

        // Création de la requête
        String sqlQueryListePrestationCIAB = getSqlListePrestationCIAB();

        // Exécution de la requête
        List<Map<String, String>> listMapResultQueryListePrestationCIAB = executeQuery(sqlQueryListePrestationCIAB);

        // traitement des données
        PrestationCIABLigneRecapitulationPojo aPrestationCIABLigneRecapPojo = null;
        ArrayList<PrestationCIABAssuranceComplPojo> aListPrestationCIABAssuranceCompl = new ArrayList<>();
        PrestationCIABAssuranceComplPojo aPrestationCIABAssuranceCompl = new PrestationCIABAssuranceComplPojo();

        listePrestationCIABPojo.setDateDebut(getDateDebut());
        listePrestationCIABPojo.setDateFin(getDateFin());
        listePrestationCIABPojo.setSelecteurPrestationLibelle(selecteurPrestationLibelle);


        String codeServicePrecedent = "";
        String codeAssuranceComplPrecedent = "";
        String codeAssuranceCompl = "";
        String libelleAssuranceCompl = "";
        String libelleAssuranceComplPrecedent = "";
        String libelleCodeService = "";
        String libelleCodeServicePrecedent = "";
        String codeService = "";
        String codePrestation = "";
        double totalMontantBrutService = 0;
        double totalMontantJourIsole = 0;
        int totalNbCasService = 0;
        double montantBrutService = 0;
        int nbCasService = 0;
        int totalJourIsole = 0;
        boolean firstTime = true;

        // Parcours de chaque ligne retournée par la requête sql
        for (Map<String, String> aMapRowResultQueryListePrestationCIAB : listMapResultQueryListePrestationCIAB) {

            // Chargement des variables récupérés de la requête
            codeService = aMapRowResultQueryListePrestationCIAB
                    .get(APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_SERVICE);
            libelleCodeService = aMapRowResultQueryListePrestationCIAB
                    .get(APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_SERVICE_LIBELLE);
            codeAssuranceCompl = aMapRowResultQueryListePrestationCIAB
                    .get(APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_CODE_ASSURANCE_COMPL);

            if (codeAssuranceCompl.equalsIgnoreCase(idAssuranceParitaireJU)){
                libelleAssuranceCompl = getSession().getLabel(LABEL_ASSURANCE_COMPLEMENT_PARITAIRE_JU);
            } else if (codeAssuranceCompl.equalsIgnoreCase(idAssuranceParitaireBE)){
                libelleAssuranceCompl = getSession().getLabel(LABEL_ASSURANCE_COMPLEMENT_PARITAIRE_BE);
            } else if (codeAssuranceCompl.equalsIgnoreCase(idAssurancePersonnelJU)){
                libelleAssuranceCompl = getSession().getLabel(LABEL_ASSURANCE_COMPLEMENT_PERSONNEL_JU);
            } else if (codeAssuranceCompl.equalsIgnoreCase(idAssurancePersonnelBE)) {
                libelleAssuranceCompl = getSession().getLabel(LABEL_ASSURANCE_COMPLEMENT_PERSONNEL_BE);
            }

            codePrestation = aMapRowResultQueryListePrestationCIAB
                    .get(APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_PRESTATION);

            String montantBrut = aMapRowResultQueryListePrestationCIAB
                    .get(APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_MONTANT_BRUT);

            //La boucle se déroule en 2 parties, d'abord le traitement des Compléements CIAB
            if (codePrestation.equalsIgnoreCase(String.valueOf(APTypeDePrestation.COMPCIAB.getCodesystem()))){
                PrestationCIABAssuranceComplPojo aPrestationCIDABAssCompl = new PrestationCIABAssuranceComplPojo();
                if (listePrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().containsKey(codeAssuranceCompl)) {
                    aPrestationCIDABAssCompl = listePrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().get(codeAssuranceCompl);
                }
                aPrestationCIDABAssCompl.setNbCas(aPrestationCIDABAssCompl.getNbCas()+1);
                aPrestationCIDABAssCompl.setMontantBrut(aPrestationCIDABAssCompl.getMontantBrut()
                        + Double.valueOf(montantBrut).doubleValue());
                aPrestationCIDABAssCompl.setCodeAssuranceCompl(codeAssuranceCompl);
                aPrestationCIDABAssCompl.setLibelleAssuranceCompl(libelleAssuranceCompl);
                totalMontantBrutService += Double.valueOf(montantBrut).doubleValue();
                totalNbCasService++;
                listePrestationCIABPojo.getMapPrestationComplementaireAssuranceCIAB().put(codeAssuranceCompl
                        , aPrestationCIDABAssCompl);

                // Ensuite les jours isolés
            } else{
                // Lors de la première execution des jours isolés, nous chargeons les variables et enregistrons les totaux des compléments CIAB
                if (firstTime) {
                    listePrestationCIABPojo.setTotalMontantBrutComplementaire(totalMontantBrutService);
                    listePrestationCIABPojo.setTotalNbCasComplementaire(totalNbCasService);
                    totalMontantBrutService = 0;
                    totalNbCasService = 0;
                    codeServicePrecedent = codeService;
                    codeAssuranceComplPrecedent = codeAssuranceCompl;
                    libelleAssuranceComplPrecedent = libelleAssuranceCompl;
                    libelleCodeServicePrecedent = libelleCodeService;
                }
                if (!codeServicePrecedent.equalsIgnoreCase(codeService)) {
                    // A chaque changement de code service un nouveau PrestationCIABLigneRecapitulationPojo et
                    // une PrestationCIABAssuranceCompl sont créés et ajoutés à la liste
                    // listePrestationCIABPojo
                    aPrestationCIABAssuranceCompl = new PrestationCIABAssuranceComplPojo();
                    aPrestationCIABAssuranceCompl.setMontantBrut(montantBrutService);
                    aPrestationCIABAssuranceCompl.setNbCas(nbCasService);
                    aPrestationCIABAssuranceCompl.setCodeAssuranceCompl(codeAssuranceComplPrecedent);
                    aPrestationCIABAssuranceCompl.setLibelleAssuranceCompl(libelleAssuranceComplPrecedent);
                    aListPrestationCIABAssuranceCompl.add(aPrestationCIABAssuranceCompl);


                    aPrestationCIABLigneRecapPojo = new PrestationCIABLigneRecapitulationPojo();
                    aPrestationCIABLigneRecapPojo.setCodeService(codeServicePrecedent);
                    aPrestationCIABLigneRecapPojo.setLibelleService(libelleCodeServicePrecedent);
                    aPrestationCIABLigneRecapPojo.setListPrestationCIABAssuranceCompl(aListPrestationCIABAssuranceCompl);
                    aPrestationCIABLigneRecapPojo.setTotalMontantBrutService(totalMontantBrutService);
                    aPrestationCIABLigneRecapPojo.setTotalNbCasService(totalNbCasService);



                    listePrestationCIABPojo.getListPrestationCIAB().add(aPrestationCIABLigneRecapPojo);
                    codeServicePrecedent = codeService;
                    libelleCodeServicePrecedent = libelleCodeService;
                    codeAssuranceComplPrecedent = codeAssuranceCompl;
                    libelleAssuranceComplPrecedent = libelleAssuranceCompl;
                    aListPrestationCIABAssuranceCompl = new ArrayList<>();
                    totalMontantBrutService = 0;
                    totalNbCasService = 0;
                    montantBrutService = 0;
                    nbCasService = 0;
                } else if (!codeAssuranceComplPrecedent.equalsIgnoreCase(codeAssuranceCompl)){
                    // Si le code service ne change pas, mais que le code Assurance n'est pas le même
                    // on ajoute PrestationCIABAssuranceCompl à notre PrestationCIABLigneRecapPojo
                    aPrestationCIABAssuranceCompl = new PrestationCIABAssuranceComplPojo();
                    aPrestationCIABAssuranceCompl.setMontantBrut(montantBrutService);
                    aPrestationCIABAssuranceCompl.setNbCas(nbCasService);
                    aPrestationCIABAssuranceCompl.setCodeAssuranceCompl(codeAssuranceComplPrecedent);
                    aPrestationCIABAssuranceCompl.setLibelleAssuranceCompl(libelleAssuranceComplPrecedent);
                    aListPrestationCIABAssuranceCompl.add(aPrestationCIABAssuranceCompl);
                    montantBrutService = 0;
                    nbCasService = 0;
                    codeAssuranceComplPrecedent = codeAssuranceCompl;
                    libelleAssuranceComplPrecedent = libelleAssuranceCompl;
                }
                montantBrutService += Double.valueOf(montantBrut).doubleValue();
                totalMontantBrutService += Double.valueOf(montantBrut).doubleValue();
                totalMontantJourIsole += Double.valueOf(montantBrut).doubleValue();
                totalNbCasService++;
                totalJourIsole++;
                nbCasService++;


                codeService = codeService.trim();
                firstTime = false;
            }

        }

        // Enregistrement du dernier élément de la liste
        aPrestationCIABAssuranceCompl = new PrestationCIABAssuranceComplPojo();
        aPrestationCIABAssuranceCompl.setMontantBrut(montantBrutService);
        aPrestationCIABAssuranceCompl.setNbCas(nbCasService);
        aPrestationCIABAssuranceCompl.setCodeAssuranceCompl(codeAssuranceCompl);
        aPrestationCIABAssuranceCompl.setLibelleAssuranceCompl(libelleAssuranceCompl);
        aListPrestationCIABAssuranceCompl.add(aPrestationCIABAssuranceCompl);


        aPrestationCIABLigneRecapPojo = new PrestationCIABLigneRecapitulationPojo();
        aPrestationCIABLigneRecapPojo.setCodeService(codeService);
        aPrestationCIABLigneRecapPojo.setLibelleService(libelleCodeService);
        aPrestationCIABLigneRecapPojo.setListPrestationCIABAssuranceCompl(aListPrestationCIABAssuranceCompl);
        aPrestationCIABLigneRecapPojo.setTotalMontantBrutService(totalMontantBrutService);
        aPrestationCIABLigneRecapPojo.setTotalNbCasService(totalNbCasService);

        if (!aPrestationCIABLigneRecapPojo.getCodeService().isEmpty()){
            listePrestationCIABPojo.getListPrestationCIAB().add(aPrestationCIABLigneRecapPojo);
            listePrestationCIABPojo.setTotalJourIsole(totalJourIsole);
            listePrestationCIABPojo.setTotalMontantBrutJourIsole(totalMontantJourIsole);
        }
    }

    private List<Map<String, String>> executeQuery(String sql) throws JadePersistenceException, JAException {

        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        List<Map<String, String>> results = new ArrayList<>();

        String dateDebutAMJ = (new JADate(dateDebut)).toAMJ().toString();
        String dateFinAMJ = (new JADate(dateFin)).toAMJ().toString();


        try {
            stmt = JadeThread.currentJdbcConnection().prepareStatement(sql);
            stmt.setString(1, CPTaxationDefinitiveManager.CS_ETAT_PRESTATION_DEFINITIF);
            stmt.setString(2, dateDebutAMJ);
            stmt.setString(3, dateFinAMJ);
            stmt.setInt(4, APTypeDePrestation.COMPCIAB.getCodesystem());
            stmt.setInt(5, APTypeDePrestation.JOUR_ISOLE.getCodesystem());
            stmt.setString(6, idAssuranceParitaireJU);
            stmt.setString(7, idAssuranceParitaireBE);
            stmt.setString(8, idAssurancePersonnelJU);
            stmt.setString(9, idAssurancePersonnelBE);
            stmt.setString(10, idAssurancePersonnelJU);
            stmt.setString(11, idAssurancePersonnelBE);
            stmt.setString(12, idAssuranceParitaireJU);
            stmt.setString(13, idAssuranceParitaireBE);
            resultSet = stmt.executeQuery();


            ResultSetMetaData md = resultSet.getMetaData();
            int columns = md.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();

                // Attention ! La première colonne du Resultset est 1 et non 0
                for (int i = 1; i <= columns; i++) row.put(md.getColumnName(i), resultSet.getString(i));

                results.add(row);
            }

        } catch (SQLException e) {
            throw new JadePersistenceException(getName() + " - " + "Unable to execute query (" + sql
                    + "), a SQLException happend!", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                    resultSet.close();
                } catch (SQLException e) {
                    JadeLogger.warn(APListePrestationCIABProcess.class,
                            "Problem to close statement in APListePrestationCIABProcess, reason : " + e.toString());
                }
            }

        }

        return results;
    }

    /**
     * 
     * Transforme une date au format aaaammjj en une date au format jj.mm.yyyy
     */
    private String formatDate(String dateAMJ) {

        String dateJMA = dateAMJ.substring(6, 8) + "." + dateAMJ.substring(4, 6) + "." + dateAMJ.substring(0, 4);

        return dateJMA;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    @Override
    protected String getEMailObject() {

        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_MAIL_OBJECT_ERROR");
        } else {
            return getSession().getLabel("PROCESS_LISTE_PRESTATION_VERSEE_MAIL_OBJECT_SUCCESS");
        }

    }

    public Boolean getEnvoyerGed() {
        return envoyerGed;
    }

    public PrestationCIABPojo getPrestationCIABPojo() {
        return listePrestationCIABPojo;
    }

    public String getSelecteurPrestation() {
        return selecteurPrestation;
    }

    private String getSqlListePrestationCIAB() throws Exception {


        String langueUser = getSession().getIdLangue();

        // Création des conditions
        String sqlWhere = " where pres.VHTETA = ? ";

        // Selon le selecteur, la condition change
        // Le selecteur est désactivé actuellement, seul Prestation par période est activé.
        if (APListePrestationCIABProcess.SELECTEUR_PRESTATION_PAR_PAIEMENT.equalsIgnoreCase(selecteurPrestation)) {
            //sqlWhere += " AND pres.VHDPMT >= " + dateDebutAMJ + " AND  pres.VHDPMT <= " + dateFinAMJ + " ";
            sqlWhere += " AND pres.VHDPMT >= ? AND  pres.VHDPMT <= ? ";
        } else if (APListePrestationCIABProcess.SELECTEUR_PRESTATION_PAR_PERIODE
                .equalsIgnoreCase(selecteurPrestation)) {
            //sqlWhere += " AND pres.VHDDEB <= " + dateFinAMJ + " AND  pres.VHDFIN >= " + dateDebutAMJ + " ";
            sqlWhere += " AND pres.VHDDEB <= ? AND  pres.VHDFIN >= ? ";
        } else {
            throw new Exception("Not implemented");
        }

        sqlWhere += " AND pres.VHTGEN IN (?,?) ";
        //sqlWhere += " AND afc.MBIASS IN (?,?,?,?) ";
        sqlWhere += " AND ((pres.VHDFIN <= afc.MEDFIN or afc.MEDFIN = 0) AND pres.VHDDEB >= afc.MEDDEB)";
        sqlWhere += " AND ((( repa.VIMMOB < repa.VIMMON AND afc.MBIASS IN (?, ?))";
        sqlWhere += "   OR ( repa.VIMMOB > repa.VIMMON AND afc.MBIASS IN (?, ?)) AND (repa.VIMMON > 0))";
        sqlWhere += " OR (((repa.VIMMOB < repa.VIMMON AND afc.MBIASS IN (?, ?)) OR ( repa.VIMMOB > repa.VIMMON AND afc.MBIASS IN (?, ?)))";
        sqlWhere += "   AND (repa.VIMMON < 0)))";

//        sqlWhere += " AND pres.VHDDEB >= afc.MEDDEB ";
//        sqlWhere += " AND (( repa.VIMMOB < repa.VIMMON AND afc.MBIASS IN (?,?) ) ";
//        sqlWhere += " OR ( repa.VIMMOB > repa.VIMMON AND afc.MBIASS IN (?,?) )) ";


       String sql = " select affi.MALNAF as "
                + APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NUMERO_AFFILIE
                + " , "
                + " afc.MBIASS as "
                + APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_CODE_ASSURANCE_COMPL
                 + " , "
                + " affi.MADESL as "
                + APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_NOM_AFFILIE
                + " , "
                + " lib_genre_service.PCOUID as "
                + APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_SERVICE
                + " , "
                + " lib_genre_service.PCOLUT as "
                + APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_SERVICE_LIBELLE
                + " , "
                + " pres.VHTGEN as "
                + APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_PRESTATION
                + " , "
                + " lib_genre_prestation.PCOLUT as "
                + APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_GENRE_PRESTATION_LIBELLE
                + " , "
                + " pres.VHDDEB as "
                + APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_DEBUT
                + " , "
                + " pres.VHDFIN as "
                + APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_FIN
                + " , "
                + " pres.VHDMOB as "
                + APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_MONTANT_BRUT
                + " , "
                + " pres.VHDPMT as "
                + APListePrestationCIABProcess.REQUETE_LISTE_PRESTATION_VERSEE_COL_NAME_DATE_PAIEMENT
                + " "
                + " from schema.APPRESP pres inner join schema.FWCOUP lib_genre_prestation on(lib_genre_prestation.PCOSID = pres.VHTGEN and lib_genre_prestation.PLAIDE='"
                + langueUser
                + "') "
                + " inner join schema.APREPAP repa on (repa.VIIPRA = pres.VHIPRS and repa.VIIAFF <> 0 AND repa.VIIPAR = 0) "
                + " inner join schema.AFAFFIP affi on (affi.MAIAFF = repa.VIIAFF) "
                + " inner join schema.APDROIP droi on (droi.VAIDRO = pres.VHIDRO) "
                + " inner join schema.FWCOUP lib_genre_service on(lib_genre_service.PCOSID = droi.VATGSE and lib_genre_service.PLAIDE='"
                + langueUser + "') " + " inner join schema.PRDEMAP dema on (dema.WAIDEM = droi.VAIDEM) "
                + " inner join schema.TITIERP tier on (tier.HTITIE = dema.WAITIE) "
                + " inner join schema.TIPAVSP pavs on (pavs.HTITIE = tier.HTITIE) "
                + " inner join schema.AFPLAFP afp on (afp.MAIAFF = affi.MAIAFF) "
                + " left outer join schema.AFCOTIP afc on (afc.MUIPLA = afp.MUIPLA AND afp.MUBINA='2') "
                + sqlWhere
                + " order by pres.VHTGEN, lib_genre_service.PCOUID, afc.MBIASS";

        sql = replaceSchemaInSqlQuery(sql);

        return sql;
    }

    private JadeThreadContext initThreadContext(BSession session) throws Exception {

        JadeThreadContext context;
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(session.getApplicationId());
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());
        String[] roles = JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService()
                .findAllIdRoleForIdUser(session.getUserId());
        if ((roles != null) && (roles.length > 0)) {
            ctxtImpl.setUserRoles(JadeConversionUtil.toList(roles));
        }
        context = new JadeThreadContext(ctxtImpl);
        context.storeTemporaryObject("bsession", session);

        return context;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private void publierListePrestationVerseeDocumentsExcel(JadePublishDocument documentAPublier) throws IOException {

        this.registerAttachedDocument(documentAPublier);

    }

    private String replaceSchemaInSqlQuery(String sqlQuery) {

        sqlQuery = sqlQuery.replaceAll("(?i)schema\\.", schemaDBWithTablePrefix);

        return sqlQuery;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setEnvoyerGed(Boolean envoyerGed) {
        this.envoyerGed = envoyerGed;
    }

    public void setListePrestationCIABPojo (PrestationCIABPojo listePrestationCIABPojo) {
        this.listePrestationCIABPojo = listePrestationCIABPojo;
    }

    public void setSelecteurPrestation(String selecteurPrestation) {
        this.selecteurPrestation = selecteurPrestation;
    }

}
