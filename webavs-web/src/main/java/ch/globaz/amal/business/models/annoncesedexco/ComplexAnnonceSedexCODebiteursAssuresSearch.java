package ch.globaz.amal.business.models.annoncesedexco;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class ComplexAnnonceSedexCODebiteursAssuresSearch extends JadeSearchComplexModel {
    private static final long serialVersionUID = 5867414265875259866L;
    private String forIdSedexCO = null;
    private String forMessageSubtype = null;
    private String forStatementYear = null;
    private String likeNssDebiteur = null;
    private String likeNssAssure = null;
    private String forIdTiersCM = null;
    private String forIdContribuable = null;
    private String forDateAnnonceLOE = null;
    private String forDateAnnonceGOE = null;
    private String forMessageSubType = null;

    @Override
    public Class whichModelClass() {
        return ComplexAnnonceSedexCODebiteursAssures.class;
    }

    public String getForIdSedexCO() {
        return forIdSedexCO;
    }

    public void setForIdSedexCO(String forIdSedexCO) {
        this.forIdSedexCO = forIdSedexCO;
    }

    public String getForMessageSubtype() {
        return forMessageSubtype;
    }

    public void setForMessageSubtype(String forMessageSubtype) {
        this.forMessageSubtype = forMessageSubtype;
    }

    public String getForStatementYear() {
        return forStatementYear;
    }

    public void setForStatementYear(String forStatementYear) {
        this.forStatementYear = forStatementYear;
    }

    public String getLikeNssDebiteur() {
        return likeNssDebiteur;
    }

    public void setLikeNssDebiteur(String likeNssDebiteur) {
        this.likeNssDebiteur = likeNssDebiteur;
    }

    public String getLikeNssAssure() {
        return likeNssAssure;
    }

    public void setLikeNssAssure(String likeNssAssure) {
        this.likeNssAssure = likeNssAssure;
    }

    public String getForIdTiersCM() {
        return forIdTiersCM;
    }

    public void setForIdTiersCM(String forIdTiersCM) {
        this.forIdTiersCM = forIdTiersCM;
    }

    public String getForIdContribuable() {
        return forIdContribuable;
    }

    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    public String getForDateAnnonceLOE() {
        return forDateAnnonceLOE;
    }

    public void setForDateAnnonceLOE(String forDateAnnonceLOE) {
        this.forDateAnnonceLOE = forDateAnnonceLOE;
    }

    public String getForDateAnnonceGOE() {
        return forDateAnnonceGOE;
    }

    public void setForDateAnnonceGOE(String forDateAnnonceGOE) {
        this.forDateAnnonceGOE = forDateAnnonceGOE;
    }

    public String getForMessageSubType() {
        return forMessageSubType;
    }

    public void setForMessageSubType(String forMessageSubType) {
        this.forMessageSubType = forMessageSubType;
    }

}
