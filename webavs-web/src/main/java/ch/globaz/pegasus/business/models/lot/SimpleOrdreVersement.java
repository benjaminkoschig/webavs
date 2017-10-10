/**
 * 
 */
package ch.globaz.pegasus.business.models.lot;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author BSC Modele simple pour les ordres de versement
 */
public class SimpleOrdreVersement extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csType = null;
    private String csTypeDomaine = null;
    private String idCompteAnnexe;
    private String idDetteComptatCompense = null;
    private String idDomaineApplication = null;
    private String idDomaineApplicationConjoint = null;
    private String idOrdreVersement = null;
    private String idPca = null;
    private String idPrestation = null;
    private String idSection = null;
    private String idSectionDetteEnCompta = null;
    private String idTiers = null;
    private String idTiersAdressePaiement = null;
    private String idTiersAdressePaiementConjoint = null;
    private String idTiersConjoint;
    private String idTiersOwnerDetteCreance = null;
    private boolean isCompense = false;
    private String montant = null;
    private String montantDetteModifier = null;
    private String noGroupePeriode = null;
    private String sousTypeGenrePrestation = null;
    private String refPaiement; // seulement utilisé pour les créanciers

    private boolean isPartCantonale = false;

    public String getCsType() {
        return csType;
    }

    public String getCsTypeDomaine() {
        return csTypeDomaine;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idOrdreVersement;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdDetteComptatCompense() {
        return idDetteComptatCompense;
    }

    public String getIdDomaineApplication() {
        return idDomaineApplication;
    }

    public String getIdDomaineApplicationConjoint() {
        return idDomaineApplicationConjoint;
    }

    public String getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public String getIdPca() {
        return idPca;
    }

    public String getIdPrestation() {
        return idPrestation;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getIdSectionDetteEnCompta() {
        return idSectionDetteEnCompta;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getIdTiersAdressePaiement() {
        return idTiersAdressePaiement;
    }

    public String getIdTiersAdressePaiementConjoint() {
        return idTiersAdressePaiementConjoint;
    }

    public String getIdTiersConjoint() {
        return idTiersConjoint;
    }

    public String getIdTiersOwnerDetteCreance() {
        return idTiersOwnerDetteCreance;
    }

    public boolean getIsCompense() {
        return isCompense;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantDetteModifier() {
        return montantDetteModifier;
    }

    public String getNoGroupePeriode() {
        return noGroupePeriode;
    }

    public String getSousTypeGenrePrestation() {
        return sousTypeGenrePrestation;
    }

    public void setCompense(boolean isCompense) {
        this.isCompense = isCompense;
    }

    public void setCsType(String csType) {
        this.csType = csType;
    }

    public void setCsTypeDomaine(String csTypeDomaine) {
        this.csTypeDomaine = csTypeDomaine;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idOrdreVersement = id;

    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdDetteComptatCompense(String idDetteComptatCompense) {
        this.idDetteComptatCompense = idDetteComptatCompense;
    }

    public void setIdDomaineApplication(String idDomaineApplication) {
        this.idDomaineApplication = idDomaineApplication;
    }

    public void setIdDomaineApplicationConjoint(String idDomaineApplicationConjoint) {
        this.idDomaineApplicationConjoint = idDomaineApplicationConjoint;
    }

    public void setIdOrdreVersement(String idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    public void setIdPrestation(String idPrestation) {
        this.idPrestation = idPrestation;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setIdSectionDetteEnCompta(String idSectionDetteEnCompta) {
        this.idSectionDetteEnCompta = idSectionDetteEnCompta;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIdTiersAdressePaiement(String idTiersAdressePaiement) {
        this.idTiersAdressePaiement = idTiersAdressePaiement;
    }

    public void setIdTiersAdressePaiementConjoint(String idTiersAdressePaiementConjoint) {
        this.idTiersAdressePaiementConjoint = idTiersAdressePaiementConjoint;
    }

    public void setIdTiersConjoint(String idTiersConjoint) {
        this.idTiersConjoint = idTiersConjoint;
    }

    public void setIdTiersOwnerDetteCreance(String idTiersOwnerDetteCreance) {
        this.idTiersOwnerDetteCreance = idTiersOwnerDetteCreance;
    }

    public void setIsCompense(boolean isCompense) {
        this.isCompense = isCompense;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantDetteModifier(String montantDetteModifier) {
        this.montantDetteModifier = montantDetteModifier;
    }

    public void setNoGroupePeriode(String noGroupePeriode) {
        this.noGroupePeriode = noGroupePeriode;
    }

    public void setSousTypeGenrePrestation(String sousTypeGenrePrestation) {
        this.sousTypeGenrePrestation = sousTypeGenrePrestation;
    }

    @Override
    public String toString() {
        return "SimpleOrdreVersement [csType=" + csType + ", csTypeDomaine=" + csTypeDomaine
                + ", idDetteComptatCompense=" + idDetteComptatCompense + ", idDomaineApplication="
                + idDomaineApplication + ", idOrdreVersement=" + idOrdreVersement + ", idPca=" + idPca
                + ", idPrestation=" + idPrestation + ", idSectionDetteEnCompta=" + idSectionDetteEnCompta
                + ", idTiers=" + idTiers + ", idTiersAdressePaiement=" + idTiersAdressePaiement
                + ", idTiersAdressePaiementConjoint=" + idTiersAdressePaiementConjoint + ", isCompense=" + isCompense
                + ", montant=" + montant + ", montantDetteModifier=" + montantDetteModifier + ", noGroupePeriode="
                + noGroupePeriode + ", sousTypeGenrePrestation=" + sousTypeGenrePrestation + "]";
    }

    public String getRefPaiement() {
        return refPaiement;
    }

    public void setRefPaiement(String refPaiement) {
        this.refPaiement = refPaiement;
    }

    public boolean isPartCantonale() {
        return isPartCantonale;
    }

    public void setPartCantonale(boolean isPartCantonale) {
        this.isPartCantonale = isPartCantonale;
    }
}
