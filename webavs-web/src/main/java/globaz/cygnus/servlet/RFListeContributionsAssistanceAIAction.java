package globaz.cygnus.servlet;

import globaz.cygnus.vb.process.RFListeContributionsAssistanceAIViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author PBA
 */
public class RFListeContributionsAssistanceAIAction extends RFDefaultAction {

    public RFListeContributionsAssistanceAIAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFListeContributionsAssistanceAIViewBean viewBean = new RFListeContributionsAssistanceAIViewBean();

        viewBean = (RFListeContributionsAssistanceAIViewBean) mainDispatcher.dispatch(viewBean, getAction());

        String destination = null;
        try {
            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        this.saveViewBean(viewBean, request);
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        RFListeContributionsAssistanceAIViewBean viewBean = (RFListeContributionsAssistanceAIViewBean) request
                .getAttribute("viewBean");

        if (viewBean == null) {
            viewBean = new RFListeContributionsAssistanceAIViewBean();
        }

        try {
            JSPUtils.setBeanProperties(request, viewBean);
            viewBean = (RFListeContributionsAssistanceAIViewBean) mainDispatcher.dispatch(viewBean, getAction());
        } catch (Exception ex) {
            viewBean.setMessage(ex.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        String destination = null;
        this.saveViewBean(viewBean, session);
        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            destination = _getDestExecuterEchec(session, request, response, viewBean);
        } else {
            destination = _getDestExecuterSucces(session, request, response, viewBean);
        }
        forward(destination, request, response);
    }
}
