/**
 * 
 */
package ch.globaz.amal.business.models.reprise;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author DHI
 * 
 */
public class SimpleInnerMFRepriseSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String forIdInnerMF = null;
    public String forIdTiers = null;

    /**
	 * 
	 */
    public SimpleInnerMFRepriseSearch() {
        super();
    }

    /**
     * @return the forIdInnerMF
     */
    public String getForIdInnerMF() {
        return forIdInnerMF;
    }

    /**
     * @return the forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @param forIdInnerMF
     *            the forIdInnerMF to set
     */
    public void setForIdInnerMF(String forIdInnerMF) {
        this.forIdInnerMF = forIdInnerMF;
    }

    /**
     * @param forIdTiers
     *            the forIdTiers to set
     */
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
        return SimpleInnerMFReprise.class;
    }

}
