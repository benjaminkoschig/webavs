package globaz.cygnus.utils;

import java.math.BigDecimal;

public class RFMyBigDecimal {

    BigDecimal valeur = new BigDecimal("0");

    public RFMyBigDecimal(BigDecimal valeur) {
        super();
        this.valeur = valeur;
    }

    synchronized public BigDecimal getValeur() {
        return valeur;
    }

    synchronized public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

}
