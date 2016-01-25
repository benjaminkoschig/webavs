package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Modèle complexe joignant un plan affiliation aux cotisations / assurance
 * 
 * @author gmo
 * 
 */
public class PlanAffiliationCotisationComplexModel extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AssuranceSimpleModel assurance = null;
    private CotisationSimpleModel cotisation = null;
    private PlanAffiliationSimpleModel planAffiliation = null;

    public PlanAffiliationCotisationComplexModel() {
        super();
        planAffiliation = new PlanAffiliationSimpleModel();
        cotisation = new CotisationSimpleModel();
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
        return planAffiliation.getId();
    }

    public PlanAffiliationSimpleModel getPlanAffiliation() {
        return planAffiliation;
    }

    @Override
    public String getSpy() {
        return planAffiliation.getSpy();
    }

    public void setAssurance(AssuranceSimpleModel assurance) {
        this.assurance = assurance;
    }

    public void setCotisation(CotisationSimpleModel cotisation) {
        this.cotisation = cotisation;
    }

    @Override
    public void setId(String id) {
        planAffiliation.setId(id);

    }

    public void setPlanAffiliation(PlanAffiliationSimpleModel planAffiliation) {
        this.planAffiliation = planAffiliation;
    }

    @Override
    public void setSpy(String spy) {
        planAffiliation.setSpy(spy);

    }

}
