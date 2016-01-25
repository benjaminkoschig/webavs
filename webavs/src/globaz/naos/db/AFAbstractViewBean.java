/*
 * Créé le 22 déc. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.naos.db;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe legere pour la recup d'informations des ecrans notamment pour eviter de stocker un process en session.
 * </p>
 * 
 * @author vre
 */
public abstract class AFAbstractViewBean implements BIPersistentObject, FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // champs necessaires a l'implementation des interfaces
    private String message = "";
    private String msgType = FWViewBeanInterface.OK;
    private BISession session;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAbstractViewBean.
     */
    public AFAbstractViewBean() {
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
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    /**
     * getter pour l'attribut id
     * 
     * @return la valeur courante de l'attribut id
     */
    @Override
    public String getId() {
        return null;
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

    /**
     * setter pour l'attribut id
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setId(String string) {
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
     * setter pour l'attribut session
     * 
     * @param session
     *            une nouvelle valeur pour cet attribut
     */
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
    }
}
