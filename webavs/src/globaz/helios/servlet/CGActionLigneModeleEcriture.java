package globaz.helios.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.helios.db.modeles.CGLigneModeleEcritureViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion des lignes d'un modèle d'écriture.
 * 
 * @author DDA
 */
public class CGActionLigneModeleEcriture extends CGDefaultServletAction {

    public CGActionLigneModeleEcriture(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";

        try {
            CGLigneModeleEcritureViewBean vBean = new CGLigneModeleEcritureViewBean();

            String selectedId = request.getParameter("selectedId");
            vBean.setIdModeleEcriture(selectedId);

            CGExerciceComptable exerciceComptable = (CGExerciceComptable) session
                    .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
            if (exerciceComptable != null) {
                vBean.setIdMandat(exerciceComptable.getIdMandat());
            }

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            action.changeActionPart(FWAction.ACTION_CHERCHER);

            vBean = (CGLigneModeleEcritureViewBean) mainDispatcher.dispatch(vBean, action);

            setSessionAttribute(session, VIEWBEAN, vBean);
            destination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}
