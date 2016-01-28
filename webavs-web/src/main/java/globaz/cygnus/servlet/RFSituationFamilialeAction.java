package globaz.cygnus.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.ISFUrlEncode;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author fha
 */
public class RFSituationFamilialeAction extends RFDefaultAction {

    public RFSituationFamilialeAction(FWServlet servlet) {
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
            String urlRetour = ISFUrlEncode.encodeUrl("/cygnus?userAction=" + IRFActions.ACTION_DOSSIER_JOINT_TIERS
                    + "." + FWAction.ACTION_CHERCHER + "&selectedId=" + idDossier + "&idDossier=" + idDossier);

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