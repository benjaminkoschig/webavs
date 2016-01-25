package ch.globaz.auriga.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.jade.context.JadeThread;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Default servlet action pour l'application AURIGA
 * 
 * @author bjo
 * 
 */
public abstract class AUAbstractDefaultServletAction extends FWDefaultServletAction {

    public AUAbstractDefaultServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestEchec(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        return super._getDestEchec(session, request, response, viewBean) + "&_method="
                + request.getParameter("_method") + "&valid=_fail";
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        // Définition de l'action custom standard pour l'application AURIGA
        // Attention, si appel de custom action, on passe le paramètre "id" au lieu de "selectedId"
        String destination = null;
        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            // Récupération du viewBean depuis la session
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, dispatcher.getPrefix());

            // Copie des propriétés
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Traitement
            viewBean = dispatcher.dispatch(viewBean, action);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            // Choix de la destination avec prise en compte des éventuels erreurs
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                destination = _getDestExecuterSucces(session, request, response, viewBean);
            } else {
                destination = _getDestExecuterEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), e.getMessage());
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        // Redirection vers la destination
        goSendRedirect(destination, request, response);
    }

    @Override
    protected void goSendRedirect(String url, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Ne propage plus les paramètres pour cette application
        super.goSendRedirectWithoutParameters(url, request, response);
    }

    /**
     * Récupère le viewBean à partir de la requête
     * 
     * @param request
     *            la requête
     * @return le viewBean
     */
    protected FWViewBeanInterface loadViewBean(HttpServletRequest request) {
        return (FWViewBeanInterface) request.getAttribute(FWServlet.VIEWBEAN);
    }

    /**
     * Récupère le viewBean à partir de la session
     * 
     * @param session
     *            la session
     * @return le viewBean
     */
    protected FWViewBeanInterface loadViewBean(HttpSession session) {
        return (FWViewBeanInterface) session.getAttribute(FWServlet.VIEWBEAN);
    }
}
