package globaz.pavo.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pyxis.db.tiers.TIPersonne;

public class CICheckDateNiassanceFPV extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        // TODO Auto-generated method stub
        AFAffiliationManager affMgr = new AFAffiliationManager();
        affMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
        affMgr.find();
        for (int i = 0; i < affMgr.size(); i++) {
            AFAffiliation aff = (AFAffiliation) affMgr.get(i);
            TIPersonne pers = new TIPersonne();
            pers.setSession(getSession());
            pers.setIdTiers(aff.getTiers().getIdTiers());
            pers.retrieve();
            if (getSession().hasErrors()) {
                System.out.println(getSession().getErrors());
            }

        }

        return true;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.READ_SHORT;
    }

}
