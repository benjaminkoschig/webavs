package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class DemandePcaPrestationSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String FOR_PCA_COURANTE_VALIDE = "forPcaCouranteValide";
    public final static String WITH_COPIE_PCA = "withCopiePca";
    private String forCsEtatPCA = null;
    private String forDateValable = null;
    private String forIdDemande = null;
    private String forIdDroit = null;
    private String forLessNoVersion = null;

    private String forNoVersion = null;

    public String getForCsEtatPCA() {
        return forCsEtatPCA;
    }

    public String getForDateValable() {
        return forDateValable;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForLessNoVersion() {
        return forLessNoVersion;
    }

    public String getForNoVersion() {
        return forNoVersion;
    }

    public void setForCsEtatPCA(String forCsEtatPCA) {
        this.forCsEtatPCA = forCsEtatPCA;
    }

    public void setForDateValable(String forDateValable) {
        this.forDateValable = forDateValable;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForLessNoVersion(String forLessNoVersion) {
        this.forLessNoVersion = forLessNoVersion;
    }

    public void setForNoVersion(String forNoVersion) {
        this.forNoVersion = forNoVersion;
    }

    @Override
    public Class<DemandePcaPrestation> whichModelClass() {
        return DemandePcaPrestation.class;
    }

}
