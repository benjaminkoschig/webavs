package globaz.osiris.servlet.action.ordres;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrementViewBean;
import globaz.osiris.db.comptes.CAOperationOrdreVersementViewBean;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.servlet.action.CADefaultServletAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour l'affichage des ordres dans l'ordre groupé.
 * 
 * @author DDA
 */
public class CAApercuOrdreAction extends CADefaultServletAction {

    public CAApercuOrdreAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _myDestination = getRelativeURL(request, session) + "_rc.jsp";

        try {

            String typeOrdreGroupe = getTypeOrdreGroupe(session, request, response, mainDispatcher);

            session.setAttribute("typeOrdreGroupe", typeOrdreGroupe);

            CAOperation element = null;
            if (typeOrdreGroupe.equals(CAOrdreGroupe.RECOUVREMENT)) {
                element = getOrdreRecouvrement(session, request, response, mainDispatcher);
            } else {
                element = getOrdreVersement(session, request, response, mainDispatcher);
            }

            setSessionAttribute(session, VB_ELEMENT, element);

            _myDestination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            _myDestination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    public CAOperationOrdreRecouvrementViewBean getOrdreRecouvrement(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, Exception {
        FWViewBeanInterface element = super.getViewBean(request, "comptes", "OperationOrdreRecouvrement", false);
        ((CAOperationOrdreRecouvrementViewBean) element).setSession((BSession) mainDispatcher.getSession());

        element = retrieve(session, request, response, mainDispatcher, element);

        if (JadeStringUtil.isIntegerEmpty(((CAOperationOrdreRecouvrementViewBean) element).getIdOrdreGroupe())
                && !JadeStringUtil.isNull(super.getId(request, "forIdOrdreGroupe"))) {
            ((CAOperationOrdreRecouvrementViewBean) element).setIdOrdreGroupe(super.getId(request, "forIdOrdreGroupe"));
        }

        return ((CAOperationOrdreRecouvrementViewBean) element);
    }

    public CAOperationOrdreVersementViewBean getOrdreVersement(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, Exception {
        FWViewBeanInterface element = super.getViewBean(request, "comptes", "OperationOrdreVersement", false);

        element = retrieve(session, request, response, mainDispatcher, element);

        if (JadeStringUtil.isIntegerEmpty(((CAOperationOrdreVersementViewBean) element).getIdOrdreGroupe())
                && !JadeStringUtil.isNull(super.getId(request, "forIdOrdreGroupe"))) {
            ((CAOperationOrdreVersementViewBean) element).setIdOrdreGroupe(super.getId(request, "forIdOrdreGroupe"));
        }

        return ((CAOperationOrdreVersementViewBean) element);
    }

    private String getTypeOrdreGroupe(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, Exception {
        FWViewBeanInterface ordre = new CAOrdreGroupe();
        ((CAOrdreGroupe) ordre).setSession((BSession) mainDispatcher.getSession());
        ((CAOrdreGroupe) ordre).setIdOrdreGroupe(super.getId(request, "forIdOrdreGroupe"));

        ordre = retrieve(session, request, response, mainDispatcher, ordre);

        return ((CAOrdreGroupe) ordre).getTypeOrdreGroupe();
    }

    /**
     * Effectue un retrieve grâce au dispatcher.
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param element
     * @return
     * @throws Exception
     */
    private FWViewBeanInterface retrieve(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface element) throws Exception {
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        action.changeActionPart(FWAction.ACTION_AFFICHER);

        element = beforeAfficher(session, request, response, element);
        element = mainDispatcher.dispatch(element, action);
        return element;
    }

}