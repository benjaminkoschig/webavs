package globaz.helios.itext.list;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.export.FWIExportManager;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.types.FWIDocumentType;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.helios.application.CGApplication;
import globaz.helios.db.classifications.CGClasseCompteManager;
import globaz.helios.db.classifications.CGClasseCompteServices;
import globaz.helios.db.classifications.CGClassificationNode;
import globaz.helios.db.classifications.CGExtendedCompteSoldeManager;
import globaz.helios.db.comptes.CGCentreCharge;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.itext.list.analyse.budgetaire.CGAnalyseBudgetaire_ParameterList;
import globaz.helios.itext.list.balance.comptes.CGCompteSoldeBean;
import globaz.helios.itext.list.utils.CGImpressionUtils;
import globaz.helios.tools.CGXLSContructor;
import globaz.helios.tools.TimeHelper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.sf.jasperreports.engine.JRException;

/**
 * @author Sylvain Crelier
 * @revision SEL mai 2013 - multi niveaux de classification
 * 
 *           L'analyse budgétaire se base soit sur un budget annuel soit à une date donnée. Si l'on souhaite cette
 *           analyse
 *           à une date donnée, le montant budgeté sera calculé au pro rata.
 * 
 * 
 *           Si au moins un budget pour une période à été saisi, l'analyse budgétaire à une date donnée ne fera pas de
 *           calcul au pro rata par rapport au budget annuel, mais prendra en compte les montants budgetés des périodes
 *           (même si égal à zéro, ou pas budgeté). Ceci est valable si l'on inclut les périodes comptables précédentes.
 *           Dans le cas contraitre, si budget == 0, calcul au pro rata.
 * 
 *           ex1. janvier = 200.-; février = 0.-; mars = 100.-; budget annuel = 12'000.-
 * 
 *           Analyse budgétaire annuel = 12'000.- Analyse budgétaire mars (sans période précédente)= 100.- Analyse
 *           budgétaire mars (avec période précédente)= 300.- Analyse budgétaire février (sans période précédente)=
 *           1'000.- (12000/12) Analyse budgétaire février (avec période précédente)= 200.-
 * 
 * 
 * 
 *           ex2. janvier = 0.-; février = 0.-; mars = 0.-; budget annuel = 12'000.-
 * 
 *           Analyse budgétaire annuel = 12'000.- Analyse budgétaire mars (sans pp)= 1000.- (12000/360*30) Analyse
 *           budgétaire mars (avec pp)= 3000.- (12000/360*90)
 * 
 * @revision SEL juillet 2013 - multi niveaux de classification
 */
