/*
 * Créé le 5 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.servlet;

import globaz.apg.db.droits.APDroitPaternite;
import globaz.apg.exceptions.APWrongViewBeanTypeException;
import globaz.apg.util.APGSeodorServiceCallUtil;
import globaz.apg.vb.droits.*;
import globaz.apg.vb.prestation.APValidationPrestationViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APDroitPatPAction extends APAbstractDroitPAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APDroitPatPAction.
     *
     * @param servlet
     */
    public APDroitPatPAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(HttpSession,
     *      HttpServletRequest, HttpServletResponse,
     *      FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        APDroitDTO dto = new APDroitDTO(((APDroitPatPViewBean) viewBean).getDroit());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);

        return this.getUserActionURL(request, IAPActions.ACTION_ENFANT_PAT, FWAction.ACTION_CHERCHER + "&"
                + APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + dto.getIdDroit());
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(HttpSession,
     *      HttpServletRequest, HttpServletResponse,
     *      FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        APDroitDTO dto = new APDroitDTO(((APDroitPatPViewBean) viewBean).getDroit());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);

        return this.getUserActionURL(request, IAPActions.ACTION_ENFANT_PAT, FWAction.ACTION_CHERCHER + "&"
                + APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + dto.getIdDroit());
    }

    /**
     * On arrive sur cette action depuis l'écran de contrôle des prestation suite à la création d'un droit.
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param vb
     * @return
     * @throws Exception
     */
    public String finaliserCreationDroit(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws Exception {

        if (!(vb instanceof APValidationPrestationViewBean)) {
            throw new APWrongViewBeanTypeException(
                    "finaliserCreationDroit wrong viewBean received. It must be an instance of class APValidationPrestationViewBean");
        }
        APValidationPrestationViewBean viewBean = (APValidationPrestationViewBean) vb;
        try {
            APDroitAPGPViewBean actionAfficherViewBean = (APDroitAPGPViewBean) mainDispatcher.dispatch(viewBean,
                    getAction());
            return this.getUserActionURL(request, IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT,
                    "chercher&forIdDroit=" + viewBean.getIdDroit());
        } catch (Exception e) {
            this.saveViewBean(viewBean, session);
            throw e;
        }
    }

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

        String[] userAction = request.getParameterValues("userAction");
        String destination;

        FWViewBeanInterface viewBeanAP = (FWViewBeanInterface) session.getAttribute("viewBean");

        if (Objects.nonNull(viewBeanAP) && viewBeanAP instanceof APDroitPatPViewBean && ((APDroitPatPViewBean)viewBeanAP).getAControler()) {
            try {
                String method = request.getParameter("_method");
                FWAction privateAction = FWAction.newInstance(userAction[0]);
                FWViewBeanInterface viewBean = new APDroitPatPViewBean();

                if (method != null && method.equalsIgnoreCase("ADD")) {
                    privateAction.changeActionPart(FWAction.ACTION_NOUVEAU);
                    viewBean = this.beforeNouveau(session, request, response, viewBeanAP);
                }

                if (method != null && (method.equalsIgnoreCase("UPD") || method.equalsIgnoreCase("READ"))) {
                    privateAction.changeActionPart(FWAction.ACTION_MODIFIER);
                    viewBean = this.beforeModifier(session, request, response, viewBeanAP);
                }

                viewBean = this.beforeAfficher(session, request, response, viewBean);
                viewBean = mainDispatcher.dispatch(viewBean, privateAction);
                viewBean.setMessage("");
                viewBean.setMsgType("");
                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", viewBean);
                request.setAttribute("viewBean", viewBean);
                if (false && viewBean.getMsgType().equals("ERROR")) {
                    destination = "/errorPage.jsp";
                } else {
                    destination = this.getRelativeURL(request, session) + "_de.jsp";
                }
            } catch (Exception var10) {
                destination = "/errorPage.jsp";
            }
            this.servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);


        }else{
            if ((userAction != null) && (userAction.length > 1) && userAction[1].toLowerCase().equals("back")) {
                super.actionAfficherApresBack(session, request, response, mainDispatcher);
            } else {
                super.actionAfficher(session, request, response, mainDispatcher);
            }
        }


        FWViewBeanInterface viewBean = this.loadViewBean(session);

