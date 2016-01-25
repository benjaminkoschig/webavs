package globaz.phenix.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.globall.db.BSession;
import globaz.phenix.translation.CodeSystem;

/**
 * Permet d'annuler une action en cours Date de création : (03.06.2003 13:32:59)
 * 
 * @author: ado
 */
public class CPActionCancel extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CPActionCancel.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CPActionCancel(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    @Override
    public void doAction(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWController mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // mettre une erreur
        String _destination = "";
        BSession varSession = null;
        try {

            varSession = CodeSystem.getSession(session);
        } catch (Exception e) {
            varSession = null;
        }
        if (varSession != null && varSession.hasErrors()) {
            session.setAttribute("errorMessage", varSession.getLabel("ERREUR") + ": "
                    + varSession.getErrors().toString());
        } else {
            session.setAttribute("errorMessage", varSession.getLabel("CP_MSG_0176"));
        }
        // HACK : goSendRedirect pour repasser par les droits (approuvé par VYJ)
        _destination = "/phenix?userAction=phenix.principale.decision.chercher&idTiers="
                + (String) session.getAttribute("idTiers") + "&selectedId2="
                + (String) session.getAttribute("idAffiliation");
        goSendRedirect(_destination, request, response);
        // servlet.getServletContext().getRequestDispatcher("/phenix?userAction=phenix.principale.decision.chercher&idTiers="
        // + (String) session.getAttribute("idTiers") + "&selectedId2=" +
        // (String) session.getAttribute("idAffiliation")).forward(request,
        // response);
    }
}
