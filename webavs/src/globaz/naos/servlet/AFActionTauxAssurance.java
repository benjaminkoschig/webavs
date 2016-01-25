/*
 * Created on 18-Jan-05
 */
package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.db.tauxAssurance.AFTauxAssuranceViewBean;
import globaz.naos.translation.CodeSystem;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classe permettant la gestion des actions pour l'entité TauxAssurance.
 * 
 * @author sau
 */
public class AFActionTauxAssurance extends AFDefaultActionChercher {

    /**
     * Constructeur d'AFActionTauxAssurance.
     * 
     * @param servlet
     */
    public AFActionTauxAssurance(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Effectue les traitements pour en afficher les détails.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = "";
        try {
            FWAction action = getAction();

            String method = request.getParameter("_method");
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                action.changeActionPart(FWAction.ACTION_NOUVEAU);
            }

            String selectedId = request.getParameter("selectedId");
            AFTauxAssuranceViewBean viewBean = new AFTauxAssuranceViewBean();
            viewBean.setTauxAssuranceId(selectedId);

            if (action.getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                viewBean = (AFTauxAssuranceViewBean) beforeNouveau(session, request, response, viewBean);
            }

            viewBean = (AFTauxAssuranceViewBean) beforeAfficher(session, request, response, viewBean);
            viewBean = (AFTauxAssuranceViewBean) mainDispatcher.dispatch(viewBean, action);

            boolean isFADPersonnel = CodeSystem.GENRE_ASS_PERSONNEL.equalsIgnoreCase(viewBean.getAssurance()
                    .getAssuranceGenre())
                    && CodeSystem.TYPE_ASS_FRAIS_ADMIN.equalsIgnoreCase(viewBean.getAssurance().getTypeAssurance());
            if ((method != null) && !method.equalsIgnoreCase("ADD") && !isFADPersonnel) {
                viewBean.setSaisieGenreValeur(AFTauxAssurance.SAISIE_BLOQUER);
            } else {
                viewBean.setSaisieGenreValeur(AFTauxAssurance.SAISIE_LIBRE);
            }

            if (viewBean.getAssurance().getAssuranceGenre().equals(CodeSystem.GENRE_ASS_PARITAIRE)) {
                viewBean.setSaisieSexe(AFTauxAssurance.SAISIE_BLOQUER);
            } else {
                viewBean.setSaisieSexe(AFTauxAssurance.SAISIE_LIBRE);
            }

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Action utilisée pour la recherche.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        AFTauxAssuranceViewBean vBean = new AFTauxAssuranceViewBean();
        vBean.setAssuranceId(request.getParameter("assuranceId"));

        this.actionChercher(vBean, session, request, response, mainDispatcher);
    }

    /**
     * Effectue des traitements avant l'ajout d'une nouvelle entité dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAjouter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFTauxAssurance vBean = (AFTauxAssurance) viewBean;
        if (CodeSystem.GEN_VALEUR_ASS_TAUX.equals(vBean.getGenreValeur())) {
            // Nothing to do
        } else if (CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equals(vBean.getGenreValeur())) {
            vBean.setValeurEmployeur(request.getParameter("valeurEmployeurVar"));
            vBean.setValeurEmploye(request.getParameter("valeurEmployeVar"));
            vBean.setFraction(request.getParameter("fractionVar"));
        } else if (CodeSystem.GEN_VALEUR_ASS_MONTANT.equals(vBean.getGenreValeur())) {
            vBean.setValeurEmployeur(request.getParameter("valeurEmployeurMon"));
            vBean.setValeurEmploye(request.getParameter("valeurEmployeMon"));
        }
        return vBean;
    }

    /**
     * Effectue des traitements avant la modification d'une entité dans la DB.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeModifier(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        return beforeAjouter(session, request, response, viewBean);
    }

    /**
     * Effectue des traitements avant la saisie d'une nouvelle entité.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeNouveau(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        AFTauxAssuranceViewBean vBean = (AFTauxAssuranceViewBean) viewBean;
        vBean.setAssuranceId(request.getParameter("assuranceId"));
        vBean.setGenreValeur(CodeSystem.GEN_VALEUR_ASS_TAUX);
        return vBean;
    }
}
