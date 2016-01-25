/*
 * Créé le 6 avr. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.lupus.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.globall.api.BISession;
import globaz.jade.log.JadeLogger;
import globaz.lupus.application.LUApplication;
import java.io.IOException;
import java.lang.reflect.Constructor;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LUMainServlet extends FWServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public FWController createController(BISession session) throws Exception {
        return new FWDispatcher(session, LUApplication.DEFAULT_APPLICATION_LUPUS, LUApplication.APPLICATION_PREFIX);
    }

    @Override
    protected void customize(FWUrlsStack aStack) {
    }

    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        request.setAttribute("mainServletPath", request.getServletPath());
        String action = request.getParameter("userAction");

        FWAction act = FWAction.newInstance(action);

        FWDefaultServletAction actionMapper = null;
        try {
            JadeLogger.debug(this, "LUPUS action " + act);
            Class actionClass = LUMainServletAction.getActionClass(act);
            Constructor c = actionClass.getConstructor(new Class[] { FWServlet.class });
            actionMapper = (FWDefaultServletAction) c.newInstance(new Object[] { this });
        } catch (Exception e) {

            // Ceci est une erreur critique, lupus ne fonctionne pas
            // sans le mapping des actions

            JadeLogger.warn(this, "*********************************************************");
            JadeLogger.warn(this, "* LUPUS : WARNING                                         ");
            JadeLogger.warn(this, "* Action mapping FAILED for action " + act);
            JadeLogger.warn(this, "*********************************************************");
            e.printStackTrace();
        }
        try {
            actionMapper.doAction(session, request, response, mainController);
        } catch (Exception e) {
            JadeLogger.warn(this, "*********************************************************");
            JadeLogger.warn(this, "* LUPUS : WARNING                                         ");
            JadeLogger.warn(this, "* Action failed  : action " + act);
            JadeLogger.warn(this, "*********************************************************");
            e.printStackTrace();
        }
    }

    @Override
    protected void goHomePage(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws Exception {
        String path = "/" + LUApplication.APPLICATION_LUPUS_REP + "/" + FWDefaultServletAction.getIdLangueIso(session)
                + "/";

        getServletContext().getRequestDispatcher(path + "homePage.jsp").forward(request, response);
    }

    /**
     * Détermine si le servlet actuel doit utiliser la langue utilisateur pour trouver les pages .jsp (genre
     * <CODE>/FR</CODE>, <CODE>/DE</CODE>, etc.).
     * 
     * @return <code>true</code> si le servlet utilise la langue utilisateur, <code>false</code> sinon. Avec FW V29 Date
     *         de création : (18.05.2005 11:00:00)
     */
    @Override
    public boolean hasLanguageInPagesPath() {
        return true;
    }
}
