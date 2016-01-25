package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DecisionForTypeDocumentSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecision = null;

    public String getForIdDecision() {
        return forIdDecision;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    @Override
    public Class whichModelClass() {
        return DecisionForTypeDocument.class;
    }

}
