package globaz.corvus.utils;

import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersUserCode;
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
     * Recuperation du code système en fonction d'un codeIsoLangue et non en fonction de la langue de l'utilisateur
     * @param codeSystemID  Code système à récupérer
     * @param codeIsoLangue langue dans lequel le récupérer
     * @param bSession      bSession permettant d'accéder aux données
     * @return              libellé du codeSystem dans la langue donnée
     * @throws Exception    En cas d'Exception lors de FWParametersUserCode.retrieve()
     */
    public static String codeSystemToLibelle(String codeSystemID, String codeIsoLangue, BSession bSession) throws Exception{
        FWParametersUserCode userCode = new FWParametersUserCode();
        userCode.setSession(bSession);
        userCode.setIdCodeSysteme(codeSystemID);
        if(codeIsoLangue.equals("IT"))
        {
            userCode.setIdLangue("I");
        } else if(codeIsoLangue.equals("DE"))
        {
            userCode.setIdLangue("D");
        } else
        {
            userCode.setIdLangue("F");
        }

        userCode.retrieve();
        return userCode.getLibelle();
    }
    /**
     * Constructeur
     */
    public RENumberFormatter() {
        super();
    }
}
