package globaz.orion.vb;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import java.util.Iterator;

public abstract class EBAbstractListViewBeanPagination implements BIPersistentObjectList, FWListViewBeanInterface {
    private String message = null;
    private String msgType = null;
    private BSession session = null;

    public abstract BManager getManager();

    public boolean canDoNext() {
        return getManager().canDoNext();
    }

    public boolean canDoPrev() {
        return getManager().canDoPrev();
    }

    @Override
    public void findNext() throws Exception {
        getManager().findNext();
    }

    @Override
    public void findPrev() throws Exception {
        getManager().findPrev();
    }

    @Override
    public int size() {
        return getManager().getSize();
    }

    public int getCount() {
        try {
            return getManager().getCount();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        return getManager().getOffset();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Iterator iterator() {
        return getManager().iterator();
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

}
