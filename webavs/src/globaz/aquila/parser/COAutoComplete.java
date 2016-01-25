/*
 * Créé le 3 avr. 06
 */
package globaz.aquila.parser;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.parser.CAAutoComplete;

/**
 * @author dvh
 */
public class COAutoComplete {

    /**
     * Recherche les affiliés en fonction du masque like (affiliés de type standard).
     * 
     * @param session
     * @param like
     * @return les affiliés en fonction du masque like (affiliés de type standard).
     */
    public static String getAffilies(BSession session, String like) {
        BSession sessionOsiris = null;

        try {
            sessionOsiris = (BSession) GlobazSystem.getApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS)
                    .newSession();
            session.connectSession(sessionOsiris);
        } catch (Exception e) {
            JadeLogger.warn(COAutoComplete.class, e);
        }

        return CAAutoComplete.getAffilies(sessionOsiris, like, CACompteAnnexe.GENRE_COMPTE_STANDARD);
    }

}
