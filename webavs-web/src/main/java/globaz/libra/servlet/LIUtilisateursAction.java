package globaz.libra.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.libra.vb.utilisateurs.LIUtilisateursViewBean;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LIUtilisateursAction extends LIDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public LIUtilisateursAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        try {

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                getAction().changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String selectedId = request.getParameter("selectedId");
            if (JadeStringUtil.isEmpty(selectedId)) {
                selectedId = request.getParameter("id");
            }

            FWViewBeanInterface viewBean;

            if (session.getAttribute(FWServlet.VIEWBEAN) instanceof LIUtilisateursViewBean
                    && session.getAttribute(FWServlet.VIEWBEAN) != null) {

                if (((LIUtilisateursViewBean) session.getAttribute(FWServlet.VIEWBEAN)).isRetourDepuisPyxis()) {
                    viewBean = (FWViewBeanInterface) session.getAttribute(FWServlet.VIEWBEAN);
                    ((LIUtilisateursViewBean) viewBean).setRetourDepuisPyxis(false);
                } else {
                    viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
                }
            } else {
                viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
            }

            Class b = Class.forName("globaz.globall.db.BIPersistentObject");
            Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
            mSetId.invoke(viewBean, new Object[] { selectedId });

            if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            }

            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";

        LIUtilisateursViewBean viewBean = new LIUtilisateursViewBean();
        viewBean.setSession((BSession) mainDispatcher.getSession());

        saveViewBean(viewBean, request);

        _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), null);

        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

}