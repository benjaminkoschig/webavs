package ch.globaz.pegasus.rpc.business.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class AnnonceDecisionSearch extends JadeSearchComplexModel {

    private String forIdAnnonce;

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

    @Override
    public Class<AnnonceDecision> whichModelClass() {
        return AnnonceDecision.class;
    }

}
