package ch.globaz.pegasus.business.domaine.pca;

import ch.globaz.pegasus.business.domaine.decision.Decision;

public class PcaDecision {
    private final Pca pca;
    private final Decision decision;
    private PcaDecision pcaDecisionPartner;

    public PcaDecision(Pca pca, Decision decision) {
        this.pca = pca;
        this.decision = decision;
    }

    public PcaDecision(Decision decision) {
        pca = null;
        this.decision = decision;
    }

    public Pca getPca() {
        return pca;
    }

    public Decision getDecision() {
        return decision;
    }

    public PcaDecision getPcaDecisionPartner() {
        return pcaDecisionPartner;
    }

    public void setPcaDecisionPartner(PcaDecision pcaDecisionPartner) {
        this.pcaDecisionPartner = pcaDecisionPartner;
    }
    
}
