package ch.globaz.auriga.business.models;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class ComplexEnfantDecisionCAPTiersSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecision = null;
    private String forIdEnfantDecision = null;

    public String getForIdDecision() {
        return forIdDecision;
    }

    public String getForIdEnfantDecision() {
        return forIdEnfantDecision;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public void setForIdEnfantDecision(String forIdEnfantDecision) {
        this.forIdEnfantDecision = forIdEnfantDecision;
    }

    @Override
    public Class<ComplexEnfantDecisionCAPTiers> whichModelClass() {
        return ComplexEnfantDecisionCAPTiers.class;
    }
}
