package globaz.perseus.vb.statistiques;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;

public class PFStatsDemandesJournalieresViewBean extends BJadePersistentObjectViewBean {

    public PFStatsDemandesJournalieresViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public String getId() {
        return null;
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
