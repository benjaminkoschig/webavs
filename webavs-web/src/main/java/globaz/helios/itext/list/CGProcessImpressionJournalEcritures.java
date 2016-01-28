package globaz.helios.itext.list;

import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.export.FWIExportManager;
import globaz.framework.printing.itext.fill.FWIImportManager;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.printing.itext.types.FWIDocumentType;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.helios.application.CGApplication;
import globaz.helios.db.comptes.CGAdvancedEcritureListViewBean;
import globaz.helios.db.comptes.CGAdvancedEcritureViewBean;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.itext.list.journal.ecritures.CGJournalEcritures_Bean;
import globaz.helios.itext.list.journal.ecritures.CGJournalEcritures_ParameterList;
import globaz.helios.tools.CGXLSContructor;
import globaz.helios.tools.TimeHelper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sylvain Crelier
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CGProcessImpressionJournalEcritures extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String NUMERO_REFERENCE_INFOROM = "0053GCF";

    public static final String TEMPLATE_DOC = "cg_list_journal_ecr";
    private CGExerciceComptable exerciceComptable = null;
    private FWIExportManager exportManager = new FWIExportManager();
    private String idExerciceComptable;
    private String idJournal;
    private String idMandat;
    private FWIImportManager importManager = new FWIImportManager();

    private CGJournal journal = null;
    private List listBeanJournalEcritures = new ArrayList();
    private CGMandat mandat = null;
    private Map parametres = new HashMap();

    private CGPeriodeComptable periodeComptable = null;

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     */
    public CGProcessImpressionJournalEcritures() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     * 
     * @param parent
     */
    public CGProcessImpressionJournalEcritures(BProcess parent) throws Exception {
        super(parent);
        init();
    }

    /**
     * Constructor for CGProcessImpressionReleveAVS.
     * 
     * @param session
     */
    public CGProcessImpressionJournalEcritures(BSession session) throws Exception {
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() {
        boolean status = true;

        try {
            // On prepare les parametres globaux
            prepareParameter();

            // Create datasource
            buildJournalEcritures();

            CGXLSContructor xlsc = new CGXLSContructor();
            try {
                xlsc.setDocumentFileName("ListJournalEcritures");
                xlsc.setSession(getSession());
                xlsc.process(listBeanJournalEcritures, parametres);
                JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
                docInfoExcel.setApplicationDomain(CGApplication.DEFAULT_APPLICATION_HELIOS);
                docInfoExcel.setDocumentTitle(xlsc.getDocumentFileName());
                docInfoExcel.setPublishDocument(true);
                docInfoExcel.setArchiveDocument(false);
                docInfoExcel.setDocumentTypeNumber(CGProcessImpressionJournalEcritures.NUMERO_REFERENCE_INFOROM);
                this.registerAttachedDocument(docInfoExcel, xlsc.getExportPath());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("XLS List Journal Ecriture contruction processed");
            }

            // Il y a des documents a imprimer
            if ((status = (importManager.size() > 0))) {
                exportManager.addAll(importManager.getList());
                exportManager.exportReport();
                if (isAborted()) {
                    return false;
                }
                JadePublishDocumentInfo documentInfo = createDocumentInfo();
                documentInfo.setDocumentTypeNumber(CGProcessImpressionJournalEcritures.NUMERO_REFERENCE_INFOROM);
                this.registerAttachedDocument(documentInfo, exportManager.getExportNewFilePath());
            } else {
                getMemoryLog().logMessage("IMPRESSION_JOURNAL_AUCUNE_ECR", null, FWMessage.FATAL,
                        this.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }
        return status;
    }

    @Override
    protected void _validate() throws Exception {

        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
            throw new Exception(getSession().getLabel("IMPRESSION_JOURNAL_ECR_ERR_2"));
        }
        if (!getSession().hasErrors()) {
            setControleTransaction(true);
            setSendCompletionMail(true);
        }

        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            getMemoryLog().logMessage(getSession().getLabel("IMPRESSION_JOURNAL_ECR_ERR_1"), FWMessage.FATAL,
                    this.getClass().getName());
            throw new Exception(getSession().getLabel("IMPRESSION_JOURNAL_ECR_ERR_1"));
        }

        if (getSession().hasErrors()) {
            abort();
        }

    }

    protected void buildJournalEcritures() {
        CGAdvancedEcritureListViewBean ds = null;

        BStatement statement = null;
        try {
            ds = new CGAdvancedEcritureListViewBean();
            ds.setSession(getSession());
            ds.changeManagerSize(BManager.SIZE_NOLIMIT);
            ds.setForIdExerciceComptable(getIdExerciceComptable());
            ds.setForIdMandat(getIdMandat());
            ds.setForIdJournal(getIdJournal());
            ds.setOrderBy(CGEcritureViewBean.FIELD_DATE);

            statement = ds.cursorOpen(getTransaction());

            CGAdvancedEcritureViewBean entity = null;
            while ((entity = (CGAdvancedEcritureViewBean) ds.cursorReadNext(statement)) != null) {

                createRow(CGJournalEcritures_Bean.class.getName(), entity, listBeanJournalEcritures);
            }
            if (listBeanJournalEcritures.size() == 0) {
                return;
            }

            importManager.clearParam();
            importManager.setDocumentName("ImpressionJournalDesEcritures");
            importManager.setParametre(parametres);
            importManager.setDocumentTemplate(CGProcessImpressionJournalEcritures.TEMPLATE_DOC);
            importManager.setBeanCollectionDataSource(listBeanJournalEcritures);
            importManager.createDocument();
        } catch (Exception e) {
            JadeLogger.fatal(this, e);
            getMemoryLog().logMessage(e.toString(), null, FWMessage.FATAL, this.getClass().getName());
        } finally {
            try {
                ds.cursorClose(statement);
            } catch (Exception e) {
                JadeLogger.fatal(this, e);
                getMemoryLog().logMessage(e.toString(), null, FWMessage.FATAL, this.getClass().getName());
            }
        }
    }

    protected void createRow(String className, BEntity entity, List list) {
        try {
            CGJournalEcritures_Bean bean = (CGJournalEcritures_Bean) Class.forName(className).newInstance();
            if (bean.prepareValue(entity, getMandat(), getPeriodeComptable(), getTransaction(), getSession())) {
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("IMPRESSION_JOURNAL_ECR_EMAIL_ERROR");
        } else {
            return getSession().getLabel("IMPRESSION_JOURNAL_ECR_EMAIL_OK");
        }
    }

    public CGExerciceComptable getExerciceComptable() {
        if (exerciceComptable == null) {
            try {
                exerciceComptable = getJournal().getExerciceComptable();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return exerciceComptable;
    }

    public String getExportManagerNewFilePath() {
        return exportManager.getExportNewFilePath();
    }

    /**
     * Returns the idExerciceComptable.
     * 
     * @return String
     */
    public String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    /**
     * Returns the idJournal.
     * 
     * @return String
     */
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * Insert the method's description here. Creation date: (03.07.2003 09:53:52)
     * 
     * @return String
     */
    public String getIdMandat() {
        return idMandat;
    }

    public CGJournal getJournal() {
        // Si pas déjà chargé
        if (journal == null) {
            try {
                journal = new CGJournal();
                journal.setSession(getSession());
                journal.setIdJournal(getIdJournal());
                journal.retrieve(getTransaction());
                if (journal.hasErrors()) {
                    getMemoryLog().logMessage(journal.getErrors().toString(), FWMessage.FATAL,
                            this.getClass().getName());
                    journal = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                journal = null;
            }
        }
        return journal;
    }

    public CGMandat getMandat() {
        if (mandat == null) {
            try {
                mandat = getJournal().getExerciceComptable().getMandat();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mandat;
    }

    public CGPeriodeComptable getPeriodeComptable() {
        if (periodeComptable == null) {
            try {
                periodeComptable = new CGPeriodeComptable();
                periodeComptable.setSession(getSession());
                periodeComptable.setIdPeriodeComptable(getJournal().getIdPeriodeComptable());
                periodeComptable.retrieve(getTransaction());
            } catch (Exception e) {
                e.printStackTrace();
                periodeComptable = null;
            }
        }
        return periodeComptable;

    }

    private void init() throws FWIException {
        // import
        importManager.setImportPath(CGApplication.APPLICATION_HELIOS_REP);
        // export
        exportManager.setExportApplicationRoot(CGApplication.APPLICATION_HELIOS_REP);
        exportManager.setExportFileName("ListJournalEcritures");
        exportManager.setExportFileType(FWIDocumentType.PDF);
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    private void prepareParameter() {
        try {

            // Header
            parametres.put(FWIImportParametre.PARAM_EXERCICE, getExerciceComptable().getPeriodeDeA());
            parametres.put(FWIImportParametre.PARAM_TITLE, getSession().getLabel("IMPRESSION_JOURNAL_ECR_TITLE"));
            parametres.put(FWIImportParametre.PARAM_COMPANYNAME, getMandat().getLibelle());

            parametres.put(CGJournalEcritures_ParameterList.LABEL_MODULE,
                    getSession().getLabel("IMPRESSION_JOURNAL_ECR_MODULE"));
            parametres.put(CGJournalEcritures_ParameterList.PARAM_MODULE, getSession().getLabel("HELIOS_MODULE"));

            parametres.put(CGJournalEcritures_ParameterList.LABEL_NUMERO,
                    getSession().getLabel("IMPRESSION_JOURNAL_ECR_NUMERO"));
            parametres.put(CGJournalEcritures_ParameterList.PARAM_NUMERO, getJournal().getNumero() + " - "
                    + getJournal().getIdJournal());
            parametres.put(CGJournalEcritures_ParameterList.LABEL_LIBELLE,
                    getSession().getLabel("IMPRESSION_JOURNAL_ECR_LIBELLE"));
            parametres.put(CGJournalEcritures_ParameterList.PARAM_LIBELLE, getJournal().getLibelle());
            parametres.put(CGJournalEcritures_ParameterList.LABEL_DATE_CREA,
                    getSession().getLabel("IMPRESSION_JOURNAL_ECR_DATE_CREA"));
            parametres.put(CGJournalEcritures_ParameterList.PARAM_DATE_CREA, getJournal().getDate());
            parametres.put(CGJournalEcritures_ParameterList.LABEL_DATE_VALEUR,
                    getSession().getLabel("IMPRESSION_JOURNAL_ECR_DATE_VALEUR"));
            parametres.put(CGJournalEcritures_ParameterList.PARAM_DATE_VALEUR, getJournal().getDateValeur());
            parametres.put(CGJournalEcritures_ParameterList.LABEL_ETAT,
                    getSession().getLabel("IMPRESSION_JOURNAL_ECR_ETAT"));
            parametres.put(CGJournalEcritures_ParameterList.PARAM_ETAT, getJournal().getEtatLibelle());

            parametres.put(CGJournalEcritures_ParameterList.LABEL_PROPRIETAIRE,
                    getSession().getLabel("IMPRESSION_JOURNAL_ECR_PROPRIETAIRE"));
            parametres.put(CGJournalEcritures_ParameterList.PARAM_PROPRIETAIRE, getJournal().getProprietaire());

            parametres.put("P_DATE_TIME", TimeHelper.getCurrentTime());

            CGPeriodeComptable periode = new CGPeriodeComptable();
            periode.setSession(getSession());
            periode.setIdPeriodeComptable(journal.getIdPeriodeComptable());

            try {
                periode.retrieve(getTransaction());
            } catch (Exception e) {
                e.printStackTrace();
                periode = null;
            }

            if ((periode != null) && !periode.isNew()) {
                parametres.put(CGJournalEcritures_ParameterList.LABEL_PERIODE, getSession().getLabel("GLOBAL_PERIODE"));
                parametres.put(CGJournalEcritures_ParameterList.PARAM_PERIODE, periode.getLibelle());
            }

            // COLUMN
            parametres.put(FWIImportParametre.getCol(1), getSession().getLabel("IMPRESSION_JOURNAL_ECR_COL_1"));
            parametres.put(FWIImportParametre.getCol(2), getSession().getLabel("IMPRESSION_JOURNAL_ECR_COL_2"));
            parametres.put(FWIImportParametre.getCol(3), getSession().getLabel("IMPRESSION_JOURNAL_ECR_COL_3"));
            parametres.put(FWIImportParametre.getCol(4), getSession().getLabel("IMPRESSION_JOURNAL_ECR_COL_4"));
            parametres.put(FWIImportParametre.getCol(5), getSession().getLabel("IMPRESSION_JOURNAL_ECR_COL_5"));
            parametres.put(FWIImportParametre.getCol(6), getSession().getLabel("IMPRESSION_JOURNAL_ECR_COL_6"));
            parametres.put(FWIImportParametre.getCol(7), getSession().getLabel("IMPRESSION_JOURNAL_ECR_COL_7"));
            parametres.put(FWIImportParametre.getCol(8), getSession().getLabel("IMPRESSION_JOURNAL_ECR_COL_8"));
            parametres.put(FWIImportParametre.getCol(9), getSession().getLabel("IMPRESSION_JOURNAL_ECR_COL_9"));
            parametres.put(FWIImportParametre.getCol(10), getSession().getLabel("IMPRESSION_JOURNAL_ECR_COL_10"));
            parametres.put(FWIImportParametre.getCol(11), getSession().getLabel("IMPRESSION_JOURNAL_ECR_COL_11"));
            parametres.put(FWIImportParametre.getCol(12), getSession().getLabel("IMPRESSION_JOURNAL_ECR_COL_12"));

            // SUMMARY
            parametres.put(CGJournalEcritures_ParameterList.LABEL_TOTAUX,
                    getSession().getLabel("IMPRESSION_JOURNAL_ECR_TOTAUX"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the idExerciceComptable.
     * 
     * @param idExerciceComptable
     *            The idExerciceComptable to set
     */
    public void setIdExerciceComptable(String idExerciceComptable) {
        this.idExerciceComptable = idExerciceComptable;
    }

    /**
     * Sets the idJournal.
     * 
     * @param idJournal
     *            The idJournal to set
     */
    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    /**
     * Insert the method's description here. Creation date: (03.07.2003 09:53:52)
     * 
     * @param newIdMandat
     *            String
     */
    public void setIdMandat(String newIdMandat) {
        idMandat = newIdMandat;
    }

}
