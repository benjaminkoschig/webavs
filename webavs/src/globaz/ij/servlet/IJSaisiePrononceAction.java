/*
 * Créé le 23 sept. 05
 */
package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.vb.prononces.IJAbstractPrononceProxyViewBean;
import globaz.ij.vb.prononces.IJGrandeIJPViewBean;
import globaz.ij.vb.prononces.IJPetiteIJPViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJSaisiePrononceAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJSaisiePrononceAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJSaisiePrononceAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        IJAbstractPrononceProxyViewBean prononceProxyViewBean = (IJAbstractPrononceProxyViewBean) viewBean;

        try {
            return getActionFullURL() + ".reAfficher" + "&detailRequerant="
                    + prononceProxyViewBean.getDetailRequerantDetail();
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        IJAbstractPrononceProxyViewBean prononceViewBean = (IJAbstractPrononceProxyViewBean) viewBean;

        try {
            return this.getUserActionURL(
                    request,
                    IIJActions.ACTION_MESURE_JOINT_AGENT_EXECUTION,
                    FWAction.ACTION_CHERCHER + "&forIdPrononce=" + prononceViewBean.getIdPrononce() + "&noAVS="
                            + prononceViewBean.getNoAVS() + "&prenomNom=" + prononceViewBean.getPrenomNom()
                            + "&dateDebutPrononce=" + prononceViewBean.getDateDebutPrononce() + "&csTypeIJ="
                            + prononceViewBean.getCsTypeIJ() + "&detailRequerant="
                            + prononceViewBean.getDetailRequerantDetail());
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }
    }

    public void actionEcranSuivant(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws ServletException, IOException {
        FWViewBeanInterface viewBean = this.loadViewBean(session);
        String destination = _getDestModifierSucces(session, request, response, viewBean);
        goSendRedirect(destination, request, response);
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWAction _action = getAction();
        String _destination = null;

        try {
            // copier coller en grande partie de l'action afficher du
            // fwdefaultServletAction.

            String selectedId = request.getParameter("selectedId");

            if (JadeStringUtil.isIntegerEmpty(selectedId)) {
                selectedId = request.getParameter("idPrononce");
            }

            /*
             * Creation de notre viewBean
             */
            FWViewBeanInterface viewBean = null;
            String csTypeIJ = vaChercherCsTypeIJ(request, mainDispatcher);

            if (JadeStringUtil.isEmpty(csTypeIJ)) {
                throw new Exception("Impossible de connaître le type d'IJ");
            }

            if (csTypeIJ.equals(IIJPrononce.CS_PETITE_IJ)) {
                viewBean = new IJPetiteIJPViewBean();
            } else {
                viewBean = new IJGrandeIJPViewBean();
            }

            ((IJAbstractPrononceProxyViewBean) viewBean).setIdPrononce(selectedId);

            viewBean = beforeAfficher(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, _action);
            ((IJAbstractPrononceProxyViewBean) viewBean).setCsTypeIJ(csTypeIJ);
            session.removeAttribute(FWServlet.VIEWBEAN);
            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            /*
             * choix destination
             */
            if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            } else {
                _destination = getRelativeURL(request, session) + "_de.jsp";
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = null;
        try {

            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);

            /*
             * recupération du bean depuis la sesison
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            IJAbstractPrononceProxyViewBean vb = (IJAbstractPrononceProxyViewBean) viewBean;

            boolean isErrorNotAffilie = false;
            // Si sit prof est indépendant, l'assuré doit avoir une affiliation
            if (IIJPrononce.CS_INDEPENDANT.equals(vb.getCsStatutProfessionnel())) {
                try {
                    Vector aff = PRAffiliationHelper.getAffiliationsTiers(mainDispatcher.getSession(), vb.loadDemande()
                            .getIdTiers());

                    if ((aff == null) || (aff.size() == 0)) {
                        isErrorNotAffilie = true;
                    }
                } catch (Exception e) {
                    isErrorNotAffilie = true;
                }

            }
            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */
            viewBean = beforeModifier(session, request, response, viewBean);
            if (mainDispatcher.getSession().hasRight(IIJActions.ACTION_SAISIE_PRONONCE, FWSecureConstants.UPDATE)) {
                viewBean = mainDispatcher.dispatch(viewBean, _action);
            }
            session.setAttribute("viewBean", viewBean);

            /*
             * choix de la destination _valid=fail : revient en mode edition
             */

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = _getDestModifierSucces(session, request, response, viewBean);
            } else {
                destination = _getDestModifierEchec(session, request, response, viewBean);
            }

            if (isErrorNotAffilie) {
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
                viewBean.setMessage(((BSession) mainDispatcher.getSession()).getLabel("INDEPENDANT_SANS_AFFIL_ERR"));
                session.setAttribute("viewBean", viewBean);
                destination = FWDefaultServletAction.ERROR_PAGE;
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(destination, request, response);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String arreterEtape3(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        ((IJAbstractPrononceProxyViewBean) viewBean).wantCallValidate(false);

        try {
            getAction().changeActionPart(FWAction.ACTION_MODIFIER);
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }

        mainDispatcher.dispatch(viewBean, getAction());

        return this.getUserActionURL(request, IIJActions.ACTION_PRONONCE_JOINT_DEMANDE, FWAction.ACTION_CHERCHER);
    }

    private String vaChercherCsTypeIJ(HttpServletRequest request, FWDispatcher mainDispatcher) {
        String csTypeIJ = request.getParameter("csTypeIJ");

        if (JadeStringUtil.isIntegerEmpty(csTypeIJ)) {
            // on essaye de le récupérer avec l'ID
            String selectedId = request.getParameter("selectedId");

            if (!JadeStringUtil.isIntegerEmpty(selectedId)) {
                IJPrononce prononce = new IJPrononce();
                prononce.setSession((BSession) (mainDispatcher.getSession()));
                prononce.setIdPrononce(selectedId);

                try {
                    prononce.retrieve();
                    csTypeIJ = prononce.getCsTypeIJ();
                } catch (Exception e) {
                    csTypeIJ = "";
                    ((BSession) mainDispatcher.getSession()).addError(e.getMessage());
                }
            }
        }

        return csTypeIJ;
    }

}
