package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleAnnonceSedexCOPersonneSearch extends JadeSearchSimpleModel {
    private String forIdFamille = null;
    private String forIdContribuable = null;
    private String forIdAnnonceSedexCO = null;

    public String getForIdFamille() {
        return forIdFamille;
    }

    public void setForIdFamille(String forIdFamille) {
        this.forIdFamille = forIdFamille;
    }

    public String getForIdContribuable() {
        return forIdContribuable;
    }

    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    public String getForIdAnnonceSedexCO() {
        return forIdAnnonceSedexCO;
    }

    public void setForIdAnnonceSedexCO(String forIdAnnonceSedexCO) {
        this.forIdAnnonceSedexCO = forIdAnnonceSedexCO;
    }

    @Override
    public Class whichModelClass() {
        return SimpleAnnonceSedexCOPersonne.class;
    }

}
