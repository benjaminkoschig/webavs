package ch.globaz.vulpecula.external.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.naos.business.model.AdhesionSimpleModel;
import ch.globaz.naos.business.model.AssuranceSimpleModel;
import ch.globaz.naos.business.model.CotisationSimpleModel;
import ch.globaz.naos.business.model.PlanCaisseSimpleModel;

public class CotisationComplexModel extends JadeComplexModel {
    public static final String SEARCH_BY_ID = "searchById";

    private CotisationSimpleModel cotisationSimpleModel;
    private PlanCaisseSimpleModel planCaisseSimpleModel;
    private AssuranceSimpleModel assuranceSimpleModel;
    private AdhesionSimpleModel adhesionSimpleModel;

    public CotisationComplexModel() {
        cotisationSimpleModel = new CotisationSimpleModel();
        planCaisseSimpleModel = new PlanCaisseSimpleModel();
        assuranceSimpleModel = new AssuranceSimpleModel();
        adhesionSimpleModel = new AdhesionSimpleModel();
    }

    @Override
    public String getId() {
        return cotisationSimpleModel.getId();
    }

    @Override
    public String getSpy() {
        return cotisationSimpleModel.getSpy();
    }

    @Override
    public void setId(String id) {
        cotisationSimpleModel.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        cotisationSimpleModel.setSpy(spy);
    }

    public CotisationSimpleModel getCotisationSimpleModel() {
        return cotisationSimpleModel;
    }

    public void setCotisationSimpleModel(CotisationSimpleModel cotisationSimpleModel) {
        this.cotisationSimpleModel = cotisationSimpleModel;
    }

    public PlanCaisseSimpleModel getPlanCaisseSimpleModel() {
        return planCaisseSimpleModel;
    }

    public void setPlanCaisseSimpleModel(PlanCaisseSimpleModel planCaisseSimpleModel) {
        this.planCaisseSimpleModel = planCaisseSimpleModel;
    }

    public AssuranceSimpleModel getAssuranceSimpleModel() {
        return assuranceSimpleModel;
    }

    public void setAssuranceSimpleModel(AssuranceSimpleModel assuranceSimpleModel) {
        this.assuranceSimpleModel = assuranceSimpleModel;
    }

    public AdhesionSimpleModel getAdhesionSimpleModel() {
        return adhesionSimpleModel;
    }

    public void setAdhesionSimpleModel(AdhesionSimpleModel adhesionSimpleModel) {
        this.adhesionSimpleModel = adhesionSimpleModel;
    }
}
