package globaz.phenix.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.db.divers.CPParametreCantonViewBean;

/**
 * Insérez la description du type ici. Date de création : (10.10.2002 16:08:43)
 * 
 * @author: Administrator
 */
public class CPActionParametreCanton extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur CGActionMandat.
     */
    public CPActionParametreCanton(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00) HACK : Surcharge cette méthode
     * puisque la redirection en cas d'erreur est différente et qu'un reafficher n'est pas envisageable et donc que le
     * forward est dans ce cas nécessaire.
     */
    @Override
    public void actionAjouter(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);
        /*
         * recuperation du bean depuis la session
         */
        CPParametreCantonViewBean viewBean = (CPParametreCantonViewBean) session.getAttribute("viewBean");
        String varS = request.getParameter("codeParametre");
        if (JadeStringUtil.isBlankOrZero(varS)) {
            if (JadeStringUtil.isBlankOrZero(request.getParameter("codeParametre1"))) {
                viewBean.setCodeParametre(request.getParameter("codeParametre2"));
            } else {
                viewBean.setCodeParametre(request.getParameter("codeParametre1"));
            }
        } else {
            viewBean.setCodeParametre(varS);
        }
        /*
         * set automatique des proprietes
         */
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * beforeAdd() call du dispatcher, puis mis en session
             */
            viewBean = (CPParametreCantonViewBean) beforeAjouter(session, request, response, viewBean);
            viewBean = (CPParametreCantonViewBean) mainController.dispatch(viewBean, _action);
        } catch (Exception e) {
            viewBean.setMessage(e.toString());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }
        session.setAttribute("viewBean", viewBean);
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);

        // --- Check view bean
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            servlet.getServletContext()
                    .getRequestDispatcher(
                            getRelativeURLwithoutClassPart(request, session)
                                    + "parametreCanton_de.jsp?_valid=fail&_back=sl").forward(request, response);
        } else {
            servlet.getServletContext().getRequestDispatcher(getActionFullURL() + ".chercher")
                    .forward(request, response);
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.05.2002 08:57:00)
     * 
     * HACK : Copie de la première partie de la méthode actionModifier de FWDefaultServletAction On garde la
     * redirection, car elle peut être différente selon la situation goSendRedirect modifier en forward sur la page
     * d'erreur
     */
    @Override
    public void actionModifier(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, globaz.framework.controller.FWDispatcher mainController)
            throws javax.servlet.ServletException, java.io.IOException {
        // --- Variables
        try {
            // --- Check view bean
            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);
            /*
             * recupération du bean depuis la sesison
             */
            CPParametreCantonViewBean viewBean = (CPParametreCantonViewBean) session.getAttribute("viewBean");
            String varS = request.getParameter("codeParametre");
            if (JadeStringUtil.isBlankOrZero(varS)) {
                if (JadeStringUtil.isBlankOrZero(request.getParameter("codeParametre1"))) {
                    viewBean.setCodeParametre(request.getParameter("codeParametre2"));
                } else {
                    viewBean.setCodeParametre(request.getParameter("codeParametre1"));
                }
            } else {
                viewBean.setCodeParametre(varS);
            }
            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */
            viewBean = (CPParametreCantonViewBean) beforeModifier(session, request, response, viewBean);
            viewBean = (CPParametreCantonViewBean) mainController.dispatch(viewBean, _action);
            session.setAttribute("viewBean", viewBean);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                servlet.getServletContext()
                        .getRequestDispatcher(
                                getRelativeURLwithoutClassPart(request, session)
                                        + "parametreCanton_de.jsp?_valid=fail&_back=sl").forward(request, response);
            } else {
                servlet.getServletContext().getRequestDispatcher(getActionFullURL() + ".chercher")
                        .forward(request, response);
            }
        } catch (Exception e) {
            // goSendRedirect(ERROR_PAGE, request, response);
            servlet.getServletContext().getRequestDispatcher("/errorPage.jsp").forward(request, response);
        }
    }
}
