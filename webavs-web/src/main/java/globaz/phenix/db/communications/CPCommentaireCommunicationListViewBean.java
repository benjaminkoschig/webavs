package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPCommentaireCommunicationListViewBean extends CPCommentaireCommunicationManager implements
        FWViewBeanInterface {
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
    public String getCodeCommentaire(int pos) {
        try {
            return globaz.phenix.translation.CodeSystem.getCode(getSession(),
                    ((CPCommentaireCommunication) getEntity(pos)).getIdCommentaire());
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdCommentaireCf(int pos) {
        return ((CPCommentaireCommunication) getEntity(pos)).getIdCommentaireCf();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getLibelleCommentaite(int pos) {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(),
                    ((CPCommentaireCommunication) getEntity(pos)).getIdCommentaire());
        } catch (Exception e) {
            getSession().addError(e.getMessage());
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
