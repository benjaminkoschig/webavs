package ch.globaz.amal.web.servlet;

import globaz.framework.servlets.FWServlet;

public class AMSubsideAnneeServletAction extends AMAbstractServletAction {

    /**
     * Constructeur
     * 
     * @param aServlet
     */
    public AMSubsideAnneeServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    // @Override
    // protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
    // HttpServletResponse response, FWViewBeanInterface viewBean) {
    // if (!(request.getParameter("s_anneeSubside") == null)
    // && !JadeStringUtil.isBlankOrZero(request.getParameter("s_anneeSubside"))) {
    // ((AMSubsideanneeViewBean) viewBean).setAnneeSubside("2011");
    // }
    //
    // return super.beforeAfficher(session, request, response, viewBean);
    // }

}
