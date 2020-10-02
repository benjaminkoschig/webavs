package ch.globaz.pegasus.business.models.habitat;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DMA
 * @date 26 juil. 2010
 */
public class SimpleLoyer extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csMotifChangementLoyer;
    private String csTypeLoyer;
    private String fraisPlacementEnfant;
    private String idBailleurRegie;
    private String idDonneeFinanciereHeader;
    private String idLoyer;
    private Boolean isFauteuilRoulant = Boolean.FALSE;
    private Boolean isTenueMenage = Boolean.FALSE;
    private String montantCharges;
    private String montantLoyerNet;
    private String nbPersonnes;
    private String pensionNonReconnue;
    private String revenuSousLocation;
    private String taxeJournalierePensionNonReconnue;
    private String csDeplafonnementAppartementPartage;
    private String idLocalite;
    private String textLibre;
    private boolean isCopy = false;

    /**
     * @return the csMotifChangementLoyer
     */
    public String getCsMotifChangementLoyer() {
        return csMotifChangementLoyer;
    }

    /**
     * @return the csTypeLoyer
     */
    public String getCsTypeLoyer() {
        return csTypeLoyer;
    }

    /**
     * @return the fraisPlacementEnfant
     */
    public String getFraisPlacementEnfant() {
        return fraisPlacementEnfant;
    }

    @Override
    public String getId() {
        return idLoyer;
    }

    /**
     * @return the idBailleurRegie
     */
    public String getIdBailleurRegie() {
        return idBailleurRegie;
    }

    /**
     * @return the idDonneeFinanciereHeader
     */
    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    /**
     * @return the idLoyer
     */
    public String getIdLoyer() {
        return idLoyer;
    }

    /**
     * @return the isFauteuilRoulant
     */
    public Boolean getIsFauteuilRoulant() {
        return isFauteuilRoulant;
    }

    /**
     * @return the isTenueMenage
     */
    public Boolean getIsTenueMenage() {
        return isTenueMenage;
    }

    /**
     * @return the montantCharges
     */
    public String getMontantCharges() {
        return montantCharges;
    }

    /**
     * @return the montantLoyerNet
     */
    public String getMontantLoyerNet() {
        return montantLoyerNet;
    }

    /**
     * @return the nbPersonnes
     */
    public String getNbPersonnes() {
        return nbPersonnes;
    }

    /**
     * @return the pensionNonReconnue
     */
    public String getPensionNonReconnue() {
        return pensionNonReconnue;
    }

    /**
     * @return the revenuSousLocation
     */
    public String getRevenuSousLocation() {
        return revenuSousLocation;
    }

    /**
     * @return the taxeJournalierePensionNonReconnue
     */
    public String getTaxeJournalierePensionNonReconnue() {
        return taxeJournalierePensionNonReconnue;
    }

    public String getCsDeplafonnementAppartementPartage() {
        return csDeplafonnementAppartementPartage;
    }

    /**
     * @param csMotifChangementLoyer
     *            the csMotifChangementLoyer to set
     */
    public void setCsMotifChangementLoyer(String csMotifChangementLoyer) {
        this.csMotifChangementLoyer = csMotifChangementLoyer;
    }

    /**
     * @param csTypeLoyer
     *            the csTypeLoyer to set
     */
    public void setCsTypeLoyer(String csTypeLoyer) {
        this.csTypeLoyer = csTypeLoyer;
    }

    /**
     * @param fraisPlacementEnfant
     *            the fraisPlacementEnfant to set
     */
    public void setFraisPlacementEnfant(String fraisPlacementEnfant) {
        this.fraisPlacementEnfant = fraisPlacementEnfant;
    }

    @Override
    public void setId(String id) {
        idLoyer = id;
    }

    /**
     * @param idBailleurRegie
     *            the idBailleurRegie to set
     */
    public void setIdBailleurRegie(String idBailleurRegie) {
        this.idBailleurRegie = idBailleurRegie;
    }

    /**
     * @param idDonneeFinanciereHeader
     *            the idDonneeFinanciereHeader to set
     */
    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    /**
     * @param idLoyer
     *            the idLoyer to set
     */
    public void setIdLoyer(String idLoyer) {
        this.idLoyer = idLoyer;
    }

    /**
     * @param isFauteuilRoulant
     *            the isFauteuilRoulant to set
     */
    public void setIsFauteuilRoulant(Boolean isFauteuilRoulant) {
        this.isFauteuilRoulant = isFauteuilRoulant;
    }

    /**
     * @param isTenueMenage
     *            the isTenueMenage to set
     */
    public void setIsTenueMenage(Boolean isTenueMenage) {
        this.isTenueMenage = isTenueMenage;
    }

    /**
     * @param montantCharges
     *            the montantCharges to set
     */
    public void setMontantCharges(String montantCharges) {
        this.montantCharges = montantCharges;
    }

    /**
     * @param montantLoyerNet
     *            the montantLoyerNet to set
     */
    public void setMontantLoyerNet(String montantLoyerNet) {
        this.montantLoyerNet = montantLoyerNet;
    }

    /**
     * @param nbPersonnes
     *            the nbPersonnes to set
     */
    public void setNbPersonnes(String nbPersonnes) {
        this.nbPersonnes = nbPersonnes;
    }

    /**
     * @param pension_non_reconnue
     *            the pensionNonReconnue to set
     */
    public void setPensionNonReconnue(String pensionNonReconnue) {
        this.pensionNonReconnue = pensionNonReconnue;
    }

    /**
     * @param revenuSousLocation
     *            the revenuSousLocation to set
     */
    public void setRevenuSousLocation(String revenuSousLocation) {
        this.revenuSousLocation = revenuSousLocation;
    }

    /**
     * @param taxeJournalierePensionNonReconnue
     *            the taxeJournalierePensionNonReconnue to set
     */
    public void setTaxeJournalierePensionNonReconnue(String taxeJournalierePensionNonReconnue) {
        this.taxeJournalierePensionNonReconnue = taxeJournalierePensionNonReconnue;
    }

    public void setCsDeplafonnementAppartementPartage(String csDeplafonnementAppartementPartage) {
        this.csDeplafonnementAppartementPartage = csDeplafonnementAppartementPartage;
    }

    public String getIdLocalite() {
        return idLocalite;
    }

    public void setIdLocalite(String idLocalite) {
        this.idLocalite = idLocalite;
    }

    public boolean isCopy() {
        return isCopy;
    }

    public void setCopy(boolean copy) {
        isCopy = copy;
    }

    public String getTextLibre() {
        return textLibre;
    }

    public void setTextLibre(String textLibre) {
        this.textLibre = textLibre;
    }
}
