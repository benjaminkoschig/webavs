package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;

public class ValidationDecisionSupressionWithPca extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private PCAccordee pcAccordee = null;

    private SimpleValidationDecision simpleValidationDecision = null;

    public ValidationDecisionSupressionWithPca() {
        super();
        simpleValidationDecision = new SimpleValidationDecision();
        pcAccordee = new PCAccordee();
    }

    @Override
    public String getId() {
        return simpleValidationDecision.getIdValidationDecision();
    }

    public PCAccordee getPcAccordee() {
        return pcAccordee;
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

    public void setPcAccordee(PCAccordee pcAccordee) {
        this.pcAccordee = pcAccordee;
    }

    public void setSimpleValidationDecision(SimpleValidationDecision simpleValidationDecision) {
        this.simpleValidationDecision = simpleValidationDecision;
    }

    @Override
    public void setSpy(String spy) {
        simpleValidationDecision.setSpy(spy);

    }

}
