/*
 * Créé le 2 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.leo.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.leo.vb.process.LEGenererEtapesSuivantesViewBean;
import globaz.leo.vb.process.LEListeFormulesEnAttenteViewBean;
import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author ald
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class LEProcessServletAction extends FWDefaultServletAction {

    /**
     * @param servlet
     */
    public LEProcessServletAction(FWServlet servlet) {
        super(servlet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // pas de passage dans le dispatcher sur cette action, les droits sont
        // vérifier au moment de l'exécution du process
        if (getAction().getActionPart().endsWith("updateFormule")) {
            String destination = "";
            LEListeFormulesEnAttenteViewBean viewBean = new LEListeFormulesEnAttenteViewBean();
            viewBean.setISession(mainDispatcher.getSession());
            if ((request.getParameter("forCategorie") != null) && request.getParameter("forCategorie").length() > 0) {
                // on a une catégorie de séléctionner
                BSession sessionUt = (BSession) ((globaz.framework.controller.FWController) session
                        .getAttribute("objController")).getSession();
                Vector formule = viewBean.getFormulesList(sessionUt, request.getParameter("forCategorie"));
                if (formule.size() > 0) {
                    viewBean.setFormule(formule);
                }
                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", viewBean);
                destination = getRelativeURL(request, session) + "_de.jsp";
                servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
            } else {
                // dans le cas où la catégorie est mise à blanc
                Vector formule = viewBean.getFormulesList();
                if (formule.size() > 0) {
                    viewBean.setFormule(formule);
                }
                viewBean.setISession(mainDispatcher.getSession());
                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", viewBean);
                destination = getRelativeURL(request, session) + "_de.jsp";
                servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
            }
        }
        // pas de passage dans le dispatcher sur cette action, les droits sont
        // vérifier au moment de l'exécution du process
        if (getAction().getActionPart().endsWith("updateFormuleEtape")) {
            String destination = "";
            LEGenererEtapesSuivantesViewBean viewBean = new LEGenererEtapesSuivantesViewBean();
            viewBean.setISession(mainDispatcher.getSession());
            if ((request.getParameter("forCategorie") != null) && request.getParameter("forCategorie").length() > 0) {
                // on a une catégorie de séléctionner
                BSession sessionUt = (BSession) ((globaz.framework.controller.FWController) session
                        .getAttribute("objController")).getSession();
                Vector formule = viewBean.getFormulesList(sessionUt, request.getParameter("forCategorie"));
                if (formule.size() > 0) {
                    viewBean.setFormule(formule);
                }
                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", viewBean);
                destination = getRelativeURL(request, session) + "_de.jsp";
                servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
            } else {
                // dans le cas où la catégorie est mise à blanc
                Vector formule = viewBean.getFormulesList();
                if (formule.size() > 0) {
                    viewBean.setFormule(formule);
                }
                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", viewBean);
                destination = getRelativeURL(request, session) + "_de.jsp";
                servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
            }
        } else if (getAction().getActionPart().endsWith("afficherEtape")) {
            String destination = "";
            LEGenererEtapesSuivantesViewBean viewBean = new LEGenererEtapesSuivantesViewBean();
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            // passage dans le dispatcher
            viewBean = (LEGenererEtapesSuivantesViewBean) mainDispatcher.dispatch(viewBean, getAction());
            destination = getRelativeURL(request, session) + "_de.jsp";
            servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
        }
    }
}