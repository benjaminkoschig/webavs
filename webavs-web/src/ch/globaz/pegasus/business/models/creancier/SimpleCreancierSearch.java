package ch.globaz.pegasus.business.models.creancier;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleCreancierSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemande = null;
    private String forIdTiers = null;

    /**
     * @return the forIdDemande
     */
    public String getForIdDemande() {
        return forIdDemande;
    }

    /**
     * @return the forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    /**
     * @param forIdTiers
     *            the forIdTiers to set
     */
    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    @Override
    public Class<SimpleCreancier> whichModelClass() {
        return SimpleCreancier.class;
    }

}
