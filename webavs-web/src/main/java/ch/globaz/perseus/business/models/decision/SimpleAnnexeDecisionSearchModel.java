package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleAnnexeDecisionSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdAnnexeDecision = null;
    private String forIdDecision = null;

    public String getForIdAnnexeDecision() {
        return forIdAnnexeDecision;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public void setForIdAnnexeDecision(String forIdAnnexeDecision) {
        this.forIdAnnexeDecision = forIdAnnexeDecision;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    @Override
    public Class whichModelClass() {
        return SimpleAnnexeDecision.class;
    }

}
