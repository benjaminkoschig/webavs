/*
 * Créé le 30 novembre 2009
 */
package globaz.cygnus.servlet;

import globaz.cygnus.vb.RFNSSDTO;
import globaz.cygnus.vb.dossiers.RFDossierJointTiersListViewBean;
import globaz.cygnus.vb.dossiers.RFDossierJointTiersViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.prestation.ged.PRGedAffichageDossier;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author jje
 */
public class RFDossierJointTiersAction extends RFDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public RFDossierJointTiersAction(FWServlet servlet) {
        super(servlet);
    }

    public void actionAfficherDossierGed(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException,
            JadeServiceLocatorException, JadeServiceActivatorException, NullPointerException, ClassCastException,
            JadeClassCastException {
        PRGedAffichageDossier.actionAfficherDossierGed(session, request, response, mainDispatcher, viewBean);
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        RFDossierJointTiersViewBean viewBean = new RFDossierJointTiersViewBean();

        if (!JadeStringUtil.isBlank(request.getParameter("likeNumeroAVS"))) {
            viewBean.setLikeNumeroAVS(request.getParameter("likeNumeroAVS"));
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

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof RFDossierJointTiersViewBean) {
            ((RFDossierJointTiersViewBean) viewBean).setIsAfficherDetail(new Boolean(request
                    .getParameter("isAfficherDetail")));
            if (null != request.getParameter("nss")) {
                ((RFDossierJointTiersViewBean) viewBean).setNss(request.getParameter("nss"));
            }
        }

        RFNSSDTO dto = new RFNSSDTO();

        if (!JadeStringUtil.isBlankOrZero(((RFDossierJointTiersViewBean) viewBean).getNss())) {
            dto.setNSS(((RFDossierJointTiersViewBean) viewBean).getNss());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
        }

        return super.beforeAfficher(session, request, response, viewBean);

    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        RFNSSDTO dto = new RFNSSDTO();

        dto.setNSS(((RFDossierJointTiersListViewBean) viewBean).getLikeNumeroAVS());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        return super.beforeLister(session, request, response, viewBean);
    }

}
