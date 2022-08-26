package ch.globaz.eform.web.servlet;

import ch.globaz.eform.constant.GFStatusEForm;
import ch.globaz.eform.utils.GFFileUtils;
import globaz.eform.vb.formulaire.GFFormulaireViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GFSuiviServletAction extends FWDefaultServletAction {
    public GFSuiviServletAction(FWServlet aServlet) {
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

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
                                     HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this.getActionFullURL() + ".afficher";
    }

    protected String _getDestChercherSucces(HttpSession session, HttpServletRequest request,
                                            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this.getActionFullURL() + ".chercher";
    }

    protected String _getDestChercherEchec(HttpSession session, HttpServletRequest request,
                                           HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this._getDestEchec(session, request, response, viewBean);
    }
}
