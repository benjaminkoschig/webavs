package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PersonneDansPlanCalculSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPersonneDansPlanCalcul = null;
    private String forIdPlanDeCalcul = null;

    /**
     * 
     * @return the forIdEnfantDansCalcul
     */
    public String getForIdPersonneDansPlanCalcul() {
        return forIdPersonneDansPlanCalcul;
    }

    /**
     * 
     * @return th forIdPlanDeCalcul
     */
    public String getForIdPlanDeCalcul() {
        return forIdPlanDeCalcul;
    }

    /**
     * @param forIdHome
     *            the forIdHome to set
     */
    public void setForIdPersonneDansPlanCalcul(String forIdHome) {
        forIdPersonneDansPlanCalcul = forIdHome;
    }

    /**
     * 
     * @param forIdPlanDeCalcul
     *            the forIdPlanDeCalcul to set
     */
    public void setForIdPlanDeCalcul(String forIdPlanDeCalcul) {
        this.forIdPlanDeCalcul = forIdPlanDeCalcul;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return PersonneDansPlanCalcul.class;
    }

}
