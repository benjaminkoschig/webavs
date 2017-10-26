package ch.globaz.pegasus.business.domaine.pca;

import java.util.ArrayList;
import java.util.List;

public class PcaDecisions {
    private final List<PcaDecision> pcaDecisions = new ArrayList<PcaDecision>();

    public boolean add(PcaDecision pcaDecision) {
        return pcaDecisions.add(pcaDecision);
    }

    public List<PcaDecision> list() {
        return new ArrayList<PcaDecision>(pcaDecisions);
    }

}
