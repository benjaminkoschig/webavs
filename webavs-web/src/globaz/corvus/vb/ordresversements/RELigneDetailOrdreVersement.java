package globaz.corvus.vb.ordresversements;

import globaz.framework.util.FWCurrency;

public class RELigneDetailOrdreVersement {

    private String designation;
    private String idOrdreVersement;
    private boolean isCompensationInterDecision;
    private boolean isCompense;
    private String montant;
    private String montantDette;
    private String typeOrdreVersement;

    public RELigneDetailOrdreVersement() {
        super();

        designation = "";
        idOrdreVersement = "";
        isCompense = false;
        isCompensationInterDecision = false;
        montant = "";
        montantDette = "";
        typeOrdreVersement = "";
    }

    public String getDesignation() {
        return designation;
    }

    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public String getMontant() {
        if (isCompense || isCompensationInterDecision) {
            return new FWCurrency(montant).toStringFormat();
        }
        return "0.00";
    }

    public String getMontantDette() {
        return new FWCurrency(montantDette).toStringFormat();
    }

    public String getTypeOrdreVersement() {
        return typeOrdreVersement;
    }

    public boolean isCompensationInterDecision() {
        return isCompensationInterDecision;
    }

    public boolean isCompense() {
        return isCompense;
    }

    public boolean isMontantDettePlusGrandQueZero() {
        return new FWCurrency(getMontantDette()).compareTo(new FWCurrency()) > 0;
    }

    public boolean isMontantPlusGrandQueZero() {
        return new FWCurrency(getMontant()).compareTo(new FWCurrency()) > 0;
    }

    public void setCompensationInterDecision(boolean isCompensationInterDecision) {
        this.isCompensationInterDecision = isCompensationInterDecision;
    }

    public void setCompense(boolean isCompense) {
        this.isCompense = isCompense;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantDette(String montantDette) {
        this.montantDette = montantDette;
    }

    public void setTypeOrdreVersement(String typeOrdreVersement) {
        this.typeOrdreVersement = typeOrdreVersement;
    }
}
