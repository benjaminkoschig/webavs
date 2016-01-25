package ch.globaz.vulpecula.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.vulpecula.vb.association.PTAssociationAjaxViewBean;
import globaz.vulpecula.vb.association.PTAssociationViewBean;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class PTAssociationAction extends FWDefaultServletAction {

    public PTAssociationAction(final FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PTAssociationViewBean) {
            PTAssociationViewBean vb = (PTAssociationViewBean) viewBean;

            String idEmployeur = request.getParameter(PTConstants.ID_EMPLOYEUR);

            Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findById(idEmployeur);
            List<Administration> associationsProfessionnelles = VulpeculaRepositoryLocator
                    .getAdministrationRepository().findAllAssociationsProfessionnelles();

            vb.setEmployeur(employeur);
            vb.setAssociationsProfessionnelles(associationsProfessionnelles);
        } else if (viewBean instanceof PTAssociationAjaxViewBean) {
            PTAssociationAjaxViewBean vb = (PTAssociationAjaxViewBean) viewBean;
            String idEmployeur = request.getParameter(PTConstants.ID_EMPLOYEUR);
            vb.setIdEmployeur(idEmployeur);
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }
}
