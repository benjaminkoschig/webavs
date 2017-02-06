/*
 * Créé le 17 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.servlet.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.phenix.db.communications.CPRejets;
import globaz.phenix.db.communications.CPRejetsListViewBean;
import globaz.phenix.db.communications.CPRejetsViewBean;
import globaz.phenix.listes.excel.CPListeRejetsProcess;
import globaz.phenix.process.communications.CPProcessEnvoyerRejets;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPActionRejets extends FWDefaultServletAction {

    public CPActionRejets(FWServlet servlet) {
        super(servlet);
    }

    public void _actionAbandonner(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        String _destination = "";
        CPRejetsViewBean viewBean = new CPRejetsViewBean();

        String[] listIdRetour = request.getParameterValues("listIdRetour");

        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            if (listIdRetour == null) {
                viewBean.setIdRejets(request.getParameter("selectedId"));
                dispatcher.dispatch(viewBean, getAction());
            } else // On boucle sur la liste des ids
            {
                for (String id : listIdRetour) {
                    viewBean.setIdRejets(id);
                    dispatcher.dispatch(viewBean, getAction());
                }
            }

        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        _destination = "/phenix?userAction=phenix.communications.rejets.chercher";
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * 
     *             HACK : Cette méthode ne fait rien du tout si ce n'est afficher l'écran de lancement du process
     *             concerné Tentative de modifier l'action en ".afficher" à la place de ".receptionner" dans le fichier
     *             "PhenixMenu" = ECHEC Pour contourner ce problème on fait appel à l'actionAfficher dans l'action
     *             custom
     * 
     */
    protected void _actionAfficherImpression(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            CPRejetsViewBean viewBean = new CPRejetsViewBean();
            viewBean.setSession((BSession) mainDispatcher.getSession());
            // Check les droits ;-)
            mainDispatcher.dispatch(viewBean, getAction());
            session.setAttribute("viewBean", viewBean);

            _destination = getRelativeURLwithoutClassPart(request, session) + "rejetsImprimer_de.jsp";

        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }
        /*
         * affiche la prochaine page
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        // super.actionAfficher(session, request, response, mainDispatcher);
    }

    private void _actionChangerStatus(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        String[] listIdRetour = request.getParameterValues("listIdRetour");

        try {
            CPRejetsViewBean viewBean = new CPRejetsViewBean();
            // Retour de parametres
            String selectedId = request.getParameter("selectedId");

            String subAction = request.getParameter("subAction");
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            CPRejets rejet = new CPRejets();

            if (listIdRetour == null) {
                rejet.changerStatus(bSession, selectedId);
            } else {
                String etat = "";

                if ("TRAITER".equals(subAction)) {
                    etat += CPRejets.CS_ETAT_TRAITE;
                } else {
                    etat += CPRejets.CS_ETAT_NON_TRAITE;
                }
                rejet.changerStatusMultiple(bSession, listIdRetour, etat);
            }

            mainDispatcher.dispatch(viewBean, getAction());

            _destination = "/phenix?userAction=phenix.communications.rejets.chercher";
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _destination + "&_valid=fail";
            } else {
                _destination = _destination + "&process=launched";
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * affiche la prochaine page
         */
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        goSendRedirect(_destination, request, response);
    }

    private void _actionEnvoiSedexAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {

        String destination = null;
        try {

            CPRejetsListViewBean viewBean = new CPRejetsListViewBean();
            CPProcessEnvoyerRejets process = new CPProcessEnvoyerRejets();

            String[] listIdRetour = request.getParameterValues("listIdRetour");
            if (listIdRetour == null) {
                listIdRetour = creationListeIdRetourDapresCriteresSelectionSansNonTraiterAbandonner(
                        (BSession) mainDispatcher.getSession(), session, request);
            } else {
                listIdRetour = supprimerIdsNonTraiterAbandonnerListeIdRetour((BSession) mainDispatcher.getSession(),
                        listIdRetour);
            }

            viewBean.setListIdRetour(listIdRetour);
            viewBean.setSession((BSession) mainDispatcher.getSession());
            process.setSession((BSession) mainDispatcher.getSession());
            process.setListIdRetour(listIdRetour);
            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, "viewBean", viewBean);

            destination = getRelativeURLwithoutClassPart(request, session) + "processEnvoiRejets_de.jsp";
        } catch (Exception ex) {
            ex.printStackTrace();
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private void _actionEnvoiSedexExecuter(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String destination = getActionFullURL() + ".envoiSedex";
        try {

            CPRejetsListViewBean viewBean = new CPRejetsListViewBean();
            CPProcessEnvoyerRejets process = new CPProcessEnvoyerRejets();

            String[] listIdRetour = request.getParameterValues("listIdRetour");
            if (listIdRetour == null) {
                listIdRetour = creationListeIdRetourDapresCriteresSelectionSansNonTraiterAbandonner(
                        (BSession) dispatcher.getSession(), session, request);
            } else {
                listIdRetour = supprimerIdsNonTraiterAbandonnerListeIdRetour((BSession) dispatcher.getSession(),
                        listIdRetour);
            }
            viewBean.setListIdRetour(listIdRetour);
            viewBean.setSession((BSession) dispatcher.getSession());
            process.setEMailAddress(request.getParameter("eMailAddress"));
            process.setSession((BSession) dispatcher.getSession());
            process.setListIdRetour(listIdRetour);
            BProcessLauncher.start(process);
            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, "viewBean", viewBean);

            // destination = getRelativeURLwithoutClassPart(request, session) +
            // "processEnqueterEnMasse_de.jsp";
            destination += "&process=launched";
        } catch (Exception ex) {
            ex.printStackTrace();
            destination += "&_valid=fail";
        }

        goSendRedirect(destination, request, response);
    }

    private void _actionImprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        try {
            CPRejetsListViewBean viewBean = new CPRejetsListViewBean();
            JSPUtils.setBeanProperties(request, viewBean);
            CPListeRejetsProcess process = new CPListeRejetsProcess();
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            process.setSession((globaz.globall.db.BSession) bSession);
            process.setManager(viewBean);
            process.setEMailAddress(viewBean.geteMailAddress());
            BProcessLauncher.start(process);
            // process.executeProcess();

            mainDispatcher.dispatch(viewBean, getAction());

            _destination = "/phenix?userAction=phenix.communications.rejets.afficherImpression";
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = _destination + "&_valid=fail";
            } else {
                _destination = _destination + "&process=launched";
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * affiche la prochaine page
         */
        // servlet.getServletContext().getRequestDispatcher(_destination).forward(request,
        // response);
        goSendRedirect(_destination, request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if ("afficherImpression".equals(getAction().getActionPart())) {
            _actionAfficherImpression(session, request, response, dispatcher);
        } else if ("abandonner".equals(getAction().getActionPart())) {
            _actionAbandonner(session, request, response, dispatcher);
        } else if ("changerStatus".equals(getAction().getActionPart())) {
            _actionChangerStatus(session, request, response, dispatcher);
        } else if ("imprimer".equals(getAction().getActionPart())) {
            _actionImprimer(session, request, response, dispatcher);
        } else if ("envoiSedex".equals(getAction().getActionPart())) {
            _actionEnvoiSedexAfficher(session, request, response, dispatcher);
        } else if ("envoiSedexExecuter".equals(getAction().getActionPart())) {
            _actionEnvoiSedexExecuter(session, request, response, dispatcher);
        } else {
            // on a demandé un page qui n'existe pas, si rien n'est renseigné,
            // on arrivera sur une page blanche
            // alors que dans ce cas nous allons sur la page under construction
            servlet.getServletContext().getRequestDispatcher(FWDefaultServletAction.UNDER_CONSTRUCTION_PAGE)
                    .forward(request, response);
        }
    }

    private String[] creationListeIdRetourDapresCriteresSelectionSansNonTraiterAbandonner(BSession session,
            HttpSession httpSession, HttpServletRequest request) {
        String[] listIdRetour = null;

        List<String> newList = new ArrayList<String>();

        String idRejet = request.getParameter("idRejet");
        if (httpSession.getAttribute("listViewBean") != null) {
            if (CPRejetsListViewBean.class.equals(httpSession.getAttribute("listViewBean").getClass())) {
                CPRejetsListViewBean manager = (CPRejetsListViewBean) httpSession.getAttribute("listViewBean");

                try {
                    manager.setForIdRejets(idRejet);
                    manager.changeManagerSize(BManager.SIZE_NOLIMIT);
                    manager.find();
                    listIdRetour = new String[manager.size()];
                } catch (Exception e) {
                    return null;
                }
                for (int i = 0; i < manager.size(); i++) {
                    CPRejetsViewBean rejet = (CPRejetsViewBean) manager.getEntity(i);
                    if (!CPRejets.CS_ETAT_NON_TRAITE.equals(rejet.getEtat())
                            && !CPRejets.CS_ETAT_ABANDONNE.equals(rejet.getEtat())) {
                        newList.add(rejet.getIdRejets());
                    }
                }
            }
            listIdRetour = newList.toArray(new String[0]);

            return listIdRetour;
        } else {
            return null;
        }
    }

    private String[] supprimerIdsNonTraiterAbandonnerListeIdRetour(BSession session, String[] listIdRetour)
            throws Exception {
        List<String> newList = new ArrayList<String>();

        for (int i = 0; i < listIdRetour.length; i++) {
            CPRejetsViewBean rejet = new CPRejetsViewBean();
            rejet.setSession(session);
            rejet.setIdRejets(listIdRetour[i]);

            rejet.retrieve();

            if (!rejet.isNew()) {
                if (!CPRejets.CS_ETAT_NON_TRAITE.equals(rejet.getEtat())
                        && !CPRejets.CS_ETAT_ABANDONNE.equals(rejet.getEtat())) {
                    newList.add(rejet.getIdRejets());
                }
            }
        }

        return newList.toArray(new String[0]);
    }
}
