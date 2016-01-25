package globaz.perseus.vb.process;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import java.util.HashMap;
import ch.globaz.jade.process.business.handler.JadeProcessAbstractViewBean;

public class PFTraitementAdaptationViewBean extends JadeProcessAbstractViewBean {

    @Override
    public String getKeyProcess() {
        return "Perseus.TraitementAdaptationPF";
    }

    @Override
    public HashMap<String, String> getProperties() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {

    }

    private BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

}
