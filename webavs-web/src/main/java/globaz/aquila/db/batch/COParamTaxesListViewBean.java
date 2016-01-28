package globaz.aquila.db.batch;

import globaz.aquila.db.access.batch.COParamTaxesManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

public class COParamTaxesListViewBean extends COParamTaxesManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forEtape = new String();
    private String forIdSequence = new String();

    @Override
    protected BEntity _newEntity() throws Exception {
        return new COParamTaxesViewBean();
    }

    @Override
    public String getForEtape() {
        return forEtape;
    }

    @Override
    public String getForIdSequence() {
        return forIdSequence;
    }

    @Override
    public void setForEtape(String forEtape) {
        this.forEtape = forEtape;
    }

    @Override
    public void setForIdSequence(String forIdSequence) {
        this.forIdSequence = forIdSequence;
    }

}
