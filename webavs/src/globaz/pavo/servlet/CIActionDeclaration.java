package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author OCA
 * 
 *         Redefinition de la gestion des actions de base pour le processus des declarations Ceci permet entre autre de
 *         pouvoir choisir ou se rendre apres le lancement du processus (executer)
 * 
 */
public class CIActionDeclaration extends FWDefaultServletAction {
    public CIActionDeclaration(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Auto-generated method stub
        return getRelativeURL(request, session) + "_de.jsp?_valid=fail";
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Auto-generated method stub
        return "/" + getAction().getApplicationPart() + "?userAction=pavo.compte.compteIndividuel.chercher";
    }

    /*
     * protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
     * FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException { String _destination =
     * ""; FWAction _action = FWAction.newInstance(request.getParameter("userAction"), FWSecureConstants.READ); try {
     * System.out.println("test1"); //UpLoad file
     * 
     * CIDeclarationViewBean viewBean = (CIDeclarationViewBean) session.getAttribute("viewBean");
     * globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean); viewBean =
     * (CIDeclarationViewBean)mainDispatcher.dispatch(viewBean, _action); session.setAttribute("viewBean", viewBean); if
     * (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) { _destination = getRelativeURL(request,
     * session) + "_de.jsp?_valid=fail"; } else { _destination = "/" + _action.getApplicationPart() +
     * "?userAction=pavo.compte.compteIndividuel.chercher"; } } catch (Exception e) { e.printStackTrace(); _destination
     * = ERROR_PAGE; } servlet.getServletContext ().getRequestDispatcher(_destination).forward(request, response); }
     */

}
