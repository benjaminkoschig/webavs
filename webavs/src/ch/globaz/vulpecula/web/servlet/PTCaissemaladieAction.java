package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.servlets.FWServlet;
import globaz.vulpecula.vb.caissemaladie.PTCaissemaladieAjaxViewBean;
import globaz.vulpecula.vb.caissemaladie.PTCaissemaladieViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;

public class PTCaissemaladieAction extends PTDefaultServletAction {

    public PTCaissemaladieAction(final FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String idTravailleur = "";
        if (viewBean instanceof PTCaissemaladieAjaxViewBean) {
            PTCaissemaladieAjaxViewBean vb = (PTCaissemaladieAjaxViewBean) viewBean;
            idTravailleur = request.getParameter(PTConstants.ID_TRAVAILLEUR);
            vb.setIdTravailleur(idTravailleur);
        } else if (viewBean instanceof PTCaissemaladieViewBean) {
            PTCaissemaladieViewBean vb = (PTCaissemaladieViewBean) viewBean;
            idTravailleur = request.getParameter(PTConstants.ID_TRAVAILLEUR);
            if (FWAction.ACTION_ADD.equals(request.getParameter(FWServlet.METHOD))) {
                vb.setTravailleur(VulpeculaRepositoryLocator.getTravailleurRepository().findById(idTravailleur));
            }
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PTCaissemaladieViewBean) {
            PTCaissemaladieViewBean vb = (PTCaissemaladieViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);
        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTCaissemaladieViewBean) {
            PTCaissemaladieViewBean vb = (PTCaissemaladieViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);

        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTCaissemaladieViewBean) {
            PTCaissemaladieViewBean vb = (PTCaissemaladieViewBean) viewBean;
            return destinationToTravailleurVueGenerale(vb);

        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTCaissemaladieViewBean) {
            PTCaissemaladieViewBean vb = (PTCaissemaladieViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.caissemaladie.caissemaladie.reAfficher&_method=add&selectedId="
                    + vb.getTravailleur().getId();
        }
        return super._getDestAjouterEchec(session, request, response, viewBean);
    }

    private String destinationToTravailleurVueGenerale(PTCaissemaladieViewBean vb) {
        return "/"
                + getAction().getApplicationPart()
                + "?userAction=vulpecula.postetravailvueglobale.travailleurvueglobale.afficher&tab=caissesmaladies&selectedId="
                + vb.getTravailleur().getId();
    }
}
