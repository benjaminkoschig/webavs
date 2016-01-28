/*
 * Créé le 30 mars 2010
 */
package globaz.cygnus.servlet;

import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.RFNSSDTO;
import globaz.cygnus.vb.decisions.RFDecisionJointTiersListViewBean;
import globaz.cygnus.vb.decisions.RFDecisionJointTiersViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.prestation.ged.PRGedAffichageDossier;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author fha
 */
public class RFDecisionJointTiersAction extends RFDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public RFDecisionJointTiersAction(FWServlet servlet) {
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

        RFDecisionJointTiersViewBean viewBean = new RFDecisionJointTiersViewBean();

        if (!JadeStringUtil.isBlank(request.getParameter("likeNumeroAVS"))) {
            viewBean.setLikeNumeroAVS(request.getParameter("likeNumeroAVS"));
        }

        if (!JadeStringUtil.isBlank(request.getParameter("idDecision"))) {
            viewBean.setIdDecision(request.getParameter("idDecision"));
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
        RFDecisionJointTiersViewBean outputViewBean = (RFDecisionJointTiersViewBean) viewBean;
        // on set le viewBean avec les params de la session puis de la request?
        try {
            outputViewBean = (RFDecisionJointTiersViewBean) session.getAttribute("viewBean");
            if (outputViewBean.isAutreRetour()) {// vient de supprimer
                outputViewBean.setAutreRetour(false);
                outputViewBean.setDescFournisseur("");
                outputViewBean.setIdDestinataire("");
            }
            if (JadeStringUtil.isBlank(outputViewBean.getIdDestinataire())) {
                JSPUtils.setBeanProperties(request, outputViewBean);
            }

        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            RFUtils.setMsgErreurInattendueViewBean(outputViewBean, "beforeAfficher", "RFDecisionJointTiersAction");
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return outputViewBean;
    }

    // public void editerCopie(HttpSession session, HttpServletRequest request, HttpServletResponse response,
    // FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws javax.servlet.ServletException,
    // java.io.IOException {
    //
    // String _destination = "";
    //
    // String action = request.getParameter("userAction");
    // FWAction _action = FWAction.newInstance(action);
    //
    // /*
    // * recuperation du bean depuis la session
    // */
    // viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
    //
    // /*
    // * retrouver la ligne à éditer
    // */
    // ((RFDecisionJointTiersViewBean) viewBean).setIdTiers(request.getParameter("idDestinataire"));
    // /*
    // * appelle du dispatcher
    // */
    // viewBean = mainDispatcher.dispatch(viewBean, _action);
    //
    // /*
    // * choix de la destination
    // */
    // boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
    // if (goesToSuccessDest) {
    // _destination = this.getRelativeURL(request, session) + "_de.jsp";
    // } else {
    // _destination = this._getDestSupprimerEchec(session, request, response, viewBean);
    // }
    // /*
    // * redirection vers la destination
    // */
    // this.servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    // }

    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        RFNSSDTO dto = new RFNSSDTO();

        dto.setNSS(((RFDecisionJointTiersListViewBean) viewBean).getLikeNumeroAVS());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        return super.beforeLister(session, request, response, viewBean);
    }

    public void devalider(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws javax.servlet.ServletException,
            java.io.IOException {

        String _destination = "";

        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);

        try {

            JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, _action);
            session.removeAttribute("viewBean");
            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix destination
             */
            _destination = getRelativeURL(request, session) + "_de.jsp";

            /*
             * redirection vers la destination
             */
            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
        }

    }

    public void supprimerCopie(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws javax.servlet.ServletException,
            java.io.IOException {

        String _destination = "";

        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);

        /*
         * recuperation du bean depuis la session
         */
        viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

        /*
         * retrouver la ligne à supprimer
         */
        ((RFDecisionJointTiersViewBean) viewBean).setIdTiers(request.getParameter("idDestinataire"));

        /*
         * appelle du dispatcher
         */
        viewBean = mainDispatcher.dispatch(viewBean, _action);

        /*
         * choix de la destination
         */
        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
        if (goesToSuccessDest) {
            _destination = getRelativeURL(request, session) + "_de.jsp";
        } else {
            _destination = _getDestSupprimerEchec(session, request, response, viewBean);
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

}
