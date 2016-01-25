package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWHelper;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionValiderViewBean;
import globaz.phenix.db.principale.CPEnteteViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class CPActionDecisionValider extends FWDefaultServletAction {
    public final static String CHECKBOX_CHECKED = "on";

    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionDecisionValider(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     * 
     * Méthode inutile
     */
    // public void actionAfficher(
    // javax.servlet.http.HttpSession session,
    // javax.servlet.http.HttpServletRequest request,
    // javax.servlet.http.HttpServletResponse response,
    // globaz.framework.controller.FWDispatcher mainController)
    // throws javax.servlet.ServletException, java.io.IOException {
    // // --- Get value from request
    // CPDecisionValiderViewBean viewBean = new CPDecisionValiderViewBean();
    // try {
    // globaz.globall.api.BISession bSession =
    // globaz.phenix.translation.CodeSystem.getSession(session);
    // viewBean.setSession((globaz.globall.db.BSession) bSession);
    // viewBean.setIdTiers((String) session.getAttribute("idTiers"));
    // viewBean.setIdDecision((String) request.getParameter("selectedId"));
    // //déplacer dans le helper
    // //viewBean.retrieve();
    // //FWHelper.afterExecute(viewBean);
    // if (request.getParameter("doc") != null) {
    // // permettre d'afficher dans une autre page le document qui vient d'être
    // généré
    // String[] docs = request.getParameterValues("doc");
    // for (int i = 0; i < docs.length; i++) {
    // viewBean.addDoc(docs[i]);
    // }
    // }
    // //mainController.dispatch(viewBean, getAction());
    // } catch (Exception e) {
    // viewBean.setMessage(e.getMessage());
    // viewBean.setMsgType(FWViewBeanInterface.ERROR);
    // }
    // // --- Check view bean
    // session.setAttribute("viewBean", viewBean);
    // super.actionAfficher(session, request, response, mainController);
    // // servlet
    // // .getServletContext()
    // // .getRequestDispatcher(
    // // getRelativeURLwithoutClassPart(request, session) +
    // "decisionValider_de.jsp?_method=upd")
    // // .forward(request, response);
    // }
    @Override
    public void actionAfficher(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        CPDecisionValiderViewBean viewBean = new CPDecisionValiderViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            viewBean.setIdTiers((String) session.getAttribute("idTiers"));
            viewBean.setIdDecision(request.getParameter("selectedId"));
            viewBean.retrieve();
            FWHelper.afterExecute(viewBean);
            if (request.getParameter("doc") != null) {
                // permettre d'afficher dans une autre page le document qui
                // vient d'être généré
                String[] docs = request.getParameterValues("doc");
                for (int i = 0; i < docs.length; i++) {
                    viewBean.addDoc(docs[i]);
                }
            }
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        // --- Check view bean
        session.setAttribute("viewBean", viewBean);
        servlet.getServletContext()
                .getRequestDispatcher(
                        getRelativeURLwithoutClassPart(request, session) + "decisionValider_de.jsp?_method=upd")
                .forward(request, response);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     * 
     * On conserve le forward car redirection sur decison_rc et non sur decisionValider_rc Retrieve executé dans le
     * helper de la classe
     */
    @Override
    public void actionChercher(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {

        // --- Variables
        CPEnteteViewBean viewBean = new CPEnteteViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            session.removeAttribute("idAffiliation");
            if (!JadeStringUtil.isNull(request.getParameter("idTiers"))) {
                session.setAttribute("idTiers", request.getParameter("idTiers"));
            } else {
                session.setAttribute("idTiers", request.getParameter("selectedId"));
            }
            session.setAttribute("idAffiliation", request.getParameter("selectedId2"));
            viewBean.setIdTiers((String) session.getAttribute("idTiers"));
            viewBean.setIdAffiliation((String) session.getAttribute("idAffiliation"));
            // transféré dans CPDecisionValiderHelper
            // viewBean.retrieve();
            mainController.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        session.setAttribute("viewBean", viewBean);
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(request, response);
        } else {
            servlet.getServletContext()
                    .getRequestDispatcher(getRelativeURLwithoutClassPart(request, session) + "decision_rc.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        if ("chercher".equals(getAction().getActionPart())) {
            actionChercher(session, request, response, mainDispatcher);
        } else if ("modifierProvenanceJournalRetour".equals(getAction().getActionPart())) {
            actionModifierProvenanceJournalRetour(session, request, response, mainDispatcher);
        }
    }

    /**
     * Réimplémentation de la méthode update à cause du problème setProperties de l'action par défaut qui remet les
     * booléens à false Date de création : (03.05.2002 08:57:00)
     * 
     * On conserve les forwards (destination spécifique en erreur et réussite) update executé dans le Helper, plus
     * besoin du afterExecute (appellé directement par la classe parente)
     */
    @Override
    public void actionModifier(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Variables
        CPDecisionValiderViewBean viewBean = (CPDecisionValiderViewBean) session.getAttribute("viewBean");

        if (viewBean == null) {
            viewBean = new CPDecisionValiderViewBean();
        }
        viewBean.setMsgType("");
        try {
            viewBean.setIdPassage(request.getParameter("idPassage"));
        } catch (Exception e) {
            servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(request, response);
        }
        try {
            /*
             * PO 01562: possibilité de forcer la validation et de bypasser les plausibilités décrit dans le PO
             */
            boolean tmp = CHECKBOX_CHECKED.equalsIgnoreCase(request.getParameter("validationForcee")) ? true : false;
            viewBean.setValidationForcee(new Boolean(tmp));
            tmp = CHECKBOX_CHECKED.equalsIgnoreCase(request.getParameter("miseEnGEDValidationRetour")) ? true : false;
            viewBean.setMiseEnGEDValidationRetour(new Boolean(tmp));
        } catch (Exception e) {
            servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(request, response);
        }
        try {
            viewBean.setDernierEtat(CPDecision.CS_VALIDATION);
            // viewBean.update();
            // FWHelper.afterExecute(viewBean);
            mainController.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        // --- Check view bean
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getRelativeURLwithoutClassPart(request, session) + "decisionValider_de.jsp?_method=upd")
                    .forward(request, response);
        } else {
            servlet.getServletContext()
                    .getRequestDispatcher(getActionFullURL() + ".chercher&idTiers=" + viewBean.getIdTiers())
                    .forward(request, response);
        }
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param mainController
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * 
     *             Forward laissé identique (redirection spécifique) Execution de la méthode update dans le Helper
     *             AfterExecute supprimé (appellé depuis la classe parente)
     */
    public void actionModifierProvenanceJournalRetour(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            globaz.framework.controller.FWDispatcher mainController) throws javax.servlet.ServletException,
            java.io.IOException {
        // --- Variables
        CPDecisionValiderViewBean viewBean = (CPDecisionValiderViewBean) session.getAttribute("viewBean");
        if (viewBean == null) {
            viewBean = new CPDecisionValiderViewBean();
        }
        viewBean.setMsgType("");
        try {
            viewBean.setIdPassage(request.getParameter("idPassage"));
        } catch (Exception e) {
            servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(request, response);
        }
        try {
            /*
             * PO 01562: possibilité de forcer la validation et de bypasser les plausibilités décrit dans le PO
             */
            boolean tmp = CHECKBOX_CHECKED.equalsIgnoreCase(request.getParameter("validationForcee")) ? true : false;
            viewBean.setValidationForcee(new Boolean(tmp));
        } catch (Exception e) {
            servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(request, response);
        }
        try {
            viewBean.setDernierEtat(CPDecision.CS_VALIDATION);
            // viewBean.update();
            // FWHelper.afterExecute(viewBean);
            mainController.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        // --- Check view bean
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getRelativeURLwithoutClassPart(request, session) + "decisionValider_de.jsp?_method=upd")
                    .forward(request, response);
        } else {
            servlet.getServletContext()
                    .getRequestDispatcher("/phenix?userAction=phenix.communications.validationJournalRetour.chercher")
                    .forward(request, response);
        }
    }

}
