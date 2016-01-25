package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.log.JadeLogger;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPRemarqueTypeListViewBean extends CPRemarqueTypeManager implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getEmplacement(int pos) {
        try {
            return globaz.phenix.translation.CodeSystem.getCode(getSession(),
                    ((CPRemarqueType) getEntity(pos)).getEmplacement());
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();

            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdRemarqueType(int pos) {
        return ((CPRemarqueType) getEntity(pos)).getIdRemarqueType();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getLangue(int pos) {
        try {
            return globaz.phenix.translation.CodeSystem.getCode(getSession(),
                    ((CPRemarqueType) getEntity(pos)).getLangue());
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getTexteRemarqueType(int pos) {
        return ((CPRemarqueType) getEntity(pos)).getTexteRemarqueType();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
        this.action = action;
    }
}
