package ch.globaz.pegasus.business.models.habitat;

import globaz.jade.persistence.model.JadeSearchComplexModel;

public class TaxeJournaliereHomeDroitSearch extends JadeSearchComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String WITH_LAPRMAS = "forVersionedForLaprams";
    public final static String WITH_VERSIONED = "forVersioned";
    private String forCsEtatDemande;
    private String forCsTypeDonneeFinanciere;
    private String forDate;
    private String forDateFin;
    private String forIdDroit;
    private String forIdDroitMemembreFamill;
    private String forIdHome;
    private String forNoVersionDroit;

    public String getForCsEtatDemande() {
        return forCsEtatDemande;
    }

    public String getForCsTypeDonneeFinanciere() {
        return forCsTypeDonneeFinanciere;
    }

    public String getForDate() {
        return forDate;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public String getForIdDroit() {
        return forIdDroit;
    }

    public String getForIdDroitMemembreFamill() {
        return forIdDroitMemembreFamill;
    }

    public String getForIdHome() {
        return forIdHome;
    }

    public String getForNoVersionDroit() {
        return forNoVersionDroit;
    }

    public void setForCsEtatDemande(String forCsEtatDemande) {
        this.forCsEtatDemande = forCsEtatDemande;
    }

    public void setForCsTypeDonneeFinanciere(String forCsTypeDonneeFinanciere) {
        this.forCsTypeDonneeFinanciere = forCsTypeDonneeFinanciere;
    }

    public void setForDate(String forDate) {
        this.forDate = forDate;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForIdDroitMemembreFamill(String forIdDroitMemembreFamill) {
        this.forIdDroitMemembreFamill = forIdDroitMemembreFamill;
    }

    public void setForIdHome(String forIdHome) {
        this.forIdHome = forIdHome;
    }

    public void setForNoVersionDroit(String forNoVersionDroit) {
        this.forNoVersionDroit = forNoVersionDroit;
    }

    @Override
    public Class<TaxeJournaliereHomeDroit> whichModelClass() {
        return TaxeJournaliereHomeDroit.class;
    }
}
