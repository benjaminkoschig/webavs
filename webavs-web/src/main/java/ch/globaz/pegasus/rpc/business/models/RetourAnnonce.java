package ch.globaz.pegasus.rpc.business.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;

public class RetourAnnonce extends JadeComplexModel {
    private SimpleAnnonce simpleAnnonce = new SimpleAnnonce();
    private SimpleLienAnnonceDecision simpleLienAnnonceDecision = new SimpleLienAnnonceDecision();
    private SimpleDecisionHeader simpleDecisionHeader = new SimpleDecisionHeader();
    private SimpleRetourAnnonce simpleRetourAnnonce = new SimpleRetourAnnonce();

    public SimpleAnnonce getSimpleAnnonce() {
        return simpleAnnonce;
    }

    public void setSimpleAnnonce(SimpleAnnonce simpleAnnonce) {
        this.simpleAnnonce = simpleAnnonce;
    }

    public SimpleLienAnnonceDecision getSimpleLienAnnonceDecision() {
        return simpleLienAnnonceDecision;
    }

    public void setSimpleLienAnnonceDecision(SimpleLienAnnonceDecision simpleLienAnnonceDecision) {
        this.simpleLienAnnonceDecision = simpleLienAnnonceDecision;
    }

    public SimpleRetourAnnonce getSimpleRetourAnnonce() {
        return simpleRetourAnnonce;
    }

    public void setSimpleRetourAnnonce(SimpleRetourAnnonce simpleRetourAnnonce) {
        this.simpleRetourAnnonce = simpleRetourAnnonce;
    }

    public SimpleDecisionHeader getSimpleDecisionHeader() {
        return simpleDecisionHeader;
    }

    public void setSimpleDecisionHeader(SimpleDecisionHeader simpleDecisionHeader) {
        this.simpleDecisionHeader = simpleDecisionHeader;
    }

    @Override
    public String getId() {
        return simpleAnnonce.getId();
    }

    @Override
    public String getSpy() {
        return simpleAnnonce.getSpy();
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public void setSpy(String spy) {

    }
}
