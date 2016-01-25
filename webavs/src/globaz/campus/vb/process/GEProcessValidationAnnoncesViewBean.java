package globaz.campus.vb.process;

import globaz.campus.process.validationAnnonces.GEProcessValidationAnnonces;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BIPersistentObject;

public class GEProcessValidationAnnoncesViewBean extends GEProcessValidationAnnonces implements BIPersistentObject,
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id = null;

    public GEProcessValidationAnnoncesViewBean() {
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
