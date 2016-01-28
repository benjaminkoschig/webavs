package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleTypeChambreSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdHome = null;
    private String forIdTypeChambre = null;

    /**
     * @return the forIdHome
     */
    public String getForIdHome() {
        return forIdHome;
    }

    /**
     * @return the forIdTypeChambre
     */
    public String getForIdTypeChambre() {
        return forIdTypeChambre;
    }

    /**
     * @param forIdHome
     *            the forIdHome to set
     */
    public void setForIdHome(String forIdHome) {
        this.forIdHome = forIdHome;
    }

    /**
     * @param forIdTypeChambre
     *            the forIdTypeChambre to set
     */
    public void setForIdTypeChambre(String forIdTypeChambre) {
        this.forIdTypeChambre = forIdTypeChambre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleTypeChambre.class;
    }

}
