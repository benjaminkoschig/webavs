/**
 * class CPListeDecisionsAvecMiseEnCompteViewBean écrit le 19/01/05 par JPA
 * 
 * class ViewBean pour les décisions avec mise en compte
 * 
 * @author JPA
 **/
package globaz.phenix.process.decision;

import globaz.framework.bean.FWViewBeanInterface;

public class CPDecisionReporterViewBean extends CPProcessReporterDecisionPreEncodee implements FWViewBeanInterface {

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
