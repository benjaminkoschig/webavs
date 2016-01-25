package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.helios.db.comptes.CGAdvancedEcritureListViewBean;
import globaz.helios.db.comptes.CGEcritureViewBean;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.helios.translation.CodeSystem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la recherche des écritures d'un journal.
 * 
 * @author DDA
 * 
 */
public class CGActionEcriture extends CGActionNeedExerciceComptable {

    public CGActionEcriture(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(HttpSession session, HttpServletRequest
     *      request, HttpServletResponse response, FWDispatcher mainDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";

        try {
            CGEcritureViewBean vBean = new CGEcritureViewBean();
            String selectedId = request.getParameter("selectedId");
            vBean.setIdJournal(selectedId);

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            action.changeActionPart(FWAction.ACTION_CHERCHER);

            vBean = (CGEcritureViewBean) mainDispatcher.dispatch(vBean, action);

            CGExerciceComptable exerciceComptable = (CGExerciceComptable) session
                    .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
            if (exerciceComptable != null) {
                vBean.setIdExerciceComptable(exerciceComptable.getIdExerciceComptable());
            }

            setSessionAttribute(session, VIEWBEAN, vBean);

            destination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        if ("listerEcritures".equals(getAction().getActionPart())) {
            actionListerEcritures(session, request, response, mainDispatcher);
        }
    }

    protected void actionListerEcritures(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = null;
        try {
            CGAdvancedEcritureListViewBean listViewBean = new CGAdvancedEcritureListViewBean();
            listViewBean.setSession((BSession) CodeSystem.getSession(session));

            listViewBean.setForIdJournal(request.getParameter("selectedId"));
            JSPUtils.setBeanProperties(request, listViewBean);

            CGExerciceComptable exerciceComptable = (CGExerciceComptable) session
                    .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
            listViewBean.setForIdExerciceComptable(exerciceComptable.getIdExerciceComptable());
            listViewBean.setForIdMandat(exerciceComptable.getIdMandat());

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));

            action.changeActionPart(FWAction.ACTION_LISTER);

            listViewBean = (CGAdvancedEcritureListViewBean) beforeLister(session, request, response, listViewBean);
            listViewBean = (CGAdvancedEcritureListViewBean) mainDispatcher.dispatch(listViewBean, action);

            request.setAttribute(VIEWBEAN, listViewBean);

            setSessionAttribute(session, "listViewBean", listViewBean);

            destination = getRelativeURL(request, session) + "_rcListe.jsp";

        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        ((BManager) viewBean).changeManagerSize(BManager.SIZE_NOLIMIT);
        return viewBean;
    }

}
