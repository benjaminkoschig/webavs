package ch.globaz.pegasus.business.models.revisionquadriennale;

import globaz.jade.persistence.model.JadeComplexModel;

public class ListRevisions extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String agenceDesignation1 = null;
    private String agenceDesignation2 = null;
    private String agenceRelationDebut = null;
    private String agenceRelationFin = null;
    private String dateNaissance = null;
    private String dateProchaineRevision = null;
    private String designation1 = null;
    private String designation2 = null;
    private String idDemande = null;
    private String idGestionnaire = null;
    private String idPays = null;
    private String idTiers = null;
    private String motifProchaineRevision = null;
    private String numAvsActuel = null;
    private String sexe = null;
    private String idDroit = null;

    public String getIdDroit() {
        return idDroit;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public ListRevisions() {
        super();
    }

    public String getAgenceDesignation1() {
        return agenceDesignation1;
    }

    public String getAgenceDesignation2() {
        return agenceDesignation2;
    }

    public String getAgenceRelationDebut() {
        return agenceRelationDebut;
    }

    public String getAgenceRelationFin() {
        return agenceRelationFin;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateProchaineRevision() {
        return dateProchaineRevision;
    }

    public String getDesignation1() {
        return designation1;
    }

    public String getDesignation2() {
        return designation2;
    }

    @Override
    public String getId() {
        return idDemande;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public String getIdPays() {
        return idPays;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMotifProchaineRevision() {
        return motifProchaineRevision;
    }

    public String getNumAvsActuel() {
        return numAvsActuel;
    }

    public String getSexe() {
        return sexe;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setAgenceDesignation1(String agenceDesignation1) {
        this.agenceDesignation1 = agenceDesignation1;
    }

    public void setAgenceDesignation2(String agenceDesignation2) {
        this.agenceDesignation2 = agenceDesignation2;
    }

    public void setAgenceRelationDebut(String agenceRelationDebut) {
        this.agenceRelationDebut = agenceRelationDebut;
    }

    public void setAgenceRelationFin(String agenceRelationFin) {
        this.agenceRelationFin = agenceRelationFin;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateProchaineRevision(String dateProchaineRevision) {
        this.dateProchaineRevision = dateProchaineRevision;
    }

    public void setDesignation1(String designation1) {
        this.designation1 = designation1;
    }

    public void setDesignation2(String designation2) {
        this.designation2 = designation2;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setIdPays(String idPays) {
        this.idPays = idPays;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMotifProchaineRevision(String motifProchaineRevision) {
        this.motifProchaineRevision = motifProchaineRevision;
    }

    public void setNumAvsActuel(String numAvsActuel) {
        this.numAvsActuel = numAvsActuel;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

}
