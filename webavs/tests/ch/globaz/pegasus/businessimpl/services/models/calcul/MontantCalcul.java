package ch.globaz.pegasus.businessimpl.services.models.calcul;

import java.math.BigDecimal;

public class MontantCalcul {

    private final static int ARRONDIS = BigDecimal.ROUND_CEILING;
    private final static int DECIMAL = 2;

    public static MontantCalcul fromFloat(float floatValue) {
        MontantCalcul mc = new MontantCalcul();
        mc.value = new BigDecimal(floatValue).setScale(MontantCalcul.DECIMAL, MontantCalcul.ARRONDIS);
        return mc;
    }

    public static MontantCalcul fromString(String stringValue) {
        if (stringValue == null) {
            throw new NullPointerException("The value passed must not be null");
        }

        MontantCalcul mc = new MontantCalcul();
        mc.value = new BigDecimal(stringValue).setScale(MontantCalcul.DECIMAL, MontantCalcul.ARRONDIS);
        return mc;
    };

    private BigDecimal value;

    private MontantCalcul() {
    }

}
