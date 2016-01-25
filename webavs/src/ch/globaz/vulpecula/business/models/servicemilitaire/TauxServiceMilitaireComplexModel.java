package ch.globaz.vulpecula.business.models.servicemilitaire;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.naos.business.model.AssuranceSimpleModel;

public class TauxServiceMilitaireComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 4617816839394304049L;

    private TauxServiceMilitaireSimpleModel tauxServiceMilitaireSimpleModel;
    private AssuranceSimpleModel assuranceSimpleModel;

    public TauxServiceMilitaireComplexModel() {
        tauxServiceMilitaireSimpleModel = new TauxServiceMilitaireSimpleModel();
        assuranceSimpleModel = new AssuranceSimpleModel();
    }

    public TauxServiceMilitaireSimpleModel getTauxServiceMilitaireSimpleModel() {
        return tauxServiceMilitaireSimpleModel;
    }

    public void setTauxServiceMilitaireSimpleModel(TauxServiceMilitaireSimpleModel tauxServiceMilitaireSimpleModel) {
        this.tauxServiceMilitaireSimpleModel = tauxServiceMilitaireSimpleModel;
    }

    public AssuranceSimpleModel getAssuranceSimpleModel() {
        return assuranceSimpleModel;
    }

    public void setAssuranceSimpleModel(AssuranceSimpleModel assuranceSimpleModel) {
        this.assuranceSimpleModel = assuranceSimpleModel;
    }

    @Override
    public String getId() {
        return tauxServiceMilitaireSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return tauxServiceMilitaireSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        tauxServiceMilitaireSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        tauxServiceMilitaireSimpleModel.setSpy(spy);
    }

}
