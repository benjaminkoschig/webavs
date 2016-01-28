package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleDroitSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDemandePC = null;
    private String forIdDroit = null;

    /**
     * @return the forIdDemandePC
     */
    public String getForIdDemandePC() {
        return forIdDemandePC;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @param forIdDemandePC
     *            the forIdDemandePC to set
     */
    public void setForIdDemandePC(String forIdDemandePC) {
        this.forIdDemandePC = forIdDemandePC;
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleDroit.class;
    }
}
