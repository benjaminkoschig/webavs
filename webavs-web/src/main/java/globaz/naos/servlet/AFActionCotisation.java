/*
 * Created on 13-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.cotisation.AFCotisationViewBean;
import globaz.naos.translation.CodeSystem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Classe permettant la gestion des actions pour l'entité Cotisation.
 * 
 * @author sau
 */
public class AFActionCotisation extends AFDefaultActionChercher {

    public final static String ACTION_AFFICHER_CREE_EXCEPTION = "afficherCreeException";
    public final static String ACTION_AFFICHER_MODIFIER_EXCEPTION = "afficherModifierException";
    public final static String ACTION_CREE_EXCEPTION = "creeException";
    public final static String ACTION_RELOAD = "reload";

    /**
     * Constructeur d'AFActionCotisation.
     * 
     * @param servlet
     */
    public AFActionCotisation(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Destination après une suppression réussie dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(HttpSession,
     *      HttpServletRequest, HttpServletResponse,
     *      FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFCotisationViewBean vBean = (AFCotisationViewBean) viewBean;
        String planAffiliationId = vBean.getPlanAffiliationId();

        return getActionFullURL() + ".chercher&planAffiliationId=" + planAffiliationId;
    }

    /**
     * Action utilisée pour la suppression d'une affiliation.
     * 
     * @see globaz.naos.db.affiliation.AFAffiliation#_supression(String idTiers, String
     *      affiliationId)
     * 
     * @param session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void actionAfficherCreeException(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException,
            IOException {

        String _destination = "";

        AFCotisationViewBean viewBean = new AFCotisationViewBean();
        String cotisationId = request.getParameter("cotisationId");

        try {
            getAction().setRight(FWSecureConstants.ADD);
            getAction().changeActionPart(FWAction.ACTION_AFFICHER);

            viewBean = (AFCotisationViewBean) FWViewBeanActionFactory.newInstance(getAction(),
                    mainDispatcher.getPrefix());
            viewBean.setCotisationId(cotisationId);
            viewBean = (AFCotisationViewBean) mainDispatcher.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            // _destination = getRelativeURL(request, session) +
            // "_de.jsp?_valid=fail";
            _destination = ERROR_PAGE;
        } else {
            _destination = getRelativeURL(request, session) + "Exception_de.jsp?_method=add";
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action utilisée pour modifier une exception
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @throws ServletException
     * @throws IOException
     */
    protected void actionAfficherModifierException(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException,
            IOException {

        String _destination = "";

        AFCotisationViewBean viewBean = new AFCotisationViewBean();
        String cotisationId = request.getParameter("cotisationId");

        try {
            getAction().setRight(FWSecureConstants.UPDATE);
            getAction().changeActionPart(FWAction.ACTION_AFFICHER);

            viewBean = (AFCotisationViewBean) FWViewBeanActionFactory.newInstance(getAction(),
                    mainDispatcher.getPrefix());
            viewBean.setCotisationId(cotisationId);
            viewBean.setException("true");// détermine qu'il s'agit d'une
            // exception de cotisation
            viewBean = (AFCotisationViewBean) mainDispatcher.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = ERROR_PAGE;
        } else {
            _destination = getRelativeURL(request, session) + "Exception_de.jsp";
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action utilisée pour la recherche.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(HttpSession,
     *      HttpServletRequest, HttpServletResponse,
     *      FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination;

        String affiliationId = request.getParameter("affiliationId");
        String planAffiliationId = request.getParameter("planAffiliationId");
        String adhesionId = request.getParameter("adhesionId");
        String planId = request.getParameter("planId");
        String planCaisseId = request.getParameter("planCaisseId");

        AFCotisationViewBean vBean = new AFCotisationViewBean();
        try {
            vBean = (AFCotisationViewBean) mainDispatcher.dispatch(vBean, getAction());
            if (!JadeStringUtil.isEmpty(planId)) {
                planAffiliationId = vBean._planAutomatique(affiliationId, planId);
            }
            if (planAffiliationId != null) {
                vBean.setPlanAffiliationId(planAffiliationId);
            }
            if (adhesionId != null) {
                vBean.setAdhesionId(adhesionId);
            }
            if (affiliationId != null) {
                vBean.setAffiliationId(affiliationId);
            }
            if (planCaisseId != null) {
                vBean.setPlanCaisseId(planCaisseId);
            }
        } catch (Exception e) {
            vBean.setMsgType(FWViewBeanInterface.ERROR);
            vBean.setMessage("ERROR Unable to read data");
        }

        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", vBean);

        if (vBean.getMessage().equals(FWViewBeanInterface.ERROR)) {
            destination = ERROR_PAGE;
        } else {
            destination = getRelativeURL(request, session) + "_rc.jsp";
            /*
             * if (JadeStringUtil.isEmpty(affiliationId)) {
             * 
             * destination = getRelativeURL(request,session)+"_rc.jsp?planAffiliationId=" + planAffiliationId; } else {
             * destination = getRelativeURL(request,session)+"_rc.jsp?affiliationId=" + affiliationId; }
             */
            /*
             * if( ! JadeStringUtil.isEmpty(newAdhesionId)) { destination = "/" + getAction().getApplicationPart() +
             * "?userAction=" + "naos.adhesion.adhesion.chercher&affiliationId=" + affiliationId; } else { destination =
             * getRelativeURL(request,session)+"_rc.jsp?planAffiliationId=" + planAffiliationId; }
             */
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Action utilisée pour la suppression d'une affiliation.
     * 
     * @see globaz.naos.db.affiliation.AFAffiliation#_supression(String idTiers, String
     *      affiliationId)
     * 
     * @param session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void actionCreeException(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String _destination = "";
        String selectedId = request.getParameter("selectedId");

        AFCotisationViewBean viewBean = (AFCotisationViewBean) session.getAttribute("viewBean");

        if(CodeSystem.TYPE_ASS_CRP_BASIC.equals(viewBean.getAssurance().getTypeAssurance())) {
            viewBean.setCotisationId(selectedId);
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(viewBean.getSession().getLabel("PAS_EXCEPTION_ASSURANCE"));
        }

        if (!viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            String periodiciteNouvelleMasse = request.getParameter("periodiciteNouvelleMasse");
            String nouvelleMasse = request.getParameter("nouvelleMasse");

            try {
                JSPUtils.setBeanProperties(request, viewBean);
                viewBean.updateMasseAnnuelle(periodiciteNouvelleMasse, nouvelleMasse);
                viewBean.add();
                String errors = viewBean.getSession().getErrors().toString();
                if (!JadeStringUtil.isEmpty(errors)) {
                    viewBean.setCotisationId(selectedId);
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                    viewBean.setMessage(errors);
                }
            } catch (Exception e) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(e.getMessage());
            }
        }
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            _destination = getRelativeURL(request, session) + "Exception_de.jsp?_valid=fail&_method=add";
        } else {
            _destination = "/" + getAction().getApplicationPart() + "?userAction=naos.cotisation.cotisation.chercher";
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Traitement des actions non standard.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(HttpSession,
     *      HttpServletRequest, HttpServletResponse,
     *      FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {

        if (ACTION_AFFICHER_CREE_EXCEPTION.equals(getAction().getActionPart())) {
            actionAfficherCreeException(session, request, response, dispatcher);
        } else if (ACTION_CREE_EXCEPTION.equals(getAction().getActionPart())) {
            actionCreeException(session, request, response);
        } else if (ACTION_AFFICHER_MODIFIER_EXCEPTION.equals(getAction().getActionPart())) {
            actionAfficherModifierException(session, request, response, dispatcher);
        } else if (ACTION_RELOAD.equals(getAction().getActionPart())) {
            // reafraichir les menus deroulants de la page (en particulier le
            // menu des plans de caisse)
            AFCotisationViewBean viewBean = (AFCotisationViewBean) session.getAttribute("viewBean");

            try {
                JSPUtils.setBeanProperties(request, viewBean);
            } catch (Exception e) {
                viewBean.setMessage(e.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            servlet.getServletContext().getRequestDispatcher(getRelativeURL(request, session) + "_de.jsp")
                    .forward(request, response);
        } else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /**
     * Effectue des traitements avant sélection.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionSelectionner(HttpSession,
     *      HttpServletRequest, HttpServletResponse,
     *      FWDispatcher)
     */
    @Override
    protected void actionSelectionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        AFCotisationViewBean viewBean = (AFCotisationViewBean) session.getAttribute("viewBean");

        String periodiciteNouvelleMasse = request.getParameter("periodiciteNouvelleMasse");
        String nouvelleMasse = request.getParameter("nouvelleMasse");

        try {
            viewBean.updateMasseAnnuelle(periodiciteNouvelleMasse, nouvelleMasse);
            viewBean.updatePeriodicite();
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);

        super.actionSelectionner(session, request, response, mainDispatcher);
    }

    /**
     * Effectue des traitements avant l'ajout d'une nouvelle entité dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAjouter(HttpSession,
     *      HttpServletRequest, HttpServletResponse,
     *      FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String periodiciteNouvelleMasse = request.getParameter("periodiciteNouvelleMasse");
        String nouvelleMasse = request.getParameter("nouvelleMasse");
        String planAffiliationId = request.getParameter("newPlanAffiliationId");
        String planCaisseId = request.getParameter("planCaisseId");

        AFCotisationViewBean vBean = (AFCotisationViewBean) viewBean;
        try {
            vBean.setPlanAffiliationId(planAffiliationId);
            vBean.setPlanCaisseId(planCaisseId);
            vBean.updateMasseAnnuelle(periodiciteNouvelleMasse, nouvelleMasse);
            vBean.updatePeriodicite();
        } catch (Exception e) {
            vBean.setMsgType(FWViewBeanInterface.ERROR);
            vBean.setMessage(e.getMessage());
        }

        return vBean;
    }

    /**
     * Effectue des traitements avant la modification d'une entité dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeModifier(HttpSession,
     *      HttpServletRequest, HttpServletResponse,
     *      FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFCotisationViewBean vBean = (AFCotisationViewBean) viewBean;
        // System.out.println("exception? "+vBean.getException());

        String periodiciteNouvelleMasse = request.getParameter("periodiciteNouvelleMasse");
        String nouvelleMasse = request.getParameter("nouvelleMasse");

        String planAffiliationId = request.getParameter("newPlanAffiliationId");
        String planCaisseId = request.getParameter("planCaisseId");

        try {
            // si on est dans le cas d'une modification d'exception
            if (vBean.getException().equals("false")) {
                vBean.setPlanAffiliationId(planAffiliationId);
                vBean.setPlanCaisseId(planCaisseId);
            }
            vBean.updateMasseAnnuelle(periodiciteNouvelleMasse, nouvelleMasse);
            vBean.updatePeriodicite();
        } catch (Exception e) {
            vBean.setMsgType(FWViewBeanInterface.ERROR);
            vBean.setMessage(e.getMessage());
        }

        return vBean;
    }

    /**
     * Effectue des traitements avant la saisie d'une nouvelle entité.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(HttpSession,
     *      HttpServletRequest, HttpServletResponse,
     *      FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFCotisationViewBean vBean = (AFCotisationViewBean) viewBean;
        vBean.setPlanAffiliationId(request.getParameter("planAffiliationId"));
        vBean.setAffiliationId(request.getParameter("affiliationId"));
        vBean.setPlanCaisseId(request.getParameter("planCaisseId"));

        return vBean;
    }

}
