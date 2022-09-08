package globaz.eform.vb.suivi;

import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.models.GFFormulaireModel;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GFSuiviViewBean extends BJadePersistentObjectViewBean {
    private static final Logger LOG = LoggerFactory.getLogger(GFSuiviViewBean.class);

    GFDaDossierModel daDossier;

    public GFSuiviViewBean() {
        super();
        daDossier = new GFDaDossierModel();
    }

    public GFSuiviViewBean(GFDaDossierModel daDossier) {
        super();
        this.daDossier = daDossier;
    }

    @Override
    public String getId() {
        return daDossier.getId();
    }

    @Override
    public void setId(String newId) {

    }

    public GFDaDossierModel getDaDossier() {
        return daDossier;
    }

    @Override
    public BSpy getSpy() {
        return (daDossier != null) && !daDossier.isNew() ? new BSpy(daDossier.getSpy()) : new BSpy(getSession());
    }

    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public void add() throws Exception {

    }

    @Override
    public void delete() throws Exception {

    }

    @Override
    public void retrieve() throws Exception {

    }

    @Override
    public void update() throws Exception {

    }
}
