/**
 * 
 */
package ch.globaz.amal.business.models.revenu;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author dhi
 * 
 */
public class SimpleRevenuDeterminantSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forIdContribuable = null;

    private String forIdRevenuDeterminant = null;

    private String forIdRevenuHistorique = null;

    /**
	 * 
	 */
    public SimpleRevenuDeterminantSearch() {
    }

    /**
     * @return the forIdContribuable
     */
    public String getForIdContribuable() {
        return forIdContribuable;
    }

    /**
     * @return the forIdRevenuDeterminant
     */
    public String getForIdRevenuDeterminant() {
        return forIdRevenuDeterminant;
    }

    /**
     * @return the forIdRevenuHistorique
     */
    public String getForIdRevenuHistorique() {
        return forIdRevenuHistorique;
    }

    /**
     * @param forIdContribuable
     *            the forIdContribuable to set
     */
    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    /**
     * @param forIdRevenu
     *            the forIdRevenu to set
     */
    public void setForIdRevenuDeterminant(String forIdRevenuDeterminant) {
        this.forIdRevenuDeterminant = forIdRevenuDeterminant;
    }

    /**
     * @param forIdRevenuHistorique
     *            the forIdRevenuHistorique to set
     */
    public void setForIdRevenuHistorique(String forIdRevenuHistorique) {
        this.forIdRevenuHistorique = forIdRevenuHistorique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleRevenuDeterminant.class;
    }

}
