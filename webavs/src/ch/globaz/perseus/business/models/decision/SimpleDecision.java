package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author MBO
 * 
 */

public class SimpleDecision extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean aideAuLogement = false;
    private Boolean aideAuxEtudes = false;
    private String csChoix = "";
    private String csEtat = "";
    private String csTypeDecision = "";
    private String dateChoix = "";
    private String dateDocument = "";
    private String datePreparation = "";
    private String dateSuppression = "";
    private String dateValidation = "";
    private String idDecision = "";
    private String idDemande = "";
    private String idDomaineApplicatifAdresseCourrier = "";
    private String idDomaineApplicatifAdressePaiement = "";
    private String idTiersAdresseCourrier = "";
    private String idTiersAdressePaiement = "";
    private String montantToucheAuRI = "";
    private String numeroDecision = "";
    private Boolean pensionAlimentaire = false;
    private String remarquesGenerales = "";
    private String remarqueUtilisateur = "";

    private String utilisateurPreparation = "";

    private String utilisateurValidation = "";

    public Boolean getAideAuLogement() {
        return aideAuLogement;
    }

    public Boolean getAideAuxEtudes() {
        return aideAuxEtudes;
    }

    public String getCsChoix() {
        return csChoix;
    }

    public String getCsEtat() {
        return csEtat;
    }

    public String getCsTypeDecision() {
        return csTypeDecision;
    }

    public String getDateChoix() {
        return dateChoix;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public String getDatePreparation() {
        return datePreparation;
    }

    public String getDateSuppression() {
        return dateSuppression;
    }

    public String getDateValidation() {
        return dateValidation;
    }

    @Override
    public String getId() {
        return idDecision;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdDomaineApplicatifAdresseCourrier() {
        return idDomaineApplicatifAdresseCourrier;
    }

    public String getIdDomaineApplicatifAdressePaiement() {
        return idDomaineApplicatifAdressePaiement;
    }

    public String getIdTiersAdresseCourrier() {
        return idTiersAdresseCourrier;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getMontantToucheAuRI() {
        return montantToucheAuRI;
    }

    public String getNumeroDecision() {
        return numeroDecision;
    }

    public Boolean getPensionAlimentaire() {
        return pensionAlimentaire;
    }

    public String getRemarquesGenerales() {
        return remarquesGenerales;
    }

    public String getRemarqueUtilisateur() {
        return remarqueUtilisateur;
    }

    public String getUtilisateurPreparation() {
        return utilisateurPreparation;
    }

    public String getUtilisateurValidation() {
        return utilisateurValidation;
    }

    public void setAideAuLogement(Boolean aideAuLogement) {
        this.aideAuLogement = aideAuLogement;
    }

    public void setAideAuxEtudes(Boolean aideAuxEtudes) {
        this.aideAuxEtudes = aideAuxEtudes;
    }

    public void setCsChoix(String csChoix) {
        this.csChoix = csChoix;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public void setCsTypeDecision(String csTypeDecision) {
        this.csTypeDecision = csTypeDecision;
    }

    public void setDateChoix(String dateChoix) {
        this.dateChoix = dateChoix;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDatePreparation(String datePreparation) {
        this.datePreparation = datePreparation;
    }

    public void setDateSuppression(String dateSuppression) {
        this.dateSuppression = dateSuppression;
    }

    public void setDateValidation(String dateValidation) {
        this.dateValidation = dateValidation;
    }

    @Override
    public void setId(String id) {
        idDecision = id;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdDomaineApplicatifAdresseCourrier(String idDomaineApplicatifAdresseCourrier) {
        this.idDomaineApplicatifAdresseCourrier = idDomaineApplicatifAdresseCourrier;
    }

    public void setIdDomaineApplicatifAdressePaiement(String idDomaineApplicatifAdressePaiement) {
        this.idDomaineApplicatifAdressePaiement = idDomaineApplicatifAdressePaiement;
    }

    public void setIdTiersAdresseCourrier(String idTiersAdresseCourrier) {
        this.idTiersAdresseCourrier = idTiersAdresseCourrier;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setMontantToucheAuRI(String montantToucheAuRI) {
        this.montantToucheAuRI = montantToucheAuRI;
    }

    public void setNumeroDecision(String numeroDecision) {
        this.numeroDecision = numeroDecision;
    }

    public void setPensionAlimentaire(Boolean pensionAlimentaire) {
        this.pensionAlimentaire = pensionAlimentaire;
    }

    public void setRemarquesGenerales(String remarquesGenerales) {
        this.remarquesGenerales = remarquesGenerales;
    }

    public void setRemarqueUtilisateur(String remarqueUtilisateur) {
        this.remarqueUtilisateur = remarqueUtilisateur;
    }

    public void setUtilisateurPreparation(String utilisateurPreparation) {
        this.utilisateurPreparation = utilisateurPreparation;
    }

    public void setUtilisateurValidation(String utilisateurValidation) {
        this.utilisateurValidation = utilisateurValidation;
    }

}
