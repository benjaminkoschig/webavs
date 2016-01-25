package globaz.pegasus.vb.process;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import java.util.HashMap;
import ch.globaz.jade.process.business.handler.JadeProcessAbstractViewBean;

public class PCAdaptationPCViewBean extends JadeProcessAbstractViewBean {

    @Override
    public String getKeyProcess() {
        return "Pegasus.AdaptationPC";
    }

    @Override
    public HashMap<String, String> getProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void retrieve() throws Exception {

    }

    private BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

}
