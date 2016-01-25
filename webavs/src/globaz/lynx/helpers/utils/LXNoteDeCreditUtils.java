package globaz.lynx.helpers.utils;

import globaz.globall.api.BISession;
import globaz.globall.db.BTransaction;
import globaz.lynx.db.facture.LXFactureViewBean;

public class LXNoteDeCreditUtils {

    /**
     * Validation des informations saisies par l'utilisateur.
     * 
     * @param session
     * @param transaction
     * @param facture
     * @throws Exception
     */
    public static void validate(BISession session, BTransaction transaction, LXFactureViewBean facture)
            throws Exception {

        LXHelperUtils.validateCommonPart(session, transaction, facture);
    }

    /**
     * Constructeur
     */
    protected LXNoteDeCreditUtils() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
