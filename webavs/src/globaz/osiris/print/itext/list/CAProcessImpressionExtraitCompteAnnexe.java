package globaz.osiris.print.itext.list;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.caisse.report.helper.ICaisseReportHelper;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.export.FWIExportManager;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.types.FWIDocumentType;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.osiris.api.APIOperation;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.extrait.CAExtraitCompteManager;
import globaz.osiris.db.contentieux.CAExtraitCompteListViewBean;
import globaz.osiris.db.contentieux.CALigneExtraitCompte;
import globaz.osiris.db.print.CAListExtraitCompteAnnexeViewBean;
import globaz.osiris.external.IntRole;
import globaz.osiris.external.IntTiers;
import globaz.osiris.print.itext.list.resume.cpt.annexe.CAResumeCptAnnexe_Bean;
import globaz.osiris.print.itext.list.resume.cpt.annexe.CAResumeCptAnnexe_ParameterList;
import globaz.osiris.translation.CACodeSystem;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.constantes.IConstantes;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.CommonExcelmlUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;

/**
 * @author dda
 */
public class CAProcessImpressionExtraitCompteAnnexe extends BProcess {

    private static final long serialVersionUID = 1L;
    public static final String EXCELML_MODEL_CELL_BLANK = "CELL_BLANK";
    public static final String EXCELML_MODEL_CELL_BLANK_2 = "CELL_BLANK_2";
    public static final String EXCELML_MODEL_CELL_NUMERO_AFFILIE = "CELL_NUMERO_AFFILIE";
    public static final String EXCELML_MODEL_CELL_TITRE = "CELL_TITRE";
    public static final String EXCELML_MODEL_CELL_TOTAL_LABEL = "CELL_TOTAL_LABEL";
    public static final String EXCELML_MODEL_CELL_TOTAL_MONTANT = "CELL_TOTAL_MONTANT";
    public static final String EXCELML_MODEL_COL_NAME_CREDIT = "COL_CREDIT";
    public static final String EXCELML_MODEL_COL_NAME_DATE_COMPTABLE = "COL_DATE_COMPTABLE";
    public static final String EXCELML_MODEL_COL_NAME_DATE_VALEUR = "COL_DATE_VALEUR";
    public static final String EXCELML_MODEL_COL_NAME_DEBIT = "COL_DEBIT";
    public static final String EXCELML_MODEL_COL_NAME_DECOMPTE = "COL_DECOMPTE";
    public static final String EXCELML_MODEL_COL_NAME_DESCRIPTION = "COL_DESCRIPTION";
    public static final String EXCELML_MODEL_COL_NAME_PROVENANCE_PAIEMENT = "COL_PROVENANCE_PAIEMENT";
    public static final String EXCELML_MODEL_COL_NAME_SOLDE = "COL_SOLDE";
    public static final String EXCELML_MODEL_NAME = "ExtraitCompteModele.xml";
    public static final String EXCELML_MODEL_NUMERO_INFOROM = "0298GCA";
    public static final String EXCELML_OUTPUT_FILE_NAME = "ExtraitCompte.xml";
    private static final String LABEL_IMPR_EXTR_CPT_ANNEXE_MONTANT_NOTRE_FAVEUR = "IMPR_EXTR_CPT_ANNEXE_MONTANT_NOTRE_FAVEUR";
    private static final String LABEL_IMPR_EXTR_CPT_ANNEXE_MONTANT_VOTRE_FAVEUR = "IMPR_EXTR_CPT_ANNEXE_MONTANT_VOTRE_FAVEUR";
    private static final int MAX_DOC_PER_PDF = 1000;
    private static final String NUMERO_REFERENCE_INFOROM = "0019GCA";
    private static final String PRINT_LANGUAGE_DE = "DE";
    private static final String PRINT_LANGUAGE_FR = "FR";
    private static final String PRINT_LANGUAGE_IT = "IT";
    public static final String TEMPLATE_DOC = "CAResumeCptAnnexe";
    public static final String TEMPLATE_DOC_EBUSINESS = "CAResumeCptAnnexeEbusiness";

