package globaz.phenix.db.divers;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPTableNonActifListViewBean extends CPTableNonActifManager implements FWViewBeanInterface {
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
    public String getAnneeVigueur(int pos) {
        return ((CPTableNonActif) getEntity(pos)).getAnneeVigueur();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getCotisationAnnuelle(int pos) {
        return ((CPTableNonActif) getEntity(pos)).getCotisationAnnuelle();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getCotisationMensuelle(int pos) {
        return ((CPTableNonActif) getEntity(pos)).getCotisationMensuelle();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getIdTableNonActif(int pos) {
        return ((CPTableNonActif) getEntity(pos)).getIdTableNac();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getRevenu(int pos) {
        return ((CPTableNonActif) getEntity(pos)).getRevenu();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getRevenuCi(int pos) {
        return ((CPTableNonActif) getEntity(pos)).getRevenuCi();
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
