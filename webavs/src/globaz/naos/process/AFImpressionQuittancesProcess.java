package globaz.naos.process;

import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.beneficiairepc.AFImpressionQuittanceViewBean;
import globaz.naos.itext.beneficiairesPC.AFQuittanceRemboursement_Doc;
import globaz.naos.itext.beneficiairesPC.AFQuittance_Doc;

public class AFImpressionQuittancesProcess extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int A_RETOURNER = 3;
    private static final int CAS_EXISTANT = 1;
    private static final int NBRE_QUITTANCES_ANNUELLES = 12;
    private static final int NOUVEAU_CAS = 2;
    private AFImpressionQuittanceViewBean viewBean = null;

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            String nomDoc = getSession().getLabel("QUITTANCE_REMBOURSEMENT") + " " + viewBean.getNumAffilie();
            // On crée un doc info
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setPublishDocument(true);
            docInfo.setArchiveDocument(false);
            docInfo.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, viewBean.getAdresseEmail());
            docInfo.setDocumentTitle(nomDoc);

            setSendCompletionMail(true);
            setEMailAddress(viewBean.getAdresseEmail());

            // On crée les formulaires de remboursement
            AFQuittanceRemboursement_Doc quittance = new AFQuittanceRemboursement_Doc();
            quittance.setISession(getSession());
            quittance.setDeleteOnExit(false);
            quittance.setSendCompletionMail(true);
            quittance.setSendMailOnError(true);
            quittance.setIdDestinataire(viewBean.getIdTiers());
            quittance.setNumAffilie(viewBean.getNumAffilie());
            quittance.setParent(this);
            quittance.executeProcess();
            int nbreQuittances = 0;
            if (!JadeStringUtil.isEmpty(viewBean.getNbreQuittances())) {
                nbreQuittances = JadeStringUtil.parseInt(viewBean.getNbreQuittances(), 0);
            } else {
                nbreQuittances = AFImpressionQuittancesProcess.NBRE_QUITTANCES_ANNUELLES;
            }

            if (!quittance.isOnError()) {
                Object quittanceDoc = getAttachedDocuments().get(getAttachedDocuments().size() - 1);
                for (int i = 1; i < nbreQuittances; i++) {
                    getAttachedDocuments().add(quittanceDoc);
                }
                this.mergePDF(docInfo, nomDoc, true, 0, false, null);
            }

            // On imprime la lettre
            AFQuittance_Doc doc = new AFQuittance_Doc();
            doc.setISession(getSession());
            doc.setDeleteOnExit(false);
            doc.setIdDestinataire(viewBean.getIdTiers());
            doc.setNumAffilie(viewBean.getNumAffilie());
            // On regarde quel type de document, on doit produire
            if (viewBean.getCasExistant().booleanValue()) {
                doc.setTypeQuittance(AFImpressionQuittancesProcess.CAS_EXISTANT);
            } else if ((viewBean.getHeures().length() > 0) && (viewBean.getDateEvaluation().length() > 0)) {
                doc.setTypeQuittance(AFImpressionQuittancesProcess.NOUVEAU_CAS);
            } else if (viewBean.getNbreQuittances().length() > 0) {
                doc.setTypeQuittance(AFImpressionQuittancesProcess.A_RETOURNER);
            } else {
                doc.setTypeQuittance(AFImpressionQuittancesProcess.CAS_EXISTANT);
            }
            doc.setDateEvaluation(viewBean.getDateEvaluation());
            doc.setNombreHeures(String.valueOf(viewBean.getHeures()));
            doc.setNombreQuittances(viewBean.getNbreQuittances());
            doc.setUser(viewBean.getUser());
            doc.setSendCompletionMail(false);
            doc.setParent(this);
            doc.executeProcess();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("QUITTANCE_REMBOURSEMENT") + " - " + viewBean.getNumAffilie();
    }

    public AFImpressionQuittanceViewBean getViewBean() {
        return viewBean;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setViewBean(AFImpressionQuittanceViewBean _viewBean) {
        viewBean = _viewBean;
    }

}
