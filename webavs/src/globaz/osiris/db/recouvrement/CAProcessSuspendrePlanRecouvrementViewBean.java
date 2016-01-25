package globaz.osiris.db.recouvrement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import java.rmi.RemoteException;

/**
 * <H1>Description</H1>
 * <p>
 * ViewBean pour l'écran de confirmation de la suspension d'un plan de paiement.
 * </p>
 * 
 * @author vre
 */
public class CAProcessSuspendrePlanRecouvrementViewBean implements FWViewBeanInterface, BIPersistentObject {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateSuspension = JACalendar.todayJJsMMsAAAA();
    private String email = "";
    private String idPlanRecouvrement = "";

    private StringBuffer message = new StringBuffer();
    private String msgType = FWViewBeanInterface.OK;
    private Boolean sendSommation = new Boolean(true);

    // lié à l'implémentation des interfaces
    private BISession session;

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
     * getter pour l'attribut date suspension.
     * 
     * @return la valeur courante de l'attribut date suspension
     */
    public String getDateSuspension() {
        return dateSuspension;
    }

    /**
     * getter pour l'attribut email.
     * 
     * @return la valeur courante de l'attribut email
     */
    public String getEmail() {
        if ((session != null) && JadeStringUtil.isEmpty(email)) {
            try {
                email = session.getUserEMail();
            } catch (RemoteException e) {
                setMessage(e.getMessage());
                setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        return email;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return idPlanRecouvrement;
    }

    /**
     * getter pour l'attribut id plan recouvrement.
     * 
     * @return la valeur courante de l'attribut id plan recouvrement
     */
    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
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
     * @return the sendSommation
     */
    public Boolean getSendSommation() {
        return sendSommation;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
    }

    /**
     * setter pour l'attribut date suspension.
     * 
     * @param dateSuspension
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateSuspension(String dateSuspension) {
        this.dateSuspension = dateSuspension;
    }

    /**
     * setter pour l'attribut email.
     * 
     * @param email
     *            une nouvelle valeur pour cet attribut
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        idPlanRecouvrement = newId;
    }

    /**
     * setter pour l'attribut id plan recouvrement.
     * 
     * @param idPlanRecouvrement
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPlanRecouvrement(String idPlanRecouvrement) {
        this.idPlanRecouvrement = idPlanRecouvrement;
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
     * @param sendSommation
     *            the sendSommation to set
     */
    public void setSendSommation(Boolean sendSommation) {
        this.sendSommation = sendSommation;
    }

    /**
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
    }
}
