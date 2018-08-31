package ch.globaz.vulpecula.web.views.decomptesalaire;

public class DecompteSalaireDetailsAnnuelView {
    private String annee;
    private String salaireAnnuel;
    private String taux;
    private String cotisationBrute;
    private String cotisationNette;
    private String pourcentage;
    private String frais;

    public String getAnnee() {
        return annee;
    }

    public String getSalaireAnnuel() {
        return salaireAnnuel;
    }

    public String getTaux() {
        return taux;
    }

    public String getCotisationBrute() {
        return cotisationBrute;
    }

    public String getCotisationNette() {
        return cotisationNette;
    }

    public String getPourcentage() {
        return pourcentage;
    }

    public String getFrais() {
        return frais;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setSalaireAnnuel(String salaireAnnuel) {
        this.salaireAnnuel = salaireAnnuel;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }

    public void setCotisationBrute(String cotisationBrute) {
        this.cotisationBrute = cotisationBrute;
    }

    public void setCotisationNette(String cotisationNette) {
        this.cotisationNette = cotisationNette;
    }

    public void setPourcentage(String pourcentage) {
        this.pourcentage = pourcentage;
    }

    public void setFrais(String frais) {
        this.frais = frais;
    }
}
