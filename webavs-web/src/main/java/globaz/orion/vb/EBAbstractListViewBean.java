package globaz.orion.vb;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BSession;
import java.util.Iterator;

public abstract class EBAbstractListViewBean implements BIPersistentObjectList, FWListViewBeanInterface {
    private String message = null;
    private String msgType = null;
    private BSession session = null;

    public boolean canDoNext() {
        return false;
    }

    public boolean canDoPrev() {
        return false;
    }

    @Override
    public void findNext() throws Exception {
        throw new Exception("Method not implemented!");
    }

    @Override
    public void findPrev() throws Exception {
        throw new Exception("Method not implemented!");
    }

    public int getCount() {
        return getSize();
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

    public final int getOffset() {
        return 0;
    }

    @Override
    public Iterator iterator() {
        return null;
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

    @Override
    public int size() {
        return getSize();
    }
}
