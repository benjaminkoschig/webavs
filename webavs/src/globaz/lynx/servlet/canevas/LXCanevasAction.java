package globaz.lynx.servlet.canevas;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.log.JadeLogger;
import globaz.lynx.db.canevas.LXCanevasViewBean;
import globaz.lynx.db.facture.LXFactureViewBean;
import globaz.lynx.db.journal.LXChoixCanevasViewBean;
import globaz.lynx.servlet.utils.LXVentilationActionUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LXCanevasAction extends FWDefaultServletAction {

    // private static final String USERACTION_CHERCHER_CANEVAS = "lynx.canevas.canevas.chercher";

    /**
     * Le cosntructeur
     * 
     * @param servlet
     */
    public LXCanevasAction(FWServlet servlet) {
        super(servlet);
    }

    // /**
    // * L'action est-elle appelée en retour du module tiers ?
    // * @param viewBean
    // * @return
    // */
    // private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
    // return (viewBean != null && viewBean instanceof LXCanevasViewBean &&
    // ((LXCanevasViewBean) viewBean).isRetourDepuisPyxis());
    // }
    //

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            FWViewBeanInterface viewBean = null;

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                viewBean = new LXCanevasViewBean();
                action.changeActionPart(FWAction.ACTION_NOUVEAU);
            } else {
                viewBean = new LXCanevasViewBean();
                JSPUtils.setBeanProperties(request, viewBean);
            }

            viewBean = mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionAjouter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination;
        FWViewBeanInterface viewBean = new LXCanevasViewBean();

        try {
            String action = request.getParameter("userAction");

            JSPUtils.setBeanProperties(request, viewBean);
            ((LXCanevasViewBean) viewBean).setVentilations(LXVentilationActionUtils.getCanevasVentilationsFromRequest(
                    request, false));

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(action));

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                ((LXCanevasViewBean) viewBean).setShowRows(((LXCanevasViewBean) viewBean).getVentilations().size());
                setSessionAttribute(session, "viewBean", viewBean);

                destination = _getDestAjouterSucces(session, request, response, viewBean);
                goSendRedirectWithoutParameters(destination, request, response);
                return;
            } else {
                ((LXCanevasViewBean) viewBean).setShowRows(0);
                setSessionAttribute(session, "viewBean", viewBean);

                destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            // viewBean.setMessage(e.toString());
            // viewBean.setMsgType(FWViewBean.ERROR);
            // setSessionAttribute(session, "viewBean", viewBean);
            JadeLogger.error(this, e.toString());
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        if ("utiliser".equals(getAction().getActionPart())) {
            actionUtiliser(session, request, response, dispatcher);
        }
    }

    /*
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination;
        FWViewBeanInterface viewBean = new LXCanevasViewBean();

        try {
            String action = request.getParameter("userAction");

            JSPUtils.setBeanProperties(request, viewBean);
            ((LXCanevasViewBean) viewBean).setVentilations(LXVentilationActionUtils.getCanevasVentilationsFromRequest(
                    request, false));

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(action));

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                ((LXCanevasViewBean) viewBean).setShowRows(((LXCanevasViewBean) viewBean).getVentilations().size());
                setSessionAttribute(session, "viewBean", viewBean);

                destination = _getDestModifierSucces(session, request, response, viewBean);
                goSendRedirectWithoutParameters(destination, request, response);
                return;
            } else {
                ((LXCanevasViewBean) viewBean).setShowRows(0);
                setSessionAttribute(session, "viewBean", viewBean);

                destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }

    /**
     * Permet la création d'une facture depuis un canevas
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @throws ServletException
     * @throws IOException
     */
    protected final void actionUtiliser(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        String destination = "";
        LXChoixCanevasViewBean viewBean = (LXChoixCanevasViewBean) session.getAttribute("viewBean");

        try {

            JSPUtils.setBeanProperties(request, viewBean);

            LXFactureViewBean factureViewBean = new LXFactureViewBean();
            factureViewBean.setIdOperation(viewBean.getIdOperationCanevas());
            factureViewBean.setIdSection(viewBean.getIdSectionCanevas());
            factureViewBean.setIdFournisseur(viewBean.getIdFournisseur());
            factureViewBean.setIdSociete(viewBean.getIdSociete());
            factureViewBean.setIdJournal(viewBean.getIdJournal());
            factureViewBean.setMontant(viewBean.getMontant());
            factureViewBean.setLibelle(viewBean.getLibelle());

            factureViewBean = (LXFactureViewBean) dispatcher.dispatch(factureViewBean, getAction());
            setSessionAttribute(session, "viewBean", factureViewBean);

            destination = "/" + getAction().getApplicationPart() + "Root/"
                    + FWDefaultServletAction.getIdLangueIso(session) + "/facture/facture_de.jsp";

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}
