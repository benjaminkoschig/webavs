package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.framework.utils.params.FWParamString;
import globaz.framework.utils.urls.FWUrl;
import globaz.framework.utils.urls.FWUrlsStack;
import globaz.globall.db.BSessionUtil;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.log.business.renderer.JadeBusinessMessageRendererJson;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.pegasus.vb.decision.IPCDecisionViewBean;
import globaz.pegasus.vb.decision.PCDecisionApresCalculViewBean;
import globaz.pegasus.vb.decision.PCDecisionRefusViewBean;
import globaz.pegasus.vb.decision.PCDecisionSuppressionViewBean;
import globaz.pegasus.vb.decision.PCDecomptViewBean;
import globaz.pegasus.vb.decision.PCDevalidationDecisionViewBean;
import globaz.pegasus.vb.decision.PCImprimerDecisionsViewBean;
import globaz.pegasus.vb.decision.PCPrepDecisionApresCalculViewBean;
import globaz.pegasus.vb.decision.PCPrepDecisionRefusViewBean;
import globaz.pegasus.vb.decision.PCPrepDecisionSuppressionViewBean;
import globaz.pegasus.vb.decision.PCValidationDecisionsViewBean;
import globaz.prestation.ged.PRGedAffichageDossier;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ch.globaz.pegasus.business.constantes.IPCActions;
import ch.globaz.pegasus.business.constantes.IPCDecision;

public class PCDecisionServletAction extends PCAbstractServletAction {

    private static final String BACK_TO_DETAIL_DECISION_AC = "/pegasus?userAction=pegasus.decision.decisionApresCalcul.afficher";
    private static final String BACK_TO_DETAIL_DECISION_REF = "/pegasus?userAction=pegasus.decision.decisionRefus.afficher";
    private static final String BACK_TO_DETAIL_DECISION_SUP = "/pegasus?userAction=pegasus.decision.decisionSuppression.afficher";
    private static final String BACK_TO_DETAIL_VALIDATION_DAC = "/pegasus?userAction=pegasus.decision.validationDecisions.afficher";

    private static final String DECISION_FROM_REPRISE_ERROR_ID = "pegasus.decisions.reprise.nodetail";

    private static final String DECISION_LIST_URL = "/pegasus?userAction=pegasus.decision.decision.chercher";

    private static final String ERROR_JSON_MSG_PARAMETER = "&decisionErrorMsg=";
    private static final String ID_DECISION_PARAM = "&idDecision=";
    private static final String RE_AFFICHER_PREPARATION_DAC = "/pegasus?userAction=pegasus.decision.prepDecisionApresCalcul.reAfficher";
    private static final String RE_AFFICHER_PREPARATION_DECREF = "/pegasus?userAction=pegasus.decision.prepDecisionRefus.reAfficher";
    private static final String RE_AFFICHER_PREPARATION_DECSUP = "/pegasus?userAction=pegasus.decision.prepDecisionSuppression.reAfficher";
    private static final String RE_AFFICHER_VALIDATION_DECISION = "/pegasus?userAction=pegasus.decision.validationDecision.reAfficher";

