package globaz.osiris.vb;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

public abstract class CAAbstractPersistenceViewBean implements BIPersistentObject, FWViewBeanInterface {
    private String eMailAddress = "";
    private String id = "";
    private String message = "";
    private String msgType = "";
    private BSession session = null;

    /**
     * @return the eMailAddress
     */
    public String getEMailAddress() {
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

    /**
     * @return the session
     */
    public BSession getSession() {
        return session;
    }

    /**
     * @param eMailAddress
     *            the eMailAddress to set
     */
    public void setEMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
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

    /**
     * @param session
     *            the session to set
     */
    public void setSession(BSession session) {
        this.session = session;
    }

}
