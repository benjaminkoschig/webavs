package ch.globaz.vulpecula.business.models.caissemaladie;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;

public class SuiviCaisseMaladieComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 8033266323797408947L;

    private SuiviCaisseMaladieSimpleModel suiviCaisseMaladieSimpleModel;
    private TravailleurComplexModel travailleurComplexModel;
    private AdministrationComplexModel administrationComplexModel;

    public SuiviCaisseMaladieComplexModel() {
        suiviCaisseMaladieSimpleModel = new SuiviCaisseMaladieSimpleModel();
        travailleurComplexModel = new TravailleurComplexModel();
        administrationComplexModel = new AdministrationComplexModel();
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

    public SuiviCaisseMaladieSimpleModel getSuiviCaisseMaladieSimpleModel() {
        return suiviCaisseMaladieSimpleModel;
    }

    public void setSuiviCaisseMaladieSimpleModel(SuiviCaisseMaladieSimpleModel suiviCaisseMaladieSimpleModel) {
        this.suiviCaisseMaladieSimpleModel = suiviCaisseMaladieSimpleModel;
    }

    @Override
    public String getId() {
        return suiviCaisseMaladieSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return suiviCaisseMaladieSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        suiviCaisseMaladieSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        suiviCaisseMaladieSimpleModel.setSpy(spy);
    }

}
