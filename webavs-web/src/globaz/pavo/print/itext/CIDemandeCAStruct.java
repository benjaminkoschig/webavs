package globaz.pavo.print.itext;

public class CIDemandeCAStruct {

    private String adresse = "";
    private boolean isEmpty = false;
    private String langue = "";
    private String numAffilie = "";

    private String politesse = "";

    public CIDemandeCAStruct() {
    }

    public String getAdresse() {
        return adresse;
    }

    public String getLangue() {
        return langue;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getPolitesse() {
        return politesse;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setPolitesse(String politesse) {
        this.politesse = politesse;
    }
}
