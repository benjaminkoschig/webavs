package globaz.lynx.format.fournisseur;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Formater standard fournit par Globaz.
 * 
 * @author DDA
 */
public class LXFournisseurStandardFormat implements ILXFournisseurFormat {

    /**
     * @see ILXFournisseurFormat#checkIdExterne(BSession, String)
     */
    @Override
    public void checkIdExterne(BISession session, String value) throws Exception {
        if (JadeStringUtil.isBlank(value) || JadeStringUtil.isIntegerEmpty(value)) {
            throw new Exception(((BSession) session).getLabel("NUMERO_FOURNISSEUR_VIDE"));
        }

        try {
            new Integer(value);
        } catch (Exception e) {
            throw new Exception(((BSession) session).getLabel("NUMERO_FOURNISSEUR_NOMBRE"));
        }
    }

}
