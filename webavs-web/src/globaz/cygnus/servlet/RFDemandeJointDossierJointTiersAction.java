/*
 * Créé le 30 novembre 2009
 */

package globaz.cygnus.servlet;

import globaz.cygnus.vb.RFNSSDTO;
import globaz.cygnus.vb.demandes.RFDemandeJointDossierJointTiersListViewBean;
import globaz.cygnus.vb.demandes.RFDemandeJointDossierJointTiersViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.ij.vb.prononces.IJPrononceViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author jje
 */
public class RFDemandeJointDossierJointTiersAction extends RFDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public RFDemandeJointDossierJointTiersAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFDemandeJointDossierJointTiersViewBean viewBean = new RFDemandeJointDossierJointTiersViewBean();

        if (!JadeStringUtil.isBlank(request.getParameter("likeNumeroAVS"))) {
            viewBean.setLikeNumeroAVS(request.getParameter("likeNumeroAVS"));
        }

        if (!JadeStringUtil.isBlank(request.getParameter("numeroDecision"))) {
            viewBean.setNumeroDecision(request.getParameter("numeroDecision"));
        }

        if (!JadeStringUtil.isBlank(request.getParameter("idDecision"))) {
            viewBean.setIdDecision(request.getParameter("idDecision"));
        }

        viewBean.setSession((BSession) mainDispatcher.getSession());
        mainDispatcher.dispatch(viewBean, getAction());
        this.saveViewBean(viewBean, request);

        if ((viewBean.getSession() == null) || viewBean.hasErrors()) {
            // si on rentre dans cygnus et qu'on a pas les droits, la session
            // vaut null
            forward(FWDefaultServletAction.ERROR_PAGE, request, response);
        } else {
            forward(getRelativeURL(request, session) + "_rc.jsp", request, response);
        }

    }

    public String actionCreerCorrection(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
        RFDemandeJointDossierJointTiersViewBean demande = new RFDemandeJointDossierJointTiersViewBean();

        try {
            JSPUtils.setBeanProperties(request, demande);

        } catch (Exception e) {
            demande.setMsgType(FWViewBeanInterface.ERROR);
            demande.setMessage(e.getMessage());
        }

        mainDispatcher.dispatch(demande, getAction());

        boolean goesToSuccessDest = !demande.getMsgType().equals(FWViewBeanInterface.ERROR);

        if (!goesToSuccessDest) {
            if (null == viewBean) {
                viewBean = new IJPrononceViewBean();
            }
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(demande.getMessage());
        }

        this.saveViewBean(demande, request);

        if (goesToSuccessDest) {
            return getRelativeURL(request, session) + "_rc.jsp";
        } else {
            return FWDefaultServletAction.ERROR_PAGE;
        }
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        RFNSSDTO dto = new RFNSSDTO();

        dto.setNSS(((RFDemandeJointDossierJointTiersListViewBean) viewBean).getLikeNumeroAVS());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        return super.beforeLister(session, request, response, viewBean);
    }

}
