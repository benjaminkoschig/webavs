package globaz.lynx.format.facture;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Formater standard fournit par Globaz.
 * 
 * @author DDA
 */
public class LXFactureStandardFormat implements ILXFactureFormat {

    private static final int IDEXTERNE_LENGTH = 6;

    /**
     * @see ILXFactureFormat#checkIdExterne(BSession, String)
     */
    @Override
    public void checkIdExterne(BISession session, String value) throws Exception {
        if (JadeStringUtil.isBlank(value) || JadeStringUtil.isIntegerEmpty(value)) {
            throw new Exception(((BSession) session).getLabel("NUMERO_FACTURE_VIDE"));
        }

        if (value.length() != IDEXTERNE_LENGTH) {
            throw new Exception(((BSession) session).getLabel("NUMERO_FACTURE_LONGUEUR_NON_VALIDE"));
        }

        try {
            new Integer(value);
        } catch (Exception e) {
            throw new Exception(((BSession) session).getLabel("NUMERO_FACTURE_NOMBRE"));
        }
    }

}
