package ch.globaz.common.domaine;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/***
 * ValueObject représentant un nombre à virgule
 * 
 * @author Arnaud Geiser (AGE) | Créé le 17 déc. 2013
 */
/**
 * @author dma
 * 
 */
public class Montant {
    private static final long serialVersionUID = 1L;

    private BigDecimal currency = BigDecimal.ZERO;

    public final static Montant ZERO = new Montant(BigDecimal.ZERO, MontantTypePeriode.SANS_PERIODE);
    public final static Montant ZERO_ANNUEL = new Montant(BigDecimal.ZERO, MontantTypePeriode.ANNUEL);

    private final static MathContext mathContext = new MathContext(16, RoundingMode.HALF_UP);
    private final static BigDecimal menuslisationJour = new BigDecimal(21.7, mathContext);

    private MontantTypePeriode typePeriode = MontantTypePeriode.SANS_PERIODE;

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

    private Montant(final BigDecimal montant, final MontantTypePeriode typePeriode) {
        currency = montant;
        this.typePeriode = typePeriode;
    }

    public Montant(final double montant, final MontantTypePeriode typePeriode) {
        currency = new BigDecimal(montant, mathContext);
        this.typePeriode = typePeriode;
    }

    private Montant(final String montant, final MontantTypePeriode typePeriode) {
        currency = new BigDecimal(montant, mathContext);
        this.typePeriode = typePeriode;
    }

    public Montant(final int value) {
        currency = new BigDecimal(value, mathContext);
    }

    public Montant(final BigDecimal value) {
        currency = value;
    }

    public Montant(final double value) {
        currency = new BigDecimal(value, mathContext);
    }

    public Montant(final String value) {
        String newValue = null;
        if (value.indexOf('\'') != -1) {
            newValue = value.replaceAll("\'", "");
        } else {
            newValue = value;
        }

        if (Double.valueOf(value) == null) {
            throw new IllegalArgumentException("Le montant doit être un double");
        }
        currency = new BigDecimal(newValue, mathContext);
    }

    Montant(Montant montant) {
        currency = montant.currency;
        typePeriode = montant.typePeriode;
    }

    public static Montant newAnnuel(String montant) {
        return new Montant(montant, MontantTypePeriode.ANNUEL);
    }

    public static Montant newMensuel(String montant) {
        return new Montant(montant, MontantTypePeriode.MENSUEL);
    }

    public static Montant newJouranlier(String montant) {
        return new Montant(montant, MontantTypePeriode.JOURNALIER);
    }

    public static Montant newAnnuel(double montant) {
        return new Montant(montant, MontantTypePeriode.ANNUEL);
    }

    public static Montant newMensuel(double montant) {
        return new Montant(montant, MontantTypePeriode.MENSUEL);
    }

    public static Montant newJouranlier(double montant) {
        return new Montant(montant, MontantTypePeriode.JOURNALIER);
    }

    protected void checkPeriodicityMutable() {
        if (!typePeriode.isSansPeriode()) {
            throw new IllegalStateException(
                    "Impossible de changer la périodicité, lorsqu'elle est déja définit ! typePeriode=" + typePeriode);
        }
    }

    public Montant addAnnuelPeriodicity() {
        return new Montant(currency, MontantTypePeriode.ANNUEL);
    }

    public Montant addMensuelPeriodicity() {
        return new Montant(currency, MontantTypePeriode.MENSUEL);
    }

    public Montant addJournalierPeriodicity() {
        return new Montant(currency, MontantTypePeriode.JOURNALIER);
    }

    public boolean isMensuel() {
        return typePeriode.isMensuel();
    }

    public boolean isAnnuel() {
        return typePeriode.isAnnuel();
    }

    public boolean isJouranlier() {
        return typePeriode.isJournalier();
    }

    public boolean isSansPeriode() {
        return typePeriode.isSansPeriode();
    }

