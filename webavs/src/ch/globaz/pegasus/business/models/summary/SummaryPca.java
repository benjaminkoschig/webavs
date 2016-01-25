package ch.globaz.pegasus.business.models.summary;

import globaz.jade.persistence.model.JadeComplexModel;

public class SummaryPca extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtatPca;
    private String csGenrePca;
    private String csRoleBeneficiaire;
    private String csTypePca;
    private String dateDebutPca;
    private String dateFinPca;
    private String idDossier;
    private String idDroit;
    private String idPca;
    private String idTiers;
    private String nss;

    public String getCsEtatPca() {
        return csEtatPca;
    }

    public String getCsGenrePca() {
        return csGenrePca;
    }

    public String getCsRoleBeneficiaire() {
        return csRoleBeneficiaire;
    }

    public String getCsTypePca() {
        return csTypePca;
    }

    public String getDateDebutPca() {
        return dateDebutPca;
    }

    public String getDateFinPca() {
        return dateFinPca;
    }

    @Override
    public String getId() {
        return null;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdPca() {
        return idPca;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNss() {
        return nss;
    }

    @Override
    public String getSpy() {
        return null;
    }

    public void setCsEtatPca(String csEtatPca) {
        this.csEtatPca = csEtatPca;
    }

    public void setCsGenrePca(String csGenrePca) {
        this.csGenrePca = csGenrePca;
    }

    public void setCsRoleBeneficiaire(String csRoleBeneficiaire) {
        this.csRoleBeneficiaire = csRoleBeneficiaire;
    }

    public void setCsTypePca(String csTypePca) {
        this.csTypePca = csTypePca;
    }

    public void setDateDebutPca(String dateDebutPca) {
        this.dateDebutPca = dateDebutPca;
    }

    public void setDateFinPca(String dateFinPca) {
        this.dateFinPca = dateFinPca;
    }

    @Override
    public void setId(String id) {
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    @Override
    public void setSpy(String spy) {
    }

    /*
     * PCPCACC1.CUTGEN AS CS_GENRE, PCPCACC1.CUDFIN AS DATE_FIN, PCPCACC1.CUTRBE AS CS_ROLE, PCPCACC1.CUTETA AS
     * CS_ETAT_PC, PCPCACC1.CUIPCA AS ID, TIPAVSP1.HXNAVS AS NSS, REPRACC1.ZTITBE AS ID_TIERS, PCPCACC1.CUTTYP AS
     * CS_TYPE, PCPCACC1.CUDDEB AS DATE_DEBUT
     */

}
