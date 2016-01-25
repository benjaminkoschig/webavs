package globaz.corvus.utils;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.util.StringTokenizer;

public class RENumberFormatter {
    public static String fmt(String source, boolean wantQuote, boolean wantDecimalsIfZero, boolean wantBlankIfZero,
            int nDecimals, int nPreZero) {
        String s1 = JANumberFormatter.fmt(source, wantQuote, wantDecimalsIfZero, wantBlankIfZero, nDecimals);
        String gauche = "";
        String droite = "";
        StringTokenizer token = new StringTokenizer(s1, ".");
        if (token.countTokens() > 0) {
            gauche = JadeStringUtil.fillWithZeroes((String) token.nextElement(), nPreZero);
            droite = token.countTokens() == 1 ? JadeStringUtil.fillWithZeroes((String) token.nextElement(), nPreZero)
                    : JadeStringUtil.fillWithZeroes("0", nDecimals);
        } else {
            gauche = JadeStringUtil.fillWithZeroes("0", nPreZero);
            droite = JadeStringUtil.fillWithZeroes("0", nDecimals);
        }
        return gauche.concat(".").concat(droite);
    }

    /**
     * Constructeur
     */
    public RENumberFormatter() {
        super();
    }
}
