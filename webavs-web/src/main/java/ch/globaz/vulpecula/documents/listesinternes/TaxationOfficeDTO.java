package ch.globaz.vulpecula.documents.listesinternes;

public class TaxationOfficeDTO {
    private String idExterne;
    private String masse;
    private String taux;
    private String montant;
    private String code;
    private String date;

    public TaxationOfficeDTO() {

    }

    public TaxationOfficeDTO(String idExterne, String masse, String taux, String montant, String code, String date) {
        this.idExterne = idExterne;
        this.masse = masse;
        this.taux = taux;
        this.montant = montant;
        this.code = code;
        this.date = date;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getMasse() {
        return masse;
    }

    public String getTaux() {
        return taux;
    }

    public String getMontant() {
        return montant;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setMasse(String masse) {
        this.masse = masse;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
