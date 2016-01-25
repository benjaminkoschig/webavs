package ch.globaz.pegasus.businessimpl.services.process.adaptation;

public class Home {
    private String idHome;
    private String numero;
    private String libelle;
    private String idTiersHome;
    private String idTiersBeneficiaire;

    public Home() {
    }

    public String getIdTiersHome() {
        return idTiersHome;
    }

    public void setIdTiersHome(String idTiersHome) {
        this.idTiersHome = idTiersHome;
    }

    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public void setIdTiersBeneficiaire(String idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public String getIdHome() {
        return idHome;
    }

    public void setIdHome(String idHome) {
        this.idHome = idHome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

}
