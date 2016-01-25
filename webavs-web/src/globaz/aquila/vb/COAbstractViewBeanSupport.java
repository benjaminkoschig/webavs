/*
 * Créé le 8 févr. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.aquila.vb;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import java.io.Serializable;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class COAbstractViewBeanSupport implements FWViewBeanInterface, BIPersistentObject, Serializable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id = "";
    private StringBuffer message = new StringBuffer();
    private String msgType = FWViewBeanInterface.OK;
    private BISession session;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe COAbstractViewBeanSupport.
     */
    public COAbstractViewBeanSupport() {
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
    }

    /**
     * @param message
     *            DOCUMENT ME!
     */
    protected void _addError(String message) {
        this.message.append(message);
        this.message.append("\n");
        msgType = FWViewBeanInterface.ERROR;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#getId()
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
        return message.toString();
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#getMsgType()
     */
    @Override
    public String getMsgType() {
        return msgType;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        id = newId;
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
        this.message.setLength(0);
        this.message.append(message);
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
