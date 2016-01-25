package globaz.phenix.process.communications;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.log.JadeLogger;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.communications.CPJournalRetour;

/**
 * Dévalide une décision - Enlève l'état validation et remet l'idPassage à blanc Ne peut se faire que si la décision
 * n'est pas en état "facturé" ou "reprise" Date de création : (25.02.2002 13:41:13)
 * 
 * @author: Administrator
 */
public final class CPProcessAbandonner extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPlausibilite = "";
    private java.lang.String forStatus = "";
    private java.lang.String fromNumAffilie = "";
    private java.lang.String idJournalRetour = "";
    private java.lang.String idRetour = "";
    private java.lang.String tillNumAffilie = "";

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessAbandonner() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2002 10:50:34)
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessAbandonner(BProcess parent) {
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
        // Sous controle d'exceptions
        try {
            BStatement statement = null;
            CPCommunicationFiscaleRetourViewBean comRetour = null;
            // Manager
            CPCommunicationFiscaleRetourManager comRetourMng = new CPCommunicationFiscaleRetourManager();
            comRetourMng.setSession(getSession());
            comRetourMng.setForIdRetour(getIdRetour());
            comRetourMng.setForIdJournalRetour(getIdJournalRetour());
            comRetourMng.setForStatus(getForStatus());
            comRetourMng.setFromNumAffilie(getFromNumAffilie());
            comRetourMng.setTillNumAffilie(getTillNumAffilie());
            comRetourMng.setForIdPlausibilite(getForIdPlausibilite());
            comRetourMng.setWhitAffiliation(true);
            // Sous controle d'exceptions
            int maxScale = comRetourMng.getCount(getTransaction());
            if (maxScale > 0) {
                setProgressScaleValue(maxScale);
            } else {
                setProgressScaleValue(1);
            }
            // itérer sur toutes les communication en retour
            statement = comRetourMng.cursorOpen(getTransaction());
            while (((comRetour = (CPCommunicationFiscaleRetourViewBean) comRetourMng.cursorReadNext(statement)) != null)
                    && (!comRetour.isNew()) && !isAborted()) {
                if (!getTransaction().hasErrors()) {
                    setIdJournalRetour(comRetour.getIdJournalRetour());
                    if (!CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE.equalsIgnoreCase(comRetour.getStatus())
                            && !CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE.equalsIgnoreCase(comRetour
                                    .getStatus())) {
                        // Si état = validé => contrôler si le journal de
                        // facturation n'est pas en traitement
                        comRetour._suppressionDecision(getTransaction(), "");
                        comRetour.setStatus(CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE);
                        comRetour.update(getTransaction());
                        if (comRetour.hasErrors()) {
                            getMemoryLog()
                                    .logStringBuffer(getTransaction().getErrors(), comRetour.getClass().getName());
                            getMemoryLog().logMessage("5029", null, FWMessage.FATAL, this.getClass().getName());
                            return false;
                        }
                    }
                }
                incProgressCounter();
            }
            // Mise à jour de l'état du journal
            CPJournalRetour jrn = new CPJournalRetour();
            jrn.setSession(getSession());
            jrn.setIdJournalRetour(getIdJournalRetour());
            jrn.retrieve(getTransaction());
            if (!jrn.isNew()) {
                jrn.update(getTransaction());
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        } // Fin de la procédure
        return !isOnError();
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
            obj = "Echec lors de l'abandon de la communication° " + getIdRetour();
            // else
            // obj = FWMessage.getMessageFromId("5030")+ " " + getIdDecision();
        }

        // Restituer l'objet
        return obj;

    }

    public String getForIdPlausibilite() {
        return forIdPlausibilite;
    }

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

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2002 13:55:43)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdRetour() {
        return idRetour;
    }

    public java.lang.String getTillNumAffilie() {
        return tillNumAffilie;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setForIdPlausibilite(String forIdPlausibilite) {
        this.forIdPlausibilite = forIdPlausibilite;
    }

    public void setForStatus(java.lang.String forStatus) {
        this.forStatus = forStatus;
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

    /**
     * Insésérez la description de la méthode ici. Date de création : (25.02.2002 13:55:43)
     * 
     * @param newIdJournal
     *            java.lang.String
     */
    public void setIdRetour(java.lang.String newIdDecision) {
        idRetour = newIdDecision;
    }

    public void setTillNumAffilie(java.lang.String tillNumAffilie) {
        this.tillNumAffilie = tillNumAffilie;
    }

}
