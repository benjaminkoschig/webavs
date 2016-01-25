package ch.globaz.vulpecula.business.models.syndicat;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;

public class ParametreSyndicatComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 296136892431105455L;

    private ParametreSyndicatSimpleModel parametreSyndicatSimpleModel;
    private AdministrationComplexModel administrationSyndicatComplexModel;
    private AdministrationComplexModel administrationCaisseMetierComplexModel;

    public ParametreSyndicatComplexModel() {
        parametreSyndicatSimpleModel = new ParametreSyndicatSimpleModel();
        administrationSyndicatComplexModel = new AdministrationComplexModel();
        administrationCaisseMetierComplexModel = new AdministrationComplexModel();
    }

    public ParametreSyndicatSimpleModel getParametreSyndicatSimpleModel() {
        return parametreSyndicatSimpleModel;
    }

    public void setParametreSyndicatSimpleModel(ParametreSyndicatSimpleModel parametreSyndicatSimpleModel) {
        this.parametreSyndicatSimpleModel = parametreSyndicatSimpleModel;
    }

    public AdministrationComplexModel getAdministrationSyndicatComplexModel() {
        return administrationSyndicatComplexModel;
    }

    public void setAdministrationSyndicatComplexModel(AdministrationComplexModel administrationSyndicatComplexModel) {
        this.administrationSyndicatComplexModel = administrationSyndicatComplexModel;
    }

    public AdministrationComplexModel getAdministrationCaisseMetierComplexModel() {
        return administrationCaisseMetierComplexModel;
    }

    public void setAdministrationCaisseMetierComplexModel(
            AdministrationComplexModel administrationCaisseMetierComplexModel) {
        this.administrationCaisseMetierComplexModel = administrationCaisseMetierComplexModel;
    }

    @Override
    public String getId() {
        return parametreSyndicatSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return parametreSyndicatSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        parametreSyndicatSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        parametreSyndicatSimpleModel.setSpy(spy);
    }
}
