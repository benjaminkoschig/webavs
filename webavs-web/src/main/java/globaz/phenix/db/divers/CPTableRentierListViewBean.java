package globaz.phenix.db.divers;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Ins?rez la description du type ici. Date de cr?ation : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPTableRentierListViewBean extends CPTableRentierManager implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAnneeRentier(int pos) {
        return ((CPTableRentier) getEntity(pos)).getAnneeRentier();
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getCotisationAnnuelle(int pos) {
        return ((CPTableRentier) getEntity(pos)).getCotisationAnnuelle();
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdTableRentier(int pos) {
        return ((CPTableRentier) getEntity(pos)).getIdTableRentier();
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getRevenuRentier(int pos) {
        return ((CPTableRentier) getEntity(pos)).getRevenuRentier();
    }

    /**
     * Ins?rez la description de la m?thode ici. Date de cr?ation : (03.05.2002 16:20:01)
     * 
     * @param action
     *            java.lang.String
     */
    public void setAction(String action) {
        this.action = action;
    }
}
