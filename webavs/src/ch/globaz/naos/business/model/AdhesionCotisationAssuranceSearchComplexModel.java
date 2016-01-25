package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class AdhesionCotisationAssuranceSearchComplexModel extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateCotisationDebutLessOrEqual;
    private String forDateCotisationFinNullGreaterOrEqual;
    private String forIdAffiliation;
    private String forTypeAssurance;
    private String forWidgetAnnee;

    public String getForDateCotisationDebutLessOrEqual() {
        return forDateCotisationDebutLessOrEqual;
    }

    public String getForDateCotisationFinNullGreaterOrEqual() {
        return forDateCotisationFinNullGreaterOrEqual;
    }

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public String getForTypeAssurance() {
        return forTypeAssurance;
    }

    public String getForWidgetAnnee() {
        return forWidgetAnnee;
    }

    public void setForDateCotisationDebutLessOrEqual(String forDateCotisationDebutLessOrEqual) {
        this.forDateCotisationDebutLessOrEqual = forDateCotisationDebutLessOrEqual;
    }

    public void setForDateCotisationFinNullGreaterOrEqual(String forDateCotisationFinNullGreaterOrEqual) {
        this.forDateCotisationFinNullGreaterOrEqual = forDateCotisationFinNullGreaterOrEqual;
    }

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    public void setForTypeAssurance(String forTypeAssurance) {
        this.forTypeAssurance = forTypeAssurance;
    }

    public void setForWidgetAnnee(String forWidgetAnnee) {
        this.forWidgetAnnee = forWidgetAnnee;
    }

    @Override
    public Class whichModelClass() {
        return AdhesionCotisationAssuranceComplexModel.class;
    }

}
