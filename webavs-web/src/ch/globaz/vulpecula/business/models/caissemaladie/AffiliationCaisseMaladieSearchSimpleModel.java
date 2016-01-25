package ch.globaz.vulpecula.business.models.caissemaladie;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class AffiliationCaisseMaladieSearchSimpleModel extends JadeSearchSimpleModel {
    private static final long serialVersionUID = -5624826917268077576L;

    private String forId;
    private String forIdTravailleur;

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

    public String getForIdTravailleur() {
        return forIdTravailleur;
    }

    public void setForIdTravailleur(String forIdTravailleur) {
        this.forIdTravailleur = forIdTravailleur;
    }

    @Override
    public Class<AffiliationCaisseMaladieSimpleModel> whichModelClass() {
        return AffiliationCaisseMaladieSimpleModel.class;
    }

}
