package ch.globaz.naos.ws.contact;

public class Contact {
    String numeroAffilie;
    String nom;
    String prenom;
    EnumSexe sexe;
    String email;
    boolean stopProspection;

    public Contact() {
        // need for jaxws
    }

    public Contact(String numeroAffilie, String nom, String prenom, EnumSexe sexe, String email, boolean stopProspection) {
        super();
        this.numeroAffilie = numeroAffilie;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.email = email;
        this.stopProspection = stopProspection;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
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

    public EnumSexe getSexe() {
        return sexe;
    }

    public void setSexe(EnumSexe sexe) {
        this.sexe = sexe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getStopProspection() {
        return stopProspection;
    }

    public void setStopProspection(boolean stopProspection) {this.stopProspection = stopProspection;
    }
}
