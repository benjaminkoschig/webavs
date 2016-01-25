package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class IJPeriodeControleAbsencesSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDossier;
    private String forIdTiers;

    public IJPeriodeControleAbsencesSearchModel() {
        super();

        forIdDossier = null;
        forIdTiers = null;
    }

    public String getForIdDossier() {
        return forIdDossier;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    @Override
    public Class<?> whichModelClass() {
        return IJPeriodeControleAbsences.class;
    }
}
