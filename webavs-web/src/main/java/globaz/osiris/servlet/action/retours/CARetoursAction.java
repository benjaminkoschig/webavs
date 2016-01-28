package globaz.osiris.servlet.action.retours;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.fweb.taglib.FWSelectorTag;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.retours.CALignesRetours;
import globaz.osiris.db.retours.CALignesRetoursListViewBean;
import globaz.osiris.db.retours.CARetours;
import globaz.osiris.db.retours.CARetoursViewBean;
import globaz.osiris.print.itext.list.CAListeRetours;
import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Prend en charge les actions personnalisées
 */
public class CARetoursAction extends FWDefaultServletAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     */
    protected static final String ATTRIBUTE_VIEWBEAN = "viewBean";

    /** DOCUMENT ME! */
    protected static final String DESACTIVE_VALIDATION = "_valid=fail&_back=sl";

    /**
     */
    protected static final String METHOD_ADD = CARetoursAction.METHOD_PARAM + "=" + CARetoursAction.METHOD_ADD_VALUE;

    /**
     */
    protected static final String METHOD_ADD_VALUE = "add";

    /**
     */
    protected static final String METHOD_PARAM = "_method";

    /**
     */
    protected static final String METHOD_UPD = CARetoursAction.METHOD_PARAM + "=" + CARetoursAction.METHOD_UPD_VALUE;

    /**
     */
    protected static final String METHOD_UPD_VALUE = "upd";

    private static final Class[] PARAMS = new Class[] { HttpSession.class, HttpServletRequest.class,
            HttpServletResponse.class, FWDispatcher.class, FWViewBeanInterface.class };

    /**
     */
    protected static final String SELECTED_ID = "selectedId";

    /**
     */
    protected static final String USER_ACTION = "userAction";

    /**
     */
    protected static final String VALID_NEW = "_valid=new";

    protected static final String VERS_ECRAN_DE = "_de.jsp";

    /**
     */
    private static final String VERS_ECRAN_DE_ADD = "_de.jsp?" + CARetoursAction.METHOD_ADD;

    /**
     */
    private static final String VERS_ECRAN_DE_UPD = "_de.jsp?" + CARetoursAction.METHOD_UPD;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Initialise l'action
     * 
     * @param servlet
     *            Le servlet concerné par cette action
     */
    public CARetoursAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = this.loadViewBean(session);

        if (isRetourDepuisPyxis(viewBean)) {
            // on revient depuis pyxis on se contente de forwarder car le bon
            // viewBean est deja en session
            ((CARetoursViewBean) viewBean).setRetourDepuisPyxis(false); // pour
            // la
            // prochaine
            // fois

            if (((CARetoursViewBean) viewBean).isNew()) {
                forward(getRelativeURL(request, session) + CARetoursAction.VERS_ECRAN_DE_ADD, request, response);
            } else {
                forward(getRelativeURL(request, session) + CARetoursAction.VERS_ECRAN_DE_UPD, request, response);
            }
        } else {
            // on ne revient pas depuis pyxis, comportement par defaut
            super.actionAfficher(session, request, response, mainDispatcher);

        }
    }

    /**
     * Ajoute une ligne de retour sur adresse de paiement
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     * @return la destination à atteindre
     */
    public String actionAjouterLignesRetoursSurAdressePaiement(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        CARetoursViewBean vbCastInRetourViewBean = (CARetoursViewBean) viewBean;

        String backupMontantRetour = vbCastInRetourViewBean.getMontantRetour();
        String backupEtatRetour = vbCastInRetourViewBean.getCsEtatRetour();

        mainDispatcher.dispatch(viewBean, getAction());

        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            restoreMontantAndEtatRetour(vbCastInRetourViewBean, backupMontantRetour, backupEtatRetour);
        }

        String destination = getDestinationAfficher(viewBean);

        return destination;
    }

    /**
     * Ajoute une ligne de retour sur section
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     * @return la destination à atteindre
     */
    public String actionAjouterLignesRetoursSurSection(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        CARetoursViewBean vbCastInRetourViewBean = (CARetoursViewBean) viewBean;

        String backupMontantRetour = vbCastInRetourViewBean.getMontantRetour();
        String backupEtatRetour = vbCastInRetourViewBean.getCsEtatRetour();

        mainDispatcher.dispatch(viewBean, getAction());

        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            restoreMontantAndEtatRetour(vbCastInRetourViewBean, backupMontantRetour, backupEtatRetour);
        }

        String destination = getDestinationAfficher(viewBean);

        return destination;
    }

    public String actionCreerRetourSplit(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        CARetoursViewBean vbCastInRetourViewBean = (CARetoursViewBean) viewBean;

        String backupMontantRetour = vbCastInRetourViewBean.getMontantRetour();
        String backupEtatRetour = vbCastInRetourViewBean.getCsEtatRetour();

        mainDispatcher.dispatch(viewBean, getAction());

        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            restoreMontantAndEtatRetour(vbCastInRetourViewBean, backupMontantRetour, backupEtatRetour);
        }

        String destination = getDestinationAfficher(viewBean);

        return destination;
    }

    /**
     * Implémentation de actionCustom qui extrait la partie action du paramètre userAction et tente de trouver une
     * méthode PUBLIQUE de notre classe qui porte ce nom et prend comme arguments {HttpSession, HttpServletRequest,
     * HttpServletResponse, FWDispatcher, FWViewBeanInterface}, si elle en trouve une, elle l'invoke et redirige vers la
     * destination que cette méthode retourne en paramètre, sinon elle redirige vers la page d'erreur.
     * <p>
     * Cette méthode extrait le viewBean de la session, appelle JSPUtils.setBeanProperties et appelle la méthode trouvée
     * en lui transmettant ce viewBean.
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
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     * @see globaz.framework.controller.FWDefaultServletAction#actionCustom(javax.servlet.http.HttpSession,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     *      globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher dispatcher) throws ServletException, IOException {
        String destination = FWDefaultServletAction.ERROR_PAGE;
        FWViewBeanInterface viewBean;

        try {
            viewBean = this.loadViewBean(session);

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
            Method methode = this.getClass().getMethod(getAction().getActionPart(), CARetoursAction.PARAMS);

            destination = (String) methode.invoke(this,
                    new Object[] { session, request, response, dispatcher, viewBean });
        } catch (Exception e) {
            // impossible de trouver une methode avec ce nom et ces parametres
            // !!!
            // Do nothing
        }

        // desactive le forward pour le cas ou la reponse a deja ete flushee
        if (!JadeStringUtil.isBlank(destination)) {
            forward(destination, request, response);
        }
    }

    /**
     * Action qui permet de lister les lignes de retour sur adresse de paiement
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            vb
     */
    public void actionListerLignesRetoursSurAdressePaiement(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws ServletException,
            IOException {

        String _destination = "";
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));

        try {

            CALignesRetoursListViewBean viewBean = new CALignesRetoursListViewBean();
            viewBean.setSession((BSession) mainDispatcher.getSession());

            // gérer droit actions grâce à un helper
            viewBean = (CALignesRetoursListViewBean) mainDispatcher.dispatch(viewBean, action);

            CARetours vbRetour = (CARetours) vb;
            viewBean.setForIdRetour(vbRetour.getIdRetour());
            viewBean.setForCsType(CALignesRetours.CS_ETAT_LIGNE_REPAIEMENT);
            viewBean.find();

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            request.setAttribute("viewBean", viewBean);
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);

            _destination = getRelativeURLwithoutClassPart(request, session)
                    + "lignesRetoursSurAdressePaiement_rcListe.jsp";

        } catch (Exception e) {

            _destination = FWDefaultServletAction.ERROR_PAGE;

        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * Action qui permet de lister les lignes de retour sur section
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            vb
     */
    public void actionListerLignesRetoursSurSection(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface vb) throws ServletException,
            IOException {

        String _destination = "";
        FWAction action = FWAction.newInstance(request.getParameter("userAction"));

        try {

            CALignesRetoursListViewBean viewBean = new CALignesRetoursListViewBean();
            viewBean.setSession((BSession) mainDispatcher.getSession());

            // gérer droit actions grâce à un helper
            viewBean = (CALignesRetoursListViewBean) mainDispatcher.dispatch(viewBean, action);

            CARetours vbRetour = (CARetours) vb;
            viewBean.setForIdRetour(vbRetour.getIdRetour());
            viewBean.setForCsType(CALignesRetours.CS_ETAT_LIGNE_COMPENSATION);
            viewBean.find();

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            request.setAttribute("viewBean", viewBean);
            session.removeAttribute("listViewBean");
            session.setAttribute("listViewBean", viewBean);

            _destination = getRelativeURLwithoutClassPart(request, session) + "lignesRetoursSurSection_rcListe.jsp";

        } catch (Exception e) {

            _destination = FWDefaultServletAction.ERROR_PAGE;

        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    /**
     * ecrase une des valeurs sauvee dans la session par FWSelectorTag de telle sorte que l'on sache exactement quelle
     * action sera executee lorsque l'on revient de pyxis et avec quels parametres.
     * 
     * @see FWSelectorTag
     * @param session
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @param mainDispatcher
     *            DOCUMENT ME!
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
        CARetoursViewBean rpViewBean = (CARetoursViewBean) this.loadViewBean(session);
        StringBuffer queryString = new StringBuffer();

        queryString.append(CARetoursAction.USER_ACTION);
        queryString.append("=osiris.retours.retours.");
        queryString.append(FWAction.ACTION_AFFICHER);
        queryString.append("&selectedId=");
        queryString.append(rpViewBean.getIdRetour());

        // HACK: on remplace une des valeurs sauvee en session par FWSelectorTag
        session.setAttribute(FWDefaultServletAction.ATTRIBUT_SELECTOR_CUSTOMERURL, queryString.toString());

        // comportement par defaut
        super.actionSelectionner(session, request, response, mainDispatcher);
    }

    /**
     * Action qui permet de supprimer une ligne de retour sur adresse de paiement
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     * @return la destination à atteindre
     */
    public String actionSupprimerLignesRetoursSurAdressePaiement(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        CARetoursViewBean vbCastInRetourViewBean = (CARetoursViewBean) viewBean;

        String backupMontantRetour = vbCastInRetourViewBean.getMontantRetour();
        String backupEtatRetour = vbCastInRetourViewBean.getCsEtatRetour();

        mainDispatcher.dispatch(viewBean, getAction());

        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            restoreMontantAndEtatRetour(vbCastInRetourViewBean, backupMontantRetour, backupEtatRetour);
        }

        String destination = getDestinationAfficher(viewBean);

        return destination;
    }

    /**
     * Action qui permet de supprimer une ligne de retour sur section
     * 
     * @throws IOException
     *             , ServletException
     * @param HttpSession
     *            session
     * @param HttpServletRequest
     *            request
     * @param HttpServletResponse
     *            response
     * @param FWDispatcher
     *            mainDispatcher
     * @param FWViewBeanInterface
     *            viewBean
     * @return la destination à atteindre
     */
    public String actionSupprimerLignesRetoursSurSection(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        CARetoursViewBean vbCastInRetourViewBean = (CARetoursViewBean) viewBean;

        String backupMontantRetour = vbCastInRetourViewBean.getMontantRetour();
        String backupEtatRetour = vbCastInRetourViewBean.getCsEtatRetour();

        mainDispatcher.dispatch(viewBean, getAction());

        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            restoreMontantAndEtatRetour(vbCastInRetourViewBean, backupMontantRetour, backupEtatRetour);
        }

        String destination = getDestinationAfficher(viewBean);

        return destination;
    }

    /**
     * @param destination
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     * @param response
     *            DOCUMENT ME!
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    protected void forward(String destination, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    private String getDestinationAfficher(FWViewBeanInterface viewBean) throws RemoteException {
        String destination = null;
        String action = null;

        if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            action = "reAfficher";
        } else {
            action = "afficher";
        }
        destination = getActionFullURL() + "." + action + "&selectedId=" + ((CARetoursViewBean) viewBean).getIdRetour()
                + "&" + CARetoursAction.METHOD_UPD;

        return destination;
    }

    /**
     * Cree une chaine de caractere du type '/nomServlet?userAction=action'
     * 
     * @param request
     *            DOCUMENT ME!
     * @param action
     *            une action COMPLETE (avec actionPart)
     * @return DOCUMENT ME!
     */
    protected String getUserActionURL(HttpServletRequest request, String action) {
        return request.getServletPath() + "?" + CARetoursAction.USER_ACTION + "=" + action;
    }

    /**
     * Cree une chaine de caractere du type '/nomServlet?userAction=actionBase.actionPart'
     * 
     * @param request
     *            DOCUMENT ME!
     * @param actionBase
     *            les parties application, package et class de la chaine action
     * @param actionPart
     *            la partie action de la chaine action (exemple 'afficher')
     * @return DOCUMENT ME!
     */
    protected String getUserActionURL(HttpServletRequest request, String actionBase, String actionPart) {
        return this.getUserActionURL(request, actionBase + "." + actionPart);
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
     * @param mainDispatcher
     *            DOCUMENT ME!
     * @param viewBean
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String imprimerListePrestations(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        FWAction action = FWAction.newInstance(request.getParameter("userAction"));

        // generer le document
        try {
            viewBean = new CARetoursViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // gérer droit actions grâce à un helper
            viewBean = mainDispatcher.dispatch(viewBean, action);

            CAListeRetours listeRetours = new CAListeRetours();
            listeRetours.setSession((BSession) mainDispatcher.getSession());
            listeRetours.setForIdLot(((CARetoursViewBean) viewBean).getForIdLot());
            listeRetours.setLikeNumNom(((CARetoursViewBean) viewBean).getLikeNumNom());
            listeRetours.setForMontantRetour(((CARetoursViewBean) viewBean).getForMontantRetour());
            listeRetours.setForCsEtatRetour(((CARetoursViewBean) viewBean).getForCsEtatRetour());
            listeRetours.setForCsMotifRetour(((CARetoursViewBean) viewBean).getForCsMotifRetour());
            listeRetours.setForDateRetour(((CARetoursViewBean) viewBean).getForDateRetour());
            listeRetours.setLikeLibelleRetour(((CARetoursViewBean) viewBean).getLikeLibelleRetour());
            listeRetours.executeProcess();

            if ((listeRetours.getAttachedDocuments() != null) && (listeRetours.getAttachedDocuments().size() > 0)) {
                ((CARetoursViewBean) viewBean).setAttachedDocuments(listeRetours.getAttachedDocuments());
            } else {
                ((CARetoursViewBean) viewBean).setAttachedDocuments(null);
            }

            getAction().changeActionPart(FWAction.ACTION_CHERCHER);
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }

        mainDispatcher.dispatch(viewBean, getAction());
        request.setAttribute(CARetoursAction.ATTRIBUTE_VIEWBEAN, viewBean);

        return getRelativeURL(request, session) + "_rc.jsp";
    }

    /**
     * inspecte le viewBean et retourne vrais si celui-ci indique que l'on revient de pyxis.
     * 
     * @param viewBean
     * @return
     */
    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof CARetoursViewBean) && ((CARetoursViewBean) viewBean)
                .isRetourDepuisPyxis());
    }

    /**
     * DOCUMENT ME!
     * 
     * @param request
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    protected FWViewBeanInterface loadViewBean(HttpServletRequest request) {
        return (FWViewBeanInterface) request.getAttribute("viewBean");
    }

    /**
     * charge le viewBean sauve dans le session sous le nom standard.
     * 
     * @param session
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    protected FWViewBeanInterface loadViewBean(HttpSession session) {
        return (FWViewBeanInterface) session.getAttribute("viewBean");
    }

    private void restoreMontantAndEtatRetour(CARetoursViewBean retourViewBean, String backupMontantRetour,
            String backupEtatRetour) {
        retourViewBean.setMontantRetour(backupMontantRetour);
        retourViewBean.setCsEtatRetour(backupEtatRetour);
    }

}
