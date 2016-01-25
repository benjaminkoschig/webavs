package ch.globaz.amal.web.servlet;

import globaz.amal.vb.copyparametre.AMCopyParametreViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AMCopyParametreServletAction extends AMAbstractServletAction {

    public AMCopyParametreServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    public String copyParams(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        String yearToCopy = request.getParameter("s_anneeSubsideToCopy");
        String newYear = request.getParameter("s_newAnnee");
        String paramsToCopy = request.getParameter("s_paramsToCopy");

        AMCopyParametreViewBean copyParametreViewBean = new AMCopyParametreViewBean();

        try {
            copyParametreViewBean.copyParams(paramsToCopy, yearToCopy, newYear);
        } catch (Exception e) {
            // copyParametreViewBean.setMessage(e.toString());
            // copyParametreViewBean.setMsgType(FWViewBeanInterface.ERROR);

            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            session.setAttribute("viewBean", viewBean);

            super.actionReAfficher(session, request, response, mainDispatcher);
            // return this._getDestEchec(session, request, response, copyParametreViewBean);
            // return FWDefaultServletAction.ERROR_PAGE;
        }

        return "/amal?userAction=amal.copyparametre.copyParametre.afficher";

    }

}
