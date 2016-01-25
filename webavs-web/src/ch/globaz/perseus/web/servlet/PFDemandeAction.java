package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.perseus.vb.demande.PFAnnulerDemandeViewBean;
import globaz.perseus.vb.demande.PFCopieDemandeViewBean;
import globaz.perseus.vb.demande.PFDemandePCAVSAIOuvertureQDViewBean;
import globaz.perseus.vb.demande.PFDemandeViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.models.situationfamille.EnfantFamilleAddCheckMessage;

/**
 * @author DDE
 * 
 */
public class PFDemandeAction extends PFAbstractDefaultServletAction {

    private String copieDemandeDestination = "";

    private final static String SESSION_WARN_FAMILLE_KEY = "enfantWarnMessage";

    /**
     * @param aServlet
     */
    public PFDemandeAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String urlPlus = "";
        if (viewBean instanceof PFDemandeViewBean) {
            PFDemandeViewBean vb = (PFDemandeViewBean) viewBean;
            urlPlus = "&idDossier=" + vb.getDemande().getDossier().getId();
        }

        return super._getDestAjouterSucces(session, request, response, viewBean) + urlPlus;
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String urlPlus = "";
        if (viewBean instanceof PFDemandeViewBean) {
            PFDemandeViewBean vb = (PFDemandeViewBean) viewBean;
            urlPlus = "&idDossier=" + vb.getDemande().getDossier().getId();
        } else if (viewBean instanceof PFDemandePCAVSAIOuvertureQDViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart() + "."
                    + getAction().getPackagePart() + ".demande.chercher";
        }

        if (viewBean instanceof PFCopieDemandeViewBean) {
            PFCopieDemandeViewBean vb = (PFCopieDemandeViewBean) viewBean;

            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart() + "."
                    + getAction().getPackagePart() + ".demande.afficher&_method=upd&afterCopie=1&idDossier="
                    + vb.getDemande().getDossier().getId() + "&selectedId=" + vb.getId();
        }

        if (viewBean instanceof PFAnnulerDemandeViewBean) {
            PFAnnulerDemandeViewBean vb = (PFAnnulerDemandeViewBean) viewBean;
            return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart() + "."
                    + getAction().getPackagePart() + ".demande.chercher&idDossier="
                    + vb.getDemande().getDossier().getId();
        }

        return super._getDestAjouterSucces(session, request, response, viewBean) + urlPlus;
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

        if (viewBean instanceof PFCopieDemandeViewBean) {
            actionModifierCopieDemande(session, request, response, mainDispatcher);

        } else {
            super.actionModifier(session, request, response, mainDispatcher);
        }

    }

    private void actionModifierCopieDemande(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException {
        try {

            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);

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
            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, newAction);

            // gestions des warning à afficher dans l'écran de détail une fois la demande effectivement créé
            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {

                session.setAttribute(SESSION_WARN_FAMILLE_KEY, EnfantFamilleAddCheckMessage.warnForCopieDemande());
            }

            session.setAttribute("viewBean", viewBean);

            /*
             * choix de la destination _valid=fail : revient en mode edition
             */

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                copieDemandeDestination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                copieDemandeDestination = _getDestModifierEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            copieDemandeDestination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(copieDemandeDestination, request, response);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        viewBean = super.beforeAfficher(session, request, response, viewBean);
        if (viewBean instanceof PFDemandeViewBean) {

            PFDemandeViewBean vb = (PFDemandeViewBean) viewBean;
            EnfantFamilleAddCheckMessage enfantFamilleWarnMessages = (EnfantFamilleAddCheckMessage) session
                    .getAttribute(SESSION_WARN_FAMILLE_KEY);

            if (null != enfantFamilleWarnMessages) {
                ((PFDemandeViewBean) viewBean).setWarnCopieFamilleMessages(enfantFamilleWarnMessages);
                session.removeAttribute(SESSION_WARN_FAMILLE_KEY);
            }

            String idDossier = request.getParameter("idDossier");

            if ("add".equals(request.getParameter("_method")) && (viewBean instanceof PFDemandeViewBean)) {
                try {
                    vb.getDemande().setDossier(PerseusServiceLocator.getDossierService().read(idDossier));
                    vb.getDemande().getSimpleDemande().setIdDossier(idDossier);
                } catch (Exception e) {
                    JadeThread.logError(PFDemandeAction.class.getName(),
                            "Erreur technique, merci d'envoyer un PrintScreen à Globaz (PFDemandeAction.beforeAfficher) : "
                                    + e.getMessage());
                }
            }

            if (!JadeStringUtil.isEmpty(request.getParameter("idDemande"))) {
                ((PFDemandeViewBean) viewBean).setId(request.getParameter("idDemande"));
            }
        }

        if (viewBean instanceof PFDemandePCAVSAIOuvertureQDViewBean) {
            if (!JadeStringUtil.isEmpty(request.getParameter("idDemande"))) {
                ((PFDemandePCAVSAIOuvertureQDViewBean) viewBean).setId(request.getParameter("idDemande"));
            }
        }

        if (viewBean instanceof PFCopieDemandeViewBean) {
            PFCopieDemandeViewBean vb = (PFCopieDemandeViewBean) viewBean;
            String idDemande = request.getParameter("idDemande");
            vb.setId(idDemande);
        }

        if (viewBean instanceof PFAnnulerDemandeViewBean) {
            PFAnnulerDemandeViewBean vb = (PFAnnulerDemandeViewBean) viewBean;
            String idDemande = request.getParameter("idDemande");
            vb.setId(idDemande);
        }

        return viewBean;
    }

    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        // Si les checkbox ne sont pas checkés setté à vide les listes si non elle garde leur ancienne valeur
        if (viewBean instanceof PFDemandeViewBean) {
            PFDemandeViewBean vb = (PFDemandeViewBean) viewBean;
            if (request.getParameter("demande.listCsAutresDemandes") == null) {
                vb.getDemande().getSimpleDemande().setListCsAutresDemandes("");
            }
            if (request.getParameter("demande.listCsAutresPrestations") == null) {
                vb.getDemande().getSimpleDemande().setListCsAutresPrestations("");
            }

            return super.beforeModifier(session, request, response, vb);
        }

        return super.beforeModifier(session, request, response, viewBean);
    }

}
