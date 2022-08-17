package ch.globaz.eform.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GFStatistiqueServletAction extends FWDefaultServletAction {
    public final static String PATH_EFORM = "eform.statistique.statistique";

    public GFStatistiqueServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
        try {

            JSPUtils.setBeanProperties(request, viewBean);

            mainDispatcher.dispatch(viewBean, getAction());

        } catch (Exception e) {
            JadeLogger.error("Failed to prepare viewBean for actionChercher", e);
        }

        super.actionChercher(session, request, response, mainDispatcher);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
                                     HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this.getActionFullURL() + ".chercher";
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                FWDispatcher dispatcher) throws ServletException, IOException {
        String actionPart = getAction().getActionPart();
        String destination = null;

        // Définition de l'action custom standard pour l'application ARIES
        // Attention, si appel de custom action, on passe le paramètre "id" au lieu de "selectedId"

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            // Récupération du viewBean depuis la session
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, dispatcher.getPrefix());
            // Copie des propriétés
            JSPUtils.setBeanProperties(request, viewBean);

            // Traitement
            viewBean = dispatcher.dispatch(viewBean, action);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            // Choix de la destination avec prise en compte des éventuels erreurs
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                destination = _getDestChercherSucces(session, request, response, viewBean);
            } else {
                destination = _getDestChercherEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), e.getMessage());
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        // Redirection vers la destination
        goSendRedirect(destination, request, response);
    }

    protected String _getDestChercherSucces(HttpSession session, HttpServletRequest request,
                                            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this.getActionFullURL() + ".chercher";
    }

    protected String _getDestChercherEchec(HttpSession session, HttpServletRequest request,
                                           HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this._getDestEchec(session, request, response, viewBean);
    }
}
