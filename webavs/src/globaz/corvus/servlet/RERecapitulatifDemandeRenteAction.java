package globaz.corvus.servlet;

import globaz.corvus.vb.demandes.RERecapitulatifDemandeRenteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author hpe
 * 
 */

public class RERecapitulatifDemandeRenteAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJPrononceJointDemandeAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public RERecapitulatifDemandeRenteAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionSupprimer(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionSupprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);
        String _destination = "";

        FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

        viewBean = beforeSupprimer(session, request, response, viewBean);
        viewBean = mainDispatcher.dispatch(viewBean, _action);

        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

        if (goesToSuccessDest) {
            _destination = "/corvus?userAction=" + IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE
                    + ".chercher";
        } else {
            _destination = _getDestSupprimerEchec(session, request, response, viewBean);
        }

        goSendRedirect(_destination, request, response);
    }

    /**
     * Méthode permettant de reprendre le selectedId et de le setter dans le viewBean
     * 
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWViewBeanInterface
     *            viewBean
     * 
     * @return le viewBean
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        RERecapitulatifDemandeRenteViewBean recapitulatifViewBean = (RERecapitulatifDemandeRenteViewBean) viewBean;

        if (JadeStringUtil.isIntegerEmpty(recapitulatifViewBean.getIdDemandeRente())) {
            recapitulatifViewBean.setIdDemandeRente(request.getParameter("selectedId"));
        }

        return recapitulatifViewBean;
    }

    public void validerHistorique(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);
        _action.setRight(FWSecureConstants.ADD);
        String _destination = "";

        viewBean = mainDispatcher.dispatch(viewBean, _action);

        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

        if (goesToSuccessDest) {
            _destination = "/corvus?userAction=corvus.demandes.recapitulatifDemandeRente.afficher";
        } else {
            _destination = _getDestSupprimerEchec(session, request, response, viewBean);
        }

        goSendRedirect(_destination, request, response);
    }

}
