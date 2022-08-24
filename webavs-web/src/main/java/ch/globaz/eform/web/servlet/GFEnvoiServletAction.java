package ch.globaz.eform.web.servlet;

import ch.globaz.eform.constant.GFStatusEForm;
import ch.globaz.eform.utils.GFFileUtils;
import globaz.eform.vb.envoi.GFEnvoiViewBean;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GFEnvoiServletAction extends FWDefaultServletAction {
    private static final Logger LOG = LoggerFactory.getLogger(GFEnvoiServletAction.class);

    public GFEnvoiServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    public final static String ACTION_UPLOAD = "upload";
    public final static String ACTION_CHANGE_STATUT = "statut";

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionAfficher(session, request, response, mainDispatcher);
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewbean");
        if (!(viewBean instanceof GFEnvoiViewBean)) {
            viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
            session.setAttribute("viewbean", viewBean);
        }
        try {

            JSPUtils.setBeanProperties(request, viewBean);

            mainDispatcher.dispatch(viewBean, getAction());

        } catch (Exception e) {
            LOG.error("Failed to prepare viewBean for actionChercher", e);
        }
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
        String destination;

        // Définition de l'action custom standard pour l'application ARIES
        // Attention, si appel de custom action, on passe le paramètre "id" au lieu de "selectedId"

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            // Récupération du viewBean depuis la session
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, dispatcher.getPrefix());
            String id = request.getParameter("selectedId");
            ((GFFormulaireViewBean) viewBean).getFormulaire().setId(id);
            if (actionPart.equals(ACTION_UPLOAD)) {
                String statut = request.getParameter("statut");
                ((GFFormulaireViewBean) viewBean).getFormulaire().setStatus(GFStatusEForm.getStatusByCode(statut).getCodeSystem());
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

    protected String _getDestChercherSucces(HttpSession session, HttpServletRequest request,
                                            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this.getActionFullURL() + ".chercher";
    }

    protected String _getDestChercherEchec(HttpSession session, HttpServletRequest request,
                                           HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this._getDestEchec(session, request, response, viewBean);
    }


    protected void actionUpload(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
//        super.actionAfficher(session, request, response, mainDispatcher);
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewbean");
        if (!(viewBean instanceof GFEnvoiViewBean)) {
            viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
            session.setAttribute("viewbean", viewBean);
        }
        try {

            JSPUtils.setBeanProperties(request, viewBean);

            mainDispatcher.dispatch(viewBean, getAction());

        } catch (Exception e) {
            LOG.error("Failed to prepare viewBean for actionChercher", e);
        }
    }
}
