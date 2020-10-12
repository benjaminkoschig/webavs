package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleHomeSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdHome = null;
    private String forNoIdentification = null;
    private String forIdTiersHome =null;

    public String getForNoIdentification() {
        return forNoIdentification;
    }

    public void setForNoIdentification(String forNoIdentification) {
        this.forNoIdentification = forNoIdentification;
    }

    /**
     * @return the forIdHome
     */
    public String getForIdHome() {
        return forIdHome;
    }

    /**
     * @param forIdHome
     *            the forIdHome to set
     */
    public void setForIdHome(String forIdHome) {
        this.forIdHome = forIdHome;
    }
    public String getForIdTiersHome() {
        return forIdTiersHome;
    }

    public void setForIdTiersHome(String forIdTiersHome) {
        this.forIdTiersHome = forIdTiersHome;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleHome.class;
    }

}
