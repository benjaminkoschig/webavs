package globaz.osiris.servlet.action.lettrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.IFWActionHandler;
import globaz.framework.servlets.FWServlet;
import globaz.osiris.application.CAApplication;
import globaz.osiris.vb.lettrage.CAChercherPlageViewBean;
import globaz.osiris.vb.lettrage.CALettragePlagesViewBean;
import globaz.osiris.vb.lettrage.CALettrageViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * Gestion des actions liées au letrrage de masse.
 * 
 * Ecran de calcul des plages: osiris.lettrage.plages.afficher -> Navigation vers l'écran de proposition de plages
 * osiris.lettrage.plages.query -> req ajax, dertermine les plages de n° de compte annexe en fonction du nombre de plage
 * passé en paramètre
 * 
 * Ecran de lettrage TODO : mettre a jour les noms d'actions pyxis.test.lettrage.afficher (action par défaut) ->
 * Navigation vers écran principale de lettrage pyxis.test.lettrage.definir -> req ajax, recherche des comptes annexes
 * "lettrable" (év. pour une plage d'affilié) pyxis.test.lettrage.querySection -> req ajax, renvoi les données pour
 * afficher les sections d'un compte annexe pyxis.test.lettrage.doLettrage -> req ajax, Execution du lettrage pour un
 * compte pyxis.test.lettrage.exlureSection -> req ajax, exclure une section du lettrage
 * pyxis.test.lettrage.inclureSection -> req ajax, inclure une section du lettrage
 */

public class CAActionLettrage implements IFWActionHandler {

    /*
     * Action "Normal" -> Affichage vers écran de lettre ou écran de plages
     */
    private void _afficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWAction action, FWViewBeanInterface viewBean, String page)
            throws ServletException, IOException {
        viewBean = mainDispatcher.dispatch(viewBean, action);
        request.setAttribute("viewBean", viewBean);
        session.setAttribute("viewBean", viewBean); // utilisé par la page d'erreur
        /*
         * choix destination
         */
        String _destination = "";
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } else {
            _destination = "/osirisRoot"
                    + FWServlet.getLanguageTreeForApplication(CAApplication.DEFAULT_APPLICATION_OSIRIS, session);
            _destination += "/lettrage/" + page + "?_method=add";
        }
        request.getRequestDispatcher(_destination).forward(request, response);
    }

    /*
     * Actions AJAX
     */
    private void _ajax(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWAction action, CALettrageViewBean viewBean) throws ServletException,
            IOException {

        viewBean = (CALettrageViewBean) mainDispatcher.dispatch(viewBean, action);
        String resp = null;
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            resp = "KO," + viewBean.getMessage();
        } else {
            resp = viewBean.getResponse().toString();
        }
        response.getOutputStream().write(resp.getBytes("UTF-8"));
    }

    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {

        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);
        FWDispatcher mainDispatcher = (FWDispatcher) mainController;

        if ("osiris.lettrage.plages.definirPlages".equals(action)) {
            _afficher(session, request, response, mainDispatcher, _action, new CALettragePlagesViewBean(),
                    "lettragePlages_de.jsp");
        } else if ("osiris.lettrage.main.display".equals(action)) {
            _afficher(session, request, response, mainDispatcher, _action, new CALettrageViewBean(), "lettrage_de.jsp");

        } else if ("osiris.lettrage.plages.listerPlages".equals(action)) {
            CAChercherPlageViewBean viewBean = new CAChercherPlageViewBean();
            viewBean.setRole(request.getParameter("role"));
            viewBean.setNbPlages(request.getParameter("nbPlages"));
            _ajax(session, request, response, mainDispatcher, _action, viewBean);

            /*
             * MODIF OCA
             */
        } else if ("osiris.lettrage.main.query".equals(action) || "osiris.lettrage.main.querySection".equals(action)
                || "osiris.lettrage.main.doLettrage".equals(action)
                || "osiris.lettrage.main.exlureSection".equals(action)
                || "osiris.lettrage.main.inclureSection".equals(action)
                || "osiris.lettrage.main.versementSection".equals(action)
                || "osiris.lettrage.main.annulerVersementSection".equals(action)
                || "osiris.lettrage.main.infoSection".equals(action)
                || "osiris.lettrage.main.reportSection".equals(action)) {

            String from = request.getParameter("from");
            String to = request.getParameter("to");
            String role = request.getParameter("role");
            String filter = request.getParameter("filter");
            String idCompteAnnexe = request.getParameter("idCompteAnnexe");
            String idSection = request.getParameter("idSection");
            String params = request.getParameter("p"); // doLettrage
            String bsParams = request.getParameter("bs"); // doLettrage

            String idExclusion = request.getParameter("idExclusion"); // inclureSection
            String montantVersement = request.getParameter("montantVersement"); // versementSection
            String category = request.getParameter("category"); // versementSection
            String idOrdre = request.getParameter("idOrdre"); // annulerVersementSection

            String idModeCompensation = request.getParameter("idModeCompensation");

            String comment = "";
            if (request.getParameter("comment") != null) {
                // un peu de décodage car encodé avec envoi ajax... voir lettrage_de.jsp function report()
                comment = new String(request.getParameter("comment").getBytes("ISO-8859-1"), "UTF-8").trim();
            }

            CALettrageViewBean viewBean = new CALettrageViewBean();
            viewBean.setRole(role);
            viewBean.setFilter(filter);
            viewBean.setIdCompteAnnexe(idCompteAnnexe);
            viewBean.setIdSection(idSection);
            viewBean.setFromCompteAnnexe(from);
            viewBean.setToCompteAnnexe(to);
            viewBean.setParams(params);
            viewBean.setBsParams(bsParams);
            viewBean.setIdExclusion(idExclusion);
            viewBean.setMontantVersement(montantVersement);
            viewBean.setCategory(category);
            viewBean.setIdOrdreEnAttente(idOrdre);
            viewBean.setIdModeCompensation(idModeCompensation);
            viewBean.setComment(comment);
            viewBean.setContextPath(request.getContextPath()); // je le fais de cette manière en attendant de bien
                                                               // séparer le html des requêtes dans CAMainHelper.

            _ajax(session, request, response, mainDispatcher, _action, viewBean);

        } else {
            throw new ServletException("Unknown action : " + action);
        }

    }

}