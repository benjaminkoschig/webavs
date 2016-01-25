package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;

public class DecisionValidationPca extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDecisionApresCalcul simpleDecisionApresCalcul = null;
    private SimpleDecisionHeader simpleDecisionHeader = null;
    private SimplePCAccordee simplePCAccordee = null;
    private SimpleValidationDecision simpleValidationDecision = null;

    public DecisionValidationPca() {
        super();
        simplePCAccordee = new SimplePCAccordee();
        simpleDecisionApresCalcul = new SimpleDecisionApresCalcul();
        simpleDecisionHeader = new SimpleDecisionHeader();
        simpleValidationDecision = new SimpleValidationDecision();
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }
}
