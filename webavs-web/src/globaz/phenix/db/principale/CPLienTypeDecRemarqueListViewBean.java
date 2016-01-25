package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.log.JadeLogger;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPLienTypeDecRemarqueListViewBean extends CPLienTypeDecRemarqueManager implements FWViewBeanInterface {
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
    public String getCodeTypeDecision(int pos) {
        try {
            return globaz.phenix.translation.CodeSystem.getCode(getSession(),
                    ((CPLienTypeDecRemarque) getEntity(pos)).getTypeDecision());
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdLienTypeRemarque(int pos) {
        return ((CPLienTypeDecRemarque) getEntity(pos)).getIdLienTypeRemarque();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getLibelleTypeDecision(int pos) {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(),
                    ((CPLienTypeDecRemarque) getEntity(pos)).getTypeDecision());
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
    }

    /**
     * Retourne le texte d'une remarque Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getTexteRemarqueType(int pos) {
        try {
            CPRemarqueType rem = new CPRemarqueType();
            rem.setSession(getSession());
            return rem.getTexteRemarqueType(((CPLienTypeDecRemarque) getEntity(pos)).getIdRemarqueType());
        } catch (Exception e) {
            JadeLogger.error(this, e);
            return "";
        }
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
