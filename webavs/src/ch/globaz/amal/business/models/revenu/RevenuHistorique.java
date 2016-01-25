/**
 * 
 */
package ch.globaz.amal.business.models.revenu;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Classe représentant les données de revenu historique
 * 
 * UTILISE POUR LA LISTE DES REVENUS UNIQUEMENT
 * 
 * @author dhi
 * 
 */
public class RevenuHistorique extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String anneeHistorique = null;

    private String anneeTaxation = null;

    private String dateAvisTaxation = null;

    private String etatCivil = null;

    private String etatCivilLibelle = null;

    private String idContribuable = null;

    private String idRevenu = null;

    private String idRevenuDeterminant = null;

    private String idRevenuHistorique = null;

    private String idTaxation = null;

    private Boolean isRecalcul = null;

    private Boolean isSourcier = null;

    private String nbEnfants = null;

    private String nbEnfantSuspens = null;

    private String revenuDeterminantCalcul = null;

    private String revenuImposable = null;

    private String revenuImposableSourcier = null;

    private String typeRevenu = null;

    private String typeSource = null;

    private String typeSourceLibelle = null;

    private String typeTaxation = null;

    private String typeTaxationLibelle = null;

    /**
	 * 
	 */
    public RevenuHistorique() {
    }

    /**
     * @return the anneeHistorique
     */
    public String getAnneeHistorique() {
        return anneeHistorique;
    }

    /**
     * @return the anneeTaxation
     */
    public String getAnneeTaxation() {
        return anneeTaxation;
    }

    /**
     * @return the dateAvisTaxation
     */
    public String getDateAvisTaxation() {
        return dateAvisTaxation;
    }

    /**
     * @return the etatCivil
     */
    public String getEtatCivil() {
        return etatCivil;
    }

    public String getEtatCivilLibelle() {
        return etatCivilLibelle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idRevenuHistorique;
    }

    /**
     * @return the idContribuable
     */
    public String getIdContribuable() {
        return idContribuable;
    }

    /**
     * @return the idRevenu
     */
    public String getIdRevenu() {
        return idRevenu;
    }

    /**
     * @return the idRevenuDeterminant
     */
    public String getIdRevenuDeterminant() {
        return idRevenuDeterminant;
    }

    /**
     * @return the idRevenuHistorique
     */
    public String getIdRevenuHistorique() {
        return idRevenuHistorique;
    }

    public String getIdTaxation() {
        return idTaxation;
    }

    public Boolean getIsRecalcul() {
        return isRecalcul;
    }

    /**
     * @return the isSourcier
     */
    public Boolean getIsSourcier() {
        return isSourcier;
    }

    /**
     * @return the nbEnfants
     */
    public String getNbEnfants() {
        return nbEnfants;
    }

    /**
     * @param annee
     * @return
     */
    public String getNbEnfantsPlusEnfantsSuspens() {
        return JANumberFormatter
                .formatZeroValues(
                        String.valueOf(Double.parseDouble(nbEnfants) + (Double.parseDouble(nbEnfantSuspens) / 2)),
                        false, false);
    }

    public String getNbEnfantSuspens() {
        return nbEnfantSuspens;
    }

    /**
     * @return the revenuDeterminantCalcul
     */
    public String getRevenuDeterminantCalcul() {
        return revenuDeterminantCalcul;
    }

    /**
     * 
     * @return le revenuDeterminantCalcul, formatted currency
     */
    public String getRevenuDeterminantCalculCurrency() {
        FWCurrency currentRevenuDeterminant = new FWCurrency();
        currentRevenuDeterminant.add(getRevenuDeterminantCalcul());
        return currentRevenuDeterminant.toStringFormat();
    }

    /**
     * @return the revenuImposable
     */
    public String getRevenuImposable() {
        return revenuImposable;
    }

    /**
     * 
     * @return revenu imposable (sourcier ou contribuable), currency formatted
     */
    public String getRevenuImposableCurrency() {
        FWCurrency currentRevenuImposable = new FWCurrency();
        if (isSourcier()) {
            currentRevenuImposable.add(getRevenuImposableSourcier());
        } else {
            currentRevenuImposable.add(getRevenuImposable());
        }
        return currentRevenuImposable.toStringFormat();
    }

    /**
     * @return the revenuImposableSourcier
     */
    public String getRevenuImposableSourcier() {
        return revenuImposableSourcier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return null;
    }

    /**
     * @return the typeRevenu
     */
    public String getTypeRevenu() {
        return typeRevenu;
    }

    /**
     * @return the typeSource
     */
    public String getTypeSource() {
        return typeSource;
    }

    /**
     * @return the typeSourceLibelle
     */
    public String getTypeSourceLibelle() {
        return typeSourceLibelle;
    }

    /**
     * @return the typeTaxation
     */
    public String getTypeTaxation() {
        return typeTaxation;
    }

    public String getTypeTaxationLibelle() {
        return typeTaxationLibelle;
    }

    /**
     * Use it !
     * 
     * @return
     */
    public Boolean isSourcier() {
        return SimpleRevenu.CS_TYPE_SOURCIER.equals(typeRevenu);
    }

    /**
     * @param anneeHistorique
     *            the anneeHistorique to set
     */
    public void setAnneeHistorique(String anneeHistorique) {
        this.anneeHistorique = anneeHistorique;
    }

    /**
     * @param anneeTaxation
     *            the anneeTaxation to set
     */
    public void setAnneeTaxation(String anneeTaxation) {
        this.anneeTaxation = anneeTaxation;
    }

    /**
     * @param dateAvisTaxation
     *            the dateAvisTaxation to set
     */
    public void setDateAvisTaxation(String dateAvisTaxation) {
        this.dateAvisTaxation = dateAvisTaxation;
    }

    /**
     * @param etatCivil
     *            the etatCivil to set
     */
    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    public void setEtatCivilLibelle(String etatCivilLibelle) {
        this.etatCivilLibelle = etatCivilLibelle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
    }

    /**
     * @param idContribuable
     *            the idContribuable to set
     */
    public void setIdContribuable(String idContribuable) {
        this.idContribuable = idContribuable;
    }

    /**
     * @param idRevenu
     *            the idRevenu to set
     */
    public void setIdRevenu(String idRevenu) {
        this.idRevenu = idRevenu;
    }

    /**
     * @param idRevenuDeterminant
     *            the idRevenuDeterminant to set
     */
    public void setIdRevenuDeterminant(String idRevenuDeterminant) {
        this.idRevenuDeterminant = idRevenuDeterminant;
    }

    /**
     * @param idRevenuHistorique
     *            the idRevenuHistorique to set
     */
    public void setIdRevenuHistorique(String idRevenuHistorique) {
        this.idRevenuHistorique = idRevenuHistorique;
    }

    public void setIdTaxation(String idTaxation) {
        this.idTaxation = idTaxation;
    }

    public void setIsRecalcul(Boolean isRecalcul) {
        this.isRecalcul = isRecalcul;
    }

    /**
     * @param isSourcier
     *            the isSourcier to set
     */
    public void setIsSourcier(Boolean isSourcier) {
        this.isSourcier = isSourcier;
    }

    /**
     * @param nbEnfants
     *            the nbEnfants to set
     */
    public void setNbEnfants(String nbEnfants) {
        this.nbEnfants = nbEnfants;
    }

    public void setNbEnfantSuspens(String nbEnfantSuspens) {
        this.nbEnfantSuspens = nbEnfantSuspens;
    }

    /**
     * @param revenuDeterminantCalcul
     *            the revenuDeterminantCalcul to set
     */
    public void setRevenuDeterminantCalcul(String revenuDeterminantCalcul) {
        this.revenuDeterminantCalcul = revenuDeterminantCalcul;
    }

    /**
     * @param revenuImposable
     *            the revenuImposable to set
     */
    public void setRevenuImposable(String revenuImposable) {
        this.revenuImposable = revenuImposable;
    }

    /**
     * @param revenuImposableSourcier
     *            the revenuImposableSourcier to set
     */
    public void setRevenuImposableSourcier(String revenuImposableSourcier) {
        this.revenuImposableSourcier = revenuImposableSourcier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
    }

    /**
     * @param typeRevenu
     *            the typeRevenu to set
     */
    public void setTypeRevenu(String typeRevenu) {
        this.typeRevenu = typeRevenu;
    }

    /**
     * @param typeSource
     *            the typeSource to set
     */
    public void setTypeSource(String typeSource) {
        this.typeSource = typeSource;
    }

    /**
     * @param typeSourceLibelle
     *            the typeSourceLibelle to set
     */
    public void setTypeSourceLibelle(String typeSourceLibelle) {
        this.typeSourceLibelle = typeSourceLibelle;
    }

    /**
     * @param typeTaxation
     *            the typeTaxation to set
     */
    public void setTypeTaxation(String typeTaxation) {
        this.typeTaxation = typeTaxation;
    }

    public void setTypeTaxationLibelle(String typeTaxationLibelle) {
        this.typeTaxationLibelle = typeTaxationLibelle;
    }

}
