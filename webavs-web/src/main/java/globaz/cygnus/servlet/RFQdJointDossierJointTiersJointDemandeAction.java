/*
 * Créé le 30 novembre 2009
 */
package globaz.cygnus.servlet;

import globaz.cygnus.vb.RFNSSDTO;
import globaz.cygnus.vb.qds.RFQdJointDossierJointTiersJointDemandeListViewBean;
import globaz.cygnus.vb.qds.RFQdJointDossierJointTiersJointDemandeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
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
public class RFQdJointDossierJointTiersJointDemandeAction extends RFDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public RFQdJointDossierJointTiersJointDemandeAction(FWServlet servlet) {
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

        RFQdJointDossierJointTiersJointDemandeViewBean viewBean = new RFQdJointDossierJointTiersJointDemandeViewBean();
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

        // super.actionChercher(session, request, response, mainDispatcher);
    }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        RFNSSDTO dto = new RFNSSDTO();

        dto.setNSS(((RFQdJointDossierJointTiersJointDemandeListViewBean) viewBean).getLikeNumeroAVS());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        return super.beforeLister(session, request, response, viewBean);
    }

}
