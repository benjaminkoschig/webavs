package globaz.lynx.helpers.utils;

import globaz.globall.api.BISession;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.lynx.application.LXApplication;
import globaz.lynx.db.canevas.LXCanevasViewBean;

/**
 * TODO Commentaire de la classe
 * 
 * @author SCO 3 déc. 2009
 */
public class LXCanevasUtils {

    /**
     * Permet de valider les champs du canevas
     * 
     * @param session
     * @param transaction
     * @param facture
     * @throws Exception
     */
    public static void validate(BISession session, BTransaction transaction, LXCanevasViewBean facture)
            throws Exception {

        // Test du numéro de canevas interne
        LXApplication application = (LXApplication) GlobazServer.getCurrentSystem().getApplication(
                LXApplication.DEFAULT_APPLICATION_LYNX);
        application.getNumeroFactureFormatter().checkIdExterne(session, facture.getIdExterne());

    }

    /**
     * Constructeur
     */
    protected LXCanevasUtils() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }

}
