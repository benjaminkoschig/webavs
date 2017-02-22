package ch.globaz.naos.ree.domain.pojo;

public class PojoSuivi {

    private String idAffilie;
    private String dateFinSuivi;
    private Boolean isPrevious;
    private String dateDebutSuivi;
    private Boolean isNext;
    private String codeCaisse;

    public Boolean getIsPrevious() {
        return isPrevious;
    }

    public void setIsPrevious(Boolean isPrevious) {
        this.isPrevious = isPrevious;
    }

    public String getDateDebutSuivi() {
        return dateDebutSuivi;
    }

    public void setDateDebutSuivi(String dateDebutSuivi) {
        this.dateDebutSuivi = dateDebutSuivi;
    }

    public Boolean getIsNext() {
        return isNext;
    }

    public void setIsNext(Boolean isNext) {
        this.isNext = isNext;
    }

    public void setIdAffilie(String idAffilie) {
        this.idAffilie = idAffilie;
    }

    public void setDateFinSuivi(String dateFinSuivi) {
        this.dateFinSuivi = dateFinSuivi;
    }

    public String getDateFinSuivi() {
        return dateFinSuivi;
    }

    public String getIdAffilie() {
        return idAffilie;
    }

    public String getCodeCaisse() {
        return codeCaisse;
    }

    public void setCodeCaisse(String codeCaisse) {
        this.codeCaisse = codeCaisse;
    }

}
