package globaz.naos.db.decisionCotisations;

public class AFDecisionCotisationsLog {

    private String motifRefus;
    private String nomAffilie;
    private String numeroAffilie;
    private String plan;
    private String prenomAffilie;

    public AFDecisionCotisationsLog(String numeroAffilie, String nomAffilie, String prenomAffilie, String plan,
            String motifRefus) {
        this.numeroAffilie = numeroAffilie;
        this.nomAffilie = nomAffilie;
        this.prenomAffilie = prenomAffilie;
        this.motifRefus = motifRefus;
        setPlan(plan);
    }

    public String getMotifRefus() {
        return motifRefus;
    }

    public String getNomAffilie() {
        return nomAffilie;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public String getPlan() {
        return plan;
    }

    public String getPrenomAffilie() {
        return prenomAffilie;
    }

    public void setMotifRefus(String motifRefus) {
        this.motifRefus = motifRefus;
    }

    public void setNomAffilie(String nomAffilie) {
        this.nomAffilie = nomAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public void setParam(String numeroAffilie, String nomAffilie, String prenomAffilie, String motifRefus) {
        setNumeroAffilie(numeroAffilie);
        setNomAffilie(nomAffilie);
        setPrenomAffilie(prenomAffilie);
        setMotifRefus(motifRefus);
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setPrenomAffilie(String prenomAffilie) {
        this.prenomAffilie = prenomAffilie;
    }

}
