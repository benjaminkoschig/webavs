package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PCAPrestationSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FOR_OLD_VERSIONNED_PCA = "forOldVersionnedPca";
    private String forDateMax;
    private String forDateMin;
    private String forIdDroit;
    private String lessNoVersionDroit;

    public String getForDateMax() {
        return forDateMax;
    }

    public String getForDateMin() {
        return forDateMin;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getLessNoVersionDroit() {
        return lessNoVersionDroit;
    }

    public void setForDateMax(String forDateMax) {
        this.forDateMax = forDateMax;
    }

    public void setForDateMin(String forDateMin) {
        this.forDateMin = forDateMin;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setLessNoVersionDroit(String lesNoVersionDroit) {
        lessNoVersionDroit = lesNoVersionDroit;
    }

    @Override
    public Class<PCAPrestation> whichModelClass() {
        return PCAPrestation.class;
    }

}
