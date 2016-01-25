/**
 * 
 */
package ch.globaz.amal.business.models.revenu;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Classe représentant les données de revenu
 * 
 * UTILISE POUR LA LISTE DES REVENUS (TAXATIONS) UNIQUEMENT
 * 
 * @author CBU
 * 
 */
public class Revenu extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeTaxation = null;
    private String dateAvisTaxation = null;
    private String etatCivil = null;
    private String fortuneImposable = null;
    private String idContribuable = null;
    private String idRevenu = null;
    private String idRevenuHistorique = null;
    private Boolean isSourcier = false;
    private String nbEnfants = null;
    private String nbEnfantSuspens = null;
    private String persChargeEnf = null;
    private String revDetUnique = null;
    private Boolean revDetUniqueOuiNon = true;
    private String revenuImposable = null;
    private String revenuImposableSourcier = null;
    private String typeRevenu = null;
    private String typeSource = null;
    private String typeTaxation = null;

    /**
	 * 
	 */
    public Revenu() {
        super();
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
    };

    /**
     * @return the etatCivil
     */
    public String getEtatCivil() {
        return etatCivil;
    }

    /**
     * @return the fortuneImposable
     */
    public String getFortuneImposable() {
        return fortuneImposable;
    }

    /**
     * 
     * @return fortune imposable , currency formatted
     */
    public String getFortuneImposableCurrency() {
        FWCurrency currentFortuneImposable = new FWCurrency();
        currentFortuneImposable.add(getFortuneImposable());
        return currentFortuneImposable.toStringFormat();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idRevenu;
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
     * @return the idRevenuHistorique
     */
    public String getIdRevenuHistorique() {
        return idRevenuHistorique;
    }

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

    public String getPersChargeEnf() {
        return persChargeEnf;
    }

    public String getRevDetUnique() {
        return revDetUnique;
    }

    public Boolean getRevDetUniqueOuiNon() {
        return revDetUniqueOuiNon;
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

        char arrondiTo = JANumberFormatter.INF;
        if (currentRevenuImposable.isNegative()) {
            arrondiTo = JANumberFormatter.SUP;

        }
        return JANumberFormatter.fmt(JANumberFormatter.round(currentRevenuImposable.toString(), 100, 2, arrondiTo),
                true, true, false, 2);
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
     * @return the typeTaxation
     */
    public String getTypeTaxation() {
        return typeTaxation;
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

    /**
     * @param fortuneImposable
     *            the fortuneImposable to set
     */
    public void setFortuneImposable(String fortuneImposable) {
        this.fortuneImposable = fortuneImposable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idRevenu = id;
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
     * @param idRevenuHistorique
     *            the idRevenuHistorique to set
     */
    public void setIdRevenuHistorique(String idRevenuHistorique) {
        this.idRevenuHistorique = idRevenuHistorique;
    }

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

    public void setPersChargeEnf(String persChargeEnf) {
        this.persChargeEnf = persChargeEnf;
    }

    public void setRevDetUnique(String revDetUnique) {
        this.revDetUnique = revDetUnique;
    }

    public void setRevDetUniqueOuiNon(Boolean revDetUniqueOuiNon) {
        this.revDetUniqueOuiNon = revDetUniqueOuiNon;
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

    /**
     * @param simpleRevenu
     */
    // public void setSimpleRevenu(SimpleRevenu simpleRevenu) {
    // this.simpleRevenu = simpleRevenu;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        // this.simpleRevenu.setSpy(spy);
    }

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
     * @param typeTaxation
     *            the typeTaxation to set
     */
    public void setTypeTaxation(String typeTaxation) {
        this.typeTaxation = typeTaxation;
    }

}