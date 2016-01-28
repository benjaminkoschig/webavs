package globaz.phenix.process.acompte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;

/**
 * Insérez la description du type ici. Date de création : (21.01.2003 08:24:32)
 * 
 * @author: Bryan Tchang
 */
public class CPAcompteSuppressionViewBean extends CPProcessAcompteSupprimer implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String libellePassage = "";

    public CPAcompteSuppressionViewBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public CPAcompteSuppressionViewBean(BProcess parent) {
        super(parent);
        // TODO Auto-generated constructor stub
    }

    public CPAcompteSuppressionViewBean(BSession session) {
        super(session);
        // TODO Auto-generated constructor stub
    }

    /*
     * Initilaisation de l'écran Recherche du passage en cours
     */
    public void _init() {

    }

    /**
     * Returns the libellePassage.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibellePassage() {
        return libellePassage;
    }

    /**
     * Sets the libellePassage.
     * 
     * @param libellePassage
     *            The libellePassage to set
     */
    public void setLibellePassage(java.lang.String libellePassage) {
        this.libellePassage = libellePassage;
    }

}
