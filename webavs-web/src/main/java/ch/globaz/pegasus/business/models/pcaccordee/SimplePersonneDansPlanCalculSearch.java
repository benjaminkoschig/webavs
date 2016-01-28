package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

public class SimplePersonneDansPlanCalculSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPersonneDansPlanCalcul = null;
    private String forIdPlanDeCalcul = null;
    private List<String> inIdPlanDeCalcul;

    /**
     * @return the forIdEnfantDansCalcul
     */
    public String getForIdPersonneDansPlanCalcul() {
        return forIdPersonneDansPlanCalcul;
    }

    /**
     * @return the forIdPlanDeCalcul
     */
    public String getForIdPlanDeCalcul() {
        return forIdPlanDeCalcul;
    }

    /**
     * @return the inIdPlanDeCalcul
     */
    public List<String> getInIdPlanDeCalcul() {
        return inIdPlanDeCalcul;
    }

    /**
     * @param forIdPersonneDansPlanCalcul
     *            the forIdEnfantDansCalcul to set
     */
    public void setForIdPersonneDansPlanCalcul(String forIdPersonneDansPlanCalcul) {
        this.forIdPersonneDansPlanCalcul = forIdPersonneDansPlanCalcul;
    }

    /**
     * @param forIdPlanDeCalcul
     *            the forIdPlanDeCalcul to set
     */
    public void setForIdPlanDeCalcul(String forIdPlanDeCalcul) {
        this.forIdPlanDeCalcul = forIdPlanDeCalcul;
    }

    /**
     * @param inIdPlanDeCalcul
     *            the inIdPlanDeCalcul to set
     */
    public void setInIdPlanDeCalcul(List<String> inIdPlanDeCalcul) {
        this.inIdPlanDeCalcul = inIdPlanDeCalcul;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimplePersonneDansPlanCalcul.class;
    }

}
