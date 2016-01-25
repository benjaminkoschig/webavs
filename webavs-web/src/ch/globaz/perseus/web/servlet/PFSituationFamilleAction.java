package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.perseus.vb.situationfamille.PFEnfantAjaxViewBean;
import globaz.perseus.vb.situationfamille.PFSituationfamilialeViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PFSituationFamilleAction extends PFAbstractDefaultServletAction {

    public PFSituationFamilleAction(FWServlet aServlet) {
        super(aServlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        super._getDestModifierSucces(session, request, response, viewBean);
        PFSituationfamilialeViewBean vb = (PFSituationfamilialeViewBean) viewBean;
        String parametres = "&selectedId=" + vb.getSituationFamiliale().getId();
        parametres += "&idDemande=" + vb.getIdDemande();
        return getActionFullURL() + ".afficher" + parametres;
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PFSituationfamilialeViewBean) {
            PFSituationfamilialeViewBean vb = (PFSituationfamilialeViewBean) viewBean;
            vb.setIdDemande(request.getParameter("idDemande"));
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PFEnfantAjaxViewBean) {
            PFEnfantAjaxViewBean vb = (PFEnfantAjaxViewBean) viewBean;
            vb.setIdDemande(request.getParameter("idDemande"));
            vb.setIdSituationFamiliale(request.getParameter("idSituationFamiliale"));

            return super.beforeAfficher(session, request, response, vb);
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

}
