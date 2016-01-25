package globaz.pavo.db.bta;

import globaz.pavo.vb.CIAbstractPersistentViewBean;

public class CIInscriptionBtaViewBean extends CIAbstractPersistentViewBean {
    private boolean simulation = false;

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
