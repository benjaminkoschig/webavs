package ch.globaz.common.domaine;

import java.math.BigDecimal;

/**
 * ValueObject définissant le taux, soit un chiffre entier entre 0 et 100.
 * 
 * @author Arnaud Geiser (AGE) | Créé le 10 déc. 2013
 */
public class Taux {
    private final BigDecimal taux;

    public static Taux ZERO() {
        return new Taux(0);
    }

    /**
     * Un taux d'occupation doit être compris entre 0 et 100 (pourcent)
     * 
     * @param taux
     *            Valeur entière entre 0 et 100
     */
    public Taux(final int taux) {
        if (!isValid(taux)) {
            throwTauxNotInRangeException();
        }
        this.taux = new BigDecimal(taux);
    }

    /**
     * Un taux d'occupation doit être compris entre 0 et 100 (pourcent)
     * 
     * @param taux
     *            Valeur entière entre 0 et 100
     */
    public Taux(final double taux) {
        if (!isValid(taux)) {
            throwTauxNotInRangeException();
        }
        this.taux = new BigDecimal(String.valueOf(taux));
    }

    public Taux(BigDecimal taux) {
        this(taux.doubleValue());
    }

    /**
     * Retourne le taux actual additionné au taux passée en paramètre
     * 
     * @param taux Taux à ajouter
     */
    public Taux addTaux(final Taux taux) {
        BigDecimal sum = getBigDecimal().add(taux.getBigDecimal());
        return new Taux(sum);
    }

    public Taux(final String taux) {

        double tauxDouble = Double.parseDouble(taux);
        if (!isValid(tauxDouble)) {
            throwTauxNotInRangeException();
        }
        this.taux = new BigDecimal(taux);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Taux) {
            Taux taux = (Taux) obj;
            if (taux.getValue().equals(getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    /**
     * Retourne le taux
     * 
     * @return String représentant le taux
     */
    public String getValue() {
        return getValueWith(2);
    }

    /**
     * Retourne le taux avec le nombre de décimales passé en paramètre.
     * 
     * @param nombre Nombre de décimales
     * @return String représentant le taux avec un certain nombre de décimales.
     */
    public String getValueWith(int nombre) {
        return taux.setScale(nombre).toPlainString();
    }

    public String getValueWith(long nombre) {
        return taux.setScale((int) nombre).toPlainString();
    }

    public String getValueWith(Long nombre) {
        return taux.setScale(nombre.intValue()).toPlainString();
    }

    public BigDecimal getBigDecimal() {
        return taux;
    }

    private boolean isValid(final double taux) {
        return (taux >= 0) && (taux <= 1000);
    }

    /**
     * Retourne si le taux est plus grand que la valeur passée en paramètre.
     * 
     * @param nombre Nombre sous condition
     * @return boolean si taux est plus grand que le nombre
     */
    public boolean isZero() {
        return taux.signum() == 0;
    }

    @Override
    public String toString() {
        return String.valueOf(taux);
    }

    private void throwTauxNotInRangeException() {
        throw new IllegalArgumentException("Le taux doit être un nombre compris entre 0-1000");
    }

    public int compareTo(final Taux o) {
        if (o == null) {
            return -1;
        } else {
            return getBigDecimal().compareTo(o.getBigDecimal());
        }
    }

    /**
     * Retourne si le taux est plus grand que la valeur passée en paramètre.
     * 
     * @param value Valeur à comparer
     * @return true si la valeur actuelle est plus grande que la valeur en paramètre
     */
    public boolean greaterThan(int value) {
        return taux.compareTo(new BigDecimal(value)) > 0;
    }

    /**
     * Retourne si le taux est plus grand que la valeur passée en paramètre.
     * 
     * @param valeur Valeur à comparer
     * @return true si la valeur actuelle est plus petite que la valeur en paramètre
     */
    public boolean lessThan(int value) {
        return taux.compareTo(new BigDecimal(value)) < 0;
    }

    public static boolean isValid(String value) {
        try {
            new Taux(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
