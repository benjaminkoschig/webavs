package ch.globaz.common.util;

import globaz.framework.util.FWCurrency;

import java.text.DecimalFormat;

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

    public static String toStringOrNullCurrencyFormat(Double toReturn){
        if(toReturn == null){
            return null;
        }
        FWCurrency currency = new FWCurrency(toReturn);
        return currency.toStringFormat();
    }
}
