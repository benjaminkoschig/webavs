/**
 * 
 */
package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author CBU
 * 
 */
public class SimpleRevenuSourcierSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdRevenuSourcier = new String();

    public String getForIdRevenuSourcier() {
        return forIdRevenuSourcier;
    }

    public void setForIdRevenuSourcier(String forIdRevenuSourcier) {
        this.forIdRevenuSourcier = forIdRevenuSourcier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleRevenuSourcier.class;
    }

}
