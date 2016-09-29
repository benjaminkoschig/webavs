package globaz.corvus.servlet;

import globaz.corvus.vb.process.REgenererRecapitulationRentesARC8DViewBean;
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

public class REGenererRecapitulationRentesARC8DAction extends REDefaultProcessAction {

    public REGenererRecapitulationRentesARC8DAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {

            FWViewBeanInterface viewBean = new REgenererRecapitulationRentesARC8DViewBean();

            // On sauvegarde le viewBean REDetailRecapMensuelleViewBean
            REDetailRecapMensuelleViewBean reDetRecMenViewBean = (REDetailRecapMensuelleViewBean) session
                    .getAttribute(FWServlet.VIEWBEAN);

            // On mémorise les champs contenant les totaux des prestations
            reDetRecMenViewBean.setTo2_510(request.getParameter("to2_510"));
            reDetRecMenViewBean.setTo2_511(request.getParameter("to2_511"));
            reDetRecMenViewBean.setTo2_513(request.getParameter("to2_513"));

            reDetRecMenViewBean.getElem510099().setMontant(request.getParameter("elem510099.montant"));
            reDetRecMenViewBean.getElem511099().setMontant(request.getParameter("elem511099.montant"));
            reDetRecMenViewBean.getElem513099().setMontant(request.getParameter("elem513099.montant"));

            ((REgenererRecapitulationRentesARC8DViewBean) viewBean).setReDetRecMenViewBean(reDetRecMenViewBean);

            // on lui donne les parametres en requete au cas ou.
            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setISession(mainDispatcher.getSession());
            String isChargementRecapStr = (String) session
                    .getAttribute(REChargerRecapMensuelleAction.ISCHARGEMENTRECAP);
            if ("True".equals(isChargementRecapStr)) {
                ((REgenererRecapitulationRentesARC8DViewBean) viewBean).setIsChargementRecap(true);
            }
            saveViewBean(viewBean, session);

        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

        String isChargementRecapStr = (String) session.getAttribute(REChargerRecapMensuelleAction.ISCHARGEMENTRECAP);
        if ("True".equals(isChargementRecapStr)) {
            ((REgenererRecapitulationRentesARC8DViewBean) viewBean).setIsChargementRecap(true);
        }

        saveViewBean(viewBean, session);
        super.actionReAfficher(session, request, response, mainDispatcher);
    }

}
