package ch.globaz.pegasus.web.servlet;

import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWController;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.serialization.JavaObjectSerializer;
import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import globaz.pegasus.vb.droit.PCCalculDroitViewBean;
import globaz.pegasus.vb.droit.PCCorrigerDroitViewBean;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import globaz.pegasus.vb.droit.PCDroitViewBean;
import globaz.pegasus.vb.droit.PCSaisieDonneesPersonnellesAjaxViewBean;
import globaz.pegasus.vb.droit.PCSaisieDonneesPersonnellesViewBean;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere;

public class PCDroitServletAction extends PCAbstractServletAction {
    private String idDemande = null;
    private String idDossier = null;
    private static final String DECISION_LIST_URL = "/pegasus?userAction=pegasus.decision.decision.chercher";

    public PCDroitServletAction(FWServlet aServlet) {
        super(aServlet);
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PCDroitViewBean) {
            // return super._getDestAjouterEchec(session, request, response, viewBean);
            session.setAttribute("messageErrorDroit", viewBean.getMessage());
            return getUrlWithOutClass() + "droit.chercher&idDemandePc=" + request.getParameter("idDemandePc");
        } else {
            return super._getDestAjouterEchec(session, request, response, viewBean);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCDroitViewBean) {
            return getActionFullURL() + ".chercher&idDemandePc=" + request.getParameter("idDemandePc");
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCDroitViewBean) {
            return getActionFullURL() + ".chercher&idDemandePc=" + request.getParameter("idDemandePc");
        } else if (viewBean instanceof PCCorrigerDroitViewBean) {
            // si il s'agit du motif décès on redirige directement sur la liste des décisions
            if (IPCDroits.CS_MOTIF_DROIT_DECES.equals(((PCCorrigerDroitViewBean) viewBean).getCsMotif())) {
                PCCorrigerDroitViewBean vb = (PCCorrigerDroitViewBean) viewBean;
                StringBuilder dest = new StringBuilder(DECISION_LIST_URL);
                dest.append("&idDroit=").append(vb.getIdDroit());
                dest.append("&idVersionDroit=").append(vb.getDroit().getSimpleVersionDroit().getIdVersionDroit());
                return dest.toString();
            }

            return getUrlWithOutClass() + "droit.chercher&idDemandePc=" + request.getParameter("idDemandePc");
        } else {
            return super._getDestModifierSucces(session, request, response, viewBean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestModifierSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCDroitViewBean) {
            return getActionFullURL() + ".chercher&idDemandePc=" + request.getParameter("idDemandePc");
        } else {
            return super._getDestSupprimerSucces(session, request, response, viewBean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAjouter(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionAjouter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        if ("droit".equalsIgnoreCase(getAction().getClassPart())) {
            // HACK - Comme on est dans le cas où l'ajout se fait directement
            // sans
            // passer par une page de détail, on doit créer le viewbean et le
            // setter
            // dans la session afin que l'action ajouter se déroule correctement

            session.setAttribute("viewBean",
                    FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix()));
        }
        super.actionAjouter(session, request, response, mainDispatcher);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        if ("droit".equalsIgnoreCase(getAction().getClassPart())) {
            // HACK - il faut chercher l'information nécessaire pour savoir si
            // le droit doit être créé (bouton nouveau = creation droit
            // initial). Le viewbean est créé afin d'appeller le helper et
            // chercher le nombre de version du droit
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix());
            try {

                JSPUtils.setBeanProperties(request, viewBean);

                viewBean = mainDispatcher.dispatch(viewBean, getAction());

            } catch (Exception e) {
                JadeLogger.error("Failed to prepare viewBean for actionChercher", e);
            }

        }

        super.actionChercher(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionLister(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String mes = (String) session.getAttribute("messageErrorDroit");
        request.setAttribute("messageErrorDroit", mes);
        session.removeAttribute("messageErrorDroit");
        super.actionLister(session, request, response, mainDispatcher);
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
        if ("droit".equalsIgnoreCase(getAction().getClassPart())) {
            // HACK - Comme on est dans le cas où la correction se fait
            // directement sans passer par une page de détail, on doit créer le
            // viewbean et le setter dans la session afin que l'action ajouter
            // se déroule correctement.
            session.setAttribute("viewBean",
                    FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix()));
        }
        super.actionModifier(session, request, response, mainDispatcher);
    }

    @Override
    protected void actionModifierAJAX(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWAJAXViewBeanInterface viewBean = null;
        String destination = null;
        try {

            String action = request.getParameter("userAction");
            FWAction newAction = FWAction.newInstance(action);

            // String selectedId = request.getParameter("idEntity");
            // if (JadeStringUtil.isEmpty(selectedId)) {
            // selectedId = request.getParameter("id");
            // }

            String hexaSerializedViewBean = request.getParameter("viewBean");

            viewBean = deserializeViewBean(hexaSerializedViewBean);
            if (FWAJAXFindInterface.class.isAssignableFrom(viewBean.getClass())) {
                ((FWAJAXFindInterface) viewBean).initList();
            }/*
              * 
              * viewBean.
              * 
              * /* set des properietes
              */
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // _action.changeActionPart(FWAction.ACTION_MODIFIER_AJAX);
            viewBean.setGetListe(true);
            /*
             * beforeUpdate, call du dispatcher puis mis en session
             */
            viewBean = (FWAJAXViewBeanInterface) beforeModifier(session, request, response, viewBean);
            viewBean = (FWAJAXViewBeanInterface) mainDispatcher.dispatch(viewBean, newAction);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            request.setAttribute("idEntity", ((BIPersistentObject) viewBean).getId());

            if (viewBean.hasList()) {
                destination = getAJAXListerSuccessDestination(session, request, viewBean);
            } else {
                destination = getAJAXAfficherSuccessDestination(session, request);
            }

            redirectAJAXModificationDestination(session, request, response, mainDispatcher, viewBean);

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_AJAX_PAGE;
            request.setAttribute("exception", e);
        } finally {
            executeFindForAajaxList(request, viewBean);
        }

        /*
         * redirection vers la destination
         */
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionSupprimer(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionSupprimer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        if ("droit".equalsIgnoreCase(getAction().getClassPart())) {
            // HACK - Comme on est dans le cas où la suppression se fait
            // directement sans passer par une page de détail, on doit créer le
            // viewbean et le setter dans la session afin que l'action ajouter
            // se déroule correctement
            session.setAttribute("viewBean",
                    FWViewBeanActionFactory.newInstance(getAction(), mainDispatcher.getPrefix()));
        }
        super.actionSupprimer(session, request, response, mainDispatcher);
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

        if (viewBean instanceof PCAbstractRequerantDonneeFinanciereViewBean) {
            PCAbstractRequerantDonneeFinanciereViewBean ardfViewBean = (PCAbstractRequerantDonneeFinanciereViewBean) viewBean;
            ardfViewBean.setNoVersion(JadeStringUtil.toNotNullString(request.getParameter("noVersion")));
            ardfViewBean.setIdVersion(JadeStringUtil.toNotNullString(request.getParameter("idVersionDroit")));
            ardfViewBean.setIdDossier(JadeStringUtil.toNotNullString(request.getParameter("idDossier")));

        }

        if (viewBean instanceof PCCalculDroitViewBean) {
            PCCalculDroitViewBean calculViewBean = (PCCalculDroitViewBean) viewBean;
            // calculViewBean.setNoVersion(JadeStringUtil.toNotNullString(request.getParameter("noVersion")));
            calculViewBean.setIdVersionDroit(JadeStringUtil.toNotNullString(request.getParameter("idVersionDroit")));
            calculViewBean.setIdDemande(JadeStringUtil.toNotNullString(request.getParameter("idDemandePc")));
        }

        if (viewBean instanceof PCDroitViewBean) {
            PCDroitViewBean droitViewBean = (PCDroitViewBean) viewBean;
            // calculViewBean.setNoVersion(JadeStringUtil.toNotNullString(request.getParameter("noVersion")));
            droitViewBean.setIdVersionDroit(JadeStringUtil.toNotNullString(request.getParameter("idVersionDroit")));
            droitViewBean.setIdDemande(JadeStringUtil.toNotNullString(request.getParameter("idDemandePc")));
        }

        if (viewBean instanceof PCSaisieDonneesPersonnellesViewBean) {
            PCSaisieDonneesPersonnellesViewBean sdpViewBean = (PCSaisieDonneesPersonnellesViewBean) viewBean;
            sdpViewBean.setIdDossier(JadeStringUtil.toNotNullString(request.getParameter("idDossier")));
        }

        return super.beforeAfficher(session, request, response, viewBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAjouter(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (viewBean instanceof PCDroitViewBean) {
            // recupère demande de id
            ((PCDroitViewBean) viewBean).setIdDemande(request.getParameter("idDemande"));
        } else {
            tryGetDroit(session, request, viewBean);
        }

        return super.beforeAjouter(session, request, response, viewBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeModifier(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCDroitViewBean) {

            ((PCDroitViewBean) viewBean).setIdVersionDroit(request.getParameter("idVersionDroit"));
        } else {
            tryGetDroit(session, request, viewBean);
        }
        return super.beforeSupprimer(session, request, response, viewBean);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeSupprimer(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCDroitViewBean) {

            ((PCDroitViewBean) viewBean).setIdVersionDroit(request.getParameter("idVersionDroit"));
        } else {
            tryGetDroit(session, request, viewBean);
        }
        return super.beforeSupprimer(session, request, response, viewBean);
    }

    public String calculer(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws Exception {

        // IPCDroits.CS_DROIT_TYPE_DE_CALCUL_STANDARD;
        // IPCDroits.CS_DROIT_TYPE_DE_CALCUL_SANS_RETRO
        String typeCalcule = request.getParameter("typeDeCalcule");

        FWViewBeanInterface oldViewBean = viewBean;

        String appPart = getAction().getApplicationPart();

        viewBean = FWViewBeanActionFactory.newInstance(getAction(), dispatcher.getPrefix());
        JSPUtils.setBeanProperties(request, viewBean);

        viewBean = dispatcher.dispatch(viewBean, getAction());
        // Les business exceptions, dans le VP
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            JadeThread.rollbackSession();

            JadeBusinessMessage calculErrorMsg = JadeThread.logMessages()[0];
            StringBuilder destination = new StringBuilder().append("/").append(appPart).append("?userAction=")
                    .append(appPart).append(".droit.droit.chercher&calculErrorMsg=")
                    .append(calculErrorMsg.getMessageId());

            if (calculErrorMsg.getParameters() != null) {
                destination.append("&CalculErrorParams=").append(StringUtils.join(calculErrorMsg.getParameters(), ","));
            }
            return addParametersFrom(request, destination.toString());

        } else if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            String destination = FWDefaultServletAction.ERROR_PAGE;
            PCCalculDroitViewBean vb = (PCCalculDroitViewBean) viewBean;
            if (vb.getCause() instanceof CalculBusinessException) {
                viewBean = oldViewBean;
            }
            session.setAttribute("viewBean", viewBean);
            return destination;
        } else if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {

            JadeBusinessMessage calculWarningMsg = JadeThread.logMessages()[0];
            // StringBuilder destination = new StringBuilder().append("/").append(appPart).append("?userAction=")
            // .append(appPart).append(".droit.droit.chercher&calculWarningMsg=")
            // .append(calculWarningMsg.getMessageId());

            StringBuilder destination = new StringBuilder().append("/").append(appPart).append("?userAction=")
                    .append(appPart).append(".pcaccordee.pcAccordee.chercher&idDroit=")
                    .append(request.getParameter("idDroit")).append("&noVersion=")
                    .append(request.getParameter("noVersion")).append("&isReadyForDac=1").append("&idVersionDroit=")
                    .append(request.getParameter("idVersionDroit")).append("&calculWarningMsg=")
                    .append(calculWarningMsg.getMessageId());

            if (calculWarningMsg.getParameters() != null) {
                destination.append("&CalculWarnParams=")
                        .append(StringUtils.join(calculWarningMsg.getParameters(), ","));
            }
            return addParametersFrom(request, destination.toString());
        }

        return "/" + appPart + "?userAction=" + appPart + ".pcaccordee.pcAccordee.chercher&idDroit="
                + request.getParameter("idDroit") + "&noVersion=" + request.getParameter("noVersion")
                + "&isReadyForDac=1" + "&idVersionDroit=" + request.getParameter("idVersionDroit");
    }

    private FWAJAXViewBeanInterface deserializeViewBean(String hexaSerializedViewBean) throws ClassNotFoundException {
        FWAJAXViewBeanInterface viewBean;
        byte[] serializedViewBean;
        try {
            serializedViewBean = Hex.decodeHex(hexaSerializedViewBean.toCharArray());
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(serializedViewBean));
            viewBean = (FWAJAXViewBeanInterface) in.readObject();
            in.close();
            return viewBean;
        } catch (DecoderException e) {
            throw new ClassNotFoundException("Could not retrieve serialized viewBean : " + e.getMessage());
        } catch (IOException e) {
            throw new ClassNotFoundException("Could not retrieve serialized viewBean : " + e.getMessage());
        }
    }

    @Override
    public void doAction(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWController mainController) throws ServletException, IOException {
        idDemande = request.getParameter("idDemande");
        idDossier = request.getParameter("idDossier");
        super.doAction(session, request, response, mainController);
    }

    private ModificateurDroitDonneeFinanciere getObjectDroit(String SerializedBean) {
        try {
            ModificateurDroitDonneeFinanciere droit = null;

            droit = (ModificateurDroitDonneeFinanciere) JavaObjectSerializer.convertHexaToObject(SerializedBean);

            return droit;

        } catch (Exception e) {
            JadeLogger.warn("no serialized object Droit was found, or it was corrupted", e);
        }
        return null;
    }

    private String getUrlWithOutClass() {
        return "/" + getAction().getApplicationPart() + "?userAction=" + getAction().getApplicationPart() + "."
                + getAction().getPackagePart() + ".";
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.framework.controller.FWDefaultServletAction# goSendRedirectWithoutParameters(java.lang.String,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void goSendRedirect(String url, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!JadeStringUtil.isEmpty(idDemande) && (url.indexOf("?") > 0)) {
            url = url + "&idDemande=" + idDemande;
        }
        super.goSendRedirect(url, request, response);
    }

    public String modifierDateAnnonce(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws InvocationTargetException,
            IllegalAccessException {
        String appPart = getAction().getApplicationPart();
        return defaultActionCustom(session, request, response, dispatcher, viewBean, "/" + appPart + "?userAction="
                + appPart + ".droit.droit.chercher&idDemandePc=" + request.getParameter("idDemandePc"));
    }

    public String synchroniser(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws InvocationTargetException,
            IllegalAccessException {

        String appPart = getAction().getApplicationPart();

        return defaultActionCustom(session, request, response, dispatcher, viewBean, "/" + appPart + "?userAction="
                + appPart + ".demande.demande.chercher&idDossier=" + idDossier);
    }

    /**
     * Methode pour reconstruire l'entité Droit du parametre de requete parentViewBean. Elle ne s'execute que lors d'une
     * action ajax liée aux données financières.
     * 
     * @param session
     *            Session HTTP en cours
     * @param request
     *            requete HTTP
     * @param viewBean
     *            viewBean de l'action en cours
     */
    private void tryGetDroit(HttpSession session, HttpServletRequest request, FWViewBeanInterface viewBean) {
        if (viewBean instanceof PCDonneeFinanciereAjaxViewBean) {
            PCDonneeFinanciereAjaxViewBean vb = (PCDonneeFinanciereAjaxViewBean) viewBean;

            vb.setDroit(getObjectDroit(request.getParameter("parentViewBean")));

            vb.setIdDroitMembreFamille(request.getParameter("idDroitMembreFamille"));
        } else if (viewBean instanceof PCSaisieDonneesPersonnellesAjaxViewBean) {
            PCSaisieDonneesPersonnellesAjaxViewBean vb = (PCSaisieDonneesPersonnellesAjaxViewBean) viewBean;

            vb.setDroit(getObjectDroit(request.getParameter("parentViewBean")));

        }
    }

}