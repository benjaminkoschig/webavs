/**
 * 
 */
package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author BSC
 * 
 */
public class SimpleDonneesPersonnellesSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDonneesPersonnelles = null;

    /**
     * @return the forIdDonneesPersonnelles
     */
    public String getForIdDonneesPersonnelles() {
        return forIdDonneesPersonnelles;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy() {
        return getOrderKey();
    }

    /**
     * @param forIdDonneesPersonnelles
     *            the forIdDonneesPersonnelles to set
     */
    public void setForIdDonneesPersonnelles(String forIdDonneesPersonnelles) {
        this.forIdDonneesPersonnelles = forIdDonneesPersonnelles;
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
        return SimpleDonneesPersonnelles.class;
    }

}
