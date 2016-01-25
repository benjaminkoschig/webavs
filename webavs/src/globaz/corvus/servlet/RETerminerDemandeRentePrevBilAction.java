/*
 * Créé le 21 septembre 09
 */
package globaz.corvus.servlet;

import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.corvus.vb.process.RETerminerDemandeRentePrevBilViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author PCA
 * 
 */
public class RETerminerDemandeRentePrevBilAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Définit le NSS du DTO associé à une session
     * 
     * @param session
     *            La session considérée
     * @param nss
     *            La valeur du NSS à définir
     */
    private static void SetNssDto(HttpSession session, String nss) {
        RENSSDTO dtoNss = new RENSSDTO();

        dtoNss.setNSS(nss);
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dtoNss);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    /**
     * @param servlet
     */
    public RETerminerDemandeRentePrevBilAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestExecuterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        // Même destination qu'en cas de succès
        return _getDestExecuterSucces(session, request, response, viewBean);
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        // A la fin du processus synchrone, on retourne sur la page de recherche des demandes
        return this.getUserActionURL(request, "corvus.demandes.demandeRenteJointPrestationAccordee.chercher");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";

        String noDemandeRente = request.getParameter("noDemandeRente");
        String nssFromRequest = request.getParameter("numNSS");
        RETerminerDemandeRentePrevBilAction.SetNssDto(session, nssFromRequest);

        try {

            RETerminerDemandeRentePrevBilViewBean viewBean = new RETerminerDemandeRentePrevBilViewBean();
            viewBean.setISession(mainDispatcher.getSession());

            viewBean.setIdDemandeRente(noDemandeRente);

            this.saveViewBean(viewBean, session);

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
