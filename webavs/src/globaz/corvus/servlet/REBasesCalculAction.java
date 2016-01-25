package globaz.corvus.servlet;

import globaz.corvus.vb.basescalcul.REBasesCalculDixiemeRevisionViewBean;
import globaz.corvus.vb.basescalcul.REBasesCalculNeuviemeRevisionViewBean;
import globaz.corvus.vb.basescalcul.REBasesCalculViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.prestation.servlet.PRHybridAction;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author hpe
 */
public class REBasesCalculAction extends PRHybridAction {

    public REBasesCalculAction(final FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestEchec(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        this.saveViewBean(viewBean, session);
        return getActionFullURL() + ".afficher";
    }

    @Override
    protected void actionAfficher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            String selectedId = request.getParameter("selectedId");
            String csTypeBasesCalcul = request.getParameter("csTypeBasesCalcul");

            if (null == selectedId) {
                selectedId = request.getParameter("idBaseCalcul");
            }

            FWViewBeanInterface viewBean = null;

            if (csTypeBasesCalcul == null) {

                viewBean = this.loadViewBean(session);

                if (viewBean.getClass().getName()
                        .equals("globaz.corvus.vb.basescalcul.REBasesCalculDixiemeRevisionViewBean")) {
                    csTypeBasesCalcul = "10";
                } else {
                    csTypeBasesCalcul = "9";
                }

            } else {
                if (csTypeBasesCalcul.equals("09") || csTypeBasesCalcul.equals("9")) {

                    viewBean = new REBasesCalculNeuviemeRevisionViewBean();

                } else if (csTypeBasesCalcul.equals("10")) {

                    viewBean = new REBasesCalculDixiemeRevisionViewBean();
                }
            }

            Class<?> b = Class.forName("globaz.globall.db.BIPersistentObject");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });

            if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            }

            viewBean = beforeAfficher(session, request, response, viewBean);

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {

                viewBean = mainDispatcher.dispatch(viewBean, getAction());

                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", viewBean);
                request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            } else {

                String testMsg = viewBean.getMessage();

                viewBean = mainDispatcher.dispatch(viewBean, getAction());

                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", viewBean);
                request.setAttribute(FWServlet.VIEWBEAN, viewBean);

                viewBean.setMessage(testMsg);
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            if (csTypeBasesCalcul.equals("09") || csTypeBasesCalcul.equals("9")) {
                _destination = "/corvusRoot/basescalcul/basesCalculNeuviemeRevision_de.jsp";
            } else if (csTypeBasesCalcul.equals("10")) {
                _destination = "/corvusRoot/basescalcul/basesCalculDixiemeRevision_de.jsp";
            }

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionChercher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        REBasesCalculViewBean viewBean = new REBasesCalculViewBean();
        viewBean.setSession((BSession) mainDispatcher.getSession());

        this.saveViewBean(viewBean, request);

        super.actionChercher(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionSupprimer(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        String action = request.getParameter("userAction");
        String idTierRequerant = request.getParameter("idTierRequerant");
        String idRenteCalculee = request.getParameter("idRenteCalculee");

        FWAction _action = FWAction.newInstance(action);
        String _destination = "";

        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

        viewBean = beforeSupprimer(session, request, response, viewBean);
        viewBean = mainDispatcher.dispatch(viewBean, _action);

        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

        if (goesToSuccessDest) {
            _destination = _getDestSupprimerSucces(session, request, response, viewBean) + "&idTierRequerant="
                    + idTierRequerant + "&idRenteCalculee=" + idRenteCalculee;
        } else {
            _destination = _getDestSupprimerEchec(session, request, response, viewBean);
        }

        goSendRedirect(_destination, request, response);
    }

    /**
     * Action custom d'édition du numéro de décision d'une base de calcul
     * 
     * @param session La session
     * @param request La requête HTTP
     * @param response La réponse HTTP
     * @param dispatcher Le dispatcher
     * @param viewBean Le view-bean
     */
    public String editerNumeroDecisionAJAX(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher, FWViewBeanInterface viewBean) {

        REBasesCalculDixiemeRevisionViewBean typedViewBean = (REBasesCalculDixiemeRevisionViewBean) viewBean;
        String value = request.getParameter("value");

        // MAJ du numéro de décision à partir du paramètre de requête "value"
        typedViewBean.setReferenceDecision(value);

        dispatcher.dispatch(viewBean, getAction());

        this.saveViewBean(viewBean, request);

        return "/jade/ajax/templateAjax_afficherAJAX.jsp";
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewBean;
    }

}