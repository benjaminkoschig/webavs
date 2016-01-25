package globaz.apg.pojo;

import java.io.Serializable;

public class PrestationVerseeLignePrestationPojo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut;
    private String dateFin;
    private String datePaiement;
    private String genrePrestationLibelle;
    private String genreService;
    private String montantBrut;
    private String nom;
    private String nss;
    private String prenom;

    public PrestationVerseeLignePrestationPojo() {
        super();
        dateDebut = "";
        dateFin = "";
        datePaiement = "";
        genreService = "";
        montantBrut = "0";
        nom = "";
        nss = "";
        prenom = "";
        genrePrestationLibelle = "";

    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDatePaiement() {
        return datePaiement;
    }

    public String getGenrePrestationLibelle() {
        return genrePrestationLibelle;
    }

    public String getGenreService() {
        return genreService;
    }

    public String getMontantBrut() {
        return montantBrut;
    }

    public String getNom() {
        return nom;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDatePaiement(String datePaiement) {
        this.datePaiement = datePaiement;
    }

    public void setGenrePrestationLibelle(String genrePrestationLibelle) {
        this.genrePrestationLibelle = genrePrestationLibelle;
    }

    public void setGenreService(String genreService) {
        this.genreService = genreService;
    }

    public void setMontantBrut(String montantBrut) {
        this.montantBrut = montantBrut;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

}
