package ch.globaz.pegasus.business.domaine.parametre.home;

public class Home {
    private String id;
    private String numeroIdentification;
    private String nomBatiment;
    private Boolean isHorsCanton = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumeroIdentification() {
        return numeroIdentification;
    }

    public void setNumeroIdentification(String numeroIdentification) {
        this.numeroIdentification = numeroIdentification;
    }

    public String getNomBatiment() {
        return nomBatiment;
    }

    public void setNomBatiment(String nomBatiment) {
        this.nomBatiment = nomBatiment;
    }

    public Boolean getIsHorsCanton() {
        return isHorsCanton;
    }

    public void setIsHorsCanton(Boolean isHorsCanton) {
        this.isHorsCanton = isHorsCanton;
    }

}
