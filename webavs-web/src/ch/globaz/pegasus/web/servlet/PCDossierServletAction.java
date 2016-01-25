package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.pegasus.vb.dossier.PCDossierViewBean;
import globaz.prestation.ged.PRGedAffichageDossier;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PCDossierServletAction extends PCAbstractServletAction {

    public PCDossierServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    /**
     * Affichage du dossier en GED
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @throws ServletException
     * @throws IOException
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws NullPointerException
     * @throws ClassCastException
     * @throws JadeClassCastException
     */
    public void actionAfficherDossierGed(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException,
            JadeServiceLocatorException, JadeServiceActivatorException, NullPointerException, ClassCastException,
            JadeClassCastException {
        PRGedAffichageDossier.actionAfficherDossierGed(session, request, response, mainDispatcher, viewBean);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCDossierViewBean) {
            PCDossierViewBean vb = (PCDossierViewBean) viewBean;
            // String appPart = this.getAction().getApplicationPart();
            // return "/" + appPart + "?userAction=" + appPart + ".droit.droit.chercher&idDroit="
            // + request.getParameter("idDroit") + "&noVersion=" + request.getParameter("noVersion")
            // + "&idDemandePc=" + request.getParameter("idDemandePc") + "&idDossier="
            // + request.getParameter("idDossier");

            return getActionFullURL() + ".chercher&idDossier=" + vb.getDossier().getDossier().getIdDossier();
        } else {

            return super._getDestAjouterSucces(session, request, response, viewBean);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAjouter(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.actionAjouter(session, request, response, mainDispatcher);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.actionModifier(session, request, response, mainDispatcher);
    }

}
