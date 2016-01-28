/**
 * 
 */
package ch.globaz.amal.business.models.annonce;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author cbu
 * 
 */
public class AnnoncesCaisseSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateAvisRip = null;
    private String forNoCaisseMaladie = null;

    public String getForDateAvisRip() {
        return forDateAvisRip;
    }

    public String getForNoCaisseMaladie() {
        return forNoCaisseMaladie;
    }

    public void setForDateAvisRip(String forDateAvisRip) {
        this.forDateAvisRip = forDateAvisRip;
    }

    public void setForNoCaisseMaladie(String forNoCaisseMaladie) {
        this.forNoCaisseMaladie = forNoCaisseMaladie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return AnnoncesCaisse.class;
    }

}
