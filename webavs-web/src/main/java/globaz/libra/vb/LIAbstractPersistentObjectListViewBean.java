package globaz.libra.vb;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BIPersistentObjectList;
import java.util.Iterator;

/**
 * 
 * @author hpe
 */
public abstract class LIAbstractPersistentObjectListViewBean implements BIPersistentObjectList, FWListViewBeanInterface {
    /** Message dans le cas de problèmes */
    private String message = new String();
    /** Type de message (ERROR, WARNING, INFO) */
    private String msgType = new String();
    /** Session courante */
    private BISession session = null;

    /**
     * Constructor for AIAbstractListViewBean
     */
    public LIAbstractPersistentObjectListViewBean() {
        super();
    }

    public abstract boolean canDoNext();

    public abstract boolean canDoPrev();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public abstract void find() throws Exception;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#findNext()
     */
    @Override
    public abstract void findNext() throws Exception;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#findPrev()
     */
    @Override
    public abstract void findPrev() throws Exception;

    public abstract int getCount() throws Exception;

    public abstract BIPersistentObject getEntity(int idx);

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#getISession()
     */
    @Override
    public BISession getISession() {
        return session;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#getMessage()
     */
    @Override
    public String getMessage() {
        return message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#getMsgType()
     */
    @Override
    public String getMsgType() {
        return msgType;
    }

    public abstract int getOffset();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWListViewBeanInterface#getSize()
     */
    @Override
    public abstract int getSize();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#iterator()
     */
    @Override
    public abstract Iterator iterator();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#setISession(globaz.globall. api.BISession)
     */
    @Override
    public void setISession(BISession newSession) {
        session = newSession;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#setMessage(java.lang.String)
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWViewBeanInterface#setMsgType(java.lang.String)
     */
    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#size()
     */
    @Override
    public int size() {
        return getSize();
    }
}
