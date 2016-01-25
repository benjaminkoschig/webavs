package ch.globaz.pegasus.business.models.process.statistiquesofas;

import globaz.jade.persistence.model.JadeComplexModel;

public class PlanCalculeDemandeDroitMembreFamille extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String canton = null;
    private String csGenrePC = null;
    private String csRoleFamillePC = null;
    private String dateDebutDemande = null;
    private String dateNaissance = null;
    private String etatCivil = null;
    private String idDroit = null;
    private String idMembreFamilleSF = null;
    private String idPcaParent = null;
    private String idPlandDeCalcule = null;
    private String idTiersMembreFamille = null;
    // private SimplePCAccordee simplePCAccordee = null;
    // private LocaliteSimpleModel localiteSimpleModel = null;
    // private MembreFamilleEtendu membreFamilleEtendu = null;
    // private SimpleDemande simpleDemande = null;
    // private SimpleDroit simpleDroit = null;
    private String idVersionDroit = null;
    private Boolean isEnfant = false;
    // private SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul = null;
    // private SimplePlanDeCalcul simplePlanDeCalcul = null;
    private String nationalite = null;
    private String numAvs = null;
    private String numCommuneOfs = null;
    private String sexe = null;

    public String getCanton() {
        return canton;
    }

    public String getCsGenrePC() {
        return csGenrePC;
    }

    public String getCsRoleFamillePC() {
        return csRoleFamillePC;
    }

    public String getDateDebutDemande() {
        return dateDebutDemande;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getEtatCivil() {
        return etatCivil;
    }

    @Override
    public String getId() {
        return null;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdMembreFamilleSF() {
        return idMembreFamilleSF;
    }

    public String getIdPcaParent() {
        return idPcaParent;
    }

    public String getIdPlandDeCalcule() {
        return idPlandDeCalcule;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public Boolean getIsEnfant() {
        return isEnfant;
    }

    public String getNationalite() {
        return nationalite;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getNumCommuneOfs() {
        return numCommuneOfs;
    }

    public String getSexe() {
        return sexe;
    }

    @Override
    public String getSpy() {
        return null;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public void setCsGenrePC(String csGenrePC) {
        this.csGenrePC = csGenrePC;
    }

    public void setCsRoleFamillePC(String csRoleFamillePC) {
        this.csRoleFamillePC = csRoleFamillePC;
    }

    public void setDateDebutDemande(String dateDebutDemande) {
        this.dateDebutDemande = dateDebutDemande;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    @Override
    public void setId(String id) {

    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdMembreFamilleSF(String idMembreFamilleSF) {
        this.idMembreFamilleSF = idMembreFamilleSF;
    }

    public void setIdPcaParent(String idPcaParent) {
        this.idPcaParent = idPcaParent;
    }

    public void setIdPlandDeCalcule(String idPlandDeCalcule) {
        this.idPlandDeCalcule = idPlandDeCalcule;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setIsEnfant(Boolean isEnfant) {
        this.isEnfant = isEnfant;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    public void setNumCommuneOfs(String numCommuneOfs) {
        this.numCommuneOfs = numCommuneOfs;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    @Override
    public void setSpy(String spy) {

    }

    public void setIdTiersMembreFamille(String idTiersMembreFamille) {
        this.idTiersMembreFamille = idTiersMembreFamille;
    }

    public String getIdTiersMembreFamille() {
        return idTiersMembreFamille;
    }

}
