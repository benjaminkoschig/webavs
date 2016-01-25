package ch.globaz.perseus.business.statsmensuelles;

import java.math.BigDecimal;

/**
 * Cette classe abstraite implémente la manière de compter les montants pour les statistiques.
 * 
 * @author rco
 * 
 */
public abstract class StatistiquesMensuellesMontantAbstract extends StatistiquesMensuellesRiHriAbstract {
    private BigDecimal montantRI = new BigDecimal(0);
    private BigDecimal montantHorsRI = new BigDecimal(0);

    @Override
    public void compter(StatistiquesMensuellesComptageMontantInterface statDemPcfDec) {
        super.compter(statDemPcfDec);
        if (statDemPcfDec.getDemande().getFromRI()) {
            addMontantRI(new BigDecimal(statDemPcfDec.getMontant()));
        } else {
            addMontantHorsRI(new BigDecimal(statDemPcfDec.getMontant()));
        }
    }

    /**
     * @param {@link BigDecimal} montantHorsRI
     */
    public void addMontantHorsRI(BigDecimal bg) {
        montantHorsRI = montantHorsRI.add(bg);
    }

    /**
     * @param {@link BigDecimal} montantRI
     */
    public void addMontantRI(BigDecimal bg) {
        montantRI = montantRI.add(bg);
    }

    /**
     * @return {@link BigDecimal} montantRI
     */
    public BigDecimal getMontantRI() {
        return montantRI;
    }

    /**
     * @return {@link BigDecimal} montantHorsRI
     */
    public BigDecimal getMontantHorsRI() {
        return montantHorsRI;
    }

    /**
     * @return {@link BigDecimal} montantRI
     */
    public BigDecimal getMontantTotal() {
        return montantRI.add(montantHorsRI);
    }

    /**
     * 
     * @param {@link BigDecimal} montantRI
     */
    public void setMontantRI(BigDecimal montantRI) {
        this.montantRI = this.montantRI.add(montantRI);
    }

    /**
     * 
     * @param {@link BigDecimal} montantHorsRI
     */
    public void setMontantHorsRI(BigDecimal montantHorsRI) {
        this.montantHorsRI = this.montantHorsRI.add(montantHorsRI);
    }

}
