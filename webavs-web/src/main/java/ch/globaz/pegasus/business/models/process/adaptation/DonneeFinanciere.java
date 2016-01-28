package ch.globaz.pegasus.business.models.process.adaptation;

import globaz.jade.persistence.model.JadeComplexModel;

public class DonneeFinanciere extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csRole;
    private String csTypeDonneeFinanciere;
    private String dessaisissementFortuneDeductions;
    private String dessaisissementFortuneMontant;
    private String idMembreFamille;
    private Boolean isParticipationLCA;
    private String montantJournalierLCA;
    private String prixChambreJournalier;
    private String renteAVSAICsType;
    private String renteAVSAICsTypeSansRente;
    private String renteAVSAIMontant;
    private String taxeJournaliereIdTypeChambre;

    public String getCsRole() {
        return csRole;
    }

    public String getCsTypeDonneeFinanciere() {
        return csTypeDonneeFinanciere;
    }

    public String getDessaisissementFortuneDeductions() {
        return dessaisissementFortuneDeductions;
    }

    public String getDessaisissementFortuneMontant() {
        return dessaisissementFortuneMontant;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    public Boolean getIsParticipationLCA() {
        return isParticipationLCA;
    }

    public String getMontantJournalierLCA() {
        return montantJournalierLCA;
    }

    public String getPrixChambreJournalier() {
        return prixChambreJournalier;
    }

    public String getRenteAVSAICsType() {
        return renteAVSAICsType;
    }

    public String getRenteAVSAICsTypeSansRente() {
        return renteAVSAICsTypeSansRente;
    }

    public String getRenteAVSAIMontant() {
        return renteAVSAIMontant;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTaxeJournaliereIdTypeChambre() {
        return taxeJournaliereIdTypeChambre;
    }

    public void setCsRole(String csRole) {
        this.csRole = csRole;
    }

    public void setCsTypeDonneeFinanciere(String csTypeDonneeFinanciere) {
        this.csTypeDonneeFinanciere = csTypeDonneeFinanciere;
    }

    public void setDessaisissementFortuneDeductions(String dessaisissementFortuneDeductions) {
        this.dessaisissementFortuneDeductions = dessaisissementFortuneDeductions;
    }

    public void setDessaisissementFortuneMontant(String dessaisissementFortuneMontant) {
        this.dessaisissementFortuneMontant = dessaisissementFortuneMontant;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    public void setIsParticipationLCA(Boolean isParticipationLCA) {
        this.isParticipationLCA = isParticipationLCA;
    }

    public void setMontantJournalierLCA(String montantJournalierLCA) {
        this.montantJournalierLCA = montantJournalierLCA;
    }

    public void setPrixChambreJournalier(String prixChambreJournalier) {
        this.prixChambreJournalier = prixChambreJournalier;
    }

    public void setRenteAVSAICsType(String renteAVSAICsType) {
        this.renteAVSAICsType = renteAVSAICsType;
    }

    public void setRenteAVSAICsTypeSansRente(String renteAVSAICsTypeSansRente) {
        this.renteAVSAICsTypeSansRente = renteAVSAICsTypeSansRente;
    }

    public void setRenteAVSAIMontant(String renteAVSAIMontant) {
        this.renteAVSAIMontant = renteAVSAIMontant;
    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

    public void setTaxeJournaliereIdTypeChambre(String taxeJournaliereIdTypeChambre) {
        this.taxeJournaliereIdTypeChambre = taxeJournaliereIdTypeChambre;
    }
}
