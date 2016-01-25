/*
 * Cr�� le 8 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.hera.tools;

import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe utilitaire pour tout ce qui concerne les BSession et BISession.
 * </p>
 * 
 * @author vre
 */
public class SFSession {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Connecter la session fournie � une nouvelle session pour l'application nomm�e.
     * 
     * <p>
     * Si la session transmise est d�j� une session pour l'application distante, elle n'est pas connect�e une deuxi�me
     * fois.
     * </p>
     * 
     * @param session
     *            une session pour notre application.
     * @param nomApplication
     *            le nom d'une application diff�rente de la n�tre.
     * 
     * @return la session de l'application distante ou la session transmise si le nom est null ou la session est d�j�
     *         une session de l'application distante.
     * 
     * @throws Exception
     *             si la connection �choue.
     */
    public static final BISession connectSession(BISession session, String nomApplication) throws Exception {
        if ((nomApplication == null) || nomApplication.equalsIgnoreCase(session.getApplicationId())) {
            return session;
        }

        BIApplication remoteApplication = GlobazSystem.getApplication(nomApplication);
        BISession remoteSession = remoteApplication.newSession();

        session.connectSession(remoteSession);

        return remoteSession;
    }
}
