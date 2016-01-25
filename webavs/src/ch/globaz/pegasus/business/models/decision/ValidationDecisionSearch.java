package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ValidationDecisionSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsTypePreparation = null;
    private String forIdPca = null;

    public String getForCsTypePreparation() {
        return forCsTypePreparation;
    }

    public String getForIdPca() {
        return forIdPca;
    }

    public void setForCsTypePreparation(String forCsTypePreparation) {
        this.forCsTypePreparation = forCsTypePreparation;
    }

    public void setForIdPca(String forIdPca) {
        this.forIdPca = forIdPca;
    }

    @Override
    public Class<ValidationDecision> whichModelClass() {
        return ValidationDecision.class;
    }

}
