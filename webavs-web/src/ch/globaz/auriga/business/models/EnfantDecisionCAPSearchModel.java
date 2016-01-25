package ch.globaz.auriga.business.models;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class EnfantDecisionCAPSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecision = null;

    @Override
    public Class<SimpleEnfantDecisionCAP> whichModelClass() {
        return SimpleEnfantDecisionCAP.class;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }
}
