package ch.globaz.eform.web.servlet;

import ch.globaz.eform.utils.GFFileUtils;
import globaz.eform.vb.formulaire.GFFormulaireViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GFFormulaireServletAction extends FWDefaultServletAction {
    public GFFormulaireServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    public final static String PATH_EFORM = "eform.formulaire.formulaire";
    public final static String ACTION_TELECHARGER = "telecharger";
    public final static String ACTION_CHANGE_STATUT = "statut";

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
        try {

            JSPUtils.setBeanProperties(request, viewBean);

            mainDispatcher.dispatch(viewBean, getAction());

        } catch (Exception e) {
            JadeLogger.error("Failed to prepare viewBean for actionChercher", e);
        }

        super.actionChercher(session, request, response, mainDispatcher);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
                                     HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this.getActionFullURL() + ".afficher";
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                FWDispatcher dispatcher) throws ServletException, IOException {
        String actionPart = getAction().getActionPart();
        String destination = null;

        // Définition de l'action custom standard pour l'application ARIES
        // Attention, si appel de custom action, on passe le paramètre "id" au lieu de "selectedId"

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            // Récupération du viewBean depuis la session
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, dispatcher.getPrefix());
            String id = request.getParameter("selectedId");
            ((GFFormulaireViewBean) viewBean).getFormulaire().setId(id);
            if (actionPart.equals(ACTION_CHANGE_STATUT)) {
                String statut = request.getParameter("statut");
                ((GFFormulaireViewBean) viewBean).getFormulaire().setStatus(statut);
            }
            // Copie des propriétés
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Traitement
            viewBean = dispatcher.dispatch(viewBean, action);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            // Choix de la destination avec prise en compte des éventuels erreurs
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                if(actionPart.equals(ACTION_TELECHARGER)) {
                    GFFileUtils.downloadFile(response, ((GFFormulaireViewBean) viewBean).getFormulaire().getAttachementName(), ((GFFormulaireViewBean) viewBean).getFormulaire().getAttachement());
                }
                destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                destination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), e.getMessage());
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        // Redirection vers la destination
        goSendRedirect(destination, request, response);
    }
}