public class CGProcessImpressionAnalyseBudgetaire extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ANALYSE_BUDGETAIRE = "AnalyseBudgetaire";
    public static final String NUMERO_REFERENCE_INFOROM = "0048GCF";
    public static final String TEMPLATE_DOC = "cg_analyse_budgetaire_multi";
    public static final String XLS_DOC_NAME = "ListeAnalyseBudgetaireMulti";

    private String classesCompteList;
    private String compteA;
    private String compteDe;

    private String forCodePeriode = "";
    private String idCentreCharge = "0";
    /** Niveau de classification */
    private String idClassification = "";
    private String idComptabilite;
    private String idDefinitionListe = "";
    private String idDomaine;
    private String idExerciceComptable;
    private String idExerciceForComparaison;
    private String idMandat;
    private String idPeriodeComptable;

    private Boolean imprimerComptes = true;
    private Boolean imprimerPageGarde = true;
    private Boolean imprimerTitres = true;
    private Boolean inclurePeriodePrecedente = true;

    /** Niveau de classification */
    private int niveauClassification = 0;

    private String typeImpression = CGImpressionUtils.TYPE_IMPRESSION_PDF;

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     */
    public CGProcessImpressionAnalyseBudgetaire() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     * 
     * @param parent
     */
    public CGProcessImpressionAnalyseBudgetaire(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     * 
     * @param session
     */
    public CGProcessImpressionAnalyseBudgetaire(BSession session) throws Exception {
        super(session);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        boolean status = true;
        FWIExportManager exportManager = new FWIExportManager();
        FWIImportManager importManager = new FWIImportManager();
        Map<String, Comparable<?>> parametres = new HashMap<String, Comparable<?>>();
        /**
         * Key = idClasseCompte
         */
        Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot = new HashMap<String, CGClassificationNode<CGCompteSoldeBean>>();

        try {

            // On prepare les parametres globaux
            prepareParameter(parametres);

            // ****************************
            // 1.Recupérarion des données à traiter
            // ****************************
            CGClasseCompteManager classeCompteManager = CGClasseCompteServices.findClasseCompte(getTransaction(),
                    idClassification);

            CGExtendedCompteSoldeManager compteSoldeManager = findCompteSolde(getTransaction(), idClassification,
                    idExerciceComptable);

            // Manager pour l'exercice de comparaison
            CGExtendedCompteSoldeManager compteSoldeExerPrecedentManager = findCompteSolde(getTransaction(),
                    idClassification, idExerciceForComparaison);

            // ****************************
            // 2. Traitement des données dans un arbre
            // ****************************
            /** Critère des classifications à afficher */
            List<String> classeListe = Arrays.asList(classesCompteList.trim().split(" "));

            List<String> genreListe = new ArrayList<String>();
            genreListe.add(CGCompte.CS_GENRE_PRODUIT);
            genreListe.add(CGCompte.CS_GENRE_CHARGE);

            CGClasseCompteServices.loadClassification(getSession().getIdLangueISO(), classeCompteManager, nodesDepot,
                    classeListe);

            CGClasseCompteServices.loadCompteSoldeLevel(compteSoldeManager, compteSoldeExerPrecedentManager,
                    nodesDepot, getSession().getIdLangueISO(),
                    CGClasseCompteServices.isProvisoire(getIdComptabilite()), classeListe, genreListe);

            // ****************************
            // 3. Preparation et impression
            // ****************************
            // Remplit la liste de beans qui servira pour l'impression uniquement
            List<CGCompteSoldeBean> listBeanDocument = sortAndDisplayResult(nodesDepot);

            // Si aucune donnée, on sort
            if (listBeanDocument.size() == 0) {
                getMemoryLog().logMessage("GLOBAL_AUCUN_DOCUMENT_A_IMPRIMER", null, FWMessage.FATAL,
                        this.getClass().getName());
                return true;
            }

            status = createDocument(exportManager, importManager, parametres, listBeanDocument);

        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            JadeLogger.error(this, e);
            status = false;
        }

        return status;
    }

    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isBlank(idExerciceComptable)) {
            throw new Exception(getSession().getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_ERR_1"));
        }
        if (JadeStringUtil.isBlank(idMandat)) {
            throw new Exception(getSession().getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_ERR_3"));
        }
        if (getParent() == null) {
            setSendCompletionMail(true);
            setControleTransaction(true);
        }

        if (getSession().hasErrors()) {
            abort();
        }

    }

    /**
     * @param exportManager
     * @param importManager
     * @param parametres
     * @param listBeanDocument
     * @return
     * @throws FWIException
     * @throws JRException
     * @throws IOException
     * @throws Exception
     */
    private boolean createDocument(FWIExportManager exportManager, FWIImportManager importManager,
            Map<String, Comparable<?>> parametres, List<CGCompteSoldeBean> listBeanDocument) throws FWIException,
            JRException, IOException, Exception {
        boolean status;

        // Initialisation des variables du document
        initDocument(importManager, exportManager);

        // Create pdf ou xls
        if (CGImpressionUtils.TYPE_IMPRESSION_PDF.equals(getTypeImpression())) {
            status = createPdf(importManager, exportManager, parametres, listBeanDocument);
        } else {
            status = createExcel(parametres, listBeanDocument);
        }
        return status;
    }

    /**
     * @param parametres
     * @return
     * @throws Exception
     */
    private boolean createExcel(Map<String, Comparable<?>> parametres, List<CGCompteSoldeBean> listBeanDocument)
            throws Exception {

        CGXLSContructor xlsc = new CGXLSContructor();
        xlsc.setDocumentFileName(CGProcessImpressionAnalyseBudgetaire.XLS_DOC_NAME);
        xlsc.setSession(getSession());
        xlsc.process(listBeanDocument, parametres);

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(CGApplication.DEFAULT_APPLICATION_HELIOS);
        docInfoExcel.setDocumentTitle(CGProcessImpressionAnalyseBudgetaire.XLS_DOC_NAME);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        docInfoExcel.setDocumentTypeNumber(CGProcessImpressionAnalyseBudgetaire.NUMERO_REFERENCE_INFOROM);
        this.registerAttachedDocument(docInfoExcel, xlsc.getExportPath());

        return true;
    }

    /**
     * @param importManager
     * @param exportManager
     * @param parametres
     * @return
     * @throws FWIException
     * @throws JRException
     * @throws IOException
     */
    private boolean createPdf(FWIImportManager importManager, FWIExportManager exportManager,
            Map<String, Comparable<?>> parametres, List<CGCompteSoldeBean> listBeanDocument) throws FWIException,
            JRException, IOException {
        importManager.clearParam();
        importManager.setDocumentName(CGProcessImpressionAnalyseBudgetaire.ANALYSE_BUDGETAIRE);
        importManager.setParametre(parametres);
        importManager.setDocumentTemplate(CGProcessImpressionAnalyseBudgetaire.TEMPLATE_DOC);
        importManager.setBeanCollectionDataSource(listBeanDocument);
        importManager.createDocument();

        // Il y a des documents a imprimer
        if (importManager.size() > 0) {
            exportManager.addAll(importManager.getList());
            exportManager.exportReport();
            if (isAborted()) {
                return false;
            }

            JadePublishDocumentInfo documentInfo = createDocumentInfo();
            documentInfo.setDocumentTypeNumber(CGProcessImpressionAnalyseBudgetaire.NUMERO_REFERENCE_INFOROM);
            this.registerAttachedDocument(documentInfo, exportManager.getExportNewFilePath());
        } else {
            getMemoryLog().logMessage("GLOBAL_AUCUN_DOCUMENT_A_IMPRIMER", null, FWMessage.FATAL,
                    this.getClass().getName());

            return false;
        }

        return true;
    }

    /**
     * @param transaction
     * @param forIdClassification
     * @return the full manager or null if Exception
     */
    private CGExtendedCompteSoldeManager findCompteSolde(BTransaction transaction, String forIdClassification,
            String forIdExerciceComptable) {
        // Construction de la source
        CGExtendedCompteSoldeManager manager = new CGExtendedCompteSoldeManager(!JadeStringUtil.isBlank(forCodePeriode));
        manager.setSession(transaction.getSession());
        manager.changeManagerSize(BManager.SIZE_NOLIMIT);

        manager.setInclurePeriode(getInclurePeriodePrecedente());
        manager.setForIdDomaine(getIdDomaine());
        manager.setForCodePeriode(forCodePeriode);
        manager.setForIdClassification(forIdClassification);
        // Paramétrage de la source
        manager.setForIdMandat(idMandat);
        manager.setForIdExerciceComptable(forIdExerciceComptable);
        manager.setForIdCentreCharge(idCentreCharge);
        manager.setForNotCodePeriode("99"); // periode de cloture a exclure

        // Compte de ... à
        manager.setForNumeroCompteMax(compteA);
        manager.setForNumeroCompteMin(compteDe);

        try {
            manager.find(transaction);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return null;
        }
        return manager;
    }

    /**
     * Returns the classesCompteList.
     * 
     * @return String
     */
    public String getClassesCompteList() {
        return classesCompteList;
    }

    /**
     * Returns the compteA.
     * 
     * @return String
     */
    public String getCompteA() {
        return compteA;
    }

    /**
     * Returns the compteDe.
     * 
     * @return String
     */
    public String getCompteDe() {
        return compteDe;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_EMAIL_ERROR");
        } else {
            return getSession().getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_EMAIL_OK");
        }
    }

    /**
     * @return
     */
    private CGExerciceComptable getExerciceComptableForComparaison() {
        if (JadeStringUtil.isIntegerEmpty(getIdExerciceForComparaison())) {
            return null;
        }

        try {
            CGExerciceComptable exercice = new CGExerciceComptable();
            exercice.setSession(getSession());
            exercice.setIdExerciceComptable(getIdExerciceForComparaison());
            exercice.retrieve(getTransaction());

            if (exercice.isNew()) {
                return null;
            } else {
                return exercice;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public String getIdCentreCharge() {
        return idCentreCharge;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:04:40)
     * 
     * @return String
     */
    public String getIdComptabilite() {
        return idComptabilite;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:57:58)
     * 
     * @return String
     */
    public String getIdDefinitionListe() {
        return idDefinitionListe;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:04:40)
     * 
     * @return String
     */
    public String getIdDomaine() {
        return idDomaine;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:57:18)
     * 
     * @return String
     */
    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * @return
     */
    public String getIdExerciceForComparaison() {
        return idExerciceForComparaison;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:58:23)
     * 
     * @return String
     */
    public String getIdMandat() {
        return idMandat;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:04:40)
     * 
     * @return String
     */
    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:00:21)
     * 
     * @return Boolean
     */
    public Boolean getImprimerPageGarde() {
        return imprimerPageGarde;
    }

    /**
     * Returns the imprimerTitres.
     * 
     * @return Boolean
     */
    public Boolean getImprimerTitres() {
        return imprimerTitres;
    }

    /**
     * Returns the inclurePeriodePrecedente.
     * 
     * @return Boolean
     */
    public Boolean getInclurePeriodePrecedente() {
        return inclurePeriodePrecedente;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:59:45)
     * 
     * @return String
     */
    public String getListeIdClasseCompte() {
        return classesCompteList;
    }

    /**
     * @return the niveauClassification
     */
    public int getNiveauClassification() {
        return niveauClassification;
    }

    public String getTypeImpression() {
        return typeImpression;
    }

    /**
     * @param nodeValue
     * @return
     */
    private boolean hasMontant(CGCompteSoldeBean nodeValue) {
        return (nodeValue.computeSoldeProduit() != null) || (nodeValue.computeSoldeCharge() != null)
                || (nodeValue.getTotalCharges() != null) || (nodeValue.getTotalProduits() != null);
    }

    /**
     * @param importManager
     * @param exportManager
     * @throws FWIException
     */
    private void initDocument(FWIImportManager importManager, FWIExportManager exportManager) throws FWIException {
        // import
        importManager.setImportPath(CGApplication.APPLICATION_HELIOS_REP);

        // export
        exportManager.setExportApplicationRoot(CGApplication.APPLICATION_HELIOS_REP);
        exportManager.setExportFileName(CGProcessImpressionAnalyseBudgetaire.ANALYSE_BUDGETAIRE);
        exportManager.setExportFileType(FWIDocumentType.PDF);
    }

    /**
     * Insert the method's description here. Creation date: (07.07.2003 17:14:22)
     * 
     * @return Boolean
     */
    public Boolean isImprimerComptes() {
        return imprimerComptes;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 11:44:45)
     * 
     * @return boolean
     */
    public Boolean isImprimerPageGarde() {
        return imprimerPageGarde;
    }

    /**
     * Insert the method's description here. Creation date: (07.07.2003 17:24:10)
     * 
     * @return Boolean
     */
    public Boolean isImprimerTitres() {
        return imprimerTitres;
    }

    public Boolean isInclurePeriodePrecedente() {
        return inclurePeriodePrecedente;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Préparation des parametres globaux
     * 
     * @param parametres
     */
    private void prepareParameter(Map<String, Comparable<?>> parametres) {
        try {
            // Assignation des paramètres
            parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_COMPTABILITE, getSession()
                    .getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_COMPTABILITE"));
            parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_DEFLISTE, getSession()
                    .getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_DEFLISTE"));
            parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_PERIODE, getSession()
                    .getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_PERIODE"));
            parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_COL1, getSession()
                    .getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_COL1"));
            parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_COL2, getSession()
                    .getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_COL2"));
            parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_COL3, getSession()
                    .getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_COL3"));
            parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_COL4, getSession()
                    .getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_COL4"));
            parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_COL5, getSession()
                    .getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_COL5"));

            if (getExerciceComptableForComparaison() != null) {
                parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_COL6,
                        getExerciceComptableForComparaison().getFullDescription());
            } else {
                parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_COL6, getSession()
                        .getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_COL6"));
            }

            parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_TOTAL, getSession()
                    .getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_TOTAL"));
            parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_TOTALFINAL, getSession()
                    .getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_TOTALFINAL"));
            parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_TITRE, getSession()
                    .getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_TITRE"));
            parametres.put("P_DATE_TIME", TimeHelper.getCurrentTime());
            // Bug 3007
            parametres.put("P_DATE_TIME_USER", TimeHelper.getCurrentTime() + " - " + getSession().getUserName());
            parametres.put("NUMERO_INFOROM", CGProcessImpressionAnalyseBudgetaire.NUMERO_REFERENCE_INFOROM);
            // Description comptabilité
            parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_COMPTABILITE,
                    globaz.helios.translation.CodeSystem.getLibelle(getSession(), idComptabilite));

            // Description exercice comptable
            globaz.helios.db.comptes.CGExerciceComptable exerComptable = new globaz.helios.db.comptes.CGExerciceComptable();
            exerComptable.setIdExerciceComptable(idExerciceComptable);
            exerComptable.setSession(getSession());
            exerComptable.retrieve();
            if (!exerComptable.isNew()) {
                // Set default value if no periode provided
                parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_EXERCICE,
                        exerComptable.getFullDescription());
                // Date des P&P à la fin de l'exercice
                parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_DATE,
                        exerComptable.getDateFin());
                parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_PERIODE,
                        exerComptable.getFullDescription());
            }

            // Description période comptable
            if (!JadeStringUtil.isIntegerEmpty(idPeriodeComptable)) {
                globaz.helios.db.comptes.CGPeriodeComptable periodeComptable = new globaz.helios.db.comptes.CGPeriodeComptable();
                periodeComptable.setIdPeriodeComptable(idPeriodeComptable);
                periodeComptable.setSession(getSession());
                periodeComptable.retrieve();
                if (!periodeComptable.isNew()) {
                    forCodePeriode = periodeComptable.getCode();
                    if (isInclurePeriodePrecedente() && (JACalendar.getMonth(periodeComptable.getDateFin()) != 1)) {
                        parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_PERIODE, getSession()
                                .getLabel("JANVIER_A") + " " + periodeComptable.getFullDescription());
                    } else {
                        parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_PERIODE,
                                periodeComptable.getFullDescription());
                    }
                    // Date à la fin de la période
                    parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_DATE,
                            periodeComptable.getDateFin());
                }
            } else {
                // Si l'option "Tout l'exercice" est selectionnée, on l'affiche sur la liste
                if ("0".equals(idPeriodeComptable)) {
                    parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_PERIODE, getSession()
                            .getLabel("TOUT_LEXERCICE"));
                }
            }

            // Description definition de liste
            globaz.helios.db.classifications.CGDefinitionListe definitionListe = new globaz.helios.db.classifications.CGDefinitionListe();
            definitionListe.setIdDefinitionListe(idDefinitionListe);
            definitionListe.setSession(getSession());
            definitionListe.retrieve();
            if (!definitionListe.isNew()) {
                parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_DEFLISTE,
                        definitionListe.getLibelle());
                idClassification = definitionListe.getIdClassification();
            }

            // Description du centre de charge
            if (!JadeStringUtil.isIntegerEmpty(getIdCentreCharge())) {
                CGCentreCharge centreCharge = new CGCentreCharge();
                centreCharge.setIdCentreCharge(getIdCentreCharge());
                centreCharge.setIdMandat(getIdMandat());
                centreCharge.setSession(getSession());
                centreCharge.retrieve();

                if (!centreCharge.isNew()) {
                    parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_CENTRECHARGE,
                            getSession().getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_CENTRECHARGE"));
                    parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_CENTRECHARGE,
                            centreCharge.getLibelle());
                }
            } else {
                parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_CENTRECHARGE, "");
            }

            // Impression détails page de garde
            parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_IMPRIMER_PAGE_GARDE,
                    imprimerPageGarde);
            // Impression titres
            parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_IMPRIMER_TITRES, imprimerTitres);
            // Impression comptes
            parametres
                    .put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_IMPRIMER_COMPTES, imprimerComptes);

            if (isInclurePeriodePrecedente().booleanValue()) {
                parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_INCLURE_PERIODE_PREC,
                        getSession().getLabel("IMPRESSION_ANALYSE_BUDGETAIRE_INCLURE_PER_PREC"));
            } else {
                parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_INCLURE_PERIODE_PREC, "");
            }

            // Description du mandat
            if (!JadeStringUtil.isIntegerEmpty(getIdMandat())) {
                globaz.helios.db.comptes.CGMandat mandat = new globaz.helios.db.comptes.CGMandat();
                mandat.setIdMandat(idMandat);
                mandat.setSession(getSession());
                mandat.retrieve();
                if (!mandat.isNew()) {
                    parametres.put(CGAnalyseBudgetaire_ParameterList.PARAM_ANALYSE_BUDGETAIRE_LABEL_COMPANYNAME,
                            mandat.getLibelle());
                }
            }
        } catch (Exception e) {
            JadeLogger.fatal(this, e);
        }
    }

    /**
     * Crée un bean avec les informations que l'on souhaite afficher dans la liste. Cela dépend si c'est un titre, un
     * compte avec solde ou un total.
     * 
     * @param node
     *            : noeud a traiter
     * @param listBeanDocument
     *            : liste de bean pour le Jasper. Un bean est soit un titre, soit un compte, soit un total.
     */
    private void recurseDisplayNode(CGClassificationNode<CGCompteSoldeBean> node,
            List<CGCompteSoldeBean> listBeanDocument) {

        if (node == null) {
            throw new IllegalArgumentException("Error : CGClassificationNode<CGBalanceComptesBean> is null !");
        }
        if (listBeanDocument == null) {
            throw new IllegalArgumentException("Error : List<CGBalanceComptesBean> is null !");
        }

        CGCompteSoldeBean nodeValue = node.getValue();
        if (!node.isLeaf()) {
            if (hasMontant(nodeValue)) {
                if (!CGClasseCompteServices.isNiveauClassificationToBig(niveauClassification, nodeValue.getNiveau())) {
                    if ((nodeValue.getTotalCharges() != null) || (nodeValue.getTotalProduits() != null)) {
                        CGCompteSoldeBean bean = new CGCompteSoldeBean(nodeValue);
                        bean.resetMvt();
                        bean.resetTotaux();
                        // HACK pour iReport afin de gérer correctement les groupes
                        bean.setNiveau(bean.getNiveau() + bean.getNoClasse());
                        listBeanDocument.add(bean);
                    }
                }
            }

            if (node.hasChildren()) {
                for (CGClassificationNode<CGCompteSoldeBean> child : node.getChildren().values()) {
                    recurseDisplayNode(child, listBeanDocument);
                }
            }

            // TOTAUX
            if (hasMontant(nodeValue)) {
                if (!CGClasseCompteServices.isNiveauClassificationToBig(niveauClassification, nodeValue.getNiveau())) {
                    if ((nodeValue.getTotalCharges() != null) || (nodeValue.getTotalProduits() != null)) {
                        CGCompteSoldeBean bean = new CGCompteSoldeBean(nodeValue);
                        bean.resetMvt();
                        // HACK pour iReport afin de gérer correctement les groupes
                        bean.setNiveau(bean.getNiveau() + bean.getNoClasse());
                        bean.setNoClasseLibelle(getSession().getLabel("IMPRESSION_BALANCE_COMPTES_TOTAL") + " "
                                + bean.getNoClasseLibelle());

                        Double totalCharges = bean.getTotalCharges() != null ? bean.getTotalCharges() : 0;
                        Double totalProduits = bean.getTotalProduits() != null ? bean.getTotalProduits() : 0;
                        Double soldeTotal = CGClasseCompteServices.stringToDouble(totalCharges.toString())
                                + CGClasseCompteServices.stringToDouble(totalProduits.toString());
                        bean.setTotalSolde(soldeTotal);

                        listBeanDocument.add(bean);
                    }
                }
            }

        } else if (hasMontant(nodeValue)) {
            if (!CGClasseCompteServices.isNiveauClassificationToBig(niveauClassification, nodeValue.getNiveau())) {
                CGCompteSoldeBean bean = nodeValue;
                CGCompteSoldeBean beanCompared = node.getValueToCompare();
                bean.resetTotaux();
                bean.setSolde(bean.computeSoldeCharge() != null ? bean.computeSoldeCharge() : bean
                        .computeSoldeProduit());

                if (beanCompared != null) {
                    bean.setSoldeExerciceComparaison(beanCompared.computeSoldeCharge() != null ? beanCompared
                            .computeSoldeCharge() : beanCompared.computeSoldeProduit());
                }

                if (JadeStringUtil.isBlankOrZero(bean.getBudget().toString())) {
                    bean.setBudget(null);
                }
                listBeanDocument.add(bean);
            }
        }
    }

    /**
     * Sets the classesCompteList.
     * 
     * @param classesCompteList
     *            The classesCompteList to set
     */
    public void setClassesCompteList(String classesCompteList) {
        this.classesCompteList = classesCompteList;
    }

    /**
     * Sets the compteA.
     * 
     * @param compteA
     *            The compteA to set
     */
    public void setCompteA(String compteA) {
        this.compteA = compteA;
    }

    /**
     * Sets the compteDe.
     * 
     * @param compteDe
     *            The compteDe to set
     */
    public void setCompteDe(String compteDe) {
        this.compteDe = compteDe;
    }

    public void setIdCentreCharge(String idCentreCharge) {
        this.idCentreCharge = idCentreCharge;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:04:40)
     * 
     * @param newIdComptabilite
     *            String
     */
    public void setIdComptabilite(String newIdComptabilite) {
        idComptabilite = newIdComptabilite;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:57:58)
     * 
     * @param newIdDefinitionListe
     *            String
     */
    public void setIdDefinitionListe(String newIdDefinitionListe) {
        idDefinitionListe = newIdDefinitionListe;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:04:40)
     * 
     * @param newIdDomaine
     *            String
     */
    public void setIdDomaine(String newIdDomaine) {
        idDomaine = newIdDomaine;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:57:18)
     * 
     * @param newIdExerciceComptable
     *            String
     */
    public void setIdExerciceComptable(String newIdExerciceComptable) {
        idExerciceComptable = newIdExerciceComptable;
    }

    /**
     * @param string
     */
    public void setIdExerciceForComparaison(String string) {
        idExerciceForComparaison = string;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 14:58:23)
     * 
     * @param newIdMandat
     *            String
     */
    public void setIdMandat(String newIdMandat) {
        idMandat = newIdMandat;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:04:40)
     * 
     * @param newIdPeriodeComptable
     *            String
     */
    public void setIdPeriodeComptable(String newIdPeriodeComptable) {
        idPeriodeComptable = newIdPeriodeComptable;
    }

    /**
     * Insert the method's description here. Creation date: (07.07.2003 17:14:22)
     * 
     * @param newImprimerComptes
     *            Boolean
     */
    public void setImprimerComptes(Boolean newImprimerComptes) {
        imprimerComptes = newImprimerComptes;
    }

    /**
     * Insert the method's description here. Creation date: (02.07.2003 15:00:21)
     * 
     * @param newImprimerPageGarde
     *            Boolean
     */
    public void setImprimerPageGarde(Boolean newImprimerPageGarde) {
        imprimerPageGarde = newImprimerPageGarde;
    }

    /**
     * Sets the imprimerTitres.
     * 
     * @param imprimerTitres
     *            The imprimerTitres to set
     */
    public void setImprimerTitres(Boolean imprimerTitres) {
        this.imprimerTitres = imprimerTitres;
    }

    /**
     * Sets the inclurePeriodePrecedente.
     * 
     * @param inclurePeriodePrecedente
     *            The inclurePeriodePrecedente to set
     */
    public void setInclurePeriodePrecedente(Boolean inclurePeriodePrecedente) {
        this.inclurePeriodePrecedente = inclurePeriodePrecedente;
    }

    /**
     * @param niveauClassification
     *            the niveauClassification to set
     */
    public void setNiveauClassification(int niveauClassification) {
        this.niveauClassification = niveauClassification;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

    /**
     * Recherche les noeuds racines afin d'en afficher l'arbre
     * 
     * @param nodesDepot
     *            : dépot de noeuds
     * @param listBeanDocument
     *            : list de bean soumise à JasperReport
     */
    private List<CGCompteSoldeBean> sortAndDisplayResult(Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot) {
        List<CGCompteSoldeBean> listBeanDocument = new ArrayList<CGCompteSoldeBean>();

        Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepotTrie = new TreeMap<String, CGClassificationNode<CGCompteSoldeBean>>();

        // Trie dans l'ordre croissant des numéros de classification
        for (CGClassificationNode<CGCompteSoldeBean> node : nodesDepot.values()) {
            nodesDepotTrie.put(node.getValue().getNoClasse(), node);
        }

        for (CGClassificationNode<CGCompteSoldeBean> node : nodesDepotTrie.values()) {
            if (node.isRoot()) {
                recurseDisplayNode(node, listBeanDocument);
            }
        }

        return listBeanDocument;
    }

}
