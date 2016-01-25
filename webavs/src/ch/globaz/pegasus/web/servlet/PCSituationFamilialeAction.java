package ch.globaz.pegasus.web.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.ISFUrlEncode;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.pegasus.business.constantes.IPCActions;

public class PCSituationFamilialeAction extends PRDefaultAction {

    public PCSituationFamilialeAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";
        String action = "";
        String idTiers = request.getParameter("idTiers");
        String idDossier = request.getParameter("idDossier");

        try {
            String urlRetour = ISFUrlEncode.encodeUrl("/pegasus?userAction=" + IPCActions.ACTION_DEMANDE + "."
                    + FWAction.ACTION_CHERCHER + "&selectedId=" + idDossier + "&idDossier=" + idDossier);

            String csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;

            action = "hera.famille.apercuRelationConjoint.entrerApplication&csDomaine=" + csDomaine + "&idTiers="
                    + idTiers + "&urlFrom=" + urlRetour;

            _destination = "/hera?userAction=" + action;

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(_destination, request, response);
    }

}
