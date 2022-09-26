package ch.globaz.eform.web.servlet;

import ch.globaz.common.util.NSSUtils;
import ch.globaz.eform.business.GFEFormServiceLocator;
import ch.globaz.eform.business.services.GFDaDossierSedexService;
import ch.globaz.eform.businessimpl.services.sedex.envoi.EnvoiSedexService;
import ch.globaz.eform.constant.GFStatusDADossier;
import ch.globaz.eform.constant.GFTypeDADossier;
import ch.globaz.eform.utils.GFFileUtils;
import globaz.eform.vb.demande.GFDemandeViewBean;
import globaz.eform.vb.envoi.GFEnvoiViewBean;
import globaz.eform.vb.suivi.GFSuiviViewBean;
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

public class GFDemandeServletAction extends FWDefaultServletAction {
    public final static String ACTION_PATH = "eform.demande.demande";
    public final static String ACTION_ENVOYER = "envoyer";

    public GFDemandeServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                FWDispatcher dispatcher) throws ServletException, IOException {
        String actionPart = getAction().getActionPart();
        String destination;

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            // Récupération du viewBean depuis la session
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            if (!(viewBean instanceof GFDemandeViewBean)) {
                viewBean = FWViewBeanActionFactory.newInstance(getAction(), dispatcher.getPrefix());
                session.setAttribute("viewBean", viewBean);
            }

            // Copie des propriétés
            JSPUtils.setBeanProperties(request, viewBean);
            viewBean = dispatcher.dispatch(viewBean, action);

            // Choix de la destination avec prise en compte des éventuelles erreurs
            if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                if (actionPart.equals(ACTION_ENVOYER)) {
                    destination = "/eform?userAction="+GFSuiviServletAction.ACTION_PATH+"."+GFSuiviServletAction.ACTION_CHERCHER+
                            "&likeNss=" + NSSUtils.unFormatNss(((GFDemandeViewBean) viewBean).getNssAffilier()) +
                            "&byCaisse=" + ((GFDemandeViewBean) viewBean).getCodeCaisse() +
                            "&byType=" + GFTypeDADossier.RECEPTION.getCodeSystem() +
                            "&byStatus=" + GFStatusDADossier.WAITING.getCodeSystem();
                } else {
                    destination = _getDestAfficherSucces(session, request, response, viewBean);
                }
            } else {
                destination = _getDestEnvoyerEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), e.getMessage());
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        // Redirection vers la destination
        goSendRedirectWithoutParameters(destination, request, response);
    }

    protected String _getDestAfficherSucces(HttpSession session, HttpServletRequest request,
                                            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this.getActionFullURL() + ".afficher";
    }

    protected String _getDestEnvoyerEchec(HttpSession session, HttpServletRequest request,
                                           HttpServletResponse response, FWViewBeanInterface viewBean) {
        return this._getDestEchec(session, request, response, viewBean);
    }
}
