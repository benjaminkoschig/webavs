package globaz.phenix.db.divers;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.log.JadeLogger;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPTableFortuneListViewBean extends CPTableFortuneManager implements FWViewBeanInterface {
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
    public String getAnneeFortune(int pos) {
        return ((CPTableFortune) getEntity(pos)).getAnneeFortune();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getCanton(int pos) {
        try {
            return globaz.phenix.translation.CodeSystem.getLibelle(getSession(),
                    ((CPTableFortune) getEntity(pos)).getCanton());
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdTableFortune(int pos) {
        return ((CPTableFortune) getEntity(pos)).getIdTableFortune();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getTauxAgricole(int pos) {
        return ((CPTableFortune) getEntity(pos)).getTauxAgricole();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getTauxNonAgricole(int pos) {
        return ((CPTableFortune) getEntity(pos)).getTauxNonAgricole();
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
