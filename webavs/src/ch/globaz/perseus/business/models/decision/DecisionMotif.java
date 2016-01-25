package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * 
 * @author MBO
 * 
 */

public class DecisionMotif extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDecision simpleDecision = null;
    private SimpleDecisionMotif simpleDecisionMotif = null;

    public DecisionMotif() {
        super();
        simpleDecision = new SimpleDecision();
        simpleDecisionMotif = new SimpleDecisionMotif();

    }

    @Override
    public String getId() {
        return simpleDecisionMotif.getId();
    }

    public SimpleDecision getSimpleDecision() {
        return simpleDecision;
    }

    public SimpleDecisionMotif getSimpleDecisionMotif() {
        return simpleDecisionMotif;
    }

    @Override
    public String getSpy() {
        return simpleDecisionMotif.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleDecisionMotif.setId(id);

    }

    public void setSimpleDecision(SimpleDecision simpleDecision) {
        this.simpleDecision = simpleDecision;
    }

    public void setSimpleDecisionMotif(SimpleDecisionMotif simpleDecisionMotif) {
        this.simpleDecisionMotif = simpleDecisionMotif;
    }

    @Override
    public void setSpy(String spy) {
        simpleDecisionMotif.setSpy(spy);

    }

}
