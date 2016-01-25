package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pegasus.vb.transfertdossier.PCDemandeTransfertDossierViewBean;
import globaz.pegasus.vb.transfertdossier.PCDemandeTransfertRenteViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.pegasus.business.constantes.IPCActions;

public class PCTransfertServletAction extends PCAbstractServletAction {
    /**
     * Constructeur
     * 
     * @param aServlet
     */
    public PCTransfertServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCDemandeTransfertRenteViewBean) {
            return "/" + getAction().getApplicationPart() + "?userAction=" + IPCActions.ACTION_DEMANDE + "."
                    + FWAction.ACTION_CHERCHER + "&idDossier="
                    + ((PCDemandeTransfertRenteViewBean) viewBean).getIdDossier();
        } else {
            return super._getDestExecuterSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PCDemandeTransfertDossierViewBean) {
            PCDemandeTransfertDossierViewBean vb = (PCDemandeTransfertDossierViewBean) viewBean;
            vb.setIdDemandePc(JadeStringUtil.toNotNullString(request.getParameter("idDemandePc")));
            vb.setIdGestionnaire(JadeStringUtil.toNotNullString(request.getParameter("idGestionnaire")));
        } else if (viewBean instanceof PCDemandeTransfertRenteViewBean) {
            PCDemandeTransfertRenteViewBean vb = (PCDemandeTransfertRenteViewBean) viewBean;
            vb.setIdDemandePc(JadeStringUtil.toNotNullString(request.getParameter("idDemandePc")));
            vb.setIdDossier(JadeStringUtil.toNotNullString(request.getParameter("idDossier")));
            vb.setIdGestionnaire(JadeStringUtil.toNotNullString(request.getParameter("idGestionnaire")));
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

}
