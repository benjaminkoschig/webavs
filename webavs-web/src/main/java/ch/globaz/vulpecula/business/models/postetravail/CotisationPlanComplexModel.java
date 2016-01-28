package ch.globaz.vulpecula.business.models.postetravail;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.naos.business.model.AssuranceSimpleModel;
import ch.globaz.naos.business.model.CotisationSimpleModel;
import ch.globaz.naos.business.model.PlanAffiliationSimpleModel;

/**
 * @author JPA
 * 
 */
public class CotisationPlanComplexModel extends JadeComplexModel {

    private AssuranceSimpleModel assurance = null;

    private CotisationSimpleModel cotisation = null;

    private PlanAffiliationSimpleModel plan = null;

    public CotisationPlanComplexModel() {
        super();
        cotisation = new CotisationSimpleModel();
        plan = new PlanAffiliationSimpleModel();
        assurance = new AssuranceSimpleModel();
    }

    public AssuranceSimpleModel getAssurance() {
        return assurance;
    }

    public CotisationSimpleModel getCotisation() {
        return cotisation;
    }

    @Override
    public String getId() {
        return cotisation.getId();
    }

    public PlanAffiliationSimpleModel getPlan() {
        return plan;
    }

    @Override
    public String getSpy() {
        return cotisation.getSpy();
    }

    public void setAssurance(AssuranceSimpleModel assurance) {
        this.assurance = assurance;
    }

    public void setCotisation(CotisationSimpleModel cotisation) {
        this.cotisation = cotisation;
    }

    @Override
    public void setId(String id) {
        cotisation.setId(id);
    }

    public void setPlan(PlanAffiliationSimpleModel plan) {
        this.plan = plan;
    }

    @Override
    public void setSpy(String spy) {
        cotisation.setSpy(spy);
    }
}
