package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class IJSimplePrononceSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsGenreReadaptation;
    private String forDateDebutPrononce;
    private String forDateFinPrononce;
    private String forIdDemande;
    private String forIdParent;
    private String forIdPrononce;

    public IJSimplePrononceSearchModel() {
        forIdPrononce = "";
        forDateDebutPrononce = "";
        forDateFinPrononce = "";
        forCsGenreReadaptation = "";
        forIdParent = "";
        forIdDemande = "";
    }

    public String getForCsGenreReadaptation() {
        return forCsGenreReadaptation;
    }

    public String getForDateDebutPrononce() {
        return forDateDebutPrononce;
    }

    public String getForDateFinPrononce() {
        return forDateFinPrononce;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdParent() {
        return forIdParent;
    }

    public String getForIdPrononce() {
        return forIdPrononce;
    }

    public void setForCsGenreReadaptation(String forCsGenreReadaptation) {
        this.forCsGenreReadaptation = forCsGenreReadaptation;
    }

    public void setForDateDebutPrononce(String forDateDebutPrononce) {
        this.forDateDebutPrononce = forDateDebutPrononce;
    }

    public void setForDateFinPrononce(String forDateFinPrononce) {
        this.forDateFinPrononce = forDateFinPrononce;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdParent(String forIdParent) {
        this.forIdParent = forIdParent;
    }

    public void setForIdPrononce(String forIdPrononce) {
        this.forIdPrononce = forIdPrononce;
    }

    @Override
    public Class<?> whichModelClass() {
        return IJSimplePrononce.class;
    }
}
