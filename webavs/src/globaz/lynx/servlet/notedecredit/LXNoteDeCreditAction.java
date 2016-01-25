package globaz.lynx.servlet.notedecredit;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.lynx.db.notedecredit.LXNoteDeCreditEncaisserViewBean;
import globaz.lynx.db.notedecredit.LXNoteDeCreditViewBean;
import globaz.lynx.servlet.utils.LXVentilationActionUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LXNoteDeCreditAction extends FWDefaultServletAction {

    private static final String JSP_ENCAISSER_DE = "Encaisser_de.jsp";

    private static final String USERACTION_CHERCHER_NOTEDECREDIT = "lynx.notedecredit.noteDeCredit.chercher";

    /**
     * Constructeur
     */
    public LXNoteDeCreditAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getActionFullURL() + ".afficher&_method=add&_back=sl&_valid=new&idJournal="
                + request.getParameter("idJournal") + "&idSociete=" + request.getParameter("idSociete")
                + "&forceNew=false";
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        FWAction action = FWAction.newInstance(LXNoteDeCreditAction.USERACTION_CHERCHER_NOTEDECREDIT);
        return action.getApplicationPart() + "?userAction=" + action.getApplicationPart() + "."
                + action.getPackagePart() + "." + action.getClassPart() + ".chercher&selectedId="
                + request.getParameter("idJournal") + "&idSociete=" + request.getParameter("idSociete") + "&idJournal="
                + request.getParameter("idJournal");
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        FWAction action = FWAction.newInstance(LXNoteDeCreditAction.USERACTION_CHERCHER_NOTEDECREDIT);
        return "/" + action.getApplicationPart() + "?userAction=" + action.getApplicationPart() + "."
                + action.getPackagePart() + "." + action.getClassPart() + ".chercher";
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            FWViewBeanInterface viewBean = null;

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                if ((request.getParameter("forceNew") != null)
                        && !(new Boolean(request.getParameter("forceNew")).booleanValue())) {
                    viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
                } else {
                    viewBean = new LXNoteDeCreditViewBean();
                }

                action.changeActionPart(FWAction.ACTION_NOUVEAU);
                ((LXNoteDeCreditViewBean) viewBean).setIdJournal(request.getParameter("idJournal"));
                ((LXNoteDeCreditViewBean) viewBean).setIdSociete(request.getParameter("idSociete"));
            } else {
                viewBean = new LXNoteDeCreditViewBean();

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

            FWViewBeanInterface viewBean = new LXNoteDeCreditViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            ((LXNoteDeCreditViewBean) viewBean).setVentilations(LXVentilationActionUtils.getVentilationsFromRequest(
                    request, false, ((LXNoteDeCreditViewBean) viewBean).getMaxRows()));

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, FWAction.newInstance(action));

            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                ((LXNoteDeCreditViewBean) viewBean).setShowRows(((LXNoteDeCreditViewBean) viewBean).getVentilations()
                        .size());
                setSessionAttribute(session, "viewBean", viewBean);

                destination = _getDestAjouterSucces(session, request, response, viewBean);
                goSendRedirectWithoutParameters(destination, request, response);
                return;
            } else {
                ((LXNoteDeCreditViewBean) viewBean).setShowRows(0);
                setSessionAttribute(session, "viewBean", viewBean);

                destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
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
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {

        if ("encaisser".equals(getAction().getActionPart())) {
            actionEncaisser(session, request, response, mainDispatcher);
        }
    }

    private void actionEncaisser(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {

            LXNoteDeCreditEncaisserViewBean viewBean = new LXNoteDeCreditEncaisserViewBean();

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setSession((BSession) mainDispatcher.getSession());

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURL(request, session) + LXNoteDeCreditAction.JSP_ENCAISSER_DE;
        } catch (Exception ex) {
            ex.printStackTrace();
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
            ((LXNoteDeCreditViewBean) viewBean).setVentilations(LXVentilationActionUtils.getVentilationsFromRequest(
                    request, true, ((LXNoteDeCreditViewBean) viewBean).getMaxRows()));
            ((LXNoteDeCreditViewBean) viewBean).setShowRows(((LXNoteDeCreditViewBean) viewBean).getVentilations()
                    .size());

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

}
