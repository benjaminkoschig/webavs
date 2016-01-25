package ch.globaz.common.domaine;

import java.math.BigDecimal;

public class Pourcentage {

    public static final Pourcentage CENT_POURCENT = new Pourcentage(100);
    public static final Pourcentage CINQUANTE_POURCENT = new Pourcentage(50);
    public static final Pourcentage ZERO_POURCENT = new Pourcentage(0);

    private double pourcentage;

    public Pourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public double doubleValue() {
        return pourcentage;
    }

    public BigDecimal multiplier(BigDecimal montant) {
        return new BigDecimal(this.multiplier(montant.doubleValue()));
    }

    public double multiplier(double valeurDouble) {
        return ((valeurDouble * pourcentage) / 100.0);
    }

    public int multiplier(int valeurEntiere) {
        return ((valeurEntiere * (int) pourcentage) / 100);
    }

    @Override
    public String toString() {
        return pourcentage + "%";
    }
}
