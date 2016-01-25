package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;

public class DecisionPCAPlanCalcule extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDecisionHeader = null;
    private String idValidationDecision = null;
    // private SimpleValidationDecision simpleValidationDecision = null;
    // private SimpleDecisionHeader simpleDecisionHeader = null;
    // private SimplePCAccordee simplePCAccordee = null;
    private SimplePlanDeCalcul simplePlanDeCalcul = null;

    public DecisionPCAPlanCalcule() {
        super();
        // this.simpleValidationDecision = new SimpleValidationDecision();
        // this.simpleDecisionHeader = new SimpleDecisionHeader();
        // this.simplePCAccordee = new SimplePCAccordee();
        setSimplePlanDeCalcul(new SimplePlanDeCalcul());
    }

    @Override
    public String getId() {
        return simplePlanDeCalcul.getId();
    }

    public String getIdDecisionHeader() {
        return idDecisionHeader;
    }

    public String getIdValidationDecision() {
        return idValidationDecision;
    }

    public SimplePlanDeCalcul getSimplePlanDeCalcul() {
        return simplePlanDeCalcul;
    }

    @Override
    public String getSpy() {
        return simplePlanDeCalcul.getSpy();
    }

    @Override
    public void setId(String id) {
        simplePlanDeCalcul.setId(id);
    }

    public void setIdDecisionHeader(String idDecisionHeader) {
        this.idDecisionHeader = idDecisionHeader;
    }

    public void setIdValidationDecision(String idValidationDecision) {
        this.idValidationDecision = idValidationDecision;
    }

    public void setSimplePlanDeCalcul(SimplePlanDeCalcul simplePlanDeCalcul) {
        this.simplePlanDeCalcul = simplePlanDeCalcul;
    }

    @Override
    public void setSpy(String spy) {
        simplePlanDeCalcul.setSpy(spy);

    }

}
