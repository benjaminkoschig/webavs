package ch.globaz.pegasus.rpc.business.models;

import java.util.Collection;
import java.util.HashSet;
import ch.globaz.common.persistence.DomaineJadeAbstractSearchModel;

public class SimpleLienAnnonceDecisionSearch extends DomaineJadeAbstractSearchModel {
    private Collection<String> forIdsAnnonce = new HashSet<String>();
    private String forIdAnnonce;

    @Override
    public Class<SimpleLienAnnonceDecision> whichModelClass() {
        return SimpleLienAnnonceDecision.class;
    }

    public Collection<String> getForIdsAnnonce() {
        return forIdsAnnonce;
    }

    public void setForIdsAnnonce(Collection<String> forIdsAnnonce) {
        this.forIdsAnnonce = forIdsAnnonce;
    }

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

}
