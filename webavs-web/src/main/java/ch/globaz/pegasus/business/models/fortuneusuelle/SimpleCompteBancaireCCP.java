package ch.globaz.pegasus.business.models.fortuneusuelle;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleCompteBancaireCCP extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /* private String csTypeCompte = null; */
    private String csTypePropriete = null;
    private String iban = null;
    private String idCompteBancaireCCP = null;
    private String idDonneeFinanciereHeader = null;
    private Boolean isSansInteret = Boolean.FALSE;
    private String montant = null;
    private String montantFraisBancaire = null;
    private String montantInteret = null;
    private String partProprieteDenominateur = null;
    private String partProprieteNumerateur = null;

    public String getCsTypePropriete() {
        return csTypePropriete;
    }

    public String getIban() {
        return iban;
    }

    @Override
    public String getId() {
        return idCompteBancaireCCP;
    }

    public String getIdCompteBancaireCCP() {
        return idCompteBancaireCCP;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public Boolean getIsSansInteret() {
        return isSansInteret;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantFraisBancaire() {
        return montantFraisBancaire;
    }

    public String getMontantInteret() {
        return montantInteret;
    }

    public String getPartProprieteDenominateur() {
        return partProprieteDenominateur;
    }

    public String getPartProprieteNumerateur() {
        return partProprieteNumerateur;
    }

    public void setCsTypePropriete(String csTypePropriete) {
        this.csTypePropriete = csTypePropriete;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @Override
    public void setId(String id) {
        idCompteBancaireCCP = id;
    }

    public void setIdCompteBancaireCCP(String idCompteBancaireCCP) {
        this.idCompteBancaireCCP = idCompteBancaireCCP;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setIsSansInteret(Boolean isSansInteret) {
        this.isSansInteret = isSansInteret;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantFraisBancaire(String montantFraisBancaire) {
        this.montantFraisBancaire = montantFraisBancaire;
    }

    public void setMontantInteret(String montantInteret) {
        this.montantInteret = montantInteret;
    }

    public void setPartProprieteDenominateur(String partProprieteDenominateur) {
        this.partProprieteDenominateur = partProprieteDenominateur;
    }

    public void setPartProprieteNumerateur(String partProprieteNumerateur) {
        this.partProprieteNumerateur = partProprieteNumerateur;
    }

}
