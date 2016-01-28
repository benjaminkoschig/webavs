package globaz.phenix.process.listes;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionListeDecisionsManquantesManager;
import globaz.phenix.listes.excel.CPListeDecisionsManquantes;

public class CPProcessListeDecisionsManquantes extends BProcess {

    private static final long serialVersionUID = 1L;
    private String fromAnneeDecision = null;
    private String toAnneeDecision = null;
    private Boolean inclureEtatCalcul = Boolean.FALSE;

    public CPProcessListeDecisionsManquantes() {
        super();
    }

    public CPProcessListeDecisionsManquantes(BProcess parent) {
        super(parent);
    }

    public CPProcessListeDecisionsManquantes(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CPDecisionListeDecisionsManquantesManager manager = new CPDecisionListeDecisionsManquantesManager();
            manager.setSession(getSession());
            manager.setFromAnneeDecision(fromAnneeDecision);
            manager.setToAnneeDecision(toAnneeDecision);
            if (getInclureEtatCalcul()) {
                manager.setForExceptEtatDecision(CPDecision.CS_CALCUL);
            }

            // Création du document
            CPListeDecisionsManquantes excelDoc = new CPListeDecisionsManquantes(getSession(), getFromAnneeDecision(),
                    getToAnneeDecision());
            excelDoc.setSession(getSession());
            excelDoc.setProcessAppelant(this);
            excelDoc.populateSheet(manager, getTransaction());
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentType("0086CCP");
            docInfo.setDocumentTypeNumber("");
            this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
            return true;
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return false;
        }
    }

    @Override
    protected void _validate() throws Exception {
        // Contrôle du mail
        if (JadeStringUtil.isBlank(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        if (JadeStringUtil.isIntegerEmpty(getFromAnneeDecision())
                || JadeStringUtil.isIntegerEmpty(getToAnneeDecision())) {
            getSession().addError(getSession().getLabel("PERIODE_OBLIGATOIRE"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        if (getSession().hasErrors()) {
            abort();
        }
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("SUJET_EMAIL_PASOK_LISTE_DECISIONS_MANQUANTES");
        } else {
            return getSession().getLabel("SUJET_EMAIL_OK_LISTE_DECISIONS_MANQUANTES");
        }
    }

    /**
     * Returns the fromAnneeDecision.
     * 
     * @return String
     */
    public String getFromAnneeDecision() {
        return fromAnneeDecision;
    }

    /**
     * Returns the toAnneeDecision.
     * 
     * @return String
     */
    public String getToAnneeDecision() {
        return toAnneeDecision;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * Sets the fromAnneeDecision.
     * 
     * @param fromAnneeDecision
     *            The fromAnneeDecision to set
     */
    public void setFromAnneeDecision(String fromAnneeDecision) {
        this.fromAnneeDecision = fromAnneeDecision;
    }

    /**
     * Sets the toAnneeDecision.
     * 
     * @param toAnneeDecision
     *            The toAnneeDecision to set
     */
    public void setToAnneeDecision(String toAnneeDecision) {
        this.toAnneeDecision = toAnneeDecision;
    }

    /**
     * Returns inclureEtatCalcul
     * 
     * @return
     */
    public Boolean getInclureEtatCalcul() {
        return inclureEtatCalcul;
    }

    /**
     * Sets inclureEtatCalcul.
     * 
     * @param inclureEtatCalcul
     */
    public void setInclureEtatCalcul(Boolean inclureEtatCalcul) {
        this.inclureEtatCalcul = inclureEtatCalcul;
    }
}
