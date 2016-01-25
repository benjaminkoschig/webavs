package globaz.helios.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Action(s) pour la gestion de mapping.
 * 
 * @author dda
 * 
 */
public class CGMappingComptabiliserAction extends FWDefaultServletAction {

    private static final String VIEWBEAN = "viewBean";

    /**
     * @param servlet
     */
    public CGMappingComptabiliserAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * Redirige sur la page de détail. Permet de chargé l'idMandat source et destination du viewBean.
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(HttpSession,
     *      HttpServletRequestHttpServletResponse, globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String destination = getRelativeURL(request, session) + "_de.jsp";
        try {
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute(VIEWBEAN);
            JSPUtils.setBeanProperties(request, viewBean);

            setSessionAttribute(session, VIEWBEAN, viewBean);

            destination = getRelativeURL(request, session) + "_de.jsp";
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}
