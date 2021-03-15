package ch.globaz.pegasus.rpc.business.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class AnnonceDecisionSearch extends JadeSearchComplexModel {

    private String forIdAnnonce;
    private String forEtat;
    private String forIdLot;

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

    public String getForEtat() {
        return forEtat;
    }

    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    @Override
    public Class<AnnonceDecision> whichModelClass() {
        return AnnonceDecision.class;
    }

}
