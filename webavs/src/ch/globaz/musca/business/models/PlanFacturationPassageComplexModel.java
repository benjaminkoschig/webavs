package ch.globaz.musca.business.models;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author Arnaud Geiser (AGE) | Créé le 20 févr. 2014
 * 
 */
public class PlanFacturationPassageComplexModel extends JadeComplexModel {
    private PlanFacturationSimpleModel planFacturationSimpleModel;
    private ModuleFacturationSimpleModel moduleFacturationSimpleModel;
    private PassageModel passageModel;

    public PlanFacturationPassageComplexModel() {
        planFacturationSimpleModel = new PlanFacturationSimpleModel();
        moduleFacturationSimpleModel = new ModuleFacturationSimpleModel();
        passageModel = new PassageModel();
    }

    public PassageModel getPassageModel() {
        return passageModel;
    }

    public void setPassageModel(PassageModel passageModel) {
        this.passageModel = passageModel;
    }

    @Override
    public String getId() {
        return passageModel.getId();
    }

    @Override
    public String getSpy() {
        return passageModel.getSpy();
    }

    @Override
    public void setId(String id) {
    }

    @Override
    public void setSpy(String spy) {
    }

    public PlanFacturationSimpleModel getPlanFacturationSimpleModel() {
        return planFacturationSimpleModel;
    }

    public void setPlanFacturationSimpleModel(PlanFacturationSimpleModel planFacturationSimpleModel) {
        this.planFacturationSimpleModel = planFacturationSimpleModel;
    }

    public ModuleFacturationSimpleModel getModuleFacturationSimpleModel() {
        return moduleFacturationSimpleModel;
    }

    public void setModuleFacturationSimpleModel(ModuleFacturationSimpleModel moduleFacturationSimpleModel) {
        this.moduleFacturationSimpleModel = moduleFacturationSimpleModel;
    }

}
