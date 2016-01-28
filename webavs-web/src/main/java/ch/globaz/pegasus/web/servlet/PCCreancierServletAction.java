package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWController;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pegasus.vb.creancier.PCCreanceAccordeeViewBean;
import globaz.pegasus.vb.creancier.PCCreancierViewBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.pegasus.business.constantes.IPCActions;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordee;

public class PCCreancierServletAction extends PCAbstractServletAction {
    private String idDemande = null;
    private String idDecision = null;

    /**
     * Constructeur
     * 
     * @param aServlet
     */
    public PCCreancierServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCCreanceAccordeeViewBean) {
            return "pegasus?userAction=" + IPCActions.ACTION_CREANCIER_LISTE_CREANCES + ".afficher";
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCCreanceAccordeeViewBean) {
            return "pegasus?userAction=" + IPCActions.ACTION_CREANCIER_LISTE_CREANCES + ".afficher";
        } else {
            return super._getDestModifierSucces(session, request, response, viewBean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAjouter(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PCCreancierViewBean) {
            // recupère demande de id
            ((PCCreancierViewBean) viewBean).getCreancier().getSimpleCreancier().setIdDemande(idDemande);
        } else if (viewBean instanceof PCCreanceAccordeeViewBean) {
            ((PCCreanceAccordeeViewBean) viewBean).setIdDemandePc(idDemande);
            ((PCCreanceAccordeeViewBean) viewBean).setIdDecision(idDecision);
        }
        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        setIdDemandByViewBean(viewBean);
        setListCreanceAccordees(request, viewBean);
        return super.beforeAjouter(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        setIdDemandByViewBean(viewBean);
        setListCreanceAccordees(request, viewBean);
        return super.beforeModifier(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        setIdDemandByViewBean(viewBean);
        return super.beforeSupprimer(session, request, response, viewBean);
    }

    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        idDemande = request.getParameter("idDemandePc");
        idDecision = request.getParameter("idDecision");

        super.doAction(session, request, response, mainController);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWDefaultServletAction# goSendRedirectWithoutParameters(java.lang.String,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goSendRedirect(String url, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!JadeStringUtil.isEmpty(idDemande)) {
            url = url + "&idDemandePc=" + idDemande;
        }
        if (!JadeStringUtil.isEmpty(idDecision)) {
            url = url + "&idDecision=" + idDecision;
        }

        super.goSendRedirect(url, request, response);
    }

    private void setIdDemandByViewBean(FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCCreancierViewBean) {
            idDemande = ((PCCreancierViewBean) viewBean).getCreancier().getSimpleCreancier().getIdDemande();
        } else if (viewBean instanceof PCCreanceAccordeeViewBean) {
            idDecision = ((PCCreanceAccordeeViewBean) viewBean).getIdDecision();
            idDemande = ((PCCreanceAccordeeViewBean) viewBean).getIdDemandePc();
        }
    }

    private void setListCreanceAccordees(HttpServletRequest request, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCCreanceAccordeeViewBean) {
            List<SimpleCreanceAccordee> list = new ArrayList<SimpleCreanceAccordee>();
            SimpleCreanceAccordee sAccordee = null;

            Map<String, String[]> map = request.getParameterMap();
            Set<Map.Entry<String, String[]>> set = map.entrySet();

            Iterator<Entry<String, String[]>> iter = set.iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String[]> entree = iter.next();
                String[] ids = entree.getKey().split("_");
                if (ids[0].equals("creanceAcc")) {
                    sAccordee = new SimpleCreanceAccordee();
                    sAccordee.setIdCreancier(ids[1]);
                    sAccordee.setIdPCAccordee(ids[2]);
                    if (ids.length == 4) {
                        sAccordee.setId(ids[3]);
                    }
                    String t = entree.getValue().toString();
                    sAccordee.setMontant(entree.getValue()[0]);

                    list.add(sAccordee);
                }
            }
            ((PCCreanceAccordeeViewBean) viewBean).setListCreanceAccordees(list);
        }
    }
}
