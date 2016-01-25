/*
 * Créé le 29 juin 07
 */

package globaz.corvus.servlet;

import globaz.corvus.vb.historiques.REHistoriqueRentesJoinTiersListViewBean;
import globaz.corvus.vb.historiques.REHistoriqueRentesJoinTiersViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWRequestActionAdapter;
import globaz.framework.controller.FWScenarios;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * 
 * @author SCR
 * 
 */

public class REHistoriqueRentesAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public REHistoriqueRentesAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    public void actionActiverEnvoiAcor(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        REHistoriqueRentesJoinTiersViewBean vb = new REHistoriqueRentesJoinTiersViewBean();
        vb.setSession((BSession) mainDispatcher.getSession());
        vb.setIdHistorique(request.getParameter("idHistorique"));
        vb.setIdTiersRequerant(request.getParameter("idTierRequerant"));
        vb.setIdTiersIn(request.getParameter("idTiersIn"));
        vb = (REHistoriqueRentesJoinTiersViewBean) mainDispatcher.dispatch(vb, getAction());
        vb.reloadHistorique(false);
        request.setAttribute("viewBean", vb);
        String _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), null);
        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";
        try {
            FWViewBeanInterface viewBean = null;

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                getAction().changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String isChangeBeneficaire = request.getParameter("changeBeneficiaire");
            // On a changé de bénéficiaire, on maj le nouveau, tout en gardant
            // les info de l'ancien bean!!!
            if ("true".equals(isChangeBeneficaire)) {
                viewBean = loadViewBean(session);

                boolean found = false;
                String idTiersRequerant = request.getParameter("idTierRequerant");
                String idTiers = request.getParameter("idTiers");

                if (idTiers != null && idTiers.equals(idTiersRequerant)) {
                    found = true;
                }

                if (!found) {
                    String ls = request.getParameter("idTiersIn");
                    if (ls != null) {
                        StringTokenizer tokenizer = new StringTokenizer(ls, ",");
                        while (tokenizer.hasMoreElements()) {
                            String token = tokenizer.nextToken();
                            if (token != null && token.trim().equals(idTiers)) {
                                found = true;
                                break;
                            }
                        }
                    }
                }

                if (!found) {
                    // Seul les historique non référencé par une RA peuvent
                    // avoir le champ NSS modifié.
                    if (JadeStringUtil.isBlankOrZero(((REHistoriqueRentesJoinTiersViewBean) viewBean)
                            .getIdRenteAccordee())) {
                        ((REHistoriqueRentesJoinTiersViewBean) viewBean)
                                .setModeAffichage(REHistoriqueRentesJoinTiersViewBean.MODE_AFFICHAGE_UPDATE);
                    } else {
                        ((REHistoriqueRentesJoinTiersViewBean) viewBean).setModeAffichage("");
                    }
                    ((REHistoriqueRentesJoinTiersViewBean) viewBean).setIdTiers(idTiersRequerant);
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(((BSession) mainDispatcher.getSession()).getLabel("MBR_IS_NOT_FAMILY_MEMBER")
                            + " # " + idTiers);
                    _destination = _getDestAjouterEchec(session, request, response, viewBean);
                    goSendRedirect(_destination, request, response);
                    if (true) {
                        return;
                    }
                } else {
                    ((REHistoriqueRentesJoinTiersViewBean) viewBean).setModeAffichage("");
                }

                ((REHistoriqueRentesJoinTiersViewBean) viewBean).setIdTiersRequerant(request
                        .getParameter("idTierRequerant"));
                ((REHistoriqueRentesJoinTiersViewBean) viewBean).setIdTiers(request.getParameter("idTiers"));
            } else {
                String selectedId = request.getParameter("selectedId");

                viewBean = new REHistoriqueRentesJoinTiersViewBean();
                ((REHistoriqueRentesJoinTiersViewBean) viewBean).setSession((BSession) mainDispatcher.getSession());
                ((REHistoriqueRentesJoinTiersViewBean) viewBean).setIdHistorique(selectedId);
                ((REHistoriqueRentesJoinTiersViewBean) viewBean).setIdTiersIn(request.getParameter("idTiersIn"));
                ((REHistoriqueRentesJoinTiersViewBean) viewBean).setIdTiersRequerant(request
                        .getParameter("idTierRequerant"));

                viewBean = beforeAfficher(session, request, response, viewBean);
                viewBean = mainDispatcher.dispatch(viewBean, getAction());

            }
            /*
             * appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            /*
             * recuperation du bean depuis la session
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            String idTiersRequerant = request.getParameter("idTierRequerant");
            String idTiers = request.getParameter("idTiers");
            ((REHistoriqueRentesJoinTiersViewBean) viewBean).setIdTiers(idTiers);
            ((REHistoriqueRentesJoinTiersViewBean) viewBean).setIdTiersRequerant(idTiersRequerant);
            ((REHistoriqueRentesJoinTiersViewBean) viewBean).setIsModifie(Boolean.TRUE);
            ((REHistoriqueRentesJoinTiersViewBean) viewBean).setIsPrendreEnCompteCalculAcor(Boolean.TRUE);

            boolean found = false;
            if (idTiers != null && idTiers.equals(idTiersRequerant)) {
                found = true;
            }

            if (!found) {
                String ls = request.getParameter("idTiersIn");
                if (ls != null) {
                    StringTokenizer tokenizer = new StringTokenizer(ls, ",");
                    while (tokenizer.hasMoreElements()) {
                        String token = tokenizer.nextToken();
                        if (token != null && token.trim().equals(idTiers)) {
                            found = true;
                            break;
                        }
                    }
                }
            }

            if (!found) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(((BSession) mainDispatcher.getSession()).getLabel("MBR_IS_NOT_FAMILY_MEMBER")
                        + " # " + idTiers);
                _destination = _getDestAjouterEchec(session, request, response, viewBean);
                goSendRedirect(_destination, request, response);
                if (true) {
                    return;
                }
            }

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix de la destination
             */
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                _destination = _getDestAjouterSucces(session, request, response, viewBean);
            } else {
                _destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(_destination, request, response);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        REHistoriqueRentesJoinTiersViewBean vb = new REHistoriqueRentesJoinTiersViewBean();
        vb.setSession((BSession) mainDispatcher.getSession());
        vb.setIdTiersRequerant(request.getParameter("idTierRequerant"));

        if ("false".equals(request.getParameter("reloadHistory"))) {
            vb.reloadHistorique(false);
        }
        // by default
        else {
            vb.reloadHistorique(true);
        }

        vb = (REHistoriqueRentesJoinTiersViewBean) mainDispatcher.dispatch(vb, getAction());

        request.setAttribute("viewBean", vb);
        String _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), null);
        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    public void actionDesactiverEnvoiAcor(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        REHistoriqueRentesJoinTiersViewBean vb = new REHistoriqueRentesJoinTiersViewBean();
        vb.setSession((BSession) mainDispatcher.getSession());
        vb.setIdHistorique(request.getParameter("idHistorique"));
        vb.setIdTiersRequerant(request.getParameter("idTierRequerant"));
        vb.setIdTiersIn(request.getParameter("idTiersIn"));
        vb.reloadHistorique(false);

        vb = (REHistoriqueRentesJoinTiersViewBean) mainDispatcher.dispatch(vb, getAction());

        request.setAttribute("viewBean", vb);
        String _destination = FWScenarios.getInstance().getDestination(
                (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                new FWRequestActionAdapter().adapt(request), null);
        if (JadeStringUtil.isBlank(_destination)) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionLister(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";
        try {

            REHistoriqueRentesJoinTiersListViewBean viewBean = new REHistoriqueRentesJoinTiersListViewBean();
            viewBean.setSession((BSession) mainDispatcher.getSession());

            String listIdTiers = request.getParameter("idTiersIn");
            viewBean.setForIdTiersIn(listIdTiers);

            viewBean = (REHistoriqueRentesJoinTiersListViewBean) beforeLister(session, request, response, viewBean);
            viewBean = (REHistoriqueRentesJoinTiersListViewBean) mainDispatcher.dispatch(viewBean, getAction());
            request.setAttribute("viewBean", viewBean);

            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);

            _destination = getRelativeURL(request, session) + "_rcListe.jsp";

        } catch (Exception e) {

            _destination = ERROR_PAGE;

        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";
        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);
            /*
             * recupération du bean depuis la sesison
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */
            ((REHistoriqueRentesJoinTiersViewBean) viewBean).setIsModifie(Boolean.TRUE);
            ((REHistoriqueRentesJoinTiersViewBean) viewBean).reloadHistorique(false);

            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);

            request.setAttribute("viewBean", viewBean);
            _destination = FWScenarios.getInstance().getDestination(
                    (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
                    new FWRequestActionAdapter().adapt(request), null);
            if (JadeStringUtil.isBlank(_destination)) {
                _destination = getRelativeURL(request, session) + "_rc.jsp";
            }
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

    }

}
