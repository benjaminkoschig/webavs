package ch.globaz.pegasus.business.models.recap;

import java.math.BigDecimal;

public class RecapituatifPaiement {
    private BigDecimal montantPresation;
    private int nbDossier;

    public BigDecimal getMontantPresation() {
        return montantPresation;
    }

    public int getNbDossier() {
        return nbDossier;
    }

    public void setMontantPresation(BigDecimal montantPresation) {
        this.montantPresation = montantPresation;
    }

    public void setNbDossier(int nbDossier) {
        this.nbDossier = nbDossier;
    }
}
