package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class AssuranceCouverteSearchComplexModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateCotisation; // entre debut et fin date coti
    private String forGenreAssurance; // ex : paritaire ou indep.
    private String forNumeroAffilie; // entre debut et fin date coti
    private String forTypeAssurance; // ex : AF
    private String forValeurNumParamAssurance;

    public String getForDateCotisation() {
        return forDateCotisation;
    }

    public String getForGenreAssurance() {
        return forGenreAssurance;
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

    public void setForDateCotisation(String forDateCotisation) {
        this.forDateCotisation = forDateCotisation;
    }

    public void setForGenreAssurance(String forGenreAssurance) {
        this.forGenreAssurance = forGenreAssurance;
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

    @Override
    public Class whichModelClass() {
        return AssuranceCouverteComplexModel.class;
    }

}
