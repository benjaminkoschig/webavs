package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class RequerantSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdMembreFamille = null;
    private String forIdRequerant = null;
    private String forIdTiers = null;

    /**
     * @return the forIdMembreFamille
     */
    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    /**
     * @return the forIdRequerant
     */
    public String getForIdRequerant() {
        return forIdRequerant;
    }

    /**
     * @return the forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @param forIdMembreFamille
     *            the forIdMembreFamille to set
     */
    public void setForIdMembreFamille(String forIdMembreFamille) {
        this.forIdMembreFamille = forIdMembreFamille;
    }

    /**
     * @param forIdRequerant
     *            the forIdRequerant to set
     */
    public void setForIdRequerant(String forIdRequerant) {
        this.forIdRequerant = forIdRequerant;
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
        return Requerant.class;
    }

}
