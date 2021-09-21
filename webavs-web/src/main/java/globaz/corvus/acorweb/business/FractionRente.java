package globaz.corvus.acorweb.business;

import org.apache.commons.lang.StringUtils;

import java.util.Objects;

public enum FractionRente {

    FRACTION_1("1", 1.0f),
    FRACTION_2("2", 0.5f),
    FRACTION_3("3", 0.75f),
    FRACTION_4("4", 0.25f);

    private final String constFraction;
    private final Float valueFraction;

    FractionRente(String constFraction, Float valueFraction) {
        this.constFraction = constFraction;
        this.valueFraction = valueFraction;
    }

    /**
     * Récupère la valeur associée à la constante pour une fraction de rente.
     *
     * @param constFraction la constante de fraction de rente.
     * @return la valeur associée à la constante.
     */
    public static Float getValueFromConst(String constFraction) {
        for (FractionRente fraction : FractionRente.values()) {
            if (StringUtils.equals(constFraction, fraction.getConstFraction())) {
                return fraction.getValueFraction();
            }
        }
        return 1.0f;
    }

    /**
     * Récupère la constante associée à la valeur pour une fraction de rente.
     *
     * @param valueFraction  la valeur de fraction de rente.
     * @return la constante associée à la valeur
     */
    public static String getConstFromValue(Float valueFraction) {
        for (FractionRente fraction : FractionRente.values()) {
            if (Objects.equals(valueFraction, fraction.getValueFraction())) {
                return fraction.getConstFraction();
            }
        }
        return StringUtils.EMPTY;
    }

    private String getConstFraction() {
        return constFraction;
    }

    private Float getValueFraction() {
        return valueFraction;
    }
}
