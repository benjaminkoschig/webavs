package globaz.helios.servlet;

import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableViewBean;
import globaz.helios.db.comptes.CGExtendedMvtCompteListViewBean;
import globaz.helios.db.comptes.CGPeriodeComptable;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.helios.tools.CGSessionDataContainerHelper;
import globaz.helios.translation.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion des mouvements d'un compte.
 * 
 * @author DDA
 */
public class CGActionMouvementCompte extends CGDefaultServletAction {
    private static final int SIZE_MANAGE_MOUVEMENT_TOUT_EXERCICE = 1000;

    public CGActionMouvementCompte(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String destination = "";

        try {
            CGPlanComptableViewBean vBean = new CGPlanComptableViewBean();
            String selectedId = request.getParameter("selectedId");
            vBean.setIdCompte(selectedId);
            vBean.setIdExerciceComptable(((CGExerciceComptableViewBean) session
                    .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE)).getIdExerciceComptable());

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            action.changeActionPart(FWAction.ACTION_AFFICHER);

            vBean = (CGPlanComptableViewBean) mainDispatcher.dispatch(vBean, action);

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
        if ("listerMouvements".equals(getAction().getActionPart())) {
            actionListerMouvements(session, request, response, mainDispatcher);
        }
    }

    protected void actionListerMouvements(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws javax.servlet.ServletException,
            java.io.IOException {
        String destination = null;
        try {
            CGExtendedMvtCompteListViewBean listViewBean = new CGExtendedMvtCompteListViewBean();
            listViewBean.setSession((BSession) CodeSystem.getSession(session));

            String idPeriodeComptable = request.getParameter("reqPeriodeComptable");
            if (idPeriodeComptable == null) {
                idPeriodeComptable = CGPeriodeComptable.ID_PERIODE_TOUT_EXERCICE;
            }

            CGSessionDataContainerHelper sessionDataContainer = new CGSessionDataContainerHelper();
            sessionDataContainer.setData(session, CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_PERIODE,
                    idPeriodeComptable.trim());

            listViewBean.setForIdCompte(request.getParameter("selectedId"));
            JSPUtils.setBeanProperties(request, listViewBean);

            listViewBean.wantForEstActive(true);

            String idTypeCompta = listViewBean.getReqComptabilite();
            sessionDataContainer.setData(session, CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_TYPE_COMPTA,
                    idTypeCompta.trim());

            CGExerciceComptable exerciceComptable = (CGExerciceComptable) session
                    .getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
            listViewBean.setForIdMandat(exerciceComptable.getIdMandat());

            if (JadeStringUtil.isIntegerEmpty(idPeriodeComptable)) {
                listViewBean.changeManagerSize(SIZE_MANAGE_MOUVEMENT_TOUT_EXERCICE);
            } else {
                listViewBean.changeManagerSize(BManager.SIZE_NOLIMIT);
            }

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            action.changeActionPart(FWAction.ACTION_LISTER);

            listViewBean = (CGExtendedMvtCompteListViewBean) beforeLister(session, request, response, listViewBean);
            listViewBean = (CGExtendedMvtCompteListViewBean) mainDispatcher.dispatch(listViewBean, action);

            request.setAttribute(VIEWBEAN, listViewBean);

            setSessionAttribute(session, "listViewBean", listViewBean);

            destination = getRelativeURL(request, session) + "_rcListe.jsp";
        } catch (Exception ex) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}
