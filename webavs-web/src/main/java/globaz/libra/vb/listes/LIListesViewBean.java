/*
 * Crée le 24 octobre 2006
 */
package globaz.libra.vb.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Ce viewBean n'étends pas un BEntity, mais un BIPersistentObject car les données ne sont pas mappées avec une table...
 * 
 * @author hpe
 * 
 */
public class LIListesViewBean implements FWViewBeanInterface, BIPersistentObject {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String id = "";
    private String message = "";
    private String msgType = FWViewBeanInterface.OK;
    private Map Parametres = new HashMap();
    private BISession session;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param errorLabel
     *            DOCUMENT ME!
     */
    public void _addError(String errorLabel) {
        BSession bSession = (BSession) session;

        bSession.addError(bSession.getLabel(errorLabel));
        setMsgType(FWViewBeanInterface.ERROR);
        message += bSession.getErrors().toString() + "\n\n";
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        throw new UnsupportedOperationException(
                "Impossible d'effectuer cette opération sur une instance de LIListesViewBean");
    }

    /**
     * @param errorMessage
     *            DOCUMENT ME!
     */
    public void addErrorAvecMessagePret(String errorMessage) {
        BSession bSession = (BSession) session;

        bSession.addError(errorMessage);
        setMsgType(FWViewBeanInterface.ERROR);
        message += bSession.getErrors().toString() + "\n\n";
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        throw new UnsupportedOperationException(
                "Impossible d'effectuer cette opération sur une instance de LIListesViewBean");
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
     * getter de l'attribut Parametres
     * 
     * @return Parametres
     */
    public Map getParametres() {
        return Parametres;
    }

    /**
     * getter pour l'attribut session
     * 
     * @return Un objet de type BSession. Peut être null si la BISession du viewBean ne peut pas être converti en
     *         BSession
     */
    public BSession getSession() {
        try {
            BSession bSession = (BSession) session;

            return bSession;
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        throw new UnsupportedOperationException(
                "Impossible d'effectuer cette opération sur une instance de LIListesViewBean");
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
     * setter de l'attribut Parametres
     * 
     * @param Parametres
     */
    public void setParametres(Map parametres) {
        Parametres = parametres;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        throw new UnsupportedOperationException(
                "Impossible d'effectuer cette opération sur une instance de LIListesViewBean");
    }

}
