/*
 * Créé le 20 févr. 07
 */
package globaz.corvus.servlet;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.itext.REListeRenteAccordee;
import globaz.corvus.vb.demandes.REDemandeParametresRCDTO;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.corvus.vb.process.REDebloquerMontantRAViewBean;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteListViewBean;
import globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import globaz.prestation.ged.PRGedAffichageDossier;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.servlet.PRHybridAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author bsc
 */
public class RERenteAccordeeJointDemandeRenteAction extends PRHybridAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_DE = "_de.jsp?";
    private static final String VERS_ECRAN_DE_ADD = RERenteAccordeeJointDemandeRenteAction.VERS_ECRAN_DE
            + PRDefaultAction.METHOD_ADD;
    private static final String VERS_ECRAN_DE_UPD = RERenteAccordeeJointDemandeRenteAction.VERS_ECRAN_DE
            + PRDefaultAction.METHOD_UPD;
    private static final String VERS_ECRAN_RC = "_rc.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public RERenteAccordeeJointDemandeRenteAction(final FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    @Override
    protected String _getDestEchec(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        this.saveViewBean(viewBean, session);
        return getActionFullURL() + ".afficher";
    }

    @Override
    protected void actionAfficher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {
        FWViewBeanInterface viewBean = this.loadViewBean(session);

        if (!(viewBean instanceof RERenteAccordeeJointDemandeRenteViewBean)) {
            viewBean = new RERenteAccordeeJointDemandeRenteViewBean();
        }

        String isChangeBeneficaire = request.getParameter("changeBeneficiaire");

        if (!JadeStringUtil.isEmpty(isChangeBeneficaire)) {
            ((RERenteAccordeeJointDemandeRenteViewBean) viewBean).setIdTiersAdressePmtICDepuisPyxis(request
                    .getParameter("idTiersBeneficiaire"));
            ((RERenteAccordeeJointDemandeRenteViewBean) viewBean).setIsChangeBeneficiaire(isChangeBeneficaire);
        }

        if (isRetourDepuisPyxis(viewBean) || !JadeStringUtil.isEmpty(isChangeBeneficaire)
                || ((viewBean != null) && FWViewBeanInterface.ERROR.equals(viewBean.getMsgType()))) {
            // on revient depuis pyxis on se contente de forwarder car le bon
            // viewBean est deja en session
            ((RERenteAccordeeJointDemandeRenteViewBean) viewBean).setRetourDepuisPyxis(false); // pour
            // la
            // prochaine
            // fois

            String testMsg = viewBean.getMessage();

            mainDispatcher.dispatch(viewBean, getAction());

            viewBean.setMessage(testMsg);
            viewBean.setMsgType(FWViewBeanInterface.ERROR);

            if (((RERenteAccordeeJointDemandeRenteViewBean) viewBean).isNew()) {
                forward(getRelativeURL(request, session) + RERenteAccordeeJointDemandeRenteAction.VERS_ECRAN_DE_ADD,
                        request, response);
            } else {
                forward(getRelativeURL(request, session) + RERenteAccordeeJointDemandeRenteAction.VERS_ECRAN_DE_UPD,
                        request, response);
            }
        } else {

            // On mémorise ls NSS dans la session
            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(getNumeroAvsFormate(request.getParameter("idTierRequerant"),
                    (BSession) mainDispatcher.getSession()));

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

            // on ne revient pas depuis pyxis, comportement par defaut
            super.actionAfficher(session, request, response, mainDispatcher);

        }
    }

    /**
     * @param session
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws JadeClassCastException
     * @throws ClassCastException
     * @throws NullPointerException
     * @throws JadeServiceActivatorException
     * @throws JadeServiceLocatorException
     */
    public void actionAfficherDossierGed(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, final FWViewBeanInterface viewBean)
            throws ServletException, IOException, JadeServiceLocatorException, JadeServiceActivatorException,
            NullPointerException, ClassCastException, JadeClassCastException {

        PRGedAffichageDossier.actionAfficherDossierGed(session, request, response, mainDispatcher, viewBean);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionAjouter(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     * 
     * --> Voir helper
     */
    @Override
    protected void actionAjouter(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        super.actionAjouter(session, request, response, mainDispatcher);
    }

    /**
     * Bloquage de la rente accordee et redirection sur la liste des rentes accordees
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @throws Exception
     */
    public String actionBloquerRA(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws Exception {

        String destination = null;

        try {
            // getAction().setRight(FWSecureConstants.UPDATE);

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                destination = getRelativeURLwithoutClassPart(request, session) + "/renteAccordeeJointDemandeRente"
                        + RERenteAccordeeJointDemandeRenteAction.VERS_ECRAN_RC;
            } else {
                destination = _getDestModifierEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        return destination;

    }

    @Override
    protected void actionChercher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {
        RERenteAccordeeJointDemandeRenteViewBean viewBean = null;
        try {
            viewBean = (RERenteAccordeeJointDemandeRenteViewBean) request.getAttribute("viewBean");
            if (viewBean == null) {
                viewBean = new RERenteAccordeeJointDemandeRenteViewBean();
            }
            JSPUtils.setBeanProperties(request, viewBean);
        } catch (ClassCastException ex) {
            viewBean = null;
        } catch (InvocationTargetException e) {
            throw new ServletException(e.toString(), e);
        } catch (IllegalAccessException e) {
            throw new ServletException(e.toString(), e);
        } finally {
            if (viewBean == null) {
                viewBean = new RERenteAccordeeJointDemandeRenteViewBean();
            }
            mainDispatcher.dispatch(viewBean, getAction());
            this.saveViewBean(viewBean, request);
        }
        forward(getRelativeURL(request, session) + "_rc.jsp", request, response);
    }

    @Override
    protected void actionCustom(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher dispatcher) throws ServletException, IOException {

        String destination = FWDefaultServletAction.ERROR_PAGE;

        FWViewBeanInterface viewBean = this.loadViewBean(session);

        // Retour depuis pyxis depuis écran de déblocage du montant de la RA.
        // Dans ce cas, on n'appellera pas la méthode setBeanProperties.

        if ((viewBean instanceof REDebloquerMontantRAViewBean)
                && ((REDebloquerMontantRAViewBean) viewBean).isRetourDepuisPyxis()) {

            // selectionner l'action en fonction du parametre transmis
            try {
                Method methode = this.getClass().getMethod(getAction().getActionPart(), PRDefaultAction.PARAMS);
                destination = (String) methode.invoke(this, new Object[] { session, request, response, dispatcher,
                        viewBean });
            } catch (Exception e) {
                // impossible de trouver une methode avec ce nom et ces
                // parametres !!!
                e.printStackTrace();
            }
            // desactive le forward pour le cas ou la reponse a deja ete flushee
            if (!JadeStringUtil.isBlank(destination)) {
                forward(destination, request, response);
            }
        }
        // Traitement standard
        else {
            super.actionCustom(session, request, response, dispatcher);
        }
    }

    /**
     * Redirection sur l'ecran d'execution du process de deblocage
     * 
     * @param session
     * @param request
     * @param response
     * @param mainDispatcher
     * @param viewBean
     * @throws Exception
     */
    public String actionDebloquerMontantRA(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws Exception {

        String destination = null;

        try {
            // getAction().setRight(FWSecureConstants.UPDATE);

            if ((viewBean instanceof REDebloquerMontantRAViewBean)
                    && ((REDebloquerMontantRAViewBean) viewBean).isRetourDepuisPyxis()) {
                ;
            } else {
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            }

            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);
            if (goesToSuccessDest) {
                destination = "/corvusRoot/rentesaccordees/debloquerMontantRA_de.jsp?_method=add";
            } else {
                destination = _getDestModifierEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        return destination;
    }

    public String actionDesactiverBlocage(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws Exception {

        String destination = null;

        try {
            // getAction().setRight(FWSecureConstants.UPDATE);

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                destination = getRelativeURLwithoutClassPart(request, session) + "/renteAccordeeJointDemandeRente"
                        + RERenteAccordeeJointDemandeRenteAction.VERS_ECRAN_RC;
            } else {
                destination = _getDestModifierEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        return destination;

    }

    public String actionExecuterDeblocage(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws Exception {

        String destination = null;

        try {
            // getAction().setRight(FWSecureConstants.UPDATE);

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            // boolean goesToSuccessDest =
            // !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            RERenteAccordeeJointDemandeRenteViewBean vb = new RERenteAccordeeJointDemandeRenteViewBean();
            String idRA = ((REDebloquerMontantRAViewBean) viewBean).getIdRenteAccordee();
            vb.setSession((BSession) mainDispatcher.getSession());
            vb.setIdPrestationAccordee(idRA);
            vb.retrieve();
            // vb.setMsgType(viewBean.getMsgType());
            // vb.setMessage(viewBean.getMessage());
            session.setAttribute("viewBean", vb);
            request.setAttribute("viewBean", null);

            // if (goesToSuccessDest) {
            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession((BSession) mainDispatcher.getSession());
            ra.setIdPrestationAccordee(idRA);
            ra.retrieve();

            destination = getRelativeURLwithoutClassPart(request, session) + "/renteAccordeeJointDemandeRente"
                    + RERenteAccordeeJointDemandeRenteAction.VERS_ECRAN_RC;
            destination += "?idBasesCalcul=" + ra.getIdBaseCalcul();

            // } else {
            // destination = _getDestModifierEchec(session, request, response,
            // vb);
            // destination = ERROR_PAGE;
            // }

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        return destination;

    }

    @Override
    protected void actionLister(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {
        super.actionLister(session, request, response, mainDispatcher);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     * 
     * --> Voir helper
     */
    @Override
    protected void actionModifier(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        super.actionModifier(session, request, response, mainDispatcher);
    }

    // On surcharge la méthode actionCustom, pour traiter différemment le retour
    // depuix pyxis,
    // dans l'écran debloquerMontantRA_de.jsp

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionSupprimer(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     * 
     * --> Voir helper
     */
    @Override
    protected void actionSupprimer(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher) throws ServletException, IOException {

        super.actionSupprimer(session, request, response, mainDispatcher);
    }

    @Override
    protected FWViewBeanInterface beforeAfficher(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {
        try {
            JSPUtils.setBeanProperties(request, viewBean);

            String selectedId = request.getParameter("selectedId");

            ((RERenteAccordeeJointDemandeRenteViewBean) viewBean).setIdPrestationAccordee(selectedId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return viewBean;
    }

    @Override
    protected FWViewBeanInterface beforeLister(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWViewBeanInterface viewBean) {

        RENSSDTO dtoNss = new RENSSDTO();

        dtoNss.setNSS(request.getParameter("likeNumeroAVS"));
        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dtoNss);

        RERenteAccordeeJointDemandeRenteListViewBean listViewBean = (RERenteAccordeeJointDemandeRenteListViewBean) viewBean;

        REDemandeParametresRCDTO dto = new REDemandeParametresRCDTO();

        dto.setForCsSexe(listViewBean.getForCsSexe());
        dto.setForDateNaissance(listViewBean.getForDateNaissance());
        dto.setLikeNom(listViewBean.getLikeNom());
        dto.setLikePrenom(listViewBean.getLikePrenom());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO, dto);

        return super.beforeLister(session, request, response, viewBean);

    }

    /**
     * Méthode qui retourne le NNSS formaté
     * 
     * @param session
     * @return NNSS formaté
     */
    private String getNumeroAvsFormate(final String idTierBeneficiaire, final BSession session) {

        String result = "";

        if (!JadeStringUtil.isIntegerEmpty(idTierBeneficiaire)) {
            TITiersViewBean tiers = new TITiersViewBean();
            tiers.setSession(session);
            String nnss = tiers.getNumAvsActuel(idTierBeneficiaire);
            result = NSUtil.formatWithoutPrefixe(nnss, nnss.length() > 14 ? true : false);
        }

        return result;
    }

    public String imprimerListeRenteAccordee(final HttpSession session, final HttpServletRequest request,
            final HttpServletResponse response, final FWDispatcher mainDispatcher, final FWViewBeanInterface viewBean)
            throws ServletException, IOException {
        RERenteAccordeeJointDemandeRenteListViewBean listeViewBean = new RERenteAccordeeJointDemandeRenteListViewBean();
        RERenteAccordeeJointDemandeRenteViewBean rViewBean = new RERenteAccordeeJointDemandeRenteViewBean();
        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, listeViewBean);

            REListeRenteAccordee listeRenteAccordee = new REListeRenteAccordee();
            listeRenteAccordee.setSession((BSession) mainDispatcher.getSession());

            listeRenteAccordee.setForCsEtatDemande(listeViewBean.getForCsEtat());
            listeRenteAccordee.setForCsSexe(listeViewBean.getForCsSexe());
            listeRenteAccordee.setForCsTypeDemande(listeViewBean.getForCsTypeDemande());

            listeRenteAccordee.setForDateNaissance(listeViewBean.getForDateNaissance());
            listeRenteAccordee.setForDroitAu(listeViewBean.getForDroitAu());
            listeRenteAccordee.setForDroitDu(listeViewBean.getForDroitDu());
            listeRenteAccordee.setForGenrePrestation(listeViewBean.getForGenrePrestation());
            listeRenteAccordee.setForCodeCasSpecial(listeViewBean.getForCodeCasSpecial());

            listeRenteAccordee.setForNoDemandeRente(listeViewBean.getForNoDemandeRente());
            listeRenteAccordee.setForNoRenteAccordee(listeViewBean.getForNoRenteAccordee());
            listeRenteAccordee.setForNoBaseCalcul(listeViewBean.getForNoBaseCalcul());

            listeRenteAccordee.setLikeNom(listeViewBean.getLikeNom());
            listeRenteAccordee.setLikeNumeroAVS(listeViewBean.getLikeNumeroAVS());
            listeRenteAccordee.setLikeNumeroAVSNNSS(listeViewBean.getLikeNumeroAVSNNSS());
            listeRenteAccordee.setLikePrenom(listeViewBean.getLikePrenom());

            listeRenteAccordee.setForEnCours(listeViewBean.getIsRechercheRenteEnCours());

            listeRenteAccordee.executeProcess();

            if ((listeRenteAccordee.getAttachedDocuments() != null)
                    && (listeRenteAccordee.getAttachedDocuments().size() > 0)) {
                rViewBean.setAttachedDocuments(listeRenteAccordee.getAttachedDocuments());
            } else {
                rViewBean.setAttachedDocuments(null);
            }

        } catch (Exception ex) {
            return FWDefaultServletAction.ERROR_PAGE;
        }

        mainDispatcher.dispatch(rViewBean, getAction());
        this.saveViewBean(rViewBean, request);

        return getRelativeURL(request, session) + "_rc.jsp";
    }

    private boolean isRetourDepuisPyxis(final FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof RERenteAccordeeJointDemandeRenteViewBean) && ((RERenteAccordeeJointDemandeRenteViewBean) viewBean)
                .isRetourDepuisPyxis());
    }
}