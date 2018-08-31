package ch.globaz.vulpecula.business.models.ebusiness;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.vulpecula.business.models.postetravail.PosteTravailComplexModel;

public class SynchronisationTravailleurEbuComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = -8211011976310870907L;

    private PosteTravailComplexModel posteTravailComplexModel;
    private SynchronisationTravailleurEbuSimpleModel synchronisationTravailleurEbuSimpleModel;
    private TravailleurEbuSimpleModel travailleurEbuSimpleModel;

    public SynchronisationTravailleurEbuComplexModel() {
        super();
        posteTravailComplexModel = new PosteTravailComplexModel();
        synchronisationTravailleurEbuSimpleModel = new SynchronisationTravailleurEbuSimpleModel();
    }

    public SynchronisationTravailleurEbuSimpleModel getSynchronisationTravailleurEbuSimpleModel() {
        return synchronisationTravailleurEbuSimpleModel;
    }

    public void setSynchronisationTravailleurEbuSimpleModel(
            SynchronisationTravailleurEbuSimpleModel synchronisationTravailleurEbuModel) {
        synchronisationTravailleurEbuSimpleModel = synchronisationTravailleurEbuModel;
    }

    public PosteTravailComplexModel getPosteTravailComplexModel() {
        return posteTravailComplexModel;
    }

    public void setPosteTravailComplexModel(PosteTravailComplexModel posteTravailComplexModel) {
        this.posteTravailComplexModel = posteTravailComplexModel;
    }

    @Override
    public String getId() {
        return synchronisationTravailleurEbuSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return synchronisationTravailleurEbuSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        synchronisationTravailleurEbuSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        synchronisationTravailleurEbuSimpleModel.setSpy(spy);
    }

    public TravailleurEbuSimpleModel getTravailleurEbuSimpleModel() {
        return travailleurEbuSimpleModel;
    }

    public void setTravailleurEbuSimpleModel(TravailleurEbuSimpleModel travailleurEbuSimpleModel) {
        this.travailleurEbuSimpleModel = travailleurEbuSimpleModel;
    }

}
