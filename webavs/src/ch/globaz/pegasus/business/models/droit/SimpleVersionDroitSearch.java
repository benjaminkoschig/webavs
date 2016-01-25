/**
 * 
 */
package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author BSC
 * 
 */
public class SimpleVersionDroitSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String forIdDroit = null;

    private String forIdVersionDroit = null;
    private String forNoVersionDroit = null;

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @return the forIdVersionDroit
     */
    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public String getForNoVersionDroit() {
        return forNoVersionDroit;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * @param forIdVersionDroit
     *            the forIdVersionDroit to set
     */
    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setForNoVersionDroit(String forNoVersionDroit) {
        this.forNoVersionDroit = forNoVersionDroit;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        setOrderKey(orderBy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleVersionDroit.class;
    }

}
