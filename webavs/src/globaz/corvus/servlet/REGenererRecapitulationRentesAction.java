package globaz.corvus.servlet;

import globaz.corvus.vb.process.REgenererRecapitulationRentesViewBean;
import globaz.corvus.vb.recap.REDetailRecapMensuelleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class REGenererRecapitulationRentesAction extends REDefaultProcessAction {

    public REGenererRecapitulationRentesAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWViewBeanInterface viewBean = new REgenererRecapitulationRentesViewBean();

            // On sauvegarde le viewBean REDetailRecapMensuelleViewBean
            REDetailRecapMensuelleViewBean reDetRecMenViewBean = (REDetailRecapMensuelleViewBean) session
                    .getAttribute(FWServlet.VIEWBEAN);

            ((REgenererRecapitulationRentesViewBean) viewBean).setReDetRecMenViewBean(reDetRecMenViewBean);

            // on lui donne les parametres en requete au cas ou.
            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setISession(mainDispatcher.getSession());
            saveViewBean(viewBean, session);
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

}
