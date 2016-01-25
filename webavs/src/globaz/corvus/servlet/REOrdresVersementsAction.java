package globaz.corvus.servlet;

import globaz.corvus.vb.ordresversements.IREOrdreVersementAjaxViewBean;
import globaz.corvus.vb.ordresversements.RECompensationInterDecisionAjaxViewBean;
import globaz.corvus.vb.ordresversements.REGestionRestitutionAjaxViewBean;
import globaz.corvus.vb.ordresversements.REOrdresVersementsAjaxViewBean;
import globaz.corvus.vb.ordresversements.REOrdresVersementsDetailAjaxViewBean;
import globaz.corvus.vb.ordresversements.REOrdresVersementsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.prestation.servlet.PRHybridAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author PBA
 */
public class REOrdresVersementsAction extends PRHybridAction {

    private static final String PAGE_DETAIL_CID = "/corvusRoot/ajax/ordresversements/compensationInterDecision_afficherAJAX.jsp";
    private static final String PAGE_DETAIL_ORDREVERSEMENT = "/corvusRoot/ajax/ordresversements/ordresVersementsAjax_afficherAJAX.jsp";
    private static final String PAGE_DETAIL_RESTITUTION = "/corvusRoot/ajax/ordresversements/gestionRestitutionSurDecision_afficherAJAX.jsp";
    private static final String PAGE_LISTE_ORDREVERSEMENT = "/corvusRoot/ajax/ordresversements/ordresVersementsAjax_listerAJAX.jsp";

    public REOrdresVersementsAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "";
        FWViewBeanInterface viewBean = new REOrdresVersementsViewBean();

        try {

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
            storeErrorRequest(viewBean, e, request);
        }

        this.saveViewBean(viewBean, request);
        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        forward(destination, request, response);
    }

    @Override
    protected void actionAfficherAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        FWViewBeanInterface viewBean;
        String destination;

        String cleComposee = request.getParameter("idEntity");
        String idOrdreVersement = cleComposee.split("-")[0];
        String provenance = cleComposee.split("-")[1];

        if ("CID".equals(provenance)) {
            viewBean = new RECompensationInterDecisionAjaxViewBean();
            ((RECompensationInterDecisionAjaxViewBean) viewBean).setIdOrdreVersement(idOrdreVersement);
            destination = REOrdresVersementsAction.PAGE_DETAIL_CID;
        } else if ("OV".equals(provenance)) {
            viewBean = new REOrdresVersementsDetailAjaxViewBean();
            ((REOrdresVersementsDetailAjaxViewBean) viewBean).setIdOrdreVersement(idOrdreVersement);
            destination = REOrdresVersementsAction.PAGE_DETAIL_ORDREVERSEMENT;
        } else {
            viewBean = new REOrdresVersementsAjaxViewBean();
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage("unknown command");
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        try {
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
        } catch (Exception e) {
            storeErrorRequest(viewBean, e, request);
        }

        this.saveViewBean(viewBean, request);
        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
        }

        forward(destination, request, response);
    }

    @Override
    protected void actionAjouterAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "/jade/ajax/templateAjax_afficherAJAX.jsp";
        FWViewBeanInterface viewBean = new RECompensationInterDecisionAjaxViewBean();

        try {
            JSPUtils.setBeanProperties(request, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());
        } catch (Exception ex) {
            storeErrorRequest(viewBean, ex, request);
        }

        this.saveViewBean(viewBean, request);
        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
        }

        forward(destination, request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        FWAction action = FWAction.newInstance(request.getParameter(FWServlet.USER_ACTION));

        if ("creerCIDAJAX".equals(action.getActionPart()) || "gererRestitutionAJAX".equals(action.getActionPart())) {

            IREOrdreVersementAjaxViewBean viewBean;
            String destination;

            if ("creerCIDAJAX".equals(action.getActionPart())) {
                viewBean = new RECompensationInterDecisionAjaxViewBean();
                destination = REOrdresVersementsAction.PAGE_DETAIL_CID;
            } else if ("gererRestitutionAJAX".equals(action.getActionPart())) {
                viewBean = new REGestionRestitutionAjaxViewBean();
                destination = REOrdresVersementsAction.PAGE_DETAIL_RESTITUTION;
            } else {
                throw new UnsupportedOperationException("unknown type of view bean");
            }

            try {
                JSPUtils.setBeanProperties(request, viewBean);
                viewBean = (IREOrdreVersementAjaxViewBean) dispatcher.dispatch(viewBean, getAction());
            } catch (Exception ex) {
                storeErrorRequest(viewBean, ex, request);
            }

            this.saveViewBean(viewBean, request);
            if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            }

            forward(destination, request, response);

        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    @Override
    protected void actionListerAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = REOrdresVersementsAction.PAGE_LISTE_ORDREVERSEMENT;
        FWViewBeanInterface viewBean = new REOrdresVersementsAjaxViewBean();

        try {

            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeLister(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, newAction);
        } catch (Exception e) {
            storeErrorRequest(viewBean, e, request);
        }

        this.saveViewBean(viewBean, request);
        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
        }

        forward(destination, request, response);
    }

    @Override
    protected void actionModifierAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "/jade/ajax/templateAjax_afficherAJAX.jsp";
        IREOrdreVersementAjaxViewBean viewBean;

        String provenance = request.getParameter("provenance");
        if ("OV".equals(provenance)) {
            viewBean = new REOrdresVersementsDetailAjaxViewBean();
        } else if ("CID".equals(provenance)) {
            viewBean = new RECompensationInterDecisionAjaxViewBean();
        } else if ("Restitution".equals(provenance)) {
            viewBean = new REGestionRestitutionAjaxViewBean();
        } else {
            throw new UnsupportedOperationException("provenance unknown");
        }

        try {
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = (IREOrdreVersementAjaxViewBean) mainDispatcher.dispatch(viewBean, getAction());

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
            storeErrorRequest(viewBean, e, request);
        }

        this.saveViewBean(viewBean, request);
        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
        }

        forward(destination, request, response);
    }

    @Override
    protected void actionSupprimerAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = "/jade/ajax/templateAjax_afficherAJAX.jsp";
        FWViewBeanInterface viewBean;

        String provenance = request.getParameter("provenance");
        if ("CID".equals(provenance)) {
            viewBean = new RECompensationInterDecisionAjaxViewBean();
        } else {
            throw new UnsupportedOperationException("can't delete this");
        }

        try {
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, getAction());

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
            storeErrorRequest(viewBean, e, request);
        }

        this.saveViewBean(viewBean, request);
        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
        }

        forward(destination, request, response);
    }

    private void storeErrorRequest(FWViewBeanInterface viewBean, Exception exception, HttpServletRequest request) {
        request.setAttribute("exception", exception);
        viewBean.setMessage(exception.toString());
        viewBean.setMsgType(FWViewBeanInterface.ERROR);
    }
}
