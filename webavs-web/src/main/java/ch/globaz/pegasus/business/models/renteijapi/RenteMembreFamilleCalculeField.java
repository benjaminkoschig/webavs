package ch.globaz.pegasus.business.models.renteijapi;

import globaz.jade.persistence.model.JadeComplexModel;

public class RenteMembreFamilleCalculeField extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtatDroit;
    private String csMotif;
    private String csRoleFamillePC;
    private String csTypeDonneeFinanciere;
    private String csTypeRenteAPI;
    private String csTypeRenteAVS;
    private String idDemandePC;
    private String idDonneeFinanciereHeader;
    private String idDroitMembreFamille;
    private String idEntity;
    private String idTiers;
    private String montantApi;
    private String montantAvsAi;
    private String nom;
    private String nss;
    private String dateNaissance;
    private String prenom;
    private String noCaisseAvs;

    public String getNoCaisseAvs() {
        return noCaisseAvs;
    }

    public void setNoCaisseAvs(String noCaisseAvs) {
        this.noCaisseAvs = noCaisseAvs;
    }

    public String getCsEtatDroit() {
        return csEtatDroit;
    }

    public String getCsMotif() {
        return csMotif;
    }

    public String getCsRoleFamillePC() {
        return csRoleFamillePC;
    }

    public String getCsTypeDonneeFinanciere() {
        return csTypeDonneeFinanciere;
    }

    public String getCsTypeRenteAPI() {
        return csTypeRenteAPI;
    }

    public String getCsTypeRenteAVS() {
        return csTypeRenteAVS;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdDemandePC() {
        return idDemandePC;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getIdDroitMembreFamille() {
        return idDroitMembreFamille;
    }

    public String getIdEntity() {
        return idEntity;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontantApi() {
        return montantApi;
    }

    public String getMontantAvsAi() {
        return montantAvsAi;
    }

    public String getNom() {
        return nom;
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

    public void setCsEtatDroit(String csEtatDroit) {
        this.csEtatDroit = csEtatDroit;
    }

    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    public void setCsRoleFamillePC(String csRoleFamillePC) {
        this.csRoleFamillePC = csRoleFamillePC;
    }

    public void setCsTypeDonneeFinanciere(String csTypeDonneeFinanciere) {
        this.csTypeDonneeFinanciere = csTypeDonneeFinanciere;
    }

    public void setCsTypeRenteAPI(String csTypeRenteAPI) {
        this.csTypeRenteAPI = csTypeRenteAPI;
    }

    public void setCsTypeRenteAVS(String csTypeRenteAVS) {
        this.csTypeRenteAVS = csTypeRenteAVS;
    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub

    }

    public void setIdDemandePC(String idDemandePC) {
        this.idDemandePC = idDemandePC;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setIdDroitMembreFamille(String idDroitMembreFamille) {
        this.idDroitMembreFamille = idDroitMembreFamille;
    }

    public void setIdEntity(String idEntity) {
        this.idEntity = idEntity;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontantApi(String montantApi) {
        this.montantApi = montantApi;
    }

    public void setMontantAvsAi(String montantAvsAi) {
        this.montantAvsAi = montantAvsAi;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
}
