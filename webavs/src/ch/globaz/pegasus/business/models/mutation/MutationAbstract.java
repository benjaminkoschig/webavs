package ch.globaz.pegasus.business.models.mutation;

import java.math.BigDecimal;

public abstract class MutationAbstract extends globaz.jade.persistence.model.JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csRoleMembreFamille = null;
    private String csTypeDecision = null;
    private String csTypePca = null;
    private String dateDebutPca = null;
    private String idDroit = null;
    private String idPcaActuel = null;
    private String idVersionDroit = null;
    private String montant = null;
    private BigDecimal montantJourAppoint = null; // Attention n'est pas peupler par la persistence;
    private String nom = null;
    private String noVersion = null;
    private String nss = null;
    private String prenom = null;

    public String getCsRoleMembreFamille() {
        return csRoleMembreFamille;
    }

    public String getCsTypeDecision() {
        return csTypeDecision;
    }

    public String getCsTypePca() {
        return csTypePca;
    }

    public String getDateDebutPca() {
        return dateDebutPca;
    }

    @Override
    public String getId() {
        return null;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdPcaActuel() {
        return idPcaActuel;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public String getMontant() {
        return montant;
    }

    public BigDecimal getMontantJourAppoint() {
        return montantJourAppoint;
    }

    public String getNom() {
        return nom;
    }

    public String getNoVersion() {
        return noVersion;
    }

    public String getNss() {
        return nss;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public String getSpy() {
        return null;
    }

    public void setCsRoleMembreFamille(String csRoleMembreFamille) {
        this.csRoleMembreFamille = csRoleMembreFamille;
    }

    public void setCsTypeDecision(String csTypeDecision) {
        this.csTypeDecision = csTypeDecision;
    }

    public void setCsTypePca(String csTypePca) {
        this.csTypePca = csTypePca;
    }

    public void setDateDebutPca(String dateDebutPca) {
        this.dateDebutPca = dateDebutPca;
    }

    @Override
    public void setId(String id) {
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdPcaActuel(String idPcaActuel) {
        this.idPcaActuel = idPcaActuel;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantJourAppoint(BigDecimal montantJourAppoint) {
        this.montantJourAppoint = montantJourAppoint;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public void setSpy(String spy) {
    }

    @Override
    public String toString() {
        return "NSS:" + nss + "No Version: " + noVersion + ", montant: " + montant + " periode: " + dateDebutPca + "-";
    }

}
