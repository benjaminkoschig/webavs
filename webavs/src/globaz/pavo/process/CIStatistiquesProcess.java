package globaz.pavo.process;

import globaz.draco.application.DSApplication;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import ch.horizon.jaspe.util.JACalendarGregorian;

public class CIStatistiquesProcess extends BProcess {

    private static final long serialVersionUID = 4690927992198358727L;
    protected final static String MSG_EMAIL_SUBJECT_ERROR = "8002";
    protected final static String MSG_EMAIL_SUBJECT_OK = "8001";
    private final static String NUM_INFOROM = "0160CCI";
    private final static String TITLE = "0160CCI";
    private String anneeDebut = "";

    private String anneeFin = "";

    /**
     * Constructor for CIStatistiquesProcess.
     */
    public CIStatistiquesProcess() {
        super();
    }

    /**
     * Constructor for CIStatistiquesProcess.
     * 
     * @param parent
     */
    public CIStatistiquesProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Constructor for CIStatistiquesProcess.
     * 
     * @param session
     */
    public CIStatistiquesProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        CIStatDocument excelDoc = new CIStatDocument("Stat 1", "Stat2", anneeDebut, anneeFin, getSession());
        excelDoc.setProcess(this);
        excelDoc.populateSheet(getSession());
        excelDoc.populateSheet2(getSession());

        try {
            excelDoc.createSheet3(getSession());
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, CIStatDocument.class.getName());
        }
        JadePublishDocumentInfo docInfoExcel = createDocumentInfo();
        docInfoExcel.setApplicationDomain(DSApplication.DEFAULT_APPLICATION_DRACO);
        docInfoExcel.setDocumentTitle(CIStatistiquesProcess.TITLE);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        docInfoExcel.setDocumentTypeNumber(CIStatistiquesProcess.NUM_INFOROM);
        this.registerAttachedDocument(docInfoExcel, excelDoc.getOutputFile());
        // this.registerAttachedDocument(excelDoc.getDocumentInfo(), excelDoc.getOutputFile());

        return !isOnError();

    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        if (JadeStringUtil.isEmpty(anneeDebut) || !(new JACalendarGregorian().isValid(anneeDebut))) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_DATE_DEBUT"));
            abort();
            return;
        }

        if (JadeStringUtil.isEmpty(anneeFin) || !(new JACalendarGregorian().isValid(anneeFin))) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_DATE_FIN"));
            abort();
            return;
        }

        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getTransaction(), getSession().getLabel("ERREUR_EMAIL_OBLIGATOIRE"));
            setSendCompletionMail(false);
            setSendMailOnError(false);
            abort();
            return;
        }

    }

    /**
     * Returns the anneeDebut.
     * 
     * @return String
     */
    public String getAnneeDebut() {
        return anneeDebut;
    }

    /**
     * Returns the anneeFin.
     * 
     * @return String
     */
    public String getAnneeFin() {
        return anneeFin;
    }

    @Override
    protected String getEMailObject() {
        String obj = "";
        if (getSession().hasErrors()) {
            obj = getSession().getLabel("MSG_IMPORT_STAT_ECHEC");
        } else {
            obj = getSession().getLabel("MSG_IMPORT_STAT");
        }
        return obj;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * Sets the anneeDebut.
     * 
     * @param anneeDebut
     *            The anneeDebut to set
     */
    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    /**
     * Sets the anneeFin.
     * 
     * @param anneeFin
     *            The anneeFin to set
     */
    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }

}
