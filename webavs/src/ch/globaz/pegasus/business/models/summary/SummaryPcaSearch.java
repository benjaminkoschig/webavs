package ch.globaz.pegasus.business.models.summary;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class SummaryPcaSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtatPca;
    private String forDateValable;
    private String forIdTiers;

    public String getForCsEtatPca() {
        return forCsEtatPca;
    }

    public String getForDateValable() {
        return forDateValable;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public void setForCsEtatPca(String forCsEtatPca) {
        this.forCsEtatPca = forCsEtatPca;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    @Override
    public Class<SummaryPca> whichModelClass() {
        return SummaryPca.class;
    }

}
