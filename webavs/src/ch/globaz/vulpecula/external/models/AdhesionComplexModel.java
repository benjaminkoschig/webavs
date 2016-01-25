package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.naos.business.model.AdhesionSimpleModel;
import ch.globaz.naos.business.model.PlanCaisseSimpleModel;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;

/**
 * Représente une adhésion au sens du module NAOS. Le modèle charge également
 * toutes les relations 1-1 qui en découlent.
 * 
 * @author Arnaud Geiser (AGE) | Créé le 6 févr. 2014
 * 
 */
public class AdhesionComplexModel extends JadeComplexModel {
    private AdhesionSimpleModel adhesionSimpleModel;
    private PlanCaisseSimpleModel planCaisseSimpleModel;
    private AdministrationComplexModel administrationForPlanCaisseComplexModel;
    private AdministrationComplexModel administrationForAdhesionComplexModel;

    public AdhesionComplexModel() {
        adhesionSimpleModel = new AdhesionSimpleModel();
        planCaisseSimpleModel = new PlanCaisseSimpleModel();
        administrationForAdhesionComplexModel = new AdministrationComplexModel();
        administrationForPlanCaisseComplexModel = new AdministrationComplexModel();
    }

    public AdhesionSimpleModel getAdhesionSimpleModel() {
        return adhesionSimpleModel;
    }

    public void setAdhesionSimpleModel(AdhesionSimpleModel adhesionSimpleModel) {
        this.adhesionSimpleModel = adhesionSimpleModel;
    }

    public PlanCaisseSimpleModel getPlanCaisseSimpleModel() {
        return planCaisseSimpleModel;
    }

    public void setPlanCaisseSimpleModel(PlanCaisseSimpleModel planCaisseSimpleModel) {
        this.planCaisseSimpleModel = planCaisseSimpleModel;
    }

    public AdministrationComplexModel getAdministrationForPlanCaisseComplexModel() {
        return administrationForPlanCaisseComplexModel;
    }

    public void setAdministrationForPlanCaisseComplexModel(
            AdministrationComplexModel administrationForPlanCaisseComplexModel) {
        this.administrationForPlanCaisseComplexModel = administrationForPlanCaisseComplexModel;
    }

    public AdministrationComplexModel getAdministrationForAdhesionComplexModel() {
        return administrationForAdhesionComplexModel;
    }

    public void setAdministrationForAdhesionComplexModel(
            AdministrationComplexModel administrationForAdhesionComplexModel) {
        this.administrationForAdhesionComplexModel = administrationForAdhesionComplexModel;
    }

    @Override
    public String getId() {
        return adhesionSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return adhesionSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        adhesionSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        adhesionSimpleModel.setSpy(spy);
    }
}
