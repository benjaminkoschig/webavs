package globaz.corvus.module.calcul.api;

import java.math.BigDecimal;

public class REMontantReferenceAPI {

    private int annee;
    private BigDecimal renteMaximale = null;
    private BigDecimal renteMinimale = null;

    public REMontantReferenceAPI(int annee, BigDecimal renteMin, BigDecimal renteMax) {
        setAnnee(annee);
        setRenteMinimale(renteMin);
        setRenteMaximale(renteMax);
    }

    /**
     * @return the annee
     */
    public int getAnnee() {
        return annee;
    }

    /**
     * @return the renteMaximale
     */
    public BigDecimal getRenteMaximale() {
        return new BigDecimal(renteMaximale.toString());
    }

    /**
     * @return the renteMinimale
     */
    public BigDecimal getRenteMinimale() {
        return new BigDecimal(renteMinimale.toString());
    }

    /**
     * @param annee
     *            the annee to set
     */
    public void setAnnee(int annee) {
        this.annee = annee;
    }

    /**
     * @param renteMaximale
     *            the renteMaximale to set
     */
    public void setRenteMaximale(BigDecimal renteMaximale) {
        this.renteMaximale = new BigDecimal(renteMaximale.toString());
    }

    /**
     * @param renteMinimale
     *            the renteMinimale to set
     */
    public void setRenteMinimale(BigDecimal renteMinimale) {
        this.renteMinimale = new BigDecimal(renteMinimale.toString());
    }

}
