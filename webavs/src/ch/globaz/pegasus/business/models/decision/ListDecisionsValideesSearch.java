package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ListDecisionsValideesSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDecisionMax = null;
    private String forDateDecisionMin = null;
    private String forValidePar = null;

    public String getForDateDecisionMax() {
        return forDateDecisionMax;
    }

    public String getForDateDecisionMin() {
        return forDateDecisionMin;
    }

    /**
     * @return the forValidePar
     */
    public String getForValidePar() {
        return forValidePar;
    }

    public void setForDateDecisionMax(String forDateDecisionMax) {
        this.forDateDecisionMax = forDateDecisionMax;
    }

    public void setForDateDecisionMin(String forDateDecisionMin) {
        this.forDateDecisionMin = forDateDecisionMin;
    }

    /**
     * @param forValidePar
     *            the forValidePar to set
     */
    public void setForValidePar(String forValidePar) {
        this.forValidePar = forValidePar;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        // TODO Auto-generated method stub
        return ListDecisionsValidees.class;
    }

}
