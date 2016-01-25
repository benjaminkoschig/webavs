/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author SCE Modele simple des décisions apres calcul 14 juil. 2010
 */
public class SimpleDecisionApresCalcul extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean allocNonActif = false;
    private Boolean annuleEtRemplacePrec = false;
    private String codeAmal = null;
    private String csTypePreparation = null;
    private String dateDecisionAmal = null;
    private String dateProchainPaiement = null;
    private Boolean diminutionPc = false;
    private String idDecisionApresCalcul = null;
    private String idDecisionHeader = null;
    private String idVersionDroit = null;
    private String introduction = null;
    private Boolean isMostRecent = false;
    private String remarqueGenerale = null;

    /**
     * @return the allocNonActif
     */
    public Boolean getAllocNonActif() {
        return allocNonActif;
    }

    /**
     * @return the annuleEtRemplacePrec
     */
    public Boolean getAnnuleEtRemplacePrec() {
        return annuleEtRemplacePrec;
    }

    public String getCodeAmal() {
        return codeAmal;
    }

    /**
     * @return the csTypePreparation
     */
    public String getCsTypePreparation() {
        return csTypePreparation;
    }

    public String getDateDecisionAmal() {
        return dateDecisionAmal;
    }

    public String getDateProchainPaiement() {
        return dateProchainPaiement;
    }

    /**
     * @return the diminutionPc
     */
    public Boolean getDiminutionPc() {
        return diminutionPc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idDecisionApresCalcul;
    }

    /**
     * @return the idDecisionApresCalcul
     */
    public String getIdDecisionApresCalcul() {
        return idDecisionApresCalcul;
    }

    /**
     * @return the idDecisionHeader
     */
    public String getIdDecisionHeader() {
        return idDecisionHeader;
    }

    /**
     * @return the idVersionDroit
     */
    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    /**
     * @return the introduction
     */
    public String getIntroduction() {
        return introduction;
    }

    public Boolean getIsMostRecent() {
        return isMostRecent;
    }

    /**
     * @return the remarqueGenerale
     */
    public String getRemarqueGenerale() {
        return remarqueGenerale;
    }

    /**
     * @param allocNonActif
     *            the allocNonActif to set
     */
    public void setAllocNonActif(Boolean allocNonActif) {
        this.allocNonActif = allocNonActif;
    }

    /**
     * @param annuleEtRemplacePrec
     *            the annuleEtRemplacePrec to set
     */
    public void setAnnuleEtRemplacePrec(Boolean annuleEtRemplacePrec) {
        this.annuleEtRemplacePrec = annuleEtRemplacePrec;
    }

    public void setCodeAmal(String codeAmal) {
        this.codeAmal = codeAmal;
    }

    /**
     * @param csTypePreparation
     *            the csTypePreparation to set
     */
    public void setCsTypePreparation(String csTypePreparation) {
        this.csTypePreparation = csTypePreparation;
    }

    public void setDateDecisionAmal(String dateDecisionAmal) {
        this.dateDecisionAmal = dateDecisionAmal;
    }

    public void setDateProchainPaiement(String dateProchainPaiement) {
        this.dateProchainPaiement = dateProchainPaiement;
    }

    /**
     * @param diminutionPc
     *            the diminutionPc to set
     */
    public void setDiminutionPc(Boolean diminutionPc) {
        this.diminutionPc = diminutionPc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDecisionApresCalcul = id;

    }

    /**
     * @param idDecisionApresCalcul
     *            the idDecisionApresCalcul to set
     */
    public void setIdDecisionApresCalcul(String idDecisionApresCalcul) {
        this.idDecisionApresCalcul = idDecisionApresCalcul;
    }

    /**
     * @param idDecisionHeader
     *            the idDecisionHeader to set
     */
    public void setIdDecisionHeader(String idDecisionHeader) {
        this.idDecisionHeader = idDecisionHeader;
    }

    /**
     * @param idVersionDroit
     *            the idVersionDroit to set
     */
    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    /**
     * @param introduction
     *            the introduction to set
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setIsMostRecent(Boolean isMostRecent) {
        this.isMostRecent = isMostRecent;
    }

    /**
     * @param remarqueGenerale
     *            the remarqueGenerale to set
     */
    public void setRemarqueGenerale(String remarqueGenerale) {
        this.remarqueGenerale = remarqueGenerale;
    }

}
