package ch.globaz.pegasus.business.models.mutation;

import globaz.jade.persistence.model.JadeComplexModel;

public class MutationMontantPCAFileds extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String ancienMontant;
    private boolean avsToAi = false; // attentions se champ n'est pas remplie par la perscistence
    private String csMotif;
    private String csTypePreparationDecision;
    private String dateDebutPcaActuel;
    private String dateDebutPcaPrecedant;
    private String dateFinPcaActuel;
    private String dateFinPcaPrecedant;
    private String idPcaActuel;
    private String idTiersActuel;
    private String idTiersPrecedant;
    private String idVersionDroit;
    private String montantActuel;
    private String nom;
    private String noVersion;
    private String nss;
    private String prenom;
    private String typeDecision;
    private String typePcActuel;
    private String typePcPrecedant;

    public String getAncienMontant() {
        return ancienMontant;
    }

    public String getCsMotif() {
        return csMotif;
    }

    public String getCsTypePreparationDecision() {
        return csTypePreparationDecision;
    }

    public String getDateDebutPcaActuel() {
        return dateDebutPcaActuel;
    }

    public String getDateDebutPcaPrecedant() {
        return dateDebutPcaPrecedant;
    }

    public String getDateFinPcaActuel() {
        return dateFinPcaActuel;
    }

    public String getDateFinPcaPrecedant() {
        return dateFinPcaPrecedant;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdPcaActuel() {
        return idPcaActuel;
    }

    public String getIdTiersActuel() {
        return idTiersActuel;
    }

    public String getIdTiersPrecedant() {
        return idTiersPrecedant;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public String getMontantActuel() {
        return montantActuel;
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
        // TODO Auto-generated method stub
        return null;
    }

    public String getTypeDecision() {
        return typeDecision;
    }

    public String getTypePcActuel() {
        return typePcActuel;
    }

    public String getTypePcPrecedant() {
        return typePcPrecedant;
    }

    public boolean isAvsToAi() {
        return avsToAi;
    }

    public void setAncienMontant(String ancienMontant) {
        this.ancienMontant = ancienMontant;
    }

    public void setAvsToAi(boolean avsToAi) {
        this.avsToAi = avsToAi;
    }

    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    public void setCsTypePreparationDecision(String csTypePreparationDecision) {
        this.csTypePreparationDecision = csTypePreparationDecision;
    }

    public void setDateDebutPcaActuel(String dateDebutPcaActuel) {
        this.dateDebutPcaActuel = dateDebutPcaActuel;
    }

    public void setDateDebutPcaPrecedant(String dateDebutPcaPrecedant) {
        this.dateDebutPcaPrecedant = dateDebutPcaPrecedant;
    }

    public void setDateFinPcaActuel(String dateFinPcaActuel) {
        this.dateFinPcaActuel = dateFinPcaActuel;
    }

    public void setDateFinPcaPrecedant(String dateFinPcaPrecedant) {
        this.dateFinPcaPrecedant = dateFinPcaPrecedant;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setIdPcaActuel(String idPcaActuel) {
        this.idPcaActuel = idPcaActuel;
    }

    public void setIdTiersActuel(String idTiersActuel) {
        this.idTiersActuel = idTiersActuel;
    }

    public void setIdTiersPrecedant(String idTiersPrecedant) {
        this.idTiersPrecedant = idTiersPrecedant;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setMontantActuel(String montantActuel) {
        this.montantActuel = montantActuel;
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
        // TODO Auto-generated method stub
    }

    public void setTypeDecision(String typeDecision) {
        this.typeDecision = typeDecision;
    }

    public void setTypePcActuel(String typePcActuel) {
        this.typePcActuel = typePcActuel;
    }

    public void setTypePcPrecedant(String typePcPrecedant) {
        this.typePcPrecedant = typePcPrecedant;
    }

}
