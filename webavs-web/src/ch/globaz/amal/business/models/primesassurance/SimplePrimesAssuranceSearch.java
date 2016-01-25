package ch.globaz.amal.business.models.primesassurance;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimplePrimesAssuranceSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeHistorique = null;
    private String forIdTiers = null;
    private String forNoCaisseMaladie = null;

    public String getForAnneeHistorique() {
        return forAnneeHistorique;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForNoCaisseMaladie() {
        return forNoCaisseMaladie;
    }

    public void setForAnneeHistorique(String forAnneeHistorique) {
        this.forAnneeHistorique = forAnneeHistorique;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForNoCaisseMaladie(String forNoCaisseMaladie) {
        this.forNoCaisseMaladie = forNoCaisseMaladie;
    }

    @Override
    public Class whichModelClass() {
        return SimplePrimesAssurance.class;
    }

}
