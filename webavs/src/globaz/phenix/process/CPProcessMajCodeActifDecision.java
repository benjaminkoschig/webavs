package globaz.phenix.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.phenix.toolbox.CPToolBox;

public class CPProcessMajCodeActifDecision extends BProcess {

    private static final long serialVersionUID = 1633042285234463104L;
    private String fromNumAffilie = "";

    private String toNumAffilie = "";

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessMajCodeActifDecision() {
        super();
    }

    public CPProcessMajCodeActifDecision(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public CPProcessMajCodeActifDecision(globaz.globall.db.BSession session) {
        super(session);
    }

    /**
     * Nettoyage après erreur ou exécution Date de création : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        // Sous controle d'exceptions

        BStatement statement = null;
        // Lire les affiliations
        AFAffiliationManager affiManager = new AFAffiliationManager();
        affiManager.setSession(getSession());
        affiManager.setFromAffilieNumero(getFromNumAffilie());
        affiManager.setToAffilieNumero(getToNumAffilie());
        affiManager.forIsTraitement(false);
        affiManager.setOrderBy(AFAffiliationManager.ORDER_AFFILIENUMERO);
        try {
            String anneeLimite = CPToolBox.anneeLimite(getTransaction());
            AFAffiliation myAffiliation = null;
            setProgressScaleValue(affiManager.getCount(getTransaction()));
            statement = affiManager.cursorOpen(getTransaction());
            // -------------------------------------------------------------------------
            while (((myAffiliation = (AFAffiliation) affiManager.cursorReadNext(statement)) != null)
                    && (!myAffiliation.isNew()) && !isAborted()) {
                // ---------------------------------------------------------------------
                CPToolBox.miseAjourDecisionActive(getTransaction(), myAffiliation, "", Integer.parseInt(anneeLimite));
                CPToolBox.miseAjourImputation(getTransaction(), myAffiliation, "", Integer.parseInt(anneeLimite));
                CPToolBox.miseAjourRemise(getTransaction(), myAffiliation, "", Integer.parseInt(anneeLimite));
                // committer
                if (!getTransaction().hasErrors() && !isAborted()) {
                    try {
                        getTransaction().commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                incProgressCounter();
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        } // Fin de la procédure

        finally {
            try {
                affiManager.cursorClose(statement);
                statement = null;
            } catch (Exception e) {
                this._addError(getTransaction(), getSession().getLabel("CP_MSG_0125") + e.getMessage());
            }
        }
        return !isOnError();

    }

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
        // Déterminer l'objet du message en fonction du code erreur
        String obj = "";
        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("CP_MSG_0103");
        }
        // else
        // obj = FWMessage.getMessageFromId("5030")+ " " + getIdDecision();
        // Restituer l'objet
        return obj;
    }

    /**
     * @return
     */
    public String getFromNumAffilie() {
        return fromNumAffilie;
    }

    /**
     * @return
     */
    public String getToNumAffilie() {
        return toNumAffilie;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * @param string
     */
    public void setFromNumAffilie(String string) {
        fromNumAffilie = string;
    }

    /**
     * @param string
     */
    public void setToNumAffilie(String string) {
        toNumAffilie = string;
    }

}
