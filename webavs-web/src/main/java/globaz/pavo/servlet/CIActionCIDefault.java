package globaz.pavo.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;

/**
 * Actions par default pour le splitting. Date de création : (22.10.2002 09:49:21)
 * 
 * @author: dgi
 */
public abstract class CIActionCIDefault extends FWDefaultServletAction {
    /**
     * Constructeur.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public CIActionCIDefault(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    /**
     * Exécute la fonction par défaut pour le splitting.
     * 
     */
    protected void actionDefault(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {
        String destination;
        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
        try {
            FWViewBeanInterface newBean = checkViewBean(viewBean);
            if (newBean != null) {
                viewBean = newBean;
                // le type du viewBean n'est pas valide, préparer le nouveau
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            }
            FWAction action = getAction();
            // action.setRight(FWSecureConstants.READ);
            viewBean = mainDispatcher.dispatch(viewBean, action);
            session.setAttribute("viewBean", viewBean);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                if ("pavo.splitting.dossierSplitting.executerSplitting".equals(request.getParameter("userAction"))) {
                    destination = getRelativeURL(request, session) + "_de.jsp";
                } else {
                    destination = getRelativeURL(request, session) + "_de.jsp?_valid=fail&_back=sl&_method=add";
                }
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Teste si le bean est du bon type. Date de création : (20.03.2003 08:18:17)
     * 
     * @return un nouveau bean si le type n'est pas le bon ou null si ok.
     * @param viewBean
     *            globaz.framework.bean.FWViewBeanInterface
     */
    public abstract FWViewBeanInterface checkViewBean(FWViewBeanInterface viewBean);
}
