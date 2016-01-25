package globaz.corvus.vb;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;

/**
 * Implémente les fonctionnalités de base d'un viewBean (message, msgType et session)
 */
public class REAbstractViewBean implements FWViewBeanInterface {

    private String message;
    private String msgType;
    private BSession session;

    public REAbstractViewBean() {
        super();

        message = "";
        msgType = "";
        session = null;
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
