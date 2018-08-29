package ch.globaz.vulpecula.domain.models.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import ch.globaz.utils.TypesValidator;

/***
 * ValueObject repr�sentant un nombre � virgule
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 17 d�c. 2013
 */
public class Montant implements ValueObject, Comparable<Montant> {
    private static final long serialVersionUID = 1L;

    private BigDecimal currency = BigDecimal.ZERO;
    public final static Montant ZERO = new Montant("0");

    public static Montant valueOf(String value) {
        return new Montant(value);
    }

    public static Montant valueOf(int value) {
        return new Montant(value);
    }

    public static Montant valueOf(double value) {
        return new Montant(value);
    }

    public static Montant valueOf(BigDecimal value) {
        return new Montant(value);
    }

    public Montant(final int value) {
        currency = new BigDecimal(value);
    }

    public Montant(final BigDecimal value) {
        currency = value;
    }

    public Montant(final double value) {
        currency = new BigDecimal(String.valueOf(value));
    }

    /**
     * @deprecated : Utiliser <code>Montant.ZERO</code>
     * @return
     */
    @Deprecated
    public static Montant ZERO() {
        return ZERO;
    }

    public Montant(final String value) {
        String newValue = null;
        if (value.indexOf('\'') != -1) {
            newValue = value.replaceAll("\'", "");
        } else {
            newValue = value;
        }

        if (!TypesValidator.isNumeric(newValue)) {
            throw new IllegalArgumentException("Le montant doit �tre un double");
        }
        currency = new BigDecimal(newValue);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Montant other = (Montant) obj;
        if (currency == null) {
            if (other.currency != null) {
                return false;
            }
        } else if (currency.compareTo(other.currency) != 0) {
            return false;
        }
        return true;
    }

    /**
     * Additionne deux montants.
     * 
     * @param montant Montant � additionner
     * @return Nouveau montant contenant l'addition des deux montants
     */
    public Montant add(final Montant montant) {
        return new Montant(currency.add(montant.getBigDecimalValue()));
    }

    public Montant add(final double montant) {
        return new Montant(currency.add(new BigDecimal(montant)));
    }

    /**
     * Soustrait le montant pass� en param�tre au montant actuel.
     * 
     * @param montantToSubstract Montant � soustraire
     * @return Nouveau montant contenant la soustraction des deux montant
     */
    public Montant substract(Montant montantToSubstract) {
        return new Montant(currency.subtract(montantToSubstract.getBigDecimalValue()));
    }

    /**
     * Retourne la valeur du montant
     * 
     * @return double repr�sentant le montant
     */
    public String getValue() {
        return currency.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    /**
     * Retourne la valeur du montant pour affichage
     * 
     * @return une repr�sentation String du montant
     */
    public String toStringValue() {
        return String.valueOf(getValue());
    }

    /**
     * Retourne la valeur n�gative du montant actuel.
     * 
     * @return Montant n�gatif de la valeur actuelle
     */
    public Montant negate() {
        return new Montant(currency.negate());
    }

    public Montant absolute() {
        return new Montant(currency.abs());
    }

    public BigDecimal getNegativeValue() {
        return getBigDecimalValue().negate();
    }

    /**
     * @return java.math.BigDecimal
     */
    public BigDecimal getBigDecimalValue() {
        return currency;
    }

    /**
     * @return La valeur en chaine de caract�res
     */
    @Override
    public String toString() {
        return currency.toString();
    }

    /**
     * 
     * @return un String selon le format "##,###,###.##"
     */
    public String toStringFormat() {
        String pattern = "###,###.##";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRANCE);
        symbols.setGroupingSeparator('\'');
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        return decimalFormat.format(currency);
    }

