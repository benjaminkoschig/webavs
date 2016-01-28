package globaz.pavo.db.bta;

public class CIInscriptionRetroBtaLog {
    private String annee;
    private String fraction;
    private String nnssRequerant;
    private String nomRequerant;
    private String prenomRequerant;

    public CIInscriptionRetroBtaLog(String annee, String nnssRequerant, String nomRequerant, String prenomRequerant,
            String fraction) {
        this.annee = annee;
        this.nnssRequerant = nnssRequerant;
        this.nomRequerant = nomRequerant;
        this.prenomRequerant = prenomRequerant;
        this.fraction = fraction;
    }

    public String getAnnee() {
        return annee;
    }

    public String getFraction() {
        return fraction;
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

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setFraction(String fraction) {
        this.fraction = fraction;
    }

    public void setNnssRequerant(String nnssRequerant) {
        this.nnssRequerant = nnssRequerant;
    }

    public void setNomRequerant(String nomRequerant) {
        this.nomRequerant = nomRequerant;
    }

    public void setParam(String annee, String nnssRequerant, String nomRequerant, String prenomRequerant,
            String fraction) {
        setAnnee(annee);
        setNnssRequerant(nnssRequerant);
        setNomRequerant(nomRequerant);
        setPrenomRequerant(prenomRequerant);
        setFraction(fraction);
    }

    public void setPrenomRequerant(String prenomRequerant) {
        this.prenomRequerant = prenomRequerant;
    }

}
