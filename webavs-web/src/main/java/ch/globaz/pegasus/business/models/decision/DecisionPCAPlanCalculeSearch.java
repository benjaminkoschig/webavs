package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DecisionPCAPlanCalculeSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecisionHeader = null;
    private Boolean forIsPlanRetenu = null;

    public String getForIdDecisionHeader() {
        return forIdDecisionHeader;
    }

    public Boolean getForIsPlanRetenu() {
        return forIsPlanRetenu;
    }

    public void setForIdDecisionHeader(String forIdDecisionHeader) {
        this.forIdDecisionHeader = forIdDecisionHeader;
    }

    public void setForIsPlanRetenu(Boolean forIsPlanRetenu) {
        this.forIsPlanRetenu = forIsPlanRetenu;
    }

    @Override
    public Class<DecisionPCAPlanCalcule> whichModelClass() {
        return DecisionPCAPlanCalcule.class;
    }
}