    /**
     * 
     * @return un String selon le format "##,###,###.00"
     */
    public String toStringFormatTwoDecimales() {
        String pattern = "###,##0.00";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRANCE);
        symbols.setGroupingSeparator('\'');
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        return decimalFormat.format(getBigDecimalNormalisee());
    }

    public Montant multiply(final int valueToMultiply) {
        return new Montant(currency.multiply(new BigDecimal(valueToMultiply)));
    }

    public Montant multiply(final double valueToMultiply) {
        return new Montant(currency.multiply(new BigDecimal(valueToMultiply)));
    }

    public Montant multiply(final Montant montant) {
        return new Montant(currency.multiply(montant.currency));
    }

    public Montant multiply(final BigDecimal value) {
        return new Montant(currency.multiply(value));
    }

    public Montant divide(Montant montant) {
        return new Montant(currency.divide(montant.getBigDecimalValue(), 2, RoundingMode.HALF_UP));
    }

    public Montant divide(int valueToDivide) {
        return new Montant(currency.divide(new BigDecimal(valueToDivide), 2, RoundingMode.HALF_UP));
    }

    public Montant divide(double valueToDivide) {
        return divide(new Montant(valueToDivide));
    }

    public Montant divideBy100() {
        return divide(100);
    }

    public Montant decimal(int i) {
        return new Montant(currency.setScale(i, RoundingMode.HALF_EVEN));
    }

    /**
     * Multiplie le montant par rapport � un taux.
     * Le taux est exprim� est pourcent (p.ex : Un taux de 50 correspondra � 50% soit une division par 2)
     * 
     * @param taux Le taux auquel la multiplication sera effectu�e
     * @return Montant repr�sentant le r�sultat de la multiplication
     */
    public Montant multiply(Taux taux) {
        BigDecimal valueOfTaux = taux.getBigDecimal().divide(new BigDecimal(100));
        return multiply(valueOfTaux);
    }

    /**
     * @return true if value is negative
     */
    public boolean isNegative() {
        return currency.signum() == -1;
    }

    /**
     * @return true if value is positive
     */
    public boolean isPositive() {
        return currency.signum() == 1;
    }

    /**
     * @return true if value is zero
     */
    public boolean isZero() {
        return currency.signum() == 0;
    }

    /**
     * @return les centimes sans le montant
     */
    public int getCentimes() {
        return getBigDecimalValue().subtract(new BigDecimal(getMontantSansCentimes())).multiply(new BigDecimal("100"))
                .intValue();
    }

    /**
     * @return les centimes sans le montant toujours sur deux positions
     */
    public String getCentimesWith0() {
        return (getCentimes() < 10 ? "0" : "") + getCentimes();
    }

    /**
     * Retourne la valeur normalis�e arrondie au 5 centimes.
     * 
     * @return La valeur arrondie aux 5 centimes sup�rieures dans le cas o� la d�cimal est 3 ou 4, la valeur arrondi au
     *         5
     *         centimes inf�rieures dans le cas o� la d�cimal est 1 ou 2
     */
    public String getValueNormalisee() {
        return getBigDecimalNormalisee().toPlainString();
    }

    /**
     * Retourne le montant normalis�e au 5 centimes.
     * 
     * @return La valeur arrondie aux 5 centimes sup�rieures dans le cas o� la d�cimal est 3 ou 4 ou 2.5, la valeur
     *         arrondi au
     *         5 inf�rieures dans le cas o� la d�cimal est 1 ou 2
     */
    public Montant normalize() {
        return new Montant(getBigDecimalNormalisee());
    }

    private BigDecimal getBigDecimalNormalisee() {
        BigDecimal increment = new BigDecimal("0.05");

        BigDecimal valueNormalisee = new BigDecimal(Double.toString(currency.doubleValue()));
        valueNormalisee = valueNormalisee.divide(increment, 0, BigDecimal.ROUND_HALF_UP);
        valueNormalisee = valueNormalisee.multiply(increment);
        return valueNormalisee.setScale(2);
    }

    public BigDecimal getBigDecimalArrondi() {
        BigDecimal increment = new BigDecimal("0.01");

        BigDecimal valueNormalisee = new BigDecimal(Double.toString(currency.doubleValue()));
        valueNormalisee = valueNormalisee.divide(increment, 0, BigDecimal.ROUND_UP);
        valueNormalisee = valueNormalisee.multiply(increment);
        return valueNormalisee.setScale(2);
    }

    /**
     * @return le montant sans les centimes
     */
    public int getMontantSansCentimes() {
        return getBigDecimalValue().intValue();
    }

    /**
     * Retourne si le montant pass� en param�tre est plus grand ou �gal au montant actuel.
     * 
     * @param montant � comparer
     * @return true si plus grand ou �gal, false si plus petit
     */
    public boolean greaterOrEquals(Montant montant) {
        Montant montantCompare = substract(montant);
        return montantCompare.isPositive() || montantCompare.isZero();
    }

    /**
     * Retourne si le montant pass� en param�tre est plus grand au montant actuel.
     * 
     * @param montant � comparer
     * @return true si plus grand, false si plus petit ou �gal
     */
    public boolean greater(Montant montant) {
        Montant montantCompare = substract(montant);
        return montantCompare.isPositive();
    }

    /**
     * Retourne false si le montant pass� en param�tre est plus petit ou �gal au montant actuel.
     * 
     * @param montant � comparer
     * @return true si plus grand, false si plus petit ou �gal
     */
    public boolean less(Montant montant) {
        Montant montantCompare = substract(montant);
        return montantCompare.isNegative();
    }

    /**
     * @return le montant absolu
     */
    public Montant getMontantAbsolu() {
        return new Montant(getBigDecimalValue().abs());
    }

    public double doubleValue() {
        return currency.doubleValue();
    }

    public int intValue() {
        return currency.intValue();
    }

    public static boolean isValid(String value) {
        try {
            new Montant(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    @Override
    public int compareTo(Montant o) {
        return getBigDecimalValue().compareTo(o.getBigDecimalValue());
    }
}
