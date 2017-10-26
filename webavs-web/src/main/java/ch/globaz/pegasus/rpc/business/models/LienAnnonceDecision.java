package ch.globaz.pegasus.rpc.business.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;

public class LienAnnonceDecision extends JadeComplexModel {
    private SimpleLienAnnonceDecision lienAnnonceDecision = new SimpleLienAnnonceDecision();
    private SimpleDecisionHeader decisionHeader = new SimpleDecisionHeader();

    public SimpleLienAnnonceDecision getLienAnnonceDecision() {
        return lienAnnonceDecision;
    }

    public void setLienAnnonceDecision(SimpleLienAnnonceDecision lienAnnonceDecision) {
        this.lienAnnonceDecision = lienAnnonceDecision;
    }

    public SimpleDecisionHeader getDecisionHeader() {
        return decisionHeader;
    }

    public void setDecisionHeader(SimpleDecisionHeader decisionHeader) {
        this.decisionHeader = decisionHeader;
    }

    @Override
    public String getId() {
        return lienAnnonceDecision.getId();
    }

    @Override
    public String getSpy() {
        return lienAnnonceDecision.getSpy();
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public void setSpy(String spy) {

    }

}
