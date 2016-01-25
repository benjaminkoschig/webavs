package globaz.lynx.format.facture;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Formater spécial pour Globaz.
 * 
 * @author DDA
 */
public class LXFactureFerCiamFormat implements ILXFactureFormat {

    private static final int DECEMBRE = 12;
    private static final int IDEXTERNE_LENGTH = 8;

    private static final int INDEX_MONTH = 2;

    private static final int INDEX_PIECE = 3;

    /**
     * @see ILXFactureFormat#checkIdExterne(BSession, String)
     */
    @Override
    public void checkIdExterne(BISession session, String value) throws Exception {
        if (JadeStringUtil.isBlank(value) || JadeStringUtil.isIntegerEmpty(value)) {
            throw new Exception(((BSession) session).getLabel("NUMERO_FACTURE_VIDE"));
        }

        if (value.length() != IDEXTERNE_LENGTH) {
            throw new Exception(((BSession) session).getLabel("FORMAT_FER_MAX_LENGTH_8"));
        }

        try {
            new Integer(value.substring(0, INDEX_MONTH));
        } catch (Exception e) {
            throw new Exception(((BSession) session).getLabel("FORMAT_FER_NOMBRE_DEUX_CARACTERES"));
        }

        int month = Integer.parseInt(value.substring(0, INDEX_MONTH));
        if (month > DECEMBRE || month == 0) {
            throw new Exception(((BSession) session).getLabel("FORMAT_FER_DEUX_PREMIERS_MOIS"));
        }

        if (value.charAt(2) != '-') {
            throw new Exception(((BSession) session).getLabel("FORMAT_FER_TIRET"));
        }

        try {
            new Integer(value.substring(INDEX_PIECE));
        } catch (Exception e) {
            throw new Exception(((BSession) session).getLabel("FORMAT_FER_NOMBRE_DERNIERS_CARACTERES"));
        }
    }

}
