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
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourValidation;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourValidationManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.db.communications.CPLienCommunicationsPlausi;
import globaz.phenix.db.communications.CPLienCommunicationsPlausiManager;
import globaz.phenix.db.communications.CPParametrePlausibilite;
import globaz.phenix.db.communications.CPReglePlausibilite;
import globaz.phenix.db.communications.CPValidationCalculCommunication;
import globaz.phenix.db.principale.CPDecision;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPProcessDevalidationSelectionJournalRetourViewBean extends BProcess implements FWViewBeanInterface {

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
            if (getForStatus().length() > 0) {
                manager.setForStatus(getForStatus());
            }
            if (getForTypeDecision().length() > 0) {
                manager.setForTypeDecision(getForTypeDecision());
            }
            manager.setForIdValidationCommunication(getForIdValidationCommunication());
            manager.setIsDevalidation(Boolean.TRUE);
            int maxScale = manager.getCount(getTransaction());
            if (maxScale > 0) {
                setProgressScaleValue(maxScale);
            } else {
                setProgressScaleValue(1);
            }
            statement = manager.cursorOpen(getTransaction());
            while ((communication = (CPCommunicationFiscaleRetourValidation) manager.cursorReadNext(statement)) != null
                    && (!communication.isNew()) && !isAborted()) {
                CPLienCommunicationsPlausiManager lienManager = new CPLienCommunicationsPlausiManager();
                lienManager.setSession(getSession());
                lienManager.setForIdCommunication(communication.getIdRetour());
                lienManager.find();
                // Mise à jour du status de la communication
                CPCommunicationFiscaleRetourViewBean retour = communication;
                retour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_SANS_ANOMALIE);
                for (int i = 0; i < lienManager.size(); i++) {
                    CPLienCommunicationsPlausi lien = (CPLienCommunicationsPlausi) lienManager.get(i);

                    CPParametrePlausibilite plausi = new CPParametrePlausibilite();
                    plausi.setSession(getSession());
                    plausi.setIdParametre(lien.getIdPlausibilite());
                    plausi.retrieve();

                    CPReglePlausibilite regle = new CPReglePlausibilite();
                    regle.setSession(getSession());
                    regle.setIdPlausibilite(plausi.getIdPlausibilite());
                    regle.retrieve();
                    if (regle.getDeclenchement().equals(CPReglePlausibilite.CS_APRES_GENERATION)) {
                        if (plausi.getTypeMessage().equals(CPParametrePlausibilite.CS_MSG_AVERTISSEMENT)) {
                            retour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_A_CONTROLER);
                        } else {
                            retour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_ERREUR);
                        }
                    }
                }
                getTransaction().disableSpy();
                retour.update(getTransaction());
                getTransaction().enableSpy();
                CPValidationCalculCommunication validation = new CPValidationCalculCommunication();
                validation.setSession(getSession());
                validation.setIdValidationCommunication(communication.getIdValidation());
                validation.retrieve();
                if (!validation.isNew()) {
                    validation.setValidation(Boolean.TRUE);
                    validation.update();
                }
                CPDecision decision = new CPDecision();
                decision.setSession(getSession());
                decision.setIdDecision(validation.getIdDecision());
                decision.retrieve();
                if (!decision.isNew()) {
                    decision.setIdPassage("0");
                    decision.setDernierEtat(CPDecision.CS_CALCUL);
                    decision.update();
                }
                // Mise à jour du status du journal
                CPJournalRetour journal = new CPJournalRetour();
                journal.setSession(getSession());
                journal.setIdJournalRetour(communication.getIdJournalRetour());
                journal.retrieve();
                journal.update();
                incProgressCounter();
            }
        } catch (Exception e) {
            return false;
        }
        return successful;
    }

    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        if (getNombreJournauxADevalider() == "0") {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage(getSession().getLabel("PROCVALPLAU_NO_IDCOMM"));
            abort();
        }
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors() || getMemoryLog().isOnErrorLevel()
                || getMemoryLog().isOnFatalLevel()) {
            return getSession().getLabel("TITRE_EMAIL_DEVALIDATION_COM_FISC_KO");
        } else {
            return getSession().getLabel("TITRE_EMAIL_DEVALIDATION_COM_FISC_OK");
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

    public String getNombreJournauxADevalider() {
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
        manager.setIsDevalidation(Boolean.TRUE);
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