    private boolean batchPrint = true;
    private CommonExcelmlContainer containerExtraitCompteExcelml = new CommonExcelmlContainer();
    private String descriptionAffilie = null;
    private String documentDate = null;
    private transient FWIExportManager exporter = new FWIExportManager();
    private FWIExportManager exportManager = new FWIExportManager();
    private String forIdCategorie = new String();
    private String forIdGenreCompte = new String();
    private String forIdTypeOperation = null;
    private String forSelectionRole = null;
    private String forSelectionSections = null;
    private String forSelectionTri = null;
    private String forTriSpecial = null;
    private String fromDate = null;
    private String fromNoAffilie = null;
    private String fromNoSatConsul = null;
    private String fromSection = null;
    private FWIImportManager importManager = new FWIImportManager();
    private Boolean imprimerExtraitCompteExcelml = new Boolean(false);
    private List<CAResumeCptAnnexe_Bean> listBeanDocument = new ArrayList<CAResumeCptAnnexe_Bean>();
    private Map<String, String> parametres = new HashMap<String, String>();
    private String printLanguage;
    private String printLanguageFromScreen;
    private String untilDate = null;
    private String untilNoAffilie = null;
    private String untilNoSatConsul = null;
    private String untilSection = null;
    private byte[] byteFile;
    private String forIdCompteAnnexe;
    private boolean ebusinessMode = false;
    private String docLocation;

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     */
    public CAProcessImpressionExtraitCompteAnnexe() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     * 
     * @param parent
     */
    public CAProcessImpressionExtraitCompteAnnexe(BProcess parent) throws Exception {
        super(parent);
        init();
    }

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     * 
     * @param session
     */
    public CAProcessImpressionExtraitCompteAnnexe(BSession session) throws Exception {
        super(session);
        init();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeCleanUp()
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Exécute le processus. Main method de la fonction "Liste Extrait des comptes annexes". Constuit les documents (1
     * pdf contenant x Extrait de compte). Attache le pdf à l'email.
     */
    @Override
    protected boolean _executeProcess() {
        try {
            if (ebusinessMode) {
                buildDocumentForEbusiness();
                exportDocument();
                List<JadePublishDocument> listAttachedDocs = getAttachedDocuments();
                if (listAttachedDocs.size() <= 0) {
                    throw new Exception("Aucun document n'a été généré");
                } else {
                    JadePublishDocument doc = listAttachedDocs.get(0);
                    docLocation = doc.getDocumentLocation();
                    setSendCompletionMail(false);
                }
            } else {
                buildDocument();
                exportDocument();
                if (getImprimerExtraitCompteExcelml().booleanValue() && (containerExtraitCompteExcelml.size() >= 1)) {
                    addHeaderInExtraitCompteExcelml();
                    String extraitCompteExcelmlFilePath = createFileExtraitCompteExcelml();
                    registerFileExtraitCompteExcelml(extraitCompteExcelmlFilePath);
                }
            }

            return true;
        } catch (Exception e) {
            JadeLogger.error(this, e);
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return false;
        }
    }

    private JadePublishDocumentInfo _fillDocInfo(CACompteAnnexe ca) throws Exception {
        JadePublishDocumentInfo documentInfo = createDocumentInfo();
        documentInfo.setDocumentTypeNumber(CAProcessImpressionExtraitCompteAnnexe.NUMERO_REFERENCE_INFOROM);

        if ((ITIRole.CS_AFFILIE.equals(ca.getRole().getIdRole()))
                || (IntRole.ROLE_AFFILIE_PARITAIRE.equals(ca.getRole().getIdRole()))
                || (IntRole.ROLE_AFFILIE_PERSONNEL.equals(ca.getRole().getIdRole()))) {
            IFormatData affilieFormater = ((globaz.pyxis.application.TIApplication) GlobazServer.getCurrentSystem()
                    .getApplication(globaz.pyxis.application.TIApplication.DEFAULT_APPLICATION_PYXIS))
                    .getAffileFormater();
            TIDocumentInfoHelper.fill(documentInfo, ca.getIdTiers(), getSession(), ca.getRole().getIdRole(),
                    ca.getIdExterneRole(), affilieFormater.unformat(ca.getIdExterneRole()));
        }

        documentInfo.setPublishDocument(false);
        documentInfo.setArchiveDocument(true);

        return documentInfo;

    }

    public final FWIExportManager _getExporter() {
        if (exporter == null) {
            exporter = new FWIExportManager();
        }
        return exporter;
    }

    @Override
    protected void _validate() throws Exception {

        if (JAUtil.isDateEmpty(getDocumentDate())) {
            this._addError(getTransaction(), getSession().getLabel("IMPRESSION_EXTR_CPT_ANNEXE_DATE_DOC_ERROR"));
        }
        if (getParent() == null) {
            setSendCompletionMail(true);
            setControleTransaction(true);
        }
    }

    private void addDataInExtraitCompteExcelml() {
        for (CAResumeCptAnnexe_Bean aResumeCompteAnnexe : listBeanDocument) {

            String dateComptable = " ";
            String dateValeur = " ";
            String decompte = " ";
            String description = " ";
            String provenancePaiement = " ";

            if (!JadeStringUtil.isEmpty(aResumeCompteAnnexe.getCOL_8())) {
                dateComptable = aResumeCompteAnnexe.getCOL_8();
            }

            if (!JadeStringUtil.isEmpty(aResumeCompteAnnexe.getCOL_1())) {
                dateValeur = aResumeCompteAnnexe.getCOL_1();
            }

            if (!JadeStringUtil.isEmpty(aResumeCompteAnnexe.getCOL_2())) {
                decompte = aResumeCompteAnnexe.getCOL_2();
            }

            if (!JadeStringUtil.isEmpty(aResumeCompteAnnexe.getCOL_3())) {
                description = aResumeCompteAnnexe.getCOL_3();
            }

            if (!JadeStringUtil.isEmpty(aResumeCompteAnnexe.getCOL_7())) {
                provenancePaiement = aResumeCompteAnnexe.getCOL_7();
            }

            String montantDebit = "0";
            String montantCredit = "0";
            String solde = "0";

            if (aResumeCompteAnnexe.getCOL_4() != null) {
                montantDebit = new FWCurrency(aResumeCompteAnnexe.getCOL_4()).toString();
            }

            if (aResumeCompteAnnexe.getCOL_5() != null) {
                montantCredit = new FWCurrency(aResumeCompteAnnexe.getCOL_5()).toString();
            }

            if (aResumeCompteAnnexe.getCOL_6() != null) {
                solde = new FWCurrency(aResumeCompteAnnexe.getCOL_6()).toString();
            }

            containerExtraitCompteExcelml.put(
                    CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_COL_NAME_DATE_COMPTABLE, dateComptable);
            containerExtraitCompteExcelml.put(
                    CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_COL_NAME_DATE_VALEUR, dateValeur);
            containerExtraitCompteExcelml.put(CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_COL_NAME_DECOMPTE,
                    decompte);
            containerExtraitCompteExcelml.put(
                    CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_COL_NAME_DESCRIPTION, description);
            containerExtraitCompteExcelml.put(CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_COL_NAME_DEBIT,
                    montantDebit);
            containerExtraitCompteExcelml.put(CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_COL_NAME_CREDIT,
                    montantCredit);
            containerExtraitCompteExcelml.put(CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_COL_NAME_SOLDE,
                    solde);
            containerExtraitCompteExcelml.put(
                    CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_COL_NAME_PROVENANCE_PAIEMENT,
                    provenancePaiement);

        }

    }

    private void addHeaderInExtraitCompteExcelml() {

        String titre = FWMessageFormat.format(getSession().getLabel("EXTRAIT_COMPTE_EXCEL_TITRE"), fromDate, untilDate);
        String numeroAffilie = getDescriptionAffilie();

        containerExtraitCompteExcelml.put(CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_CELL_BLANK_2, " ");
        containerExtraitCompteExcelml.put(CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_CELL_TITRE, titre);
        containerExtraitCompteExcelml.put(CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_CELL_NUMERO_AFFILIE,
                numeroAffilie);

    }

    private void addSoldeCompteAnnexeInExtraitCompteExcelml(FWCurrency soldeCompteAnnexe) {

        String idLabel = "EXTRAIT_COMPTE_EXCEL_TOTAL_EN_NOTRE_FAVEUR";
        if (soldeCompteAnnexe.doubleValue() < 0) {
            idLabel = "EXTRAIT_COMPTE_EXCEL_TOTAL_EN_VOTRE_FAVEUR";
        }

        String totalLabel = FWMessageFormat.format(getSession().getLabel(idLabel), untilDate);
        String totalMontant = soldeCompteAnnexe.toString();

        containerExtraitCompteExcelml.put(CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_CELL_BLANK, " ");
        containerExtraitCompteExcelml.put(CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_CELL_TOTAL_LABEL,
                totalLabel);
        containerExtraitCompteExcelml.put(CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_CELL_TOTAL_MONTANT,
                totalMontant);

    }

    /**
     * Construit le document (1 pdf contenant x Extrait de compte).
     */
    protected void buildDocument() {
        CACompteAnnexeManager caMgr = new CACompteAnnexeManager();
        BStatement statement = null;
        // Construction de la source
        try {
            setCompteAnnexeManagerParameters(caMgr);

            CACompteAnnexe ca = null;

            CAApplication application = (CAApplication) getSession().getApplication();
            statement = caMgr.cursorOpen(getTransaction());

            importManager.deleteAll();

            CAExtraitCompteManager manager = new CAExtraitCompteManager();

            setProgressScaleValue(caMgr.getCount());

            while ((ca = (CACompteAnnexe) caMgr.cursorReadNext(statement)) != null) {
                managePrintLanguage(ca);

                // 6820 remplissage du documentInfo avant le traitement de l'entête
                JadePublishDocumentInfo documentInfo = _fillDocInfo(ca);

                ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                        documentInfo, getSession().getApplication(), getPrintLanguage());
                CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
                listBeanDocument.clear();

                prepareHeaderParameters(application, headerBean, getPrintLanguage());
                prepareContentParameters(application, getPrintLanguage());

                CAExtraitCompteListViewBean ds = new CAExtraitCompteListViewBean();
                setExtraitCompteListViewBeanParameters(ca, ds);

                ds.find(getTransaction(), manager);

                FWCurrency soldeCumule = createRows(ds, ca.getTiers().getLangueISO());

                if (listBeanDocument.size() != 0) {
                    prepareVariableParameters(ca, application, soldeCumule);

                    prepareAdresse(ca, application, headerBean);
                    prepareNoAffilie(ca, headerBean);

                    importManager.clearParam();
                    importManager.setDocumentName(ca.getIdExterneRole());

                    // Doivent être appelé avant les addHeader/Footer/Signature
                    importManager.setParametre(parametres);
                    importManager.setDocumentTemplate(CAProcessImpressionExtraitCompteAnnexe.TEMPLATE_DOC);

                    caisseReportHelper.addHeaderParameters(importManager, headerBean);
                    caisseReportHelper.addFooterParameters(importManager);
                    caisseReportHelper.addSignatureParameters(importManager);

                    importManager.setBeanCollectionDataSource(listBeanDocument);
                    importManager.createDocument();

                    if (getImprimerExtraitCompteExcelml().booleanValue()) {
                        addDataInExtraitCompteExcelml();
                        addSoldeCompteAnnexeInExtraitCompteExcelml(soldeCumule);
                    }

                    if (!getMemoryLog().hasErrors()) {
                        exportManager.deleteAll();
                        exportManager.addAll(importManager.getList());
                        exportManager.exportReport();
                        String filename = exportManager.getExportNewFilePath();

                        super.registerAttachedDocument(documentInfo, filename);
                        importManager.deleteAll();
                    }

                    incProgressCounter();
                }

                if ((batchPrint)
                        && (getAttachedDocuments().size() == CAProcessImpressionExtraitCompteAnnexe.MAX_DOC_PER_PDF)) {
                    exportDocument();
                }
            }
        } catch (FWIException e) {
            getMemoryLog().logMessage(null, e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.fatal(this, e);
        } catch (Exception e2) {
            getMemoryLog().logMessage(null, e2.getMessage(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.fatal(this, e2);
        } finally {
            try {
                caMgr.cursorClose(statement);
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
    }

    protected void buildDocumentForEbusiness() {
        try {
            CAApplication application = (CAApplication) getSession().getApplication();
            CACompteAnnexe compteAnnexe = new CACompteAnnexe();
            compteAnnexe.setSession(getSession());
            compteAnnexe.setIdCompteAnnexe(forIdCompteAnnexe);
            compteAnnexe.retrieve();

            setForSelectionRole(compteAnnexe.getIdRole());
            setForSelectionTri(CAExtraitCompteManager.ORDER_BY_DATE_COMPTABLE);
            setForIdTypeOperation(APIOperation.CAECRITURE);
            setForSelectionSections(CAExtraitCompteManager.SOLDE_ALL);

            importManager.deleteAll();

            managePrintLanguage(compteAnnexe);
            JadePublishDocumentInfo documentInfo = _fillDocInfo(compteAnnexe);

            ICaisseReportHelper caisseReportHelper = CaisseHelperFactory.getInstance().getCaisseReportHelper(
                    documentInfo, getSession().getApplication(), getPrintLanguage());
            CaisseHeaderReportBean headerBean = new CaisseHeaderReportBean();
            listBeanDocument.clear();

            prepareHeaderParametersForEbusiness(application, headerBean, getPrintLanguage());
            prepareContentParameters(application, getPrintLanguage());

            CAExtraitCompteListViewBean extraitCompteListViewBean = new CAExtraitCompteListViewBean();
            setExtraitCompteListViewBeanParameters(compteAnnexe, extraitCompteListViewBean);
            extraitCompteListViewBean.find(getTransaction(), new CAExtraitCompteManager());

            FWCurrency soldeCumule = createRows(extraitCompteListViewBean, compteAnnexe.getTiers().getLangueISO());

            prepareVariableParameters(compteAnnexe, application, soldeCumule);
            prepareAdresse(compteAnnexe, application, headerBean);
            prepareNoAffilie(compteAnnexe, headerBean);

            importManager.clearParam();
            importManager.setDocumentName(compteAnnexe.getIdExterneRole());

            // Doivent être appelé avant les addHeader/Footer/Signature
            importManager.setParametre(parametres);
            importManager.setDocumentTemplate(CAProcessImpressionExtraitCompteAnnexe.TEMPLATE_DOC_EBUSINESS);

            caisseReportHelper.addHeaderParameters(importManager, headerBean);
            caisseReportHelper.addFooterParameters(importManager);
            caisseReportHelper.addSignatureParameters(importManager);

            importManager.setBeanCollectionDataSource(listBeanDocument);
            // importManager.setParametre(ICaisseReportHelper.PARAM_SIGNATURE_SIGNATAIRE, "");
            importManager.createDocument();

            if (getImprimerExtraitCompteExcelml().booleanValue()) {
                addDataInExtraitCompteExcelml();
                addSoldeCompteAnnexeInExtraitCompteExcelml(soldeCumule);
            }

            if (!getMemoryLog().hasErrors()) {
                exportManager.deleteAll();
                exportManager.addAll(importManager.getList());
                exportManager.exportReport();
                String filename = exportManager.getExportNewFilePath();

                super.registerAttachedDocument(documentInfo, filename);
                importManager.deleteAll();
            }

            if ((batchPrint)
                    && (getAttachedDocuments().size() == CAProcessImpressionExtraitCompteAnnexe.MAX_DOC_PER_PDF)) {
                exportDocument();
            }

        } catch (FWIException e) {
            getMemoryLog().logMessage(null, e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.fatal(this, e);
        } catch (Exception e2) {
            getMemoryLog().logMessage(null, e2.getMessage(), FWMessage.FATAL, this.getClass().getName());
            JadeLogger.fatal(this, e2);
        }
    }

    private String createFileExtraitCompteExcelml() throws Exception {

        String xmlModelPath = Jade.getInstance().getExternalModelDir() + CAApplication.DEFAULT_OSIRIS_ROOT
                + "/model/excelml/" + getSession().getIdLangueISO().toUpperCase() + "/"
                + CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_NAME;

        String xmlOutputPath = Jade.getInstance().getPersistenceDir()
                + JadeFilenameUtil
                        .addOrReplaceFilenameSuffixUID(CAProcessImpressionExtraitCompteAnnexe.EXCELML_OUTPUT_FILE_NAME);

        xmlOutputPath = CommonExcelmlUtils.createDocumentExcel(xmlModelPath, xmlOutputPath,
                containerExtraitCompteExcelml);

        return xmlOutputPath;

    }

    /**
     * Ajoute une ligne à l'extait.
     * 
     * @param className
     * @param ligne
     * @param doitAvoir
     * @param soldeCumule
     * @param list
     */
    protected void createRow(String className, CALigneExtraitCompte ligne, FWCurrency doitAvoir,
            FWCurrency soldeCumule, String langueIso) throws Exception {
        CAResumeCptAnnexe_Bean bean = (CAResumeCptAnnexe_Bean) Class.forName(className).newInstance();
        if (bean.prepareValue(ligne, doitAvoir, soldeCumule, getTransaction(), getSession(), langueIso)) {
            listBeanDocument.add(bean);
        }
    }

    /**
     * Ajoute toutes les lignes de l'extrait de compte.
     * 
     * @param ds
     * @return
     * @throws Exception
     */
    private FWCurrency createRows(CAExtraitCompteListViewBean ds, String langueIso) throws Exception {
        FWCurrency soldeCumule = new FWCurrency();
        for (int j = 0; j < ds.size(); j++) {
            CALigneExtraitCompte entity = (CALigneExtraitCompte) ds.getLigneExtraitCompte().get(j);
            FWCurrency doitAvoir = new FWCurrency();
            doitAvoir.add(entity.getTotal());
            soldeCumule.add(entity.getTotal());

            createRow(CAResumeCptAnnexe_Bean.class.getName(), entity, doitAvoir, soldeCumule, langueIso);
        }

        return soldeCumule;
    }

    /**
     * Export les documents à imprimer. Reset importManager.
     * 
     * @return
     * @throws JRException
     */
    private boolean exportDocument() throws Exception {
        if (getAttachedDocuments().size() > 0) {
            // exportManager.deleteAll();
            // exportManager.addAll(importManager.getList());
            // exportManager.exportReport();

            if (isAborted()) {
                return false;
            }

            JadePublishDocumentInfo documentInfo = createDocumentInfo();
            documentInfo.setDocumentTypeNumber(CAProcessImpressionExtraitCompteAnnexe.NUMERO_REFERENCE_INFOROM);
            this.mergePDF(documentInfo, false, 0, false, null);

            // importManager.deleteAll();

            return true;
        } else {
            getMemoryLog().logMessage(null, getSession().getLabel("IMPRESSION_EXTR_CPT_ANNEXE_NOT_FOUND"),
                    FWMessage.AVERTISSEMENT, this.getClass().getName());
            return false;
        }
    }

    public CommonExcelmlContainer getContainerExtraitCompteExcelml() {
        return containerExtraitCompteExcelml;
    }

    /**
     * @return the descriptionAffilie
     */
    public String getDescriptionAffilie() {
        return descriptionAffilie;
    }

    /**
     * Returns the documentDate. Si le documentDate est null ou vide la méthode renvoie la date du jour.
     * 
     * @return String
     */
    public String getDocumentDate() {
        if (!JadeStringUtil.isBlank(documentDate)) {
            return documentDate;
        } else {
            return getFormatedDateToday();
        }
    }

    @Override
    protected String getEMailObject() {
        if ((!JadeStringUtil.isBlank(getFromNoAffilie()) && !JadeStringUtil.isBlank(getUntilNoAffilie()))
                && (getFromNoAffilie().equals(getUntilNoAffilie()))) {
            if (isOnError()) {
                return FWMessageFormat.format(getSession().getLabel("IMPRESSION_EXTR_CPT_ANNEXE_UNIQUE_EMAIL_ERROR"),
                        getUntilNoAffilie());
            } else {
                return FWMessageFormat.format(getSession().getLabel("IMPRESSION_EXTR_CPT_ANNEXE_UNIQUE_EMAIL_OK"),
                        getUntilNoAffilie());
            }
        } else {
            if (isOnError()) {
                return getSession().getLabel("IMPRESSION_EXTR_CPT_ANNEXE_EMAIL_ERROR");
            } else {
                return getSession().getLabel("IMPRESSION_EXTR_CPT_ANNEXE_EMAIL_OK");
            }
        }
    }

    /**
     * @return
     */
    public String getForIdCategorie() {
        return forIdCategorie;
    }

    /**
     * @return
     */
    public String getForIdGenreCompte() {
        return forIdGenreCompte;
    }

    /**
     * Returns the forIdTypeOperation.
     * 
     * @return String
     */
    public String getForIdTypeOperation() {
        return forIdTypeOperation;
    }

    /**
     * Return la date actuelle. Date default de la fonction "Impression de la liste des extraits des comptes annexe".
     * 
     * @return
     */
    public String getFormatedDateToday() {
        return JACalendar.todayJJsMMsAAAA();
    }

    public String getForSelectionRole() {
        return forSelectionRole;
    }

    /**
     * Returns the forSelectionSections.
     * 
     * @return String
     */
    public String getForSelectionSections() {
        return forSelectionSections;
    }

    /**
     * @return
     */
    public String getForSelectionTri() {
        return forSelectionTri;
    }

    /**
     * @return
     */
    public String getForTriSpecial() {
        return forTriSpecial;
    }

    /**
     * Returns the fromDate.
     * 
     * @return String
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * Returns the fromNoAffilie.
     * 
     * @return String
     */
    public String getFromNoAffilie() {
        return fromNoAffilie;
    }

    /**
     * @return
     */
    public String getFromNoSatConsul() {
        return fromNoSatConsul;
    }

    /**
     * @return
     */
    public String getFromSection() {
        return fromSection;
    }

    /**
     * Retourne le document à importer dans l'email.
     * 
     * @return
     */
    public FWIImportManager getImporter() {
        try {
            // Create datasource
            batchPrint = false;
            buildDocument();

            return importManager;

        } catch (Exception e) {
            JadeLogger.error(this, e);
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return null;
        }
    }

    public Boolean getImprimerExtraitCompteExcelml() {
        return imprimerExtraitCompteExcelml;
    }

    /**
     * @return
     */
    public String getPrintLanguage() {
        return printLanguage;
    }

    /**
     * @return
     */
    public String getPrintLanguageFromScreen() {
        return printLanguageFromScreen;
    }

    /**
     * @return
     */
    public String getUntilDate() {
        return untilDate;
    }

    /**
     * Returns the untilNoAffilie.
     * 
     * @return String
     */
    public String getUntilNoAffilie() {
        return untilNoAffilie;
    }

    /**
     * @return
     */
    public String getUntilNoSatConsul() {
        return untilNoSatConsul;
    }

    /**
     * @return
     */
    public String getUntilSection() {
        return untilSection;
    }

    /**
     * Initialisation des manager d'importations et exportations.
     * 
     * @throws FWIException
     */
    private void init() throws FWIException {
        // import
        importManager.setImportPath(CAApplication.DEFAULT_OSIRIS_ROOT);
        // export
        exportManager.setExportApplicationRoot(CAApplication.DEFAULT_OSIRIS_ROOT);
        exportManager.setExportFileName("ExtraitsCompesAnnexes");
        exportManager.setExportFileType(FWIDocumentType.PDF);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Si le langue d'impression est vierge la langue d'impression sera alors celle du tiers. Cette méthode sert aussi a
     * redéfinir la langue d'impression des Tag-lib en format "FR", "DE" ou "IT".
     * 
     * @param ca
     */
    private void managePrintLanguage(CACompteAnnexe ca) {
        if (JadeStringUtil.isBlank(getPrintLanguageFromScreen())) {
            if ((ca.getTiers() != null) && (!JadeStringUtil.isBlank(ca.getTiers().getLangueISO()))) {
                setPrintLanguage(ca.getTiers().getLangueISO());
            } else {
                setPrintLanguage(CAProcessImpressionExtraitCompteAnnexe.PRINT_LANGUAGE_DE);
            }
        } else {
            if (getPrintLanguageFromScreen().equals(CACodeSystem.CS_ALLEMAND)
                    || getPrintLanguageFromScreen().equals(CAProcessImpressionExtraitCompteAnnexe.PRINT_LANGUAGE_DE)) {
                setPrintLanguage(CAProcessImpressionExtraitCompteAnnexe.PRINT_LANGUAGE_DE);
            } else if (getPrintLanguageFromScreen().equals(CACodeSystem.CS_FRANCAIS)
                    || getPrintLanguageFromScreen().equals(CAProcessImpressionExtraitCompteAnnexe.PRINT_LANGUAGE_FR)) {
                setPrintLanguage(CAProcessImpressionExtraitCompteAnnexe.PRINT_LANGUAGE_FR);
            } else if (getPrintLanguageFromScreen().equals(CACodeSystem.CS_ITALIEN)
                    || getPrintLanguageFromScreen().equals(CAProcessImpressionExtraitCompteAnnexe.PRINT_LANGUAGE_IT)) {
                setPrintLanguage(CAProcessImpressionExtraitCompteAnnexe.PRINT_LANGUAGE_IT);
            } else {
                setPrintLanguage(CAProcessImpressionExtraitCompteAnnexe.PRINT_LANGUAGE_DE);
            }
        }
    }

    /**
     * Ajout de l'adresse de courrier du compte.
     * 
     * @param ca
     * @param application
     * @param headerBean
     */
    private void prepareAdresse(CACompteAnnexe ca, CAApplication application, CaisseHeaderReportBean headerBean) {
        try {
            String domaine = ca._getDefaultDomainFromRole();
            headerBean.setAdresse(ca.getTiers().getAdresseLienAsString(IntTiers.TYPE_LIEN_MANDATAIRE,
                    IConstantes.CS_AVOIR_ADRESSE_COURRIER, domaine, getDocumentDate(), getPrintLanguageFromScreen()));
        } catch (Exception e) {
            JadeLogger.error(this, e);
            headerBean.setAdresse(application.getLabel("IMPR_EXTR_CPT_ANNEXE_ADRESSE_NOT_FOUND", getPrintLanguage()));
        }
    }

    /**
     * Ajoute les paramètes nécessaire au Contenu du document.
     * 
     * @param application
     * @param headerBean
     * @param isoLangue
     */
    private void prepareContentParameters(CAApplication application, String isoLangue) {
        try {
            parametres.put(CAResumeCptAnnexe_ParameterList.COL_1,
                    application.getLabel("IMPR_EXTR_CPT_ANNEXE_COL_1", isoLangue));

            parametres.put(CAResumeCptAnnexe_ParameterList.COL_2,
                    application.getLabel("IMPR_EXTR_CPT_ANNEXE_COL_2", isoLangue));

            parametres.put(CAResumeCptAnnexe_ParameterList.COL_3,
                    application.getLabel("IMPR_EXTR_CPT_ANNEXE_COL_3", isoLangue));
            parametres.put(CAResumeCptAnnexe_ParameterList.COL_4,
                    application.getLabel("IMPR_EXTR_CPT_ANNEXE_COL_4", isoLangue));
            parametres.put(CAResumeCptAnnexe_ParameterList.COL_5,
                    application.getLabel("IMPR_EXTR_CPT_ANNEXE_COL_5", isoLangue));
            parametres.put(CAResumeCptAnnexe_ParameterList.COL_6,
                    application.getLabel("IMPR_EXTR_CPT_ANNEXE_COL_6", isoLangue));
            parametres.put(CAResumeCptAnnexe_ParameterList.COL_7,
                    application.getLabel("IMPR_EXTR_CPT_ANNEXE_COL_7", isoLangue));
            parametres.put(CAResumeCptAnnexe_ParameterList.COL_8,
                    application.getLabel("IMPR_EXTR_CPT_ANNEXE_COL_8", isoLangue));
            parametres.put(CAResumeCptAnnexe_ParameterList.PARAM_PAGE,
                    application.getLabel("IMPR_EXTR_CPT_ANNEXE_PAGE", isoLangue));
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    /**
     * Ajoute les paramètes nécessaire au Header du document.
     * 
     * @param application
     * @param headerBean
     * @param isoLangue
     */
    private void prepareHeaderParameters(CAApplication application, CaisseHeaderReportBean headerBean, String isoLangue) {
        try {
            headerBean.setEmailCollaborateur(getSession().getUserEMail());
            headerBean.setNomCollaborateur(getSession().getUserFullName());
            headerBean.setTelCollaborateur(getSession().getUserInfo().getPhone());
            headerBean.setUser(getSession().getUserInfo());

            headerBean.setDate(JACalendar.format(getDocumentDate(), getPrintLanguage()));

            if (CAApplication.getApplicationOsiris().getCAParametres().isConfidentiel()) {
                headerBean.setConfidentiel(true);
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    private void prepareHeaderParametersForEbusiness(CAApplication application, CaisseHeaderReportBean headerBean,
            String isoLangue) {
        headerBean.setDate(JACalendar.format(getDocumentDate(), isoLangue));
        if (application.getCAParametres().isConfidentiel()) {
            headerBean.setConfidentiel(true);
        }
    }

    /**
     * Ajout du numéro d'affilié au header du document. Pour la caisse suisse, les informations satellites et consulat
     * sont rajoutés aux n° d'affilié.
     * 
     * @param ca
     * @param headerBean
     */
    private void prepareNoAffilie(CACompteAnnexe ca, CaisseHeaderReportBean headerBean) throws Exception {
        String tmpNumAffilie = ca.getIdExterneRole();

        headerBean.setNoAffilie(tmpNumAffilie);

    }

    /**
     * On prepare les parametres variable.
     * 
     * @param ca
     * @param application
     * @param soldeCumule
     */
    private void prepareVariableParameters(CACompteAnnexe ca, CAApplication application, FWCurrency soldeCumule) {
        String dateToPrint;

        if (JadeStringUtil.isBlank(getUntilDate())
                || getForSelectionTri().equals(CAExtraitCompteManager.ORDER_BY_IDSECTION)) {
            dateToPrint = JACalendar.format(getDocumentDate(), getPrintLanguage());
        } else {
            dateToPrint = JACalendar.format(getUntilDate(), getPrintLanguage());
        }

        if ((getFromDate() != null) && (getFromDate().trim().length() > 0)
                && !getForSelectionTri().equals(CAExtraitCompteManager.ORDER_BY_IDSECTION)) {
            parametres.put(CAResumeCptAnnexe_ParameterList.PARAM_5, FWMessageFormat.format(
                    application.getLabel("IMPR_EXTR_CPT_ANNEXE_CPT_DU_AU", getPrintLanguage()),
                    JACalendar.format(getFromDate(), getPrintLanguage()), dateToPrint));
        } else {
            parametres.put(CAResumeCptAnnexe_ParameterList.PARAM_5,
                    application.getLabel("IMPR_EXTR_CPT_ANNEXE_CPT_AU", getPrintLanguage()) + " " + dateToPrint);
        }

        if ((soldeCumule != null) && (soldeCumule.doubleValue() > 0)) {
            parametres.put(CAResumeCptAnnexe_ParameterList.PARAM_6, FWMessageFormat.format(application.getLabel(
                    CAProcessImpressionExtraitCompteAnnexe.LABEL_IMPR_EXTR_CPT_ANNEXE_MONTANT_NOTRE_FAVEUR,
                    getPrintLanguage()), dateToPrint));
        } else if ((soldeCumule != null) && (soldeCumule.doubleValue() < 0)) {
            parametres.put(CAResumeCptAnnexe_ParameterList.PARAM_6, FWMessageFormat.format(application.getLabel(
                    CAProcessImpressionExtraitCompteAnnexe.LABEL_IMPR_EXTR_CPT_ANNEXE_MONTANT_VOTRE_FAVEUR,
                    getPrintLanguage()), dateToPrint));
        }
    }

    private void registerFileExtraitCompteExcelml(String docPath) throws Exception {

        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(CAApplication.DEFAULT_APPLICATION_OSIRIS);
        docInfoExcel.setDocumentTitle(CAProcessImpressionExtraitCompteAnnexe.EXCELML_OUTPUT_FILE_NAME);
        docInfoExcel.setDocumentTypeNumber(CAProcessImpressionExtraitCompteAnnexe.EXCELML_MODEL_NUMERO_INFOROM);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        this.registerAttachedDocument(docInfoExcel, docPath);
    }

    /**
     * Défini les paramètres de recherches pour CACompteAnnexeManager. CACompteAnnexeManager sert à charger tous les
     * comptes que buildDocument doit travailler.
     * 
     * @param caMgr
     */
    private void setCompteAnnexeManagerParameters(CACompteAnnexeManager caMgr) {
        caMgr.setSession(getSession());
        caMgr.setForSelectionRole(getForSelectionRole());

        if ((getFromNoAffilie() != null) && (getFromNoAffilie().trim().length() > 0)) {
            caMgr.setFromIdExterneRole(getFromNoAffilie());
        }

        if ((getUntilNoAffilie() != null) && (getUntilNoAffilie().trim().length() > 0)) {
            caMgr.setUntilIdExterneRole(getUntilNoAffilie());
        }

        // Ordonné par idExterneRole
        caMgr.setOrderBy("1");

        if (this instanceof CAListExtraitCompteAnnexeViewBean) {
            // on prend tout les soldes
            caMgr.setForSelectionCompte("");
        } else {
            // on ne prend pas les soldes == 0
            caMgr.setForSelectionCompte("1");
        }

        caMgr.setForIdGenreCompte(getForIdGenreCompte());
        caMgr.setForIdCategorie(getForIdCategorie());

        if (!JadeStringUtil.isBlank(getForTriSpecial())) {
            caMgr.setForTriSpecial(getForTriSpecial());
        }

        if (!JadeStringUtil.isBlank(getFromNoSatConsul())) {
            caMgr.setFromNoSatConsul(getFromNoSatConsul());
        }

        if (!JadeStringUtil.isBlank(getUntilNoSatConsul())) {
            caMgr.setUntilNoSatConsul(getUntilNoSatConsul());
        }
    }

    public void setContainerExtraitCompteExcelml(CommonExcelmlContainer containerExtraitCompteExcelml) {
        this.containerExtraitCompteExcelml = containerExtraitCompteExcelml;
    }

    /**
     * @param descriptionAffilie
     *            the descriptionAffilie to set
     */
    public void setDescriptionAffilie(String descriptionAffilie) {
        this.descriptionAffilie = descriptionAffilie;
    }

    /**
     * Sets the documentDate.
     * 
     * @param documentDate
     *            The documentDate to set
     */
    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    /**
     * Défini les paramètres de recherches pour ExtraitCompteListViewBean. ExtraitCompteListViewBean sert à charger
     * toutes les entrées de l'extrait d'un compte.
     * 
     * @param ca
     * @param ds
     */
    private void setExtraitCompteListViewBeanParameters(CACompteAnnexe ca, CAExtraitCompteListViewBean ds) {
        ds.setSession(getSession());

        // Paramétrage de la source
        ds.setForIdTypeOperation(getForIdTypeOperation());
        ds.setForSelectionSections(getForSelectionSections());

        // Tri par date ou par section
        if ((!JadeStringUtil.isBlank(getForSelectionTri()))
                && (getForSelectionTri().equals(CAExtraitCompteManager.ORDER_BY_IDSECTION))) {
            ds.setForSelectionTri(CAExtraitCompteManager.ORDER_BY_IDSECTION);
        } else if ((!JadeStringUtil.isBlank(getForSelectionTri()))
                && (getForSelectionTri().equals(CAExtraitCompteManager.ORDER_BY_DATE_VALEUR))) {
            ds.setForSelectionTri(CAExtraitCompteManager.ORDER_BY_DATE_VALEUR);
        } else {
            ds.setForSelectionTri(CAExtraitCompteManager.ORDER_BY_DATE_COMPTABLE);
        }

        if ((getForSelectionTri().equals(CAExtraitCompteManager.ORDER_BY_DATE_COMPTABLE))
                || (getForSelectionTri().equals(CAExtraitCompteManager.ORDER_BY_DATE_VALEUR))) {
            if ((!JadeStringUtil.isBlank(getFromDate()))) {
                ds.setFromPositionnement(getFromDate());
            }

            if ((!JadeStringUtil.isBlank(getUntilDate()))) {
                ds.setUntilDate(getUntilDate());
            }
        }

        if ((!JadeStringUtil.isBlank(getFromSection()))
                && (getForSelectionTri().equals(CAExtraitCompteManager.ORDER_BY_IDSECTION))) {
            ds.setFromPositionnement(getFromSection());
        }

        if ((!JadeStringUtil.isBlank(getUntilSection()))
                && (getForSelectionTri().equals(CAExtraitCompteManager.ORDER_BY_IDSECTION))) {
            ds.setUntilSection(getUntilSection());
        }

        ds.setIdCompteAnnexe(ca.getIdCompteAnnexe());
        ds.setPrintLanguage(getPrintLanguage());

        ds.setModeScreen(false);
    }

    /**
     * @param string
     */
    public void setForIdCategorie(String s) {
        forIdCategorie = s;
    }

    /**
     * @param string
     */
    public void setForIdGenreCompte(String s) {
        forIdGenreCompte = s;
    }

    /**
     * Sets the forIdTypeOperation.
     * 
     * @param forIdTypeOperation
     *            The forIdTypeOperation to set
     */
    public void setForIdTypeOperation(String forIdTypeOperation) {
        this.forIdTypeOperation = forIdTypeOperation;
    }

    public void setForSelectionRole(String string) {
        forSelectionRole = string;
    }

    /**
     * Sets the forSelectionSections.
     * 
     * @param forSelectionSections
     *            The forSelectionSections to set
     */
    public void setForSelectionSections(String forSelectionSections) {
        this.forSelectionSections = forSelectionSections;
    }

    /**
     * @param string
     */
    public void setForSelectionTri(String string) {
        forSelectionTri = string;
    }

    /**
     * @param string
     */
    public void setForTriSpecial(String string) {
        forTriSpecial = string;
    }

    /**
     * Sets the fromDate.
     * 
     * @param fromDate
     *            The fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Sets the fromNoAffilie.
     * 
     * @param fromNoAffilie
     *            The fromNoAffilie to set
     */
    public void setFromNoAffilie(String fromNoAffilie) {
        this.fromNoAffilie = fromNoAffilie;
    }

    /**
     * @param string
     */
    public void setFromNoSatConsul(String string) {
        fromNoSatConsul = string;
    }

    /**
     * @param string
     */
    public void setFromSection(String string) {
        fromSection = string;
    }

    public void setImprimerExtraitCompteExcelml(Boolean imprimerExtraitCompteExcelml) {
        this.imprimerExtraitCompteExcelml = imprimerExtraitCompteExcelml;
    }

    /**
     * @param string
     */
    public void setPrintLanguage(String string) {
        printLanguage = string;
    }

    /**
     * @param string
     */
    public void setPrintLanguageFromScreen(String string) {
        printLanguageFromScreen = string;
    }

    /**
     * @param string
     */
    public void setUntilDate(String s) {
        untilDate = s;
    }

    /**
     * Sets the untilNoAffilie.
     * 
     * @param untilNoAffilie
     *            The untilNoAffilie to set
     */
    public void setUntilNoAffilie(String untilNoAffilie) {
        this.untilNoAffilie = untilNoAffilie;
    }

    /**
     * @param string
     */
    public void setUntilNoSatConsul(String string) {
        untilNoSatConsul = string;
    }

    /**
     * @param string
     */
    public void setUntilSection(String string) {
        untilSection = string;
    }

    public byte[] getByteFile() {
        return byteFile;
    }

    public void setByteFile(byte[] byteFile) {
        this.byteFile = byteFile;
    }

    public String getForIdCompteAnnexe() {
        return forIdCompteAnnexe;
    }

    public void setForIdCompteAnnexe(String forIdCompteAnnexe) {
        this.forIdCompteAnnexe = forIdCompteAnnexe;
    }

    public boolean isEbusinessMode() {
        return ebusinessMode;
    }

    public void setEbusinessMode(boolean ebusinessMode) {
        this.ebusinessMode = ebusinessMode;
    }

    public String getDocLocation() {
        return docLocation;
    }

    public void setDocLocation(String docLocation) {
        this.docLocation = docLocation;
    }
}
