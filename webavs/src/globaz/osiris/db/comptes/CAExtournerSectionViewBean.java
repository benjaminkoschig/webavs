package globaz.osiris.db.comptes;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;

public class CAExtournerSectionViewBean implements globaz.framework.bean.FWViewBeanInterface, java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String comment = new String();
    private java.lang.String idSection = new String();
    private String message = null;
    private String msgType = "";
    private CASection section = null;
    private BSession session = null;

    public CAExtournerSectionViewBean() {
        super();
    }

    /**
     * Returns the comment.
     * 
     * @return String
     */
    public String getComment() {
        return comment;
    }

    /**
     * Returns the description.
     * 
     * @return String
     */
    public String getDescription() {

        try {
            return getSection().getIdExterne() + " - " + getSection().getDescription();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the idSection.
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdSection() {
        return idSection;
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
     * Renvoie le type de message contenu dans le bean (utilisé seulement si l'application utilise des View Beans)
     * 
     * @return le type de message
     */
    @Override
    public final String getMsgType() {
        return msgType;
    }

    public CASection getSection() throws Exception {
        // Si pas déjà chargé
        if (section == null) {
            section = new CASection();
            section.setSession((BSession) getISession());
            section.setIdSection(getIdSection());
            section.retrieve();
        }
        return section;
    }

    /**
     * Sets the comment.
     * 
     * @param comment
     *            The comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setId(java.lang.String idOperation) {
        setIdSection(idOperation);
    }

    /**
     * Sets the idSection.
     * 
     * @param idSection
     *            The idSection to set
     */
    public void setIdSection(java.lang.String idSection) {
        this.idSection = idSection;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setISession(BISession)
     */
    @Override
    public void setISession(BISession newSession) {
        if (newSession instanceof BSession) {
            setSession((BSession) newSession);
        } else {
            try {
                setSession(new BSession(newSession.getApplicationId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Définit le message d'erreur du bean (utilisé seulement si l'application utilise des View Beans)
     * 
     * @param newMessage
     *            le nouveau message d'erreur
     */
    @Override
    public final void setMessage(String newMessage) {
        message = newMessage;
    }

    /**
     * Définit le type de message du bean (utilisé seulement si l'application utilise des View Beans)
     * 
     * @param newMessage
     *            le nouveau type de message
     */
    @Override
    public final void setMsgType(String newMsgType) {
        msgType = newMsgType;
    }

    /**
     * Modifie la session en cours
     * 
     * @param newSession
     *            la nouvelle session
     */
    public void setSession(BSession newSession) {
        session = newSession;
    }

}
