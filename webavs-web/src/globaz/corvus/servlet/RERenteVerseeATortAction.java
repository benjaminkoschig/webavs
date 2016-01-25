package globaz.corvus.servlet;

import globaz.corvus.vb.rentesaccordees.RERenteVerseeATortAjaxViewBean;
import globaz.corvus.vb.rentesaccordees.RERenteVerseeATortViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRHybridAction;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author PBA
 */
public class RERenteVerseeATortAction extends PRHybridAction {

    public RERenteVerseeATortAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";

        String idDemandeRente = request.getParameter("noDemandeRente");
        if (JadeStringUtil.isBlankOrZero(idDemandeRente)) {
            idDemandeRente = request.getParameter("noDemande");
        }

        RERenteVerseeATortViewBean renteVerseeATortViewBean = new RERenteVerseeATortViewBean();
        renteVerseeATortViewBean.setIdDemandeRente(Long.parseLong(idDemandeRente));

        try {
            JSPUtils.setBeanProperties(request, renteVerseeATortViewBean);

            String userAction = request.getParameter("userAction");
            FWAction action = FWAction.newInstance(userAction);

            renteVerseeATortViewBean = (RERenteVerseeATortViewBean) mainDispatcher.dispatch(renteVerseeATortViewBean,
                    action);
            session.removeAttribute(FWServlet.VIEWBEAN);
            this.saveViewBean(renteVerseeATortViewBean, request);

        } catch (InvocationTargetException e) {
            renteVerseeATortViewBean.setMessage(e.getMessage());
            renteVerseeATortViewBean.setMsgType(FWViewBeanInterface.ERROR);
            destination = FWDefaultServletAction.ERROR_PAGE;
        } catch (IllegalAccessException e) {
            renteVerseeATortViewBean.setMessage(e.getMessage());
            renteVerseeATortViewBean.setMsgType(FWViewBeanInterface.ERROR);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        forward(destination, request, response);
    }

    @Override
    protected void actionAfficherAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String idEntity = request.getParameter("idEntity");
        String idRenteVerseeATort = idEntity.split("-")[0];

        boolean modificationPossible = Boolean.parseBoolean(idEntity.split("-")[1]);

        RERenteVerseeATortAjaxViewBean ajaxViewBean = new RERenteVerseeATortAjaxViewBean();
        ajaxViewBean.setIdRenteVerseeATort(idRenteVerseeATort);
        ajaxViewBean.setModificationPossible(modificationPossible);

        afficherAjax(ajaxViewBean, request, response, mainDispatcher);
    }

    @Override
    protected void actionAjouterAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "/corvusRoot/ajax/rentesaccordees/renteVerseeATortAjax_afficherAJAX.jsp";
        RERenteVerseeATortAjaxViewBean ajaxViewBean = new RERenteVerseeATortAjaxViewBean();
        try {
            JSPUtils.setBeanProperties(request, ajaxViewBean);
            ajaxViewBean = (RERenteVerseeATortAjaxViewBean) mainDispatcher.dispatch(ajaxViewBean, getAction());
        } catch (Exception ex) {
            ajaxViewBean.setMsgType(FWViewBeanInterface.ERROR);
            if (JadeStringUtil.isBlank(ex.getMessage()) && (ex.getCause() != null)) {
                ajaxViewBean.setMessage(ex.getCause().getMessage());
            } else {
                ajaxViewBean.setMessage(ex.getMessage());
            }
        }
        this.saveViewBean(ajaxViewBean, request);

        forward(destination, request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        FWAction action = FWAction.newInstance(request.getParameter(FWServlet.USER_ACTION));

        if ("creerNouvelleEntiteeAJAX".equals(action.getActionPart())) {
            String idDemandeRente = request.getParameter("idDemandeRente");

            RERenteVerseeATortAjaxViewBean viewBean = new RERenteVerseeATortAjaxViewBean();
            viewBean.setModificationPossible(true);
            viewBean.setIdDemandeRente(idDemandeRente);

            afficherAjax(viewBean, request, response, dispatcher);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    @Override
    protected void actionModifierAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "/corvusRoot/ajax/rentesaccordees/renteVerseeATortAjax_afficherAJAX.jsp";
        RERenteVerseeATortAjaxViewBean ajaxViewBean = new RERenteVerseeATortAjaxViewBean();
        try {
            JSPUtils.setBeanProperties(request, ajaxViewBean);
            ajaxViewBean = (RERenteVerseeATortAjaxViewBean) mainDispatcher.dispatch(ajaxViewBean, getAction());
        } catch (Exception ex) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
            ajaxViewBean.setMsgType(FWViewBeanInterface.ERROR);
            ajaxViewBean.setMessage(ex.toString());
        }
        this.saveViewBean(ajaxViewBean, request);

        forward(destination, request, response);
    }

    @Override
    protected void actionSupprimerAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "/jade/ajax/templateAjax_afficherAJAX.jsp";
        RERenteVerseeATortAjaxViewBean ajaxViewBean = new RERenteVerseeATortAjaxViewBean();
        try {
            JSPUtils.setBeanProperties(request, ajaxViewBean);
            ajaxViewBean = (RERenteVerseeATortAjaxViewBean) mainDispatcher.dispatch(ajaxViewBean, getAction());
        } catch (Exception ex) {
            ajaxViewBean.setMsgType(FWViewBeanInterface.ERROR);
            if (JadeStringUtil.isBlank(ex.getMessage()) && (ex.getCause() != null)) {
                ajaxViewBean.setMessage(ex.getCause().getMessage());
            } else {
                ajaxViewBean.setMessage(ex.getMessage());
            }
        }
        this.saveViewBean(ajaxViewBean, request);

        forward(destination, request, response);
    }

    private void afficherAjax(RERenteVerseeATortAjaxViewBean viewBean, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "/corvusRoot/ajax/rentesaccordees/renteVerseeATortAjax_afficherAJAX.jsp";

        try {
            viewBean = (RERenteVerseeATortAjaxViewBean) mainDispatcher.dispatch(viewBean,
                    FWAction.newInstance(request.getParameter("userAction")));
            this.saveViewBean(viewBean, request);
        } catch (Exception ex) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
        }

        forward(destination, request, response);
    }
}
