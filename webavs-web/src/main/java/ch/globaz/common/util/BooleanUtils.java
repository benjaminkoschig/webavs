package ch.globaz.common.util;

import org.apache.commons.lang.StringUtils;

public class BooleanUtils {
    public static final String YES_FR = "Oui";
    public static final String YES_DE = "Ja";
    public static final String YES_EN = "Yes";
    public static final String YES_IT = "Si";

    public static boolean translateBoolean(String bool) {
        return StringUtils.equalsIgnoreCase(YES_FR, bool) || StringUtils.equalsIgnoreCase(YES_DE, bool) ||
                StringUtils.equalsIgnoreCase(YES_EN, bool) || StringUtils.equalsIgnoreCase(YES_IT, bool);
    }
}
