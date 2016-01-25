package ch.globaz.pegasus.business.models.home;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PeriodeServiceEtatSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdHome = null;

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

    @Override
    public Class whichModelClass() {
        return PeriodeServiceEtat.class;
    }

}
