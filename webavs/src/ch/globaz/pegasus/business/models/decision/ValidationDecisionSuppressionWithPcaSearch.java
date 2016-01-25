package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ValidationDecisionSuppressionWithPcaSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecisionHeader = null;
    private String forIdPca = null;
    private String forIdValidation = null;

    public String getForIdDecisionHeader() {
        return forIdDecisionHeader;
    }

    public String getForIdPca() {
        return forIdPca;
    }

    public String getForIdValidation() {
        return forIdValidation;
    }

    public void setForIdDecisionHeader(String forIdDecisionHeader) {
        this.forIdDecisionHeader = forIdDecisionHeader;
    }

    public void setForIdPca(String forIdPca) {
        this.forIdPca = forIdPca;
    }

    public void setForIdValidation(String forIdValidation) {
        this.forIdValidation = forIdValidation;
    }

    @Override
    public Class whichModelClass() {
        return ValidationDecisionSupressionWithPca.class;
    }

}
