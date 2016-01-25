package ch.globaz.al.business.models.droit;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Modèle permettant de faire une recherche sur les enfants (liés au tiers)
 * 
 * @author GMO
 */
public class EnfantComplexSearchModel extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche par la date de naissance
     */
    private String forDateNaissance = null;
    /**
     * Recherche par l'identifiant de l'enfant
     */
    private String forIdEnfant = null;
    /**
     * Recherche par l'identifiant du tiers
     */
    private String forIdTiers = null;
    /**
     * Recherche par l'identifiant du nom
     */
    private String forNom = null;
    /**
     * Recherche par le NSS
     */
    private String forNss = null;

    /**
     * Recherche par l'identifiant du prénom
     */
    private String forPrenom = null;

    /**
     * @return the forDateNaissance
     */
    public String getForDateNaissance() {
        return forDateNaissance;
    }

    /**
     * 
     * @return forIdEnfant
     */
    public String getForIdEnfant() {
        return forIdEnfant;
    }

    /**
     * 
     * @return forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @return the forNom
     */
    public String getForNom() {
        return forNom;
    }

    /**
     * @return the forNss
     */
    public String getForNss() {
        return forNss;
    }

    /**
     * @return the forPrenom
     */
    public String getForPrenom() {
        return forPrenom;
    }

    /**
     * @param forDateNaissance
     *            the forDateNaissance to set
     */
    public void setForDateNaissance(String forDateNaissance) {
        this.forDateNaissance = forDateNaissance;
    }

    /**
     * 
     * @param forIdEnfant
     *            : the forIdEnfant to set
     */
    public void setForIdEnfant(String forIdEnfant) {
        this.forIdEnfant = forIdEnfant;
    }

    /**
     * 
     * @param forIdTiers
     *            : the forIdTiers to set
     */
    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    /**
     * @param forNom
     *            the forNom to set
     */
    public void setForNom(String forNom) {
        this.forNom = forNom;
    }

    /**
     * @param forNss
     *            the forNss to set
     */
    public void setForNss(String forNss) {
        this.forNss = forNss;
    }

    /**
     * @param forPrenom
     *            the forPrenom to set
     */
    public void setForPrenom(String forPrenom) {
        this.forPrenom = forPrenom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return EnfantComplexModel.class;
    }

}
