package ch.globaz.vulpecula.business.models.servicemilitaire;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class TauxServiceMilitaireSearchComplexModel extends JadeSearchComplexModel {

    private static final long serialVersionUID = 1455285716240767484L;

    private String forId;
    private String forIdServiceMilitaire;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdServiceMilitaire() {
        return forIdServiceMilitaire;
    }

    public void setForIdServiceMilitaire(String forIdServiceMilitaire) {
        this.forIdServiceMilitaire = forIdServiceMilitaire;
    }

    @Override
    public Class<TauxServiceMilitaireComplexModel> whichModelClass() {
        return TauxServiceMilitaireComplexModel.class;
    }

}
