package globaz.phenix.db.principale;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class CPLienTypeDecRemarqueViewBean extends CPLienTypeDecRemarque implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String action;
    private String texteRemarqueType;

    public void _chargerEntete() throws Exception {
        // --- Initialisation des zones
        // -------- Recherche des donn�es de la remarque type --------
        CPRemarqueType rema = new CPRemarqueType();
        rema.setSession(getSession());
        rema.setIdRemarqueType(getIdRemarqueType());
        try {
            rema.retrieve();
            if (!rema.isNew()) {
                setTexteRemarqueType(rema.getTexteRemarqueType());
            } else {
                setTexteRemarqueType("");
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            setTexteRemarqueType("");
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.05.2002 16:18:35)
     * 
     * @return java.lang.String
     */
    public String getAction() {
        return (action);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.02.2003 16:33:49)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTexteRemarqueType() {
        return texteRemarqueType;
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
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (27.02.2003 16:33:49)
     * 
     * @param newTexteRemarqueType
     *            java.lang.String
     */
    public void setTexteRemarqueType(java.lang.String newTexteRemarqueType) {
        texteRemarqueType = newTexteRemarqueType;
    }
}
