package ch.globaz.vulpecula.business.models.postetravail;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author JPA
 * 
 */
public class PosteTravailSimpleModel extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 4721513979639342970L;

    private String id;
    private String posteCorrelationId;
    private String idEmployeur;
    private String idTravailleur;
    private String debutActivite;
    private String finActivite;
    private String genreSalaire;
    private Boolean posteFranchiseAVS;
    private String qualification;
    private String dateValiditeQualification;
    private String dateValiditeTypeSalaire;

    public PosteTravailSimpleModel() {
        super();
    }

    public String getDebutActivite() {
        return debutActivite;
    }

    public String getFinActivite() {
        return finActivite;
    }

    public String getGenreSalaire() {
        return genreSalaire;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public Boolean getPosteFranchiseAVS() {
        return posteFranchiseAVS;
    }

    public String getQualification() {
        return qualification;
    }

    public void setDebutActivite(String debutActivite) {
        this.debutActivite = debutActivite;
    }

    public void setFinActivite(String finActivite) {
        this.finActivite = finActivite;
    }

    public void setGenreSalaire(String genreSalaire) {
        this.genreSalaire = genreSalaire;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public void setIdTravailleur(String idTravailleur) {
        this.idTravailleur = idTravailleur;
    }

    public void setPosteFranchiseAVS(boolean posteFranchiseAVS) {
        this.posteFranchiseAVS = posteFranchiseAVS;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getPosteCorrelationId() {
        return posteCorrelationId;
    }

    public void setPosteCorrelationId(String posteCorrelationId) {
        this.posteCorrelationId = posteCorrelationId;
    }

    public String getDateValiditeQualification() {
        return dateValiditeQualification;
    }

    public String getDateValiditeTypeSalaire() {
        return dateValiditeTypeSalaire;
    }

    public void setDateValiditeQualification(String dateValiditeQualification) {
        this.dateValiditeQualification = dateValiditeQualification;
    }

    public void setDateValiditeTypeSalaire(String dateValiditeTypeSalaire) {
        this.dateValiditeTypeSalaire = dateValiditeTypeSalaire;
    }

}
