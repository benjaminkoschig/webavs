/*
 * Créé le 6 août 07
 */

package globaz.corvus.servlet;

import globaz.corvus.vb.interetsmoratoires.RECalculInteretMoratoireViewBean;
import globaz.corvus.vb.interetsmoratoires.REInteretMoratoireViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.fweb.taglib.FWSelectorTag;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author BSC
 * 
 */

public class REInteretMoratoireAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_DE_ADD = "_de.jsp?" + METHOD_ADD;
    private static final String VERS_ECRAN_DE_UPD = "_de.jsp?" + METHOD_UPD;
    private static final String VERS_ECRAN_RC = "_rc.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public REInteretMoratoireAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

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

        FWViewBeanInterface viewBean = loadViewBean(session);

        if (isRetourDepuisPyxis(viewBean)) {

            // on revient depuis pyxis on se contente de forwarder car le bon
            // viewBean est deja en session
            ((REInteretMoratoireViewBean) viewBean).setRetourDepuisPyxis(false); // pour
            // la
            // prochaine
            // fois

            if (((REInteretMoratoireViewBean) viewBean).isNew()) {
                forward(getRelativeURL(request, session) + VERS_ECRAN_DE_ADD, request, response);
            } else {
                forward(getRelativeURL(request, session) + VERS_ECRAN_DE_UPD, request, response);
            }
        } else {

            super.actionAfficher(session, request, response, mainDispatcher);
        }
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

        FWViewBeanInterface viewBean = loadViewBean(session);
        REInteretMoratoireViewBean imViewBean;

        if (isRetourDepuisPyxis(viewBean)) {
            // on revient de pyxis, on recupere le viewBean en session pour
            // afficher les donnees dans la page rc.
            imViewBean = (REInteretMoratoireViewBean) viewBean;

        } else {
            /*
             * on affiche cette page par un chemin habituel, dans ce cas tout est normal, mise a part que l'on met un
             * viewBean dans la session qui contient les donnees qui doivent s'afficher dans le cadre rc de la ca page.
             */
            imViewBean = new REInteretMoratoireViewBean();

            try {
                JSPUtils.setBeanProperties(request, imViewBean);
            } catch (Exception e) {
                imViewBean.setMessage(e.getMessage());
                imViewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            imViewBean.setSession((BSession) mainDispatcher.getSession());
            if (!JadeStringUtil.isNull(request.getParameter("noDemandeRente"))) {
                imViewBean.setIdDemandeRente(request.getParameter("noDemandeRente"));
            }
            if (!JadeStringUtil.isNull(request.getParameter("idTierRequerant"))) {
                imViewBean.setIdTiersDemandeRente(request.getParameter("idTierRequerant"));
            }
            if (!JadeStringUtil.isNull(request.getParameter("dateDepotDemande"))) {
                imViewBean.setDateDepotDemande(request.getParameter("dateDepotDemande"));
            }
            if (!JadeStringUtil.isNull(request.getParameter("dateDebutDroit"))) {
                imViewBean.setDateDebutDroit(request.getParameter("dateDebutDroit"));
            }
            if (!JadeStringUtil.isNull(request.getParameter("dateDecision"))) {
                imViewBean.setDateDecision(request.getParameter("dateDecision"));
            }
        }

        // on appelle le helper qui va charger les interets moratoires qui
        // doivent s'afficher
        mainDispatcher.dispatch(imViewBean, getAction());

        /*
         * on sauve de toutes facons le viewBean dans la requete meme s'il est deja en session car c'est la que la page
         * rc va le rechercher.
         */
        saveViewBean(imViewBean, request);
        saveViewBean(imViewBean, session);
        forward(getRelativeURL(request, session) + VERS_ECRAN_RC, request, response);

    }

    /**
     * ecrase une des valeurs sauvee dans la session par FWSelectorTag de telle sorte que l'on sache exactement quelle
     * action sera executee lorsque l'on revient de pyxis et avec quels parametres.
     * 
     * @see FWSelectorTag
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
    protected void actionSelectionner(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        // recuperer les differents parametres pour le retour depuis pyxis
        String idDemandeRente = request.getParameter("idDemandeRente");
        String idTiersDemandeRente = request.getParameter("idTiersDemandeRente");
        String dateDepotDemande = request.getParameter("dateDepotDemande");
        String dateDebutDroit = request.getParameter("dateDebutDroit");
        String dateDecision = request.getParameter("dateDecision");

        StringBuffer queryString = new StringBuffer();
        queryString.append(USER_ACTION);
        queryString.append("=");
        queryString.append(IREActions.ACTION_INTERET_MORATOIRE);
        queryString.append(".");
        queryString.append(FWAction.ACTION_CHERCHER);
        queryString.append("&idDemandeRente=");
        queryString.append(idDemandeRente);
        queryString.append("&idTiersDemandeRente=");
        queryString.append(idTiersDemandeRente);
        queryString.append("&dateDepotDemande=");
        queryString.append(dateDepotDemande);
        queryString.append("&dateDebutDroit=");
        queryString.append(dateDebutDroit);
        queryString.append("&dateDecision=");
        queryString.append(dateDecision);

        // HACK: on remplace une des valeurs sauvee en session par FWSelectorTag
        session.setAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL, queryString.toString());

        // comportement par defaut
        super.actionSelectionner(session, request, response, mainDispatcher);
    }

    /**
     * redefini pour ne pas perdre les parametres qui sont transmis dans la requete.
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
     */
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewBean;
    }

    /**
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @throws Exception
     */
    public String calculerInteretMoratoire(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        String destination = null;

        try {
            RECalculInteretMoratoireViewBean vb = (RECalculInteretMoratoireViewBean) viewBean;
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(vb, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                destination = getRelativeURLwithoutClassPart(request, session) + "/interetMoratoire" + VERS_ECRAN_RC;
            } else {
                destination = getRelativeURLwithoutClassPart(request, session) + "/interetMoratoire" + VERS_ECRAN_RC;
            }
        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        return destination;
    }

    /**
     * inspecte le viewBean et retourne vrais si celui-ci indique que l'on revient de pyxis.
     * 
     * @param viewBean
     * 
     * @return
     */
    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof REInteretMoratoireViewBean) && ((REInteretMoratoireViewBean) viewBean)
                .isRetourDepuisPyxis());
    }

}
