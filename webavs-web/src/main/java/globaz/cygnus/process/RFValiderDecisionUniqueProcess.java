package globaz.cygnus.process;

import globaz.cygnus.services.RFSetEtatProcessService;
import globaz.cygnus.services.validerDecision.RFDecisionDocumentData;
import globaz.cygnus.services.validerDecision.RFValiderDecisionServiceFactory;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BTransaction;
import globaz.jade.job.AbstractJadeJob;
import java.util.ArrayList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * 
 * @author jje
 * 
 */
public class RFValiderDecisionUniqueProcess extends AbstractJadeJob {

    private static final long serialVersionUID = 1L;
    private String dateSurDocument = "";
    private DocumentData docData = new DocumentData();
    private String emailAdresse = "";
    private String idDecision = "";
    private String idGestionnaire = "";
    private Boolean isMiseEnGed = Boolean.FALSE;
    private FWMemoryLog memoryLog = new FWMemoryLog();
    private String numeroDecision = "";
    private StringBuffer pdfDecisionURL = new StringBuffer("null");

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

    public String getEmailAdresse() {
        return emailAdresse;
    }

    public String getIdDecision() {
        return idDecision;
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

    public String getNumeroDecision() {
        return numeroDecision;
    }

    public StringBuffer getPdfDecisionURL() {
        return pdfDecisionURL;
    }

    private void initTransaction() throws Exception {
        BITransaction transaction = new BTransaction(getSession());
        transaction.openTransaction();
        setTransaction((BTransaction) transaction);
    }

    @Override
    public void run() {

        try {

            initTransaction();
            RFSetEtatProcessService.setEtatProcessValiderDecision(true, getSession());

            ArrayList<RFDecisionDocumentData> decisionArray = new ArrayList<RFDecisionDocumentData>();

            this.createDocuments(RFValiderDecisionServiceFactory.getRFValiderDecisionUniqueService()
                    .genererDecisionDocument(getDateSurDocument(), getDocData(), getEmailAdresse(),
                            getIdGestionnaire(), getIdDecision(), getIsMiseEnGed(), false, getMemoryLog(),
                            getPdfDecisionURL(), decisionArray, this, getSession(), getTransaction()));

        } catch (Exception e1) {
            RFValiderDecisionServiceFactory.getRFValiderDecisionUniqueService().logErreurs(getEmailAdresse(),
                    getMemoryLog(), e1, this, getSession(), getTransaction());

        } finally {
            RFValiderDecisionServiceFactory.getRFValiderDecisionUniqueService().commitTransaction(getTransaction(),
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

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
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

    public void setNumeroDecision(String numeroDecision) {
        this.numeroDecision = numeroDecision;
    }

    public void setPdfDecisionURL(StringBuffer pdfDecisionURL) {
        this.pdfDecisionURL = pdfDecisionURL;
    }

}
