package globaz.hercule.db;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <H1>Description</H1>
 * <p>
 * Classe legere pour la recup d'informations des ecrans notamment pour eviter de stocker un process en session.
 * </p>
 * 
 * @author vre
 * @since Créé le 22 déc. 05
 * @revision SCO 8 juin 2010
 */
public abstract class CEAbstractViewBean implements BIPersistentObject, FWViewBeanInterface {

    private String email;
    private String id;
    private String message;
    private String msgType;
    private BISession session;

    /**
     * Crée une nouvelle instance de la classe AFAbstractViewBean.
     */
    public CEAbstractViewBean() {
        super();
        msgType = FWViewBeanInterface.OK;
        message = "";
        id = "";
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }

        return email;
    }

    /**
     * getter pour l'attribut id
     * 
     * @return la valeur courante de l'attribut id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getISession()
     */
    @Override
    public BISession getISession() {
        return session;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getMessage()
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getMsgType()
     */
    @Override
    public String getMsgType() {
        return msgType;
    }

    /**
     * getter pour l'attribut session
     * 
     * @return la valeur courante de l'attribut session
     */
    public BSession getSession() {
        return (BSession) session;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setISession(globaz.globall.api.BISession)
     */
    @Override
    public void setISession(BISession newSession) {
        session = newSession;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setMessage(java.lang.String)
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setMsgType(java.lang.String)
     */
    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
    }
}
