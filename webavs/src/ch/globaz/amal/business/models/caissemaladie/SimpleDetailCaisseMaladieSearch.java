/**
 * 
 */
package ch.globaz.amal.business.models.caissemaladie;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author cbu
 * 
 */
public class SimpleDetailCaisseMaladieSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDetailCaisseMaladie = null;
    private String forIdTiers = null;

    public String getForIdDetailCaisseMaladie() {
        return forIdDetailCaisseMaladie;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public void setForIdDetailCaisseMaladie(String forIdDetailCaisseMaladie) {
        this.forIdDetailCaisseMaladie = forIdDetailCaisseMaladie;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleDetailCaisseMaladie.class;
    }

}
