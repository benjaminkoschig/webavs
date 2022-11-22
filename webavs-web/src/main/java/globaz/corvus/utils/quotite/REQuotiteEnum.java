package globaz.corvus.utils.quotite;

import java.util.Objects;

/**
 * Enum de mapping entre la fraction et la quotité
 */
public enum REQuotiteEnum {
    QUOTITE_100("1", "100.00"),
    QUOTITE_75("3", "75.00"),
    QUOTITE_50("2", "50.00"),
    QUOTITE_25("4", "25.00");

    private String fraction;
    private String quotite;

    REQuotiteEnum(String fraction, String quotite) {
        this.fraction = fraction;
        this.quotite = quotite;
    }

    public static String getQuotiteFromFraction(String aFraction) {
        for (REQuotiteEnum eachEnum : values()) {
            if (Objects.equals(eachEnum.fraction, aFraction)) {
                return eachEnum.quotite;
            }
        }
        return QUOTITE_100.quotite;
    }

}
