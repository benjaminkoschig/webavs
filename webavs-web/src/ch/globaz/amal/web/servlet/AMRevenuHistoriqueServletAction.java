/**
 * 
 */
package ch.globaz.amal.web.servlet;

import globaz.amal.vb.revenuHistorique.AMRevenuHistoriqueViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author dhi
 * 
 */
public class AMRevenuHistoriqueServletAction extends AMAbstractServletAction {

    /**
     * Default constructor
     * 
     * @param aServlet
     */
    public AMRevenuHistoriqueServletAction(FWServlet aServlet) {
        super(aServlet);
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
        AMRevenuHistoriqueViewBean revenuViewBean = (AMRevenuHistoriqueViewBean) viewBean;
        String destination = super._getDestAjouterEchec(session, request, response, viewBean);
        destination += "&contribuableId=" + revenuViewBean.getContribuable().getId();
        destination += "&_method=add";
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces (HttpSession
     * session,HttpServletRequest request, HttpServletResponse response, FWViewBeanInterface viewBean)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = "";
        AMRevenuHistoriqueViewBean revenuViewBean = (AMRevenuHistoriqueViewBean) viewBean;
        destination = "/amal?userAction=amal.contribuable.contribuable.afficher&selectedId=";
        destination += revenuViewBean.getContribuable().getId();
        destination += "&selectedTabId=1";
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierEchec(javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        AMRevenuHistoriqueViewBean revenuViewBean = (AMRevenuHistoriqueViewBean) viewBean;
        String destination = super._getDestModifierEchec(session, request, response, viewBean);
        destination += "&selectedId=" + revenuViewBean.getId();
        destination += "&contribuableId=" + revenuViewBean.getContribuable().getId();
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces (HttpSession
     * session,HttpServletRequest request, HttpServletResponse response, FWViewBeanInterface viewBean)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = "";
        AMRevenuHistoriqueViewBean revenuViewBean = (AMRevenuHistoriqueViewBean) viewBean;
        destination = "/amal?userAction=amal.revenuHistorique.revenuHistorique.afficher&selectedId=";
        destination += revenuViewBean.getRevenuHistoriqueComplex().getSimpleRevenuHistorique().getIdRevenuHistorique();
        destination += "&contribuableId=";
        destination += revenuViewBean.getContribuable().getId();
        destination += "&selectedTabId=";

        try {
            destination += request.getParameter("selectedTabId");
        } catch (Exception e) {
            destination += "0";
        }
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces (HttpSession
     * session,HttpServletRequest request, HttpServletResponse response, FWViewBeanInterface viewBean)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String destination = "";
        AMRevenuHistoriqueViewBean revenuViewBean = (AMRevenuHistoriqueViewBean) viewBean;
        destination = "/amal?userAction=amal.contribuable.contribuable.afficher&selectedId=";
        destination += revenuViewBean.getContribuable().getId();
        destination += "&selectedTabId=1";
        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeSupprimer(HttpSession session, HttpServletRequest
     * request, HttpServletResponse response, FWViewBeanInterface viewBean)
     */
    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String selectedId = request.getParameter("selectedId");
        String contribuableId = request.getParameter("contribuableId");
        if (!JadeStringUtil.isEmpty(selectedId)) {
            AMRevenuHistoriqueViewBean revenuViewBean = new AMRevenuHistoriqueViewBean();
            revenuViewBean.setId(selectedId);
            if (!JadeStringUtil.isEmpty(contribuableId)) {
                revenuViewBean.setIdContribuable(contribuableId);
            }
            return revenuViewBean;
        } else {
            return null;
        }
    }

}
