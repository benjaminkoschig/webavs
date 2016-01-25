package globaz.apg.servlet;

import globaz.apg.vb.droits.APInfoComplViewBean;
import globaz.apg.vb.process.APGenererDecisionRefusViewBean;
import globaz.babel.api.doc.CTScalableDocumentFactory;
import globaz.babel.api.doc.ICTScalableDocument;
import globaz.babel.api.doc.ICTScalableDocumentProperties;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author JJE
 */
public class APGenererDecisionRefusAction extends PRDefaultAction {

    private final String AFFICHER_DEPUIS_CHOIX_ANN_COP_ACTIONPART = "afficherDepuisChoixAnnexesCopies";
    private final String GENERER_DECISION_REFUS_PROCESS_CLASSNAME = "globaz.apg.process.APGenererDecisionRefusProcess";

    public APGenererDecisionRefusAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        APInfoComplViewBean InputVb = (APInfoComplViewBean) session.getAttribute(FWServlet.VIEWBEAN);

        String destination = getRelativeURL(request, session) + "_de.jsp?" + METHOD_UPD;

        try {
            APGenererDecisionRefusViewBean viewBean = new APGenererDecisionRefusViewBean();

            // on lui donne les parametres en requete au cas ou.
            JSPUtils.setBeanProperties(request, viewBean);

            viewBean.setISession(mainDispatcher.getSession());

            viewBean.setIdDroit(InputVb.getIdPrononce());
            viewBean.setNoAVS(InputVb.getNoAVS());

            saveViewBean(viewBean, session);

        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    public void afficherDepuisChoixAnnexesCopies(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp?" + METHOD_UPD;
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /**
     * Redirige vers l'écran "GCT0006" permettant de choisir les tiers ou admin. à mettre en copie lors de la génération
     * des décisions de refus.
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
    public void allerVersChoixAnnexesEtCopies(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        String destination = null;

        // on enregistre les scalbleProperties du document
        CTScalableDocumentFactory factory = CTScalableDocumentFactory.getInstance();

        try {

            ICTScalableDocumentProperties scalableProperties;
            if (((ICTScalableDocument) viewBean).getScalableDocumentProperties() == null) {
                scalableProperties = factory.createNewScalableDocumentProperties();
            } else {
                scalableProperties = ((ICTScalableDocument) viewBean).getScalableDocumentProperties();
            }

            scalableProperties.setParameter("idDroit", ((APGenererDecisionRefusViewBean) viewBean).getIdDroit());
            scalableProperties.setParameter("eMailAddress", request.getParameter("eMailAddress"));
            scalableProperties.setParameter("dateSurDocument", request.getParameter("dateSurDocument"));

            // on cherche l'id du tiers principal
            PRTiersWrapper tiers = PRTiersHelper.getTiers(mainDispatcher.getSession(),
                    ((APGenererDecisionRefusViewBean) viewBean).getNoAVS());
            scalableProperties.setIdTiersPrincipal(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));

            // TODO prendre le bon document
            // scalableProperties.setIdDocument("14000000001");

            // les parametres du premier ecran
            ((ICTScalableDocument) viewBean).setScalableDocumentProperties(scalableProperties);
            ((ICTScalableDocument) viewBean).setEMailAddress(request.getParameter("eMailAddress"));
            ((ICTScalableDocument) viewBean).setNomPrenomTiersPrincipal(tiers.getProperty(PRTiersWrapper.PROPERTY_NOM)
                    + " " + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM));
            ((ICTScalableDocument) viewBean).setNoAVSTiersPrincipal(((APGenererDecisionRefusViewBean) viewBean)
                    .getNoAVS());
            ((ICTScalableDocument) viewBean).setGeneratorImplClassName(GENERER_DECISION_REFUS_PROCESS_CLASSNAME);
            ((ICTScalableDocument) viewBean).setUrlFirstPage(getUserActionURL(request,
                    IAPActions.ACTION_GENERER_DECISION_REFUS, AFFICHER_DEPUIS_CHOIX_ANN_COP_ACTIONPART));
            ((ICTScalableDocument) viewBean).setReturnUrl(getUserActionURL(request, IAPActions.ACTION_DROIT_LAPG,
                    FWAction.ACTION_CHERCHER));

            // pour la navigation
            ((ICTScalableDocument) viewBean).setWantSelectParagraph(false);
            ((ICTScalableDocument) viewBean).setWantEditParagraph(false);
            ((ICTScalableDocument) viewBean).setWantSelectAnnexeCopie(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // getAction().setRight(FWSecureConstants.READ);
        mainDispatcher.dispatch(viewBean, getAction());

        boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

        if (goesToSuccessDest) {
            destination = ((ICTScalableDocument) viewBean).getNextUrl() + "&" + METHOD_UPD;
        } else {
            destination = _getDestAjouterEchec(session, request, response, viewBean);
        }

        session.removeAttribute(FWServlet.VIEWBEAN);
        session.setAttribute(FWServlet.VIEWBEAN, viewBean);

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);

    }
}
