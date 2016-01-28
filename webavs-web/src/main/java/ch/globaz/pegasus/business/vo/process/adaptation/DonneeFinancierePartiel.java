package ch.globaz.pegasus.business.vo.process.adaptation;

public class DonneeFinancierePartiel {
    private String donation;
    private String montantRentes;
    private String nbEnfant;
    private String prixHome;
    private String prixHomeConjoint;

    public String getDonation() {
        return donation;
    }

    public String getMontantRentes() {
        return montantRentes;
    }

    public String getNbEnfant() {
        return nbEnfant;
    }

    public String getPrixHome() {
        return prixHome;
    }

    public String getPrixHomeConjoint() {
        return prixHomeConjoint;
    }

    public void setDonation(String donation) {
        this.donation = donation;
    }

    public void setMontantRentes(String montantRentes) {
        this.montantRentes = montantRentes;
    }

    public void setNbEnfant(String nbEnfant) {
        this.nbEnfant = nbEnfant;
    }

    public void setPrixHome(String prixHome) {
        this.prixHome = prixHome;
    }

    public void setPrixHomeConjoint(String prixHomeConjoint) {
        this.prixHomeConjoint = prixHomeConjoint;
    }
}
