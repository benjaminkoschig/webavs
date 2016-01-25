package ch.globaz.amal.business.models.uploadfichierreprise;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleUploadFichierRepriseSearch extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCustomValue = null;
    private String forIdJob = null;
    private String forIdUploadFichierReprise = null;
    private String forNoContribuable = null;
    private String likeTypeReprise = null;
    private String likeTypeRepriseContribuable = null;
    private String likeTypeReprisePersonneCharge = null;

    public String getForCustomValue() {
        return forCustomValue;
    }

    public String getForIdJob() {
        return forIdJob;
    }

    public String getForIdUploadFichierReprise() {
        return forIdUploadFichierReprise;
    }

    public String getForNoContribuable() {
        return forNoContribuable;
    }

    public String getLikeTypeReprise() {
        return likeTypeReprise;
    }

    public String getLikeTypeRepriseContribuable() {
        return likeTypeRepriseContribuable;
    }

    public String getLikeTypeReprisePersonneCharge() {
        return likeTypeReprisePersonneCharge;
    }

    public void setForCustomValue(String forCustomValue) {
        this.forCustomValue = forCustomValue;
    }

    public void setForIdJob(String forIdJob) {
        this.forIdJob = forIdJob;
    }

    public void setForIdUploadFichierReprise(String forIdUploadFichierReprise) {
        this.forIdUploadFichierReprise = forIdUploadFichierReprise;
    }

    public void setForNoContribuable(String forNoContribuable) {
        this.forNoContribuable = forNoContribuable;
    }

    public void setLikeTypeReprise(String likeTypeReprise) {
        this.likeTypeReprise = likeTypeReprise;
    }

    public void setLikeTypeRepriseContribuable(String likeTypeRepriseContribuable) {
        this.likeTypeRepriseContribuable = likeTypeRepriseContribuable;
    }

    public void setLikeTypeReprisePersonneCharge(String likeTypeReprisePersonneCharge) {
        this.likeTypeReprisePersonneCharge = likeTypeReprisePersonneCharge;
    }

    @Override
    public Class whichModelClass() {
        return SimpleUploadFichierReprise.class;
    }

}
