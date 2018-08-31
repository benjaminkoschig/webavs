package ch.globaz.vulpecula.businessimpl.services.decompte;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.process.journal.CAComptabiliserJournal;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class ComptabiliserJournalCA extends BProcessWithContext {
    private static final long serialVersionUID = -1228965146801743634L;
    private String idJournal;

    /**
     * constructeur par défaut pour la sérialisation du job
     */
    public ComptabiliserJournalCA() {
        super();
    }

    public ComptabiliserJournalCA(String idJournal) {
        super();
        this.idJournal = idJournal;
    }

    @Override
    protected final boolean _executeProcess() throws Exception {
        new CAComptabiliserJournal().comptabiliser(this, getJournal());
        return true;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    @Override
    protected String getEMailObject() {
        return "Comptabilisation du journal " + idJournal + " suite à l'annulation d'une Taxation d'Office";
    }

    public String getIdJournal() {
        return idJournal;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    private CAJournal getJournal() {

        // Si si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            return null;
        }

        // Instancier un nouveau LOG
        CAJournal journal = new CAJournal();
        journal.setSession(getSession());

        // Récupérer le log en question
        journal.setIdJournal(getIdJournal());
        try {
            journal.retrieve();
            if (journal.hasErrors() || journal.isNew()) {
                journal = null;
            }
        } catch (Exception e) {
            _addError(null, e.getMessage());
            journal = null;
        }

        return journal;
    }

}
