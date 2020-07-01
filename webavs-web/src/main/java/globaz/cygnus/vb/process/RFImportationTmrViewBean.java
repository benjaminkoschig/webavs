package globaz.cygnus.vb.process;

import ch.globaz.jade.process.business.handler.JadeProcessAbstractViewBean;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;

import java.util.HashMap;

public class RFImportationTmrViewBean extends JadeProcessAbstractViewBean {

    @Override
    public String getKeyProcess() {
        return "Cygnus.import.tmr";
    }

    @Override
    public HashMap<String, String> getProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    public BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    @Override
    public void retrieve() throws Exception {
    }
}
