package ch.globaz.pegasus.business.models.fortuneusuelle;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleBienImmobilierHabitationNonPrincipale extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String autresTypeBien = null;
    private String csTypeBien = null;
    private String csTypePropriete = null;
    private String idBienImmobilierHabitationNonPrincipale = null;
    private String idCommuneDuBien = null;
    private String idDonneeFinanciereHeader = null;
    private String idPays = null;
    private String montantDetteHypothecaire = null;
    private String montantInteretHypothecaire = null;
    private String montantLoyesEncaisses = null;
    private String montantSousLocation = null;
    private String montantValeurLocative = null;
    private String noFeuillet = null;
    private String noHypotheque = null;
    private String nomCompagnie = null;
    private String partProprieteDenominateur = null;
    private String partProprieteNumerateur = null;
    private String valeurVenale = null;
    private Boolean isConstructionMoinsDixAns = Boolean.FALSE;
    private Boolean isConstructionPlusVingtAns = Boolean.FALSE;
    private Boolean isImmeubleCommerciale = Boolean.FALSE;

    public Boolean getIsConstructionMoinsDixAns() {
        return isConstructionMoinsDixAns;
    }

    public void setIsConstructionMoinsDixAns(Boolean isConstructionMoinsDixAns) {
        this.isConstructionMoinsDixAns = isConstructionMoinsDixAns;
    }

    public String getAutresTypeBien() {
        return autresTypeBien;
    }

    public String getCsTypeBien() {
        return csTypeBien;
    }

    public String getCsTypePropriete() {
        return csTypePropriete;
    }

    @Override
    public String getId() {
        return idBienImmobilierHabitationNonPrincipale;
    }

    public String getIdBienImmobilierHabitationNonPrincipale() {
        return idBienImmobilierHabitationNonPrincipale;
    }

    public String getIdCommuneDuBien() {
        return idCommuneDuBien;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getIdPays() {
        return idPays;
    }

    public String getMontantDetteHypothecaire() {
        return montantDetteHypothecaire;
    }

    public String getMontantInteretHypothecaire() {
        return montantInteretHypothecaire;
    }

    public String getMontantLoyesEncaisses() {
        return montantLoyesEncaisses;
    }

    public String getMontantSousLocation() {
        return montantSousLocation;
    }

    public String getMontantValeurLocative() {
        return montantValeurLocative;
    }

    public String getNoFeuillet() {
        return noFeuillet;
    }

    public String getNoHypotheque() {
        return noHypotheque;
    }

    public String getNomCompagnie() {
        return nomCompagnie;
    }

    public String getPartProprieteDenominateur() {
        return partProprieteDenominateur;
    }

    public String getPartProprieteNumerateur() {
        return partProprieteNumerateur;
    }

    public String getValeurVenale() {
        return valeurVenale;
    }

    public void setAutresTypeBien(String autresTypeBien) {
        this.autresTypeBien = autresTypeBien;
    }

    public void setCsTypeBien(String csTypeBien) {
        this.csTypeBien = csTypeBien;
    }

    public void setCsTypePropriete(String csTypePropriete) {
        this.csTypePropriete = csTypePropriete;
    }

    @Override
    public void setId(String id) {
        idBienImmobilierHabitationNonPrincipale = id;
    }

    public void setIdBienImmobilierHabitationNonPrincipale(String idBienImmobilierHabitationNonPrincipale) {
        this.idBienImmobilierHabitationNonPrincipale = idBienImmobilierHabitationNonPrincipale;
    }

    public void setIdCommuneDuBien(String idCommuneDuBien) {
        this.idCommuneDuBien = idCommuneDuBien;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setIdPays(String idPays) {
        this.idPays = idPays;
    }

    public void setMontantDetteHypothecaire(String montantDetteHypothecaire) {
        this.montantDetteHypothecaire = montantDetteHypothecaire;
    }

    public void setMontantInteretHypothecaire(String montantInteretHypothecaire) {
        this.montantInteretHypothecaire = montantInteretHypothecaire;
    }

    public void setMontantLoyesEncaisses(String montantLoyesEncaisses) {
        this.montantLoyesEncaisses = montantLoyesEncaisses;
    }

    public void setMontantSousLocation(String montantSousLocation) {
        this.montantSousLocation = montantSousLocation;
    }

    public void setMontantValeurLocative(String montantValeurLocative) {
        this.montantValeurLocative = montantValeurLocative;
    }

    public void setNoFeuillet(String noFeuillet) {
        this.noFeuillet = noFeuillet;
    }

    public void setNoHypotheque(String noHypotheque) {
        this.noHypotheque = noHypotheque;
    }

    public void setNomCompagnie(String nomCompagnie) {
        this.nomCompagnie = nomCompagnie;
    }

    public void setPartProprieteDenominateur(String partProprieteDenominateur) {
        this.partProprieteDenominateur = partProprieteDenominateur;
    }

    public void setPartProprieteNumerateur(String partProprieteNumerateur) {
        this.partProprieteNumerateur = partProprieteNumerateur;
    }

    public void setValeurVenale(String valeurVenale) {
        this.valeurVenale = valeurVenale;
    }

    public Boolean getIsConstructionPlusVingtAns() {
        return isConstructionPlusVingtAns;
    }

    public void setIsConstructionPlusVingtAns(Boolean isConstructionPlusVingtAns) {
        this.isConstructionPlusVingtAns = isConstructionPlusVingtAns;
    }

    public Boolean getIsImmeubleCommerciale() {
        return isImmeubleCommerciale;
    }

    public void setIsImmeubleCommerciale(Boolean isImmeubleCommerciale) {
        this.isImmeubleCommerciale = isImmeubleCommerciale;
    }
}
