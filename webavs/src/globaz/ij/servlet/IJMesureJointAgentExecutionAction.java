/*
 * Créé le 27 sept. 05
 */
package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.fweb.taglib.FWSelectorTag;
import globaz.globall.http.JSPUtils;
import globaz.ij.vb.prononces.IJMesureJointAgentExecutionViewBean;
import globaz.prestation.servlet.PRDefaultAction;
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
public class IJMesureJointAgentExecutionAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJMesureJointAgentExecutionAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJMesureJointAgentExecutionAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getUserActionURL(request, IIJActions.ACTION_MESURE_JOINT_AGENT_EXECUTION + "."
                + FWAction.ACTION_AFFICHER)
                + "&_method=add&_valid=new";
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
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        return getUserActionURL(request, IIJActions.ACTION_MESURE_JOINT_AGENT_EXECUTION + "."
                + FWAction.ACTION_AFFICHER)
                + "&_method=add&_valid=new";
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
        return getUserActionURL(request, IIJActions.ACTION_MESURE_JOINT_AGENT_EXECUTION + "."
                + FWAction.ACTION_AFFICHER)
                + "&_method=add&_valid=new";
    }

    /**
     * redefinition pour intercepter les retours depuis pyxis et faire en sorte que le cadre de detail s'affiche avec
     * les donnes de la situation professionnelle dont l'id de l'employeur vient d'etre modifie et pas une ecran vide.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = loadViewBean(session);

        if (isRetourDepuisPyxis(viewBean)) {
            // on revient depuis pyxis on se contente de forwarder car le bon
            // viewBean est deja en session
            ((IJMesureJointAgentExecutionViewBean) viewBean).setRetourDepuisPyxis(false);

            if (((IJMesureJointAgentExecutionViewBean) viewBean).isNew()) {
                forward(getRelativeURL(request, session) + "_de.jsp?_method=add", request, response);
            } else {
                forward(getRelativeURL(request, session) + "_de.jsp?_method=upd", request, response);
            }
        } else {
            // on ne revient pas depuis pyxis, comportement par defaut
            super.actionAfficher(session, request, response, mainDispatcher);
        }
    }

    /**
     * redefinition pour charger les informations qui doivent s'afficher dans l'ecran rc.
     * 
     * <p>
     * cette methode inspecte la session pour savoir si l'on revient depuis pyxis. Si c'est le cas, l'ancien viewBean
     * est conserve dans la session et une propriete du viewBean est renseignee avec une action qui permettra de
     * rafficher le bon ecran de detail pour la repartition de paiement.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = loadViewBean(session);
        IJMesureJointAgentExecutionViewBean spViewBean;

        if (isRetourDepuisPyxis(viewBean)) {
            /*
             * on revient depuis pyxis
             */
            spViewBean = (IJMesureJointAgentExecutionViewBean) viewBean;
        } else {
            /*
             * on affiche cette page par un chemin habituel, dans ce cas tout est normal, mise a part que l'on met un
             * viewBean dans la session qui contient les donnees qui doivent s'afficher dans le cadre rc de la ca page.
             */
            spViewBean = new IJMesureJointAgentExecutionViewBean();
            spViewBean.setIdPrononce(request.getParameter("forIdPrononce"));

            try {
                JSPUtils.setBeanProperties(request, spViewBean);
            } catch (Exception e) {
                spViewBean.setMessage(e.getMessage());
                spViewBean.setMsgType(FWViewBeanInterface.ERROR);
            }
        }

        saveViewBean(spViewBean, request);
        mainDispatcher.dispatch(spViewBean, getAction());
        forward(getRelativeURL(request, session) + "_rc.jsp", request, response);
    }

    /**
     * ecrase une des valeurs sauvee dans la session par FWSelectorTag de telle sorte que l'on sache exactement quelle
     * action sera executee lorsque l'on revient de pyxis et avec quels parametres.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     * 
     * @see FWSelectorTag
     */
    @Override
    protected void actionSelectionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        IJMesureJointAgentExecutionViewBean mesureJointAgentExecutionViewBean = (IJMesureJointAgentExecutionViewBean) session
                .getAttribute("viewBean");

        // HACK: on remplace une des valeurs sauvee en session par FWSelectorTag
        session.setAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL,
                "userAction=" + IIJActions.ACTION_MESURE_JOINT_AGENT_EXECUTION + "." + FWAction.ACTION_CHERCHER
                        + "&forIdPrononce=" + mesureJointAgentExecutionViewBean.getIdPrononce() + "&noAVS="
                        + mesureJointAgentExecutionViewBean.getNoAVS() + "&prenomNom="
                        + mesureJointAgentExecutionViewBean.getPrenomNom() + "&dateDebutPrononce="
                        + mesureJointAgentExecutionViewBean.getDateDebutPrononce() + "&csTypeIJ="
                        + mesureJointAgentExecutionViewBean.getCsTypeIJ());

        // comportement par defaut
        super.actionSelectionner(session, request, response, mainDispatcher);
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
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        IJMesureJointAgentExecutionViewBean mesureJointAgentExecutionViewBean = (IJMesureJointAgentExecutionViewBean) viewBean;

        mesureJointAgentExecutionViewBean.setIdPrononce(request.getParameter("idPrononce"));
        mesureJointAgentExecutionViewBean.setCsTypeIJ(request.getParameter("csTypeIJ"));
        mesureJointAgentExecutionViewBean.setDateDebutPrononce(request.getParameter("dateDebutPrononce"));
        mesureJointAgentExecutionViewBean.setPrenomNom(request.getParameter("prenomNom"));
        mesureJointAgentExecutionViewBean.setNoAVS(request.getParameter("noAVS"));

        return super.beforeAfficher(session, request, response, viewBean);
    }

    /**
     * inspecte le viewBean et retourne vrai si celui-ci indique que l'on revient de pyxis.
     * 
     * @param viewBean
     * 
     * @return
     */
    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof IJMesureJointAgentExecutionViewBean) && ((IJMesureJointAgentExecutionViewBean) viewBean)
                .isRetourDepuisPyxis());
    }
}
