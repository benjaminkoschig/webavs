/**
 * class CPProcessListeDecisionsAnneeEnDouble écrit le 19/01/05 par JPA
 * 
 * class process qui lance la création du document xls (CPImpressionAnneeEnDouble)
 * 
 * @author JPA
 **/
package globaz.phenix.process.listes;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.db.principale.CPDecisionAnneeMultipleManager;
import globaz.phenix.listes.excel.CPListeDecisionsAnneesMultiples;

public class CPProcessListeDecisionsAnneesMultiples extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPassage = "";

    public CPProcessListeDecisionsAnneesMultiples() {
        super();
    }

    public CPProcessListeDecisionsAnneesMultiples(BProcess parent) {
        super(parent);
    }

    public CPProcessListeDecisionsAnneesMultiples(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            CPDecisionAnneeMultipleManager manager = new CPDecisionAnneeMultipleManager();
            manager.setSession(getSession());
            manager.setForIdPassage(getIdPassage());
            // manager.find(getTransaction());
            // if (!manager.isEmpty()) {
            // Création du document
            CPListeDecisionsAnneesMultiples excelDoc = new CPListeDecisionsAnneesMultiples(getSession(), getIdPassage());
            excelDoc.setProcessAppelant(this);
            excelDoc.populateSheet(manager, getTransaction(), getIdPassage());
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentType("0084CCP");
            docInfo.setDocumentTypeNumber("");
            this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
            // }
            return true;
        } catch (Exception e) {
            getSession().addError(getSession().getLabel("ERREUR_PASSAGE"));
            getTransaction().addErrors(e.getMessage());
            return false;
        }
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    @Override
    protected void _validate() throws Exception {
        // Contrôle du mail
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
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
        String passageName = "";
        if (JadeStringUtil.isIntegerEmpty(getIdPassage())) {
            passageName = getSession().getLabel("TOUS");
        } else {
            passageName = getIdPassage();
        }
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("SUJET_EMAIL_PASOK_LISTE_DECISION_A_CONTROLER") + " Job:" + passageName;
        } else {
            return getSession().getLabel("SUJET_EMAIL_OK_LISTE_DECISION_A_CONTROLER") + "  Job :" + passageName;
        }
    }

    public String getIdPassage() {
        return idPassage;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setIdPassage(String string) {
        idPassage = string;
    }
}
