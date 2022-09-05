package globaz.eform.vb.suivi;

import ch.globaz.eform.business.models.GFDaDossierModel;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GFSuiviViewBean extends BJadePersistentObjectViewBean {
    private static final Logger LOG = LoggerFactory.getLogger(GFSuiviViewBean.class);

    GFDaDossierModel suivi;

    public GFDaDossierModel getSuivi() {
        return suivi;
    }

    @Override
    public BSpy getSpy() {
        return (suivi != null) && !suivi.isNew() ? new BSpy(suivi.getSpy()) : new BSpy(getSession());
    }

    @Override
    public void add() throws Exception {

    }

    @Override
    public void delete() throws Exception {

    }

    @Override
    public String getId() {
        return suivi.getId();
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
