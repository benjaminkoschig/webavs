package ch.globaz.aries.business.models;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class DetailDecisionCGASSearchModel extends JadeSearchSimpleModel {

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
    public Class<SimpleDetailDecisionCGAS> whichModelClass() {
        return SimpleDetailDecisionCGAS.class;
    }
}
