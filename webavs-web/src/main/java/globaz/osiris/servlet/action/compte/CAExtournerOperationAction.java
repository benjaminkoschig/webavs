package globaz.osiris.servlet.action.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.comptes.CAExtournerOperationViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour effectuer les extourne.
 * 
 * @author DDA
 */
public class CAExtournerOperationAction extends CADefaultServletAction {

    /**
     * Constructor for CAExtournerOperationAction.
     * 
     * @param servlet
     */
    public CAExtournerOperationAction(FWServlet servlet) {
        super(servlet);
    }

    protected void _actionAfficherExtourneOperation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";

        try {

            FWAction _action = FWAction.newInstance(request.getParameter("userAction"));

            String selectedId = request.getParameter("id");

            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(_action, mainDispatcher.getPrefix());
            ((CAExtournerOperationViewBean) viewBean).setIdOperation(selectedId);
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);

            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    protected void _actionExtournerOperation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String _destination = "";

        try {
            FWAction _action = FWAction.newInstance(request.getParameter("userAction"));

            String selectedId = request.getParameter("selectedId");

            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(_action, mainDispatcher.getPrefix());
            ((CAExtournerOperationViewBean) viewBean).setIdOperation(selectedId);
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, _action);

            session.setAttribute("viewBean", viewBean);
            String idSection = ((CAExtournerOperationViewBean) viewBean).getOperation().getIdSection();

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                _destination = _getDestExecuterEchec(session, request, response, viewBean);
            } else {
                _destination = "/" + _action.getApplicationPart() + "?userAction=" + _action.getApplicationPart() + "."
                        + _action.getPackagePart() + ".apercuSectionDetaille.chercher&id=" + idSection;
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        String actionSuite = getActionSuite(request);
        if ("extournerOperation".equals(actionSuite)) {
            if ("executerExtourne".equals(getAction().getActionPart())) {
                _actionExtournerOperation(session, request, response, dispatcher);
            } else if ("afficherOperation".equals(getAction().getActionPart())) {
                _actionAfficherExtourneOperation(session, request, response, dispatcher);
            }
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

}
