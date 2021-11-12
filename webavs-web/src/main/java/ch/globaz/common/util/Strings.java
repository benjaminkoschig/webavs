package ch.globaz.common.util;

import globaz.framework.util.FWCurrency;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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
}
