package globaz.naos.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.naos.db.beneficiairepc.AFQuittanceManager;
import globaz.naos.db.beneficiairepc.AFQuittanceViewBean;
import globaz.naos.itext.beneficiairesPC.AFBeneficiaireFacturationErreurs;

public class AFImpressionQuittancesErreursProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idJournalQuittance = "";

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        AFQuittanceManager manager = new AFQuittanceManager();
        manager.setSession(getSession());
        manager.setForIdJournalQuittance(getIdJournalQuittance());
        manager.setInEtat(AFQuittanceViewBean.ETAT_ERREUR_FACTU + ", " + AFQuittanceViewBean.ETAT_ERREUR_CI);
        manager.setOrder("MAQTIB");
        manager.changeManagerSize(BManager.SIZE_NOLIMIT);
        try {
            manager.find(getTransaction());
            // création du doc xls
            AFBeneficiaireFacturationErreurs excelDoc = new AFBeneficiaireFacturationErreurs(getSession());
            excelDoc.setProcessAppelant(this);
            excelDoc.populateSheet(manager, getTransaction());
            setSendCompletionMail(true);
            if (excelDoc != null) {
                JadePublishDocument publishDoc = new JadePublishDocument(excelDoc.getOutputFile(), createDocumentInfo());
                this.registerAttachedDocument(publishDoc);
            }
            publishDocuments();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "Liste des erreurs");
            return false;
        }
        return true;
    }

    @Override
    protected String getEMailObject() {
        return "facturation des quittances PCG";
    }

    public String getIdJournalQuittance() {
        return idJournalQuittance;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setIdJournalQuittance(String idJournalQuittance) {
        this.idJournalQuittance = idJournalQuittance;
    }

}
