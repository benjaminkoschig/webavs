/**
 * 
 */
package ch.globaz.amal.business.models.reprise;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author DHI
 * 
 */
public class ContribuableRepriseSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateNaissance = null;
    private String forIdContribuable = null;
    private String forIdTier = null;
    private String forNoContribuable = null;
    private String likeNoContribuableFormate = null;
    private String likeNom = null;
    private String likePrenom = null;

    /**
	 * 
	 */
    public ContribuableRepriseSearch() {
        super();
    }

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * @return the forIdContribuable
     */
    public String getForIdContribuable() {
        return forIdContribuable;
    }

    /**
     * @return the forIdTier
     */
    public String getForIdTier() {
        return forIdTier;
    }

    /**
     * @return the forNoContribuable
     */
    public String getForNoContribuable() {
        return forNoContribuable;
    }

    /**
     * @return the likeNoContribuableFormate
     */
    public String getLikeNoContribuableFormate() {
        return likeNoContribuableFormate;
    }

    /**
     * @return the likeNom
     */
    public String getLikeNom() {
        return likeNom;
    }

    /**
     * @return the likePrenom
     */
    public String getLikePrenom() {
        return likePrenom;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    /**
     * @param forIdContribuable
     *            the forIdContribuable to set
     */
    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    /**
     * @param forIdTier
     *            the forIdTier to set
     */
    public void setForIdTier(String forIdTier) {
        this.forIdTier = forIdTier;
    }

    /**
     * @param forNoContribuable
     *            the forNoContribuable to set
     */
    public void setForNoContribuable(String forNoContribuable) {
        this.forNoContribuable = forNoContribuable;
    }

    /**
     * @param likeNoContribuableFormate
     *            the likeNoContribuableFormate to set
     */
    public void setLikeNoContribuableFormate(String likeNoContribuableFormate) {
        this.likeNoContribuableFormate = likeNoContribuableFormate;
    }

    /**
     * @param likeNom
     *            the likeNom to set
     */
    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    /**
     * @param likePrenom
     *            the likePrenom to set
     */
    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return ContribuableReprise.class;
    }

}
