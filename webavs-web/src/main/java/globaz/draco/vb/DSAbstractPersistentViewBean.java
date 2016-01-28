package globaz.draco.vb;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

public abstract class DSAbstractPersistentViewBean implements BIPersistentObject, FWViewBeanInterface {

    private String eMailAddress = "";
    private String id = "";
    private String message = "";
    private String msgType = null;
    private BSession session = null;

    @Override
    public void add() throws Exception {
        throw new Exception("NOT IMPLEMENTED!");
    }

    @Override
    public void delete() throws Exception {
        throw new Exception("NOT IMPLEMENTED!");
    }

    public String getEmailAddress() {
        return eMailAddress;
    }

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
    public void retrieve() throws Exception {
    }

    public void setEMailAddress(String eMailAdress) {
        eMailAddress = eMailAdress;
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    @Override
    public void setISession(BISession newSession) {
        session = (BSession) newSession;
        if (JadeStringUtil.isBlankOrZero(eMailAddress)) {
            setEMailAddress(session.getUserEMail());
        }
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
        setSession(session);

    }

    @Override
    public void update() throws Exception {
        throw new Exception("NOT IMPLEMENTED!");
    }
}
