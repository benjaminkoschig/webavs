package globaz.eform.vb.demande;

import ch.globaz.eform.business.models.GFDemandeModel;
import ch.globaz.eform.business.models.GFFormulaireModel;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GFDemandeViewBean extends BJadePersistentObjectViewBean {
    private static final Logger LOG = LoggerFactory.getLogger(GFDemandeViewBean.class);

    public GFDemandeViewBean() {
        super();
    }

    public GFDemandeViewBean(GFDemandeModel demande) {
        super();
    }

    @Override
    public BSpy getSpy() {
        return null;
    }


    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public void update() throws Exception {
    }

    public BSession getSession() {
        return (BSession) getISession();
    }
}
