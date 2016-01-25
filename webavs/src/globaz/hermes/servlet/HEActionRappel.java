package globaz.hermes.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.globall.db.BSession;
import globaz.hermes.db.gestion.HERappelViewBean;
import globaz.hermes.print.itext.HERappelDocument_Doc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//
public class HEActionRappel extends globaz.framework.controller.FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur HEActionRappel.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public HEActionRappel(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    /**
     * cette méthode passe bien dans le dispatcher et gère donc bien les droits. Cependant il serait préférable
     * d'instancier le process à travers un helper dédié qui se chargerait également de transmettre les paramètres du
     * viewbean au le helper
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            // récupère le viewBean
            HERappelViewBean crtViewBean = (globaz.hermes.db.gestion.HERappelViewBean) session.getAttribute("viewBean");
            HERappelDocument_Doc processPrint = new HERappelDocument_Doc();
            processPrint.setSession((BSession) mainDispatcher.getSession());
            String email = request.getParameter("email");
            String nbJours = request.getParameter("nbJours");
            processPrint.setEmail(email);
            processPrint.setNbJours(nbJours);
            processPrint.setForNumAVS(crtViewBean.getNumeroAvs());
            processPrint.setForCaisse(crtViewBean.getNumeroCaisse());
            processPrint.setForDateOrdre(crtViewBean.getDate());
            processPrint.setForMotif(crtViewBean.getMotif());
            processPrint = (HERappelDocument_Doc) mainDispatcher.dispatch(processPrint,
                    FWAction.newInstance(request.getParameter("userAction")));
            if (processPrint.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                try {
                    HERappelViewBean viewBean = (HERappelViewBean) session.getAttribute("viewBean");
                    viewBean.setMessage(processPrint.getMessage());
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    session.setAttribute("viewBean", viewBean);
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
                _destination = ERROR_PAGE;
            } else {
                _destination = getActionBack();
                // _destination = "/" + getAction().getApplicationPart() +
                // "?userAction=hermes.gestion.inputAnnonce.chercher";
            }
            // }
        } catch (Exception e) {
            e.printStackTrace();
            _destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    // protected void actionCustom(HttpSession session, HttpServletRequest
    // request, HttpServletResponse response, FWDispatcher dispatcher) throws
    // ServletException, IOException {
    // // super.actionCustom(session, request, response, dispatcher);
    // String _destination = UNDER_CONSTRUCTION_PAGE;
    // if (getAction().toString().endsWith("afficherImpression")) {
    // _destination = getRelativeURL(request, session) + "_de.jsp";
    // BSession bSession = (BSession) dispatcher.getSession();
    // HERappelViewBean viewBean = new HERappelViewBean();
    // viewBean.setSession(bSession);
    // session.setAttribute("viewBean", viewBean);
    // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
    // response);
    // }
    // }
}
