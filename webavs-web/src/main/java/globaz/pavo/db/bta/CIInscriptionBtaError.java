package globaz.pavo.db.bta;

public class CIInscriptionBtaError {

    private String error;
    private String nnssRequerant;
    private String nomRequerant;
    private String prenomRequerant;

    public CIInscriptionBtaError(String nnssRequerant, String nomRequerant, String prenomRequerant, String error) {
        this.nnssRequerant = nnssRequerant;
        this.nomRequerant = nomRequerant;
        this.prenomRequerant = prenomRequerant;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public String getNnssRequerant() {
        return nnssRequerant;
    }

    public String getNomRequerant() {
        return nomRequerant;
    }

    public String getPrenomRequerant() {
        return prenomRequerant;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setNnssRequerant(String nnssRequerant) {
        this.nnssRequerant = nnssRequerant;
    }

    public void setNomRequerant(String nomRequerant) {
        this.nomRequerant = nomRequerant;
    }

    public void setParam(String nnssRequerant, String nomRequerant, String prenomRequerant, String error) {
        setNnssRequerant(nnssRequerant);
        setNomRequerant(nomRequerant);
        setPrenomRequerant(prenomRequerant);
        setError(error);
    }

    public void setPrenomRequerant(String prenomRequerant) {
        this.prenomRequerant = prenomRequerant;
    }
}
