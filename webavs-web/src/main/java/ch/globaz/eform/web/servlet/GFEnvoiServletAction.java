package ch.globaz.eform.web.servlet;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.eform.constant.GFStatusDADossier;
import ch.globaz.eform.constant.GFTypeDADossier;
import ch.globaz.eform.utils.GFFileUtils;
import globaz.eform.vb.demande.GFDemandeViewBean;
import globaz.eform.vb.envoi.GFEnvoiViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.context.JadeThread;
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

    public final static String ACTION_PATH = "eform.envoi.envoi";
    public final static String ACTION_UPLOAD = "upload";
    public final static String ACTION_REMOVEFILE = "removeFile";
    public final static String ACTION_ENVOYER = "envoyer";

    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                    FWDispatcher mainDispatcher) throws ServletException, IOException {

        beforeAfficher(session, request, response, (FWViewBeanInterface) session.getAttribute("viewBean"));
        super.actionReAfficher(session, request, response, mainDispatcher);
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
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            if (!(viewBean instanceof GFEnvoiViewBean)) {
                viewBean = FWViewBeanActionFactory.newInstance(getAction(), dispatcher.getPrefix());
                session.setAttribute("viewBean", viewBean);
            }
            // Copie des propriétés
            JSPUtils.setBeanProperties(request, viewBean);
            if (viewBean instanceof GFEnvoiViewBean && actionPart.equals(ACTION_UPLOAD)) {
                GFFileUtils.uploadFile((GFEnvoiViewBean) viewBean);
            } else if (viewBean instanceof GFEnvoiViewBean && actionPart.equals(ACTION_REMOVEFILE)) {
                String fileName = (String) request.getParameter("fileName");
                GFFileUtils.deleteFile((GFEnvoiViewBean) viewBean, fileName);
            }

            // Traitement
            viewBean = dispatcher.dispatch(viewBean, action);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            // Choix de la destination avec prise en compte des éventuels erreurs
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                destination = _getDestChercherSucces(session, request, response, viewBean);
                if (actionPart.equals(ACTION_UPLOAD) || actionPart.equals(ACTION_REMOVEFILE)) {
                    destination = this.getActionFullURL() + ".reAfficher";
                } else if(actionPart.equals(ACTION_ENVOYER)) {
                    destination = "/eform?userAction=eform.suivi.suivi.chercher" +
                    "&likeNss=" + NSSUtils.unFormatNss(((GFEnvoiViewBean) viewBean).getNss()) +
                            "&byCaisse=" + ((GFEnvoiViewBean) viewBean).getCaisseDestinatrice() +
                            "&byType=" + GFTypeDADossier.SEND_TYPE.getCodeSystem() +
                            "&byStatus=" + GFStatusDADossier.SEND.getCodeSystem();
                }
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

}
