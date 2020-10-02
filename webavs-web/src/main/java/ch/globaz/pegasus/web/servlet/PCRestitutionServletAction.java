package ch.globaz.pegasus.web.servlet;

import ch.globaz.pegasus.business.constantes.IPCDroits;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.pegasus.vb.droit.PCCorrigerDroitViewBean;
import globaz.pegasus.vb.droit.PCDroitViewBean;
import globaz.pegasus.vb.restitution.PCRestitutionViewBean;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PCRestitutionServletAction extends PCAbstractServletAction {

    private static final Logger LOG = Logger.getLogger(PCRestitutionServletAction.class);

    public PCRestitutionServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
                                                 HttpServletResponse response, FWViewBeanInterface viewBean) {


        if ((viewBean instanceof PCRestitutionViewBean)) {
            PCRestitutionViewBean vb = (PCRestitutionViewBean) viewBean;

            String idDossier = request.getParameter("idDossier");
            vb.setIdDossier(idDossier);
        }

        return viewBean;

    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
                                           HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCRestitutionViewBean) {
            PCRestitutionViewBean vb = (PCRestitutionViewBean) viewBean;
            return getActionFullURL() + ".afficher&idDossier=" + vb.getIdDossier();
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
                                            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCRestitutionViewBean) {
            PCRestitutionViewBean vb = (PCRestitutionViewBean) viewBean;
            return getActionFullURL() + ".afficher&idDossier=" + vb.getIdDossier();
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }

}
