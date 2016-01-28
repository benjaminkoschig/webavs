package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class AdaptationPsalSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forCsEtat = null;
    private String forMontantCotisationsAnnuelle = null;
    private String forNumeroVersion = null;

    public String getForCsEtat() {
        return forCsEtat;
    }

    public String getForMontantCotisationsAnnuelle() {
        return forMontantCotisationsAnnuelle;
    }

    public String getForNumeroVersion() {
        return forNumeroVersion;
    }

    public void setForCsEtat(String forCsEtat) {
        this.forCsEtat = forCsEtat;
    }

    public void setForMontantCotisationsAnnuelle(String forMontantCotisationsAnnuelle) {
        this.forMontantCotisationsAnnuelle = forMontantCotisationsAnnuelle;
    }

    public void setForNumeroVersion(String forNumeroVersion) {
        this.forNumeroVersion = forNumeroVersion;
    }

    @Override
    public Class<AdaptationPsal> whichModelClass() {
        return AdaptationPsal.class;
    }

}
