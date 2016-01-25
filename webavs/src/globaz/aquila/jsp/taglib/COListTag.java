package globaz.aquila.jsp.taglib;

import globaz.aquila.api.ICOApplication;
import globaz.aquila.util.COBeanUtils;
import globaz.framework.controller.FWController;
import globaz.fweb.taglib.FWListTag;
import globaz.globall.api.BIEntity;
import globaz.globall.api.BISession;
import globaz.globall.db.BApplication;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.log.JadeLogger;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * G�re l'affichage d'une liste d'options directement depuis un manager
 * 
 * @author Pascal Lovy, 18-oct-2004
 */
public abstract class COListTag extends FWListTag {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Le nom de la propri�t� qui contient le label */
    private String labelProperty = null;
    /** Le manager des entit�s � lister */
    private Object manager = null;
    /** Les param�tres du manager */
    private Map managerParameters = new Hashtable();
    /** Le nom de la propri�t� qui contient la valeur */
    private String valueProperty = null;
    /** TRUE si on affiche une s�l�ction vide */
    private boolean wantBlank = false;

    /**
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        // Evalue le contenu du tag
        return Tag.EVAL_BODY_INCLUDE;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getLabelProperty() {
        return labelProperty;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public Object getManager() {
        return manager;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public Map getManagerParameters() {
        return managerParameters;
    }

    /**
     * Tente de retourner la session courante ou une session anonyme
     * 
     * @return La session ou null si aucune session n'est trouv�e
     */
    public BISession getSession() {
        HttpSession httpSession = pageContext.getSession();
        BISession session = null;
        FWController controller = (FWController) httpSession.getAttribute("objController");
        try {
            if (controller != null) {
                session = controller.getSession();
                if (!session.isConnected()) {
                    session = ((BSession) session).getApplication().getAnonymousSession();
                    if (!session.isConnected()) {
                        session = ((BApplication) GlobazServer.getCurrentSystem().getApplication(
                                ICOApplication.DEFAULT_APPLICATION_AQUILA)).getAnonymousSession();
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
        return session;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public String getValueProperty() {
        return valueProperty;
    }

    /**
     * @return La valeur courante de la propri�t�
     */
    public boolean isWantBlank() {
        return wantBlank;
    }

    /**
     * Parcours tous les �l�ments du manager et cr�� la liste d'options
     * 
     * @see globaz.jsp.taglib.FWList#prepareListe()
     */
    @Override
    public void prepareListe() {
        try {
            if (manager != null) {
                BManager theManager = null;
                // Si on a re�u directement le manager on l'utilise
                if (BManager.class.isAssignableFrom(manager.getClass())) {
                    theManager = (BManager) manager;
                    // Si on a re�u le nom du manager on l'instancie
                } else if (String.class.isAssignableFrom(manager.getClass())) {
                    theManager = (BManager) Class.forName((String) manager).newInstance();
                } else {
                    throw new Exception("La propri�t� 'manager' ne supporte pas le type "
                            + manager.getClass().getName());
                }
                // Si le manager n'est pas charg�, on tente de le charger
                if (!theManager.isLoaded()) {
                    if (theManager.getSession() == null) {
                        theManager.setISession(getSession());
                    }
                    // On utilise les �ventuels param�tres
                    COBeanUtils.setAllProperties(theManager, managerParameters);
                    theManager.find();
                }

                Vector data = new Vector();
                // Affiche une s�l�ction vide si demand�
                if (wantBlank) {
                    String[] vide = { "", "" };
                    data.addElement(vide);
                }
                // Remplis la liste
                for (int i = 0; i < theManager.size(); i++) {
                    BIEntity entity = theManager.getEntity(i);
                    String[] element = new String[2];
                    element[0] = lancerMethode(entity, valueProperty);
                    element[1] = lancerMethode(entity, labelProperty);
                    data.addElement(element);
                }
                setData(data);
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        // Suite du traitement
        super.prepareListe();
    }

    /**
     * @param obj
     * @param nomMethode
     * @return
     * @throws Exception
     */
    String lancerMethode(Object obj, String nomMethode) throws Exception {
        Class[] paramTypes = null;
        Method m = obj.getClass().getMethod(nomMethode, paramTypes);
        return (String) m.invoke(obj);
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setLabelProperty(String string) {
        labelProperty = string;
    }

    /**
     * @param value
     *            La nouvelle valeur de la propri�t�
     */
    public void setManager(Object value) {
        manager = value;
    }

    /**
     * @param map
     *            La nouvelle valeur de la propri�t�
     */
    public void setManagerParameters(Map map) {
        managerParameters = map;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propri�t�
     */
    public void setValueProperty(String string) {
        valueProperty = string;
    }

    /**
     * @param b
     *            La nouvelle valeur de la propri�t�
     */
    public void setWantBlank(boolean b) {
        wantBlank = b;
    }

}
