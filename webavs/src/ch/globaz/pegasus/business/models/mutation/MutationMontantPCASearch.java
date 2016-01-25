package ch.globaz.pegasus.business.models.mutation;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class MutationMontantPCASearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDecisionMax;
    private String forDateDecisionMin;

    public String getForDateDecisionMax() {
        return forDateDecisionMax;
    }

    public String getForDateDecisionMin() {
        return forDateDecisionMin;
    }

    public void setForDateDecisionMax(String forDateDecisionMax) {
        this.forDateDecisionMax = forDateDecisionMax;
    }

    public void setForDateDecisionMin(String forDateDecisionMin) {
        this.forDateDecisionMin = forDateDecisionMin;
    }

    @Override
    public Class<MutationMontantPCA> whichModelClass() {
        return MutationMontantPCA.class;
    }

}
