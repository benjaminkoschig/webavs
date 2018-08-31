package ch.globaz.vulpecula.documents.listesinternes;

public class RecapitulatifDTO {
    private String idExterne;
    private String libelle;
    private String montant;
    private String masse;
    private String type;
    private String taux;

    public String getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getMasse() {
        return masse;
    }

    public void setMasse(String masse) {
        this.masse = masse;
    }

    public String getTaux() {
        return taux;
    }

    public String getType() {
        return type;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }

    public void setType(String type) {
        this.type = type;
    }

}
