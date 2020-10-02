package ch.globaz.pegasus.primeassurancemaladie;

public class PrimeAssuranceMaladieFromCSV {

    private String nss;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String montant;
    private String nomCaisse;
    private boolean isPrimeMoyenne;

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }


    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNomCaisse() {
        return nomCaisse;
    }

    public void setNomCaisse(String nomCaisse) {
        this.nomCaisse = nomCaisse;
    }

    public boolean isPrimeMoyenne() {
        return isPrimeMoyenne;
    }

    public void setPrimeMoyenne(boolean primeMoyenne) {
        isPrimeMoyenne = primeMoyenne;
    }
}
