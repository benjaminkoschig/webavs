package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.ij.vb.acor.IJCalculACORIJViewBean;
import globaz.ij.vb.prononces.IJSituationProfessionnelleViewBean;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJCalculACORIJAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String ATTACHEMENT_ACOR_BAT = "attachment; attachment; filename=\"globaz.ij.acor.bat\"";
    private static final String DEUX_MINUTES_EN_CACHE = "private, max-age=120";
    private static final String ENTETE_CONTENU = "Content-Disposition";
    private static final String ENTETE_CONTROLE_CACHE = "Cache-Control";
    private static final String TYPE_MIME_BAT = "application/x-bat";

    private static final String VERS_ECRAN_DE = "_de.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJCalculACORIJAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJCalculACORIJAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * instancie un nouveau viewBean pour le calcul des ij et delegue au helper le chargement du prononce pour
     * l'affichage dans l'ecran de.
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
        IJCalculACORIJViewBean viewBean = new IJCalculACORIJViewBean();

        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainDispatcher.dispatch(viewBean, getAction());
        saveViewBean(viewBean, session);

        forward(getRelativeURL(request, session) + VERS_ECRAN_DE, request, response);
    }

    /**
     * Prepare la servlet a retourner un fichier de script et delegue au helper l'ecriture en question.
     * 
     * <p>
     * Note: comme on ecrit le script directement dans la reponse, il n'est pas possible de faire une redirection
     * ensuite et il est donc impossible d'avertir l'utilisateur des erreurs.
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
     * @param viewBean
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String actionExporterScriptACOR(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        // preparer la servlet a retourner un fichier de script
        response.setContentType(TYPE_MIME_BAT);
        response.setHeader(ENTETE_CONTENU, ATTACHEMENT_ACOR_BAT);

        // force la mise en cache pour permettre l'execution du script sur le
        // client
        response.setHeader(ENTETE_CONTROLE_CACHE, DEUX_MINUTES_EN_CACHE);

        // ecrire le script dans la reponse http
        PrintWriter writer = response.getWriter();

        try {
            ((IJCalculACORIJViewBean) viewBean).setWriter(writer);

            // l'ecriture se fait depuis le helper
            mainDispatcher.dispatch(viewBean, getAction());
        } finally {
            writer.close(); // terminer la connexion http
            ((IJCalculACORIJViewBean) viewBean).setWriter(null); // finaliser le
            // flux
        }

        // terminer le traitement par la servlet
        return "";
    }

    public String actionExporterScriptACOR2(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        try {

            // l'ecriture se fait depuis le helper
            mainDispatcher.dispatch(viewBean, getAction());
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        } finally {
            saveViewBean(viewBean, session);
        }

        // terminer le traitement par la servlet
        FWAction action = FWAction.newInstance(IIJActions.ACTION_CALCUL_IJ + "." + FWAction.ACTION_REAFFICHER);
        return getUserActionURL(request, action.toString());
    }

    /**
     * importe le contenu des fichiers retournes par ACOR dans le cadre du dernier calul.
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
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String actionImporterIJ(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        IJCalculACORIJViewBean caViewBean = (IJCalculACORIJViewBean) viewBean;

        mainDispatcher.dispatch(viewBean, getAction());

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            return getRelativeURL(request, session) + VERS_ECRAN_DE;
        } else {
            return getUserActionURL(request, IIJActions.ACTION_IJ_CALCULEES,
                    "chercher&idPrononce=" + caViewBean.getIdPrononce() + "&csTypeIJ=" + caViewBean.getCsTypeIJ());
        }
    }

    /**
     * Création de l'IJ calculee en fonction du prononce AIT
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
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public String calculerAit(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        IJSituationProfessionnelleViewBean spViewBean = null;

        if (viewBean != null && viewBean instanceof IJSituationProfessionnelleViewBean) {
            spViewBean = (IJSituationProfessionnelleViewBean) viewBean;
        } else {
            spViewBean = new IJSituationProfessionnelleViewBean();
            spViewBean.setSession((BSession) mainDispatcher.getSession());
            spViewBean.setIdPrononce(request.getParameter("idPrononce"));
            spViewBean.setCsTypeIJ(request.getParameter("csTypeIJ"));
        }

        mainDispatcher.dispatch(spViewBean, getAction());

        saveViewBean(spViewBean, request);

        if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR)) {
            return getRelativeURL(request, session) + VERS_ECRAN_DE;
        } else {
            return getUserActionURL(request, IIJActions.ACTION_IJ_CALCULEES,
                    "chercher&idPrononce=" + spViewBean.getIdPrononce() + "&csTypeIJ=" + spViewBean.getCsTypeIJ());
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

        FWAction action = FWAction.newInstance(IIJActions.ACTION_CALCUL_IJ + "." + FWAction.ACTION_REAFFICHER);
        return this.getUserActionURL(request, action.toString());

    }

}
