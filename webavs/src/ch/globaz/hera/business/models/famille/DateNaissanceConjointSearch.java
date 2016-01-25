package ch.globaz.hera.business.models.famille;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DateNaissanceConjointSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdMembreFamille = null;
    private String forIdMembreFamilleIn = null;
    private String forNss = null;

    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    public void setForIdMembreFamille(String forIdMembreFamille) {
        this.forIdMembreFamille = forIdMembreFamille;
    }

    public String getForIdMembreFamilleIn() {
        return forIdMembreFamilleIn;
    }

    public void setForIdMembreFamilleIn(String forIdMembreFamilleIn) {
        this.forIdMembreFamilleIn = forIdMembreFamilleIn;
    }

    public String getForNss() {
        return forNss;
    }

    public void setForNss(String forNss) {
        this.forNss = forNss;
    }

    @Override
    public Class whichModelClass() {
        return DateNaissanceConjoint.class;
    }

}