    /**
     * Affichage du dossier en GED
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @throws ServletException
     * @throws IOException
     * @throws JadeServiceLocatorException
     * @throws JadeServiceActivatorException
     * @throws NullPointerException
     * @throws ClassCastException
     * @throws JadeClassCastException
     */
    public void actionAfficherDossierGed(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException,
            JadeServiceLocatorException, JadeServiceActivatorException, NullPointerException, ClassCastException,
            JadeClassCastException {
        PRGedAffichageDossier.actionAfficherDossierGed(session, request, response, mainDispatcher, viewBean);
    }

    /**
     * Gestion des paramnètres en fonction du viewBean (type) de décision
     * 
     * @param request
     * @param viewBean
     */
    private static void dealDecisionViewBean(HttpServletRequest request, FWViewBeanInterface viewBean) {
        // Si decision dee refus detail
        if (viewBean instanceof PCDecisionRefusViewBean) {
            ((PCDecisionRefusViewBean) viewBean).setIdDemande(request.getParameter("idDemandePc"));
            ((PCDecisionRefusViewBean) viewBean).setIdDecision(request.getParameter("idDecision"));

        }

        // Si decision de suppression detail
        if (viewBean instanceof PCDecisionSuppressionViewBean) {
            // ((PCDecisionSuppressionViewBean) viewBean).setIdDroit(request.getParameter("idDroit"));
            ((PCDecisionSuppressionViewBean) viewBean).setIdDecision(request.getParameter("idDecision"));
            // ((PCDecisionSuppressionViewBean) viewBean).setIdVersionDroit(request.getParameter("idVersionDroit"));

        }
        // Si decision apres calcul detail
        if (viewBean instanceof PCDecisionApresCalculViewBean) {
            // ((PCDecisionApresCalculViewBean) viewBean).setIdDroit(request.getParameter("idDroit"));
            ((PCDecisionApresCalculViewBean) viewBean).setIdDecision(request.getParameter("idDecision"));
            ((PCDecisionApresCalculViewBean) viewBean).setIdVersionDroit(request.getParameter("idVersionDroit"));
            // ((PCDecisionApresCalculViewBean) viewBean).setNoVersion(request.getParameter("noVersion"));
            ((PCDecisionApresCalculViewBean) viewBean).setLotDecision(request.getParameter("lotPagination"));
        }

        // Si validation decision apres calcul detail
        if ((viewBean instanceof PCValidationDecisionsViewBean)) {

            ((PCValidationDecisionsViewBean) viewBean).setIdDroit(request.getParameter("idDroit"));
            ((PCValidationDecisionsViewBean) viewBean).setIdVersionDroit(request.getParameter("idVersionDroit"));
            ((PCValidationDecisionsViewBean) viewBean).setNoVersion(request.getParameter("noVersion"));
        }
        // impression des décision AC
        if ((viewBean instanceof PCImprimerDecisionsViewBean)) {
            ((PCImprimerDecisionsViewBean) viewBean).setIdDroit(request.getParameter("idDroit"));
            ((PCImprimerDecisionsViewBean) viewBean).setIdVersionDroit(request.getParameter("idVersionDroit"));
            ((PCImprimerDecisionsViewBean) viewBean).setNoVersion(request.getParameter("noVersion"));
            ((PCImprimerDecisionsViewBean) viewBean).setIdDecision(request.getParameter("idDecision"));
            ((PCImprimerDecisionsViewBean) viewBean).setIdDemandePc(request.getParameter("idDemande"));
            ((PCImprimerDecisionsViewBean) viewBean).setMailGest(request.getParameter("mailGest"));
            ((PCImprimerDecisionsViewBean) viewBean).setPersref(request.getParameter("persref"));
        }
    }

    /**
     * Gestion du type de paramètres à setter dans le viewBEan en fonction du type de décisions
     * 
     * @param viewBean
     * @param request
     * @param typePreparationParametre
     */
    private static void dealPreparationType(FWViewBeanInterface viewBean, HttpServletRequest request,
            String typePreparationParametre) {

        // Si decision de suppression
        if (IPCDecision.CS_TYPE_SUPPRESSION_SC.equals(typePreparationParametre)) {
            ((PCPrepDecisionSuppressionViewBean) viewBean).setIdDroit(request.getParameter("idDroit"));
            ((PCPrepDecisionSuppressionViewBean) viewBean).setIdVersionDroit(request.getParameter("idVersionDroit"));
        }
        // Si decision de refus
        if (IPCDecision.CS_TYPE_REFUS_SC.equals(typePreparationParametre)) {
            ((PCPrepDecisionRefusViewBean) viewBean).setIdDemande(request.getParameter("idDemandePc"));
        }
        // Si decision aprescalcul
        if (IPCDecision.CS_TYPE_OCTROI_AC.equals(typePreparationParametre)
                || IPCDecision.CS_TYPE_REFUS_AC.equals(typePreparationParametre)
                || IPCDecision.CS_TYPE_PARTIEL_AC.equals(typePreparationParametre)) {
            ((PCPrepDecisionApresCalculViewBean) viewBean).setIdVersionDroit(request.getParameter("idVersionDroit"));
            ((PCPrepDecisionApresCalculViewBean) viewBean).setNoVersion(request.getParameter("noVersion"));
        }
    }

    private final String DECISION_IMPRIMER_ERROR_PERSREF = "pegasus.decisions.impression.nopersref";
    private String destination = "";

    private final String PREPARATION_PATH = "/pegasus?userAction=pegasus.decision";

    /**
     * Constructeur
     * 
     * @param aServlet
     */
    public PCDecisionServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    /**
     * Retourne l'url en cas de problème d'ajout
     */
    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PCPrepDecisionRefusViewBean) {
            return PREPARATION_PATH + ".prepDecisionRefus.reAfficher";
        }
        if (viewBean instanceof PCPrepDecisionSuppressionViewBean) {
            return PREPARATION_PATH + ".prepDecisionSuppression.reAfficher";
        }
        if (viewBean instanceof PCPrepDecisionApresCalculViewBean) {

            return PREPARATION_PATH + ".prepDecisionApresCalcul.reAfficher";
        }

