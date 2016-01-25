package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

public class AffiliationSimpleModel extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String affiliationId;
    private String affilieNumero;
    private String brancheEconomique;
    private String dateDebut;
    private String dateFin;
    private String idConvention;
    private String idTiers;
    private String motifFin;
    private String periodicite;
    private String personnaliteJuridique;
    private String raisonSociale;
    private String raisonSocialeCourt;
    private String raisonSocialeUpper;
    private Boolean releveParitaire;
    private Boolean relevePersonnel;
    private String typeAffiliation;
    private String accesSecurite;

    public String getAffiliationId() {
        return affiliationId;
    }

    public String getAffilieNumero() {
        return affilieNumero;
    }

    public String getBrancheEconomique() {
        return brancheEconomique;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getId() {
        return getAffiliationId();
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMotifFin() {
        return motifFin;
    }

    public String getPeriodicite() {
        return periodicite;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public String getRaisonSocialeCourt() {
        return raisonSocialeCourt;
    }

    public String getRaisonSocialeUpper() {
        return raisonSocialeUpper;
    }

    public Boolean getReleveParitaire() {
        return releveParitaire;
    }

    public Boolean getRelevePersonnel() {
        return relevePersonnel;
    }

    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    public void setAffilieNumero(String affilieNumero) {
        this.affilieNumero = affilieNumero;
    }

    public void setBrancheEconomique(String brancheEconomique) {
        this.brancheEconomique = brancheEconomique;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setId(String id) {
        setAffiliationId(id);
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMotifFin(String motifFin) {
        this.motifFin = motifFin;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public void setRaisonSocialeCourt(String raisonSocialeCourt) {
        this.raisonSocialeCourt = raisonSocialeCourt;
    }

    public void setRaisonSocialeUpper(String raisonSocialUpper) {
        raisonSocialeUpper = raisonSocialUpper;
    }

    public void setReleveParitaire(Boolean releveParitaire) {
        this.releveParitaire = releveParitaire;
    }

    public void setRelevePersonnel(Boolean relevePersonnel) {
        this.relevePersonnel = relevePersonnel;
    }

    public void setTypeAffiliation(String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }

    public String getIdConvention() {
        return idConvention;
    }

    public void setIdConvention(String idConvention) {
        this.idConvention = idConvention;
    }

    public String getPersonnaliteJuridique() {
        return personnaliteJuridique;
    }

    public void setPersonnaliteJuridique(String personnaliteJuridique) {
        this.personnaliteJuridique = personnaliteJuridique;
    }

    public String getAccesSecurite() {
        return accesSecurite;
    }

    public void setAccesSecurite(String accesSecurite) {
        this.accesSecurite = accesSecurite;
    }

}
