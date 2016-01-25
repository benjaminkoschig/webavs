package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ConjointSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdConjoint = null;
    private String forIdMembreFamille = null;
    private String forIdTiers = null;

    /**
     * @return the forIdConjoint
     */
    public String getForIdConjoint() {
        return forIdConjoint;
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
     * @param forIdConjoint
     *            the forIdConjoint to set
     */
    public void setForIdConjoint(String forIdConjoint) {
        this.forIdConjoint = forIdConjoint;
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
        return Conjoint.class;
    }

}
