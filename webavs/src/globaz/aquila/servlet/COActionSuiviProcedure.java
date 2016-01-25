package globaz.aquila.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class COActionSuiviProcedure extends CODefaultServletAction {

    /**
     * Initialise l'action.
     * 
     * @param servlet
     *            Le servlet concerné par cette action
     */
    public COActionSuiviProcedure(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".chercher" + getIdContentieuxParam(request);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".chercher" + getIdContentieuxParam(request);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".chercher" + getIdContentieuxParam(request);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if ("aquila.suiviprocedure.annulerEtape.afficher".equals(request.getParameter("userAction"))) {
            chargerContentieux(request, session, (BSession) mainDispatcher.getSession());
        }
        super.actionAfficher(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        chargerContentieux(request, session, (BSession) mainDispatcher.getSession());
        super.actionChercher(session, request, response, mainDispatcher);
    }

    private String getIdContentieuxParam(HttpServletRequest request) {
        if (!JadeStringUtil.isIntegerEmpty(request.getParameter("idContentieux"))) {
            return "&selectedId=" + request.getParameter("idContentieux");
        }

        return "";
    }

}
