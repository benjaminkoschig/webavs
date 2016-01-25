package globaz.libra.servlet;

import globaz.envoi.db.parametreEnvoi.access.ENRappel;
import globaz.envoi.db.parametreEnvoi.access.ENRappelManager;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.libra.vb.formules.LIChampsViewBean;
import globaz.libra.vb.formules.LIFormulesDetailViewBean;
import globaz.libra.vb.formules.LIFormulesViewBean;
import globaz.libra.vb.formules.LIRappelViewBean;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LIFormulesAction extends LIDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public LIFormulesAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof LIFormulesDetailViewBean) {
            return "/libra?userAction=" + ILIActions.ACTION_FORMULES_RC + "." + FWAction.ACTION_CHERCHER;
        } else if (viewBean instanceof LIRappelViewBean) {
            return "/libra?userAction=" + ILIActions.ACTION_FORMULES_RC + "." + FWAction.ACTION_CHERCHER;
        } else {
            return super._getDestModifierSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof LIFormulesDetailViewBean) {
            return "/libra?userAction=" + ILIActions.ACTION_FORMULES_RC + "." + FWAction.ACTION_CHERCHER;
        } else if (viewBean instanceof LIRappelViewBean) {
            return "/libra?userAction=" + ILIActions.ACTION_FORMULES_RC + "." + FWAction.ACTION_CHERCHER;
        } else {
            return super._getDestModifierSucces(session, request, response, viewBean);
        }
    }

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

            boolean isDispatch = true;
            FWViewBeanInterface viewBean;

            if (session.getAttribute(FWServlet.VIEWBEAN) instanceof LIFormulesDetailViewBean
                    && session.getAttribute(FWServlet.VIEWBEAN) != null) {
                if (((LIFormulesDetailViewBean) session.getAttribute(FWServlet.VIEWBEAN)).isRetourSelCS()) {
                    viewBean = (FWViewBeanInterface) session.getAttribute(FWServlet.VIEWBEAN);
                    ((LIFormulesDetailViewBean) viewBean).setRetourSelCS(false);
                    ((LIFormulesDetailViewBean) viewBean).getFormulePDF().setClasseName(
                            ((LIFormulesDetailViewBean) viewBean).getClassName());
                    ((LIFormulesDetailViewBean) viewBean).getFormulePDF().setDomaine(
                            ((LIFormulesDetailViewBean) viewBean).getCsDomaine());
                    isDispatch = false;
                } else {
                    viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
                }
            } else if (session.getAttribute(FWServlet.VIEWBEAN) instanceof LIRappelViewBean
                    && session.getAttribute(FWServlet.VIEWBEAN) != null) {
                if (((LIRappelViewBean) session.getAttribute(FWServlet.VIEWBEAN)).isRetourSelCS()) {
                    viewBean = (FWViewBeanInterface) session.getAttribute(FWServlet.VIEWBEAN);
                    ((LIRappelViewBean) viewBean).setRetourSelCS(false);
                    isDispatch = false;
                } else {
                    viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
                }
            } else {
                viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
            }

            if (getAction().getClassPart().equals("rappel") && isDispatch) {
                viewBean = new LIRappelViewBean();

                ENRappelManager rapMgr = new ENRappelManager();
                rapMgr.setSession((BSession) mainDispatcher.getSession());
                rapMgr.setForIdFormule(selectedId);
                rapMgr.find();

                if (!rapMgr.isEmpty()) {
                    selectedId = ((ENRappel) rapMgr.getFirstEntity()).getIdRappel();
                }

            }

            if (isDispatch) {
                Class b = Class.forName("globaz.globall.db.BIPersistentObject");
                Method mSetId = b.getDeclaredMethod("setId", new Class[] { String.class });
                mSetId.invoke(viewBean, new Object[] { selectedId });
            }

            if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = beforeNouveau(session, request, response, viewBean);
            }

            viewBean = beforeAfficher(session, request, response, viewBean);

            if (isDispatch) {
                viewBean = mainDispatcher.dispatch(viewBean, getAction());
            }

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

        FWViewBeanInterface viewBean = new LIChampsViewBean();

        if (getAction().getClassPart().equals("formules")) {
            viewBean = new LIFormulesViewBean();
        }

        if (getAction().getClassPart().equals("champs")) {
            viewBean = new LIChampsViewBean();
            String id = request.getParameter("selectedId");
            ((LIChampsViewBean) viewBean).setIdFormule(id);
        }

        viewBean.setISession(mainDispatcher.getSession());

        saveViewBean(viewBean, request);

        String _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), null);

        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

}
