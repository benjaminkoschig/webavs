package globaz.libra.vb;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSpy;

public abstract class LIAbstractPersistentObjectViewBean implements FWViewBeanInterface, BIPersistentObject {
    /** Représente les messages gérés par le framework */
    private String message = "";
    /** Représente les types de messages gérés par le framework */
    private String msgType = "";
    /** Session courante */
    private BISession session = null;

    /**
     * Constructeur de l'object persistant
     */
    public LIAbstractPersistentObjectViewBean() {
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
     * Méthode permettant de récupérer le spy de l'entité qui fait fois
     * 
     * @return Le spy de l'entité qui fait fois
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
