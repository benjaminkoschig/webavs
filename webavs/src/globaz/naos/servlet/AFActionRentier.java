package globaz.naos.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.secure.FWSecureConstants;
import globaz.jade.log.JadeLogger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (25.06.2003 15:56:54)
 * 
 * @author: ado
 */
public class AFActionRentier extends FWDefaultServletAction {

    /**
     * Constructeur d'AFActionRentier.
     * 
     * @param servlet
     */
    public AFActionRentier(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        // FWAction act = FWAction.newInstance(getAction().getElement(),
        // FWSecureConstants.UPDATE);
        getAction().setRight(FWSecureConstants.READ);

        try {
            /*
             * creation automatique du listviewBean
             */
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);

            if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        String _destination = "";
        try {
            if (getAction().getClassPart().equals("export")) { // c'est bien
                // l'export
                // Excel
                FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
                viewBean = mainDispatcher.dispatch(viewBean, getAction());
                // session.setAttribute("viewBean", viewBean);
                // _destination = getActionFullURL() + ".detail";
                if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                    _destination = ERROR_PAGE;
                } else {
                    _destination = _getDestExecuterSucces(session, request, response, viewBean);

                }
            } else { // rien d'autre à faire
                _destination = getActionFullURL() + ".detail";

            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = ERROR_PAGE;
        }
        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }
}
