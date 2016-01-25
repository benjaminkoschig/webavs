package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSessionUtil;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.vulpecula.vb.absencesjustifiees.PTAbsencesjustifieesAjaxViewBean;
import globaz.vulpecula.vb.absencesjustifiees.PTAbsencesjustifieesViewBean;
import globaz.vulpecula.vb.absencesjustifiees.PTAbsencesjustifieesnotitleViewBean;
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
import ch.globaz.vulpecula.util.CodeSystem;
import ch.globaz.vulpecula.util.CodeSystemUtil;

public class PTAbsencesJustifieesAction extends FWDefaultServletAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(PTAbsencesJustifieesAction.class.getName());

    public PTAbsencesJustifieesAction(final FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTAbsencesjustifieesViewBean) {
            PTAbsencesjustifieesViewBean vb = (PTAbsencesjustifieesViewBean) viewBean;
            String idTravailleur = request.getParameter("idTravailleur");

            if (request.getParameter("ajoutSucces") != null) {
                vb.setAjoutSucces("true");
            }

            if (isAdd(request)) {
                List<PosteTravail> postesDroitsAJ = VulpeculaServiceLocator.getPosteTravailService()
                        .getPostesTravailsWithDroitsAJ(idTravailleur);
                Travailleur travailleur = VulpeculaRepositoryLocator.getTravailleurRepository().findById(idTravailleur);
                List<CodeSystem> genrePrestations = CodeSystemUtil
                        .getCodesSystemesForFamille(PTConstants.CS_GROUPE_GENRE_PRESTATIONS_AJ);
                List<CodeSystem> parentes = CodeSystemUtil.getCodesSystemesForFamille(PTConstants.CS_GROUP_PARENTES);

                try {
                    Passage passage = VulpeculaServiceLocator.getPassageService().findPassageActif(
                            FAModuleFacturation.CS_MODULE_ABSENCES_JUSTIFIEES);
                    vb.setPassage(passage);
                } catch (PassageSearchException e) {
                    LOGGER.error(e.getMessage());
                }

                vb.setTravailleur(travailleur);
                vb.setPosteTravailsPossibles(postesDroitsAJ);
                vb.setGenrePrestations(genrePrestations);
                vb.setParentes(parentes);
            }
        }
        if (viewBean instanceof PTAbsencesjustifieesAjaxViewBean) {
            PTAbsencesjustifieesAjaxViewBean vb = (PTAbsencesjustifieesAjaxViewBean) viewBean;
            String idTravailleur = request.getParameter("idTravailleur");
            vb.setIdTravailleur(idTravailleur);
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTAbsencesjustifieesnotitleViewBean) {
            PTAbsencesjustifieesnotitleViewBean vb = (PTAbsencesjustifieesnotitleViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.absencesjustifiees.absencesjustifieesnotitle.afficher&idTravailleur="
                    + vb.getTravailleur().getId() + "&_method=add&ajoutSucces=true";
        } else if (viewBean instanceof PTAbsencesjustifieesViewBean) {
            PTAbsencesjustifieesViewBean vb = (PTAbsencesjustifieesViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);
        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTAbsencesjustifieesViewBean) {
            PTAbsencesjustifieesViewBean vb = (PTAbsencesjustifieesViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);

        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTAbsencesjustifieesViewBean) {
            PTAbsencesjustifieesViewBean vb = (PTAbsencesjustifieesViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);

        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    private String destinationToTravailleurVueGenerale(PTAbsencesjustifieesViewBean vb) {
        return "/"
                + getAction().getApplicationPart()
                + "?userAction=vulpecula.postetravailvueglobale.travailleurvueglobale.afficher&tab=absencesjustifiees&selectedId="
                + vb.getTravailleur().getId();
    }

    private boolean isAdd(HttpServletRequest request) {
        String method = request.getParameter("_method");
        return method != null;
    }

    @Override
    protected void actionSupprimerAJAX(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        String actionString = request.getParameter("userAction");
        FWAction action = FWAction.newInstance(actionString);

        boolean hasRight = BSessionUtil.getSessionFromThreadContext().hasRight(action.getElement(), action.getRight());

        if (action.getRight() != null && hasRight) {
            String idAbsenceJustifiee = request.getParameter("idAbsenceJustifiee");
            if (idAbsenceJustifiee != null && idAbsenceJustifiee.length() > 0) {
                VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().deleteById(idAbsenceJustifiee);
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
                + "?userAction=vulpecula.absencesjustifiees.absencesjustifiees.reAfficher&_method=add";
    }
}
