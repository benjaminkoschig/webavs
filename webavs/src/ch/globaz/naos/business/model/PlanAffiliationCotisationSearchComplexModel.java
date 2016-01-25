package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Modèle complexe de recherche sur <code>PlanAffiliationCotisationComplexModel</code>
 * 
 * @author gmo
 * 
 */
public class PlanAffiliationCotisationSearchComplexModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAffiliationId = null;
    private String forDateCotisation = null;
    private String forTypeAssurance = null;

    public String getForAffiliationId() {
        return forAffiliationId;
    }

    public String getForDateCotisation() {
        return forDateCotisation;
    }

    public String getForTypeAssurance() {
        return forTypeAssurance;
    }

    public void setForAffiliationId(String forAffiliationId) {
        this.forAffiliationId = forAffiliationId;
    }

    public void setForDateCotisation(String forDateCotisation) {
        this.forDateCotisation = forDateCotisation;
    }

    public void setForTypeAssurance(String forTypeAssurance) {
        this.forTypeAssurance = forTypeAssurance;
    }

    @Override
    public Class whichModelClass() {
        return PlanAffiliationCotisationComplexModel.class;
    }

}
