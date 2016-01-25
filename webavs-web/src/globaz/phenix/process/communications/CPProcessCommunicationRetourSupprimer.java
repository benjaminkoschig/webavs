package globaz.phenix.process.communications;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourManager;
import globaz.phenix.db.communications.CPJournalRetour;
import globaz.phenix.interfaces.ICommunicationRetour;
import globaz.phenix.interfaces.ICommunicationrRetourManager;

/**
 * Dévalide une décision - Enlève l'état validation et remet l'idPassage à blanc Ne peut se faire que si la décision
 * n'est pas en état "facturé" ou "reprise" Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public final class CPProcessCommunicationRetourSupprimer extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPlausibilite = "";
    private java.lang.String forStatus = "";
    private java.lang.String fromNumAffilie = "";
    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#_validate()
     */
    private java.lang.String idJournalRetour = "";
    private java.lang.String tillNumAffilie = "";

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessCommunicationRetourSupprimer() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessCommunicationRetourSupprimer(BProcess parent) {
        super(parent);
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
        BStatement statement = null;
        ICommunicationrRetourManager comManager = null;
        // Sous controle d'exceptions
        try {
            // Test du canton pour savoir quel manager utiliser.
            CPJournalRetour jrn = new CPJournalRetour();
            jrn.setSession(getSession());
            jrn.setIdJournalRetour(getIdJournalRetour());
            jrn.retrieve(getTransaction());
            if (!jrn.isNew()) {
                comManager = jrn.determinationManager();
            }
            comManager.setSession(getSession());
            comManager.setForIdJournalRetour(getIdJournalRetour());
            comManager.setFromNumAffilie(getFromNumAffilie());
            comManager.setTillNumAffilie(getTillNumAffilie());
            comManager.setForStatus(getForStatus());
            comManager.setWhitAffiliation(true);
            comManager.setForIdPlausibilite(getForIdPlausibilite());
            int maxScale = comManager.getCount(getTransaction());
            if (maxScale > 0) {
                setProgressScaleValue(maxScale);
            } else {
                setProgressScaleValue(1);
            }
            // itérer sur toutes les communications
            statement = comManager.cursorOpen(getTransaction());
            ICommunicationRetour comRet = null;
            while ((comRet = (ICommunicationRetour) comManager.cursorReadNext(statement)) != null && (!comRet.isNew())
                    && !isAborted()) {
                getTransaction().disableSpy();
                comRet.deleteCas(getTransaction());
                getTransaction().enableSpy();
                incProgressCounter();
            }
            // Mise à jour du journal
            jrn.retrieve(getTransaction());
            if (!jrn.isNew()) {
                // Suppression du journal si il n'y a plus de communication
                CPCommunicationFiscaleRetourManager comManager1 = new CPCommunicationFiscaleRetourManager();
                comManager1.setSession(getSession());
                comManager1.setForIdJournalRetour(getIdJournalRetour());
                int nbRetour = comManager1.getCount();
                if (nbRetour == 0) {
                    jrn.delete(getTransaction());
                } else {
                    jrn.update(getTransaction());
                }
                if (getTransaction().hasErrors()) {
                    getMemoryLog().logStringBuffer(getTransaction().getErrors(), jrn.getClass().getName());
                    getMemoryLog().logMessage("5029", null, FWMessage.FATAL, getClass().getName());
                    return false;
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, getClass().getName());
        } finally {
            try {
                comManager.cursorClose(statement);
                comManager = null;
                statement = null;
            } catch (Exception e) {
                getMemoryLog().logMessage("Erreur lors de la suppression des retours", e.toString(),
                        this.getClass().getName());
            }
        }
        return !isOnError();
    }

    @Override
    protected void _validate() throws Exception {
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

    /**
     * Insérez la description de la méthode ici. Date de création : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {

        // Déterminer l'objet du message en fonction du code erreur
        String obj = "";

        if (getMemoryLog().hasErrors()) {
            obj = getSession().getLabel("PROCSUPPRIMERRETOUR_ERROR") + getIdJournalRetour();
        } else {
            obj = getSession().getLabel("PROCSUPPRIMERRETOUR_SUCCES") + getIdJournalRetour();
        }
        // Restituer l'objet
        return obj;

    }

    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

    /**
     * @return
     */
    public java.lang.String getForStatus() {
        return forStatus;
    }

    public java.lang.String getFromNumAffilie() {
        return fromNumAffilie;
    }

    /**
     * @return
     */
    public java.lang.String getIdJournalRetour() {
        return idJournalRetour;
    }

    public java.lang.String getTillNumAffilie() {
        return tillNumAffilie;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setForIdPlausibilite(String forIdPlausibilite) {
        this.forIdPlausibilite = forIdPlausibilite;
    }

    /**
     * @param string
     */
    public void setForStatus(java.lang.String string) {
        forStatus = string;
    }

    public void setFromNumAffilie(java.lang.String fromNumAffilie) {
        this.fromNumAffilie = fromNumAffilie;
    }

    /**
     * @param string
     */
    public void setIdJournalRetour(java.lang.String string) {
        idJournalRetour = string;
    }

    public void setTillNumAffilie(java.lang.String tillNumAffilie) {
        this.tillNumAffilie = tillNumAffilie;
    }

}
