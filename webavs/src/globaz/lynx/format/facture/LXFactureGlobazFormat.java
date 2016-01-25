package globaz.lynx.format.facture;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Formater spécial pour Globaz.
 * 
 * @author DDA
 */
public class LXFactureGlobazFormat implements ILXFactureFormat {

    private static final int IDEXTERNE_LENGTH = 8;
    private static final int INDEX_CODE = 5;

    /**
     * @see ILXFactureFormat#checkIdExterne(BSession, String)
     */
    @Override
    public void checkIdExterne(BISession session, String value) throws Exception {

        if (JadeStringUtil.isBlank(value) || JadeStringUtil.isIntegerEmpty(value)) {
            throw new Exception(((BSession) session).getLabel("NUMERO_FACTURE_VIDE"));
        }

        if (value.length() != IDEXTERNE_LENGTH) {
            throw new Exception(((BSession) session).getLabel("FORMAT_GLOBAZ_MAX_LENGTH_8"));
        }

        try {
            new Integer(value.substring(0, INDEX_CODE));
        } catch (Exception e) {
            throw new Exception(((BSession) session).getLabel("FORMAT_GLOBAZ_NOMBRE_CINQ_CARACTERES"));
        }

        if (value.charAt(5) != '-') {
            throw new Exception(((BSession) session).getLabel("FORMAT_GLOBAZ_TIRET"));
        }

        try {
            new Integer(value.substring(INDEX_CODE));
        } catch (Exception e) {
            throw new Exception(((BSession) session).getLabel("FORMAT_GLOBAZ_NOMBRE_DERNIERS_CARACTERES"));
        }
    }
}
