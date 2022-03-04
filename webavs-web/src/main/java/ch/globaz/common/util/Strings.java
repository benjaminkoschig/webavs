package ch.globaz.common.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilitaire pour les méthodes liés aux string
 */
public class Strings {

    public static String toStringOrNull(Object toReturn){
        if(toReturn == null){
            return null;
        }
        return String.valueOf(toReturn);
    }

    public static String toStringOrNullDoubleFormat(Double toReturn){
        if(toReturn == null){
            return null;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(toReturn);
    }

    public static boolean match(String s, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        return m.matches();
    }
}