    public Montant annualise() {
        if (isJouranlier()) {
            return new Montant(currency.multiply(menuslisationJour).multiply(new BigDecimal(12)),
                    MontantTypePeriode.ANNUEL);
        } else if (isMensuel()) {
            return new Montant(currency.multiply(new BigDecimal(12)), MontantTypePeriode.ANNUEL);
        } else if (isAnnuel()) {
            return new Montant(currency, typePeriode);
        } else {
            throw new IllegalStateException("Imposible d'anulaiser le montant. Le type de période est non définit");
        }
    }

    /**
     * Additionne deux montants.
     * 
     * @param montant Montant à additionner
     * @return Nouveau montant contenant l'addition des deux montants
     */
    public Montant add(final Montant montant) {
        return new Montant(currency.add(montant.getBigDecimalValue()), resolveTypePeriode(montant));
    }

    private MontantTypePeriode resolveTypePeriode(final Montant montant) {
        MontantTypePeriode type = typePeriode;
        if (typePeriode.isSansPeriode()) {
            type = montant.typePeriode;
        }
        return type;
    }

    public Montant add(final double montant) {
        return new Montant(currency.add(new BigDecimal(montant)));
    }

    public Montant max(BigDecimal montant) {
        return new Montant(currency.max(montant), typePeriode);
    }

    public Montant max(Montant montant) {
        return new Montant(currency.max(montant.getCurrency()), resolveTypePeriode(montant));
    }

    /**
     * Soustrait le montant passé en paramètre au montant actuel.
     * 
     * @param montantToSubstract Montant à soustraire
     * @return Nouveau montant contenant la soustraction des deux montant
     */
    public Montant substract(Montant montantToSubstract) {
        return new Montant(currency.subtract(montantToSubstract.getBigDecimalValue()),
                resolveTypePeriode(montantToSubstract));
    }

