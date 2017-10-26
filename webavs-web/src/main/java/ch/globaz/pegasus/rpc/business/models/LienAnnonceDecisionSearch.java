package ch.globaz.pegasus.rpc.business.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;
import java.util.HashSet;

public class LienAnnonceDecisionSearch extends JadeSearchComplexModel {

    private Collection<String> forIdsAnnonce = new HashSet<String>();

    public Collection<String> getForIdsAnnonce() {
        return forIdsAnnonce;
    }

    public void setForIdsAnnonce(Collection<String> forIdsAnnonce) {
        this.forIdsAnnonce = forIdsAnnonce;
    }

    @Override
    public Class<LienAnnonceDecision> whichModelClass() {
        return LienAnnonceDecision.class;
    }

}
