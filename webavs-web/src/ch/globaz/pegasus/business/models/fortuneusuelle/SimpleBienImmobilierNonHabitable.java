package ch.globaz.pegasus.business.models.fortuneusuelle;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleBienImmobilierNonHabitable extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String autresTypeBien = null;
    private String csTypeBien = null;
    private String csTypePropriete = null;
    private String idBienImmobilierNonHabitable = null;
    private String idCommuneDuBien = null;
    private String idDonneeFinanciereHeader = null;
    private String idPays = null;

    private String montantDetteHypothecaire = null;

    private String montantInteretHypothecaire = null;
    private String montantRendement = null;
    private String noFeuillet = null;
    private String noHypotheque = null;
    private String nomCompagnie = null;
    private String partProprieteDenominateur = null;
    private String partProprieteNumerateur = null;
    private String valeurVenale = null;

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
        return idBienImmobilierNonHabitable;
    }

    public String getIdBienImmobilierNonHabitable() {
        return idBienImmobilierNonHabitable;
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

    public String getMontantRendement() {
        return montantRendement;
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
        idBienImmobilierNonHabitable = id;
    }

    public void setIdBienImmobilierNonHabitable(String idBienImmobilierNonHabitable) {
        this.idBienImmobilierNonHabitable = idBienImmobilierNonHabitable;
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

    public void setMontantRendement(String montantRendement) {
        this.montantRendement = montantRendement;
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

}
