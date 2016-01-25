package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.05.2002 09:35:05)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.03.2003 12:49:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdDonneesCalcul(int pos) {
        return ((CPDonneesCalcul) getEntity(pos)).getIdDonneesCalcul();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getMontantCalcul(int pos) {
        return ((CPDonneesCalcul) getEntity(pos)).getMontant();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (14.03.2003 12:49:07)
     * 
     * @param newIdDecision
     *            java.lang.String
     */
    public void setIdDecision(java.lang.String newIdDecision) {
        idDecision = newIdDecision;
    }
}
