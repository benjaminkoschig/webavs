/*
 * Créé le 12 juil. 07
 */

package globaz.corvus.servlet;

import globaz.corvus.vb.ci.RESaisieManuelleInscriptionCIViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author BSC
 * 
 */

public class RESaisieManuelleInscriptionCIAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    protected static final String VERS_ECRAN_DE = "_de.jsp";
    private String _destination = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RESaisieManuelleInscriptionCIAction.
     * 
     * @param FWServlet
     *            servlet
     */
    public RESaisieManuelleInscriptionCIAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        try {

            // Ecran toujours utilisé pour l'ajout
            getAction().changeActionPart(FWAction.ACTION_NOUVEAU);

            // Reprise des éléments passés en requête
            String idRCI = request.getParameter("selectedId");
            String idTiers = request.getParameter("idTiers");
            String isSequenceAjouterCI = request.getParameter("isSequenceAjouterCI");

            // Création du viewBean
            RESaisieManuelleInscriptionCIViewBean viewBean = new RESaisieManuelleInscriptionCIViewBean();

            viewBean.setIdTiers(idTiers);
            viewBean.setIdRCI(idRCI);
            if (!JadeStringUtil.isNull(isSequenceAjouterCI)) {
                viewBean.setIsSequenceAjouterCI(isSequenceAjouterCI);
            }

            viewBean = (RESaisieManuelleInscriptionCIViewBean) mainDispatcher.dispatch(viewBean, getAction());

            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + VERS_ECRAN_DE + "?" + METHOD_ADD;
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAjouter(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     * 
     * --> Voir helper
     */

    public void ajouterInscriptionCI(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        try {

            // getAction().setRight(FWSecureConstants.ADD);
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            RESaisieManuelleInscriptionCIViewBean vb = (RESaisieManuelleInscriptionCIViewBean) viewBean;

            if (goesToSuccessDest) {
                _destination = "/corvus?userAction=corvus.ci.saisieManuelleInscriptionCI.afficher&idTiers="
                        + vb.getIdTiers() + "&idRCI=" + vb.getIdRCI();
            } else {
                _destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        goSendRedirect(_destination, request, response);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @throws ServletException
     * @throws IOException
     */
    public void arreter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        RESaisieManuelleInscriptionCIViewBean vb = (RESaisieManuelleInscriptionCIViewBean) viewBean;
        _destination = "/corvus?userAction=corvus.ci.inscriptionCI.chercher&idTiers=" + vb.getIdTiers()
                + "&selectedId=" + vb.getIdRCI() + "&idRCI=" + vb.getIdRCI();

        goSendRedirect(_destination, request, response);
    }

}
