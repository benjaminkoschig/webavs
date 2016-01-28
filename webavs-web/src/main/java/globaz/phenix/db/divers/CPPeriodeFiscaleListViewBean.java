package globaz.phenix.db.divers;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPPeriodeFiscaleListViewBean extends CPPeriodeFiscaleManager implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private String colonneSelection = "";

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAnneeDecision(int pos) {
        return ((CPPeriodeFiscale) getEntity(pos)).getAnneeDecisionDebut();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAnneeRevenuDebut(int pos) {
        return ((CPPeriodeFiscale) getEntity(pos)).getAnneeRevenuDebut();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAnneeRevenuFin(int pos) {
        return ((CPPeriodeFiscale) getEntity(pos)).getAnneeRevenuFin();
    }

    public String getColonneSelection() {
        return colonneSelection;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdIfd(int pos) {
        return ((CPPeriodeFiscale) getEntity(pos)).getIdIfd();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getNumIfd(int pos) {
        return ((CPPeriodeFiscale) getEntity(pos)).getNumIfd();
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

    public void setColonneSelection(String value) {
        colonneSelection = value;
    }
}
