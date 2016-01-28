package globaz.aquila.db.rdp.cashin.model;

public class Ecriture {

    private String numeroExterneFacture;
    private String montant;
    private String dateEcriture;
    private String libelle;
    private String typeEcriture;
    private String numeroExterneEcriture;
    private String numeroExterneEcritureAAnnuler;

    public String getNumeroExterneFacture() {
        return numeroExterneFacture;
    }

    public void setNumeroExterneFacture(String numeroExterneFacture) {
        this.numeroExterneFacture = numeroExterneFacture;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getDateEcriture() {
        return dateEcriture;
    }

    public void setDateEcriture(String dateEcriture) {
        this.dateEcriture = dateEcriture;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getTypeEcriture() {
        return typeEcriture;
    }

    public void setTypeEcriture(String typeEcriture) {
        this.typeEcriture = typeEcriture;
    }

    public String getNumeroExterneEcriture() {
        return numeroExterneEcriture;
    }

    public void setNumeroExterneEcriture(String numeroExterneEcriture) {
        this.numeroExterneEcriture = numeroExterneEcriture;
    }

    public String getNumeroExterneEcritureAAnnuler() {
        return numeroExterneEcritureAAnnuler;
    }

    public void setNumeroExterneEcritureAAnnuler(String numeroExterneEcritureAAnnuler) {
        this.numeroExterneEcritureAAnnuler = numeroExterneEcritureAAnnuler;
    }
}
