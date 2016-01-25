package globaz.phenix.servlet;

import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */

public class CPActionTableFortune extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionTableFortune(FWServlet servlet) {
        super(servlet);
    }
    // protected void actionAjouter(
    // HttpSession session,
    // HttpServletRequest request,
    // HttpServletResponse response,
    // FWDispatcher mainDispatcher)
    // throws javax.servlet.ServletException, java.io.IOException {
    // String _destination = "";
    // try {
    // /*
    // * recuperation du bean depuis la session
    // */
    // FWViewBeanInterface viewBean =
    // (FWViewBeanInterface) session.getAttribute("viewBean");
    //
    // /*
    // * set automatique des proprietes
    // */
    // globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
    //
    // /*
    // * beforeAdd() call du dispatcher, puis mis en session
    // */
    // viewBean = beforeAjouter(session, request, response, viewBean);
    // viewBean = mainDispatcher.dispatch(viewBean, getAction());
    // session.setAttribute("viewBean", viewBean);
    //
    // /*
    // * chois de la destination
    // * _valid=fail : revient en mode edition
    // * _back=sl : sans effacer les champs deja rempli par l'utilisateur
    // *
    // */
    // if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
    // _destination =
    // getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl";
    // } else {
    // _destination = getActionFullURL() + ".afficher&_method=add&_valid=new";
    // }
    // } catch (Exception e) {
    // _destination = ERROR_PAGE;
    // }
    //
    // /*
    // * redirection vers la destination
    // */
    //
    // servlet.getServletContext().getRequestDispatcher(_destination).forward(
    // request,
    // response);
    // }
}
