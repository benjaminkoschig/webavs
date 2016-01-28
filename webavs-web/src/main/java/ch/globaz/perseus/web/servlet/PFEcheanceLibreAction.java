package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.perseus.vb.echeance.PFEcheanceLibreViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PFEcheanceLibreAction extends PFAbstractDefaultServletAction {

    public PFEcheanceLibreAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String urlPlus = "";
        if (viewBean instanceof PFEcheanceLibreViewBean) {
            PFEcheanceLibreViewBean vb = (PFEcheanceLibreViewBean) viewBean;
            urlPlus = "&idDossier=" + vb.getEcheanceLibre().getSimpleEcheanceLibre().getIdDossier();
        }

        return super._getDestAjouterSucces(session, request, response, viewBean) + urlPlus;
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String urlPlus = "";
        if (viewBean instanceof PFEcheanceLibreViewBean) {
            PFEcheanceLibreViewBean vb = (PFEcheanceLibreViewBean) viewBean;
            urlPlus = "&idDossier=" + vb.getEcheanceLibre().getSimpleEcheanceLibre().getIdDossier();
        }
        return super._getDestModifierSucces(session, request, response, viewBean) + urlPlus;
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String urlPlus = "";
        if (viewBean instanceof PFEcheanceLibreViewBean) {
            PFEcheanceLibreViewBean vb = (PFEcheanceLibreViewBean) viewBean;
            urlPlus = "&idDossier=" + vb.getEcheanceLibre().getSimpleEcheanceLibre().getIdDossier();
        }
        return super._getDestSupprimerSucces(session, request, response, viewBean) + urlPlus;
    }

}
