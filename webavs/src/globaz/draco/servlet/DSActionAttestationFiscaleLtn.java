package globaz.draco.servlet;

import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;

/**
 * à la rue
 * 
 * Date de création : (18.11.2002 18:19:41)
 * 
 * @author: Administrator
 */
public class DSActionAttestationFiscaleLtn extends DSActionCustomFind {
    public DSActionAttestationFiscaleLtn(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {

        if ("customFind".equals(getAction().getActionPart())) {
            _actionCustomFind(session, request, response, mainDispatcher);
        }
    }
    /*
     * protected void actionAfficher(HttpSession session, HttpServletRequest request,HttpServletResponse response,
     * FWDispatcher mainDispatcher) throws ServletException, IOException { //FWAction _monAction =
     * FWAction.newInstance(request.getParameter("userAction")); FWAction _monAction = getAction(); String _destination
     * = ""; if("attestationFiscaleLtn".equals(_monAction.getClassPart())){ try{ DSAttestationFiscaleLtnViewBean
     * viewBean = new DSAttestationFiscaleLtnViewBean(); viewBean.setSession((BSession)mainDispatcher.getSession());
     * globaz.globall.http.JSPUtils.setBeanProperties(request,viewBean); session.setAttribute("viewBean", viewBean);
     * 
     * //passage dans le dispatcher viewBean = (DSAttestationFiscaleLtnViewBean) mainDispatcher.dispatch(viewBean,
     * _monAction);
     * 
     * _destination = getRelativeURL(request, session) + "_de.jsp"; servlet.getServletContext().getRequestDispatcher(
     * _destination).forward(request, response); }catch(Exception e){ e.printStackTrace(); } }else{
     * super.actionAfficher(session, request, response, mainDispatcher); } }
     */

}