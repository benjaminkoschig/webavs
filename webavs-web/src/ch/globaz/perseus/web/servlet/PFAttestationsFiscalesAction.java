package ch.globaz.perseus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.perseus.vb.attestationsfiscales.PFAttestationsFiscalesViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author MBO
 * 
 */

public class PFAttestationsFiscalesAction extends PFAbstractDefaultServletAction {

    /**
     * 
     * @param aServlet
     */
    public PFAttestationsFiscalesAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String destination = "";
        try {
            PFAttestationsFiscalesViewBean viewBean = new PFAttestationsFiscalesViewBean();

            String email = request.getParameter("email");
            String dateDocument = request.getParameter("dateDocument");
            String anneeAttestation = request.getParameter("anneeAttestation");
            String csCaisse = request.getParameter("caisse");
            String idDossier = request.getParameter("idDossier");

            viewBean.seteMailAdresse(email);
            viewBean.setDateDocument(dateDocument);
            viewBean.setAnneeAttestation(anneeAttestation);
            viewBean.setCaisse(csCaisse);
            if (!JadeStringUtil.isEmpty(csCaisse)) {
                viewBean.setDonneeCaisse(csCaisse);
            }

            if (!JadeStringUtil.isNull(idDossier)) {
                viewBean.setIdDossier(request.getParameter("idDossier"));
                viewBean.init();
            }

            viewBean = (PFAttestationsFiscalesViewBean) mainDispatcher.dispatch(viewBean, getAction());
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
                destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                destination = getRelativeURL(request, session) + "_de.jsp";
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
