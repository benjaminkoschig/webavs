package ch.globaz.vulpecula.domain.models.syndicat;

public class ListeTravailleursSansSyndicat {

    private String numAvs;
    private String nomTravailleur;
    private String prenomTravailleur;
    private String dateNaissance;
    private String idCaisseMetier;
    private String libCaisseMetier;

    public String getIdCaisseMetier() {
        return idCaisseMetier;
    }

    public String getLibCaisseMetier() {
        return libCaisseMetier;
    }

    public void setIdCaisseMetier(String idCaisseMetier) {
        this.idCaisseMetier = idCaisseMetier;
    }

    public void setLibCaisseMetier(String libCaisseMetier) {
        this.libCaisseMetier = libCaisseMetier;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getNomTravailleur() {
        return nomTravailleur;
    }

    public String getPrenomTravailleur() {
        return prenomTravailleur;
    }

    public void setNomTravailleur(String nomTravailleur) {
        this.nomTravailleur = nomTravailleur;
    }

    public void setPrenomTravailleur(String prenomTravailleur) {
        this.prenomTravailleur = prenomTravailleur;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
}
