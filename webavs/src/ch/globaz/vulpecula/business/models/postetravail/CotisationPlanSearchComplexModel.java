package ch.globaz.vulpecula.business.models.postetravail;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author JPA
 * 
 */
public class CotisationPlanSearchComplexModel extends JadeSearchComplexModel {
    private String forIdAffiliation;

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    @Override
    public Class<CotisationPlanComplexModel> whichModelClass() {
        return CotisationPlanComplexModel.class;
    }
}
