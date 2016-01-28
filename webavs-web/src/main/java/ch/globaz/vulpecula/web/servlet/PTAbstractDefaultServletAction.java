package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe abstraite héritant de <code>FWDefaultServletAction</code> - Controller
 * de vue - qui va définir les comportements par défauts attendus par le
 * framework Jade
 * 
 * @author vyj
 */
public abstract class PTAbstractDefaultServletAction extends FWDefaultServletAction {

    /**
     * @param aServlet
     */
    public PTAbstractDefaultServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestEchec(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        return super._getDestEchec(session, request, response, viewBean) + "&_method="
                + request.getParameter("_method") + "&valid=_fail";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.framework.controller.FWDefaultServletAction#actionCustom(javax
     * .servlet.http.HttpSession, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        // Définition de l'action custom standard pour l'application WEB@AF
        // Attention, si appel de custom action, on passe le paramètre "id" au
        // lieu de "selectedId"
        String destination = null;
        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            /*
             * recupération du bean depuis la sesison
             */
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, dispatcher.getPrefix());
            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = dispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);

            /*
             * choix de la destination _valid=fail : revient en mode edition
             */

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestExecuterSucces(session, request, response, viewBean);
            } else {
                destination = _getDestExecuterEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(destination, request, response);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.framework.controller.FWDefaultServletAction#goSendRedirect(java
     * .lang.String, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goSendRedirect(String url, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Ne propage plus les paramètres pour cette application
        super.goSendRedirectWithoutParameters(url, request, response);
    }

    /**
     * charge le viewBean sauve dans la request sous le nom standard.
     * 
     * @param request
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected FWViewBeanInterface loadViewBean(HttpServletRequest request) {
        return (FWViewBeanInterface) request.getAttribute(FWServlet.VIEWBEAN);
    }

    /**
     * charge le viewBean sauve dans la session sous le nom standard.
     * 
     * @param session
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected FWViewBeanInterface loadViewBean(HttpSession session) {
        return (FWViewBeanInterface) session.getAttribute(FWServlet.VIEWBEAN);
    }
}
