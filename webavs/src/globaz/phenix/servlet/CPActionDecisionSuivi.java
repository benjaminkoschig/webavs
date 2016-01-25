package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.principale.CPDecisionListViewBean;
import globaz.phenix.db.principale.CPDecisionListerViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */
public class CPActionDecisionSuivi extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionDecisionSuivi(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     */
    @Override
    public void actionChercher(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher dispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Get value from request
        // --- Variables
        CPDecisionListerViewBean viewBean = new CPDecisionListerViewBean();
        try {
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            session.setAttribute("viewBean", viewBean);
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        super.actionChercher(session, request, response, dispatcher);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        CPDecisionListViewBean viewBean = new CPDecisionListViewBean();
        try {
            /*
             * creation automatique du listviewBean
             */
            globaz.globall.api.BISession bSession = globaz.phenix.translation.CodeSystem.getSession(session);
            viewBean.setSession((globaz.globall.db.BSession) bSession);
            /*
             * set automatique des properietes du listViewBean depuis la requete
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setForIdPassage(request.getParameter("idPassage"));
            // Ordre d'affichage
            viewBean.setUseTiers(Boolean.TRUE);
            String testChamp = "";
            // Si champ nom renseigné pour la recherche => tri par nom
            testChamp = request.getParameter("likeNom");
            if (!JadeStringUtil.isEmpty(testChamp)) {
                viewBean.orderByNom();
            }
            // Si champ prénom renseigné pour la recherche => tri par prénom
            testChamp = request.getParameter("likePrenom");
            if (!JadeStringUtil.isEmpty(testChamp)) {
                viewBean.orderByPrenom();
            }
            viewBean.orderByNumAffilie();
            viewBean.orderByAnneeDecision();
            viewBean.orderByIdDecision();
            viewBean.changeManagerSize(20);
            // le fin est passé dans le helper
            // viewBean.find();
            request.setAttribute("viewBean", viewBean);
            mainDispatcher.dispatch(viewBean, getAction());
            /*
             * destination : remarque : si erreur, on va quand meme sur la liste avec le bean vide en erreur
             */
            _destination = getRelativeURLwithoutClassPart(request, session) + "decisionSuivi_rcListe.jsp";
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }
}
