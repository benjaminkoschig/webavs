package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleMembreFamilleSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdTiers = null;

    public SimpleMembreFamilleSearchModel() {
        super();
    }

    /**
     * @return the forIdTiers
     */
    public String getForIdTiers() {
        return forIdTiers;
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
        return SimpleMembreFamille.class;
    }

}
