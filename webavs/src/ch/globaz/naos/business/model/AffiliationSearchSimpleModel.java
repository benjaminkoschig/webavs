package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class AffiliationSearchSimpleModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forDateValidite;
    private String forIdTiers;
    /*
     * Properties
     */
    private String forNumeroAffilie;
    private String forNumeroAffilieLike;

    public String getForDateValidite() {
        return forDateValidite;
    }

    /*
     * Getter et Setter
     */

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    public String getForNumeroAffilieLike() {
        return forNumeroAffilieLike;
    }

    public void setForDateValidite(String forDateValidite) {
        this.forDateValidite = forDateValidite;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    public void setForNumeroAffilieLike(String forNumeroAffilieLike) {
        this.forNumeroAffilieLike = forNumeroAffilieLike;
    }

    @Override
    public Class whichModelClass() {
        return AffiliationSimpleModel.class;
    }

}
