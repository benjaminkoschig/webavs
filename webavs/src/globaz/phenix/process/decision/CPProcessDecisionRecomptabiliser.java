package globaz.phenix.process.decision;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.db.principale.CPDecisionNonComptabilisee;
import globaz.phenix.db.principale.CPDecisionNonComptabiliseeManager;
import globaz.phenix.process.CPProcessValidationFacturation;

/**
 * Insérez la description du type ici. Date de création : (18.11.2004 14:00:00)
 * 
 * @author: acr
 */
public class CPProcessDecisionRecomptabiliser extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idDecision = "";
    private Boolean wantMajCI = Boolean.TRUE;

    // private CIJournal journal = null;
    // private String erreurs = "";
    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessDecisionRecomptabiliser() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessDecisionRecomptabiliser(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public CPProcessDecisionRecomptabiliser(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Traitement de facturation pour les décisions qui ont été prises pour l'année suivante et qui sont déjà à l'état
     * comptabilisé mais qui n'ont pas encore été facturées. Ce traitement utilise un paramêtre "année limite" qui doit
     * être modifié avant son exécution. Date de création : (19.11.2004 08:00:00)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        boolean result = true;
        try {
            // Lecture des décisions à recomptabiliser
            CPDecisionNonComptabiliseeManager decManager = new CPDecisionNonComptabiliseeManager();
            decManager.setSession(getSession());
            decManager.setSelectMaxDateInformation(new Boolean(false));
            if (!JadeStringUtil.isNull(getIdDecision())) {
                decManager.setForIdDecision(getIdDecision());
            }
            decManager.changeManagerSize(BManager.SIZE_NOLIMIT);
            decManager.setOrderBy("MALNAF");
            decManager.find();
            for (int i = 0; i < decManager.size(); i++) {
                CPDecision decision = new CPDecision();
                decision.setIdDecision(((CPDecisionNonComptabilisee) decManager.getEntity(i)).getIdDecision());
                decision.setSession(getSession());
                decision.retrieve();
                // Test s'il n'y a pas eu depuis une nouvelle décision qui a été
                // comptabilisé
                // Dans ce cas la décision peut être validée sans qu'elle mette
                // à jour d'autres données
                CPDecisionManager decM = new CPDecisionManager();
                decM.setSession(getSession());
                decM.setForIdAffiliation(decision.getIdAffiliation());
                decM.setForAnneeDecision(decision.getAnneeDecision());
                decM.setInEtat(CPDecision.CS_VALIDATION + ", " + CPDecision.CS_FACTURATION);
                decM.setForNotComplementaire(Boolean.TRUE);
                decM.setFromGtIdDecision(decision.getIdDecision());
                decM.find();
                if (decM.getSize() > 0) {
                    decision.setDernierEtat(CPDecision.CS_FACTURATION);
                    decision.update();
                } else {
                    CPDecisionNonComptabilisee decAComptabiliser = (CPDecisionNonComptabilisee) decManager.getEntity(i);
                    CPProcessValidationFacturation processValidationFacturation = new CPProcessValidationFacturation(
                            getSession());
                    processValidationFacturation.setIdDecision(decAComptabiliser.getIdDecision());
                    processValidationFacturation.setOptionReComptabiliser(true);
                    processValidationFacturation.setSendCompletionMail(false);
                    processValidationFacturation.setSendMailOnError(false);
                    processValidationFacturation.setWantMajCI(getWantMajCI());
                    processValidationFacturation.executeProcess();
                    getMemoryLog().logMessage(processValidationFacturation.getMemoryLog());
                }
            }
            // contrôler si le process a fonctionné
            if (getTransaction().hasErrors()) {
                result = false;
            }
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            return false;
        }
        return result;
    }

    @Override
    protected java.lang.String getEMailObject() {
        return getSession().getLabel("CP_PROCESSUS_RECPT");
    }

    /**
     * Returns the idDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    public Boolean getWantMajCI() {
        return wantMajCI;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * Sets the idDecision.
     * 
     * @param idDecision
     *            The idDecision to set
     */
    public void setIdDecision(java.lang.String idPassage) {
        idDecision = idPassage;
    }

    public void setWantMajCI(Boolean wantMajCI) {
        this.wantMajCI = wantMajCI;
    }

}
