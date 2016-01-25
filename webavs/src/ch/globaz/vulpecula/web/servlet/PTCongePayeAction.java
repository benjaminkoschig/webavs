package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.params.FWParamString;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.globall.db.BSessionUtil;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.vulpecula.vb.absencesjustifiees.PTAbsencesjustifieesAjaxViewBean;
import globaz.vulpecula.vb.congepaye.PTCompteursAjaxViewBean;
import globaz.vulpecula.vb.congepaye.PTCongePayeAjaxViewBean;
import globaz.vulpecula.vb.congepaye.PTCongepayeViewBean;
import globaz.vulpecula.vb.congepaye.PTCongepayenotitleViewBean;
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
import ch.globaz.vulpecula.domain.models.prestations.TypePrestation;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.external.services.musca.PassageSearchException;

/**
 * Actions liées aux congés payés
 * 
 * @since WebBMS 0.01.04
 */
public class PTCongePayeAction extends FWDefaultServletAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(PTAbsencesJustifieesAction.class.getName());

    public PTCongePayeAction(final FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTCongepayeViewBean) {
            PTCongepayeViewBean vb = (PTCongepayeViewBean) viewBean;
            String idTravailleur = request.getParameter("idTravailleur");

            if (request.getParameter("ajoutSucces") != null) {
                vb.setAjoutSucces("true");
            }

            if (isAdd(request)) {
                List<PosteTravail> postesDroitsCP = VulpeculaServiceLocator.getPosteTravailService()
                        .getPostesTravailsWithDroitsCP(idTravailleur);

                Travailleur travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(idTravailleur);
                Passage passage = null;

                try {
                    passage = VulpeculaServiceLocator.getPassageService().findPassageActif(
                            FAModuleFacturation.CS_MODULE_CONGE_PAYE);
                } catch (PassageSearchException e) {
                    LOGGER.error(e.getMessage());
                }

                if (postesDroitsCP.size() > 0) {
                    vb.getCongePaye().setPosteTravail(postesDroitsCP.get(0));
                }
                vb.setTravailleur(travailleur);
                vb.setPosteTravailsPossibles(postesDroitsCP);

                vb.setPassage(passage);
            }

            if (viewBean instanceof PTCongepayenotitleViewBean) {
                PTCongepayenotitleViewBean notitleViewBean = (PTCongepayenotitleViewBean) viewBean;
                FWUrlsStack stack = (FWUrlsStack) session.getAttribute(FWServlet.URL_STACK);
                FWUrl newUrl = new FWUrl(stack.peek());
                newUrl.addParam(new FWParamString(PTConstants.ID_TRAVAILLEUR, notitleViewBean.getIdTravailleur()));
                newUrl.addParam(new FWParamString(PTConstants.TYPE_PRESTATION, TypePrestation.CONGES_PAYES.getValue()));
                stack.push(newUrl);
            }
        } else if (viewBean instanceof PTAbsencesjustifieesAjaxViewBean) {
            PTAbsencesjustifieesAjaxViewBean vb = (PTAbsencesjustifieesAjaxViewBean) viewBean;
            String idTravailleur = request.getParameter("idTravailleur");
            vb.setIdTravailleur(idTravailleur);
        }

        else if (viewBean instanceof PTCompteursAjaxViewBean) {
            PTCompteursAjaxViewBean vb = (PTCompteursAjaxViewBean) viewBean;
            String idCompteur = request.getParameter("idCompteur");
            vb.setIdCompteur(idCompteur);
        }

        else if (viewBean instanceof PTCongePayeAjaxViewBean) {
            PTCongePayeAjaxViewBean vb = (PTCongePayeAjaxViewBean) viewBean;
            String idTravailleur = request.getParameter("idTravailleur");
            vb.setIdTravailleur(idTravailleur);
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTCongepayenotitleViewBean) {
            PTCongepayenotitleViewBean vb = (PTCongepayenotitleViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.congepaye.congepayenotitle.afficher&idTravailleur="
                    + vb.getTravailleur().getId() + "&_method=add&ajoutSucces=true";
        } else if (viewBean instanceof PTCongepayeViewBean) {
            PTCongepayeViewBean vb = (PTCongepayeViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);
        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    private String destinationToTravailleurVueGenerale(PTCongepayeViewBean vb) {
        return "/"
                + getAction().getApplicationPart()
                + "?userAction=vulpecula.postetravailvueglobale.travailleurvueglobale.afficher&tab=congespayes&selectedId="
                + vb.getTravailleur().getId();
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return "/" + getAction().getApplicationPart()
                + "?userAction=vulpecula.congepaye.congepaye.reAfficher&_method=add";
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTCongepayeViewBean) {
            PTCongepayeViewBean vb = (PTCongepayeViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);

        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTCongepayeViewBean) {
            PTCongepayeViewBean vb = (PTCongepayeViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);

        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected void actionSupprimerAJAX(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        String actionString = request.getParameter("userAction");
        FWAction action = FWAction.newInstance(actionString);

        boolean hasRight = BSessionUtil.getSessionFromThreadContext().hasRight(action.getElement(), action.getRight());

        if (action.getRight() != null && hasRight) {
            String idCongePaye = request.getParameter("idCongePaye");
            if (idCongePaye != null && idCongePaye.length() > 0) {
                VulpeculaRepositoryLocator.getCongePayeRepository().deleteById(idCongePaye);
            }
        }
    }

    private boolean isAdd(HttpServletRequest request) {
        String method = request.getParameter("_method");
        return method != null;
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
}
