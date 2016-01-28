/*
 * Créé le 27 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.prestation.vb;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une classe abstraite qui implément FWViewBeanInterface. Utile pour créer des écrans simples, non liés à la base comme
 * par exemple les écrans des process.
 * </p>
 * 
 * @author vre
 */
public abstract class PRAbstractViewBeanSupport implements FWViewBeanInterface, BIPersistentObject {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String action = "";
    private String id = "";
    private String message = "";
    private String msgType = FWViewBeanInterface.OK;
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
                "Impossible d'effectuer cette opération sur une instance de PRAbstractViewBeanSupport");
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
                "Impossible d'effectuer cette opération sur une instance de PRAbstractViewBeanSupport");
    }

    /**
     * getter pour l'attribut action
     * 
     * @return la valeur courante de l'attribut action
     */
    public String getAction() {
        return action;
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
     * Renvoie le champ espion de l'entité.
     * 
     * @return le champ espion
     */
    public BSpy getSpy() {
        return null;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        throw new UnsupportedOperationException(
                "Impossible d'effectuer cette opération sur une instance de PRAbstractViewBeanSupport");
    }

    /**
     * setter pour l'attribut action
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setAction(String string) {
        action = string;
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
        throw new UnsupportedOperationException(
                "Impossible d'effectuer cette opération sur une instance de PRAbstractViewBeanSupport");
    }

    /**
     * Valide les données de ce viewBean.
     * 
     * @return vrai si les données de ce viewBean sont valides.
     */
    public abstract boolean validate();

}
