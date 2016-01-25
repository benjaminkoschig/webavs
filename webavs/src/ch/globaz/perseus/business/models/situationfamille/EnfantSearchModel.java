package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class EnfantSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdEnfant = null;
    private String forIdMembreFamille = null;
    private String forIdTiers = null;

    /**
     * @return the forIdEnfant
     */
    public String getForIdEnfant() {
        return forIdEnfant;
    }

    /**
     * @return the forIdMembreFamille
     */
    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    /**
     * @return the forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @param forIdEnfant
     *            the forIdEnfant to set
     */
    public void setForIdEnfant(String forIdEnfant) {
        this.forIdEnfant = forIdEnfant;
    }

    /**
     * @param forIdMembreFamille
     *            the forIdMembreFamille to set
     */
    public void setForIdMembreFamille(String forIdMembreFamille) {
        this.forIdMembreFamille = forIdMembreFamille;
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
        return Enfant.class;
    }

}
