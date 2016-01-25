package ch.globaz.prestation.business.models.echance;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class EcheanceSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String FOR_A_TRAITER = "forATraiter";

    public static final String FOR_NEAREST_TERM = "forNearestTerm";

    private String forIdExterne;
    private String forIdTiers;
    private String forCsDomaine;
    private String forCsEtat;
    private String forCsTypeEcheance;

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

    @Override
    public Class<EcheanceModel> whichModelClass() {
        return EcheanceModel.class;
    }

    @Override
    public String toString() {
        return "EcheanceSearch [forIdExterne=" + forIdExterne + ", forIdTiers=" + forIdTiers + ", forCsDomaine="
                + forCsDomaine + ", forCsEtat=" + forCsEtat + ", forCsTypeEcheance=" + forCsTypeEcheance + "]";
    }

}
