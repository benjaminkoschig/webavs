/**
 * 
 */
package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author CBU
 * 
 */
public class SimpleRevenuContribuableSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdRevenuContribuable = new String();

    public String getForIdRevenuContribuable() {
        return forIdRevenuContribuable;
    }

    public void setForIdRevenuContribuable(String forIdRevenuContribuable) {
        this.forIdRevenuContribuable = forIdRevenuContribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleRevenuContribuable.class;
    }

}
