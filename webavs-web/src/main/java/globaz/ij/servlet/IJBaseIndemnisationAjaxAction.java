package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.ij.vb.controleAbsences.IJBaseIndemnisationAjaxViewBean;
import globaz.jade.log.JadeLogger;
import globaz.prestation.servlet.PRHybridAction;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class IJBaseIndemnisationAjaxAction extends PRHybridAction {

    public IJBaseIndemnisationAjaxAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = null;
        try {
            IJBaseIndemnisationAjaxViewBean viewBean = new IJBaseIndemnisationAjaxViewBean();
            JSPUtils.setBeanProperties(request, viewBean);
            mainDispatcher.dispatch(viewBean, getAction());
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception ex) {
            JadeLogger.error(this, ex);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionAjouterAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionAjouterAJAX(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionModifierAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionModifierAJAX(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionSupprimerAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionSupprimerAJAX(session, request, response, mainDispatcher);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof IJBaseIndemnisationAjaxViewBean) {
            try {
                JSPUtils.setBeanProperties(request, viewBean);
            } catch (Exception e) {
                try {
                    servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.ERROR_AJAX_PAGE)
                            .forward(request, response);
                } catch (Exception e1) {
                    // :( Pas le chois c'est pas beau... car il faudrait pouvoir remonter l'exception.
                    throw new RuntimeException(e1);
                }
            }
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        IJBaseIndemnisationAjaxViewBean vb = (IJBaseIndemnisationAjaxViewBean) viewBean;

        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            // request.getCharacterEncoding();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String dateDeDebut = request.getParameter("baseIndemnisation.dateDeDebut");
        String dateDeFin = request.getParameter("baseIndemnisation.dateDeFin");
        String joursExternes = request.getParameter("baseIndemnisation.joursExternes");
        String joursInternes = request.getParameter("baseIndemnisation.joursInternes");
        String joursInterruption = request.getParameter("baseIndemnisation.joursInterruption");
        String motifInterruption = request.getParameter("baseIndemnisation.motifInterruption");
        vb.getCurrentEntity().setRemarque(request.getParameter("baseIndemnisation.remarque"));

        vb.getCurrentEntity().setDateDeDebut(dateDeDebut);
        vb.getCurrentEntity().setDateDeFin(dateDeFin);
        vb.getCurrentEntity().setJoursExternes(joursExternes);
        vb.getCurrentEntity().setJoursInternes(joursInternes);
        vb.getCurrentEntity().setJoursInterruption(joursInterruption);
        vb.getCurrentEntity().setMotifInterruption(motifInterruption);
        return super.beforeModifier(session, request, response, viewBean);
    }
}
