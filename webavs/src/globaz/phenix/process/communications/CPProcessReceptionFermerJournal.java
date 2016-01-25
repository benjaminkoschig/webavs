package globaz.phenix.process.communications;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.phenix.db.communications.CPJournalRetour;

/**
 * @author btc
 * @revision SCO 09-12-2009
 */
public class CPProcessReceptionFermerJournal extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idJournalRetour = "";

    /**
     * Commentaire relatif au constructeur CAProcessAnnulerJournal.
     */
    public CPProcessReceptionFermerJournal() {
        super();
    }

    /**
     * Constructeur du type BProcess.
     * 
     * @param parent
     *            BProcess
     */
    public CPProcessReceptionFermerJournal(BProcess parent) {
        super(parent);
    }

    /**
     * Constructeur du type BSession.
     * 
     * @param session
     *            la session utilisée par le process
     */
    public CPProcessReceptionFermerJournal(BSession session) {
        super(session);
    }

    /**
     * @see globaz.globall.db.BProcess#_executeCleanUp()
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
    protected boolean _executeProcess() throws Exception {
        CPJournalRetour journal = new CPJournalRetour();
        journal.setIdJournalRetour(getIdJournalRetour());
        journal.setSession(getSession());
        journal.retrieve();
        journal.fermerJournal();
        return !isOnError();
    }

    /**
     * @see globaz.globall.db.BProcess#_validate()
     */
    @Override
    protected void _validate() throws java.lang.Exception {
        setSendCompletionMail(false);
    }

    /**
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {

        if (getMemoryLog().hasErrors()) {
            return getSession().getLabel("PROCRECEPTGENDEC_EMAIL_OBJECT_FAILED");
        }

        return getSession().getLabel("SUJET_EMAIL_RECEPTION_CALCUL");
    }

    public String getIdJournalRetour() {
        return idJournalRetour;
    }

    /**
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setIdJournalRetour(String idJournalRetour) {
        this.idJournalRetour = idJournalRetour;
    }

}
