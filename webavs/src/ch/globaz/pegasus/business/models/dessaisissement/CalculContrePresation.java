package ch.globaz.pegasus.business.models.dessaisissement;

import globaz.framework.util.FWCurrency;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;
import ch.globaz.hera.business.models.famille.MembreFamille;

public class CalculContrePresation implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal dessaisissement = null;
    private Map<MembreFamille, FWCurrency> facteurCapitalisation = null;
    private MembreFamille KeyFacteurCapitalisationPlusFavorable = null;
    private BigDecimal montantNetDuBien = null;
    private BigDecimal rendementNet = null;
    private BigDecimal rendementNetAvecFacteur = null;
    private BigDecimal totalArrondi = null;
    private String typeValeur = null;

    /**
     * @return the dessaisissement
     */
    public BigDecimal getDessaisissement() {
        return dessaisissement;
    }

    /**
     * @return the facteurCapitalisation
     */
    public Map<MembreFamille, FWCurrency> getFacteurCapitalisation() {
        return facteurCapitalisation;
    }

    /**
     * @return the keyFacteurCapitalisationPlusFavorable
     */
    public MembreFamille getKeyFacteurCapitalisationPlusFavorable() {
        return KeyFacteurCapitalisationPlusFavorable;
    }

    /**
     * @return the montantNetDuBien
     */
    public BigDecimal getMontantNetDuBien() {
        return montantNetDuBien;
    }

    /**
     * @return the rendementNet
     */
    public BigDecimal getRendementNet() {
        return rendementNet;
    }

    /**
     * @return the rendementNetAvecFacteur
     */
    public BigDecimal getRendementNetAvecFacteur() {
        return rendementNetAvecFacteur;
    }

    /**
     * @return the totalArrondi
     */
    public BigDecimal getTotalArrondi() {
        return totalArrondi;
    }

    public String getTypeValeur() {
        return typeValeur;
    }

    /**
     * @param dessaisissement
     *            the dessaisissement to set
     */
    public void setDessaisissement(BigDecimal dessaisissement) {
        this.dessaisissement = dessaisissement;
    }

    /**
     * @param facteurCapitalisation
     *            the facteurCapitalisation to set
     */
    public void setFacteurCapitalisation(Map<MembreFamille, FWCurrency> facteurCapitalisation) {
        this.facteurCapitalisation = facteurCapitalisation;
    }

    /**
     * @param keyFacteurCapitalisationPlusFavorable
     *            the keyFacteurCapitalisationPlusFavorable to set
     */
    public void setKeyFacteurCapitalisationPlusFavorable(MembreFamille keyFacteurCapitalisationPlusFavorable) {
        KeyFacteurCapitalisationPlusFavorable = keyFacteurCapitalisationPlusFavorable;
    }

    /**
     * @param montantNetDuBien
     *            the montantNetDuBien to set
     */
    public void setMontantNetDuBien(BigDecimal montantNetDuBien) {
        this.montantNetDuBien = montantNetDuBien;
    }

    /**
     * @param rendementNet
     *            the rendementNet to set
     */
    public void setRendementNet(BigDecimal rendementNet) {
        this.rendementNet = rendementNet;
    }

    /**
     * @param rendementNetAvecFacteur
     *            the rendementNetAvecFacteur to set
     */
    public void setRendementNetAvecFacteur(BigDecimal rendementNetAvecFacteur) {
        this.rendementNetAvecFacteur = rendementNetAvecFacteur;
    }

    /**
     * @param totalArrondi
     *            the totalArrondi to set
     */
    public void setTotalArrondi(BigDecimal totalArrondi) {
        this.totalArrondi = totalArrondi;
    }

    public void setTypeValeur(String typeValeur) {
        this.typeValeur = typeValeur;
    }

}
