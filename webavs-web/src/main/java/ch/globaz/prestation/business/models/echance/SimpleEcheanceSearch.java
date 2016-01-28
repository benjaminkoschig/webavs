package ch.globaz.prestation.business.models.echance;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

@SuppressWarnings("serial")
public class SimpleEcheanceSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdExterne;
    private String forIdTiers;
    private String forCsDomaine;
    private String forCsEtat;
    private String forCsTypeEcheance;
    private String forDateDeTraitement;
    private String forDateEcheance;

    public String getForIdExterne() {
        return forIdExterne;
    }

    public void setForIdExterne(String forIdExterne) {
        this.forIdExterne = forIdExterne;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public String getForCsDomaine() {
        return forCsDomaine;
    }

    public void setForCsDomaine(String forCsDomaine) {
        this.forCsDomaine = forCsDomaine;
    }

    public String getForCsEtat() {
        return forCsEtat;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public String getForCsTypeEcheance() {
        return forCsTypeEcheance;
    }

    public void setForCsTypeEcheance(String forCsTypeEcheance) {
        this.forCsTypeEcheance = forCsTypeEcheance;
    }

    public String getForDateDeTraitement() {
        return forDateDeTraitement;
    }

    public void setForDateDeTraitement(String forDateDeTraitement) {
        this.forDateDeTraitement = forDateDeTraitement;
    }

    public String getForDateEcheance() {
        return forDateEcheance;
    }

    public void setForDateEcheance(String forDateEcheance) {
        this.forDateEcheance = forDateEcheance;
    }

    @Override
    public Class<SimpleEcheance> whichModelClass() {
        return SimpleEcheance.class;
    }

    @Override
    public String toString() {
        return "SimpleEcheanceSearch [forIdExterne=" + forIdExterne + ", forIdTiers=" + forIdTiers + ", forCsDomaine="
                + forCsDomaine + ", forCsEtat=" + forCsEtat + ", forCsTypeEcheance=" + forCsTypeEcheance
                + ", forDateDeTraitement=" + forDateDeTraitement + ", forDateEcheance=" + forDateEcheance + "]";
    }

}
