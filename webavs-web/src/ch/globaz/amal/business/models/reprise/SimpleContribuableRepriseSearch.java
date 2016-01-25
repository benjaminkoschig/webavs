/**
 * 
 */
package ch.globaz.amal.business.models.reprise;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author DHI
 * 
 */
public class SimpleContribuableRepriseSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdContribuable = null;

    /**
	 * 
	 */
    public SimpleContribuableRepriseSearch() {
        super();
    }

    /**
     * @return the forIdContribuable
     */
    public String getForIdContribuable() {
        return forIdContribuable;
    }

    /**
     * @param forIdContribuable
     *            the forIdContribuable to set
     */
    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleContribuableReprise.class;
    }

}
