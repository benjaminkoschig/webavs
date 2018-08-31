package ch.globaz.vulpecula.domain.models.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import ch.globaz.utils.TypesValidator;

/**
 * ValueObject d�finissant le taux, soit un chiffre entier entre -1000 et 1000.
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 10 d�c. 2013
 */
public class Taux implements ValueObject, Comparable<Taux> {
    private final BigDecimal taux;

    public static Taux ZERO() {
        return new Taux(0);
    }

    /**
     * Un taux d'occupation doit �tre compris entre -1000 et 1000
     * 
     * @param taux
     *            Valeur enti�re entre -1000 et 1000
     */
    public Taux(final int taux) {
        if (!isValid(taux)) {
            throwTauxNotInRangeException(taux);
        }
        this.taux = new BigDecimal(taux);
    }

    /**
     * Un taux d'occupation doit �tre compris entre -1000 et 1000
     * 
     * @param taux
     *            Valeur enti�re entre -1000 et 1000j
     */
    public Taux(final double taux) {
        if (!isValid(taux)) {
            throwTauxNotInRangeException(taux);
        }
        this.taux = new BigDecimal(String.valueOf(taux));
    }

    public Taux(BigDecimal taux) {
        this(taux.doubleValue());
    }

    /**
     * Retourne le taux actual additionn� au taux pass�e en param�tre
     * 
     * @param taux Taux � ajouter
     */
    public Taux addTaux(final Taux taux) {
        BigDecimal sum = getBigDecimal();
        if (taux != null) {
            sum = getBigDecimal().add(taux.getBigDecimal());
        }
        return new Taux(sum);
    }

    public Taux(final String taux) {
        if (!TypesValidator.isNumeric(taux)) {
            throw new IllegalArgumentException("Le taux n'est pas de type num�rique");
        }
        double tauxDouble = Double.parseDouble(taux);
        if (!isValid(tauxDouble)) {
            throwTauxNotInRangeException(tauxDouble);
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
     * @return String repr�sentant le taux
     */
    public String getValue() {
        return getValueWith(5);
    }

    /**
     * Retourne le taux avec le nombre de d�cimales pass� en param�tre selon le formater pr�d�finit "###,##0.00###".
     * 
     * @param nombre Nombre de d�cimales
     * @return String repr�sentant le taux avec un certain nombre de d�cimales.
     */
    public String getValueWith(int nombre) {
        DecimalFormat df = new DecimalFormat("###,##0.00###");
        return df.format(taux.setScale(nombre, RoundingMode.HALF_UP).doubleValue());
    }

    public String getValueWith(long nombre) {
        return getValueWith((int) nombre);
    }

    public String getValueWith(Long nombre) {
        return getValueWith(nombre.intValue());
    }

    public BigDecimal getBigDecimal() {
        return taux;
    }

    private boolean isValid(final double taux) {
        return (taux >= -1000) && (taux <= 1000);
    }

    /**
     * Retourne si le taux est plus grand que la valeur pass�e en param�tre.
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

    private void throwTauxNotInRangeException(double taux) {
        throw new IllegalArgumentException("Le taux " + taux + " doit �tre un nombre compris entre 0-1000");
    }

    @Override
    public int compareTo(final Taux o) {
        if (o == null) {
            return -1;
        } else {
            return getBigDecimal().compareTo(o.getBigDecimal());
        }
    }

    /**
     * Retourne si le taux est plus grand que la valeur pass�e en param�tre.
     * 
     * @param value Valeur � comparer
     * @return true si la valeur actuelle est plus grande que la valeur en param�tre
     */
    public boolean greaterThan(int value) {
        return taux.compareTo(new BigDecimal(value)) > 0;
    }

    /**
     * Retourne si le taux est plus grand que la valeur pass�e en param�tre.
     * 
     * @param valeur Valeur � comparer
     * @return true si la valeur actuelle est plus petite que la valeur en param�tre
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

    public double doubleValue() {
        return taux.doubleValue();
    }
}
