/*
 * Créé le 6 juin 05
 */
package globaz.osiris.servlet.action.compte;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexeViewBean;
import globaz.osiris.db.comptes.CAEcritureManagerListViewBean;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour consulter l'historique du compte annexe.
 * 
 * @author DDA
 */
public class CAHistoriqueCompteAnnexeAction extends CADefaultServletAction {

    /**
     * @param servlet
     */
    public CAHistoriqueCompteAnnexeAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String myDestination = getRelativeURL(request, session) + "_rc.jsp";

        try {
            CACompteAnnexeViewBean element = getSection(request, mainDispatcher);

            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            action.changeActionPart(FWAction.ACTION_AFFICHER);

            element = (CACompteAnnexeViewBean) mainDispatcher.dispatch(element, action);

            setSessionAttribute(session, VB_ELEMENT, element);
        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String myDestination = getRelativeURL(request, session) + "_rcListe.jsp";

        try {
            FWViewBeanInterface manager = (CAEcritureManagerListViewBean) JSPUtils.useBean(request, "manager",
                    "globaz.osiris.db.comptes.CAEcritureManagerListViewBean", "session", true);

            ((CAEcritureManagerListViewBean) manager).setVueOperationCaCcSe(new Boolean(true));
            ((CAEcritureManagerListViewBean) manager).setForEtat("205002");

            if (!JadeStringUtil.isEmpty(request.getParameter("forIdCompteAnnexe"))) {
                ((CAEcritureManagerListViewBean) manager).setForIdCompteAnnexe(request
                        .getParameter("forIdCompteAnnexe"));
            }

            if (!JadeStringUtil.isEmpty(request.getParameter("forSelectionTri"))) {
                ((CAEcritureManagerListViewBean) manager).setForSelectionTri(request.getParameter("forSelectionTri"));
            }

            if (!JadeStringUtil.isEmpty(request.getParameter("fromDate"))) {
                ((CAEcritureManagerListViewBean) manager).setFromDate(request.getParameter("fromDate"));
            }

            if (!JadeStringUtil.isEmpty(request.getParameter("forAnneeCotisation"))) {
                ((CAEcritureManagerListViewBean) manager).setForAnneeCotisation(request
                        .getParameter("forAnneeCotisation"));
            }
            if (!JadeStringUtil.isEmpty(request.getParameter("likeIdExterneRubrique"))) {
                ((CAEcritureManagerListViewBean) manager).setLikeIdExterneRubrique(request
                        .getParameter("likeIdExterneRubrique"));
            }
            if (!JadeStringUtil.isEmpty(request.getParameter("likeIdExterneSection"))) {
                ((CAEcritureManagerListViewBean) manager).setLikeIdExterneSection(request
                        .getParameter("likeIdExterneSection"));
            }

            ((CAEcritureManagerListViewBean) manager).setSession((BSession) mainDispatcher.getSession());

            manager = beforeLister(session, request, response, manager);
            manager = mainDispatcher.dispatch(manager, FWAction.newInstance(request.getParameter("userAction")));

            JSPUtils.setBeanProperties(request, manager);
            setSessionAttribute(session, VBL_ELEMENT, manager);
        } catch (Exception e) {
            myDestination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(myDestination).forward(request, response);
    }

    protected CACompteAnnexeViewBean getSection(HttpServletRequest request, FWDispatcher mainDispatcher)
            throws ServletException, Exception {
        CACompteAnnexeViewBean element = (CACompteAnnexeViewBean) JSPUtils.useBean(request, "element",
                "globaz.osiris.db.comptes.CACompteAnnexeViewBean", "session", true);

        element.setSession((BSession) mainDispatcher.getSession());

        if ((!JadeStringUtil.isBlank(getId(request, "idCompteAnnexe")))
                && (!JadeStringUtil.isNull(super.getId(request, "idCompteAnnexe")))) {
            element.setIdCompteAnnexe(super.getId(request, "idCompteAnnexe"));
        }

        return element;
    }

}
