package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;

public class CotisationPersonnelleWebAvs {
    private String libelle;
    private BigDecimal montantDeterminant;
    private double taux;
    private BigDecimal cotisation;
    private String periodicite;

    public CotisationPersonnelleWebAvs() {

    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public BigDecimal getCotisation() {
        return cotisation;
    }

    public void setCotisation(BigDecimal cotisation) {
        this.cotisation = cotisation;
    }

    public BigDecimal getMontantDeterminant() {
        return montantDeterminant;
    }

    public void setMontantDeterminant(BigDecimal montantDeterminant) {
        this.montantDeterminant = montantDeterminant;
    }

    public String getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

}
