package ch.globaz.vulpecula.business.models.syndicat;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.vulpecula.business.models.travailleur.TravailleurComplexModel;

public class AffiliationSyndicatComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 8142883174655927291L;

    private AffiliationSyndicatSimpleModel affiliationSyndicatSimpleModel;
    private TravailleurComplexModel travailleurComplexModel;
    private AdministrationComplexModel administrationComplexModel;

    public AffiliationSyndicatComplexModel() {
        affiliationSyndicatSimpleModel = new AffiliationSyndicatSimpleModel();
        travailleurComplexModel = new TravailleurComplexModel();
        administrationComplexModel = new AdministrationComplexModel();
    }

    @Override
    public String getId() {
        return affiliationSyndicatSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return affiliationSyndicatSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        affiliationSyndicatSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        affiliationSyndicatSimpleModel.setSpy(spy);
    }

    public AffiliationSyndicatSimpleModel getAffiliationSyndicatSimpleModel() {
        return affiliationSyndicatSimpleModel;
    }

    public void setAffiliationSyndicatSimpleModel(AffiliationSyndicatSimpleModel affiliationSyndicatSimpleModel) {
        this.affiliationSyndicatSimpleModel = affiliationSyndicatSimpleModel;
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

}
