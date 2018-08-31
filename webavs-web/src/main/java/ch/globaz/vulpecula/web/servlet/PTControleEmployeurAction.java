package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.servlets.FWServlet;
import globaz.vulpecula.vb.ctrlemployeur.PTControleEmployeurViewBean;
import globaz.vulpecula.vb.ctrlemployeur.PTLettreControleViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;

public class PTControleEmployeurAction extends PTDefaultServletAction {

    public PTControleEmployeurAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTControleEmployeurViewBean) {
            String method = request.getParameter("_method");
            if (FWAction.ACTION_ADD.equals(method)) {
                String idEmployeur = request.getParameter(PTConstants.ID_EMPLOYEUR);
                PTControleEmployeurViewBean vb = (PTControleEmployeurViewBean) viewBean;
                vb.setEmployeur(VulpeculaRepositoryLocator.getEmployeurRepository().findByIdAffilie(idEmployeur));
            }
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTControleEmployeurViewBean) {
            PTControleEmployeurViewBean vb = (PTControleEmployeurViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.postetravailvueglobale.employeurvueglobale.afficher&selectedId="
                    + vb.getEmployeur().getId() + "&tab=controlesEmployeurs";
        }
        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTControleEmployeurViewBean) {
            PTControleEmployeurViewBean vb = (PTControleEmployeurViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.postetravailvueglobale.employeurvueglobale.afficher&selectedId="
                    + vb.getEmployeur().getId() + "&tab=controlesEmployeurs";
        }
        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTControleEmployeurViewBean) {
            PTControleEmployeurViewBean vb = (PTControleEmployeurViewBean) viewBean;
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.postetravailvueglobale.employeurvueglobale.afficher&selectedId="
                    + vb.getEmployeur().getId() + "&tab=controlesEmployeurs";
        }
        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTLettreControleViewBean) {
            return "/" + getAction().getApplicationPart()
                    + "?userAction=vulpecula.ctrlemployeur.lettreControle.afficher&process=launched";
        }
        return super._getDestExecuterSucces(session, request, response, viewBean);
    }
}
