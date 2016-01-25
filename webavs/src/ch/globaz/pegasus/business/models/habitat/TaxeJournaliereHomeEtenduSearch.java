package ch.globaz.pegasus.business.models.habitat;

import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;

public class TaxeJournaliereHomeEtenduSearch extends AbstractDonneeFinanciereSearchModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdTiers = null;

    public String getForIdTiers() {
        return forIdTiers;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    @Override
    public Class<TaxeJournaliereHomeEtendu> whichModelClass() {
        return TaxeJournaliereHomeEtendu.class;
    }

}
