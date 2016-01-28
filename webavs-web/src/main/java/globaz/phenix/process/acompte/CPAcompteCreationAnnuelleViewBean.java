package globaz.phenix.process.acompte;

import globaz.framework.bean.FWViewBeanInterface;

/**
 * Insérez la description du type ici. Date de création : (21.01.2003 08:24:32)
 * 
 * @author: Bryan Tchang
 */
public class CPAcompteCreationAnnuelleViewBean extends CPProcessAcompteCreationAnnuelle implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String libellePassage = "";

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
