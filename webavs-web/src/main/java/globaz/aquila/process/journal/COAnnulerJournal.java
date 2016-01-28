package globaz.aquila.process.journal;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.db.access.journal.COElementJournalBatch;
import globaz.aquila.db.access.journal.COElementJournalBatchManager;
import globaz.aquila.db.access.journal.COJournalBatch;
import globaz.aquila.process.journal.utils.COUtilsJournal;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;

public class COAnnulerJournal extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String LABEL_ELEMENT_NI_OUVERT_NI_ERREUR = "ELEMENT_NI_OUVERT_NI_ERREUR";
    private static final String LABEL_JOURNAL_ANNULATION_ERROR = "JOURNAL_ANNULATION_ERROR";
    private static final String LABEL_JOURNAL_ANNULATION_OK = "JOURNAL_ANNULATION_OK";

    private String idJournal;

    /**
     * Constructor for COExecuterJournal.
     */
    public COAnnulerJournal() throws Exception {
        this(new BSession(ICOApplication.DEFAULT_APPLICATION_AQUILA));
    }

    /**
     * Constructor for COExecuterJournal.
     * 
     * @param parent
     */
    public COAnnulerJournal(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for COExecuterJournal.
     * 
     * @param session
     */
    public COAnnulerJournal(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {

    }

    /**
     * Set l'état des éléments du journal à INACTIF et le journal à ANNULE.
     * 
     * @see globaz.globall.db.BProcess#_executeProcess()
     */
    @Override
    protected boolean _executeProcess() throws Exception {
        COElementJournalBatchManager manager = new COElementJournalBatchManager();
        manager.setSession(getSession());

        manager.setForIdJournal(getIdJournal());

        manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

        setProgressScaleValue(manager.size());
        for (int i = 0; (i < manager.size()) && !isAborted(); i++) {
            COElementJournalBatch element = (COElementJournalBatch) manager.get(i);

            if (!element.isOuvert() && !element.isErreur()) {
                throw new Exception(getSession().getLabel(COAnnulerJournal.LABEL_ELEMENT_NI_OUVERT_NI_ERREUR));
            }

            element.setEtat(COElementJournalBatch.INACTIF);
            element.update(getTransaction());

            incProgressCounter();
        }

        if (isAborted()) {
            return false;
        }

        COUtilsJournal.updateEtatJournal(getSession(), getTransaction(), getIdJournal(), COJournalBatch.ANNULE);

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);

        COUtilsJournal.isIdJournalEmpty(getSession(), getIdJournal());

        COJournalBatch journal = COUtilsJournal.getJournal(getSession(), getTransaction(), getIdJournal());
        if (!(journal.isOuvert() || journal.isErreur())) {
            this._addError(getSession().getLabel(COElementJournalBatch.LABEL_JOURNAL_NI_OUVERT_NI_ERREUR));
            throw new Exception(getSession().getLabel(COElementJournalBatch.LABEL_JOURNAL_NI_OUVERT_NI_ERREUR));
        }
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        if (isOnError() || getSession().hasErrors()) {
            return getSession().getLabel(COAnnulerJournal.LABEL_JOURNAL_ANNULATION_ERROR);
        } else {
            return getSession().getLabel(COAnnulerJournal.LABEL_JOURNAL_ANNULATION_OK);
        }
    }

    public String getIdJournal() {
        return idJournal;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

}
