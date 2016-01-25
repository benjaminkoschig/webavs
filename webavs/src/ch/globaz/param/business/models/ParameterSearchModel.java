package ch.globaz.param.business.models;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class ParameterSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String ORDER_DEFINITION_ECRAN = "fromEcranGestion";
    public static String SEARCH_DEFINITION_ECRAN = "fromEcranGestion";

    private String forDateDebutValidite = null;
    private String forIdActeurParametre = null;
    private String forIdApplParametre = null;
    private String forIdCleDiffere = null;
    private String forIdCodeSysteme = null;
    private String forIdTypeCode = null;
    private String forPlageValParametre = null;
    // private String forPlageValFinParametre = null;
    private String forUniteParametre = null;
    private String forValeurAlphaParametre = null;
    private String forValeurNumParametre = null;
    private String likeDesignationParametre = null;

    public String getForDateDebutValidite() {
        return forDateDebutValidite;
    }

    public String getForIdActeurParametre() {
        return forIdActeurParametre;
    }

    public String getForIdApplParametre() {
        return forIdApplParametre;
    }

    public String getForIdCleDiffere() {
        return forIdCleDiffere;
    }

    public String getForIdCodeSysteme() {
        return forIdCodeSysteme;
    }

    public String getForIdTypeCode() {
        return forIdTypeCode;
    }

    public String getForPlageValParametre() {
        return forPlageValParametre;
    }

    public String getForUniteParametre() {
        return forUniteParametre;
    }

    public String getForValeurAlphaParametre() {
        return forValeurAlphaParametre;
    }

    public String getForValeurNumParametre() {
        return forValeurNumParametre;
    }

    public String getLikeDesignationParametre() {
        return likeDesignationParametre;
    }

    public void setForDateDebutValidite(String forDateDebutValidite) {
        this.forDateDebutValidite = forDateDebutValidite;
    }

    public void setForIdActeurParametre(String forIdActeurParametre) {
        this.forIdActeurParametre = forIdActeurParametre;
    }

    public void setForIdApplParametre(String forIdApplParametre) {
        this.forIdApplParametre = forIdApplParametre;
    }

    public void setForIdCleDiffere(String forIdCleDiffere) {
        this.forIdCleDiffere = forIdCleDiffere;
    }

    public void setForIdCodeSysteme(String forIdCodeSysteme) {
        this.forIdCodeSysteme = forIdCodeSysteme;
    }

    public void setForIdTypeCode(String forIdTypeCode) {
        this.forIdTypeCode = forIdTypeCode;
    }

    public void setForPlageValParametre(String forPlageValParametre) {
        this.forPlageValParametre = forPlageValParametre;
    }

    public void setForUniteParametre(String forUniteParametre) {
        this.forUniteParametre = forUniteParametre;
    }

    public void setForValeurAlphaParametre(String forValeurAlphaParametre) {
        this.forValeurAlphaParametre = forValeurAlphaParametre;
    }

    public void setForValeurNumParametre(String forValeurNumParametre) {
        this.forValeurNumParametre = forValeurNumParametre;
    }

    public void setLikeDesignationParametre(String likeDesignationParametre) {
        this.likeDesignationParametre = likeDesignationParametre;
    }

    @Override
    public Class whichModelClass() {
        return ParameterModel.class;
    }

}
