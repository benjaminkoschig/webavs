package globaz.helios.parser;

/**
 * Représente une ligne du bilan imprimé à l'écran.
 * 
 * @author dda
 * 
 */
public class CGComptePertesProfitsLine {
    private String genreLibelle = new String();
    private String idCompte = new String();

    private String idDomaine = new String();

    private String idExterne = new String();
    private String idGenre = new String();

    private String idNature = new String();
    private String libelle = new String();

    private String soldeCharges = new String();
    private String soldeProduits = new String();

    /**
     * @return
     */
    public String getGenreLibelle() {
        return genreLibelle;
    }

    /**
     * @return
     */
    public String getIdCompte() {
        return idCompte;
    }

    public String getIdDomaine() {
        return idDomaine;
    }

    /**
     * @return
     */
    public String getIdExterne() {
        return idExterne;
    }

    public String getIdGenre() {
        return idGenre;
    }

    public String getIdNature() {
        return idNature;
    }

    /**
     * @return
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * @return
     */
    public String getSoldeCharges() {
        return soldeCharges;
    }

    /**
     * @return
     */
    public String getSoldeProduits() {
        return soldeProduits;
    }

    /**
     * @param s
     */
    public void setGenreLibelle(String s) {
        genreLibelle = s;
    }

    /**
     * @param s
     */
    public void setIdCompte(String s) {
        idCompte = s;
    }

    public void setIdDomaine(String idDomaine) {
        this.idDomaine = idDomaine;
    }

    /**
     * @param s
     */
    public void setIdExterne(String s) {
        idExterne = s;
    }

    public void setIdGenre(String idGenre) {
        this.idGenre = idGenre;
    }

    public void setIdNature(String idNature) {
        this.idNature = idNature;
    }

    /**
     * @param s
     */
    public void setLibelle(String s) {
        libelle = s;
    }

    /**
     * @param string
     */
    public void setSoldeCharges(String s) {
        soldeCharges = s;
    }

    /**
     * @param string
     */
    public void setSoldeProduits(String s) {
        soldeProduits = s;
    }

}
