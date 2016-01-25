package globaz.musca.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class FAActionProcessServices extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public FAActionProcessServices(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Appeler la page _de.jsp de PassageFacturation Date de création : (06.03.2003 14:23:35)
     * 
     * @auteur: btc
     */
    private void _actionBrowseDirectory(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";

        /*
         * choix destination
         */
        _destination = "/" + globaz.musca.application.FAApplication.APPLICATION_MUSCA_REP + "/"
                + FWDefaultServletAction.getIdLangueIso(session) + "/browseDirectory.jsp"; // la
        // page
        // jsp
        // qui
        // liste
        // le
        // répertoire

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        if (getAction().getActionPart().equalsIgnoreCase("browseDirectory")) {
            // chercher avec chargement des données nécessaire
            _actionBrowseDirectory(session, request, response, dispatcher);
        }
    }
}
