package globaz.hercule.process.statOfas;

public class CEStatOfasEmployeurActifBean {
    private Double cumulMasse;
    private Integer dateDebutAffiliation;
    private Integer dateDebutCotisation;
    private Integer dateFinAffiliation;
    private Integer dateFinCotisation;
    private String numeroAffilie;

    public Double getCumulMasse() {
        return cumulMasse;
    }

    public Integer getDateDebutAffiliation() {
        return dateDebutAffiliation;
    }

    public Integer getDateDebutCotisation() {
        return dateDebutCotisation;
    }

    public Integer getDateFinAffiliation() {
        return dateFinAffiliation;
    }

    public Integer getDateFinCotisation() {
        return dateFinCotisation;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setCumulMasse(Double cumulMasse) {
        this.cumulMasse = cumulMasse;
    }

    public void setDateDebutAffiliation(Integer dateDebutAffiliation) {
        this.dateDebutAffiliation = dateDebutAffiliation;
    }

    public void setDateDebutCotisation(Integer dateDebutCotisation) {
        this.dateDebutCotisation = dateDebutCotisation;
    }

    public void setDateFinAffiliation(Integer dateFinAffiliation) {
        this.dateFinAffiliation = dateFinAffiliation;
    }

    public void setDateFinCotisation(Integer dateFinCotisation) {
        this.dateFinCotisation = dateFinCotisation;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }
}
