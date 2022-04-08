package globaz.eform.vb.statistique;

import ch.globaz.eform.business.models.GFFormulaireModel;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GFStatistiqueViewBean extends BJadePersistentObjectViewBean {

    private static final Logger LOG = LoggerFactory.getLogger(GFStatistiqueViewBean.class);

    public GFStatistiqueViewBean() {
        super();
    }

    public GFStatistiqueViewBean(GFFormulaireModel formulaire) {
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
