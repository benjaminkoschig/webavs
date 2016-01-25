package globaz.phenix.vb;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;

public abstract class CPAbstractPersistentObjectViewBean implements BIPersistentObject, FWViewBeanInterface {
    private String id = "";
    private String message = "";
    private String msgType = "";
    private BSession session = null;

    @Override
    public String getId() {
        return id;
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
    public void setId(String newId) {
        id = newId;
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

}
