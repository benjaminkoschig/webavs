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

public class CIEffaceJournal extends FWProcess {

    private static final long serialVersionUID = -7957999608386869356L;
    private String idJournal = new String();

    public CIEffaceJournal() {
        super();

    }

    public CIEffaceJournal(BSession session) {
        super(session);

    }

    public CIEffaceJournal(FWProcess parent) {
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
            int counter = 0;
            ecrMgr.setSession(getSession());
            ecrMgr.setForIdJournal(idJournal);
            ecrMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            statement = ecrMgr.cursorOpen(getTransaction());
            CIEcriture ecriture = null;
            while ((ecriture = (CIEcriture) ecrMgr.cursorReadNext(statement)) != null) {
                ecriture.simpleDelete(getTransaction());
                counter++;
                if (counter % 100 == 0) {
                    getTransaction().commit();
                    System.out.println("commit counter");
                }
            }
            ecrMgr.cursorClose(statement);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return !isOnError();

    }

    @Override
    public void _validate() {
        CIJournal journal = new CIJournal();
        journal.setSession(getSession());
        journal.setIdJournal(idJournal);
        try {
            journal.retrieve();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (journal == null) {
            abort();
            return;
        }
        setControleTransaction(true);
        if (JAUtil.isStringEmpty(getIdJournal())) {
            abort();
            return;

        }
        if (!CIJournal.CS_OUVERT.equals(journal.getIdEtat())) {
            _addError(getTransaction(), getSession().getLabel("MSG_JOURNAL_UPDATE"));
            return;
        }
        if (!getSession().hasErrors()) {
            setControleTransaction(true);
            setSendCompletionMail(true);
        }

    }

    @Override
    protected String getEMailObject() {
        return "";

    }

    public String getIdJournal() {
        return idJournal;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }

    public void setIdJournal(String string) {
        idJournal = string;
    }

}
