package globaz.phenix.db.divers;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.phenix.translation.CodeSystem;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPRevenuCotisationCantonListViewBean extends CPRevenuCotisationCantonManager implements
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;

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
    public String getAnneeDebut(int pos) {
        return ((CPRevenuCotisationCanton) getEntity(pos)).getAnneeDebut();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public Boolean getAvecCotisation(int pos) {
        return ((CPRevenuCotisationCanton) getEntity(pos)).getAvecCotisation();
    }

    public String getDateActivite(int pos) {
        return ((CPRevenuCotisationCanton) getEntity(pos)).getDateActivite();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdRevCotiCanton(int pos) {
        return ((CPRevenuCotisationCanton) getEntity(pos)).getIdRevCotiCanton();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getLibelleCanton(int pos) {
        String lib = ((CPRevenuCotisationCanton) getEntity(pos)).getCanton();
        try {
            return CodeSystem.getLibelle(getSession(), lib);
        } catch (Exception e) {
            return "";
        }
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
}
