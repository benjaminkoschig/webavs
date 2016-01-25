package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.vulpecula.vb.servicemilitaire.PTServicemilitaireAjaxViewBean;
import globaz.vulpecula.vb.servicemilitaire.PTServicemilitaireViewBean;
import globaz.vulpecula.vb.servicemilitaire.PTServicemilitairenotitleViewBean;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.services.musca.PassageSearchException;

public class PTServiceMilitaireAction extends FWDefaultServletAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(PTServiceMilitaireAction.class.getName());

    public PTServiceMilitaireAction(final FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTServicemilitaireViewBean) {
            PTServicemilitaireViewBean vb = (PTServicemilitaireViewBean) viewBean;
            String idTravailleur = request.getParameter("idTravailleur");

            if (request.getParameter("ajoutSucces") != null) {
                vb.setAjoutSucces("true");
            }

            if (isAdd(request)) {
                List<PosteTravail> postesDroitsSM = VulpeculaServiceLocator.getPosteTravailService()
                        .getPostesTravailsWithDroitsSM(idTravailleur);

                Travailleur travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(idTravailleur);
                Passage passage = null;

                try {
                    passage = VulpeculaServiceLocator.getPassageService().findPassageActif(
                            FAModuleFacturation.CS_MODULE_SERVICE_MILITAIRE);
                } catch (PassageSearchException e) {
                    LOGGER.error(e.getMessage());
                }

                vb.setTravailleur(travailleur);
                vb.setPosteTravailsPossibles(postesDroitsSM);
                vb.setPassage(passage);
            }
        } else if (viewBean instanceof PTServicemilitaireAjaxViewBean) {
            PTServicemilitaireAjaxViewBean vb = (PTServicemilitaireAjaxViewBean) viewBean;
            String idTravailleur = request.getParameter(PTConstants.ID_TRAVAILLEUR);
            vb.setIdTravailleur(idTravailleur);
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    private boolean isAdd(HttpServletRequest request) {
        String method = request.getParameter("_method");
        return method != null;
    }

    @Override
    protected void actionAfficherAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = getAJAXAfficherSuccessDestination(session, request);
        try {
            String action = request.getParameter("userAction");
            FWAction privateAction = FWAction.newInstance(action);
            String selectedId = request.getParameter("idEntity");

            if (JadeStringUtil.isEmpty(selectedId)) {
                selectedId = request.getParameter("id");
            }

            FWAJAXViewBeanInterface viewBean = getAJAXViewBean(mainDispatcher, selectedId);

            /*
             * initialisation du viewBean appelle beforeAfficher, puis le Dispatcher, puis met le bean en session
             */
            viewBean = (FWAJAXViewBeanInterface) beforeAfficher(session, request, response, viewBean);
            viewBean = (FWAJAXViewBeanInterface) mainDispatcher.dispatch(viewBean, privateAction);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
            request.setAttribute("exception", e);
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTServicemilitairenotitleViewBean) {
            PTServicemilitairenotitleViewBean vb = (PTServicemilitairenotitleViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.servicemilitaire.servicemilitairenotitle.afficher&idTravailleur="
                    + vb.getTravailleur().getId() + "&_method=add&ajoutSucces=true";
        } else if (viewBean instanceof PTServicemilitaireViewBean) {
            PTServicemilitaireViewBean vb = (PTServicemilitaireViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);
        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTServicemilitaireViewBean) {
            PTServicemilitaireViewBean vb = (PTServicemilitaireViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);

        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTServicemilitaireViewBean) {
            PTServicemilitaireViewBean vb = (PTServicemilitaireViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);

        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    private String destinationToTravailleurVueGenerale(PTServicemilitaireViewBean vb) {
        return "/"
                + getAction().getApplicationPart()
                + "?userAction=vulpecula.postetravailvueglobale.travailleurvueglobale.afficher&tab=servicesmilitaires&selectedId="
                + vb.getTravailleur().getId();
    }

    @Override
    protected void actionSupprimerAJAX(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {
        String actionString = request.getParameter("userAction");
        FWAction action = FWAction.newInstance(actionString);

        boolean hasRight = BSessionUtil.getSessionFromThreadContext().hasRight(action.getElement(), action.getRight());

        if (action.getRight() != null && hasRight) {
            String idServiceMilitaire = request.getParameter("idServiceMilitaire");
            if (idServiceMilitaire != null && idServiceMilitaire.length() > 0) {
                VulpeculaRepositoryLocator.getServiceMilitaireRepository().deleteById(idServiceMilitaire);
            }
        }
    }

    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = null;
        try {
            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);

            /*
             * recuperation du bean depuis la session
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set automatique des proprietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeAdd() call du dispatcher, puis mis en session
             */
            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, newAction);
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix de la destination
             */
            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestAjouterSucces(session, request, response, viewBean);
            } else {
                destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirectWithoutParameters(destination, request, response);
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return "/" + getAction().getApplicationPart()
                + "?userAction=vulpecula.servicemilitaire.servicemilitaire.reAfficher&_method=add";
    }

}
