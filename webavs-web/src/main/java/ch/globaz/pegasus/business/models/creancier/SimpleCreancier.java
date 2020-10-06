package ch.globaz.pegasus.business.models.creancier;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DMA
 * @date 30 nov. 2010
 */
public class SimpleCreancier extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csEtat = null;
    private String csTypeCreance = null;
    private String idAffilieAdressePaiment = null;
    private String idCreancier = null;
    private String idDemande = null;
    private String idDomaineApplicatif = null;
    private String idTiers = null;
    private String idTiersAdressePaiement = null;
    private String idTiersRegroupement = null;
    private Boolean isBloque = false;
    private String montant = null;
    private String referencePaiement = null;
    private Boolean isCalcule = false;

    /**
     * @return the csEtat
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * @return the csTypeCreance
     */
    public String getCsTypeCreance() {
        return csTypeCreance;
    }

    @Override
    public String getId() {
        return getIdCreancier();
    }

    /**
     * @return the idAffilieAdressePaiment
     */
    public String getIdAffilieAdressePaiment() {
        return idAffilieAdressePaiment;
    }

    /**
     * @return the idCreancier
     */
    public String getIdCreancier() {
        return idCreancier;
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return the idDomaineApplicatif
     */
    public String getIdDomaineApplicatif() {
        return idDomaineApplicatif;
    }

    /**
     * @return the idTiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return the idTiersAdressePaiement
     */
    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    /**
     * @return the idTiersRegroupement
     */
    public String getIdTiersRegroupement() {
        return idTiersRegroupement;
    }

    /**
     * @return the isBloque
     */
    public Boolean getIsBloque() {
        return isBloque;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the referencePaiement
     */
    public String getReferencePaiement() {
        return referencePaiement;
    }

    /**
     * @param csEtat
     *            the csEtat to set
     */
    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    /**
     * @param csTypeCreance
     *            the csTypeCreance to set
     */
    public void setCsTypeCreance(String csTypeCreance) {
        this.csTypeCreance = csTypeCreance;
    }

    @Override
    public void setId(String id) {
        idCreancier = id;
    }

    /**
     * @param idAffilieAdressePaiment
     *            the idAffilieAdressePaiment to set
     */
    public void setIdAffilieAdressePaiment(String idAffilieAdressePaiment) {
        this.idAffilieAdressePaiment = idAffilieAdressePaiment;
    }

    /**
     * @param idCreancier
     *            the idCreancier to set
     */
    public void setIdCreancier(String idCreancier) {
        this.idCreancier = idCreancier;
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param idDomaineApplicatif
     *            the idDomaineApplicatif to set
     */
    public void setIdDomaineApplicatif(String idDomaineApplicatif) {
        this.idDomaineApplicatif = idDomaineApplicatif;
    }

    /**
     * @param idTiers
     *            the idTiers to set
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @param idTiersAdressePaiement
     *            the idTiersAdressePaiement to set
     */
    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    /**
     * @param idTiersRegroupement
     *            the idTiersRegroupement to set
     */
    public void setIdTiersRegroupement(String idTiersRegroupement) {
        this.idTiersRegroupement = idTiersRegroupement;
    }

    /**
     * @param isBloque
     *            the isBloque to set
     */
    public void setIsBloque(Boolean isBloque) {
        this.isBloque = isBloque;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param referencePaiement
     *            the referencePaiement to set
     */
    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

    public Boolean getIsCalcule() {
        return isCalcule;
    }

    public void setIsCalcule(Boolean isCalcule) {
        this.isCalcule = isCalcule;
    }

}
