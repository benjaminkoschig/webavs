package globaz.orion.vb;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;

public abstract class EBAbstractViewBean implements BIPersistentObject, FWViewBeanInterface {
    private String message = null;
    private String msgType = null;
    private BSession session = null;

    @Override
    public void add() throws Exception {
        // nothing
    }

    @Override
    public void delete() throws Exception {
        // nothing
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public BISession getISession() {
        return session;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    public BSession getSession() {
        return session;
    }

    @Override
    public void retrieve() throws Exception {
        // nothing
    }

    @Override
    public void setId(String newId) {
        // nothing
    }

    @Override
    public void setISession(BISession newSession) {
        session = (BSession) newSession;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    @Override
    public void update() throws Exception {
        // nothing
    }
}
