package globaz.osiris.process.journal;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.print.itext.list.CAIListJournalEcritures_Doc;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (28.03.2002 08:48:00)
 * 
 * @author: Administrator
 */
public class CAProcessComptabiliserJournal extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idJournal = new String();
    private Boolean imprimerJournal = new Boolean(true);
    private globaz.osiris.db.comptes.CAJournal journal = null;
    private int showDetail = 0;

    /**
     * Commentaire relatif au constructeur CAProcessComptabiliserJournal.
     */
    public CAProcessComptabiliserJournal() {
        super();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.04.2002 11:10:23)
     * 
     * @param parent
     *            BProcess
     */
    public CAProcessComptabiliserJournal(BProcess parent) {
        super(parent);
    }

    /**
     * Nettoyage apr�s erreur ou ex�cution Date de cr�ation : (13.02.2002 14:12:14)
     */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 14:26:51)
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {

        // Sous contr�le d'exceptions
        try {
            // V�rifier l'identifiant de l'ordre group�
            if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
                getMemoryLog().logMessage("5507", null, FWMessage.FATAL, this.getClass().getName());
                return false;
            }

            // Instancier le journal
            _getJournal();
            if (isOnError()) {
                return false;
            }

            // V�rifier condition de sortie
            if (isAborted()) {
                return false;
            }

            // Partager le log
            journal.setMemoryLog(getMemoryLog());

            // Demander la comptabilisation
            if (!new CAComptabiliserJournal().comptabiliser(this, journal)) {
                return false;
            }

            // V�rifier condition de sortie
            if (isAborted()) {
                return false;
            }

            // Lancer l'impression automatique
            if (getImprimerJournal().booleanValue()) {
                // S'il n'y a pas d'erreurs
                if (!isOnError()) {
                    getTransaction().commit();
                    setState(getSession().getLabel("6110"));
                    super.setSendCompletionMail(true);
                    CAIListJournalEcritures_Doc report = new CAIListJournalEcritures_Doc(this);
                    report.setShowDetail(getShowDetail());
                    report.setIdJournal(getIdJournal());
                    report.executeProcess();
                }
            }

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        return !isOnError();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.03.2002 17:37:02)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public CAJournal _getJournal() {
        // Si pas d�j� charg�
        if (journal == null) {
            try {
                journal = new CAJournal();
                journal.setSession(getSession());
                journal.setIdJournal(getIdJournal());
                journal.retrieve(getTransaction());
                if (journal.isNew() || journal.hasErrors()) {
                    getMemoryLog().logMessage("5157", getIdJournal(), FWMessage.FATAL, this.getClass().getName());
                    journal = null;
                }
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
                journal = null;
            }
        }

        // Forcer la transaction
        if (getTransaction() != null) {
            journal.setSession(getSession());
        }

        return journal;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.02.2002 14:22:21)
     * 
     * @return java.lang.String
     */
    @Override
    protected String getEMailObject() {
        if (getMemoryLog().hasErrors()) {
            return getSession().getLabel("5007") + " " + getIdJournal();
        } else {
            return getSession().getLabel("5006") + " " + getIdJournal();
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.03.2002 08:48:29)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdJournal() {
        return idJournal;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.04.2002 16:00:19)
     * 
     * @return Boolean
     */
    public Boolean getImprimerJournal() {
        return imprimerJournal;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.04.2002 15:17:23)
     * 
     * @return boolean
     */
    public int getShowDetail() {
        return showDetail;
    }

    /**
     * Method jobQueue. Cette m�thode d�finit la nature du traitement s'il s'agit d'un processus qui doit-�tre lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.03.2002 08:48:29)
     * 
     * @param newIdJournal
     *            java.lang.String
     */
    public void setIdJournal(java.lang.String newIdJournal) {
        idJournal = newIdJournal;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (19.04.2002 16:00:19)
     * 
     * @param newImprimerJournal
     *            Boolean
     */
    public void setImprimerJournal(Boolean newImprimerJournal) {
        imprimerJournal = newImprimerJournal;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.04.2002 15:17:23)
     * 
     * @param newShowDetail
     *            boolean
     */
    public void setShowDetail(int newShowDetail) {
        showDetail = newShowDetail;
    }
}
