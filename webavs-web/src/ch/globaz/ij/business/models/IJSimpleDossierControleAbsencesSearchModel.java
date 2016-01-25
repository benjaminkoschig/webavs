package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class IJSimpleDossierControleAbsencesSearchModel extends JadeAbstractSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateDebutFPI;
    private String forDateDebutIJAI;
    private String forIdDossierControleAbsences;
    private String forIdTiers;
    private Boolean forIsHistorise;

    public String getForDateDebutFPI() {
        return forDateDebutFPI;
    }

    public String getForDateDebutIJAI() {
        return forDateDebutIJAI;
    }

    public String getForIdDossierControleAbsences() {
        return forIdDossierControleAbsences;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public Boolean getForIsHistorise() {
        return forIsHistorise;
    }

    public void setForDateDebutFPI(String forDateDebutFPI) {
        this.forDateDebutFPI = forDateDebutFPI;
    }

    public void setForDateDebutIJAI(String forDateDebutIJAI) {
        this.forDateDebutIJAI = forDateDebutIJAI;
    }

    public void setForIdDossierControleAbsences(String forIdDossierControleAbsences) {
        this.forIdDossierControleAbsences = forIdDossierControleAbsences;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public final void setForIsHistorise(boolean forIsHistorise) {
        this.forIsHistorise = forIsHistorise;
    }

    @Override
    public Class<?> whichModelClass() {
        return IJSimpleDossierControleAbsences.class;
    }
}
