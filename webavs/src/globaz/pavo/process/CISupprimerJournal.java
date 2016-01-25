package globaz.pavo.process;

import globaz.framework.process.FWProcess;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.inscriptions.CIJournal;

public class CISupprimerJournal extends FWProcess {

    private static final long serialVersionUID = -4636985443984183446L;

    private String idJournal = new String();

    public CISupprimerJournal() {
        super();
    }

    /**
     * @param session
     */
    public CISupprimerJournal(BSession session) {
        super(session);
    }

    /**
     * @param parent
     */
    public CISupprimerJournal(FWProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        BStatement statement = null;
        CIEcritureManager ecrMgr = new CIEcritureManager();
        try {
            CIJournal journal = new CIJournal();
            journal.setSession(getSession());
            journal.setIdJournal(idJournal);
            journal.retrieve();
            if (journal == null || journal.isNew()) {
                _addError(getTransaction(), getSession().getLabel("MSG_SUPPRESSION_JOURNAL_IMPOSSIBLE"));
                abort();
                return false;
            }
            if (JAUtil.isStringEmpty(getIdJournal())) {
                _addError(getTransaction(), getSession().getLabel("MSG_SUPPRESSION_JOURNAL_IMPOSSIBLE"));
                abort();
                return false;
            }
            if (!CIJournal.CS_OUVERT.equals(journal.getIdEtat())) {
                _addError(getTransaction(), getSession().getLabel("MSG_JOURNAL_UPDATE"));
                abort();
                return false;
            }
            CIEcritureManager ecrMng = new CIEcritureManager();
            ecrMng.setSession(getSession());
            ecrMng.setForIdTypeCompteList(new String[] { CIEcriture.CS_CI, CIEcriture.CS_GENRE_6,
                    CIEcriture.CS_GENRE_7, CIEcriture.CS_CI_SUSPENS });
            ecrMng.setForIdJournal(getIdJournal());
            int ecrCINumber = ecrMng.getCount(getTransaction());
            if (ecrCINumber > 0) {
                _addError(getTransaction(), getSession().getLabel("MSG_JOURNAL_DELETE_TYPECOMPTE"));

                abort();
                // Pas la peine d'effacer les ecritures
                return false;
            }

            int counter = 0;
            ecrMgr.setSession(getSession());
            ecrMgr.setForIdJournal(idJournal);
            ecrMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            ecrMgr.setForIdTypeCompteList(new String[] { CIEcriture.CS_TEMPORAIRE, CIEcriture.CS_TEMPORAIRE_SUSPENS });
            statement = ecrMgr.cursorOpen(getTransaction());
            if (JAUtil.isStringEmpty(idJournal)) {
                _addError(getTransaction(), getSession().getLabel("MSG_SUPPRESSION_JOURNAL_IMPOSSIBLE"));
                abort();
                return false;
            }
            CIEcriture ecriture = null;
            while ((ecriture = (CIEcriture) ecrMgr.cursorReadNext(statement)) != null) {
                ecriture.simpleDelete(getTransaction());
                counter++;
                if (counter % 100 == 0) {
                    if (!getTransaction().hasErrors()) {
                        getTransaction().commit();
                    } else {
                        getTransaction().rollback();
                    }
                    System.out.println("commit counter");
                }
            }
            ecrMgr.cursorClose(statement);
            if (!getTransaction().hasErrors()) {
                journal.wantCallMethodBefore(false);
                journal.delete(getTransaction());
            }
            // _addError(getTransaction(),
            // getSession().getLabel("MSG_JOURNAL_UPDATE"));
            if (!getTransaction().hasErrors()) {
                setSendCompletionMail(false);
            } else {
                abort();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !isOnError();
    }

    @Override
    protected void _validate() throws Exception {
        setSendMailOnError(true);
    }

    @Override
    protected String getEMailObject() {
        if (isOnError() || isAborted() || getTransaction().hasErrors()) {
            return "la suppression du journal a échouée";
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getIdJournal() {
        return idJournal;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    /**
     * @param string
     */
    public void setIdJournal(String string) {
        idJournal = string;
    }
}
