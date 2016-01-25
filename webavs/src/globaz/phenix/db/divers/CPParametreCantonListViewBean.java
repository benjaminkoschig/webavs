package globaz.phenix.db.divers;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.phenix.translation.CodeSystem;

/**
 * Insérez la description du type ici. Date de création : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPParametreCantonListViewBean extends CPParametreCantonManager implements FWViewBeanInterface {
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

    public String getDateDebut(int pos) {
        try {
            return ((CPParametreCanton) getEntity(pos)).getDateDebut();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdParametreCanton(int pos) {
        return ((CPParametreCanton) getEntity(pos)).getIdParametreCanton();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getLibelleCanton(int pos) {
        String lib = ((CPParametreCanton) getEntity(pos)).getCanton();
        try {
            return CodeSystem.getLibelle(getSession(), lib);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getLibelleCodeParametre(int pos) {
        String lib = ((CPParametreCanton) getEntity(pos)).getCodeParametre();
        try {
            return CodeSystem.getLibelle(getSession(), lib);
        } catch (Exception e) {
            return "";
        }
    }

    public String getLibelleGenreAffilie(int pos) {
        String lib = ((CPParametreCanton) getEntity(pos)).getGenreAffilie();
        try {
            return CodeSystem.getLibelle(getSession(), lib);
        } catch (Exception e) {
            return "";
        }
    }

    public String getLibelleTypeParametre(int pos) {
        String lib = ((CPParametreCanton) getEntity(pos)).getTypeParametre();
        try {
            return CodeSystem.getLibelle(getSession(), lib);
        } catch (Exception e) {
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
