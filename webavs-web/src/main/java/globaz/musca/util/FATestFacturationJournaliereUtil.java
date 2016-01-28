/*
 * Cr�� le 8 nov. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.musca.util;

import globaz.framework.controller.FWController;
import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.globall.db.BSession;
import javax.servlet.http.HttpSession;

public class FATestFacturationJournaliereUtil {
    /**
     * Constantes
     */
    public static final String USER_TEST_FACTURATION_JOURNALIERE = "TestFactuJournaliere";

    public static final boolean isUserTestFacturationJournaliere(HttpSession httpSession) {
        try {
            BSession session = (BSession) ((FWController) httpSession.getAttribute("objController")).getSession();

            FWSecureUserDetail user = new FWSecureUserDetail();
            user.setSession(session);
            user.setUser(session.getUserId());
            user.setLabel(FATestFacturationJournaliereUtil.USER_TEST_FACTURATION_JOURNALIERE);

            user.retrieve();

            return "true".equalsIgnoreCase(user.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
