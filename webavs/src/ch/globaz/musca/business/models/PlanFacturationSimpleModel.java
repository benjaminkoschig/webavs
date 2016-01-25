package ch.globaz.musca.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle représentant un plan de facturation
 * 
 * @author JPA
 * 
 */
public class PlanFacturationSimpleModel extends JadeSimpleModel {
    private String idPlanFacturation = "";
    private String idModFac = "";

    public String getIdModFac() {
        return idModFac;
    }

    public void setIdModFac(String idModFac) {
        this.idModFac = idModFac;
    }

    public String getIdPlanFacturation() {
        return idPlanFacturation;
    }

    public void setIdPlanFacturation(String idPlanFacturation) {
        this.idPlanFacturation = idPlanFacturation;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }
}
