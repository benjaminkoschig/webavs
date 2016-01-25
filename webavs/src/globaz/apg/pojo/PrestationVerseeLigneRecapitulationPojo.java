package globaz.apg.pojo;

import java.io.Serializable;

public class PrestationVerseeLigneRecapitulationPojo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String genrePrestationLibelle;
    private String genreService;
    private String libelleGenreService;
    private String montantBrut;
    private String nombreCas;

    public PrestationVerseeLigneRecapitulationPojo() {
        super();
        genreService = "";
        libelleGenreService = "";
        montantBrut = "0";
        nombreCas = "0";
        genrePrestationLibelle = "";
    }

    public String getGenrePrestationLibelle() {
        return genrePrestationLibelle;
    }

    public String getGenreService() {
        return genreService;
    }

    public String getLibelleGenreService() {
        return libelleGenreService;
    }

    public String getMontantBrut() {
        return montantBrut;
    }

    public String getNombreCas() {
        return nombreCas;
    }

    public void setGenrePrestationLibelle(String genrePrestationLibelle) {
        this.genrePrestationLibelle = genrePrestationLibelle;
    }

    public void setGenreService(String genreService) {
        this.genreService = genreService;
    }

    public void setLibelleGenreService(String libelleGenreService) {
        this.libelleGenreService = libelleGenreService;
    }

    public void setMontantBrut(String montantBrut) {
        this.montantBrut = montantBrut;
    }

    public void setNombreCas(String nombreCas) {
        this.nombreCas = nombreCas;
    }

}
