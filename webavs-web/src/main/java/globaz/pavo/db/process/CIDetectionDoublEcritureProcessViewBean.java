package globaz.pavo.db.process;

import globaz.pavo.vb.CIAbstractPersistentViewBean;

public class CIDetectionDoublEcritureProcessViewBean extends CIAbstractPersistentViewBean {
    private String forAnnee = "";

    public CIDetectionDoublEcritureProcessViewBean() throws Exception {
        super();
        // TODO Raccord de constructeur auto-généré
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getForAnnee() {
        return forAnnee;
    }

    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
