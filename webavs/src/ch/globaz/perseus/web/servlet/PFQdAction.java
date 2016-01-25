/**
 * 
 */
package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.perseus.vb.qd.PFDetailfactureViewBean;
import globaz.perseus.vb.qd.PFFactureViewBean;
import globaz.perseus.vb.qd.PFOuvertureQdViewBean;
import globaz.perseus.vb.qd.PFQdChercherViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author DDE
 * 
 */
public class PFQdAction extends PFAbstractDefaultServletAction {

    public PFQdAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFFactureViewBean) {
            PFFactureViewBean vb = (PFFactureViewBean) viewBean;
            // S140507_002
            // ajout d'un bouton sur lequel la destination apres validation diffère (rester sur la page d'ajout)
            if (request.getParameter("PFQdRenew") != null && "true".equalsIgnoreCase(request.getParameter("PFQdRenew"))) {
                String urlPlus = "&_method=add&idDossier="
                        + vb.getFacture().getQd().getQdAnnuelle().getDossier().getId();
                return getActionFullURL() + ".afficher" + urlPlus;
            }
            String urlPlus = "&idDossier=" + vb.getFacture().getQd().getQdAnnuelle().getDossier().getId();
            return getActionFullURL() + ".chercher" + urlPlus;
        }

        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFDetailfactureViewBean) {
            PFDetailfactureViewBean vb = (PFDetailfactureViewBean) viewBean;
            String urlPlus = "&idDossier=" + vb.getFacture().getQd().getQdAnnuelle().getDossier().getId();
            return getActionFullURL().replace("detailfacture", "facture") + ".chercher" + urlPlus;
        }

        if (viewBean instanceof PFFactureViewBean) {
            PFFactureViewBean vb = (PFFactureViewBean) viewBean;
            String urlPlus = "&idDossier=" + vb.getFacture().getQd().getQdAnnuelle().getDossier().getId();
            return getActionFullURL() + ".chercher" + urlPlus;
        }

        if (viewBean instanceof PFOuvertureQdViewBean) {
            PFOuvertureQdViewBean vb = (PFOuvertureQdViewBean) viewBean;
            String urlPlus = "&idDossier=" + vb.getDemande().getDossier().getId();
            return getActionFullURL().replace("ouvertureQd", "qd") + ".chercher" + urlPlus;
        }

        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFDetailfactureViewBean) {
            PFDetailfactureViewBean vb = (PFDetailfactureViewBean) viewBean;
            String urlPlus = "&idDossier=" + vb.getFacture().getQd().getQdAnnuelle().getDossier().getId();
            return getActionFullURL().replace("detailfacture", "facture") + ".chercher" + urlPlus;
        }

        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.actionAfficher(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        /*
         * recupération du bean depuis la session
         */
        FWViewBeanInterface viewBeanInt = (FWViewBeanInterface) session.getAttribute("viewBean");

        PFQdChercherViewBean viewBean = new PFQdChercherViewBean();

        if (viewBeanInt instanceof PFFactureViewBean) {
            PFFactureViewBean vb = (PFFactureViewBean) viewBeanInt;
            String urlPlus = "&idDossier=" + vb.getFacture().getQd().getQdAnnuelle().getDossier().getId();
            String destination = getActionFullURL().replace("detailfacture", "facture") + ".chercher" + urlPlus;

            session.setAttribute("viewBean", viewBean);
            // viewBean.setIdDossier(vb.getFacture().getQd().getQdAnnuelle().getDossier().getId());

            goSendRedirect(destination, request, response);
        } else {
            viewBean.setIdDossier(request.getParameter("idDossier"));
            try {
                viewBean.init();
                request.setAttribute("viewBean", viewBean);
            } catch (Exception e) {
                JadeThread.logError(
                        PFQdAction.class.getName(),
                        "Erreur technique, merci d'envoyer un PrintScreen à Globaz (PFQdAction.actionChercher) : "
                                + e.toString());
            }
            super.actionChercher(session, request, response, mainDispatcher);
        }
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PFFactureViewBean) {
            PFFactureViewBean vb = (PFFactureViewBean) viewBean;
            try {
                vb.getDossier().setId(request.getParameter("idDossier"));
            } catch (Exception e) {
                JadeThread.logError(
                        PFQdAction.class.getName(),
                        "Erreur technique, merci d'envoyer un PrintScreen à Globaz (PFQdAction.beforeAfficher) : "
                                + e.toString());
            }

            return super.beforeAfficher(session, request, response, vb);
        }

        if (viewBean instanceof PFDetailfactureViewBean) {
            if (request.getParameter("idFacture") != null) {
                ((PFDetailfactureViewBean) viewBean).setId(request.getParameter("idFacture"));
            }
        }

        if (viewBean instanceof PFOuvertureQdViewBean) {
            String idDemande = request.getParameter("idDemande");
            ((PFOuvertureQdViewBean) viewBean).setId(idDemande);
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeModifier(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFFactureViewBean) {
            PFFactureViewBean vb = (PFFactureViewBean) viewBean;

            vb.getFacture()
                    .getSimpleFacture()
                    .setMontantRembourse(
                            JadeStringUtil.change(vb.getFacture().getSimpleFacture().getMontantRembourse(), "'", ""));

            vb.getFacture()
                    .getSimpleFacture()
                    .setExcedantRevenuCompense(
                            JadeStringUtil.change(vb.getFacture().getSimpleFacture().getExcedantRevenuCompense(), "'",
                                    ""));

            vb.getFacture().getSimpleFacture()
                    .setMontant(JadeStringUtil.change(vb.getFacture().getSimpleFacture().getMontant(), "'", ""));

        }

        return super.beforeModifier(session, request, response, viewBean);

    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        // Définition de l'action custom standard pour l'application WEB@AF
        // Attention, si appel de custom action, on passe le paramètre "id" au
        // lieu de "selectedId"
        String destination = null;
        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            /*
             * recupération du bean depuis la sesison
             */
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, dispatcher.getPrefix());
            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = dispatcher.dispatch(viewBean, action);

            if (viewBean instanceof PFDetailfactureViewBean) {
                PFDetailfactureViewBean vb = (PFDetailfactureViewBean) viewBean;
                String urlPlus = "&idDossier=" + vb.getFacture().getQd().getQdAnnuelle().getDossier().getId();
                destination = getActionFullURL().replace("detailfacture", "facture") + ".chercher" + urlPlus;
                session.setAttribute("viewBean", viewBean);
            } else {
                session.setAttribute("viewBean", viewBean);
                /*
                 * choix de la destination _valid=fail : revient en mode edition
                 */
                boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
                if (goesToSuccessDest) {
                    destination = _getDestExecuterSucces(session, request, response, viewBean);
                } else {
                    destination = _getDestExecuterEchec(session, request, response, viewBean);
                }
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(destination, request, response);
    }

}
