package globaz.pavo.db.bta;

import globaz.pavo.vb.CIAbstractPersistentViewBean;

public class CIRepetitionBtaViewBean extends CIAbstractPersistentViewBean {

    private boolean simulation = false;

    public CIRepetitionBtaViewBean() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public boolean isSimulation() {
        return simulation;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
