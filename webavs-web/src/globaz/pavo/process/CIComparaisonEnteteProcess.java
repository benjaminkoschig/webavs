package globaz.pavo.process;

import globaz.framework.process.FWProcess;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;

public class CIComparaisonEnteteProcess extends FWProcess {

    private static final long serialVersionUID = 5110678513334177233L;

    public CIComparaisonEnteteProcess() {
        super();
    }

    /**
     * @param session
     */
    public CIComparaisonEnteteProcess(BSession session) {
        super(session);
    }

    /**
     * @param parent
     */
    public CIComparaisonEnteteProcess(FWProcess parent) {
        super(parent);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {

        CICompteIndividuelPreparedManager mgrPrp = new CICompteIndividuelPreparedManager();
        BStatement statement = null;
        BPreparedStatement ciPrepared = null;
        try {
            ciPrepared = new BPreparedStatement(getTransaction());
            ciPrepared.prepareStatement(mgrPrp.getSqlForCopy(ciPrepared));
            ciPrepared.setInt(1, 309001);
            ciPrepared.setString(2, "1");
            ciPrepared.execute();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.FATAL, "copie des CI");
        } finally {
            ciPrepared.closePreparedStatement();
        }

        return !isAborted();
    }

    @Override
    protected String getEMailObject() {
        if (!isAborted()) {
            return "La copie des comptes s'est effectuée avec succes";
        } else {
            return "La copie des comptes a échoué!";
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_LONG;
    }
}
