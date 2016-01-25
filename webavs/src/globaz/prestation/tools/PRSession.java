/*
 * Créé le 8 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.prestation.tools;

import globaz.framework.controller.FWController;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe utilitaire pour tout ce qui concerne les BSession et BISession.
 * </p>
 * 
 * @author vre
 */
public class PRSession {

    private final static String SESSION_CONTROLLER_NAME = "objController";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Connecter la session fournie à une nouvelle session pour l'application nommée.
     * 
     * <p>
     * Si la session transmise est déjà une session pour l'application distante, elle n'est pas connectée une deuxième
     * fois.
     * </p>
     * 
     * @param session
     *            une session pour notre application.
     * @param nomApplication
     *            le nom d'une application différente de la nôtre.
     * 
     * @return la session de l'application distante ou la session transmise si le nom est null ou la session est déjà
     *         une session de l'application distante.
     * 
     * @throws Exception
     *             si la connection échoue.
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

    public static BISession getSession(HttpSession httpSession) throws Exception {
        globaz.globall.api.BISession session = null;
        FWController controller = (FWController) httpSession.getAttribute(SESSION_CONTROLLER_NAME);
        if (controller != null) {
            session = controller.getSession();
        } else {
            throw new Exception("Aucune session connectée");
        }
        return session;
    }

}
