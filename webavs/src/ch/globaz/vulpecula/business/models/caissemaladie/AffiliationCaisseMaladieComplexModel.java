package ch.globaz.vulpecula.business.models.caissemaladie;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;

public class AffiliationCaisseMaladieComplexModel extends JadeComplexModel {
    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    private static final long serialVersionUID = 8033266323797408947L;

    private AffiliationCaisseMaladieSimpleModel affiliationCaisseMaladieSimpleModel;
    private TravailleurComplexModel travailleurComplexModel;
    private AdministrationComplexModel administrationComplexModel;
    private String idPosteTravail = "";

    public AffiliationCaisseMaladieComplexModel() {
        affiliationCaisseMaladieSimpleModel = new AffiliationCaisseMaladieSimpleModel();
        travailleurComplexModel = new TravailleurComplexModel();
        administrationComplexModel = new AdministrationComplexModel();
    }

    public AffiliationCaisseMaladieSimpleModel getAffiliationCaisseMaladieSimpleModel() {
        return affiliationCaisseMaladieSimpleModel;
    }

    public void setAffiliationCaisseMaladieSimpleModel(
            AffiliationCaisseMaladieSimpleModel affiliationCaisseMaladieSimpleModel) {
        this.affiliationCaisseMaladieSimpleModel = affiliationCaisseMaladieSimpleModel;
    }

    public TravailleurComplexModel getTravailleurComplexModel() {
        return travailleurComplexModel;
    }

    public void setTravailleurComplexModel(TravailleurComplexModel travailleurComplexModel) {
        this.travailleurComplexModel = travailleurComplexModel;
    }

    public AdministrationComplexModel getAdministrationComplexModel() {
        return administrationComplexModel;
    }

    public void setAdministrationComplexModel(AdministrationComplexModel administrationComplexModel) {
        this.administrationComplexModel = administrationComplexModel;
    }

    @Override
    public String getId() {
        return affiliationCaisseMaladieSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return affiliationCaisseMaladieSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        affiliationCaisseMaladieSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        affiliationCaisseMaladieSimpleModel.setSpy(spy);
    }
}
