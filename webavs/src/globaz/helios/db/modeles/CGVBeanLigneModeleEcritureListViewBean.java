package globaz.helios.db.modeles;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import java.util.ArrayList;
import java.util.List;

public class CGVBeanLigneModeleEcritureListViewBean implements FWListViewBeanInterface {

    private List entities = null;
    private String message = "";
    private String msgType = "";

    private BSession session = null;

    /**
     * Commentaire relatif au constructeur CGLigneModeleEcritureManager.
     */
    public CGVBeanLigneModeleEcritureListViewBean() {
        super();
        entities = new ArrayList();

    }

    public void addEntity(Object obj) {
        entities.add(obj);
    }

    public boolean canDoNext() {
        return false;
    }

    public boolean canDoPrev() {
        return false;
    }

    public int getCount() {
        return -1;
    }

    public Object getEntity(int idx) {
        return entities.get(idx);
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

    public int getOffset() {
        return -1;
    }

    /**
     * @see globaz.framework.bean.FWListViewBeanInterface#getSize()
     */
    @Override
    public int getSize() {
        return entities.size();
    }

    public void removeEntity(int idx) {
        entities.remove(idx);
    }

    public void removeEntity(Object entity) {
        entities.remove(entity);
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
     * @see globaz.framework.bean.FWViewBeanInterface#setMessage(String)
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @see globaz.framework.bean.FWViewBeanInterface#setMsgType(String)
     */
    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
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

    public int size() {
        return 0;
    }

    public void updateEntity(Object entity) {
        if (entities.contains(entity)) {
            entities.remove(entity);
        }
        entities.add(entity);
    }

}
