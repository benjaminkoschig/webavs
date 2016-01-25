package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationJointTiersManager;
import globaz.ij.vb.basesindemnisation.IJBaseIndemnisationJoinTiersViewBean;
import globaz.ij.vb.controleAbsences.IJDossierControleAbsencesAjaxViewBean;
import globaz.jade.log.JadeLogger;
import globaz.prestation.servlet.PRHybridAction;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class IJDossierControleAbsencesAjaxAction extends PRHybridAction {

    public IJDossierControleAbsencesAjaxAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = null;
        try {

            IJDossierControleAbsencesAjaxViewBean viewBean = new IJDossierControleAbsencesAjaxViewBean();

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
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        IJBaseIndemnisationJoinTiersViewBean viewBean = new IJBaseIndemnisationJoinTiersViewBean();
        IJBaseIndemnisationJointTiersManager manager = new IJBaseIndemnisationJointTiersManager();
        manager.setSession((BSession) mainDispatcher.getSession());
        try {
            JSPUtils.setBeanProperties(request, viewBean);
            String h = viewBean.getIdPrononce();
            String o = viewBean.getIdBaseIndemisation();
            String p = viewBean.getIdTiers();
            manager.setForIdPrononce(viewBean.getIdPrononce());
            try {
                manager.find();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (manager.getSize() == 1) {
                IJBaseIndemnisationJoinTiersViewBean data = (IJBaseIndemnisationJoinTiersViewBean) manager
                        .getFirstEntity();
                System.out.println(data.getIdBaseIndemisation());
            }
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        super.actionAjouter(session, request, response, mainDispatcher);
    }
}
