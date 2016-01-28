package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.log.JadeLogger;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPRemarqueDecisionListViewBean extends CPRemarqueDecisionManager implements FWViewBeanInterface {
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
                    ((CPRemarqueDecision) getEntity(pos)).getEmplacement());
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
    public String getIdRemarqueDecision(int pos) {
        return ((CPRemarqueDecision) getEntity(pos)).getIdRemarqueDecision();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getTexteRemarque(int pos) {
        return ((CPRemarqueDecision) getEntity(pos)).getTexteRemarqueDecision();
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
