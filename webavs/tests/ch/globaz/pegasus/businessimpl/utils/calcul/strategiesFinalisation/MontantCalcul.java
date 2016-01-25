package ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation;

import java.math.BigDecimal;

public class MontantCalcul {

    private BigDecimal montant;

    public MontantCalcul(float f) {
        montant = new BigDecimal(f).setScale(2, BigDecimal.ROUND_DOWN).setScale(0, BigDecimal.ROUND_CEILING);

    }

    public MontantCalcul(String s) {
        this(Float.parseFloat(s));
    }

    public BigDecimal getMontant() {
        return montant;
    }
}
