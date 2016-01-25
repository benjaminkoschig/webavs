package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.params.FWParamString;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Remplace la FWDefaultServletAction dans le cadre du BMS.
 * Cette Servlet écrase les actions afin qu'elles redirigent sans les paramètres.
 */
public abstract class PTDefaultServletAction extends FWDefaultServletAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(PTDefaultServletAction.class);

    public PTDefaultServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = "";
        try {
            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);

            /*
             * recuperation du bean depuis la session
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set automatique des proprietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeAdd() call du dispatcher, puis mis en session
             */
            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, newAction);
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix de la destination
             */
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestAjouterSucces(session, request, response, viewBean);
            } else {
                destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
            e.printStackTrace();
        }

        /*
         * redirection vers la destination
         */
        goSendRedirectWithoutParameters(destination, request, response);
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination;
        try {
            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);

            /*
             * recupération du bean depuis la sesison
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */
            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, newAction);
            session.setAttribute("viewBean", viewBean);

            /*
             * choix de la destination _valid=fail : revient en mode edition
             */

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirectWithoutParameters(destination, request, response);
    }

    protected final void setBeanProperties(HttpServletRequest request, FWViewBeanInterface viewBean) {
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (InvocationTargetException e) {
            LOGGER.error(e.toString());
        } catch (IllegalAccessException e) {
            LOGGER.error(e.toString());
        }
    }

    @Override
    protected void actionListerAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.actionListerAJAX(session, request, response, dispatcher);

        if ("true".equals(request.getParameter("changeStack"))) {
            FWUrlsStack stack = ((FWUrlsStack) session.getAttribute(FWServlet.URL_STACK));
            FWUrl url = stack.peek();
            FWUrl urlFromRequest = new FWUrl(request);
            urlFromRequest.removeParam(FWServlet.USER_ACTION);
            String parametersList = urlFromRequest.getParamsList();
            FWUrl newUrl = new FWUrl(request.getRequestURL() + "?" + parametersList);
            newUrl.addParam(new FWParamString(FWServlet.USER_ACTION, url.getParam(FWServlet.USER_ACTION).getValue()
                    .toString()));
            stack.push(newUrl);
            session.setAttribute(FWServlet.URL_STACK, stack);
        }
    }
}
