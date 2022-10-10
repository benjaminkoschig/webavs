package ch.globaz.eform.web.servlet;

import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.search.GFDaDossierSearch;
import ch.globaz.eform.constant.GFStatusDADossier;
import globaz.eform.vb.suivi.GFSuiviViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.prestation.ged.PRGedAffichageDossier;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GFSuiviServletAction extends FWDefaultServletAction {

    public final static String ACTION_PATH = "eform.suivi.suivi";
    public final static String ACTION_CHERCHER = "chercher";
    public final static String ACTION_STATUT = "statut";

    public GFSuiviServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
        try {
            JSPUtils.setBeanProperties(request, viewBean);

            mainDispatcher.dispatch(viewBean, getAction());
            session.setAttribute("viewBean", viewBean);
        } catch (Exception e) {
            JadeLogger.error("Failed to prepare viewBean for actionChercher", e);
        }

        super.actionChercher(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWDispatcher dispatcher) throws ServletException, IOException {
        String actionPart = getAction().getActionPart();
        if (GFMainServlet.ACTION_DOSSIER_GED.equals(actionPart)) {
            actionDossierGed(session, request, response, dispatcher);
        } else {
            String destination;

            // Définition de l'action custom standard pour l'application ARIES
            // Attention, si appel de custom action, on passe le paramètre "id" au lieu de "selectedId"

            try {
                FWAction action = FWAction.newInstance(request.getParameter("userAction"));

                // Récupération du viewBean depuis la session
                FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, dispatcher.getPrefix());
                String id = request.getParameter("selectedId");
                ((GFSuiviViewBean) viewBean).getDaDossier().setId(id);
                if (actionPart.equals(ACTION_STATUT)) {
                    String statut = request.getParameter("statut");
                    ((GFSuiviViewBean) viewBean).getDaDossier().setStatus(GFStatusDADossier.getByCode(statut).getCodeSystem());
                }
                // Copie des propriétés
                JSPUtils.setBeanProperties(request, viewBean);

                // Traitement
                viewBean = dispatcher.dispatch(viewBean, action);
                session.setAttribute(FWServlet.VIEWBEAN, viewBean);

                // Choix de la destination avec prise en compte des éventuels erreurs
                boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

                if (goesToSuccessDest) {
                    destination = _getDestChercherSucces(session, request, response, viewBean);
                } else {
                    destination = _getDestChercherEchec(session, request, response, viewBean);
                }

            } catch (Exception e) {
                JadeThread.logError(this.getClass().getName(), e.getMessage());
                destination = FWDefaultServletAction.ERROR_PAGE;
            }

            // Redirection vers la destination
            goSendRedirect(destination, request, response);

        }
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
                                     HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this.getActionFullURL() + ".afficher";
    }

    protected String _getDestChercherSucces(HttpSession session, HttpServletRequest request,
                                            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this.getActionFullURL() + ".chercher";
    }

    protected String _getDestChercherEchec(HttpSession session, HttpServletRequest request,
                                           HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this._getDestEchec(session, request, response, viewBean);
    }

    private void actionDossierGed(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                  FWDispatcher mainDispatcher) {
        GFSuiviViewBean viewBean = new GFSuiviViewBean();
        try {
            PRGedAffichageDossier.actionAfficherDossierGed(session, request, response, mainDispatcher, viewBean);
        } catch (JadeServiceLocatorException | JadeServiceActivatorException | NullPointerException |
                 ClassCastException | JadeClassCastException | IOException | ServletException e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }
    }
}
