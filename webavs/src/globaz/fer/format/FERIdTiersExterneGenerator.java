/*
 * Créé le 24 févr. 06
 */
package globaz.fer.format;

import globaz.globall.db.BTransaction;
import globaz.globall.db.FWIncrementation;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.util.ITIIdTiersExterneGenerator;
import globaz.pyxis.util.TICodeEAN13;

/**
 * pour la FER 10[2..6] puis 9 position numérique puis digit EAN 13 ex : 1060001234568 formaté : 106.000123456.8
 * 
 * @author oca
 * 
 */
public class FERIdTiersExterneGenerator implements ITIIdTiersExterneGenerator, IFormatData {
    @Override
    public String check(Object value) throws Exception {
        return value + "";
    }

    @Override
    public String format(String value) throws Exception {
        if (JadeStringUtil.isEmpty(value)) {
            return "";
        }
        try {
            return value.substring(0, 3) + "." + value.substring(3, 12) + "." + value.charAt(12);
        } catch (Exception e) {
            return value;
        }

    }

    @Override
    public String next(BTransaction transaction) throws Exception {

        FWIncrementation incrementation = new FWIncrementation();
        incrementation.setSession(transaction.getSession());
        incrementation.setIdIncrement("TINUMEXT");
        incrementation.setIdCodeSysteme("");
        incrementation.setAnneeIncrement("");
        incrementation.execute(transaction);

        String numCaisseFormate = GlobazServer.getCurrentSystem().getApplication("PYXIS")
                .getProperty("noCaisseFormate");

        // 106 par défaut, pour la 106.1 CIAM
        String prefixe = "106";

        // si agences :
        if ("106.2".equals(numCaisseFormate)) {
            prefixe = "102";// CIFA

        } else if ("106.3".equals(numCaisseFormate)) {
            prefixe = "103";// CIGA

        } else if ("106.4".equals(numCaisseFormate)) {
            prefixe = "104";// CIAN

        } else if ("106.5".equals(numCaisseFormate)) {
            prefixe = "105";// CIAB

        }

        String res = prefixe + JadeStringUtil.fillWithZeroes(incrementation.getValeurIncrement(), 9);
        res += TICodeEAN13.buildDigitCode(res);

        return res;

    }

    @Override
    public String unformat(String value) throws Exception {
        return JadeStringUtil.removeChar(value, '.');
    }

}
