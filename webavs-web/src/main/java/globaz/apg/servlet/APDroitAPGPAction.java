package globaz.apg.servlet;

import ch.globaz.common.document.reference.AbstractReference;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.PropertiesException;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APModeEditionDroit;
import globaz.apg.exceptions.APWrongViewBeanTypeException;
import globaz.apg.properties.APProperties;
import globaz.apg.util.APGSeodorDataBean;
import globaz.apg.util.APGSeodorErreurEntity;
import globaz.apg.util.APGSeodorServiceCallUtil;
import globaz.apg.util.APGSeodorServiceMappingUtil;
import globaz.apg.vb.droits.APDroitAPGDTO;
import globaz.apg.vb.droits.APDroitAPGPViewBean;
import globaz.apg.vb.prestation.APValidationPrestationViewBean;
import globaz.apg.ws.APRapgConsultationUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.prestation.beans.PRPeriode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.DatatypeConfigurationException;

/**
 * <H1>Description</H1> Créé le 7 juil. 05
 * 
 * @author vre
 */
public class APDroitAPGPAction extends APAbstractDroitPAction {

    private static final Logger LOG = LoggerFactory.getLogger(APDroitAPGPAction.class);

    public APDroitAPGPAction(FWServlet servlet) {
        super(servlet);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        APDroitAPGDTO dto = new APDroitAPGDTO(((APDroitAPGPViewBean) viewBean).getDroit());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);

