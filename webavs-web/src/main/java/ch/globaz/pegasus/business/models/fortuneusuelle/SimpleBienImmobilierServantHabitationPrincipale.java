package ch.globaz.pegasus.business.models.fortuneusuelle;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleBienImmobilierServantHabitationPrincipale extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String autresTypeBien = null;
    private String csTypeBien = null;
    private String csTypePropriete = null;
    private String idBienImmobilierServantHabitationPrincipale = null;
    private String idCommuneDuBien = null;
    private String idDonneeFinanciereHeader = null;
    private String montantDetteHypothecaire = null;
    private String montantInteretHypothecaire = null;
    private String montantLoyesEncaisses = null;
    private String montantSousLocation = null;
    private String montantValeurFiscale = null;
    private String montantValeurLocative = null;
    private String noFeuillet = null;
    private String noHypotheque = null;
    private String nombrePersonnes = null;
    private String nomCompagnie = null;
    private String partProprieteDenominateur = null;
    private String partProprieteNumerateur = null;
    private Boolean isConstructionMoinsDixAns = Boolean.FALSE;

    public String getAutresTypeBien() {
        return autresTypeBien;
    }

    public String getCsTypeBien() {
        return csTypeBien;
    }

    public String getCsTypePropriete() {
        return csTypePropriete;
    }

    @Override
    public String getId() {
        return idBienImmobilierServantHabitationPrincipale;
    }

    public String getIdBienImmobilierServantHabitationPrincipale() {
        return idBienImmobilierServantHabitationPrincipale;
    }

    public String getIdCommuneDuBien() {
        return idCommuneDuBien;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getMontantDetteHypothecaire() {
        return montantDetteHypothecaire;
    }

    public String getMontantInteretHypothecaire() {
        return montantInteretHypothecaire;
    }

    public String getMontantLoyesEncaisses() {
        return montantLoyesEncaisses;
    }

    public String getMontantSousLocation() {
        return montantSousLocation;
    }

    public String getMontantValeurFiscale() {
        return montantValeurFiscale;
    }

    public String getMontantValeurLocative() {
        return montantValeurLocative;
    }

    public String getNoFeuillet() {
        return noFeuillet;
    }

    public String getNoHypotheque() {
        return noHypotheque;
    }

    public String getNombrePersonnes() {
        return nombrePersonnes;
    }

    public String getNomCompagnie() {
        return nomCompagnie;
    }

    public String getPartProprieteDenominateur() {
        return partProprieteDenominateur;
    }

    public String getPartProprieteNumerateur() {
        return partProprieteNumerateur;
    }

    public void setAutresTypeBien(String autresTypeBien) {
        this.autresTypeBien = autresTypeBien;
    }

    public void setCsTypeBien(String csTypeBien) {
        this.csTypeBien = csTypeBien;
    }

    public void setCsTypePropriete(String csTypePropriete) {
        this.csTypePropriete = csTypePropriete;
    }

    @Override
    public void setId(String id) {
        idBienImmobilierServantHabitationPrincipale = id;
    }

    public void setIdBienImmobilierServantHabitationPrincipale(String idBienImmobilierServantHabitationPrincipale) {
        this.idBienImmobilierServantHabitationPrincipale = idBienImmobilierServantHabitationPrincipale;
    }

    public void setIdCommuneDuBien(String idCommuneDuBien) {
        this.idCommuneDuBien = idCommuneDuBien;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setMontantDetteHypothecaire(String montantDetteHypothecaire) {
        this.montantDetteHypothecaire = montantDetteHypothecaire;
    }

    public void setMontantInteretHypothecaire(String montantInteretHypothecaire) {
        this.montantInteretHypothecaire = montantInteretHypothecaire;
    }

    public void setMontantLoyesEncaisses(String montantLoyesEncaisses) {
        this.montantLoyesEncaisses = montantLoyesEncaisses;
    }

    public void setMontantSousLocation(String montantSousLocation) {
        this.montantSousLocation = montantSousLocation;
    }

    public void setMontantValeurFiscale(String montantValeurFiscale) {
        this.montantValeurFiscale = montantValeurFiscale;
    }

    public void setMontantValeurLocative(String montantValeurLocative) {
        this.montantValeurLocative = montantValeurLocative;
    }

    public void setNoFeuillet(String noFeuillet) {
        this.noFeuillet = noFeuillet;
    }

    public void setNoHypotheque(String noHypotheque) {
        this.noHypotheque = noHypotheque;
    }

    public void setNombrePersonnes(String nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
    }

    public void setNomCompagnie(String nomCompagnie) {
        this.nomCompagnie = nomCompagnie;
    }

    public void setPartProprieteDenominateur(String partProprieteDenominateur) {
        this.partProprieteDenominateur = partProprieteDenominateur;
    }

    public void setPartProprieteNumerateur(String partProprieteNumerateur) {
        this.partProprieteNumerateur = partProprieteNumerateur;
    }

    /**
     * Retourne une valeur booléenne permettant de savoir si l'habitation principale à moins de 10 ans
     * 
     * @return une valeur booléenne permettant de savoir si l'habitation principale à moins de 10 ans
     */
    public Boolean getIsConstructionMoinsDixAns() {
        return isConstructionMoinsDixAns;
    }

    /**
     * Définit à l'aide d'une valeur booléenne permettant de savoir si l'habitation principale à moins de 10 ans
     * 
     * @param constructionMoinsDixAns valeur booléenne permettant de savoir si l'habitation principale à moins de 10 ans
     */
    public void setIsConstructionMoinsDixAns(Boolean isConstructionMoinsDixAns) {
        this.isConstructionMoinsDixAns = isConstructionMoinsDixAns;
    }

}
