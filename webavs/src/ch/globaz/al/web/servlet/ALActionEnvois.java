/**
 * 
 */
package ch.globaz.al.web.servlet;

import globaz.al.vb.envois.ALEnvoisViewBean;
import globaz.al.vb.envois.ALParametresViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author dhi
 * 
 */
public class ALActionEnvois extends ALAbstractDefaultAction {

    /**
     * @param servlet
     */
    public ALActionEnvois(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterEchec(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALParametresViewBean) {
            String destination = super._getDestAjouterEchec(session, request, response, viewBean);
            destination += "&_method=add";
            return destination;
        } else {
            return super._getDestAjouterEchec(session, request, response, viewBean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof ALEnvoisViewBean) {
            ALEnvoisViewBean currentViewBean = (ALEnvoisViewBean) viewBean;
            if (currentViewBean.getJobIsSelected()) {
                // suppression du job >> supprimer success par défaut
                return super._getDestSupprimerSucces(session, request, response, viewBean);
            } else {
                // suppression d'un envoi >> afficher la même page
                String destination = "/" + getAction().getApplicationPart();
                destination += "?userAction=al.envois.envois.afficher&selectedId=";
                destination += currentViewBean.getId();
                return destination;
            }
        } else {
            return super._getDestSupprimerSucces(session, request, response, viewBean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeModifier(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALEnvoisViewBean) {
            ALEnvoisViewBean currentViewBean = (ALEnvoisViewBean) viewBean;
            String newStatus = request.getParameter("newStatus");
            currentViewBean.setNewStatus(newStatus);
        }
        return super.beforeModifier(session, request, response, viewBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeSupprimer(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof ALEnvoisViewBean) {
            String isJobString = request.getParameter("isJob");
            String idToDelete = request.getParameter("idToDelete");
            Boolean isJob = Boolean.parseBoolean(isJobString);
            ALEnvoisViewBean currentViewBean = (ALEnvoisViewBean) viewBean;
            currentViewBean.setIdToDelete(idToDelete);
            currentViewBean.setJobIsSelected(isJob);
        }
        return super.beforeSupprimer(session, request, response, viewBean);
    }

}
