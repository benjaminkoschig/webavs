package globaz.pavo.db.inscriptions;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;
import globaz.pavo.process.CIImpressionDemandeCA;

public class CIDemandeCAImprimerViewBean extends CIImpressionDemandeCA implements BIPersistentObject,
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id = null;

    public CIDemandeCAImprimerViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    @Override
    public void update() throws Exception {
    }
}
