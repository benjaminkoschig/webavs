package globaz.ij.servlet;

import globaz.babel.api.doc.CTScalableDocumentFactory;
import globaz.babel.api.doc.ICTScalableDocument;
import globaz.babel.api.doc.ICTScalableDocumentProperties;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.ij.vb.process.IJGenererDecisionViewBean;
import globaz.ij.vb.prononces.IJNSSDTO;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author BSC
 */
public class IJGenererDecisionAction extends PRDefaultAction {

    protected static final String USER_ACTION = "userAction";

    public IJGenererDecisionAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp?";
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));

        try {
            FWViewBeanInterface viewBean = this.loadViewBean(session);
            if ((viewBean != null)
                    && (viewBean instanceof IJGenererDecisionViewBean)
                    && (((IJGenererDecisionViewBean) viewBean).isRetourDepuisPyxis() || ((ICTScalableDocument) viewBean)
                            .isPreviousAction())) {
                ((ICTScalableDocument) viewBean).setPreviousAction(false);
            } else {

                viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());

                viewBean.setISession(mainDispatcher.getSession());
                ((IJGenererDecisionViewBean) viewBean).setIsRetourDepuisPyxis(false);
                ((IJGenererDecisionViewBean) viewBean).setIdPersonneReference(((IJGenererDecisionViewBean) viewBean)
                        .getCurrentUserId());

                // on lui donne les paramètres en requête au cas ou.
                JSPUtils.setBeanProperties(request, viewBean);
            }

            viewBean = mainDispatcher.dispatch(viewBean, action);

            ((IJGenererDecisionViewBean) viewBean)
                    .setGeneratorImplClassName("globaz.ij.process.IJGenererDecisionProcess");
            ((IJGenererDecisionViewBean) viewBean).setReturnUrl(this.getUserActionURL(request,
                    IIJActions.ACTION_PRONONCE_JOINT_DEMANDE, FWAction.ACTION_CHERCHER));
            ((IJGenererDecisionViewBean) viewBean).setUrlFirstPage(this.getUserActionURL(request,
                    IIJActions.ACTION_GENERER_DECISION, FWAction.ACTION_AFFICHER));

            session.setAttribute(FWServlet.VIEWBEAN, viewBean);

            // On mémorise dans la session le Num. AVS
            IJNSSDTO dto = new IJNSSDTO();
            dto.setNSS(((IJGenererDecisionViewBean) viewBean).getNoAVSTiersPrincipal());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        destination = destination + PRDefaultAction.METHOD_ADD;

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionSelectionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        IJGenererDecisionViewBean gdViewBean = (IJGenererDecisionViewBean) this.loadViewBean(session);
        StringBuffer queryString = new StringBuffer();
        queryString.append(IJGenererDecisionAction.USER_ACTION);
        queryString.append("=");
        queryString.append("ij.process.genererDecision");
        queryString.append(".");
        queryString.append(FWAction.ACTION_AFFICHER);
        queryString.append("&idPrononce=");
        queryString.append(gdViewBean.getIdPrononce());
        // comportement par défaut
        super.actionSelectionner(session, request, response, mainDispatcher);
    }

    public void allerVersChoisirParagraphes(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {
        String destination = null;

        // on enregistre les scalbleProperties du document
        CTScalableDocumentFactory factory = CTScalableDocumentFactory.getInstance();

        try {

            JSPUtils.setBeanProperties(request, viewBean);

            ICTScalableDocumentProperties scalableProperties;
            if (((ICTScalableDocument) viewBean).getScalableDocumentProperties() == null) {
                scalableProperties = factory.createNewScalableDocumentProperties();
            } else {
                scalableProperties = ((ICTScalableDocument) viewBean).getScalableDocumentProperties();
            }

            scalableProperties.setParameter("idPrononce", request.getParameter("idPrononce"));
            scalableProperties.setParameter("idDemande", request.getParameter("idDemande"));
            scalableProperties.setParameter("eMailAddress", request.getParameter("eMailAddress"));
            scalableProperties.setParameter("dateSurDocument", request.getParameter("dateSurDocument"));
            scalableProperties.setParameter("idTierAdresseCourrier", request.getParameter("idTierAdresseCourrier"));
            scalableProperties.setParameter("beneficiaire", request.getParameter("beneficiaire"));
            scalableProperties.setParameter("idTierAdressePaiement", request.getParameter("idTierAdressePaiement"));
            scalableProperties.setParameter("idTierEmployeurAdressePaiement",
                    request.getParameter("idTierEmployeurAdressePaiement"));
            scalableProperties.setParameter("idTierAssureAdressePaiement",
                    request.getParameter("idTierAssureAdressePaiement"));
            scalableProperties.setParameter("idPersonneReference", request.getParameter("idPersonneReference"));
            scalableProperties.setParameter("cantonTauxImposition", request.getParameter("cantonTauxImposition"));
            scalableProperties.setParameter("tauxImposition", request.getParameter("tauxImposition"));
            scalableProperties.setParameter("remarque", request.getParameter("remarque"));
            scalableProperties.setParameter("isSentToGed", request.getParameter("isSentToGed"));
            scalableProperties.setParameter("garantitRevision", request.getParameter("garantitRevision"));
            scalableProperties.setParameter("isSendToGed", request.getParameter("isSendToGed"));
            scalableProperties.setParameter("displaySendToGed", request.getParameter("displaySendToGed"));

            scalableProperties.setParameter("personnalisationAdressePaiement",
                    request.getParameter("personnalisationAdressePaiement"));

            scalableProperties.setParameter("idTiersAdressePaiementPersonnalisee",
                    request.getParameter("idTiersAdressePaiementPersonnalisee"));
            scalableProperties.setParameter("idDomaineApplicationAdressePaiementPersonnalisee",
                    request.getParameter("idDomaineApplicationAdressePaiementPersonnalisee"));
            scalableProperties.setParameter("numAffilieAdressePaiementPersonnalisee",
                    request.getParameter("numAffilieAdressePaiementPersonnalisee"));

            request.setAttribute("remarque", "");
            session.setAttribute("remarque", "");

            // on cherche l'id du tiers principal
            PRTiersWrapper tiers = PRTiersHelper.getTiers(mainDispatcher.getSession(),
                    request.getParameter("noAvsTiersPrincipal"));
            scalableProperties.setIdTiersPrincipal(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));

            // TODO prendre le bon document (Decision IJ) depuis IJ.properties
            scalableProperties.setIdDocument("14000000001");

            // les paramètres du premier écran
            ((ICTScalableDocument) viewBean).setScalableDocumentProperties(scalableProperties);
            ((ICTScalableDocument) viewBean).setEMailAddress(request.getParameter("eMailAddress"));

            // pour la navigation
            ((ICTScalableDocument) viewBean).setWantSelectParagraph(false);
            ((ICTScalableDocument) viewBean).setWantEditParagraph(false);
            ((ICTScalableDocument) viewBean).setWantSelectAnnexeCopie(true);

            destination = ((ICTScalableDocument) viewBean).getNextUrl();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Validation
        mainDispatcher.dispatch(viewBean, getAction());

        session.setAttribute(FWServlet.VIEWBEAN, viewBean);
        request.setAttribute(FWServlet.VIEWBEAN, viewBean);

        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

        if (!goesToSuccessDest) {
            destination = _getDestAjouterEchec(session, request, response, viewBean);
        }

        request.setAttribute(FWServlet.VIEWBEAN, viewBean);

        forward(destination, request, response);

    }

    public String arreterGenererDecision(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {
        try {
            getAction().changeActionPart(FWAction.ACTION_AFFICHER);
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }

        mainDispatcher.dispatch(viewBean, getAction());
        session.removeAttribute(FWServlet.VIEWBEAN);

        return this.getUserActionURL(request, IIJActions.ACTION_PRONONCE_JOINT_DEMANDE, FWAction.ACTION_CHERCHER);
    }

    /**
     * Crée une chaîne de caractères du type '/nomServlet?userAction=action'
     * 
     * @param action
     *            une action COMPLETE (avec actionPart)
     */
    @Override
    protected String getUserActionURL(HttpServletRequest request, String action) {
        return request.getServletPath() + "?" + IJGenererDecisionAction.USER_ACTION + "=" + action;
    }

    /**
     * Crée une chaîne de caractères du type '/nomServlet?userAction=actionBase.actionPart'
     * 
     * @param actionBase
     *            les parties application, package et class de la chaîne action
     * @param actionPart
     *            la partie action de la chaîne action (exemple 'afficher')
     */
    @Override
    protected String getUserActionURL(HttpServletRequest request, String actionBase, String actionPart) {
        return this.getUserActionURL(request, actionBase + "." + actionPart);
    }
}
