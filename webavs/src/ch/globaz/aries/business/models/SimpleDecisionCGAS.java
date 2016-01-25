package ch.globaz.aries.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDecisionCGAS extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = null;
    private String cotisationAnnuelle = null;
    private String cotisationMensuelle = null;
    private String cotisationPeriode = null;
    private String cotisationTrimestrielle = null;
    private String dateDebut = null;
    private String dateDonnees = null;
    private String dateFin = null;
    private String etat = null;
    private Boolean exempte = null;
    private String idAffiliation = null;
    private String idDecision = null;
    private String idDecisionRectifiee = null;
    private String idPassageFacturation = null;
    private String idResponsable = null;
    private String type = null;

    public SimpleDecisionCGAS() {
        super();
        exempte = false;
    }

    public String getAnnee() {
        return annee;
    }

    public String getCotisationAnnuelle() {
        return cotisationAnnuelle;
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

    public Boolean getExempte() {
        return exempte;
    }

    @Override
    public String getId() {
        return idDecision;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDecisionRectifiee() {
        return idDecisionRectifiee;
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public String getIdResponsable() {
        return idResponsable;
    }

    public String getType() {
        return type;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCotisationAnnuelle(String cotisationAnnuelle) {
        this.cotisationAnnuelle = cotisationAnnuelle;
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

    public void setExempte(Boolean exempte) {
        this.exempte = exempte;
    }

    @Override
    public void setId(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDecisionRectifiee(String idDecisionRectifiee) {
        this.idDecisionRectifiee = idDecisionRectifiee;
    }

    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public void setIdResponsable(String idResponsable) {
        this.idResponsable = idResponsable;
    }

    public void setType(String type) {
        this.type = type;
    }
}