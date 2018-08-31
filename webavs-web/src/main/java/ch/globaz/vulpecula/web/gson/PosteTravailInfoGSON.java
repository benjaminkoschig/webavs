package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;

public class PosteTravailInfoGSON implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7432652942718791110L;
    String idPosteTravailExistant;
    String correlationId;
    String posteCorrelationId;
    String idPortail;
    String idTravailleur;
    String idAffiliation;
    String qualification;
    String dateDebut;
    String dateFin;
    String typeSalaire;
    String tauxActivite;
    String dateTauxActivite;
    String salaireBase;
    String assurance;

    public String getIdPosteTravailExistant() {
        return idPosteTravailExistant;
    }

    public void setIdPosteTravailExistant(String idPosteTravailExistant) {
        this.idPosteTravailExistant = idPosteTravailExistant;
    }

    public String getIdTravailleur() {
        return idTravailleur;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getQualification() {
        return qualification;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getTypeSalaire() {
        return typeSalaire;
    }

    public String getTauxActivite() {
        return tauxActivite;
    }

    public String getDateTauxActivite() {
        return dateTauxActivite;
    }

    public String getIdPortail() {
        return idPortail;
    }

    public void setIdPortail(String idPortail) {
        this.idPortail = idPortail;
    }

    public String getSalaireBase() {
        return salaireBase;
    }

    public void setSalaireBase(String salaireBase) {
        this.salaireBase = salaireBase;
    }

    public String getAssurance() {
        return assurance;
    }

    public void setAssurance(String assurance) {
        this.assurance = assurance;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getPosteCorrelationId() {
        return posteCorrelationId;
    }

    public void setPosteCorrelationId(String posteCorrelationId) {
        this.posteCorrelationId = posteCorrelationId;
    }

}
