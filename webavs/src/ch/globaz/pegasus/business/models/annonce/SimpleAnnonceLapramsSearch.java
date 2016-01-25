package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

public class SimpleAnnonceLapramsSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> inIdsDecision = null;

    public List<String> getInIdsDecision() {
        return inIdsDecision;
    }

    public void setInIdsDecision(List<String> inIdsDecision) {
        this.inIdsDecision = inIdsDecision;
    }

    @Override
    public Class<SimpleAnnonceLaprams> whichModelClass() {
        return SimpleAnnonceLaprams.class;
    }

}
