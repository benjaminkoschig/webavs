package globaz.draco.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWListViewBeanActionFactory;
import globaz.framework.secure.FWSecureConstants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (27.11.2002 10:21:36)
 * 
 * @author: Administrator
 */
public class DSActionCustomFind extends globaz.framework.controller.FWDefaultServletAction {

    /**
     * Commentaire relatif au constructeur TIActionTiers.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public DSActionCustomFind(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    /**
     * action customFind
     * 
     * permet de faire sans passer par un BManager, pour faire les jointures entre les table db en fonction des criteres
     * de recherche que l'on désire.
     * 
     */
    protected void _actionCustomFind(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        String _destination = "";

        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action, FWSecureConstants.READ);
            FWViewBeanInterface viewBean = FWListViewBeanActionFactory.newInstance(_action, mainDispatcher.getPrefix());

            /*
             * transfere les criteres de recherche de la request vers le bean
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            /*
             * Appelle le helper pour traiter l'action customFind
             */
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            request.removeAttribute("viewBean");
            request.setAttribute("viewBean", viewBean);
            /*
             * choix de la destination
             */
            if ((viewBean == null) || (viewBean.getMsgType() == FWViewBeanInterface.ERROR)) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_rcListe.jsp";
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * affiche la prochaine page
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    @Override
    protected void actionCustom(javax.servlet.http.HttpSession session, javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, FWDispatcher mainDispatcher)
            throws javax.servlet.ServletException, java.io.IOException {

        if ("customFind".equals(getAction().getActionPart())) {
            _actionCustomFind(session, request, response, mainDispatcher);
        }
    }
}
