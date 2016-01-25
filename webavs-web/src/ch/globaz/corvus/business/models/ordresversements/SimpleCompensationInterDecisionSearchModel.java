package ch.globaz.corvus.business.models.ordresversements;

import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class SimpleCompensationInterDecisionSearchModel extends JadeAbstractSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdCompensationInterDecision;

    public SimpleCompensationInterDecisionSearchModel() {
        super();

        forIdCompensationInterDecision = null;
    }

    public String getForIdCompensationInterDecision() {
        return forIdCompensationInterDecision;
    }

    public void setForIdCompensationInterDecision(String forIdCompensationInterDecision) {
        this.forIdCompensationInterDecision = forIdCompensationInterDecision;
    }

    @Override
    public Class<? extends JadeAbstractModel> whichModelClass() {
        return SimpleCompensationInterDecision.class;
    }
}
