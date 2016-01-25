package globaz.phenix.process.acompte;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliationTiers;
import globaz.phenix.db.principale.CPDecisionAffiliationTiersManager;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
/**
 * Insérez la description du type ici. Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public class CPProcessAcompteSupprimer extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forGenreAffilie = "";
    private String forTypeDecision = "";
    // Paramètres utiliser par l'écran du decisionRepriseViewBean
    private String idPassage = "";

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessAcompteSupprimer() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessAcompteSupprimer(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public CPProcessAcompteSupprimer(BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Calcul des montants de cotisation Date de création : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        // Lire les décisions du passage
        BStatement statement = null;
        CPDecisionAffiliationTiersManager decManager = new CPDecisionAffiliationTiersManager();
        decManager.setSession(getSession());
        decManager.setForIdPassage(getIdPassage());
        decManager.setForGenreAffilie(getForGenreAffilie());
        decManager.setForTypeDecision(getForTypeDecision());
        decManager.setSelectMaxDateInformation(Boolean.FALSE);
        decManager.setForNotEtat(CPDecision.CS_FACTURATION + ", " + CPDecision.CS_PB_COMPTABILISATION);
        decManager.orderByNumAffilie();
        decManager.orderByIdDecision();
        try {
            statement = decManager.cursorOpen(getTransaction());
            CPDecisionAffiliationTiers myDecision = null;
            // compteur du progress
            long progressCounter = 0;
            // Permet d'afficher les données
            setProgressScaleValue(decManager.getCount());
            // -------------------------------------------------------------------------
            // itérer sur toutes les décisions de factures du passage
            while (((myDecision = (CPDecisionAffiliationTiers) decManager.cursorReadNext(statement)) != null)
                    && (!myDecision.isNew()) && !isAborted()) {
                setProgressCounter(progressCounter++);
                if (!CPDecision.CS_FACTURATION.equalsIgnoreCase(myDecision.getEtat())
                        && !CPDecision.CS_PB_COMPTABILISATION.equalsIgnoreCase(myDecision.getEtat())) {
                    CPDecision decision = new CPDecision();
                    decision.setIdDecision(myDecision.getIdDecision());
                    decision.setSession(getSession());
                    decision.retrieve();
                    if (!decision.isNew()) {
                        decision.delete(getTransaction());
                    }
                }
                // committer
                if (!getTransaction().hasErrors() && !isAborted()) {
                    try {
                        getTransaction().commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        } // Fin de la procédure
        finally {
            try {
                decManager.cursorClose(statement);
                statement = null;
            } catch (Exception e) {
                this._addError(getTransaction(), e.getMessage());
            }
        }
        return !isOnError();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.03.2003 10:44:29)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate() throws java.lang.Exception {
        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0101"));
        }
        if (JadeStringUtil.isIntegerEmpty(getIdPassage())) {
            this._addError(getTransaction(), getSession().getLabel("CP_MSG_0102"));
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        String obj = "";
        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("SUJET_EMAIL_ACOMPTE_SUPPRESSION_FAILED");
        } else {
            obj = getSession().getLabel("SUJET_EMAIL_ACOMPTE_SUPPRESSION");
        }
        // Restituer l'objet
        return obj;
    }

    public String getForGenreAffilie() {
        return forGenreAffilie;
    }

    public String getForTypeDecision() {
        return forTypeDecision;
    }

    /**
     * Returns the idPassage.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdPassage() {
        return idPassage;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setForGenreAffilie(String forGenreAffilie) {
        this.forGenreAffilie = forGenreAffilie;
    }

    public void setForTypeDecision(String forTypeDecision) {
        this.forTypeDecision = forTypeDecision;
    }

    /**
     * Sets the idPassage.
     * 
     * @param idPassage
     *            The idPassage to set
     */
    public void setIdPassage(java.lang.String idPassage) {
        this.idPassage = idPassage;
    }

}
