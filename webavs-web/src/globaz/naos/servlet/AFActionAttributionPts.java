/*
 * Created on 13-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.api.BISession;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JATime;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.controleEmployeur.AFAttributionPtsListViewBean;
import globaz.naos.db.controleEmployeur.AFAttributionPtsViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité Control Employeur.
 * 
 * @author jpa
 */
public class AFActionAttributionPts extends AFDefaultActionChercher {

    /**
     * Constructeur d'AFActionControleEmployeur.
     * 
     * @param servlet
     */
    public AFActionAttributionPts(FWServlet servlet) {
        super(servlet);
    }

    private void _actionAfficherDerniere(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            AFAttributionPtsViewBean viewBean = new AFAttributionPtsViewBean();
            JSPUtils.setBeanProperties(request, viewBean);
            AFAttributionPtsListViewBean listViewBean = new AFAttributionPtsListViewBean();
            listViewBean.setISession(mainDispatcher.getSession());
            listViewBean.setForNumAffilie(viewBean.getNumAffilie());
            listViewBean.setOrderByModification(true);
            listViewBean.find();
            if (!listViewBean.isEmpty()) {
                viewBean = (AFAttributionPtsViewBean) listViewBean.getFirstEntity();
            } else {
                viewBean.setISession(mainDispatcher.getSession());
            }
            viewBean.setNom(rechercheNom(viewBean.getNumAffilie(), mainDispatcher.getSession()));
            request.setAttribute("viewBean", viewBean);
            _destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionAfficherHistorique(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            AFAttributionPtsListViewBean listViewBean = new AFAttributionPtsListViewBean();
            listViewBean.setISession(mainDispatcher.getSession());
            if (!JadeStringUtil.isEmpty(request.getParameter("selectedId"))) {
                // listViewBean.setForNumAffilie((String)request.getParameter("selectedId"));
                listViewBean.setLikeNumAffilie(request.getParameter("selectedId"));
            }
            request.setAttribute("listViewBean", listViewBean);
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionAfficherParticuliere(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            AFAttributionPtsViewBean viewBean = new AFAttributionPtsViewBean();
            viewBean.setISession(mainDispatcher.getSession());
            viewBean.setNom(rechercheNom(viewBean.getNumAffilie(), mainDispatcher.getSession()));
            viewBean.setIdAttributionPts(request.getParameter("selectedId"));
            viewBean.retrieve();
            if (JadeStringUtil.isEmpty(viewBean.getNom())) {
                viewBean.setNom(rechercheNom(viewBean.getNumAffilie(), mainDispatcher.getSession()));
            }
            request.setAttribute("viewBean", viewBean);
            _destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    private void _actionEnregistrer(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        try {
            AFAttributionPtsViewBean viewBean = new AFAttributionPtsViewBean();
            request.setAttribute("viewBean", viewBean);
            JSPUtils.setBeanProperties(request, viewBean);
            // On set les checkbox
            if (!JadeStringUtil.isNull(request.getParameter("isCasSpecial2"))) {
                if (request.getParameter("isCasSpecial2").equalsIgnoreCase("on")) {
                    viewBean.setIsCasSpecial(Boolean.TRUE);
                }
            }
            viewBean.setISession(mainDispatcher.getSession());
            viewBean.setLastUser(mainDispatcher.getSession().getUserId());
            JATime heure = new JATime(JACalendar.now());
            viewBean.setLastModification(JACalendar.todayJJsMMsAAAA() + " / " + heure.toStr(":"));
            viewBean.add();
            if (mainDispatcher.getSession().hasErrors()) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(mainDispatcher.getSession().getErrors().toString());
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext()
                .getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp?_method=add&_valid=error")
                .forward(request, response);
    }

    private void _actionListerHistorique(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String _destination = "";
        try {
            AFAttributionPtsListViewBean listViewBean = new AFAttributionPtsListViewBean();
            if (!JadeStringUtil.isEmpty(request.getParameter("likeNumAffilie"))) {
                listViewBean.setLikeNumAffilie(request.getParameter("likeNumAffilie"));
            }
            if (!JadeStringUtil.isEmpty(request.getParameter("likeUser"))) {
                listViewBean.setLikeUser(request.getParameter("likeUser"));
            }
            if (!JadeStringUtil.isEmpty(request.getParameter("forDateModification"))) {
                listViewBean.setForLastModification(request.getParameter("forDateModification"));
            }
            if (!JadeStringUtil.isEmpty(request.getParameter("forNumAffilie"))) {
                listViewBean.setForNumAffilie(request.getParameter("forNumAffilie"));
            }
            listViewBean.setISession(mainDispatcher.getSession());
            listViewBean.find();
            request.setAttribute("viewBean", listViewBean);
            _destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionChoisir(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.actionChoisir(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if (getAction().getActionPart().equals("enregistrer")) {
            _actionEnregistrer(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("afficherDerniere")) {
            _actionAfficherDerniere(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("historique")) {
            _actionAfficherHistorique(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("listerHistorique")) {
            _actionListerHistorique(session, request, response, dispatcher);
        }
        if (getAction().getActionPart().equals("afficherParticuliere")) {
            _actionAfficherParticuliere(session, request, response, dispatcher);
        }
    }

    private String rechercheNom(String numAffilie, BISession session) {
        try {
            if (!JadeStringUtil.isEmpty(numAffilie)) {
                AFAffiliationManager affManager = new AFAffiliationManager();
                affManager.setISession(session);
                affManager.setForAffilieNumero(numAffilie);
                affManager.setForTypesAffParitaires();
                affManager.find();
                if (!affManager.isEmpty()) {
                    AFAffiliation affiliation = (AFAffiliation) affManager.getFirstEntity();
                    TITiersViewBean tiers = new TITiersViewBean();
                    tiers.setISession(session);
                    tiers.setIdTiers(affiliation.getIdTiers());
                    tiers.retrieve();
                    if (!tiers.isNew()) {
                        return tiers.getNomPrenom();
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } catch (Exception ex) {
            return "";
        }
    }
}