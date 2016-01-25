package ch.globaz.musca.business.models;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche sur les modèles simples des plans de facturation
 * 
 * @author JPA
 * 
 */
public class PlanFacturationSearchSimpleModel extends JadeSearchSimpleModel {
    private String forIdPlanFacturation;

    public String getForIdPlanFacturation() {
        return forIdPlanFacturation;
    }

    public void setForIdPlanFacturation(String forIdPlanFacturation) {
        this.forIdPlanFacturation = forIdPlanFacturation;
    }

    @Override
    public Class<PlanFacturationSimpleModel> whichModelClass() {
        return PlanFacturationSimpleModel.class;
    }

}
