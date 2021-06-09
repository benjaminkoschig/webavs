package globaz.corvus.acor2020.utils;

public class Acor2020Token {

    private String idDemande;
    private String noAVSDemande;
    private String idTiers;
    private String dateDemande;
    private String timeDemande;
    private String timeStampGedo;
    private String langue;
    private String email;
    private String userId;

    public boolean demGedoExist(){
        boolean exist = dateDemande != null && !dateDemande.equals("0");
        exist = exist && timeDemande != null && !timeDemande.equals("0");
        return exist;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public String getNoAVSDemande() {
        return noAVSDemande;
    }

    public void setNoAVSDemande(String noAVSDemande) {
        this.noAVSDemande = noAVSDemande;
    }

    public String getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(String dateDemande) {
        this.dateDemande = dateDemande;
    }

    public String getTimeDemande() {
        return timeDemande;
    }

    public void setTimeDemande(String timeDemande) {
        this.timeDemande = timeDemande;
    }

    public String getTimeStampGedo() {
        return timeStampGedo;
    }

    public void setTimeStampGedo(String timeStampGedo) {
        this.timeStampGedo = timeStampGedo;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }
}
