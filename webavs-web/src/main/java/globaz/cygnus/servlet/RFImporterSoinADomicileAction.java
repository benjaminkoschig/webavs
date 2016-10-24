package globaz.cygnus.servlet;

import globaz.framework.servlets.FWServlet;

public class RFImporterSoinADomicileAction extends RFDefaultAction {

    public RFImporterSoinADomicileAction(FWServlet servlet) {
        super(servlet);
    }

    // @Override
    // protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
    // HttpServletResponse response, FWViewBeanInterface viewBean) {
    // return getActionFullURL() + ".reAfficher" + "&dateSurDocument="
    // + ((RFValiderDecisionsViewBean) viewBean).getDateSurDocument();
    // }
    //
    // @Override
    // protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
    // HttpServletResponse response, FWViewBeanInterface viewBean) {
    //
    // try {
    // globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
    // ((RFValiderDecisionsViewBean) viewBean).setIdDecision(request.getParameter("idDecision"));
    // return viewBean;
    //
    // } catch (Exception e) {
    // RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
    // return viewBean;
    // }
    // }
}
