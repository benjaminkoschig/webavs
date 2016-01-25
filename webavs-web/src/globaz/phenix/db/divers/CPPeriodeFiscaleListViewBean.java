package globaz.phenix.db.divers;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
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
    public String getAnneeDecision(int pos) {
        return ((CPPeriodeFiscale) getEntity(pos)).getAnneeDecisionDebut();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAnneeRevenuDebut(int pos) {
        return ((CPPeriodeFiscale) getEntity(pos)).getAnneeRevenuDebut();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
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
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdIfd(int pos) {
        return ((CPPeriodeFiscale) getEntity(pos)).getIdIfd();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getNumIfd(int pos) {
        return ((CPPeriodeFiscale) getEntity(pos)).getNumIfd();
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

    public void setColonneSelection(String value) {
        colonneSelection = value;
    }
}
