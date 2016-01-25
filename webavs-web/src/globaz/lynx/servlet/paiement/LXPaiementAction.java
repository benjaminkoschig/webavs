package globaz.lynx.servlet.paiement;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.lynx.db.paiement.LXPaiementViewBean;
import globaz.lynx.servlet.utils.LXVentilationActionUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LXPaiementAction extends FWDefaultServletAction {

    private static final String USERACTION_CHERCHER_PAIEMENT = "lynx.paiement.paiement.chercher";

    public LXPaiementAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher&_method=add&_back=sl&_valid=new&idJournal="
                + request.getParameter("idJournal") + "&idSociete=" + request.getParameter("idSociete")
                + "&forceNew=false";
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        FWAction action = FWAction.newInstance(LXPaiementAction.USERACTION_CHERCHER_PAIEMENT);
        return action.getApplicationPart() + "?userAction=" + action.getApplicationPart() + "."
                + action.getPackagePart() + "." + action.getClassPart() + ".chercher&selectedId="
                + request.getParameter("idOrdreGroupe") + "&idSociete=" + request.getParameter("idSociete")
                + "&idOrganeExecution=" + request.getParameter("idOrganeExecution") + "&idOrdreGroupe="
                + request.getParameter("idOrdreGroupe");
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        FWAction action = FWAction.newInstance(LXPaiementAction.USERACTION_CHERCHER_PAIEMENT);
        return "/" + action.getApplicationPart() + "?userAction=" + action.getApplicationPart() + "."
                + action.getPackagePart() + "." + action.getClassPart() + ".chercher";
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

        if (!isRequestForceNew(request) && isRetourDepuisPyxis(viewBean)) {
            servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp")
                    .forward(request, response);
        } else {
            actionAfficherLynx(session, request, response, mainDispatcher);
        }
    }

    /**
     * Action de lynx pour afficher une facture.
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws ServletException
     * @throws IOException
     */
    private void actionAfficherLynx(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            FWViewBeanInterface viewBean = null;

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                if (isRequestForceNew(request)) {
                    viewBean = new LXPaiementViewBean();
                } else {
                    viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
                }

                action.changeActionPart(FWAction.ACTION_NOUVEAU);
                ((LXPaiementViewBean) viewBean).setIdJournal(request.getParameter("idJournal"));
                ((LXPaiementViewBean) viewBean).setIdSociete(request.getParameter("idSociete"));
            } else {
                viewBean = new LXPaiementViewBean();

                JSPUtils.setBeanProperties(request, viewBean);
            }

            viewBean = mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination;

        try {
            String action = request.getParameter("userAction");

            FWViewBeanInterface viewBean = new LXPaiementViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            ((LXPaiementViewBean) viewBean).setVentilations(LXVentilationActionUtils.getVentilationsFromRequest(
                    request, false, ((LXPaiementViewBean) viewBean).getMaxRows()));

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(action));

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                ((LXPaiementViewBean) viewBean).setShowRows(((LXPaiementViewBean) viewBean).getVentilations().size());
                setSessionAttribute(session, "viewBean", viewBean);

                destination = _getDestAjouterSucces(session, request, response, viewBean);
                goSendRedirectWithoutParameters(destination, request, response);
                return;
            } else {
                ((LXPaiementViewBean) viewBean).setShowRows(0);
                setSessionAttribute(session, "viewBean", viewBean);

                destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination;

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            JSPUtils.setBeanProperties(request, viewBean);
            ((LXPaiementViewBean) viewBean).setVentilations(LXVentilationActionUtils.getVentilationsFromRequest(
                    request, true, ((LXPaiementViewBean) viewBean).getMaxRows()));
            ((LXPaiementViewBean) viewBean).setShowRows(((LXPaiementViewBean) viewBean).getVentilations().size());

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, action);

            setSessionAttribute(session, "viewBean", viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirectWithoutParameters(destination, request, response);
    }

    /**
     * La request contient-elle forceNew ?
     * 
     * @param request
     * @return
     */
    private boolean isRequestForceNew(HttpServletRequest request) {
        return ((request.getParameter("forceNew") != null) && (new Boolean(request.getParameter("forceNew"))
                .booleanValue()));
    }

    /**
     * L'action est-elle appelée en retour du module tiers ?
     * 
     * @param viewBean
     * @return
     */
    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof LXPaiementViewBean) && ((LXPaiementViewBean) viewBean)
                .isRetourDepuisPyxis());
    }

}
