/**
 * 
 */
package ch.globaz.amal.business.models.reprise;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author DHI
 * 
 */
public class SimpleFamilleRepriseSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdContribuable = null;
    private String forIdFamille = null;
    private String forPereMereEnfant = null;
    private Boolean isContribuable = null;

    /**
	 * 
	 */
    public SimpleFamilleRepriseSearch() {
        super();
    }

    /**
     * @return the forIdContribuable
     */
    public String getForIdContribuable() {
        return forIdContribuable;
    }

    /**
     * @return the forIdFamille
     */
    public String getForIdFamille() {
        return forIdFamille;
    }

    /**
     * @return the forPereMereEnfant
     */
    public String getForPereMereEnfant() {
        return forPereMereEnfant;
    }

    /**
     * @return the isContribuable
     */
    public Boolean getIsContribuable() {
        return isContribuable;
    }

    /**
     * @param forIdContribuable
     *            the forIdContribuable to set
     */
    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    /**
     * @param forIdFamille
     *            the forIdFamille to set
     */
    public void setForIdFamille(String forIdFamille) {
        this.forIdFamille = forIdFamille;
    }

    /**
     * @param forPereMereEnfant
     *            the forPereMereEnfant to set
     */
    public void setForPereMereEnfant(String forPereMereEnfant) {
        this.forPereMereEnfant = forPereMereEnfant;
    }

    /**
     * @param isContribuable
     *            the isContribuable to set
     */
    public void setIsContribuable(Boolean isContribuable) {
        this.isContribuable = isContribuable;
    }

    @Override
    public Class whichModelClass() {
        return SimpleFamilleReprise.class;
    }

}
