package ch.globaz.pegasus.business.models.revenusdepenses;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * 
 * @author FHA
 * 
 */
public class SimplePensionAlimentaire extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String autreLienAvecRequerantPC = null;
    private String csLienAvecRequerantPC = null;
    private String csMotif = null;
    private String csTypePension = null;
    private String dateEcheance = null;
    private String idDonneeFinanciereHeader = null;
    private String idPensionAlimentaire = null;
    private String idTiers = null;
    private Boolean isDeductionRenteEnfant = Boolean.FALSE;
    private String montantPensionAlimentaire = null;
    private String montantRenteEnfant = null;

    public String getAutreLienAvecRequerantPC() {
        return autreLienAvecRequerantPC;
    }

    public String getCsLienAvecRequerantPC() {
        return csLienAvecRequerantPC;
    }

    public String getCsMotif() {
        return csMotif;
    }

    public String getCsTypePension() {
        return csTypePension;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idPensionAlimentaire;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getIdPensionAlimentaire() {
        return idPensionAlimentaire;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsDeductionRenteEnfant() {
        return isDeductionRenteEnfant;
    }

    public String getMontantPensionAlimentaire() {
        return montantPensionAlimentaire;
    }

    public String getMontantRenteEnfant() {
        return montantRenteEnfant;
    }

    public void setAutreLienAvecRequerantPC(String autreLienAvecRequerantPC) {
        this.autreLienAvecRequerantPC = autreLienAvecRequerantPC;
    }

    public void setCsLienAvecRequerantPC(String csLienAvecRequerantPC) {
        this.csLienAvecRequerantPC = csLienAvecRequerantPC;
    }

    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    public void setCsTypePension(String csTypePension) {
        this.csTypePension = csTypePension;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idPensionAlimentaire = id;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setIdPensionAlimentaire(String idPensionAlimentaire) {
        this.idPensionAlimentaire = idPensionAlimentaire;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsDeductionRenteEnfant(Boolean isDeductionRenteEnfant) {
        this.isDeductionRenteEnfant = isDeductionRenteEnfant;
    }

    public void setMontantPensionAlimentaire(String montantPensionAlimentaire) {
        this.montantPensionAlimentaire = montantPensionAlimentaire;
    }

    public void setMontantRenteEnfant(String montantRenteEnfant) {
        this.montantRenteEnfant = montantRenteEnfant;
    }

}