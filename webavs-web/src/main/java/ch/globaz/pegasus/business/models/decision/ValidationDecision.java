package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;

public class ValidationDecision extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDecisionApresCalcul simpleDecisionApresCalcul = null;
    private SimpleDecisionHeader simpleDecisionHeader = null;
    private SimpleValidationDecision simpleValidationDecision = null;

    public ValidationDecision() {
        super();
        simpleDecisionApresCalcul = new SimpleDecisionApresCalcul();
        simpleDecisionHeader = new SimpleDecisionHeader();
        simpleValidationDecision = new SimpleValidationDecision();
    }

    @Override
    public String getId() {
        return simpleValidationDecision.getId();
    }

    public SimpleDecisionApresCalcul getSimpleDecisionApresCalcul() {
        return simpleDecisionApresCalcul;
    }

    public SimpleDecisionHeader getSimpleDecisionHeader() {
        return simpleDecisionHeader;
    }

    public SimpleValidationDecision getSimpleValidationDecision() {
        return simpleValidationDecision;
    }

    @Override
    public String getSpy() {
        return simpleValidationDecision.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleValidationDecision.setId(id);
    }

    public void setSimpleDecisionApresCalcul(SimpleDecisionApresCalcul simpleDecisionApresCalcul) {
        this.simpleDecisionApresCalcul = simpleDecisionApresCalcul;
    }

    public void setSimpleDecisionHeader(SimpleDecisionHeader simpleDecisionHeader) {
        this.simpleDecisionHeader = simpleDecisionHeader;
    }

    public void setSimpleValidationDecision(SimpleValidationDecision simpleValidationDecision) {
        this.simpleValidationDecision = simpleValidationDecision;
    }

    @Override
    public void setSpy(String spy) {
        simpleValidationDecision.setSpy(spy);
    }

}
