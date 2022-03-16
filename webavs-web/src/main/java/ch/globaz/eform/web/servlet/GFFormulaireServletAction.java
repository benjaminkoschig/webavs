package ch.globaz.eform.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.log.JadeLogger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GFFormulaireServletAction extends FWDefaultServletAction {
    public GFFormulaireServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
        try {

            JSPUtils.setBeanProperties(request, viewBean);

            mainDispatcher.dispatch(viewBean, getAction());

        } catch (Exception e) {
            JadeLogger.error("Failed to prepare viewBean for actionChercher", e);
        }

        super.actionChercher(session, request, response, mainDispatcher);
    }
}
