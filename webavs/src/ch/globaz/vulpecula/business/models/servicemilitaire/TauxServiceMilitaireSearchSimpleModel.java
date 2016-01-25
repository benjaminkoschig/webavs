package ch.globaz.vulpecula.business.models.servicemilitaire;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class TauxServiceMilitaireSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = 832885298303091260L;

    private String forId;
    private String forIdServiceMilitaire;
    private String forIdCotisation;

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

    public String getForIdCotisation() {
        return forIdCotisation;
    }

    public void setForIdCotisation(String forIdCotisation) {
        this.forIdCotisation = forIdCotisation;
    }

    @Override
    public Class<TauxServiceMilitaireSimpleModel> whichModelClass() {
        return TauxServiceMilitaireSimpleModel.class;
    }

}
