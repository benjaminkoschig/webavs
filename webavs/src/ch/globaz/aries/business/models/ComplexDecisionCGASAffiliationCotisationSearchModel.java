package ch.globaz.aries.business.models;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class ComplexDecisionCGASAffiliationCotisationSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateFinAffiliationGreater = null;
    private String forDateFinCotisationGreater = null;
    private String forDateFinDecision = null;
    private String forGenreAssurance = null;
    private String forNotEtatDecision = null;
    private String forNumeroAffilieGreaterEqual = null;
    private String forNumeroAffilieLessEqual = null;
    private String forTypeAssurance = null;

    private List<String> inEtatDecision = null;

    public String getForDateFinAffiliationGreater() {
        return forDateFinAffiliationGreater;
    }

    public String getForDateFinCotisationGreater() {
        return forDateFinCotisationGreater;
    }

    public String getForDateFinDecision() {
        return forDateFinDecision;
    }

    public String getForGenreAssurance() {
        return forGenreAssurance;
    }

    public String getForNotEtatDecision() {
        return forNotEtatDecision;
    }

    public String getForNumeroAffilieGreaterEqual() {
        return forNumeroAffilieGreaterEqual;
    }

    public String getForNumeroAffilieLessEqual() {
        return forNumeroAffilieLessEqual;
    }

    public String getForTypeAssurance() {
        return forTypeAssurance;
    }

    public List<String> getInEtatDecision() {
        return inEtatDecision;
    }

    public void setForDateFinAffiliationGreater(String forDateFinAffiliationGreater) {
        this.forDateFinAffiliationGreater = forDateFinAffiliationGreater;
    }

    public void setForDateFinCotisationGreater(String forDateFinCotisationGreater) {
        this.forDateFinCotisationGreater = forDateFinCotisationGreater;
    }

    public void setForDateFinDecision(String forDateFinDecision) {
        this.forDateFinDecision = forDateFinDecision;
    }

    public void setForGenreAssurance(String forGenreAssurance) {
        this.forGenreAssurance = forGenreAssurance;
    }

    public void setForNotEtatDecision(String forNotEtatDecision) {
        this.forNotEtatDecision = forNotEtatDecision;
    }

    public void setForNumeroAffilieGreaterEqual(String forNumeroAffilieGreaterEqual) {
        this.forNumeroAffilieGreaterEqual = forNumeroAffilieGreaterEqual;
    }

    public void setForNumeroAffilieLessEqual(String forNumeroAffilieLessEqual) {
        this.forNumeroAffilieLessEqual = forNumeroAffilieLessEqual;
    }

    public void setForTypeAssurance(String forTypeAssurance) {
        this.forTypeAssurance = forTypeAssurance;
    }

    public void setInEtatDecision(List<String> inEtatDecision) {
        this.inEtatDecision = inEtatDecision;
    }

    @Override
    public Class<ComplexDecisionCGASAffiliationCotisation> whichModelClass() {
        return ComplexDecisionCGASAffiliationCotisation.class;
    }

}
