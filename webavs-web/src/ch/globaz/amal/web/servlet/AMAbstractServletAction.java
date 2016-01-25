package ch.globaz.amal.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AMAbstractServletAction extends FWDefaultServletAction {
    protected static final String DESACTIVE_VALIDATION = FWServlet.VALID + "=fail&_back=sl";

    protected static final String METHOD_ADD = FWServlet.METHOD + "=" + AMAbstractServletAction.METHOD_ADD_VALUE;

    protected static final String METHOD_ADD_VALUE = "add";
    protected static final String METHOD_UPD = FWServlet.METHOD + "=" + FWServlet.UPD;
    protected static final Class[] PARAMS = new Class[] { HttpSession.class, HttpServletRequest.class,
            HttpServletResponse.class, FWDispatcher.class, FWViewBeanInterface.class };
    protected static final String VALID_NEW = FWServlet.VALID + "=new";
    protected static final String VERS_ECRAN_DE_ADD = "_de.jsp?" + AMAbstractServletAction.METHOD_ADD;
    protected static final String VERS_ECRAN_DE_UPD = "_de.jsp?" + AMAbstractServletAction.METHOD_UPD;
    protected static final String VERS_ECRAN_RC = "_rc.jsp";

    public AMAbstractServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        String destination = FWDefaultServletAction.ERROR_PAGE;
        FWViewBeanInterface viewBean;

        try {
            viewBean = this.loadViewBean(session);

            if (viewBean != null) {

                JSPUtils.setBeanProperties(request, viewBean);
            }

            Method methode = this.getClass().getMethod(getAction().getActionPart(), AMAbstractServletAction.PARAMS);

            destination = (String) methode.invoke(this,
                    new Object[] { session, request, response, dispatcher, viewBean });
        } catch (Exception e) {
            JadeLogger.error("Error in AMActionCustom", e);
        }

        // desactive le forward pour le cas ou la reponse a deja ete flushee
        if (!JadeStringUtil.isBlank(destination)) {
            goSendRedirect(destination, request, response);
        }
    }

    protected String defaultActionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher, FWViewBeanInterface viewBean, String successDestination)
            throws InvocationTargetException, IllegalAccessException {

        viewBean = FWViewBeanActionFactory.newInstance(getAction(), dispatcher.getPrefix());
        JSPUtils.setBeanProperties(request, viewBean);

        viewBean = dispatcher.dispatch(viewBean, getAction());
        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            return FWDefaultServletAction.ERROR_PAGE;
        }
        return successDestination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#goSendRedirect(java .lang.String,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
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
