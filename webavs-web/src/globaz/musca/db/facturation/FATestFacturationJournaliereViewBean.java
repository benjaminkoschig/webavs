package globaz.musca.db.facturation;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * @author MMO
 * @since 01 octbore 2010
 */
public class FATestFacturationJournaliereViewBean implements BIPersistentObject, FWViewBeanInterface {

    private String email = "";
    private String message = "";
    private String messageType = FWViewBeanInterface.OK;
    private BISession session;

    /**
     * Constructeur de CEEmployeurChangementMasseSalarialeViewBean
     */
    public FATestFacturationJournaliereViewBean() throws Exception {
    }

    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete() throws Exception {
        // TODO Auto-generated method stub

    }

    public String getEmail() {
        if (JadeStringUtil.isBlank(email)) {
            email = getSession().getUserEMail();
        }
        return email;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BISession getISession() {
        return session;
    }

    /**
     * GETTER
     */

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMsgType() {
        return messageType;
    }

    public BSession getSession() {
        return (BSession) session;
    }

    // appelé lors de la pression du bouton "traiter"
    @Override
    public void retrieve() throws Exception {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setISession(BISession newSession) {
        session = newSession;
    }

    /**
     * SETTER
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setMsgType(String msgType) {
        messageType = msgType;
    }

    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