        return "";
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String id = null;
        String responseUrl = null;

        // Si decision de refus
        if (viewBean instanceof PCPrepDecisionRefusViewBean) {
            id = ((PCPrepDecisionRefusViewBean) viewBean).getDecisionRefus().getDecisionHeader()
                    .getSimpleDecisionHeader().getId();
            responseUrl = PCDecisionServletAction.DECISION_LIST_URL.concat("&idDecision=" + id);
        } else if ((viewBean instanceof PCPrepDecisionSuppressionViewBean)
                && IPCDecision.CS_MOTIF_SUPPRESSION_TRANSFERT_DOSSIER
                        .equals(((PCPrepDecisionSuppressionViewBean) viewBean).getDecisionSuppression()
                                .getSimpleDecisionSuppression().getCsMotif())) {
            // Si décision de suppression pour un transfert de dossier dans un autre canton, aller à la page de détail
            // pour saisir la suite
            PCPrepDecisionSuppressionViewBean vb = (PCPrepDecisionSuppressionViewBean) viewBean;
            responseUrl = PCDecisionServletAction.BACK_TO_DETAIL_DECISION_SUP + "&idVersionDroit="
                    + request.getParameter("idVersionDroit") + "&idDroit=" + vb.getIdDroit() + "&noVersion="
                    + vb.getDecisionSuppression().getVersionDroit().getSimpleVersionDroit().getNoVersion()
                    + "&idDecision=" + vb.getDecisionSuppression().getSimpleDecisionSuppression().getIdDecisionHeader();
        } else {
            if (!"".equals(request.getParameter("idVersionDroit"))) {
                responseUrl = PCDecisionServletAction.DECISION_LIST_URL.concat("&idVersionDroit=")
                        + request.getParameter("idVersionDroit");
            } else {
                responseUrl = PCDecisionServletAction.DECISION_LIST_URL;
            }

        }

