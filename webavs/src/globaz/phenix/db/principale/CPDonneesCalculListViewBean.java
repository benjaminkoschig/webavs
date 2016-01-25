package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPDonneesCalculListViewBean extends CPDonneesCalculManager implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private java.lang.String idDecision = "";;

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2003 12:49:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdDonneesCalcul(int pos) {
        return ((CPDonneesCalcul) getEntity(pos)).getIdDonneesCalcul();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getLibelleDonnee(int pos) {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(),
                    ((CPDonneesCalcul) getEntity(pos)).getIdDonneesCalcul());
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
    public String getMontantCalcul(int pos) {
        return ((CPDonneesCalcul) getEntity(pos)).getMontant();
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

    /**
     * Insérez la description de la méthode ici. Date de création : (14.03.2003 12:49:07)
     * 
     * @param newIdDecision
     *            java.lang.String
     */
    public void setIdDecision(java.lang.String newIdDecision) {
        idDecision = newIdDecision;
    }
}
