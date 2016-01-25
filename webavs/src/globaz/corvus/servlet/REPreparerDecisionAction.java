package globaz.corvus.servlet;

import globaz.corvus.vb.decisions.REPreparerDecisionAvecAjournementViewBean;
import globaz.corvus.vb.decisions.REPreparerDecisionSpecifiqueViewBean;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.servlet.PRHybridAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Actions pour la préparation de décision lorsqu'il y a du spécifique métier (ajournement, incarcération)
 */
public class REPreparerDecisionAction extends PRHybridAction {

    public REPreparerDecisionAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionExecuterAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "/jade/ajax/templateAjax_afficherAJAX.jsp";
        String action = request.getParameter(FWServlet.USER_ACTION);

        if (JadeStringUtil.isBlank(action)) {
            throw new IllegalArgumentException("an action is mandatory");
        }

        REPreparerDecisionSpecifiqueViewBean viewBean = null;
        if (action.startsWith(IREActions.ACTION_PREPARER_DECISION_AVEC_AJOURNEMENT)) {
            viewBean = new REPreparerDecisionAvecAjournementViewBean();
        }

        if (viewBean == null) {
            throw new IllegalArgumentException("Unknown action : " + action);
        }

        try {
            JSPUtils.setBeanProperties(request, viewBean);
            mainDispatcher.dispatch(viewBean, getAction());
        } catch (Exception ex) {
            PRDefaultAction.addErrorToViewBean(viewBean, ex.toString());
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
        }

        this.saveViewBean(viewBean, request);
        forward(destination, request, response);
    }
}
