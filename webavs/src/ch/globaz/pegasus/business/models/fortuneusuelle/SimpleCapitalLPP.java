package ch.globaz.pegasus.business.models.fortuneusuelle;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleCapitalLPP extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypePropriete = null;
    private String dateLiberation = null;
    private String destinationLiberation = null;
    private String idCapitalLPP = null;
    private String idDonneeFinanciereHeader = null;
    private String idInstitutionPrevoyance = null;
    private Boolean isSansInteret = Boolean.FALSE;
    private String montantCapitalLPP = null;
    private String montantFrais = null;
    private String montantInteret = null;
    private String noPoliceNoCompte = null;
    private String partProprieteDenominateur = null;
    private String partProprieteNumerateur = null;

    public String getCsTypePropriete() {
        return csTypePropriete;
    }

    public String getDateLiberation() {
        return dateLiberation;
    }

    public String getDestinationLiberation() {
        return destinationLiberation;
    }

    @Override
    public String getId() {
        return idCapitalLPP;
    }

    public String getIdCapitalLPP() {
        return idCapitalLPP;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getIdInstitutionPrevoyance() {
        return idInstitutionPrevoyance;
    }

    public Boolean getIsSansInteret() {
        return isSansInteret;
    }

    public String getMontantCapitalLPP() {
        return montantCapitalLPP;
    }

    public String getMontantFrais() {
        return montantFrais;
    }

    public String getMontantInteret() {
        return montantInteret;
    }

    public String getNoPoliceNoCompte() {
        return noPoliceNoCompte;
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

    public void setDateLiberation(String dateLiberation) {
        this.dateLiberation = dateLiberation;
    }

    public void setDestinationLiberation(String destinationLiberation) {
        this.destinationLiberation = destinationLiberation;
    }

    @Override
    public void setId(String id) {
        idCapitalLPP = id;
    }

    public void setIdCapitalLPP(String idCapitalLPP) {
        this.idCapitalLPP = idCapitalLPP;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setIdInstitutionPrevoyance(String idInstitutionPrevoyance) {
        this.idInstitutionPrevoyance = idInstitutionPrevoyance;
    }

    public void setIsSansInteret(Boolean isSansInteret) {
        this.isSansInteret = isSansInteret;
    }

    public void setMontantCapitalLPP(String montantCapitalLPP) {
        this.montantCapitalLPP = montantCapitalLPP;
    }

    public void setMontantFrais(String montantFrais) {
        this.montantFrais = montantFrais;
    }

    public void setMontantInteret(String montantInteret) {
        this.montantInteret = montantInteret;
    }

    public void setNoPoliceNoCompte(String noPoliceNoCompte) {
        this.noPoliceNoCompte = noPoliceNoCompte;
    }

    public void setPartProprieteDenominateur(String partProprieteDenominateur) {
        this.partProprieteDenominateur = partProprieteDenominateur;
    }

    public void setPartProprieteNumerateur(String partProprieteNumerateur) {
        this.partProprieteNumerateur = partProprieteNumerateur;
    }

}
