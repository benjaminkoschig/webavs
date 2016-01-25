package ch.globaz.vulpecula.business.models.parametre;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class ParametreAssureurMaladieSearchSimpleModel extends JadeSearchSimpleModel {
    private String forDesignation = "";
    private String forIdParametreAssureurMaladie = "";
    private String likeDesignation = "";

    /**
     * @return the forDesignation
     */
    public String getForDesignation() {
        return forDesignation;
    }

    /**
     * @return the forIdParametreAssureurMaladie
     */
    public String getForIdParametreAssureurMaladie() {
        return forIdParametreAssureurMaladie;
    }

    /**
     * @return the likeDesignation
     */
    public String getLikeDesignation() {
        return likeDesignation;
    }

    /**
     * @param forDesignation
     *            the forDesignation to set
     */
    public void setForDesignation(String forDesignation) {
        this.forDesignation = JadeStringUtil.convertSpecialChars(forDesignation.toUpperCase());
    }

    /**
     * @param forIdParametreAssureurMaladie
     *            the forIdParametreAssureurMaladie to set
     */
    public void setForIdParametreAssureurMaladie(String forIdParametreAssureurMaladie) {
        this.forIdParametreAssureurMaladie = forIdParametreAssureurMaladie;
    }

    /**
     * @param likeDesignation
     *            the likeDesignation to set
     */
    public void setLikeDesignation(String likeDesignation) {
        this.likeDesignation = JadeStringUtil.convertSpecialChars(likeDesignation.toUpperCase());
    }

    @Override
    public Class<ParametreAssureurMaladieSimpleModel> whichModelClass() {
        return ParametreAssureurMaladieSimpleModel.class;
    }
}
