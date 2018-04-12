package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;

public class LigneCotisation {
    private String libelleCotisation;
    private Double taux;
    private BigDecimal montant;
    private BigDecimal montantBaseCalcul;

    public String getLibelleCotisation() {
        return libelleCotisation;
    }

    public void setLibelleCotisation(String libelleCotisation) {
        this.libelleCotisation = libelleCotisation;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getMontantBaseCalcul() {
        return montantBaseCalcul;
    }

    public void setMontantBaseCalcul(BigDecimal montantBaseCalcul) {
        this.montantBaseCalcul = montantBaseCalcul;
    }

}
