package globaz.hermes.utils;

import globaz.globall.util.JANumberFormatter;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class CurrencyUtils {
    public static String formatCurrency(String amount) {
        return formatCurrency(amount, true, true, true, 2);
    }

    //
    public static String formatCurrency(String amount, boolean wantQuote, boolean wantDecimalsIfZero,
            boolean wantBlankIfZero, int nDecimals) {
        try {
            return JANumberFormatter.fmt(amount, wantQuote, wantDecimalsIfZero, wantBlankIfZero, nDecimals);
        } catch (Exception e) {
            return amount;
        }
    }

    /**
     * Constructor for CurrencyUtils.
     */
    public CurrencyUtils() {
        super();
    }
}
