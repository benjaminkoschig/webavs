package ch.globaz.pegasus.business.models.fortuneusuelle;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleTitre extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String autreGenreTitre = null;
    private String csGenreTitre = null;
    private String csTypePropriete = null;
    private String designationTitre = null;
    private String droitDeGarde = null;
    private String idDonneeFinanciereHeader = null;
    private String idTitre = null;
    private Boolean isSansRendement = Boolean.FALSE;
    private String montantTitre = null;
    private String numeroValeur = null;
    private String partProprieteDenominateur = null;
    private String partProprieteNumerateur = null;
    private String rendementTitre = null;

    public String getAutreGenreTitre() {
        return autreGenreTitre;
    }

    public String getCsGenreTitre() {
        return csGenreTitre;
    }

    public String getCsTypePropriete() {
        return csTypePropriete;
    }

    public String getDesignationTitre() {
        return designationTitre;
    }

    public String getDroitDeGarde() {
        return droitDeGarde;
    }

    @Override
    public String getId() {
        return idTitre;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getIdTitre() {
        return idTitre;
    }

    public Boolean getIsSansRendement() {
        return isSansRendement;
    }

    public String getMontantTitre() {
        return montantTitre;
    }

    public String getNumeroValeur() {
        return numeroValeur;
    }

    public String getPartProprieteDenominateur() {
        return partProprieteDenominateur;
    }

    public String getPartProprieteNumerateur() {
        return partProprieteNumerateur;
    }

    public String getRendementTitre() {
        return rendementTitre;
    }

    public void setAutreGenreTitre(String autreGenreTitre) {
        this.autreGenreTitre = autreGenreTitre;
    }

    public void setCsGenreTitre(String csGenreTitre) {
        this.csGenreTitre = csGenreTitre;
    }

    public void setCsTypePropriete(String csTypePropriete) {
        this.csTypePropriete = csTypePropriete;
    }

    public void setDesignationTitre(String designationTitre) {
        this.designationTitre = designationTitre;
    }

    public void setDroitDeGarde(String droitDeGarde) {
        this.droitDeGarde = droitDeGarde;
    }

    @Override
    public void setId(String id) {
        idTitre = id;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setIdTitre(String idTitre) {
        this.idTitre = idTitre;
    }

    public void setIsSansRendement(Boolean isSansRendement) {
        this.isSansRendement = isSansRendement;
    }

    public void setMontantTitre(String montantTitre) {
        this.montantTitre = montantTitre;
    }

    public void setNumeroValeur(String numeroValeur) {
        this.numeroValeur = numeroValeur;
    }

    public void setPartProprieteDenominateur(String partProprieteDenominateur) {
        this.partProprieteDenominateur = partProprieteDenominateur;
    }

    public void setPartProprieteNumerateur(String partProprieteNumerateur) {
        this.partProprieteNumerateur = partProprieteNumerateur;
    }

    public void setRendementTitre(String rendementTitre) {
        this.rendementTitre = rendementTitre;
    }

}
