package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.fweb.taglib.FWSelectorTag;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.ij.vb.prestations.IJRepartitionJointPrestationViewBean;
import globaz.ij.vb.prononces.IJNSSDTO;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Une serie de hacks permettent de simplifier le processus de la selection dans pyxis de la facon suivante:
 * </p>
 * 
 * <ol>
 * <li>on force l'action chercher comme action qui sera executee lors du retour depuis pyxis en ecrasant une des valeurs
 * mises en session par le tag de selection (voir
 * {@link #actionSelectionner(HttpSession, HttpServletRequest, HttpServletResponse, FWDispatcher) actionSelectionner})</li>
 * <li>l'utilisation dans la methode actionChoisir() des methodes de transfert des donnees entre les deux viewbean
 * (celui de pyxis et le notre) entrainent la mise d'un flag a true dans le viewbean (voir
 * {@link globaz.ij.vb.prestation.IJRepartitionJointPrestationViewBean#setIdAdressePaiementDepuisPyxis(String)
 * setIdAdressePaiementDepuisPyxis}).</li>
 * <li>ce flag est detecte dans
 * {@link #actionChercher(HttpSession, HttpServletRequest, HttpServletResponse, FWDispatcher) la methode chercher}. S'il
 * est a vrai, la methode recupere le viewBean dans la session.</li>
 * <li>ce flag est egalement detecte ensuite dans la methode
 * {@link #actionAfficher(HttpSession, HttpServletRequest, HttpServletResponse, FWDispatcher) actionAfficher} ou il sera
 * remis a false. La methode, (qui est appellee lors de l'affichage du cadre de detail de la ca page, je le rappelle),
 * se contente alors de rediriger vers la jsp sans rien faire, de cette facon, la repartition qui vient d'etre modifie
 * est de nouveau affichee.</li>
 * </ol>
 * 
 * @author vre
 */
public class IJRepartitionJointPrestationAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static FWAction ACTION_PRE_CHERCHER;
    private static final String VERS_ECRAN_DE_ADD = "_de.jsp?" + METHOD_ADD;
    private static final String VERS_ECRAN_DE_NEW = "_de.jsp?" + VALID_NEW;
    private static final String VERS_ECRAN_DE_UPD = "_de.jsp?" + METHOD_UPD;

    private static final String VERS_ECRAN_RC = "_rc.jsp";

    static {
        ACTION_PRE_CHERCHER = FWAction.newInstance(IIJActions.ACTION_REPARTITION_PAIEMENTS + ".actionPreparerChercher");
        ACTION_PRE_CHERCHER.setRight(FWSecureConstants.ADD);
    }

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJRepartitionJointPrestationAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJRepartitionJointPrestationAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * on donne un parametre special dans l'url de la page de qui fait que celle-ci force le rechargement de la liste.
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
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        saveNewViewBean(session, (IJRepartitionJointPrestationViewBean) viewBean);

        return super._getDestAjouterSucces(session, request, response, viewBean);
    }

    /**
     * on donne un parametre special dans l'url de la page de qui fait que celle-ci force le rechargement de la liste.
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
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        saveNewViewBean(session, (IJRepartitionJointPrestationViewBean) viewBean);

        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    /**
     * on donne un parametre special dans l'url de la page de qui fait que celle-ci force le rechargement de la liste.
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
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        saveNewViewBean(session, (IJRepartitionJointPrestationViewBean) viewBean);

        return super._getDestSupprimerSucces(session, request, response, viewBean);
    }

    /**
     * redefinition pour intercepter les retours depuis pyxis et faire en sorte que le cadre de detail s'affiche avec
     * les donnes de la repartition dont l'adresse de paiement vient d'etre modifiee et pas une ecran vide.
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

        if (isRetourDepuisPyxis(viewBean)
                || ((IJRepartitionJointPrestationViewBean) viewBean).isActionRechercherAffilie()) {

            ((IJRepartitionJointPrestationViewBean) viewBean).setActionRechercherAffilie(false);

            // Au retour de pyxis, on contrôle si l'on était en mode création
            // d'un nouveau bénéficiaire.
            // Ce mode est setté dans l'attribut :
            // FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL
            // Lors de l'appel de l'action sélectionné, cad avant d'appeler
            // pyxis.
            String customerUrl = (String) session.getAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL);
            StringTokenizer token = new StringTokenizer(customerUrl, "&");
            while (token.hasMoreElements()) {
                String elem = token.nextToken();
                if (elem.indexOf("isCreationNouvelleRepartition") != -1) {
                    if (elem.indexOf("true") != -1) {
                        ((IJRepartitionJointPrestationViewBean) viewBean).setIsCreationNouvelleRepartition(true);
                    } else {
                        ((IJRepartitionJointPrestationViewBean) viewBean).setIsCreationNouvelleRepartition(false);
                    }
                }
            }

            // on revient depuis pyxis on se contente de forwarder car le bon
            // viewBean est deja en session
            ((IJRepartitionJointPrestationViewBean) viewBean).setRetourDepuisPyxis(false); // pour
            // la
            // prochaine
            // fois

            if (((IJRepartitionJointPrestationViewBean) viewBean).isNew()) {
                forward(getRelativeURL(request, session) + VERS_ECRAN_DE_ADD, request, response);
            } else {
                forward(getRelativeURL(request, session) + VERS_ECRAN_DE_UPD, request, response);
            }
        } else {

            String method = request.getParameter("_method");
            // L'on veut créer un nouveau bénéficiaire de paiement.
            if ((method != null) && (method.equalsIgnoreCase("ADD"))) {
                IJRepartitionJointPrestationViewBean vb = new IJRepartitionJointPrestationViewBean();
                vb.setIsCreationNouvelleRepartition(true);
                vb.setSession((BSession) mainDispatcher.getSession());

                String idPrestation = request.getParameter("idPrestation");
                // On a cliquer sur le bouton nouveau dans l'ecran RC des
                // répartition des paiements
                if (!JadeStringUtil.isIntegerEmpty(idPrestation)) {
                    vb.setIdPrestation(idPrestation);
                }
                // On a cliquer sur le menu d'option des prestations :
                // Repartition des paiements
                else if (viewBean instanceof IJRepartitionJointPrestationViewBean) {
                    vb.setIdPrestation(((IJRepartitionJointPrestationViewBean) viewBean).getIdPrestation());
                }

                saveViewBean(vb, session);

                String destination = getRelativeURL(request, session) + "_de.jsp";
                servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
            } else {
                // on ne revient pas depuis pyxis, comportement par defaut
                super.actionAfficher(session, request, response, mainDispatcher);
            }
        }
    }

    /**
     * redefinition pour charger les informations qui doivent s'afficher dans l'ecran rc.
     * 
     * <p>
     * cette methode inspecte la session pour savoir si l'on revient depuis pyxis. Si c'est le cas, l'ancien viewBean
     * est conserve dans la session et il sera reutilise pour l'action afficher.
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
        IJRepartitionJointPrestationViewBean rpViewBean;

        if (isRetourDepuisPyxis(viewBean)) {
            // on revient de pyxis, on recupere le viewBean en session pour
            // afficher les donnees dans la page rc.
            rpViewBean = (IJRepartitionJointPrestationViewBean) viewBean;

        } else {
            /*
             * on affiche cette page par un chemin habituel, dans ce cas tout est normal, mise a part que l'on met un
             * viewBean dans la session qui contient les donnees qui doivent s'afficher dans le cadre rc de la ca page.
             */
            if (null == viewBean) {
                rpViewBean = new IJRepartitionJointPrestationViewBean();

                try {
                    JSPUtils.setBeanProperties(request, rpViewBean);
                } catch (Exception e) {
                    rpViewBean.setMessage(e.getMessage());
                    rpViewBean.setMsgType(FWViewBeanInterface.ERROR);
                }

            } else if (viewBean instanceof IJRepartitionJointPrestationViewBean) {
                rpViewBean = (IJRepartitionJointPrestationViewBean) viewBean;
            } else {
                rpViewBean = new IJRepartitionJointPrestationViewBean();

                try {
                    JSPUtils.setBeanProperties(request, rpViewBean);
                } catch (Exception e) {
                    rpViewBean.setMessage(e.getMessage());
                    rpViewBean.setMsgType(FWViewBeanInterface.ERROR);
                }
            }

        }

        // on appelle le helper qui va charger les infos sur le droit et la
        // prestation qui doivent s'afficher
        mainDispatcher.dispatch(rpViewBean, getAction());

        /*
         * on sauve de toutes facons le viewBean dans la requete meme s'il est deja en session car c'est la que la page
         * rc va le rechercher.
         */
        saveViewBean(rpViewBean, request);
        saveViewBean(rpViewBean, session);
        forward(getRelativeURL(request, session) + VERS_ECRAN_RC, request, response);

        IJNSSDTO dto = new IJNSSDTO();
        dto.setNSS(rpViewBean.getNoAVSAssure());
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
    }

    /**
     * remplace le viewBean de repartition dans la session par une nouvelle ventilation pour cette repartition et
     * redirige sur la page de detail.
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
    public String actionNouvelleVentilation(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        IJRepartitionJointPrestationViewBean oldViewBean = (IJRepartitionJointPrestationViewBean) viewBean;
        IJRepartitionJointPrestationViewBean newViewBean = new IJRepartitionJointPrestationViewBean();

        newViewBean.setIdPrononce(oldViewBean.getIdPrononce());
        newViewBean.setCsTypeIJ(oldViewBean.getCsTypeIJ());
        newViewBean.setIdParent(oldViewBean.getIdRepartitionPaiement());
        newViewBean.setIdBaseIndemnisation(oldViewBean.getIdBaseIndemnisation());
        newViewBean.setIdPrestation(oldViewBean.getIdPrestation());
        newViewBean.setMontantNet(oldViewBean.getMontantNet());
        newViewBean.setSession((BSession) mainDispatcher.getSession());

        saveViewBean(newViewBean, session);

        return getRelativeURL(request, session) + VERS_ECRAN_DE_ADD;
    }

    /**
     * passe a la prestation precedante pour ce droit s'il en existe une.
     * 
     * <p>
     * delegue au helper le rechargement des infos sur le droit et des informations sur la nouvelle prestation.
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
    public String actionPrestationPrecedante(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        IJRepartitionJointPrestationViewBean rpViewBean = new IJRepartitionJointPrestationViewBean();

        try {
            JSPUtils.setBeanProperties(request, rpViewBean);
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        rpViewBean.prestationPrecedante();
        mainDispatcher.dispatch(rpViewBean, ACTION_PRE_CHERCHER);
        saveViewBean(rpViewBean, request);

        return getRelativeURL(request, session) + VERS_ECRAN_RC;
    }

    /**
     * passe a la prestation suivante pour ce droit s'il en existe une.
     * 
     * <p>
     * delegue au helper le rechargement des infos sur le droit et des informations sur la nouvelle prestation.
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
    public String actionPrestationSuivante(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        IJRepartitionJointPrestationViewBean rpViewBean = new IJRepartitionJointPrestationViewBean();

        try {
            JSPUtils.setBeanProperties(request, rpViewBean);
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        rpViewBean.prestationSuivante();
        mainDispatcher.dispatch(rpViewBean, ACTION_PRE_CHERCHER);
        saveViewBean(rpViewBean, request);

        return getRelativeURL(request, session) + VERS_ECRAN_RC;
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
        // recuperer les id et genre de service du droit pour le retour depuis
        // pyxis
        IJRepartitionJointPrestationViewBean rpViewBean = (IJRepartitionJointPrestationViewBean) loadViewBean(session);
        StringBuffer queryString = new StringBuffer();

        queryString.append(USER_ACTION);
        queryString.append("=");
        queryString.append(IIJActions.ACTION_REPARTITION_PAIEMENTS);
        queryString.append(".");
        queryString.append(FWAction.ACTION_CHERCHER);
        queryString.append("&idPrestation=");
        queryString.append(rpViewBean.getIdPrestation());

        if (rpViewBean.isCreationNouvelleRepartition()) {
            queryString.append("&isCreationNouvelleRepartition='true'");
        }

        // HACK: on remplace une des valeurs sauvee en session par FWSelectorTag
        session.setAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL, queryString.toString());

        // comportement par defaut
        super.actionSelectionner(session, request, response, mainDispatcher);
    }

    /**
     * redefini pour ne pas perdre les idPrononce et csTypeIJ qui sont transmis dans la requete.
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
     * inspecte le viewBean et retourne vrai si celui-ci indique que l'on revient de pyxis.
     * 
     * @param viewBean
     * 
     * @return
     */
    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof IJRepartitionJointPrestationViewBean) && ((IJRepartitionJointPrestationViewBean) viewBean)
                .isRetourDepuisPyxis());
    }

    /**
     * DOCUMENT ME ! --> hpe
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
    public void rechercherAffilie(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        IJRepartitionJointPrestationViewBean spViewBean = null;
        try {
            JSPUtils.setBeanProperties(request, viewBean);

            spViewBean = (IJRepartitionJointPrestationViewBean) viewBean;

            // on appelle le helper qui va charger les infos concernante les
            // employeurs

            getAction().setRight(FWSecureConstants.READ);
            spViewBean = (IJRepartitionJointPrestationViewBean) mainDispatcher.dispatch(spViewBean, getAction());
            // setDTO(session, spViewBean);
            spViewBean.setActionRechercherAffilie(true);
            /*
             * on sauve de toutes facons le viewBean dans la requete meme s'il est deja en session car c'est la que la
             * page rc va le rechercher.
             */
        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        } finally {
            saveViewBean(spViewBean, session);
            saveViewBean(spViewBean, request);

            forward(getRelativeURL(request, session) + VERS_ECRAN_RC, request, response);
        }
    }

    /**
     * sauve un nouveau viewBean dans la session.
     * 
     * @param session
     * @param oldViewBean
     */
    private void saveNewViewBean(HttpSession session, IJRepartitionJointPrestationViewBean oldViewBean) {
        IJRepartitionJointPrestationViewBean newViewBean = new IJRepartitionJointPrestationViewBean();

        newViewBean.setIdPrononce(oldViewBean.getIdPrononce());
        newViewBean.setCsTypeIJ(oldViewBean.getCsTypeIJ());
        newViewBean.setIdPrestation(oldViewBean.getIdPrestation());
        newViewBean.setIdBaseIndemnisation(oldViewBean.getIdBaseIndemnisation());

        saveViewBean(newViewBean, session);
    }

}
