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
import globaz.helios.db.classifications.CGDefinitionListe;
import globaz.helios.db.classifications.CGExtendedCompteSoldeManager;
import globaz.helios.db.comptes.CGCentreCharge;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.itext.list.balance.comptes.CGBalanceComptes_ParameterList;
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
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

/**
 * @author Sylvain Crelier.
 * @revision SCO 27 janv. 2010
 * @revision SEL mai 2013 - multi niveaux de classification
 */
public class CGProcessImpressionBalanceComptes extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String BALANCE_COMPTES = "BalanceComptes";
    private static final String NUMERO_REFERENCE_INFOROM = "0050GCF";
    private static final String TEMPLATE_DOC = "cg_balance_comptes_multi_niveau";
    public static final String XLS_DOC_NAME = CGProcessImpressionBalanceComptes.BALANCE_COMPTES + "Multi";

    /** Définition de listes */
    private String classesCompteList = "";
    private String forCodePeriode = "";
    /** Centre de charge */
    private String idCentreCharge = "0";
    /** Niveau de classification */
    private String idClassification = "";
    /** Comptabilité : Provisoire ou définitif */
    private String idComptabilite = "";
    /** Permete de déterminer idClassification */
    private String idDefinitionListe = "";
    /** Domaine des comptes */
    private String idDomaine = "";
    /** Exercice */
    private String idExerciceComptable = "";
    /** Mandat */
    private String idMandat = "";
    /** Période comptable : Nécessaire pour déterminer forCodePeriode */
    private String idPeriodeComptable = "";

    /** Imprimer les comptes */
    private Boolean imprimerComptes = true;
    /** Imprimer la page de garde */
    private Boolean imprimerPageGarde = true;
    /** Imprimer les titres */
    private Boolean imprimerTitres = true;
    /** Inclure période précédente */
    private Boolean inclurePeriodePrecedente = true;
    /** Niveau de classification */
    private int niveauClassification = 0;
    /** Type d'impression */
    private String typeImpression = CGImpressionUtils.TYPE_IMPRESSION_PDF;

    /**
     * Constructor for CGProcessImpressionBalanceComptes.
     */
    public CGProcessImpressionBalanceComptes() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * Constructor for CGProcessImpressionBalanceComptes.
     * 
     * @param parent
     */
    public CGProcessImpressionBalanceComptes(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CGProcessImpressionBalanceComptes.
     * 
     * @param session
     */
    public CGProcessImpressionBalanceComptes(BSession session) throws Exception {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        boolean status = true;
        FWIImportManager importManager = new FWIImportManager();
        FWIExportManager exportManager = new FWIExportManager();
        Map<String, Comparable<?>> parametres = new HashMap<String, Comparable<?>>();
        Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot = new HashMap<String, CGClassificationNode<CGCompteSoldeBean>>();

        try {
            // On prepare les parametres globaux
            // Doit être placé avant loadClassification et findCompteSolde car l'on initialise idClassification
            prepareParameter(parametres);

            CGClasseCompteManager classeCompteManager = CGClasseCompteServices.findClasseCompte(getTransaction(),
                    idClassification);
            CGExtendedCompteSoldeManager compteSoldeManager = findCompteSolde(getTransaction(), idClassification);

            /** Critère des classifications à afficher */
            List<String> classeListe = Arrays.asList(classesCompteList.trim().split(" "));

            CGClasseCompteServices.loadClassification(getSession().getIdLangueISO(), classeCompteManager, nodesDepot,
                    classeListe);

            CGClasseCompteServices.loadCompteSoldeLevel(compteSoldeManager, nodesDepot, getSession().getIdLangueISO(),
                    CGClasseCompteServices.isProvisoire(getIdComptabilite()), classeListe);

            // Remplit la liste de beans
            List<CGCompteSoldeBean> listBeanDocument = sortAndDisplayResult(nodesDepot);

            // Si aucune donnée, on sort
            if (listBeanDocument.size() == 0) {
                getMemoryLog().logMessage("GLOBAL_AUCUN_DOCUMENT_A_IMPRIMER", null, FWMessage.FATAL,
                        this.getClass().getName());
                return true;
            }

            status = createDocument(importManager, exportManager, parametres, listBeanDocument);

        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            JadeLogger.error(this, e);
            status = false;
        }
        return status;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isBlank(idExerciceComptable)) {
            this._addError(getTransaction(), getSession().getLabel("IMPRESSION_BALANCE_COMPTES_ERR_1"));
        }
        if (JadeStringUtil.isBlank(idMandat)) {
            this._addError(getTransaction(), getSession().getLabel("IMPRESSION_BALANCE_COMPTES_ERR_3"));
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
     * Construction du document. Charge les données
     */
    protected void buildDocument(FWIImportManager importManager, Map<String, Comparable<?>> parametres,
            List<CGCompteSoldeBean> listBeanDocument) {

        try {
            if (listBeanDocument.size() == 0) {
                return;
            }

            importManager.clearParam();
            importManager.setDocumentName(CGProcessImpressionBalanceComptes.BALANCE_COMPTES);
            importManager.setParametre(parametres);
            importManager.setDocumentTemplate(CGProcessImpressionBalanceComptes.TEMPLATE_DOC);
            importManager.setBeanCollectionDataSource(listBeanDocument);
            importManager.createDocument();
        } catch (FWIException e) {
            JadeLogger.fatal(this, e);
        }
    }

    /**
     * @param importManager
     * @param exportManager
     * @param parametres
     * @param listBeanDocument
     * @return
     * @throws FWIException
     * @throws JRException
     * @throws IOException
     * @throws Exception
     */
    private boolean createDocument(FWIImportManager importManager, FWIExportManager exportManager,
            Map<String, Comparable<?>> parametres, List<CGCompteSoldeBean> listBeanDocument) throws FWIException,
            JRException, IOException, Exception {
        boolean status;
        // Initialisation des variables du document
        initDocument(importManager, exportManager);

        buildDocument(importManager, parametres, listBeanDocument);

        // Create pdf ou xls
        if (CGImpressionUtils.TYPE_IMPRESSION_PDF.equals(getTypeImpression())) {
            status = createPdf(importManager, exportManager);
        } else {
            status = createExcel(parametres, listBeanDocument);
        }
        return status;
    }

    /**
     * Création du fichier Excel
     * 
     * @return true si tout c'est bien passé
     * @throws Exception
     */
    private boolean createExcel(Map<String, Comparable<?>> parametres, List<CGCompteSoldeBean> listBeanDocument)
            throws Exception {

        CGXLSContructor xlsc = new CGXLSContructor();
        xlsc.setDocumentFileName(CGProcessImpressionBalanceComptes.XLS_DOC_NAME);
        xlsc.setSession(getSession());
        xlsc.process(listBeanDocument, parametres);

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(CGApplication.DEFAULT_APPLICATION_HELIOS);
        docInfoExcel.setDocumentTitle(CGProcessImpressionBalanceComptes.XLS_DOC_NAME);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        docInfoExcel.setDocumentTypeNumber(CGProcessImpressionBalanceComptes.NUMERO_REFERENCE_INFOROM);
        this.registerAttachedDocument(docInfoExcel, xlsc.getExportPath());

        return true;
    }

    /**
     * Création du fichier PDF
     * 
     * @return true si tout c'est bien passé, false si le traitement a été aborded
     * @throws JRException
     * @throws IOException
     */
    private boolean createPdf(FWIImportManager importManager, FWIExportManager exportManager) throws JRException,
            IOException {

        // Il y a des documents a imprimer
        exportManager.addAll(importManager.getList());
        exportManager.exportReport();
        if (isAborted()) {
            return false;
        }

        JadePublishDocumentInfo documentInfo = createDocumentInfo();
        documentInfo.setDocumentTypeNumber(CGProcessImpressionBalanceComptes.NUMERO_REFERENCE_INFOROM);
        this.registerAttachedDocument(documentInfo, exportManager.getExportNewFilePath());

        return true;
    }

    /**
     * @param transaction
     * @param forIdClassification
     * @return the full manager or null if Exception
     */
    private CGExtendedCompteSoldeManager findCompteSolde(BTransaction transaction, String forIdClassification) {
        // Construction de la source
        CGExtendedCompteSoldeManager manager = new CGExtendedCompteSoldeManager();
        manager.setSession(transaction.getSession());
        manager.changeManagerSize(BManager.SIZE_NOLIMIT);

        manager.setForIdDomaine(getIdDomaine());
        manager.setInclurePeriode(getInclurePeriodePrecedente());
        manager.setForCodePeriode(forCodePeriode);
        manager.setForIdClassification(forIdClassification);
        // Paramétrage de la source
        manager.setForIdMandat(idMandat);
        manager.setForIdExerciceComptable(idExerciceComptable);
        manager.setForIdCentreCharge(idCentreCharge);

        try {
            manager.find(transaction);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return null;
        }
        return manager;
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("IMPRESSION_BALANCE_COMPTES_EMAIL_ERROR");
        } else {
            return getSession().getLabel("IMPRESSION_BALANCE_COMPTES_EMAIL_OK");
        }
    }

    public String getIdCentreCharge() {
        return idCentreCharge;
    }

    public String getIdComptabilite() {
        return idComptabilite;
    }

    public String getIdDefinitionListe() {
        return idDefinitionListe;
    }

    public String getIdDomaine() {
        return idDomaine;
    }

    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    public String getIdMandat() {
        return idMandat;
    }

    public String getIdPeriodeComptable() {
        return idPeriodeComptable;
    }

    /**
     * @return the imprimerComptes
     */
    public Boolean getImprimerComptes() {
        return imprimerComptes;
    }

    public Boolean getImprimerPageGarde() {
        return imprimerPageGarde;
    }

    /**
     * @return the imprimerTitres
     */
    public Boolean getImprimerTitres() {
        return imprimerTitres;
    }

    public Boolean getInclurePeriodePrecedente() {
        return inclurePeriodePrecedente;
    }

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
     * @param node
     * @return
     */
    private boolean hasMontant(CGClassificationNode<CGCompteSoldeBean> node) {
        return (node.getValue().getMvtDebit() != null) || (node.getValue().getMvtDebitTotal() != null)
                || (node.getValue().getMvtCredit() != null) || (node.getValue().getMvtCreditTotal() != null);
    }

    /**
     * Initialisation des variables.
     * 
     * @param importManager
     * @param exportManager
     * @throws FWIException
     */
    private void initDocument(FWIImportManager importManager, FWIExportManager exportManager) throws FWIException {
        // import
        importManager.setImportPath(CGApplication.APPLICATION_HELIOS_REP);

        // export
        exportManager.setExportApplicationRoot(CGApplication.APPLICATION_HELIOS_REP);
        exportManager.setExportFileName(CGProcessImpressionBalanceComptes.BALANCE_COMPTES);

        if (isTypeImpressionXls()) {
            exportManager.setExportFileType(FWIDocumentType.XLS);
            exportManager.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exportManager.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
            exportManager.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
        } else {
            exportManager.setExportFileType(FWIDocumentType.PDF);
        }
    }

    public Boolean isImprimerComptes() {
        return imprimerComptes;
    }

    public Boolean isImprimerPageGarde() {
        return imprimerPageGarde;
    }

    public Boolean isImprimerTitres() {
        return imprimerTitres;
    }

    public boolean isInclurePeriodePrecedente() {
        return getInclurePeriodePrecedente().booleanValue();
    }

    private boolean isTypeImpressionXls() {
        return getTypeImpression().equals(CGImpressionUtils.TYPE_IMPRESSION_XLS);
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
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_COMPTABILITE, getSession()
                    .getLabel("IMPRESSION_BALANCE_COMPTES_COMPTABILITE"));
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_DEFLISTE,
                    getSession().getLabel("IMPRESSION_BALANCE_COMPTES_DEFLISTE"));
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_PERIODE,
                    getSession().getLabel("IMPRESSION_BALANCE_COMPTES_PERIODE"));
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_COL1,
                    getSession().getLabel("IMPRESSION_BALANCE_COMPTES_COL1"));
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_COL2,
                    getSession().getLabel("IMPRESSION_BALANCE_COMPTES_COL2"));
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_COL3,
                    getSession().getLabel("IMPRESSION_BALANCE_COMPTES_COL3"));
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_COL4,
                    getSession().getLabel("IMPRESSION_BALANCE_COMPTES_COL4"));
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_COL5,
                    getSession().getLabel("IMPRESSION_BALANCE_COMPTES_COL5"));
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_COL6,
                    getSession().getLabel("IMPRESSION_BALANCE_COMPTES_COL6"));
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_TOTAL,
                    getSession().getLabel("IMPRESSION_BALANCE_COMPTES_TOTAL"));
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_TOTALFINAL, getSession()
                    .getLabel("IMPRESSION_BALANCE_COMPTES_TOTALFINAL"));
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_TITRE,
                    getSession().getLabel("IMPRESSION_BALANCE_COMPTES_TITRE"));
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_BENEFICE,
                    getSession().getLabel("IMPRESSION_BALANCE_COMPTES_BENEFICE"));
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_PERTE,
                    getSession().getLabel("IMPRESSION_BALANCE_COMPTES_PERTE"));
            parametres.put("P_DATE_TIME", TimeHelper.getCurrentTime());
            // Bug 3007
            parametres.put("P_DATE_TIME_USER", TimeHelper.getCurrentTime() + " - " + getSession().getUserName());
            // Description comptabilité
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_COMPTABILITE,
                    globaz.helios.translation.CodeSystem.getLibelle(getSession(), idComptabilite));

            // Description exercice comptable
            CGExerciceComptable exerComptable = retrieveExerciceComptable();
            if (!exerComptable.isNew()) {
                // Set default value if no periode provided
                parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_EXERCICE,
                        exerComptable.getFullDescription());
                // Date des P&P à la fin de l'exercice
                parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_DATE, exerComptable.getDateFin());
                parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_PERIODE,
                        exerComptable.getFullDescription());
            }

            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_REFERENCE,
                    CGImpressionUtils.getReference(getSession(), this.getClass().toString()));

            parametres
                    .put(CGBalanceComptes_ParameterList.PARAM_XLS_MODE, new Boolean(isTypeImpressionXls()).toString());

            // Description période comptable
            if (!JadeStringUtil.isIntegerEmpty(idPeriodeComptable)) {
                CGPeriodeComptable periodeComptable = retrievePeriodeComptable();
                if (!periodeComptable.isNew()) {
                    forCodePeriode = periodeComptable.getCode();
                    if (isInclurePeriodePrecedente() && (JACalendar.getMonth(periodeComptable.getDateFin()) != 1)) {
                        parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_PERIODE, getSession()
                                .getLabel("JANVIER_A") + " " + periodeComptable.getFullDescription());
                    } else {
                        parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_PERIODE,
                                periodeComptable.getFullDescription());
                    }
                    // Date des P&P à la fin de la période
                    parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_DATE,
                            periodeComptable.getDateFin());
                }
            } else {
                // Si l'option "Tout l'exercice" est selectionnée, on l'affiche sur la liste
                if ("0".equals(idPeriodeComptable)) {
                    parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_PERIODE,
                            getSession().getLabel("TOUT_LEXERCICE"));
                }
            }

            // Description definition de liste
            CGDefinitionListe definitionListe = retrieveDefinitionListe();
            if (!definitionListe.isNew()) {
                parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_DEFLISTE,
                        definitionListe.getLibelle());
                idClassification = definitionListe.getIdClassification();
            }

            // Description du centre de charge
            if (!JadeStringUtil.isIntegerEmpty(getIdCentreCharge())) {
                CGCentreCharge centreCharge = retrieveCentreCharge();
                if (!centreCharge.isNew()) {
                    parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_CENTRECHARGE,
                            getSession().getLabel("IMPRESSION_BALANCE_COMPTES_CENTRECHARGE"));
                    parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_CENTRECHARGE,
                            centreCharge.getLibelle());
                }
            } else {
                parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_CENTRECHARGE, "");
            }

            // Impression détails page de garde
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_IMPRIMER_PAGE_GARDE, imprimerPageGarde);
            // Impression titres
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_IMPRIMER_TITRES, imprimerTitres);
            // Impression comptes
            parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_IMPRIMER_COMPTES, imprimerComptes);

            // Numero inforom
            parametres.put("NUMERO_INFOROM", CGProcessImpressionBalanceComptes.NUMERO_REFERENCE_INFOROM);

            // Description du mandat
            if (!JadeStringUtil.isIntegerEmpty(getIdMandat())) {
                globaz.helios.db.comptes.CGMandat mandat = retrieveMandat();
                if (!mandat.isNew()) {
                    parametres.put(CGBalanceComptes_ParameterList.PARAM_BALANCE_COMPTES_LABEL_COMPANYNAME,
                            mandat.getLibelle());
                }
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            JadeLogger.error(this, e);
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

        if (!node.isLeaf()) {
            if (hasMontant(node)) {
                if (!CGClasseCompteServices.isNiveauClassificationToBig(niveauClassification, node.getValue()
                        .getNiveau())) {
                    CGCompteSoldeBean bean = new CGCompteSoldeBean(node.getValue());
                    bean.resetMvt();
                    bean.resetTotaux();
                    // HACK pour iReport afin de gérer correctement les groupes
                    bean.setNiveau(bean.getNiveau() + bean.getNoClasse());
                    listBeanDocument.add(bean);
                }
            }

            if (node.hasChildren()) {
                for (CGClassificationNode<CGCompteSoldeBean> child : node.getChildren().values()) {
                    recurseDisplayNode(child, listBeanDocument);
                }
            }

            // TOTAUX
            if (hasMontant(node)) {
                if (!CGClasseCompteServices.isNiveauClassificationToBig(niveauClassification, node.getValue()
                        .getNiveau())) {
                    CGCompteSoldeBean bean = new CGCompteSoldeBean(node.getValue());
                    bean.resetMvt();
                    // HACK pour iReport afin de gérer correctement les groupes
                    bean.setNiveau(bean.getNiveau() + bean.getNoClasse());
                    bean.setNoClasseLibelle(getSession().getLabel("IMPRESSION_BALANCE_COMPTES_TOTAL") + " "
                            + bean.getNoClasseLibelle());
                    listBeanDocument.add(bean);
                }
            }

        } else if (hasMontant(node)) {
            if (!CGClasseCompteServices.isNiveauClassificationToBig(niveauClassification, node.getValue().getNiveau())) {
                CGCompteSoldeBean bean = node.getValue();
                bean.resetTotaux();
                listBeanDocument.add(bean);
            }
        }

    }

    /**
     * @return an entity of CGCentreCharge
     * @throws Exception
     */
    private CGCentreCharge retrieveCentreCharge() throws Exception {
        CGCentreCharge centreCharge = new CGCentreCharge();
        centreCharge.setIdCentreCharge(getIdCentreCharge());
        centreCharge.setIdMandat(getIdMandat());
        centreCharge.setSession(getSession());
        centreCharge.retrieve(getTransaction());
        return centreCharge;
    }

    /**
     * @return an entity of CGDefinitionListe
     * @throws Exception
     */
    private CGDefinitionListe retrieveDefinitionListe() throws Exception {
        CGDefinitionListe definitionListe = new CGDefinitionListe();
        definitionListe.setIdDefinitionListe(idDefinitionListe);
        definitionListe.setSession(getSession());
        definitionListe.retrieve();
        return definitionListe;
    }

    /**
     * @return an entity of CGExerciceComptable
     * @throws Exception
     */
    private CGExerciceComptable retrieveExerciceComptable() throws Exception {
        CGExerciceComptable exerComptable = new CGExerciceComptable();
        exerComptable.setIdExerciceComptable(idExerciceComptable);
        exerComptable.setSession(getSession());
        exerComptable.retrieve();
        return exerComptable;
    }

    /**
     * @return an entity of CGMandat
     * @throws Exception
     */
    private globaz.helios.db.comptes.CGMandat retrieveMandat() throws Exception {
        globaz.helios.db.comptes.CGMandat mandat = new globaz.helios.db.comptes.CGMandat();
        mandat.setIdMandat(idMandat);
        mandat.setSession(getSession());
        mandat.retrieve();
        return mandat;
    }

    /**
     * @return an entity of CGPeriodeComptable
     * @throws Exception
     */
    private CGPeriodeComptable retrievePeriodeComptable() throws Exception {
        CGPeriodeComptable periodeComptable = new CGPeriodeComptable();
        periodeComptable.setIdPeriodeComptable(idPeriodeComptable);
        periodeComptable.setSession(getSession());
        periodeComptable.retrieve();
        return periodeComptable;
    }

    public void setClassesCompteList(String classesCompteList) {
        this.classesCompteList = classesCompteList;
    }

    public void setIdCentreCharge(String newIdCentreCharge) {
        idCentreCharge = newIdCentreCharge;
    }

    public void setIdComptabilite(String newIdComptabilite) {
        idComptabilite = newIdComptabilite;
    }

    public void setIdDefinitionListe(String newIdDefinitionListe) {
        idDefinitionListe = newIdDefinitionListe;
    }

    public void setIdDomaine(String newIdDomaine) {
        idDomaine = newIdDomaine;
    }

    public void setIdExerciceComptable(String newIdExerciceComptable) {
        idExerciceComptable = newIdExerciceComptable;
    }

    public void setIdMandat(String newIdMandat) {
        idMandat = newIdMandat;
    }

    public void setIdPeriodeComptable(String newIdPeriodeComptable) {
        idPeriodeComptable = newIdPeriodeComptable;
    }

    public void setImprimerComptes(Boolean newImprimerComptes) {
        imprimerComptes = newImprimerComptes;
    }

    public void setImprimerPageGarde(Boolean newImprimerPageGarde) {
        imprimerPageGarde = newImprimerPageGarde;
    }

    public void setImprimerTitres(Boolean newImprimerTitres) {
        imprimerTitres = newImprimerTitres;
    }

    public void setInclurePeriodePrecedente(Boolean inclurePeriodePrecedente) {
        this.inclurePeriodePrecedente = inclurePeriodePrecedente;
    }

    public void setListeIdClasseCompte(String newListeIdClasseCompte) {
        classesCompteList = newListeIdClasseCompte;
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
     * @return liste de bean soumise à JasperReport
     */
    private List<CGCompteSoldeBean> sortAndDisplayResult(Map<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepot) {
        List<CGCompteSoldeBean> listBeanDocument = new ArrayList<CGCompteSoldeBean>();
        TreeMap<String, CGClassificationNode<CGCompteSoldeBean>> nodesDepotTrie = new TreeMap<String, CGClassificationNode<CGCompteSoldeBean>>();

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
