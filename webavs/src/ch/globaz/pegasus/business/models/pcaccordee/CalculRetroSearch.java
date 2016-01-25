package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class CalculRetroSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FOR_CURRENT_VERSIONED_WITHOUT_COPIE = "forCurrentPcaForRetroWithoutCopie";
    public static final String FOR_OLD_VERSIONED_PCA_WITH_MONTANT_MENSUELLE_FOR_DECOMPTE = "forOldVersionnedPcaWithMontantMensuelleForDecompte";

    private String forDateDebut = null;
    private String forDateMax = null;
    private String forDateMin = null;
    private String forExcludePcaPartiel = null;
    private String forIdDemande = null;
    private String forIdVersionDroit = null;
    private String forNoVersion = null;

    public String getForDateDebut() {
        return forDateDebut;
    }

    public String getForDateMax() {
        return forDateMax;
    }

    public String getForDateMin() {
        return forDateMin;
    }

    public String getForExcludePcaPartiel() {
        return forExcludePcaPartiel;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public String getForNoVersion() {
        return forNoVersion;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public void setForDateMax(String forDateMax) {
        this.forDateMax = forDateMax;
    }

    public void setForDateMin(String forDateMin) {
        this.forDateMin = forDateMin;
    }

    public void setForExcludePcaPartiel(String forExcludePcaPartiel) {
        this.forExcludePcaPartiel = forExcludePcaPartiel;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setForNoVersion(String forNoVersion) {
        this.forNoVersion = forNoVersion;
    }

    @Override
    public Class<CalculRetro> whichModelClass() {
        return CalculRetro.class;
    }

}
