package ch.globaz.vulpecula.documents.listesinternes;

public class RecapitulatifParGenreCaisseDTO {
    private String cotisation;
    private String idExterne;
    private String montant;
    private String masse;
    private String genre;
    private String type;
    private String typeLibelle;
    private String idSecteur;

    public String getCotisation() {
        return cotisation;
    }

    public void setCotisation(String cotisation) {
        this.cotisation = cotisation;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeLibelle() {
        return typeLibelle;
    }

    public void setTypeLibelle(String typeLibelle) {
        this.typeLibelle = typeLibelle;
    }

    public String getIdSecteur() {
        return idSecteur;
    }

    public void setIdSecteur(String idSecteur) {
        this.idSecteur = idSecteur;
    }
}
