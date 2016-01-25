/**
 * CPValidationJournalRetourViewBean Créé le 26.02.07
 * 
 * @author : jpa
 */
package globaz.phenix.process.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourValidation;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourValidationManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.principale.CPDecisionValiderViewBean;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPProcessAbandonSelectionJournalRetourViewBean extends BProcess implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forAnnee = "";

    private String forGenreAffilie = "";

    private String forGrpExtraction = "";

    private String forGrpTaxation = "";

    private String forIdValidationCommunication = "";

    private String forJournal = "";

    private String forStatus = "";

    private String forTypeDecision = "";

    private Boolean forValide = new Boolean(false);

    private String idJournalFacturation = "";

    private String libellePassage = "";

    private String likeNumAffilie = "";

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub
    }

    @Override
    protected boolean _executeProcess() {
        boolean successful = true;
        BStatement statement = null;
        CPCommunicationFiscaleRetourValidation communication = null;
        try {
            // On boucle sur le manager et on valide les communications les unes
            // après les autres !
            CPCommunicationFiscaleRetourValidationManager manager = new CPCommunicationFiscaleRetourValidationManager();
            manager.setWhitAffiliation(true);
            manager.setSession(getSession());
            if (getForGenreAffilie().length() > 0) {
                manager.setForGenreAffilie(getForGenreAffilie());
            }
            if (getForJournal().length() > 0) {
                manager.setForJournal(getForJournal());
            }
            if (getLikeNumAffilie().length() > 0) {
                manager.setLikeNumAffilie(getLikeNumAffilie());
            }
            if (getForAnnee().length() > 0) {
                manager.setForAnnee(getForAnnee());
            }
            if (getForGrpExtraction().length() > 0) {
                manager.setForGrpExtraction(getForGrpExtraction());
            }
            if (getForTypeDecision().length() > 0) {
                manager.setForTypeDecision(getForTypeDecision());
            }
            if (getForGrpTaxation().length() > 0) {
                manager.setForGrpTaxation(getForGrpTaxation());
            }
            if (getForStatus().length() > 0) {
                manager.setForStatus(getForStatus());
            }
            manager.setForIdValidationCommunication(getForIdValidationCommunication());
            manager.orderByIdCommunicationRetour();
            int maxScale = manager.getCount(getTransaction());
            if (maxScale > 0) {
                setProgressScaleValue(maxScale);
            } else {
                setProgressScaleValue(1);
            }
            String idJrnBk = "";
            statement = manager.cursorOpen(getTransaction());
            while ((communication = (CPCommunicationFiscaleRetourValidation) manager.cursorReadNext(statement)) != null
                    && (!communication.isNew()) && !isAborted()) {
                CPDecisionValiderViewBean decision = new CPDecisionValiderViewBean();
                decision.setSession(getSession());
                decision.setIdDecision(communication.getIdDecision());
                decision.setSuppressionExterne(true);
                decision.retrieve();
                if (!decision.isNew()) {
                    decision.delete();
                }
                CPCommunicationFiscaleRetourViewBean retour = new CPCommunicationFiscaleRetourViewBean();
                retour.setSession(getSession());
                retour.setIdRetour(communication.getIdRetour());
                retour.retrieve();
                if (!retour.isNew()) {
                    retour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE);
                    retour.update(getTransaction());
                }
                // Mise à jour du status du journal à chaque changement d'
                // idJournalRetour
                // Test pour ne pas le faire au début (sinon le status ne sera
                // pas mis à jour)
                if (!JadeStringUtil.isEmpty(idJrnBk)) {
                    if (!idJrnBk.equalsIgnoreCase(communication.getIdJournalRetour())) {
                        CPJournalRetour jrn = new CPJournalRetour();
                        jrn.setSession(getSession());
                        jrn.setIdJournalRetour(idJrnBk);
                        jrn.retrieve(getTransaction());
                        if (!jrn.isNew()) {
                            jrn.update(getTransaction());
                        }
                        idJrnBk = communication.getIdJournalRetour();
                    }
                } else {
                    idJrnBk = communication.getIdJournalRetour();
                }
                incProgressCounter();
            }
            // Mise à jour du dernier journal lu
            CPJournalRetour jrn = new CPJournalRetour();
            jrn.setSession(getSession());
            jrn.setIdJournalRetour(idJrnBk);
            jrn.retrieve(getTransaction());
            if (!jrn.isNew()) {
                jrn.update(getTransaction());
            }
        } catch (Exception e) {
            return false;
        }
        return successful;
    }

    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        if (getNombreAbandon() == "0") {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("PROCVALPLAU_NO_IDCOMM"));
            abort();
        }
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().isOnErrorLevel()
                || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("TITRE_EMAIL_ABANDON_COM_FISC_KO");
        } else {
            return getSession().getLabel("TITRE_EMAIL_ABANDON_COM_FISC_OK");
        }
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public String getForGenreAffilie() {
        return forGenreAffilie;
    }

    public String getForGrpExtraction() {
        return forGrpExtraction;
    }

    public String getForGrpTaxation() {
        return forGrpTaxation;
    }

    public String getForIdValidationCommunication() {
        return forIdValidationCommunication;
    }

    public String getForJournal() {
        return forJournal;
    }

    public String getForStatus() {
        return forStatus;
    }

    public String getForTypeDecision() {
        return forTypeDecision;
    }

    public Boolean getForValide() {
        return forValide;
    }

    public String getIdJournalFacturation() {
        return idJournalFacturation;
    }

    public String getLibellePassage() {
        return libellePassage;
    }

    public String getLikeNumAffilie() {
        return likeNumAffilie;
    }

    public String getNombreAbandon() {
        CPCommunicationFiscaleRetourValidationManager manager = new CPCommunicationFiscaleRetourValidationManager();
        manager.setWhitAffiliation(true);
        manager.setSession(getSession());
        if (getForJournal().length() > 0) {
            manager.setForJournal(getForJournal());
        }
        if (getLikeNumAffilie().length() > 0) {
            manager.setLikeNumAffilie(getLikeNumAffilie());
        }
        if (getForAnnee().length() > 0) {
            manager.setForAnnee(getForAnnee());
        }
        if (getForGenreAffilie().length() > 0) {
            manager.setForGenreAffilie(getForGenreAffilie());
        }
        if (getForGrpExtraction().length() > 0) {
            manager.setForGrpExtraction(getForGrpExtraction());
        }
        if (getForGrpTaxation().length() > 0) {
            manager.setForGrpTaxation(getForGrpTaxation());
        }
        if (getForTypeDecision().length() > 0) {
            manager.setForTypeDecision(getForTypeDecision());
        }
        if (getForStatus().length() > 0) {
            manager.setForStatus(getForStatus());
        }
        try {
            return String.valueOf(manager.getCount());
        } catch (Exception e) {
            return "0";
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForGenreAffilie(String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    public void setForGrpExtraction(String forGrpExtraction) {
        this.forGrpExtraction = forGrpExtraction;
    }

    public void setForGrpTaxation(String forGrpTaxation) {
        this.forGrpTaxation = forGrpTaxation;
    }

    public void setForIdValidationCommunication(String forIdValidationCommunication) {
        this.forIdValidationCommunication = forIdValidationCommunication;
    }

    public void setForJournal(String forJournal) {
        this.forJournal = forJournal;
    }

    public void setForStatus(String forStatus) {
        this.forStatus = forStatus;
    }

    public void setForTypeDecision(String forTypeDecision) {
        this.forTypeDecision = forTypeDecision;
    }

    public void setForValide(Boolean forValide) {
        this.forValide = forValide;
    }

    public void setIdJournalFacturation(String idJournalFacturation) {
        this.idJournalFacturation = idJournalFacturation;
    }

    public void setLibellePassage(String libellePassage) {
        this.libellePassage = libellePassage;
    }

    public void setLikeNumAffilie(String likeNumAffilie) {
        this.likeNumAffilie = likeNumAffilie;
    }
}
