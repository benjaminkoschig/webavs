package globaz.pavo.db.upidaily;

public class CIUpiDailyLog {

    private String ligne;
    private String messageErreur;
    private String nomFichier;
    private String numLigne;

    public CIUpiDailyLog(String nomFichier, String numLigne, String ligne, String messageErreur) {
        setNomFichier(nomFichier);
        setNumLigne(numLigne);
        setLigne(ligne);
        setMessageErreur(messageErreur);
    }

    public String getLigne() {
        return ligne;
    }

    public String getMessageErreur() {
        return messageErreur;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public String getNumLigne() {
        return numLigne;
    }

    public void setLigne(String ligne) {
        this.ligne = ligne;
    }

    public void setMessageErreur(String messageErreur) {
        this.messageErreur = messageErreur;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public void setNumLigne(String numLigne) {
        this.numLigne = numLigne;
    }

    public void setParam(String nomFichier, String numLigne, String messageErreur, String ligne) {
        setNomFichier(nomFichier);
        setNumLigne(numLigne);
        setLigne(ligne);
        setMessageErreur(messageErreur);
    }

}
