package ch.globaz.common.domaine;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Part {

    private BigDecimal numerateur;
    private BigDecimal denominateur;

    public Part(final BigDecimal numerateur, final BigDecimal denominateur) {
        this.numerateur = numerateur;
        this.denominateur = denominateur;
    }

    public Part(final int numerateur, final int denominateur) {
        this.numerateur = new BigDecimal(numerateur);
        this.denominateur = new BigDecimal(denominateur);
    }

    public Part(final String numerateur, final String denominateur) {
        if (numerateur == null) {
            this.numerateur = BigDecimal.ZERO;
        } else {
            this.numerateur = new BigDecimal(numerateur);
        }
        if (numerateur == null) {
            this.denominateur = BigDecimal.ZERO;
        } else {
            this.denominateur = new BigDecimal(denominateur);
        }
    }

    public BigDecimal resultat() {
        if (denominateur.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        } else {
            return numerateur.divide(denominateur, 4, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    }

    public BigDecimal getNumerateur() {
        return numerateur;
    }

    public BigDecimal getDenominateur() {
        return denominateur;
    }

    @Override
    public String toString() {
        return "Part [numerateur=" + numerateur + ", denominateur=" + denominateur + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((denominateur == null) ? 0 : denominateur.hashCode());
        result = prime * result + ((numerateur == null) ? 0 : numerateur.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Part other = (Part) obj;
        if (denominateur == null) {
            if (other.denominateur != null) {
                return false;
            }
        } else if (!denominateur.equals(other.denominateur)) {
            return false;
        }
        if (numerateur == null) {
            if (other.numerateur != null) {
                return false;
            }
        } else if (!numerateur.equals(other.numerateur)) {
            return false;
        }
        return true;
    }

}
