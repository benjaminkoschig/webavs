package ch.globaz.pegasus.business.models.revisionquadriennale;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;

public class ListrevisionWithPcaRequerantConjoint extends JadeComplexModel {

    private String idDemande;
    private String dateProchaineRevision;
    private String motifProchaineRevision;
    private String idGestionnaire;
    private String idTiersRequerant;
    private String nssRequerant;
    private String nomRequerant;
    private String prenomRequerant;
    private String dateNaissanceRequerant;
    private String sexeRequerant;
    private String idPaysRequerant;
    private String agenceDesignation1;
    private String agenceDesignation2;
    private String agenceRelationDebut;
    private String agenceRelationFin;
    private String idDroit;
    private String idTiersConjoint;
    private String nssConjoint;
    private String nomConjoint;
    private String prenomConjoint;
    private String dateNaissanceConjoint;
    private String idPaysConjoint;
    private String idPresationCojoint;
    private SimplePCAccordee pcaRequerant = new SimplePCAccordee();
    private SimplePCAccordee pcaConjoint = new SimplePCAccordee();

    public ListrevisionWithPcaRequerantConjoint() {
        pcaRequerant = new SimplePCAccordee();
        pcaConjoint = new SimplePCAccordee();
    }

    public SimplePCAccordee getPcaRequerant() {
        return pcaRequerant;
    }

    public void setPcaRequerant(SimplePCAccordee pcaRequerant) {
        this.pcaRequerant = pcaRequerant;
    }

    public SimplePCAccordee getPcaConjoint() {
        return pcaConjoint;
    }

    public void setPcaConjoint(SimplePCAccordee pcaConjoint) {
        this.pcaConjoint = pcaConjoint;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public String getDateProchaineRevision() {
        return dateProchaineRevision;
    }

    public void setDateProchaineRevision(String dateProchaineRevision) {
        this.dateProchaineRevision = dateProchaineRevision;
    }

    public String getMotifProchaineRevision() {
        return motifProchaineRevision;
    }

    public void setMotifProchaineRevision(String motifProchaineRevision) {
        this.motifProchaineRevision = motifProchaineRevision;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

    public String getNssRequerant() {
        return nssRequerant;
    }

    public void setNssRequerant(String nssRequerant) {
        this.nssRequerant = nssRequerant;
    }

    public String getNomRequerant() {
        return nomRequerant;
    }

    public void setNomRequerant(String nomRequerant) {
        this.nomRequerant = nomRequerant;
    }

    public String getPrenomRequerant() {
        return prenomRequerant;
    }

    public void setPrenomRequerant(String prenomRequerant) {
        this.prenomRequerant = prenomRequerant;
    }

    public String getDateNaissanceRequerant() {
        return dateNaissanceRequerant;
    }

    public void setDateNaissanceRequerant(String dateNaissanceRequerant) {
        this.dateNaissanceRequerant = dateNaissanceRequerant;
    }

    public String getSexeRequerant() {
        return sexeRequerant;
    }

    public void setSexeRequerant(String sexeRequerant) {
        this.sexeRequerant = sexeRequerant;
    }

    public String getIdPaysRequerant() {
        return idPaysRequerant;
    }

    public void setIdPaysRequerant(String idPaysRequerant) {
        this.idPaysRequerant = idPaysRequerant;
    }

    public String getAgenceDesignation1() {
        return agenceDesignation1;
    }

    public void setAgenceDesignation1(String agenceDesignation1) {
        this.agenceDesignation1 = agenceDesignation1;
    }

    public String getAgenceDesignation2() {
        return agenceDesignation2;
    }

    public void setAgenceDesignation2(String agenceDesignation2) {
        this.agenceDesignation2 = agenceDesignation2;
    }

    public String getAgenceRelationDebut() {
        return agenceRelationDebut;
    }

    public void setAgenceRelationDebut(String agenceRelationDebut) {
        this.agenceRelationDebut = agenceRelationDebut;
    }

    public String getAgenceRelationFin() {
        return agenceRelationFin;
    }

    public void setAgenceRelationFin(String agenceRelationFin) {
        this.agenceRelationFin = agenceRelationFin;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public String getIdTiersConjoint() {
        return idTiersConjoint;
    }

    public void setIdTiersConjoint(String idTiersConjoint) {
        this.idTiersConjoint = idTiersConjoint;
    }

    public String getNssConjoint() {
        return nssConjoint;
    }

    public void setNssConjoint(String nssConjoint) {
        this.nssConjoint = nssConjoint;
    }

    public String getNomConjoint() {
        return nomConjoint;
    }

    public void setNomConjoint(String nomConjoint) {
        this.nomConjoint = nomConjoint;
    }

    public String getPrenomConjoint() {
        return prenomConjoint;
    }

    public void setPrenomConjoint(String prenomConjoint) {
        this.prenomConjoint = prenomConjoint;
    }

    public String getDateNaissanceConjoint() {
        return dateNaissanceConjoint;
    }

    public void setDateNaissanceConjoint(String dateNaissanceConjoint) {
        this.dateNaissanceConjoint = dateNaissanceConjoint;
    }

    public String getIdPaysConjoint() {
        return idPaysConjoint;
    }

    public void setIdPaysConjoint(String idPaysConjoint) {
        this.idPaysConjoint = idPaysConjoint;
    }

    public String getIdPresationCojoint() {
        return idPresationCojoint;
    }

    public void setIdPresationCojoint(String idPresationCojoint) {
        this.idPresationCojoint = idPresationCojoint;
    }

    @Override
    public String getId() {
        return idDemande;
    }

    @Override
    public String getSpy() {
        return null;
    }

    @Override
    public void setId(String id) {
        idDemande = id;
    }

    @Override
    public void setSpy(String spy) {
    }

}
