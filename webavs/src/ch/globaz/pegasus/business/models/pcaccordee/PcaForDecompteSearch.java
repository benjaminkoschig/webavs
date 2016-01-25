package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class PcaForDecompteSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FOR_OLD_VERSIONED_PCA_WITH_MONTANT_MENSUELLE_FOR_DECOMPTE = "forOldVersionnedPcaWithMontantMensuelleForDecompte";
    public static final String ORDER_BY_DATE_DEBUT_AND_CS_ROLE = "byDateDebutAndCsRole";

    private String forDateMax;
    private String forDateMin;
    private String forIdDemande;
    private String forIdDroit;
    private String forNoVersion;

    public String getForDateMax() {
        return forDateMax;
    }

    public String getForDateMin() {
        return forDateMin;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForNoVersion() {
        return forNoVersion;
    }

    public void setForDateMax(String forDateMax) {
        this.forDateMax = forDateMax;
    }

    public void setForDateMin(String forDateMin) {
        this.forDateMin = forDateMin;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForNoVersion(String forNoVersion) {
        this.forNoVersion = forNoVersion;
    }

    @Override
    public Class<PcaForDecompte> whichModelClass() {
        return PcaForDecompte.class;
    }
}
