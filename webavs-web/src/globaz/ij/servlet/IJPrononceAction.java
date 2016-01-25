/*
 * Créé le 12 sept. 05
 */
package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.ij.vb.prononces.IJCorrigerDepuisPrononceViewBean;
import globaz.ij.vb.prononces.IJNSSDTO;
import globaz.ij.vb.prononces.IJSaisirEcheanceViewBean;
import globaz.ij.vb.prononces.IJSaisirNoDecisionViewBean;
import globaz.ij.vb.prononces.IJSaisirTauxISViewBean;
import globaz.ij.vb.prononces.IJTerminerPrononceViewBean;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.prestation.ged.PRGedAffichageDossier;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJPrononceAction extends PRDefaultAction {

    private final static String NO_AVS = "noAVS";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJPrononceAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJPrononceAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestSupprimerEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String action = IIJActions.ACTION_RECAPITULATIF_PRONONCE + "." + FWAction.ACTION_AFFICHER;

        return this.getUserActionURL(request, action);
    }

    /**
     * (non-Javadoc).
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String action = IIJActions.ACTION_PRONONCE_JOINT_DEMANDE + "." + FWAction.ACTION_CHERCHER;

        return this.getUserActionURL(request, action);
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws JadeClassCastException
     * @throws ClassCastException
     * @throws NullPointerException
     * @throws JadeServiceActivatorException
     * @throws JadeServiceLocatorException
     */
    public void actionAfficherDossierGed(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException,
            JadeServiceLocatorException, JadeServiceActivatorException, NullPointerException, ClassCastException,
            JadeClassCastException {

        PRGedAffichageDossier.actionAfficherDossierGed(session, request, response, mainDispatcher, viewBean);

    }

    public String afficherDateFin(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws Exception {

        IJTerminerPrononceViewBean viewBean = new IJTerminerPrononceViewBean();

        viewBean.setIdPrononce(request.getParameter("idPrononce"));

        FWAction action = getAction();
        mainDispatcher.dispatch(viewBean, action);

        try {
            JSPUtils.setBeanProperties(request, viewBean);

            IJNSSDTO dto = new IJNSSDTO();
            dto.setNSS(viewBean.getNoAVS());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        this.saveViewBean(viewBean, session);

        return getRelativeURL(request, session) + "_de.jsp";

    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof IJCorrigerDepuisPrononceViewBean) {
            // ((IJCorrigerDepuisPrononceViewBean) viewBean).setNoAVS(request.getParameter(IJPrononceAction.NO_AVS));
            // ((IJCorrigerDepuisPrononceViewBean) viewBean).setId(request.getParameter(""))
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    public String modiferEcheance(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws Exception {

        // Recherche du viewBean en base de donnée
        IJSaisirEcheanceViewBean viewBean = new IJSaisirEcheanceViewBean();

        viewBean.setSession((BSession) mainDispatcher.getSession());

        viewBean.setIdPrononce(request.getParameter("idPrononce"));

        viewBean.retrieve();

        // MàJ des champs du viewBean
        JSPUtils.setBeanProperties(request, viewBean);

        mainDispatcher.dispatch(viewBean, getAction());

        try {
            JSPUtils.setBeanProperties(request, viewBean);

            IJNSSDTO dto = new IJNSSDTO();
            dto.setNSS(viewBean.getNoAVS());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        String action = IIJActions.ACTION_PRONONCE_JOINT_DEMANDE + "." + FWAction.ACTION_CHERCHER;

        return this.getUserActionURL(request, action);
    }

    public String modiferNoDecision(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws Exception {

        IJSaisirNoDecisionViewBean viewBean = new IJSaisirNoDecisionViewBean();
        viewBean.setSession((BSession) mainDispatcher.getSession());
        viewBean.setIdPrononce(request.getParameter("idPrononce"));
        viewBean.setNewNoDecisionAI(request.getParameter("newNoDecisionAI"));
        viewBean.setNoAVS(request.getParameter("noAVS"));

        viewBean = (IJSaisirNoDecisionViewBean) mainDispatcher.dispatch(viewBean, getAction());

        String action = IIJActions.ACTION_PRONONCE_JOINT_DEMANDE + "." + FWAction.ACTION_CHERCHER;

        try {
            JSPUtils.setBeanProperties(request, viewBean);

            IJNSSDTO dto = new IJNSSDTO();
            dto.setNSS(viewBean.getNoAVS());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            return saisirNoDecision(session, request, response, mainDispatcher, viewBean);
        }

        return this.getUserActionURL(request, action);
    }

    public String modiferTauxIS(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws Exception {

        // Recherche du viewBean en base de donnée
        IJSaisirTauxISViewBean viewBean = new IJSaisirTauxISViewBean();

        viewBean.setSession((BSession) mainDispatcher.getSession());

        viewBean.setIdPrononce(request.getParameter("idPrononce"));

        viewBean.retrieve();

        // MàJ des champs du viewBean
        JSPUtils.setBeanProperties(request, viewBean);

        mainDispatcher.dispatch(viewBean, getAction());

        try {
            JSPUtils.setBeanProperties(request, viewBean);

            IJNSSDTO dto = new IJNSSDTO();
            dto.setNSS(viewBean.getNoAVS());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        String action = IIJActions.ACTION_PRONONCE_JOINT_DEMANDE + "." + FWAction.ACTION_CHERCHER;

        return this.getUserActionURL(request, action);
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param vb
     * @return
     * @throws Exception
     */
    public String saisirEcheance(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws Exception {

        IJSaisirEcheanceViewBean viewBean = new IJSaisirEcheanceViewBean();
        viewBean.setSession((BSession) mainDispatcher.getSession());

        JSPUtils.setBeanProperties(request, viewBean);
        viewBean.retrieve();

        mainDispatcher.dispatch(viewBean, getAction());

        try {
            JSPUtils.setBeanProperties(request, viewBean);

            IJNSSDTO dto = new IJNSSDTO();
            dto.setNSS(viewBean.getNoAVS());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        this.saveViewBean(viewBean, session);

        String destination = getRelativeURL(request, session) + "_de.jsp";

        return destination;
    }

    /**
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param vb
     * @return
     * @throws Exception
     */
    public String saisirNoDecision(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws Exception {

        IJSaisirNoDecisionViewBean viewBean = new IJSaisirNoDecisionViewBean();
        viewBean.setSession((BSession) mainDispatcher.getSession());
        JSPUtils.setBeanProperties(request, viewBean);
        viewBean.retrieve();

        if ((vb != null) && (vb instanceof IJSaisirNoDecisionViewBean)
                && FWViewBeanInterface.ERROR.equals(vb.getMsgType())) {

            viewBean.setMsgType(vb.getMsgType());
            viewBean.setMessage(vb.getMessage());
        } else {
            mainDispatcher.dispatch(viewBean, getAction());
        }

        try {
            JSPUtils.setBeanProperties(request, viewBean);

            IJNSSDTO dto = new IJNSSDTO();
            dto.setNSS(viewBean.getNoAVS());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        this.saveViewBean(viewBean, session);

        String destination = getRelativeURL(request, session) + "_de.jsp";

        return destination;
    }

    /**
     * 
     * Affiche la page permettant la modification du taux d'IS
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param vb
     * @return
     * @throws Exception
     */
    public String saisirTauxIS(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws Exception {

        IJSaisirTauxISViewBean viewBean = new IJSaisirTauxISViewBean();
        viewBean.setSession((BSession) mainDispatcher.getSession());

        JSPUtils.setBeanProperties(request, viewBean);
        viewBean.retrieve();

        mainDispatcher.dispatch(viewBean, getAction());

        try {
            JSPUtils.setBeanProperties(request, viewBean);

            IJNSSDTO dto = new IJNSSDTO();
            dto.setNSS(viewBean.getNoAVS());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        this.saveViewBean(viewBean, session);

        String destination = getRelativeURL(request, session) + "_de.jsp";

        return destination;
    }

    public String terminerPrononce(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws Exception {

        IJTerminerPrononceViewBean viewBean = (IJTerminerPrononceViewBean) vb;

        mainDispatcher.dispatch(viewBean, getAction());

        try {
            boolean tmp = viewBean.isBaseIndAfterEnd();
            JSPUtils.setBeanProperties(request, viewBean);
            viewBean.setBaseIndAfterEnd(tmp);
            IJNSSDTO dto = new IJNSSDTO();
            dto.setNSS(viewBean.getNoAVS());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        if (viewBean.isBaseIndAfterEnd()) {
            return getRelativeURL(request, session) + "_de.jsp";
        } else {
            String action = IIJActions.ACTION_PRONONCE_JOINT_DEMANDE + "." + FWAction.ACTION_CHERCHER;
            return this.getUserActionURL(request, action);
        }
    }

    public String terminerPrononceFinal(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws Exception {

        IJTerminerPrononceViewBean viewBean = (IJTerminerPrononceViewBean) vb;

        mainDispatcher.dispatch(viewBean, getAction());

        try {
            JSPUtils.setBeanProperties(request, viewBean);

            IJNSSDTO dto = new IJNSSDTO();
            dto.setNSS(viewBean.getNoAVS());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }

        String action = IIJActions.ACTION_PRONONCE_JOINT_DEMANDE + "." + FWAction.ACTION_CHERCHER;

        return this.getUserActionURL(request, action);

    }

}
