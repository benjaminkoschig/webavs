package globaz.draco.servlet;

import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.declaration.DSLigneDeclarationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * à la rue
 * 
 * Date de création : (18.11.2002 18:19:41)
 * 
 * @author: Administrator
 */
public class DSActionLigneDeclaration extends FWDefaultServletAction {
    public DSActionLigneDeclaration(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        DSDeclarationViewBean vBean = new DSDeclarationViewBean();
        String selectedId = request.getParameter("selectedId");
        if (JadeStringUtil.isBlankOrZero(selectedId)) {
            selectedId = request.getParameter("idDeclaration");
        }
        vBean.setIdDeclaration(selectedId);
        String destination = "";
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));
        try {
            action.changeActionPart(FWAction.ACTION_AFFICHER);
            vBean = (DSDeclarationViewBean) mainDispatcher.dispatch(vBean, action);
            session.removeAttribute("viewBeanRC");
            session.setAttribute("viewBeanRC", vBean);
            destination = getRelativeURL(request, session) + "_rc.jsp";
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        viewBean = super.beforeAfficher(session, request, response, viewBean);

        DSLigneDeclarationViewBean vBean = (DSLigneDeclarationViewBean) viewBean;
        vBean.setIdDeclaration(request.getParameter("idDeclaration"));
        return vBean;
    }
}
