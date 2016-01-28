package globaz.helios.parser;

/**
 * Représente une ligne du bilan imprimé à l'écran.
 * 
 * @author dda
 * 
 */
public class CGBilanLine {
    private String codeISOMonnaie = new String();
    private String genreLibelle = new String();

    private String idCompte = new String();
    private String idDomaine = new String();
    private String idExterne = new String();

    private String idGenre = new String();
    private String idNature = new String();

    private String libelle = new String();
    private String soldeActif = new String();

    private String soldeMonnaie = new String();
    private String soldePassif = new String();

    /**
     * @return
     */
    public String getCodeISOMonnaie() {
        return codeISOMonnaie;
    }

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

    /**
     * @return
     */
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
    public String getSoldeActif() {
        return soldeActif;
    }

    /**
     * @return
     */
    public String getSoldeMonnaie() {
        return soldeMonnaie;
    }

    /**
     * @return
     */
    public String getSoldePassif() {
        return soldePassif;
    }

    /**
     * @param s
     */
    public void setCodeISOMonnaie(String s) {
        codeISOMonnaie = s;
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

    /**
     * @param s
     */
    public void setIdNature(String s) {
        idNature = s;
    }

    /**
     * @param s
     */
    public void setLibelle(String s) {
        libelle = s;
    }

    /**
     * @param s
     */
    public void setSoldeActif(String s) {
        soldeActif = s;
    }

    /**
     * @param s
     */
    public void setSoldeMonnaie(String s) {
        soldeMonnaie = s;
    }

    /**
     * @param s
     */
    public void setSoldePassif(String s) {
        soldePassif = s;
    }

    @Override
    public String toString() {
        return "IdExterne : " + getIdExterne() + ", Libelle : " + getLibelle();
    }

}
