package globaz.cygnus.process;

import globaz.cygnus.services.RFSetEtatProcessService;
import globaz.cygnus.services.validerDecision.RFDecisionDocumentData;
import globaz.cygnus.services.validerDecision.RFValiderDecisionServiceFactory;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.ArrayList;
import java.util.Date;
import ch.globaz.topaz.datajuicer.DocumentData;

public class RFValiderDecisionAvasadProcess extends AbstractJadeJob {

    private String dateSurDocument = "";
    private DocumentData docData = new DocumentData();
    private String emailAdress = "";
    private String idExecutionProcess = "";
    private String idGestionnaire = "";
    private Boolean isMiseEnGed = Boolean.FALSE;
    private FWMemoryLog memoryLog = new FWMemoryLog();
    private StringBuffer pdfDecisionURL = new StringBuffer("null");

    /**
     * Crée une nouvelle instance de la classe RFValiderDecisionsProcess.
     */
    public RFValiderDecisionAvasadProcess() {
        super();
    }

    private JadePublishDocumentInfo createDocInfoDecompte() {

        JadePublishDocumentInfo documentInfo = JadePublishDocumentInfoProvider.newInstance(this);
        documentInfo.setOwnerEmail(getEmailAdress());
        documentInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, getEmailAdress());
        documentInfo.setDocumentTitle(getSession().getLabel("PROCESS_SIMULER_VALIDATION_DECISION"));
        documentInfo.setArchiveDocument(false);
        documentInfo.setPublishDocument(false);
        documentInfo.setDocumentSubject(getSession().getLabel("PROCESS_SIMULER_VALIDATION_DECISION"));

        return documentInfo;
    }

    public String getDateSurDocument() {
        return dateSurDocument;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public DocumentData getDocData() {
        return docData;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public String getIdExecutionProcess() {
        return idExecutionProcess;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public Boolean getIsMiseEnGed() {
        return isMiseEnGed;
    }

    public FWMemoryLog getMemoryLog() {
        return memoryLog;
    }

    @Override
    public String getName() {
        return null;
    }

    public String getObjetMail() {

        if (getMemoryLog().hasErrors()) {
            return getSession().getLabel("RF_IMPORT_AVASAD_PROCESS_VALIDATION_DECISION_DOCUMENT_FAILED");
        } else {
            return getSession().getLabel("RF_IMPORT_AVASAD_PROCESS_VALIDATION_DECISION_DOCUMENT_SUCCESS");
        }
    }

    public StringBuffer getPdfDecisionURL() {
        return pdfDecisionURL;
    }

    /*
     * private void initTransaction() throws Exception { BITransaction transaction = new
     * BTransaction(this.getSession()); transaction.openTransaction(); this.setTransaction((BTransaction) transaction);
     * }
     */

    public void initTransaction() throws Exception {
        BITransaction newtransaction = getSession().newTransaction();
        newtransaction.openTransaction();

        setTransaction((BTransaction) newtransaction);
    }

    @Override
    public void run() {

        try {

            getMemoryLog().setSession(getSession());

            initTransaction();

            RFSetEtatProcessService.setEtatProcessValiderDecision(true, getSession());

            ArrayList<RFDecisionDocumentData> decisionArray = new ArrayList<RFDecisionDocumentData>();

            // comme on ne peut pas donner de date sur document dans le processus, on récupère la date du jour.
            setDateSurDocument(JadeDateUtil.getGlobazFormattedDate(new Date()));

            // Récupération des décisions
            RFValiderDecisionServiceFactory.getRFValiderDecisionAvasadService().recupererDecisions(getIdGestionnaire(),
                    getIdExecutionProcess(), getSession(), decisionArray);

            // Validation des décisions
            RFValiderDecisionServiceFactory.getRFValiderDecisionAvasadService().miseAJourEntites(decisionArray,
                    getDateSurDocument(), false, getIdGestionnaire(), getSession(), getTransaction());

        } catch (Exception e1) {
            RFValiderDecisionServiceFactory.getRFValiderDecisionAvasadService().logErreurs(getEmailAdress(),
                    getMemoryLog(), e1, this, getSession(), getTransaction());

        } finally {

            RFValiderDecisionServiceFactory.getRFValiderDecisionAvasadService().commitTransaction(getTransaction(),
                    false);
            try {
                RFSetEtatProcessService.setEtatProcessValiderDecision(false, getSession());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void setDateSurDocument(String dateSurDocument) {
        this.dateSurDocument = dateSurDocument;
    }

    public void setDocData(DocumentData docData) {
        this.docData = docData;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public void setIdExecutionProcess(String idExecutionProcess) {
        this.idExecutionProcess = idExecutionProcess;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIsMiseEnGed(Boolean isMiseEnGed) {
        this.isMiseEnGed = isMiseEnGed;
    }

    public void setMemoryLog(FWMemoryLog memoryLog) {
        this.memoryLog = memoryLog;
    }

    public void setPdfDecisionURL(StringBuffer pdfDecisionURL) {
        this.pdfDecisionURL = pdfDecisionURL;
    }

}
