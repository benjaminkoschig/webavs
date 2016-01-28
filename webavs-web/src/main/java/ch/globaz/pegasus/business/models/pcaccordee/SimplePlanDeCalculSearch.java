package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

public class SimplePlanDeCalculSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPCAccordee = null;
    private String forIdPlanDeCalcul = null;
    private Boolean forIsPlanRetenu = null;
    private String forEtatPc = null;

    public String getForEtatPc() {
        return forEtatPc;
    }

    public void setForEtatPc(String forEtatPc) {
        this.forEtatPc = forEtatPc;
    }

    private List<String> inIdPlanDeCalcul = null;

    /**
     * @return the forIdPCAccordee
     */
    public String getForIdPCAccordee() {
        return forIdPCAccordee;
    }

    /**
     * @return the forIdPlanDeCalcul
     */
    public String getForIdPlanDeCalcul() {
        return forIdPlanDeCalcul;
    }

    /**
     * @return the forIsPlanRetenu
     */
    public Boolean getForIsPlanRetenu() {
        return forIsPlanRetenu;
    }

    /**
     * @return the inIdPlanDeCalcul
     */
    public List<String> getInIdPlanDeCalcul() {
        return inIdPlanDeCalcul;
    }

    /**
     * @param forIdPCAccordee
     *            the forIdPCAccordee to set
     */
    public void setForIdPCAccordee(String forIdPCAccordee) {
        this.forIdPCAccordee = forIdPCAccordee;
    }

    /**
     * @param forIdPlanDeCalcul
     *            the forIdPlanDeCalcul to set
     */
    public void setForIdPlanDeCalcul(String forIdPlanDeCalcul) {
        this.forIdPlanDeCalcul = forIdPlanDeCalcul;
    }

    /**
     * @param forIsPlanRetenu
     *            the forIsPlanRetenu to set
     */
    public void setForIsPlanRetenu(Boolean forIsPlanRetenu) {
        this.forIsPlanRetenu = forIsPlanRetenu;
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
        return SimplePlanDeCalcul.class;
    }

}
