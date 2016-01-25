package globaz.helios.parser;

/**
 * Représente une ligne du bilan imprimé à l'écran.
 * 
 * @author dda
 * 
 */
public class CGSoldesDesComptesLine {
    private String avoir = new String();
    private String codeISOMonnaie = new String();

    private String doit = new String();
    private String genreLibelle = new String();

    private String idCompte = new String();
    private String idExterne = new String();
    private String idNature = new String();

    private String libelle = new String();
    private String solde = new String();

    /**
     * @return
     */
    public String getAvoir() {
        return avoir;
    }

    /**
     * @return
     */
    public String getCodeISOMonnaie() {
        return codeISOMonnaie;
    }

    /**
     * @return
     */
    public String getDoit() {
        return doit;
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

    /**
     * @return
     */
    public String getIdExterne() {
        return idExterne;
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
    public String getSolde() {
        return solde;
    }

    /**
     * @param s
     */
    public void setAvoir(String s) {
        avoir = s;
    }

    /**
     * @param string
     */
    public void setCodeISOMonnaie(String s) {
        codeISOMonnaie = s;
    }

    /**
     * @param s
     */
    public void setDoit(String s) {
        doit = s;
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

    /**
     * @param s
     */
    public void setIdExterne(String s) {
        idExterne = s;
    }

    /**
     * @param string
     */
    public void setIdNature(String string) {
        idNature = string;
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
    public void setSolde(String s) {
        solde = s;
    }

}
