package globaz.osiris.servlet.action.ebill;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.ebill.CAGenerationListeAffiliesEBillViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class CAGenerationListeAffiliesEBillAction extends CADefaultServletAction {
    /**
     * Constructor for CADefaultServletAction.
     *
     * @param servlet
     */
    public CAGenerationListeAffiliesEBillAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     *
     */
    public void executerAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                        FWDispatcher mainDispatcher) throws ServletException{

    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                  FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        CAGenerationListeAffiliesEBillViewBean viewBean;

        try {
            /*if ((session.getAttribute(CADefaultServletAction.VB_ELEMENT) != null)
                    && (session.getAttribute(CADefaultServletAction.VB_ELEMENT) instanceof CAGenerationListeAffiliesEBillViewBean)) {
                viewBean = (CAGenerationListeAffiliesEBillViewBean) session.getAttribute(CADefaultServletAction.VB_ELEMENT);
            } else {*/
                viewBean = new CAGenerationListeAffiliesEBillViewBean();
            //}
            viewBean.setISession(mainDispatcher.getSession());
            JSPUtils.setBeanProperties(request, viewBean);
            setSessionAttribute(session, CADefaultServletAction.VB_ELEMENT, viewBean);
        } catch (IllegalAccessException e){
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        } catch (InvocationTargetException e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        } catch(Exception e){
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
