package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimplePCAccordee extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeRente = null;
    private String csEtatPC = null;
    private String csGenrePC = null;
    private String csRoleBeneficiaire = null;
    private String csTypePC = null;
    private String dateDebut = null;
    private String dateFin = null;
    private Boolean hasCalculComparatif = null;
    private Boolean hasJoursAppoint = null;
    private String idEntity = null;
    private String idEntityGroup = null;
    private String idPCAccordee = null;
    private String idPcaParent = null;
    private String idPrestationAccordee = null;
    private String idPrestationAccordeeConjoint = null;
    private String idVersionDroit = null;
    private Boolean isCalculManuel = null;
    private Boolean isCalculRetro = null;
    private Boolean isSupprime = null;
    private Boolean isDateFinForce = false;
    private Boolean isProvisoire = false;

    //NOT IN DB
    private String montantMensuel = "";

    public String getCodeRente() {
        return codeRente;
    }

    /**
     * @return the csEtatPC
     */
    public String getCsEtatPC() {
        return csEtatPC;
    }

    public String getCsGenrePC() {
        return csGenrePC;
    }

    public String getCsRoleBeneficiaire() {
        return csRoleBeneficiaire;
    }

    public String getCsTypePC() {
        return csTypePC;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return the hasCalculComparatif
     */
    public Boolean getHasCalculComparatif() {
        return hasCalculComparatif;
    }

    public Boolean getHasJoursAppoint() {
        return hasJoursAppoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idPCAccordee;
    }

    public String getIdEntity() {
        return idEntity;
    }

    public String getIdEntityGroup() {
        return idEntityGroup;
    }

    public String getIdPCAccordee() {
        return idPCAccordee;
    }

    public String getIdPcaParent() {
        return idPcaParent;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    public String getIdPrestationAccordeeConjoint() {
        return idPrestationAccordeeConjoint;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public Boolean getIsCalculManuel() {
        return isCalculManuel;
    }

    public Boolean getIsCalculRetro() {
        return isCalculRetro;
    }

    public Boolean getIsSupprime() {
        return isSupprime;
    }

    public void setCodeRente(String codeRente) {
        this.codeRente = codeRente;
    }

    /**
     * @param csEtatPC
     *            the csEtatPC to set
     */
    public void setCsEtatPC(String csEtatPC) {
        this.csEtatPC = csEtatPC;
    }

    public void setCsGenrePC(String csGenrePC) {
        this.csGenrePC = csGenrePC;
    }

    public void setCsRoleBeneficiaire(String csRoleBeneficiaire) {
        this.csRoleBeneficiaire = csRoleBeneficiaire;
    }

    public void setCsTypePC(String csTypePC) {
        this.csTypePC = csTypePC;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param hasCalculComparatif
     *            the hasCalculComparatif to set
     */
    public void setHasCalculComparatif(Boolean hasCalculComparatif) {
        this.hasCalculComparatif = hasCalculComparatif;
    }

    public void setHasJoursAppoint(Boolean hasJoursAppoint) {
        this.hasJoursAppoint = hasJoursAppoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idPCAccordee = id;
    }

    public void setIdEntity(String idEntity) {
        this.idEntity = idEntity;
    }

    public void setIdEntityGroup(String idEntityGroup) {
        this.idEntityGroup = idEntityGroup;
    }

    public void setIdPCAccordee(String idPCAccordee) {
        this.idPCAccordee = idPCAccordee;
    }

    public void setIdPcaParent(String idPcaParent) {
        this.idPcaParent = idPcaParent;
    }

    public void setIdPrestationAccordee(String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    public void setIdPrestationAccordeeConjoint(String idPrestationAccordeeConjoint) {
        this.idPrestationAccordeeConjoint = idPrestationAccordeeConjoint;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public void setIsCalculManuel(Boolean isCalculManuel) {
        this.isCalculManuel = isCalculManuel;
    }

    public void setIsCalculRetro(Boolean isCalculRetro) {
        this.isCalculRetro = isCalculRetro;
    }

    public void setIsSupprime(Boolean isSupprime) {
        this.isSupprime = isSupprime;
    }

    public Boolean getIsDateFinForce() {
        return isDateFinForce;
    }

    public void setIsDateFinForce(Boolean isDateFinForce) {
        this.isDateFinForce = isDateFinForce;
    }

    public Boolean getIsProvisoire() {
        return isProvisoire;
    }

    public void setIsProvisoire(Boolean isProvisoire) {
        this.isProvisoire = isProvisoire;
    }
    public String getMontantMensuel() {
        return montantMensuel;
    }

    public void setMontantMensuel(String montantMensuel) {
        this.montantMensuel = montantMensuel;
    }
}
