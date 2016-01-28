package ch.globaz.auriga.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDecisionCAP extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = null;
    private String categorie = null;
    private String cotisationAnnuelle = null;
    private String cotisationBrute = null;
    private String cotisationMensuelle = null;
    private String cotisationPeriode = null;
    private String cotisationTrimestrielle = null;
    private String dateDebut = null;
    private String dateDonnees = null;
    private String dateFin = null;
    private String etat = null;
    private String forfait = null;
    private String idAffiliation = null;
    private String idAssurance = null;
    private String idDecision = null;
    private String idDecisionRectifiee = null;
    private String idPassageFacturation = null;
    private String idResponsable = null;
    private String prestation = null;
    private String revenuFRV = null;
    private String revenuIFD = null;
    private String tauxAssurance = null;
    private String type = null;

    public String getAnnee() {
        return annee;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getCotisationAnnuelle() {
        return cotisationAnnuelle;
    }

    public String getCotisationBrute() {
        return cotisationBrute;
    }

    public String getCotisationMensuelle() {
        return cotisationMensuelle;
    }

    public String getCotisationPeriode() {
        return cotisationPeriode;
    }

    public String getCotisationTrimestrielle() {
        return cotisationTrimestrielle;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDonnees() {
        return dateDonnees;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getEtat() {
        return etat;
    }

    public String getForfait() {
        return forfait;
    }

    @Override
    public String getId() {
        return idDecision;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdAssurance() {
        return idAssurance;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public String getIdResponsable() {
        return idResponsable;
    }

    public String getPrestation() {
        return prestation;
    }

    public String getRevenuFRV() {
        return revenuFRV;
    }

    public String getRevenuIFD() {
        return revenuIFD;
    }

    public String getTauxAssurance() {
        return tauxAssurance;
    }

    public String getType() {
        return type;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setCotisationAnnuelle(String cotisationAnnuelle) {
        this.cotisationAnnuelle = cotisationAnnuelle;
    }

    public void setCotisationBrute(String cotisationBrute) {
        this.cotisationBrute = cotisationBrute;
    }

    public void setCotisationMensuelle(String cotisationMensuelle) {
        this.cotisationMensuelle = cotisationMensuelle;
    }

    public void setCotisationPeriode(String cotisationPeriode) {
        this.cotisationPeriode = cotisationPeriode;
    }

    public void setCotisationTrimestrielle(String cotisationTrimestrielle) {
        this.cotisationTrimestrielle = cotisationTrimestrielle;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateDonnees(String dateDonnees) {
        this.dateDonnees = dateDonnees;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setForfait(String forfait) {
        this.forfait = forfait;
    }

    @Override
    public void setId(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdAssurance(String idAssurance) {
        this.idAssurance = idAssurance;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public void setIdResponsable(String idResponsable) {
        this.idResponsable = idResponsable;
    }

    public void setPrestation(String prestation) {
        this.prestation = prestation;
    }

    public void setRevenuFRV(String revenuFRV) {
        this.revenuFRV = revenuFRV;
    }

    public void setRevenuIFD(String revenuIFD) {
        this.revenuIFD = revenuIFD;
    }

    public void setTauxAssurance(String tauxAssurance) {
        this.tauxAssurance = tauxAssurance;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdDecisionRectifiee() {
        return idDecisionRectifiee;
    }

    public void setIdDecisionRectifiee(String idDecisionRectifiee) {
        this.idDecisionRectifiee = idDecisionRectifiee;
    }
}