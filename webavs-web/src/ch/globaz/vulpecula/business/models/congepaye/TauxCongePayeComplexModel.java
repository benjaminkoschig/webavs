package ch.globaz.vulpecula.business.models.congepaye;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.naos.business.model.AssuranceSimpleModel;

public class TauxCongePayeComplexModel extends JadeComplexModel {
    private static final long serialVersionUID = 1077971888027075482L;

    private TauxCongePayeSimpleModel tauxCongePayeSimpleModel;
    private AssuranceSimpleModel assuranceSimpleModel;

    public TauxCongePayeComplexModel() {
        tauxCongePayeSimpleModel = new TauxCongePayeSimpleModel();
        assuranceSimpleModel = new AssuranceSimpleModel();
    }

    @Override
    public String getId() {
        return tauxCongePayeSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return tauxCongePayeSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        tauxCongePayeSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        tauxCongePayeSimpleModel.setSpy(spy);
    }

    public TauxCongePayeSimpleModel getTauxCongePayeSimpleModel() {
        return tauxCongePayeSimpleModel;
    }

    public void setTauxCongePayeSimpleModel(TauxCongePayeSimpleModel tauxCongePayeSimpleModel) {
        this.tauxCongePayeSimpleModel = tauxCongePayeSimpleModel;
    }

    public AssuranceSimpleModel getAssuranceSimpleModel() {
        return assuranceSimpleModel;
    }

    public void setAssuranceSimpleModel(AssuranceSimpleModel assuranceSimpleModel) {
        this.assuranceSimpleModel = assuranceSimpleModel;
    }
}
