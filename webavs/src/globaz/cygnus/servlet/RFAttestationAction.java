package globaz.cygnus.servlet;

import globaz.cygnus.application.RFApplication;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.attestations.RFAttestationJointDossierJointTiersViewBean;
import globaz.cygnus.vb.attestations.RFAttestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author fha
 */
public class RFAttestationAction extends RFDefaultAction {

    public RFAttestationAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String _destination = RFApplication.CYGNUS_RELATIVE_URL + IRFActions.ACTION_ATTESTATION_JOINT_TIERS
                + ".chercher";
        return _destination;
    }

    /*
     * peut-on éviter de tout redéfinir?
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFAttestationJointDossierJointTiersViewBean viewBean = new RFAttestationJointDossierJointTiersViewBean();

        if (!JadeStringUtil.isBlank(request.getParameter("likeNumeroAVS"))) {
            viewBean.setLikeNumeroAVS(request.getParameter("likeNumeroAVS"));
        }

        viewBean.setSession((BSession) mainDispatcher.getSession());
        mainDispatcher.dispatch(viewBean, getAction());
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", viewBean);
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);

        this.saveViewBean(viewBean, request);

        /*
         * choix destination
         */
        String _destination = "";

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        } else {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            ((RFAttestationViewBean) viewBean).setIdDossier(request.getParameter("idDossier"));
            ((RFAttestationViewBean) viewBean).setInfosDossier(request.getParameter("infosDossier"));

            return viewBean;

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }
    }

}