//        APDroitDTO dto = new APDroitDTO();
//        dto.setDateDebutDroit(((APDroitPatPViewBean) viewBean).getDateDebutDroit());
//        dto.setGenreService(((APDroitPatPViewBean) viewBean).getGenreService());
//        dto.setIdDroit(((APDroitPatPViewBean) viewBean).getIdDroit());
//        dto.setNoAVS(((APDroitPatPViewBean) viewBean).getNss());
//        dto.setNomPrenom(((APDroitPatPViewBean) viewBean).getNomPrenom());

        APDroitAPGDTO dto = new APDroitAPGDTO(((APDroitPatPViewBean) viewBean).getDroit());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);
    }

    /**
     * Ajout d'un nouveau droit. Step 1/3 de la saisie
     */
    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                 FWDispatcher mainDispatcher) throws ServletException, IOException {

        APDroitPatPViewBean viewBean = new APDroitPatPViewBean();
        String action = request.getParameter("userAction");
        FWAction newAction = FWAction.newInstance(action);

        String destination = null;

        try {

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean = (APDroitPatPViewBean) beforeAjouter(session, request, response, viewBean);
            viewBean = (APDroitPatPViewBean) mainDispatcher.dispatch(viewBean, newAction);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                request.setAttribute(FWServlet.VIEWBEAN, viewBean);
                destination = _getDestAjouterSucces(session, request, response, viewBean);
            } else {
                session.setAttribute("viewBean", viewBean);
                request.setAttribute(FWServlet.VIEWBEAN, viewBean);
                destination = _getDestAjouterEchec(session, request, response, viewBean);
            }

        } catch (Exception e) {
            this.saveViewBean(viewBean, session);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        goSendRedirect(destination, request, response);

    }
    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                    FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionReAfficher(session, request, response, mainDispatcher);
    }


    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                  FWDispatcher mainDispatcher) throws ServletException, IOException {
        APDroitPatPViewBean viewBean = new APDroitPatPViewBean();
        String action = request.getParameter("userAction");
        FWAction newAction = FWAction.newInstance(action);

        String destination = null;
        try {

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean = (APDroitPatPViewBean) beforeAjouter(session, request, response, viewBean);

            if (viewBean.getAControler()) {
                APGSeodorServiceCallUtil.callWSSeodor(viewBean, mainDispatcher);
            }
            if (viewBean.hasMessagePropError()) {
                newAction = FWAction.newInstance(IAPActions.ACTION_SAISIE_CARTE_APAT + ".afficher");
                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", viewBean);
                request.setAttribute("viewBean", viewBean);
                destination =request.getServletPath() + "?" + PRDefaultAction.USER_ACTION + "=" + newAction;
            }else{
                viewBean = (APDroitPatPViewBean) mainDispatcher.dispatch(viewBean, newAction);

                boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

                if (goesToSuccessDest) {
                    request.setAttribute(FWServlet.VIEWBEAN, viewBean);
                    destination = _getDestAjouterSucces(session, request, response, viewBean);
                } else {
                    session.setAttribute("viewBean", viewBean);
                    request.setAttribute(FWServlet.VIEWBEAN, viewBean);
                    destination = _getDestAjouterEchec(session, request, response, viewBean);
                }

            }


        } catch (Exception e) {
            this.saveViewBean(viewBean, session);
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

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
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            ((APDroitPaternite) ((APDroitPatPViewBean) viewBean).getDroit()).calculerDateFinDroit((BSession)session.getAttribute("objSession"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewBean;
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
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            ((APDroitPaternite) ((APDroitPatPViewBean) viewBean).getDroit()).calculerDateFinDroit((BSession)session.getAttribute("objSession"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewBean;
    }

}