    /**
     * Retourne la valeur du montant
     * 
     * @return double représentant le montant
     */
    public String getValue() {
        return currency.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    /**
     * Retourne la valeur du montant
     * 
     * @return double représentant le montant
     */
    public Double getValueDouble() {
        return currency.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Retourne la valeur du montant pour affichage
     * 
     * @return une représentation String du montant
     */
    public String toStringValue() {
        return String.valueOf(getValue());
    }

    /**
     * Retourne la valeur négative du montant actuel.
     * 
     * @return Montant négatif de la valeur actuelle
     */
    public Montant negate() {
        return new Montant(currency.negate());
    }

    public BigDecimal getNegativeValue() {
        return getBigDecimalValue().negate();
    }

    /**
     * @return java.math.BigDecimal
     */
    public BigDecimal getBigDecimalValue() {
        return currency.stripTrailingZeros();
    }

    /**
     * @return La valeur en chaine de caractères
     */
    @Override
    public String toString() {
        return "Montant [currency=" + currency + ", typePeriode=" + typePeriode + "]";
    }

    /**
     * 
     * @return un String selon le format "##,###,##0.00"
     */
    public String toStringFormat() {
        String pattern = "###,##0.00";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRANCE);
        symbols.setGroupingSeparator('\'');
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        return decimalFormat.format(currency.setScale(2, RoundingMode.HALF_UP));
    }

    public Montant multiply(final int valueToMultiply) {
        return new Montant(currency.multiply(new BigDecimal(valueToMultiply)), typePeriode);
    }

    public Montant multiply(final double valueToMultiply) {
        return new Montant(currency.multiply(new BigDecimal(valueToMultiply)), typePeriode);
    }

    public Montant multiply(final Montant montant) {
        return new Montant(currency.multiply(montant.currency), resolveTypePeriode(montant));
    }

    public Montant multiply(final BigDecimal value) {
        return new Montant(currency.multiply(value), typePeriode);
    }

    public Montant multiply(final Part part) {
        return new Montant(currency.multiply(part.resultat()), typePeriode);
    }

    public Montant divide(Montant montant) {
        return new Montant(currency.divide(montant.getBigDecimalValue(), 2, RoundingMode.HALF_UP),
                resolveTypePeriode(montant));
    }

    public Montant divide(int valueToDivide) {
        return new Montant(currency.divide(new BigDecimal(valueToDivide, mathContext), 2, RoundingMode.HALF_UP),
                typePeriode);
    }

    public Montant divide(double valueToDivide) {
        return divide(new Montant(valueToDivide, typePeriode));
    }

    public Montant divideBy100() {
        return divide(100);
    }

    public Montant decimal(int i) {
        return new Montant(currency.setScale(i, RoundingMode.HALF_EVEN), typePeriode);
    }

    /**
     * Multiplie le montant par rapport à un taux.
     * Le taux est exprimé est pourcent (p.ex : Un taux de 50 correspondra à 50% soit une division par 2)
     * 
     * @param taux Le taux auquel la multiplication sera effectuée
     * @return Montant représentant le résultat de la multiplication
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
     * Retourne la valeur normalisée arrondie au 5 centimes.
     * 
     * @return La valeur arrondie aux 5 centimes supérieures dans le cas où la décimal est 3 ou 4, la valeur arrondi au
     *         5
     *         centimes inférieures dans le cas où la décimal est 1 ou 2
     */
    public String getValueNormalisee() {
        return getBigDecimalNormalisee().toPlainString();
    }

    /**
     * Retourne le montant normalisée au 5 centimes.
     * 
     * @return La valeur arrondie aux 5 centimes supérieures dans le cas où la décimal est 3 ou 4, la valeur arrondi au
     *         5 inférieures dans le cas où la décimal est 1 ou 2
     */
    public Montant normalize() {
        return new Montant(getBigDecimalNormalisee());
    }

    /**
     * Détermine si le montant a été normalisé.
     * Regarde si on à un centime qui est égale à 5 ou 0;
     */
    public boolean isNormalized() {
        BigDecimal increment = new BigDecimal("0.05");
        BigDecimal decimal = currency.abs().setScale(2, BigDecimal.ROUND_FLOOR).remainder(BigDecimal.ONE);
        if (decimal.compareTo(BigDecimal.ZERO) == 0 || decimal.compareTo(increment) == 0) {
            return true;
        }
        return false;
    }

    private BigDecimal getBigDecimalNormalisee() {
        BigDecimal increment = new BigDecimal("0.05");

        BigDecimal valueNormalisee = new BigDecimal(Double.toString(currency.doubleValue()));
        valueNormalisee = valueNormalisee.divide(increment, 0, BigDecimal.ROUND_HALF_EVEN);
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
     * Arrondi le montant a un entier supérieur:
     * 100.12 -> 100.00
     * 100.44 -> 100.00
     * 100.50 -> 101.00
     * 100.75 -> 101.00
     * 
     * @return
     */
    public Montant arrondiAUnIntier() {
        return new Montant(getBigDecimalArrondiAUnIntier());
    }

    private BigDecimal getBigDecimalArrondiAUnIntier() {
        return currency.setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * @return le montant sans les centimes
     */
    public int getMontantSansCentimes() {
        return getBigDecimalValue().intValue();
    }

    /**
     * Retourne si le montant passé en paramètre est plus grand ou égal au montant actuel.
     * 
     * @param montant à comparer
     * @return true si plus grand ou égal, false si plus petit
     */
    public boolean greaterOrEquals(Montant montant) {
        Montant montantCompare = substract(montant);
        return montantCompare.isPositive() || montantCompare.isZero();
    }

    /**
     * Retourne si le montant passé en paramètre est plus grand au montant actuel.
     * 
     * @param montant à comparer
     * @return true si plus grand, false si plus petit ou égal
     */
    public boolean greater(Montant montant) {
        Montant montantCompare = substract(montant);
        return montantCompare.isPositive();
    }

    /**
     * Retourne si le montant passé en paramètre est plus petit au montant actuel.
     * 
     * @param montant à comparer
     * @return true si plus grand, false si plus petit ou égal
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

    BigDecimal getCurrency() {
        return currency;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currency == null) ? 0 : currency.hashCode());
        result = prime * result + ((typePeriode == null) ? 0 : typePeriode.hashCode());
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
        Montant other = (Montant) obj;
        if (currency == null) {
            if (other.currency != null) {
                return false;
            }
        } else if (currency.compareTo(other.currency) != 0) {
            return false;
        }
        if (typePeriode != other.typePeriode) {
            return false;
        }
        return true;
    }

}
