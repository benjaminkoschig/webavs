package ch.globaz.ariesaurigacommon.utils;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;

public class AriesAurigaNumericUtils {

    public static boolean isNumericDecimal(String theNumeric, int integerPartMaxSize, int decimalPartMaxSize) {

        theNumeric = JANumberFormatter.deQuote(theNumeric);

        if (!JadeNumericUtil.isNumeric(theNumeric)) {
            return false;
        }

        return AriesAurigaNumericUtils.isNumericDecimalCommon(theNumeric, integerPartMaxSize, decimalPartMaxSize);

    }

    private static boolean isNumericDecimalCommon(String theNumeric, int integerPartMaxSize, int decimalPartMaxSize) {

        theNumeric = JANumberFormatter.deQuote(theNumeric);

        int indexOfSeparateurDecimal = JadeStringUtil.indexOf(theNumeric, ".");
        if (indexOfSeparateurDecimal == -1) {
            return false;
        }

        String theIntegerPart = theNumeric.substring(0, indexOfSeparateurDecimal);
        String theDecimalPart = theNumeric.substring(indexOfSeparateurDecimal + 1);

        // Gestion des nombres négatifs
        // le - ne doit pas être pris en compte pour calculer la taille de la partie entière
        theIntegerPart = JadeStringUtil.removeChar(theIntegerPart, '-');

        if (JadeStringUtil.isEmpty(theIntegerPart) || (theIntegerPart.length() > integerPartMaxSize)) {
            return false;
        }

        if (JadeStringUtil.isEmpty(theDecimalPart) || (theDecimalPart.length() > decimalPartMaxSize)) {
            return false;
        }

        return true;
    }

    public static boolean isNumericDecimalPositif(String theNumeric, int integerPartMaxSize, int decimalPartMaxSize) {

        theNumeric = JANumberFormatter.deQuote(theNumeric);

        if (!JadeNumericUtil.isNumericPositif(theNumeric)) {
            return false;
        }

        return AriesAurigaNumericUtils.isNumericDecimalCommon(theNumeric, integerPartMaxSize, decimalPartMaxSize);
    }

    public static boolean isNumericIntegerPositif(String theNumeric, int maxSize) {

        return JadeNumericUtil.isIntegerPositif(theNumeric) && (theNumeric.length() <= maxSize);

    }

    public static boolean isNumericIntegerPositifFixedSize(String theNumeric, int size) {

        return JadeNumericUtil.isIntegerPositif(theNumeric) && (theNumeric.length() == size);

    }

    public static String removeDecimalPartIfZero(FWCurrency theCurrency) {
        String formattedValue = theCurrency.toStringFormat();
        return formattedValue.replace(".00", "");
    }

}
