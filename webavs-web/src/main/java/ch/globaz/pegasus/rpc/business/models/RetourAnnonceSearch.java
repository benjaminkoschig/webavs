package ch.globaz.pegasus.rpc.business.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class RetourAnnonceSearch extends JadeSearchComplexModel {

    private String forIdAnnonce;
    private String forIdLot;
    private String forIdDecision;
    private String forCategoryPlausi;
    private String forAnnonceCodeTraitement;

    public String getForIdAnnonce() {
        return forIdAnnonce;
    }

    public void setForIdAnnonce(String forIdAnnonce) {
        this.forIdAnnonce = forIdAnnonce;
    }

    public String getForIdLot() {
        return forIdLot;
    }

    public void setForIdLot(String forIdLot) {
        this.forIdLot = forIdLot;
    }

    public String getForIdDecision() {
        return forIdDecision;
    }

    public void setForIdDecision(String forIdDecision) {
        this.forIdDecision = forIdDecision;
    }

    public String getForCategoryPlausi() {
        return forCategoryPlausi;
    }

    public void setForCategoryPlausi(String forCategoryPlausi) {
        this.forCategoryPlausi = forCategoryPlausi;
    }

    public String getForAnnonceCodeTraitement() {
        return forAnnonceCodeTraitement;
    }

    public void setForAnnonceCodeTraitement(String forAnnonceCodeTraitement) {
        this.forAnnonceCodeTraitement = forAnnonceCodeTraitement;
    }

    @Override
    public Class<RetourAnnonce> whichModelClass() {
        return RetourAnnonce.class;
    }

}
