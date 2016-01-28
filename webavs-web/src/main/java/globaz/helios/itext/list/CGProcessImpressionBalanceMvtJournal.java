package globaz.helios.itext.list;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.helios.application.CGApplication;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.itext.list.balancemvt.CGBalMvtJournal_DS;
import globaz.helios.itext.list.balancemvt.CGBalMvtJournal_ParameterList;
import globaz.helios.itext.list.utils.CGGeneric_BeanComparator;
import globaz.helios.itext.list.utils.CGImpressionUtils;
import globaz.helios.tools.CGXLSContructor;
import globaz.helios.tools.TimeHelper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Insert the type's description here. Creation date: (04.07.2003 08:46:30)
 * 
 * @author: Administrator
 */
public class CGProcessImpressionBalanceMvtJournal extends FWIDocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String NUMERO_REFERENCE_INFOROM = "0049GCF";

    // Construction de la source
    private CGBalMvtJournal_DS ds = new CGBalMvtJournal_DS(getSession());
    private java.lang.String idExerciceComptable;
    private boolean isFirst = true;
    private List listBeanDocument = new ArrayList();
    private String typeImpression = "pdf";

    public CGProcessImpressionBalanceMvtJournal() throws Exception {
        this(new BSession(CGApplication.DEFAULT_APPLICATION_HELIOS));
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public CGProcessImpressionBalanceMvtJournal(BProcess parent, String arg1, String arg2) throws FWIException {
        super(parent, arg1, arg2);
        super.setDocumentTitle(parent.getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_TITLE"));
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws globaz.framework.printing.itext.exception.FWIException
     */
    public CGProcessImpressionBalanceMvtJournal(BSession session, String arg1, String arg2) throws FWIException {
        super(session, arg1, arg2);
        super.setDocumentTitle(session.getLabel("IMPRESSION_BAL_MVT_JOURNAL_TITLE"));
    }

    /**
     * CGBalMvtJournal_Doc constructor comment.
     * 
     * @param session
     *            globaz.globall.db.BSession
     */
    public CGProcessImpressionBalanceMvtJournal(globaz.globall.db.BSession session) throws FWIException {
        this(session, CGApplication.APPLICATION_HELIOS_REP, "BalanceMvtJournal");

    }

    @Override
    protected void _validate() throws Exception {

        if (JadeStringUtil.isBlank(idExerciceComptable)) {
            throw new Exception(getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_ERR_1"));
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
     * @see FWIDocumentManager#afterBuildReport()
     */
    @Override
    public void afterBuildReport() {
        super.afterBuildReport();
        getDocumentInfo().setDocumentTypeNumber(CGProcessImpressionBalanceMvtJournal.NUMERO_REFERENCE_INFOROM);

        if (isTypeImpressionXls()) {
            CGXLSContructor xlsc = new CGXLSContructor();
            try {
                listBeanDocument = ds.getListBeans();

                // On applique un tri en fonction de la clé de tri des objets de la map
                Collections.sort(listBeanDocument, new CGGeneric_BeanComparator());

                xlsc.setDocumentFileName("BalMvtJournal");
                xlsc.setSession(getSession());
                xlsc.process(listBeanDocument, super.getImporter().getParametre());
                JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
                docInfoExcel.setApplicationDomain(CGApplication.DEFAULT_APPLICATION_HELIOS);
                docInfoExcel.setDocumentTitle(xlsc.getDocumentFileName());
                docInfoExcel.setPublishDocument(true);
                docInfoExcel.setArchiveDocument(false);
                docInfoExcel.setDocumentTypeNumber(CGProcessImpressionBalanceMvtJournal.NUMERO_REFERENCE_INFOROM);
                this.registerAttachedDocument(docInfoExcel, xlsc.getExportPath());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("XLS Balance Mouvement Journal contruction processed");
            }
        }
    }

    /**
     * Dernière méthode lancé avant la création du document par JasperReport Dernier minute pour fournir le nom du
     * rapport à utiliser avec la méthode setTemplateFile(String) et si nécessaire le type de document à sortir avec la
     * méthode setFileType(String [PDF|CSV|HTML|XSL]) par défaut PDF Date de création : (25.02.2003 10:18:15)
     */

    @Override
    public void beforeBuildReport() {
        try {
            // Assignation des paramètres
            super.setParametres(CGBalMvtJournal_ParameterList.PARAM_BAL_MVT_JOURNAL_LABEL_COL1,
                    getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_COL_1"));
            super.setParametres(CGBalMvtJournal_ParameterList.PARAM_BAL_MVT_JOURNAL_LABEL_COL2,
                    getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_COL_2"));
            super.setParametres(CGBalMvtJournal_ParameterList.PARAM_BAL_MVT_JOURNAL_LABEL_COL3,
                    getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_COL_3"));
            super.setParametres(CGBalMvtJournal_ParameterList.PARAM_BAL_MVT_JOURNAL_LABEL_COL4,
                    getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_COL_4"));
            super.setParametres(CGBalMvtJournal_ParameterList.PARAM_BAL_MVT_JOURNAL_LABEL_COL5,
                    getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_COL_5"));
            super.setParametres(CGBalMvtJournal_ParameterList.PARAM_BAL_MVT_JOURNAL_LABEL_TOTAL,
                    getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_COL_TOTAL"));

            super.setParametres("P_DATE_TIME", TimeHelper.getCurrentTime());
            super.setParametres(FWIImportParametre.PARAM_TITLE,
                    getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_TITLE"));

            // Description exercice comptable
            globaz.helios.db.comptes.CGExerciceComptable exerComptable = new globaz.helios.db.comptes.CGExerciceComptable();
            exerComptable.setIdExerciceComptable(idExerciceComptable);
            exerComptable.setSession(getSession());
            exerComptable.retrieve();
            if (!exerComptable.isNew()) {
                super.setParametres(FWIImportParametre.PARAM_EXERCICE, exerComptable.getFullDescription());

                CGMandat mandat = new CGMandat();
                mandat.setIdMandat(exerComptable.getIdMandat());
                mandat.setSession(getSession());
                mandat.retrieve();
                if (!mandat.isNew()) {
                    super.setParametres(FWIImportParametre.PARAM_COMPANYNAME, mandat.getLibelle());
                }

            }

            // Assignation template
            setTemplateFile("cg_balance_mouvement_journal");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Première méthode appelé (sauf _validate()) avant le chargement des données par le processus On initialise le
     * manager principal définit dans le constructeur ou si on fournit un JRDataSource on le fournit aussi ici avec la
     * méthode setSource et setSubSource (setSubReport(true) si on a un sousRapport avec des valeurs non paramètres)
     */
    @Override
    public void beforeExecuteReport() {
    }

    @Override
    public boolean beforePrintDocument() {
        if (isTypeImpressionXls()) {
            return false;
        }

        return super.beforePrintDocument();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 11:53:46)
     * 
     * @param id
     *            java.lang.String
     */
    public void bindData(String idExerciceComptableVal) throws java.lang.Exception {
        // Paramètres de génération du rapport
        setIdExerciceComptable(idExerciceComptableVal);
        super._executeProcess();
    }

    /**
     * Methode appelé pour la création des valeurs pour le document 1) addRow (si nécessaire) 2) Appèle des méthodes
     * pour la création des paramètres
     */
    @Override
    public void createDataSource() throws Exception {

        // Paramétrage de la source
        ds.setForIdExerciceComptable(getIdExerciceComptable());
        this.setDataSource(ds);
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_EMAIL_ERROR");
        } else {
            return getSession().getLabel("IMPRESSION_BAL_MVT_JOURNAL_EMAIL_OK");
        }
    }

    /**
     * Insert the method's description here. Creation date: (04.07.2003 10:06:47)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdExerciceComptable() {
        return idExerciceComptable;
    }

    public String getTypeImpression() {
        return typeImpression;
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#next()
     */
    @Override
    public boolean next() throws FWIException {
        if (isFirst) {
            isFirst = false;
            return true;
        }
        return false;
    }

    /**
     * Insert the method's description here. Creation date: (04.07.2003 10:06:47)
     * 
     * @param newIdExerciceComptable
     *            java.lang.String
     */
    public void setIdExerciceComptable(java.lang.String newIdExerciceComptable) {
        idExerciceComptable = newIdExerciceComptable;
    }

    public void setTypeImpression(String typeImpression) {
        this.typeImpression = typeImpression;
    }

}
