package globaz.corvus.servlet;

import globaz.corvus.vb.deblocage.REDeblocageAjaxViewBean;
import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.prestation.servlet.PRHybridAction;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class REDeblocageAction extends PRHybridAction {

    public REDeblocageAction(FWServlet servlet) {
        super(servlet);
    }

    private FWAJAXViewBeanInterface getViewBeanAndSetProperties(HttpServletRequest request,
            FWDispatcher mainDispatcher, FWAction newAction) throws InvocationTargetException, IllegalAccessException,
            Exception {
        FWAJAXViewBeanInterface viewBean;
        viewBean = (FWAJAXViewBeanInterface) FWViewBeanActionFactory.newInstance(newAction, mainDispatcher.getPrefix());
        // viewBean = this.deserializeViewBean(hexaSerializedViewBean);
        if (FWAJAXFindInterface.class.isAssignableFrom(viewBean.getClass())) {
            ((FWAJAXFindInterface) viewBean).initList();
        }
        globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
        ((BJadePersistentObjectViewBean) viewBean).retrieve();

        globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

        return viewBean;
    }

    @Override
    protected void actionModifierAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;

        FWAJAXViewBeanInterface viewBean = null;
        try {
            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);

            viewBean = getViewBeanAndSetProperties(request, mainDispatcher, newAction);

            viewBean.setGetListe(true);
            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */
            viewBean = (FWAJAXViewBeanInterface) beforeModifier(session, request, response, viewBean);
            viewBean = (FWAJAXViewBeanInterface) mainDispatcher.dispatch(viewBean, newAction);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            if (viewBean.hasList()) {
                destination = getAJAXListerSuccessDestination(session, request, viewBean);
            } else {
                destination = getAJAXAfficherSuccessDestination(session, request);
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
            request.setAttribute("exception", e);
        } finally {
            executeFindForAajaxList(request, viewBean);
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionSupprimerAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String action = request.getParameter("userAction");
        FWAction newAction = FWAction.newInstance(action);
        FWAJAXViewBeanInterface viewBean = new REDeblocageAjaxViewBean();
        String hexaSerializedViewBean = request.getParameter("viewBean");

        if (hexaSerializedViewBean != null && !"null".equals(hexaSerializedViewBean)) {
            super.actionSupprimerAJAX(session, request, response, mainDispatcher);
        } else {
            try {
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            /*
             * appelle du dispatcher
             */
            viewBean = (FWAJAXViewBeanInterface) beforeSupprimer(session, request, response, viewBean);
            viewBean.setGetListe(true);

            viewBean = (FWAJAXViewBeanInterface) mainDispatcher.dispatch(viewBean, newAction);

            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            servlet.getServletContext().getRequestDispatcher(getAJAXAfficherSuccessDestination(session, request))
                    .forward(request, response);
        }

    }

}
