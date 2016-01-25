package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.servlets.FWServlet;
import globaz.vulpecula.vb.syndicat.PTSyndicatAjaxViewBean;
import globaz.vulpecula.vb.syndicat.PTSyndicatViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;

public class PTSyndicatAction extends PTDefaultServletAction {

    public PTSyndicatAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTSyndicatViewBean) {
            PTSyndicatViewBean vb = (PTSyndicatViewBean) viewBean;
            if (FWAction.ACTION_ADD.equals(request.getParameter(FWServlet.METHOD))) {
                String idTravailleur = request.getParameter(PTConstants.ID_TRAVAILLEUR);
                vb.setTravailleur(VulpeculaRepositoryLocator.getTravailleurRepository().findById(idTravailleur));
            }
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTSyndicatAjaxViewBean) {
            PTSyndicatAjaxViewBean vb = (PTSyndicatAjaxViewBean) viewBean;
            vb.setIdTravailleur(request.getParameter(PTConstants.ID_TRAVAILLEUR));
        }
        return super.beforeLister(session, request, response, viewBean);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PTSyndicatViewBean) {
            PTSyndicatViewBean vb = (PTSyndicatViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);
        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTSyndicatViewBean) {
            PTSyndicatViewBean vb = (PTSyndicatViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);

        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTSyndicatViewBean) {
            PTSyndicatViewBean vb = (PTSyndicatViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);

        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTSyndicatViewBean) {
            PTSyndicatViewBean vb = (PTSyndicatViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.syndicat.syndicat.reAfficher&_method=add&selectedId="
                    + vb.getTravailleur().getId();
        }
        return super._getDestAjouterEchec(session, request, response, viewBean);
    }

    private String destinationToTravailleurVueGenerale(PTSyndicatViewBean vb) {
        return "/"
                + getAction().getApplicationPart()
                + "?userAction=vulpecula.postetravailvueglobale.travailleurvueglobale.afficher&tab=syndicats&selectedId="
                + vb.getTravailleur().getId();
    }

}
