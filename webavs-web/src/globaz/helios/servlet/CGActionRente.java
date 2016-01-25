package globaz.helios.servlet;

import globaz.framework.controller.FWDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la saisie manuelle des récaps des rentes.
 * 
 * @author JPA
 */
public class CGActionRente extends CGActionNeedExerciceComptable {

    public CGActionRente(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if ("recapitulation".equals(getAction().getActionPart())) {
            actionRecapitulation(session, request, response, mainDispatcher);
        }
    }

    protected void actionRecapitulation(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {
            destination = getRelativeURL(request, session) + ".html";
        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}
