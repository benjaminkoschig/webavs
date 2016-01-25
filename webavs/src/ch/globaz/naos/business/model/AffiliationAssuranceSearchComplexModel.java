package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class AffiliationAssuranceSearchComplexModel extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateAffiliation;
    private String forDateCotisation; // entre debut et fin date coti
    private String forDateCotisationDebutLessEqualFinGreaterNull = null;
    private String forGenreAssurance; // ex : paritaire ou indep.
    private String forIdAffiliation;
    private String forNumeroAffilie; // entre debut et fin date coti
    private String forTypeAssurance; // ex : AF
    private String forValeurNumParamAssurance;
    private List<String> inTypeAssurance = null;
    private String forDateFinCotisationGreaterEqual = null;

    public String getForDateFinCotisationGreaterEqual() {
        return forDateFinCotisationGreaterEqual;
    }

    public void setForDateFinCotisationGreaterEqual(String forDateFinCotisationGreaterEqual) {
        this.forDateFinCotisationGreaterEqual = forDateFinCotisationGreaterEqual;
    }

    public String getForDateAffiliation() {
        return forDateAffiliation;
    }

    public String getForDateCotisation() {
        return forDateCotisation;
    }

    public String getForDateCotisationDebutLessEqualFinGreaterNull() {
        return forDateCotisationDebutLessEqualFinGreaterNull;
    }

    public String getForGenreAssurance() {
        return forGenreAssurance;
    }

    public String getForIdAffiliation() {
        return forIdAffiliation;
    }

    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    public String getForTypeAssurance() {
        return forTypeAssurance;
    }

    public String getForValeurNumParamAssurance() {
        return forValeurNumParamAssurance;
    }

    public List<String> getInTypeAssurance() {
        return inTypeAssurance;
    }

    public void setForDateAffiliation(String forDateAffiliation) {
        this.forDateAffiliation = forDateAffiliation;
    }

    public void setForDateCotisation(String forDateCotisation) {
        this.forDateCotisation = forDateCotisation;
    }

    public void setForDateCotisationDebutLessEqualFinGreaterNull(String forDateCotisationDebutLessEqualFinGreaterNull) {
        this.forDateCotisationDebutLessEqualFinGreaterNull = forDateCotisationDebutLessEqualFinGreaterNull;
    }

    public void setForGenreAssurance(String forGenreAssurance) {
        this.forGenreAssurance = forGenreAssurance;
    }

    public void setForIdAffiliation(String forIdAffiliation) {
        this.forIdAffiliation = forIdAffiliation;
    }

    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    public void setForTypeAssurance(String forTypeAssurance) {
        this.forTypeAssurance = forTypeAssurance;
    }

    public void setForValeurNumParamAssurance(String forValeurNumParamAssurance) {
        this.forValeurNumParamAssurance = forValeurNumParamAssurance;
    }

    public void setInTypeAssurance(List<String> inTypeAssurance) {
        this.inTypeAssurance = inTypeAssurance;
    }

    @Override
    public Class whichModelClass() {
        return AffiliationAssuranceComplexModel.class;
    }

}
