/*
 * Créé le 24 févr. 06
 */
package globaz.fer.format;

import globaz.globall.db.BTransaction;
import globaz.globall.db.FWIncrementation;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.util.ITIIdTiersExterneGenerator;
import globaz.pyxis.util.TICodeEAN13;

/**
 * 
 * 
 * Ancienne implémentation de FERIdTiersExterneGenerator, qui ne tient pas compte du numéro des agences.
 * Cette classe a été crééer a la version 1-11-0 au cas ou les modification sur FERIdTiersExterneGenerator
 * ne fonctionneraient pas.
 * 
 * Eventuellement à supprimer en 1-12-0 une fois les modifs de FERIdTiersExterneGenerator validée.
 * 
 * pour la FER 106 puis 9 position numérique puis digit EAN 13 ex : 1060001234568 formaté : 106.000123456.8
 * 
 * @author oca
 * 
 */
public class FER106IdTiersExterneGenerator implements ITIIdTiersExterneGenerator, IFormatData {
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

        String res = "106" + JadeStringUtil.fillWithZeroes(incrementation.getValeurIncrement(), 9);

        res += TICodeEAN13.buildDigitCode(res);

        return res;

    }

    @Override
    public String unformat(String value) throws Exception {
        return JadeStringUtil.removeChar(value, '.');
    }

}
