package globaz.aquila.print.journal;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;

public class COImprimerJournal extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String IMPRIMER_ELEMENTS = "elements";

    public static final String IMPRIMER_FICHIER_TS_OPGE = "bande";
    public static final String IMPRIMER_RECAPITULATIF = "recapitulatif";
    private static final String LABEL_IMPRIMER_JOURNAL_TITRE = "IMPRIMER_JOURNAL_TITRE";

    private String forIdJournal;
    private String typeListe;

    public COImprimerJournal() {
        super();
    }

    public COImprimerJournal(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
        // Do nothing
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        if (IMPRIMER_FICHIER_TS_OPGE.equals(getTypeListe())) {
            COTSOPGEImprimerRequisitionPoursuiteJournal journal = new COTSOPGEImprimerRequisitionPoursuiteJournal();

            journal.setSession(getSession());

            journal.setForIdJournal(getForIdJournal());
            journal.setEMailAddress(getEMailAddress());

            journal.start();
        } else if (IMPRIMER_RECAPITULATIF.equals(getTypeListe())) {
            COImprimerRecapitulatifJournal journal = new COImprimerRecapitulatifJournal();

            journal.setSession(getSession());

            journal.setForIdJournal(getForIdJournal());
            journal.setEMailAddress(getEMailAddress());

            journal.start();
        } else {
            COImprimerElementsJournal journal = new COImprimerElementsJournal();
            journal.setSession(getSession());

            journal.setForIdJournal(getForIdJournal());
            journal.setEMailAddress(getEMailAddress());

            journal.start();
        }

        return true;
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws Exception {
        setSendCompletionMail(false);
        setSendMailOnError(false);
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel(LABEL_IMPRIMER_JOURNAL_TITRE);
    }

    public String getForIdJournal() {
        return forIdJournal;
    }

    public String getTypeListe() {
        return typeListe;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setForIdJournal(String forIdJournal) {
        this.forIdJournal = forIdJournal;
    }

    public void setTypeListe(String typeListe) {
        this.typeListe = typeListe;
    }

}
