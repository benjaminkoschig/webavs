package globaz.pavo.print.itext;

public class CIConcordanceEmployeStruc {

    private String DateNaiss = "";
    private String Nnss = "";
    private String NoAvs = "";
    private String NomPrenom = "";
    private String Sexe = "";

    /**
     * Structure de données de l'employe pour l'impression CSV/PDF
     */
    public CIConcordanceEmployeStruc() {
    }

    public String getDateNaiss() {
        return DateNaiss;
    }

    public String getNnss() {
        return Nnss;
    }

    public String getNoAvs() {
        return NoAvs;
    }

    public String getNomPrenom() {
        return NomPrenom;
    }

    public String getSexe() {
        return Sexe;
    }

    public void setDateNaiss(String dateNaiss) {
        DateNaiss = dateNaiss;
    }

    public void setNnss(String nnss) {
        Nnss = nnss;
    }

    public void setNoAvs(String noAvs) {
        NoAvs = noAvs;
    }

    public void setNomPrenom(String nomPrenom) {
        NomPrenom = nomPrenom;
    }

    public void setSexe(String sexe) {
        Sexe = sexe;
    }

}