        return responseUrl;
    }

    @Override
    protected String _getDestExecuterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        String parametersStr = "&idVersionDroit=" + request.getParameter("idVersionDroit") + "&idDroit="
                + request.getParameter("idDroit") + "&noVersion=" + request.getParameter("noVersion") + "&idDecision="
                + request.getParameter("idDecision");

        return PCDecisionServletAction.BACK_TO_DETAIL_DECISION_AC.concat(parametersStr);
    }

    @Override
    protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if ((viewBean instanceof PCDecisionApresCalculViewBean)) {
            String parametersStr = "&idVersionDroit=" + request.getParameter("idVersionDroit") + "&idDroit="
                    + request.getParameter("idDroit") + "&noVersion=" + request.getParameter("noVersion")
                    + "&idDecision=" + request.getParameter("idDecision");
            return PCDecisionServletAction.BACK_TO_DETAIL_DECISION_AC.concat(parametersStr);
        } else if (viewBean instanceof PCDecisionSuppressionViewBean) {
            String parametersStr = "&idVersionDroit=" + request.getParameter("idVersionDroit") + "&idDroit="
                    + request.getParameter("idDroit") + "&noVersion=" + request.getParameter("noVersion")
                    + "&idDecision=" + request.getParameter("idDecision");
            return PCDecisionServletAction.BACK_TO_DETAIL_DECISION_SUP.concat(parametersStr);
        } else if (viewBean instanceof PCValidationDecisionsViewBean) {
            String parametersStr = "&idVersionDroit=" + request.getParameter("idVersionDroit") + "&idDroit="
                    + request.getParameter("idDroit") + "&noVersion=" + request.getParameter("noVersion")
                    + "&idDecision=" + request.getParameter("idDecision");

            return PCDecisionServletAction.DECISION_LIST_URL.concat(parametersStr);
        } else {
            return PCDecisionServletAction.DECISION_LIST_URL;
        }

    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if ((viewBean instanceof PCDecisionApresCalculViewBean)) {
            String parametersStr = "&idVersionDroit=" + request.getParameter("idVersionDroit") + "&idDroit="
                    + request.getParameter("idDroit") + "&noVersion=" + request.getParameter("noVersion")
                    + "&idDecision=" + request.getParameter("idDecision");
            return PCDecisionServletAction.BACK_TO_DETAIL_DECISION_AC.concat(parametersStr);
        } else if (viewBean instanceof PCDecisionSuppressionViewBean) {
            String parametersStr = "&idVersionDroit=" + request.getParameter("idVersionDroit") + "&idDroit="
                    + request.getParameter("idDroit") + "&noVersion=" + request.getParameter("noVersion")
                    + "&idDecision=" + request.getParameter("idDecision");
            return PCDecisionServletAction.BACK_TO_DETAIL_DECISION_SUP.concat(parametersStr);
        } else if (viewBean instanceof PCDecisionRefusViewBean) {
            String parametersStr = "&idVersionDroit=" + request.getParameter("idVersionDroit") + "&idDroit="
                    + request.getParameter("idDroit") + "&noVersion=" + request.getParameter("noVersion")
                    + "&idDecision=" + request.getParameter("idDecision") + "&idDemandePc="
                    + request.getParameter("idDemandePc");

            return PCDecisionServletAction.BACK_TO_DETAIL_DECISION_REF.concat(parametersStr);
        } else if (viewBean instanceof PCValidationDecisionsViewBean) {
            String parametersStr = "&idVersionDroit=" + request.getParameter("idVersionDroit") + "&idDroit="
                    + request.getParameter("idDroit") + "&noVersion=" + request.getParameter("noVersion")
                    + "&idDecision=" + request.getParameter("idDecision");

            return PCDecisionServletAction.DECISION_LIST_URL.concat(parametersStr);
        } else {
            return PCDecisionServletAction.DECISION_LIST_URL;
        }

    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        super.actionAfficher(session, request, response, mainDispatcher);

    }

    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String action = request.getParameter("userAction");
        if (IPCActions.ACTION_DECISION_AC_IMPRIMER.equals(action)) {

            if (JadeStringUtil.isBlankOrZero(request.getParameter("persref"))) {
                actionReAfficher(session, request, response, mainDispatcher);
            } else {
                FWViewBeanInterface viewBean = new PCImprimerDecisionsViewBean();
                session.setAttribute("viewBean", viewBean);
                super.actionExecuter(session, request, response, mainDispatcher);
            }

            // } else if (IPCActions.ACTION_DECISION_DEVALIDER.equals(action)) {
            // super.actionExecuter(session, request, response, mainDispatcher);
            // TODO la bonne pratique voudrait que par défaut le super soit appelé. Verifier si c'est possible
            // } else {
            // super.actionExecuter(session, request, response, mainDispatcher);

        }
    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        try {

            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);
            /*
             * recupération du bean depuis la sesison
             */
            FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
            /*
             * set des properietes
             */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */
            viewBean = beforeModifier(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, newAction);
            session.setAttribute("viewBean", viewBean);
            /*
             * Gestion des erreurs si erreur
             */
            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                destination = generateJadeThreadErrorLevelUrl(viewBean, getAction().getApplicationPart(), request);
            } else if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
                destination = generateFWViewBeanInterfaceErrorUrl(viewBean, session);
            } else {
                // tout est ok
                destination = _getDestModifierSucces(session, request, response, viewBean);
            }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(destination, request, response);
    }

    @Override
    protected void actionReAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {

        StringBuilder jsp = new StringBuilder(getRelativeURL(request, session));

        if (session.getAttribute("viewBean") instanceof PCPrepDecisionApresCalculViewBean) {
            PCPrepDecisionApresCalculViewBean viewBean = (PCPrepDecisionApresCalculViewBean) session
                    .getAttribute("viewBean");
            viewBean.setMessage("");
            jsp.append("_de.jsp");
        } else if (session.getAttribute("viewBean") instanceof PCPrepDecisionSuppressionViewBean) {
            PCPrepDecisionSuppressionViewBean viewBean = (PCPrepDecisionSuppressionViewBean) session
                    .getAttribute("viewBean");
            jsp.append("_de.jsp");
        } else if (session.getAttribute("viewBean") instanceof PCImprimerDecisionsViewBean) {
            jsp = new StringBuilder("/pegasusRoot/decision/imprimerDecisions_de.jsp");
            request.setAttribute("errorMsg", DECISION_IMPRIMER_ERROR_PERSREF);
        } else {
            jsp.append("_de.jsp");
        }

        String processLaunchedStr = request.getParameter("process");
        boolean processSeemsOk = "launched".equals(processLaunchedStr);

        String validFail = processSeemsOk ? "" : "?_valid=fail";

        servlet.getServletContext().getRequestDispatcher(jsp.toString()).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        // gestion des paramètres en fonction du type
        PCDecisionServletAction.dealPreparationType(viewBean, request, request.getParameter("csTypeDecisionPrep"));
        // gestion des paramètres en fonction du viewBean
        PCDecisionServletAction.dealDecisionViewBean(request, viewBean);

        // Si decompte
        if (viewBean instanceof PCDecomptViewBean) {
            ((PCDecomptViewBean) viewBean).setIdVersionDroit(request.getParameter("idVersionDroit"));
            ((PCDecomptViewBean) viewBean).setIdDecision(request.getParameter("idDecision"));
            ((PCDecomptViewBean) viewBean).setNoVersion(request.getParameter("noVersion"));
            ((PCDecomptViewBean) viewBean).setIdDemande(request.getParameter("idDemandePc"));
            if (JadeStringUtil.isEmpty(((PCDecomptViewBean) viewBean).getIdDemande())) {
                ((PCDecomptViewBean) viewBean).setIdDemande(request.getParameter("idDemande"));
            }
            ((PCDecomptViewBean) viewBean).setIdDroit(request.getParameter("idDroit"));
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    @Override
    protected FWViewBeanInterface beforeModifier(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {

        // Gestion motifs sous motifs decref
        if (viewBean instanceof PCDecisionRefusViewBean) {
            if (JadeStringUtil.isEmpty(request.getParameter("decisionRefus.simpleDecisionRefus.csSousMotif"))) {
                ((PCDecisionRefusViewBean) viewBean).getDecisionRefus().getSimpleDecisionRefus().setCsSousMotif("0");
            }
        }
        // Gestion motifs sous motifs decsup
        if (viewBean instanceof PCDecisionSuppressionViewBean) {
            if (JadeStringUtil.isEmpty(request
                    .getParameter("decisionSuppression.simpleDecisionSuppression.csSousMotif"))) {
                ((PCDecisionSuppressionViewBean) viewBean).getDecisionSuppression().getSimpleDecisionSuppression()
                        .setCsSousMotif("0");
            }
        }

        return viewBean;
    }

    /**
     * Custon action détail, pour permettre la gestion des messages d'erreurs liés au décision issues de la reprise
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @param viewBean
     * @return
     * @throws Exception
     */
    public String detail(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws Exception {

        viewBean = FWViewBeanActionFactory.newInstance(getAction(), dispatcher.getPrefix());
        JSPUtils.setBeanProperties(request, viewBean);

        viewBean = dispatcher.dispatch(viewBean, getAction());
        // String idDecision = ((PCDecisionApresCalculViewBean)viewBean).getIdDecision();
        JadeBusinessMessage decisionWithNoDetail = null;

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            // on traite que le message d'erreur lié à une décision sans détail
            for (JadeBusinessMessage message : JadeThread.logMessages()) {
                if (message.getMessageId().equals(PCDecisionServletAction.DECISION_FROM_REPRISE_ERROR_ID)) {
                    decisionWithNoDetail = message;
                }
            }

            StringBuilder destination = new StringBuilder(PCDecisionServletAction.DECISION_LIST_URL + "&errorMsg=");
            destination.append(decisionWithNoDetail.getMessageId());
            return addParametersFrom(request, destination.toString());

        } else if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            String destination = FWDefaultServletAction.ERROR_PAGE;
            session.setAttribute("viewBean", viewBean);
            return destination;
        }

        return addParametersFrom(request, getDestinationAfterDetail((IPCDecisionViewBean) viewBean));
    }

    /**
     * Custom action dévalidation
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @param viewBean
     * @return
     * @throws Exception
     */
    public String devalider(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws Exception {

        // preparation du viewbean pour le handler
        viewBean = FWViewBeanActionFactory.newInstance(getAction(), dispatcher.getPrefix());
        JSPUtils.setBeanProperties(request, viewBean);

        viewBean = dispatcher.dispatch(viewBean, getAction());

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            return generateJadeThreadErrorLevelUrl(viewBean, getAction().getApplicationPart(), request);
        } else if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            return generateFWViewBeanInterfaceErrorUrl(viewBean, session);
        }
        // session.setAttribute("viewBean", oldViewBean);
        // retour à la page des décisions
        return new StringBuilder(PCDecisionServletAction.DECISION_LIST_URL).append("&idDroit=")
                .append(request.getParameter("idDroit")).append("&idVersionDroit=")
                .append(request.getParameter("idVersionDroit")).toString();

    }

    /**
     * Traitement des erreurs [FWViewBeanInterface.ERROR] et retour de l'url avec id error en paramètres
     * 
     * @param viewBean
     * @param session
     * @return
     */
    private String generateFWViewBeanInterfaceErrorUrl(FWViewBeanInterface viewBean, HttpSession session) {
        String destination = FWDefaultServletAction.ERROR_PAGE;
        session.setAttribute("viewBean", viewBean);
        return destination;
    }

    /**
     * Traitement des erreurs [JadeThread.logLevel Error] et retour de l'url avec error json en paramètres
     * 
     * @param viewBean
     * @param appPart
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    private String generateJadeThreadErrorLevelUrl(FWViewBeanInterface viewBean, String appPart,
            HttpServletRequest request) throws UnsupportedEncodingException {

        // StringBuilder pour url de retour
        StringBuilder destination = null;
        StringBuilder mainUrl = new StringBuilder("");

        JadeBusinessMessageRendererJson msgrender = new JadeBusinessMessageRendererJson();
        String jsonMessageError = msgrender.render(JadeThread.logMessages(), BSessionUtil.getSessionFromThreadContext()
                .getIdLangueISO());

        // **********Préparation************, on passe par la méthode réAffiche, avec l'erreur json en paramètre
        // DAC
        if (viewBean instanceof PCPrepDecisionApresCalculViewBean) {
            mainUrl.append(PCDecisionServletAction.RE_AFFICHER_PREPARATION_DAC);
        }
        // DECSUP
        else if (viewBean instanceof PCPrepDecisionSuppressionViewBean) {
            mainUrl.append(PCDecisionServletAction.RE_AFFICHER_PREPARATION_DECSUP);
        }
        // DECREf
        else if (viewBean instanceof PCPrepDecisionRefusViewBean) {
            mainUrl.append(PCDecisionServletAction.RE_AFFICHER_PREPARATION_DECREF);
        }

        // ************ Prévalidation et modification (update) **************, on relance le retrieve pour nepas garder
        // les
        // champs en erreur dans le viewBean
        // Update et prevalidation DAC
        else if (viewBean instanceof PCDecisionApresCalculViewBean) {
            mainUrl.append(PCDecisionServletAction.BACK_TO_DETAIL_DECISION_AC);
        }// Suppression
        else if (viewBean instanceof PCDecisionSuppressionViewBean) {
            mainUrl.append(PCDecisionServletAction.BACK_TO_DETAIL_DECISION_SUP);
        }// refus
        else if (viewBean instanceof PCDecisionRefusViewBean) {
            mainUrl.append(PCDecisionServletAction.BACK_TO_DETAIL_DECISION_REF);
        }
        // *************************** Vaidation **********************************3
        else if (viewBean instanceof PCValidationDecisionsViewBean) {
            mainUrl.append(PCDecisionServletAction.BACK_TO_DETAIL_VALIDATION_DAC);
        } else if (viewBean instanceof PCDevalidationDecisionViewBean) {

            FWUrlsStack stack = (FWUrlsStack) request.getSession().getAttribute(FWServlet.URL_STACK);
            FWUrl last = stack.peekAt(stack.size() - 2);
            FWParamString actionLast = last.getParam("userAction");
            String action = "";
            if (actionLast != null) {
                action = (String) actionLast.getValue();
            }
            FWAction fwAction = FWAction.newInstance(action);
            // /pegasus?userAction=pegasus.decision.decisionSuppression.afficher
            mainUrl.append("pegasus?userAction=");
            mainUrl.append(fwAction.getApplicationPart());
            mainUrl.append(".");
            mainUrl.append(fwAction.getPackagePart());
            mainUrl.append(".");
            mainUrl.append(fwAction.getClassPart());
            mainUrl.append(".afficher");
        }
        // ajout paramètres erreurs
        mainUrl.append(PCDecisionServletAction.ERROR_JSON_MSG_PARAMETER).append(jsonMessageError);
        // retour sur détail
        return addParametersFrom(request, mainUrl.toString());
    }

    /**
     * Retourne la destination du détail d'une décision (REFUS, SUPPRESSION OU DAC)
     * 
     * @param viewBean
     * @return
     */
    private String getDestinationAfterDetail(IPCDecisionViewBean viewBean) {
        StringBuilder dest = null;
        String idDecision = viewBean.getIdDecision();

        if (viewBean instanceof PCDecisionApresCalculViewBean) {

            dest = new StringBuilder(PCDecisionServletAction.BACK_TO_DETAIL_DECISION_AC);
        } else if (viewBean instanceof PCDecisionRefusViewBean) {
            dest = new StringBuilder(PCDecisionServletAction.BACK_TO_DETAIL_DECISION_REF);
        } else if (viewBean instanceof PCDecisionSuppressionViewBean) {
            dest = new StringBuilder(PCDecisionServletAction.BACK_TO_DETAIL_DECISION_SUP);
        }
        dest.append(PCDecisionServletAction.ID_DECISION_PARAM).append(idDecision);
        return dest.toString();
    }

    public String imprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws Exception {

        // gestion prorpiétés bean
        JSPUtils.setBeanProperties(request, viewBean);
        // dispatch action
        viewBean = dispatcher.dispatch(viewBean, getAction());

        // Si erreur
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            return generateJadeThreadErrorLevelUrl(viewBean, getAction().getApplicationPart(), request);
        } else if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            return generateFWViewBeanInterfaceErrorUrl(viewBean, session);
        }

        // StringBuilder pour retour url
        StringBuilder dest = null;

        if (viewBean instanceof PCDecisionSuppressionViewBean) {
            PCDecisionSuppressionViewBean vb = (PCDecisionSuppressionViewBean) viewBean;

            dest = new StringBuilder(PCDecisionServletAction.BACK_TO_DETAIL_DECISION_SUP);
            dest.append("&idDroit=").append(vb.getIdDroit());
            dest.append("&idVersionDroit=").append(vb.getIdVersionDroit());
            dest.append("&idDecision=").append(vb.getIdDecision());
        } else {
            String destination = FWDefaultServletAction.ERROR_PAGE;
            session.setAttribute("viewBean", viewBean);
            return destination;
        }

        return dest.toString();
    }

    /**
     * Custon action préparer décision
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @param viewBean
     * @return
     * @throws Exception
     */
    public String preparer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws Exception {

        // Set properties Bean
        JSPUtils.setBeanProperties(request, viewBean);
        // lancement action
        viewBean = dispatcher.dispatch(viewBean, getAction());

        // Si erreur
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            return generateJadeThreadErrorLevelUrl(viewBean, getAction().getApplicationPart(), request);
        } else if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            return generateFWViewBeanInterfaceErrorUrl(viewBean, session);
        }

        // Si tout est ok, retour sur la liste des décisions
        StringBuilder responseUrl = new StringBuilder(PCDecisionServletAction.DECISION_LIST_URL);

        if (!JadeStringUtil.isBlank(request.getParameter("idVersionDroit"))) {
            responseUrl.append("&idVersionDroit=").append(request.getParameter("idVersionDroit"));
        }
        if (!JadeStringUtil.isBlank(request.getParameter("idDemandePc"))) {
            responseUrl.append("&idDemandePc=").append(request.getParameter("idDemandePc"));
        }

        return responseUrl.toString();
    }

    /**
     * Custom action prevalider
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @param viewBean
     * @return
     * @throws Exception
     */
    public String prevalider(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws Exception {

        // gestion prorpiétés bean
        JSPUtils.setBeanProperties(request, viewBean);
        // dispatch action
        viewBean = dispatcher.dispatch(viewBean, getAction());

        // Si erreur
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)
                || JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            return generateJadeThreadErrorLevelUrl(viewBean, getAction().getApplicationPart(), request);

        } else if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            return generateFWViewBeanInterfaceErrorUrl(viewBean, session);
        }
        // retour sur détail de la décision
        return addParametersFrom(request, getDestinationAfterDetail((IPCDecisionViewBean) viewBean));
    }

    /**
     * Custom action valider
     * 
     * @param session
     * @param request
     * @param response
     * @param dispatcher
     * @param viewBean
     * @return
     * @throws Exception
     */
    public String valider(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws Exception {

        // recup appPart
        String appPart = getAction().getApplicationPart();
        // gestion prorpiétés bean
        JSPUtils.setBeanProperties(request, viewBean);
        // dispatch action
        viewBean = dispatcher.dispatch(viewBean, getAction());

        // Si erreur
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            return generateJadeThreadErrorLevelUrl(viewBean, appPart, request);
        } else if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            return generateFWViewBeanInterfaceErrorUrl(viewBean, session);
        }

        // StringBuilder pour retour url
        StringBuilder dest = null;

        // validations DAC
        if (viewBean instanceof PCValidationDecisionsViewBean) {
            PCValidationDecisionsViewBean vb = (PCValidationDecisionsViewBean) viewBean;

            dest = new StringBuilder(PCDecisionServletAction.DECISION_LIST_URL);
            dest.append("&idDroit=").append(vb.getIdDroit());
            dest.append("&idVersionDroit=").append(vb.getIdVersionDroit());
        }
        // Validation DECSUP
        else if (viewBean instanceof PCDecisionSuppressionViewBean) {
            // String url = BACK_TO_DETAIL_DECISION_SUP;
            // return this.addParametersFrom(request, PCDecisionServletAction.BACK_TO_DETAIL_DECISION_SUP);
            return addParametersFrom(request, PCDecisionServletAction.DECISION_LIST_URL);
        }
        // Validation DECREF
        else if (viewBean instanceof PCDecisionRefusViewBean) {

            return addParametersFrom(request, PCDecisionServletAction.DECISION_LIST_URL);
        }

        return dest.toString();
    }

}