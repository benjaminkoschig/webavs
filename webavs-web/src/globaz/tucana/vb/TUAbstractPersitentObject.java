package globaz.tucana.vb;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSpy;

/**
 * Classe abstraite permettant d'implémenter des classe viewBean non rattachées à un fichier DB
 * 
 * @author fgo date de création : 5 juil. 06
 * @version : version 1.0
 * 
 */
public abstract class TUAbstractPersitentObject implements BIPersistentObject, FWViewBeanInterface {
    private String id = "";
    private String message = "";
    private String msgType = "";
    private BISession session = null;

    /**
	 * 
	 */
    public TUAbstractPersitentObject() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public abstract void add() throws Exception;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public abstract void delete() throws Exception;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public abstract String getId();

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

    /**
     * Récupère un BSpy
     * 
     * @return
     */
    public abstract BSpy getSpy();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public abstract void retrieve() throws Exception;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public abstract void setId(String newId);

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
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public abstract void update() throws Exception;
}
