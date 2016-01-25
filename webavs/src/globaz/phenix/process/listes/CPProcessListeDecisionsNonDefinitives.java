/**
 * class CPProcessListeDecisionNonDefinitive écrit le 19/01/05 par JPA
 * 
 * class process qui lance la création du document xls (CPImpressionDecisionAvecMiseEnCompte)
 * 
 * @author JPA
 **/
package globaz.phenix.process.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.db.principale.CPDecisionNonDefinitivesManager;
import globaz.phenix.listes.excel.CPListeDecisionsNonDefinitives;

public class CPProcessListeDecisionsNonDefinitives extends BProcess implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Décration
    private String anneeDecision = "";

    // Constructeurs
    public CPProcessListeDecisionsNonDefinitives() {
        super();
    }

    public CPProcessListeDecisionsNonDefinitives(BProcess parent) {
        super(parent);
    }

    public CPProcessListeDecisionsNonDefinitives(globaz.globall.db.BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CPDecisionNonDefinitivesManager manager = new CPDecisionNonDefinitivesManager();
            manager.setSession(getSession());
            manager.setForAnnee(getAnneeDecision());
            // manager.find();
            // Création du document
            CPListeDecisionsNonDefinitives excelDoc = new CPListeDecisionsNonDefinitives(getSession(),
                    getAnneeDecision());
            excelDoc.setProcessAppelant(this);
            excelDoc.populateSheet(manager, getTransaction());
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentType("0087CCP");
            docInfo.setDocumentTypeNumber("");
            this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Validate
    @Override
    protected void _validate() throws Exception {
        // Contrôle du mail
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        // Contrôle de l'année de décision
        if (JadeStringUtil.isEmpty(getAnneeDecision())) {
            getSession().addError(getSession().getLabel("DECISION_INVALIDE"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        if (getSession().hasErrors()) {
            abort();
        }
    }

    public String getAnneeDecision() {
        return anneeDecision;
    }

    @Override
    protected String getEMailObject() {
        if ((isAborted() || getSession().hasErrors())) {
            return getSession().getLabel("SUJET_EMAIL_PASOK_NONDEF");
        } else {
            return getSession().getLabel("SUJET_EMAIL_OK_NONDEF");
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setAnneeDecision(String string) {
        anneeDecision = string;
    }
}
