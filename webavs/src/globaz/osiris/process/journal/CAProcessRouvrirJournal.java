package globaz.osiris.process.journal;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperationManager;
import java.util.ArrayList;

public class CAProcessRouvrirJournal extends BProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idJournal = new String();

    /**
	 * 
	 */
    public CAProcessRouvrirJournal() {
        super();
    }

    /**
     * @param parent
     *            BProcess
     */
    public CAProcessRouvrirJournal(BProcess parent) {
        super(parent);
    }

    /**
	 *  
	 */
    @Override
    protected void _executeCleanUp() {
    }

    /**
     * Réouvre un journal annulé si ce dernier ne contient aucune opération non ouvertes.
     * 
     * @return boolean
     */
    @Override
    protected boolean _executeProcess() {
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            getMemoryLog().logMessage("5157", null, FWMessage.FATAL, this.getClass().getName());
            return false;
        }

        try {
            CAJournal journal = getJournal();

            if (!testJournal(journal)) {
                return false;
            }

            CAOperationManager manager = new CAOperationManager();
            manager.setSession(getSession());
            manager.setForIdJournal(getIdJournal());

            ArrayList etat = new ArrayList();
            etat.add(APIOperation.ETAT_INACTIF);
            etat.add(APIOperation.ETAT_OUVERT);
            etat.add(APIOperation.ETAT_ERREUR);
            manager.setForEtatNotIn(etat);

            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            if (manager.hasErrors()) {
                return false;
            }

            if (!manager.isEmpty()) {
                this._addError(getTransaction(), getSession().getLabel("OPERATIONS_NON_OUVERTES"));
            }

            journal.setEtat(CAJournal.OUVERT);
            journal.update(getTransaction());

            if (journal.hasErrors()) {
                return false;
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.FATAL, this.getClass().getName());
        }

        return true;
    }

    @Override
    protected void _validate() throws Exception {
        super._validate();

        setSendCompletionMail(true);
        setSendMailOnError(true);
        setControleTransaction(true);

        CAJournal journal = getJournal();
        testJournal(journal);
    }

    /**
     * Set le titre de l'email.
     * 
     * @return String
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors()) {
            return getSession().getLabel("REVOUVERTURE_JOURNAL_ERROR");
        } else {
            return getSession().getLabel("REVOUVERTURE_JOURNAL_OK");
        }
    }

    /**
     * @return String
     */
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * Return le journal en fonction de l'id.
     * 
     * @return
     */
    public CAJournal getJournal() {
        CAJournal journal = new CAJournal();
        journal.setSession(getSession());
        journal.setIdJournal(getIdJournal());
        try {
            journal.retrieve(getTransaction());
        } catch (Exception e) {
            return null;
        }
        return journal;
    }

    /**
     * Method jobQueue. Cette méthode définit la nature du traitement s'il s'agit d'un processus qui doit-être lancer de
     * jour en de nuit
     * 
     * @return GlobazJobQueue
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * @param newIdJournal
     *            String
     */
    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    /**
     * Test du journal. Le journal doit être annulé. Un journal de type contentieux ne peut pas être réouvert
     * 
     * @param journal
     * @return
     */
    private boolean testJournal(CAJournal journal) {
        if (journal.isNew() || journal.hasErrors()) {
            this._addError(getTransaction(), getSession().getLabel("5157"));
            return false;
        }

        if (!journal.getEtat().equalsIgnoreCase(CAJournal.ANNULE)) {
            this._addError(getTransaction(), getSession().getLabel("JOURNAL_NON_ANNULE"));
            return false;
        }
        // Les journaux de contentieux ne peuvent pas être réouvert
        if (CAJournal.TYPE_CONTENTIEUX.equals(journal.getTypeJournal())
                || CAJournal.TYPE_JOURNALIER_CONTENTIEUX.equals(journal.getTypeJournal())) {
            this._addError(getTransaction(), getSession().getLabel("JOURNAL_NON_ANNULE_CONTENTIEUX"));
            return false;
        }

        return true;
    }

}
