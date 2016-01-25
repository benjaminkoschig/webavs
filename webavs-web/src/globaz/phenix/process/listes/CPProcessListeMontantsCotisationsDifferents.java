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
import globaz.phenix.listes.excel.CPListeMontantsCotisationsDifferents;

public class CPProcessListeMontantsCotisationsDifferents extends BProcess implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean decisionsValideesPassageComptabilise = new Boolean(false);
    private String idPassage = "";

    public CPProcessListeMontantsCotisationsDifferents() {
        super();
    }

    public CPProcessListeMontantsCotisationsDifferents(BProcess parent) {
        super(parent);
    }

    public CPProcessListeMontantsCotisationsDifferents(globaz.globall.db.BSession session) {
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
            String trimestre = "";
            String trimestrePrecedent = "";
            JADate date = JACalendar.today();
            int annee = date.getYear();
            int mois = date.getMonth();
            int moisPrecedent = 0;
            int anneePrecedente = 0;
            if ((mois >= 1) && (mois <= 3)) {
                anneePrecedente = annee - 1;
                moisPrecedent = 12;
            } else {
                moisPrecedent = mois - 3;
                anneePrecedente = annee;
            }
            // Trimestre en cours
            if ((mois >= 1) && (mois <= 3)) {
                trimestre = "31.03." + annee;
            }
            if ((mois >= 4) && (mois <= 6)) {
                trimestre = "30.06." + annee;
            }
            if ((mois >= 7) && (mois <= 9)) {
                trimestre = "30.09." + annee;
            }
            if ((mois >= 10) && (mois <= 12)) {
                trimestre = "31.12." + annee;
            }
            // Trimestre précédent
            if ((moisPrecedent >= 1) && (moisPrecedent <= 3)) {
                trimestrePrecedent = "31.03." + anneePrecedente;
            }
            if ((moisPrecedent >= 4) && (moisPrecedent <= 6)) {
                trimestrePrecedent = "30.06." + anneePrecedente;
            }
            if ((moisPrecedent >= 7) && (moisPrecedent <= 9)) {
                trimestrePrecedent = "30.09." + anneePrecedente;
            }
            if ((moisPrecedent >= 10) && (moisPrecedent <= 12)) {
                trimestrePrecedent = "31.12." + anneePrecedente;
            }
            manager.setToDateDebutDecision(trimestre);
            manager.setFromDateFinAffiliation(trimestrePrecedent);
            manager.setForAnneeDecisionCP(String.valueOf(annee));
            manager.setForDecisionsValideesPassageComptabilise(decisionsValideesPassageComptabilise);
            // Création du document
            CPListeMontantsCotisationsDifferents excelDoc = new CPListeMontantsCotisationsDifferents(getSession());
            excelDoc.setProcessAppelant(this);
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
            getSession().addError(getSession().getLabel("CP_MSG_0145"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
        if (getSession().hasErrors()) {
            abort();
        }
    }

    public Boolean getDecisionsValideesPassageComptabilise() {
        return decisionsValideesPassageComptabilise;
    }

    @Override
    protected String getEMailObject() {
        if (isAborted() || getSession().hasErrors()) {
            return getSession().getLabel("SUJET_EMAIL_PASOK_COTISATION_DIFFERENTE");
        } else {
            return getSession().getLabel("SUJET_EMAIL_OK_COTISATION_DIFFERENTE");
        }
    }

    public String getIdPassage() {
        return idPassage;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setDecisionsValideesPassageComptabilise(Boolean boolean1) {
        decisionsValideesPassageComptabilise = boolean1;
    }

    public void setIdPassage(String string) {
        idPassage = string;
    }

}