        return this.getUserActionURL(request, IAPActions.ACTION_SITUATION_PROFESSIONNELLE, FWAction.ACTION_CHERCHER
                + "&" + APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + dto.getIdDroit());
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        APDroitAPGDTO dto = new APDroitAPGDTO(((APDroitAPGPViewBean) viewBean).getDroit());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);

        return this.getUserActionURL(request, IAPActions.ACTION_SITUATION_PROFESSIONNELLE, FWAction.ACTION_CHERCHER
                + "&" + APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + dto.getIdDroit());
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

        if (Objects.nonNull(viewBeanAP) && viewBeanAP instanceof APDroitAPGPViewBean && ((APDroitAPGPViewBean)viewBeanAP).getACorriger()) {
            try {
                String method = request.getParameter("_method");
                FWAction privateAction = FWAction.newInstance(userAction[0]);
                FWViewBeanInterface viewBean = new APDroitAPGPViewBean();

                if (method != null && method.equalsIgnoreCase("ADD")) {
                    privateAction.changeActionPart(FWAction.ACTION_NOUVEAU);
                    viewBean = this.beforeNouveau(session, request, response, viewBeanAP);
                }

                if (method != null && method.equalsIgnoreCase("UPD")) {
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
        } else {
            if ((userAction != null) && (userAction.length > 1) && userAction[1].toLowerCase().equals("back")) {
                super.actionAfficherApresBack(session, request, response, mainDispatcher);
            } else {
                super.actionAfficher(session, request, response, mainDispatcher);
            }
        }

        FWViewBeanInterface viewBean = this.loadViewBean(session);

        APDroitAPGDTO dto = new APDroitAPGDTO(((APDroitAPGPViewBean) viewBean).getDroit());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);

    }

    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                    FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionReAfficher(session, request, response, mainDispatcher);
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
        APDroitAPGPViewBean actionAfficherViewBean = null;
        try {
            actionAfficherViewBean = (APDroitAPGPViewBean) mainDispatcher.dispatch(viewBean, getAction());
            this.saveViewBean(actionAfficherViewBean, session);
            return this.getUserActionURL(request, IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT,
                    "chercher&forIdDroit=" + viewBean.getIdDroit());
        } catch (Exception e) {
            this.saveViewBean(actionAfficherViewBean, session);
            throw e;
        }
    }

    /**
     * Ajout d'un nouveau droit. Step 1/3 de la saisie
     */
    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        APDroitAPGPViewBean viewBean = new APDroitAPGPViewBean();
        String action = request.getParameter("userAction");
        FWAction newAction = FWAction.newInstance(action);

        String destination = null;
        try {

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean = (APDroitAPGPViewBean) beforeAjouter(session, request, response, viewBean);

            // Appel du WebService Seodor
            if (viewBean.getACorriger()) {
                callWSSeodor(viewBean, mainDispatcher);
            }


            // Fin contrôle SEODOR
            // Si l'on corrige les données, alors on revient sur la page de modification du droit
            if (viewBean.hasMessagePropError()) {
                newAction = FWAction.newInstance(IAPActions.ACTION_SAISIE_CARTE_APG + ".afficher");
                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", viewBean);
                request.setAttribute("viewBean", viewBean);
                destination =request.getServletPath() + "?" + PRDefaultAction.USER_ACTION + "=" + newAction;
            } else{
                // Sinon, on continue le process originel
                viewBean = (APDroitAPGPViewBean) mainDispatcher.dispatch(viewBean, newAction);
                boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

                if (goesToSuccessDest) {
                    // APSituationProfessionnelleViewBean newViewBean = new APSituationProfessionnelleViewBean();
                    // newViewBean.setIdDroit(viewBean.getIdDroit());
                    // session.setAttribute("viewBean", newViewBean);
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

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        APDroitAPGPViewBean viewBean = new APDroitAPGPViewBean();
        String action = request.getParameter("userAction");
        FWAction newAction = FWAction.newInstance(action);

        String destination = null;
        try {

            JSPUtils.setBeanProperties(request, viewBean);
            viewBean = (APDroitAPGPViewBean) beforeAjouter(session, request, response, viewBean);

            //
            if (viewBean.getACorriger()) {
                callWSSeodor(viewBean, mainDispatcher);
            }

            // Fin contrôle SEODOR
            // Si l'on corrige les données, alors on revient sur la page de modification du droit
            if (viewBean.hasMessagePropError()) {
                newAction = FWAction.newInstance(IAPActions.ACTION_SAISIE_CARTE_APG + ".afficher");
                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", viewBean);
                request.setAttribute("viewBean", viewBean);
                destination =request.getServletPath() + "?" + PRDefaultAction.USER_ACTION + "=" + newAction;
            } else {
                viewBean = (APDroitAPGPViewBean) mainDispatcher.dispatch(viewBean, newAction);
                boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

                if (goesToSuccessDest) {
                    // APSituationProfessionnelleViewBean newViewBean = new APSituationProfessionnelleViewBean();
                    // newViewBean.setIdDroit(viewBean.getIdDroit());
                    // session.setAttribute("viewBean", newViewBean);
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

    @Override
    protected void actionFindNext(HttpSession session, HttpServletRequest request, HttpServletResponse response, FWDispatcher mainDispatcher) throws ServletException, IOException{

        APDroitAPGPViewBean viewBean = (APDroitAPGPViewBean) loadViewBean(session);
        String[] userAction = request.getParameterValues("userAction");
        String[] methode = request.getParameterValues("_method");
        FWAction newAction = null;

        if (methode[0].equalsIgnoreCase("read")) {
                newAction = FWAction.newInstance(IAPActions.ACTION_SITUATION_PROFESSIONNELLE + ".chercher");
        } else {
            if (viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION)){
                newAction = FWAction.newInstance(IAPActions.ACTION_SAISIE_CARTE_APG + ".ajouter");
			} else if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.EDITION)){
                newAction = FWAction.newInstance(IAPActions.ACTION_SAISIE_CARTE_APG + ".modifier");
			} else if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.LECTURE)){
                if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION) || viewBean.getModeEditionDroit().equals(APModeEditionDroit.EDITION) ){
                    newAction = FWAction.newInstance(IAPActions.ACTION_SAISIE_CARTE_APG + ".modifier");
                }
                else {
                    newAction = FWAction.newInstance(IAPActions.ACTION_SITUATION_PROFESSIONNELLE + ".chercher");
                }
			}
        }

        String destination =request.getServletPath() + "?" + PRDefaultAction.USER_ACTION + "=" + newAction;
        goSendRedirect(destination, request, response);
    }

    private List<APGSeodorDataBean> callWSSeodor (APDroitAPGPViewBean viewBean, FWDispatcher mainDispatcher) {

        List<APGSeodorDataBean> apgSeodorDataBeans = new ArrayList<>();
        List<APGSeodorErreurEntity> messagesError = new ArrayList<>();
        APGSeodorDataBean apgSeodorDataBean = new APGSeodorDataBean();

        try{
            if (!Objects.isNull(APProperties.SEODOR_TYPE_SERVICE.getValue()) && !APProperties.SEODOR_TYPE_SERVICE.getValue().isEmpty()
                    && APProperties.SEODOR_TYPE_SERVICE.getValue().contains(APGenreServiceAPG.resoudreGenreParCodeSystem(viewBean.getGenreService()).getCodePourAnnonce())) {
                // Controle SEODOR à implémenter

                // TODO mapper la requete
                String nss = viewBean.getNss().replaceAll("\\.","");
                PRPeriode periode1er = viewBean.getPeriodes().get(0);
                Date dateDebut = new Date(periode1er.getDateDeDebut());
                apgSeodorDataBean.setNss(nss);
                apgSeodorDataBean.setStartDate(dateDebut.toXMLGregorianCalendar());
                apgSeodorDataBeans = APGSeodorServiceCallUtil.getPeriode(((BSession) mainDispatcher.getSession()), apgSeodorDataBean);

                if (!apgSeodorDataBeans.isEmpty()) {
                    if (apgSeodorDataBeans.get(0).isHasTechnicalError()) {
                        messagesError.add(new APGSeodorErreurEntity(apgSeodorDataBeans.get(0).getMessageTechnicalError()));
                    } else {
                        List<PRPeriode> periodesAControler = viewBean.getPeriodes();
                        messagesError = APGSeodorServiceMappingUtil.controlePeriodesSeodor(apgSeodorDataBeans, periodesAControler);
                    }
                } else {
                    // TODO A remplacer par Label
                    messagesError.add(new APGSeodorErreurEntity("\nPas de données"));
                }
            }
        } catch (PropertiesException e) {
            // La propriété n'existe pas
            LOG.error("La propriété apg.rapg.genre.service.seodor n'a pas été trouvé : ", e);
            messagesError.add(new APGSeodorErreurEntity(((BSession) mainDispatcher.getSession()).getLabel("WEBSERVICE_SEODOR_PROP_MANQUANTE")));
        } catch (DatatypeConfigurationException e) {
            // TODO Gérer exception

            messagesError.add(new APGSeodorErreurEntity("Erreur de données lors de l'appel au webService"));
            viewBean.setMessagesError(messagesError);
            viewBean.setMessagePropError(true);
        } catch (ParseException e) {
            messagesError.add(new APGSeodorErreurEntity("Erreur de structure des données reçu de la centrale"));
            viewBean.setMessagesError(messagesError);
            viewBean.setMessagePropError(true);
        }

        // On ajoute les erreurs à la ViewBean et on la tag pour afficher les erreurs lors du rechargement de la page.
        if (!messagesError.isEmpty()) {
            viewBean.setMessagePropError(true);
            viewBean.setMessagesError(messagesError);
        }

        return apgSeodorDataBeans;
    }
}
