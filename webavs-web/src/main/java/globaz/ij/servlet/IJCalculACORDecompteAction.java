package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import globaz.ij.vb.acor.IJCalculACORDecompteViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * les actions specifiques a la page d'import export globaz.ij.acor pour le calcul des decomptes.
 * </p>
 * 
 * @author vre
 * @see globaz.ij.helpers.acor.IJCalculACORDecompteHelper
 */
public class IJCalculACORDecompteAction extends PRDefaultAction {

    private static final String ATTACHEMENT_ACOR_BAT = "attachment; attachment; filename=\"globaz.ij.acor.bat\"";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String DEUX_MINUTES_EN_CACHE = "private, max-age=120";
    private static final String ENTETE_CONTENU = "Content-Disposition";
    private static final String ENTETE_CONTROLE_CACHE = "Cache-Control";
    private static final Class[] PARAMS = new Class[] { HttpSession.class, HttpServletRequest.class,
            HttpServletResponse.class, FWDispatcher.class, FWViewBeanInterface.class };
    private static final String TYPE_MIME_BAT = "application/x-bat";

    private static final String VERS_ECRAN_DE = "_de.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJCalculACORDecompteAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJCalculACORDecompteAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String actionCalculerPrestation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws Exception {
        IJCalculACORDecompteViewBean caViewBean = (IJCalculACORDecompteViewBean) viewBean;

        dispatcher.dispatch(viewBean, getAction());

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            return getRelativeURL(request, session) + VERS_ECRAN_DE;
        }
        // Comprtement standard
        else {
            if (caViewBean.hasIJCalculeeSuivante()) {
                // rediriger vers l'écran de calcul pour l'ij suivante s'il y en
                // a encore a calculer
                caViewBean.ijCalculeeSuivante();

                return getRelativeURL(request, session) + VERS_ECRAN_DE + "?" + DESACTIVE_VALIDATION;
            } else {
                // sinon rediriger vers l'ecran des prestations
                return getUserActionURL(request, IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE,
                        FWAction.ACTION_CHERCHER + "&forIdPrononce=" + caViewBean.loadPrononce().getIdPrononce()
                                + "&forNoBaseIndemnisation=" + caViewBean.getIdBaseIndemnisation());
            }
        }
    }

    /**
     * Idem PRDefaultAction mais avec un goSendRedirect a la place d'un forward pour l'action "actionImporterDecompte"
     * pour eviter une erreure dans IE "la page ne peut pas être actualisée sans le renvoi d'information"
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String destination = ERROR_PAGE;
        FWViewBeanInterface viewBean;

        try {
            viewBean = loadViewBean(session);

            if (viewBean != null) {
                JSPUtils.setBeanProperties(request, viewBean);
            }
        } catch (Exception e) {
            // impossible de trouver le viewBean dans la session. arreter le
            // processus et rediriger vers les erreurs
            goSendRedirect(destination, request, response);

            return;
        }

        // selectionner l'action en fonction du parametre transmis
        try {
            Method methode = getClass().getMethod(getAction().getActionPart(), PARAMS);

            destination = (String) methode.invoke(this,
                    new Object[] { session, request, response, dispatcher, viewBean });
        } catch (Exception e) {
            // impossible de trouver une methode avec ce nom et ces parametres
            // !!!
            e.printStackTrace();
        }

        // desactive le forward pour le cas ou la reponse a deja ete flushee
        if (!JadeStringUtil.isBlank(destination)) {
            forward(destination, request, response);
        }
    }

    /**
     * exporte le script d'execution de ACOR.
     * 
     * <p>
     * note: comme on ecrit le fichier directement dans la reponse http, il n'est pas possible de catcher les exceptions
     * et de rediriger vers une page d'erreur.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param dispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String actionExporterScriptACOR(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws Exception {
        // preparer la servlet a retourner un fichier de script
        response.setContentType(TYPE_MIME_BAT);
        response.setHeader(ENTETE_CONTENU, ATTACHEMENT_ACOR_BAT);

        // force la mise en cache pour permettre l'execution du script sur le
        // client
        response.setHeader(ENTETE_CONTROLE_CACHE, DEUX_MINUTES_EN_CACHE);

        // ecrire le script dans la reponse http
        PrintWriter writer = response.getWriter();

        try {
            ((IJCalculACORDecompteViewBean) viewBean).setWriter(writer);

            // l'ecriture se fait depuis le helper
            dispatcher.dispatch(viewBean, getAction());
        } finally {
            writer.close(); // terminer la connexion http
            ((IJCalculACORDecompteViewBean) viewBean).setWriter(null); // finaliser
            // le
            // flux
        }

        // terminer le traitement par la servlet
        return "";
    }

    public String actionExporterScriptACOR2(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws Exception {

        try {

            // l'ecriture se fait depuis le helper
            dispatcher.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        } finally {
            saveViewBean(viewBean, session);
        }

        FWAction action = FWAction.newInstance(IIJActions.ACTION_CALCUL_DECOMPTE + "." + FWAction.ACTION_REAFFICHER);
        return getUserActionURL(request, action.toString());
    }

    /**
     * affiche l'ecran d'import/export ACOR pour l'ij precedante de la base d'indemnisation choisie.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param dispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String actionIJCalculeePrecedante(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws Exception {
        ((IJCalculACORDecompteViewBean) viewBean).ijCalculeePrecedante();

        return getRelativeURL(request, session) + VERS_ECRAN_DE + "?" + DESACTIVE_VALIDATION;
    }

    /**
     * affiche l'ecran d'import/export ACOR pour l'ij suivante de la base d'indemnisation choisie.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param dispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String actionIJCalculeeSuivante(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws Exception {
        ((IJCalculACORDecompteViewBean) viewBean).ijCalculeeSuivante();

        return getRelativeURL(request, session) + VERS_ECRAN_DE + "?" + DESACTIVE_VALIDATION;
    }

    /**
     * delegue l'importation des donnees en retour d'globaz.ij.acor au helper et redirige vers l'ecran approprie.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param dispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String actionImporterDecompte(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher, FWViewBeanInterface viewBean) throws Exception {
        IJCalculACORDecompteViewBean caViewBean = (IJCalculACORDecompteViewBean) viewBean;

        dispatcher.dispatch(viewBean, getAction());

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            return getRelativeURL(request, session) + VERS_ECRAN_DE;
        }
        // Comprtement standard
        else {
            if (caViewBean.hasIJCalculeeSuivante()) {
                // rediriger vers l'écran de calcul pour l'ij suivante s'il y en
                // a encore a calculer
                caViewBean.ijCalculeeSuivante();

                return getRelativeURL(request, session) + VERS_ECRAN_DE + "?" + DESACTIVE_VALIDATION;
            } else {
                // sinon rediriger vers l'ecran des prestations
                return getUserActionURL(request, IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE,
                        FWAction.ACTION_CHERCHER + "&forIdPrononce=" + caViewBean.loadPrononce().getIdPrononce()
                                + "&forNoBaseIndemnisation=" + caViewBean.getIdBaseIndemnisation());
            }
        }
    }

    public String actionCallACORWeb(HttpSession session, HttpServletRequest request,
                                    HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {

        try {
            // l'ecriture se fait depuis le helper
            mainDispatcher.dispatch(viewBean, getAction());

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }
        this.saveViewBean(viewBean, session);

        FWAction action = FWAction.newInstance(IIJActions.ACTION_CALCUL_DECOMPTE + "." + FWAction.ACTION_REAFFICHER);
        return this.getUserActionURL(request, action.toString());

    }

}
