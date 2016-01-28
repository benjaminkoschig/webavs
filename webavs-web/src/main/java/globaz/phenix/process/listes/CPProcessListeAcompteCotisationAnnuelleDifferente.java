/**
 * class CPProcessListeDecisionsAvecMiseEnCompte écrit le 19/01/05 par JPA
 * 
 * class process qui lance la création du document xls (CPImpressionDecisionAvecMiseEnCompte)
 * 
 * @author JPA
 **/
package globaz.phenix.process.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.phenix.db.principale.CPCotisationDifferenteManager;
import globaz.phenix.listes.excel.CPListeAcompteCotisationAnnuellleDifferente;

public class CPProcessListeAcompteCotisationAnnuelleDifferente extends BProcess implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPassage = "";

    public CPProcessListeAcompteCotisationAnnuelleDifferente() {
        super();
    }

    public CPProcessListeAcompteCotisationAnnuelleDifferente(BProcess parent) {
        super(parent);
    }

    public CPProcessListeAcompteCotisationAnnuelleDifferente(globaz.globall.db.BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            // CPCotisationDifferenteViewBean viewBean= new
            // CPCotisationDifferenteViewBean();
            CPCotisationDifferenteManager manager = new CPCotisationDifferenteManager();
            manager.setSession(getSession());
            // récupération du trimestre et de l'année, nécessaire pour la
            // requête du manager

            // Calcul du tri
            String trimestre = "";
            JADate date = JACalendar.today();
            int annee = date.getYear();
            int mois = date.getMonth();
            if ((mois >= 1) && (mois <= 6)) {
                trimestre = "01.01." + annee;
            } else if ((mois >= 7) && (mois <= 12)) {
                annee = annee + 1;
                trimestre = "01.01." + annee;
            }
            manager.setFromDateFinAffiliation(trimestre);
            manager.setForIdPassage(getIdPassage());
            manager.setForAnneeDecisionCP(Integer.toString(annee));
            manager.setForDecisionsValideesPassageComptabilise(Boolean.TRUE);
            manager.setForTestSurCoti("1");
            // Création du document
            CPListeAcompteCotisationAnnuellleDifferente excelDoc = new CPListeAcompteCotisationAnnuellleDifferente(
                    getSession());
            excelDoc.setProcessAppelant(this);
            excelDoc.setIdPassage(getIdPassage());
            excelDoc.populateSheet(manager, getTransaction());
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentType("0083CCP");
            docInfo.setDocumentTypeNumber("");
            this.registerAttachedDocument(docInfo, excelDoc.getOutputFile());
            return true;
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
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
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("CP_MSG_0145"));
        }
        // Contrôle du mail
        if (JadeStringUtil.isEmpty(getIdPassage())) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("NUMPASSAGE_INVALIDE"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        if (!JadeStringUtil.isEmpty(getMessage())) {
            abort();
        }
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("SUJET_EMAIL_PASOK_ACOMPTE_DIFFERENT");
        } else {
            return getSession().getLabel("SUJET_EMAIL_OK_ACOMPTE_DIFFERENT");
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
