/*
 * Créé le 18 juil. 07
 */

package globaz.corvus.servlet;

import globaz.commons.nss.NSUtil;
import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REDeleteCascadeDemandeAPrestationsDues;
import globaz.corvus.db.creances.RECreanceAccordee;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REDecisionsManager;
import globaz.corvus.vb.creances.RECreancierViewBean;
import globaz.corvus.vb.creances.RERepartirLesCreancesViewBean;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.fweb.taglib.FWSelectorTag;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author BSC
 * 
 */

public class RECreancierAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_DE_ADD = "_de.jsp?" + PRDefaultAction.METHOD_ADD;
    private static final String VERS_ECRAN_DE_UPD = "_de.jsp?" + PRDefaultAction.METHOD_UPD;
    private static final String VERS_ECRAN_RC = "_rc.jsp";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * @param servlet
     */
    public RECreancierAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // ----------------------------------------------------------------------

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (!JadeStringUtil.isEmpty(request.getParameter("menuOptionToLoad"))
                && "decision".equals(request.getParameter("menuOptionToLoad"))) {
            return "/corvus?userAction=" + IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE + ".chercher";
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (!JadeStringUtil.isEmpty(request.getParameter("menuOptionToLoad"))
                && "decision".equals(request.getParameter("menuOptionToLoad"))) {
            return "/corvus?userAction=" + IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE + ".chercher";
        } else {
            return super._getDestModifierSucces(session, request, response, viewBean);
        }
    }

    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        if (!JadeStringUtil.isEmpty(request.getParameter("menuOptionToLoad"))
                && "decision".equals(request.getParameter("menuOptionToLoad"))) {
            return "/corvus?userAction=" + IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE + ".chercher";
        } else {
            return super._getDestSupprimerSucces(session, request, response, viewBean);
        }
    }

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

        FWViewBeanInterface viewBean = this.loadViewBean(session);
        String type = request.getParameter("csType");

        if (isRetourDepuisPyxis(viewBean)) {

            // on revient depuis pyxis on se contente de forwarder car le bon
            // viewBean est deja en session
            ((RECreancierViewBean) viewBean).setRetourDepuisPyxis(false); // pour
            // la
            // prochaine
            // fois

            if (((RECreancierViewBean) viewBean).isNew()) {
                forward(getRelativeURL(request, session) + RECreancierAction.VERS_ECRAN_DE_ADD, request, response);
            } else {
                forward(getRelativeURL(request, session) + RECreancierAction.VERS_ECRAN_DE_UPD, request, response);
            }
        } else {

            if (!JadeStringUtil.isEmpty(type)) {
                String destination = getRelativeURL(request, session) + "_de.jsp";
                // RECreancierViewBean creancier = new RECreancierViewBean();
                // creancier.setCsType(type);
                RECreancierViewBean creancier = (RECreancierViewBean) viewBean;
                mainDispatcher.dispatch(creancier, getAction());
                this.saveViewBean(creancier, session);
                servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
            } else {
                super.actionAfficher(session, request, response, mainDispatcher);
            }
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

        FWViewBeanInterface viewBean = this.loadViewBean(session);
        RECreancierViewBean crViewBean;

        if (isRetourDepuisPyxis(viewBean)) {
            // on revient de pyxis, on recupere le viewBean en session pour
            // afficher les donnees dans la page rc.
            crViewBean = (RECreancierViewBean) viewBean;

        } else {
            /*
             * on affiche cette page par un chemin habituel, dans ce cas tout est normal, mise a part que l'on met un
             * viewBean dans la session qui contient les donnees qui doivent s'afficher dans le cadre rc de la ca page.
             */
            crViewBean = new RECreancierViewBean();

            try {
                JSPUtils.setBeanProperties(request, crViewBean);
            } catch (Exception e) {
                crViewBean.setMessage(e.getMessage());
                crViewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            crViewBean.setIdDemandeRente(request.getParameter("noDemandeRente"));
        }

        // on appelle le helper qui va charger les creanciers qui doivent
        // s'afficher
        mainDispatcher.dispatch(crViewBean, getAction());

        RENSSDTO dto = new RENSSDTO();
        dto.setNSS(getNumeroAvsFormate(request.getParameter("idTierRequerant"), (BSession) mainDispatcher.getSession()));

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
        PRSessionDataContainerHelper
                .setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO, null);

        /*
         * on sauve de toutes facons le viewBean dans la requete meme s'il est deja en session car c'est la que la page
         * rc va le rechercher.
         */
        this.saveViewBean(crViewBean, request);
        this.saveViewBean(crViewBean, session);

        servlet.getServletContext()
                .getRequestDispatcher(getRelativeURL(request, session) + RECreancierAction.VERS_ECRAN_RC)
                .forward(request, response);
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
    public String actionRepartirLesCreances(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {
        String destination = null;

        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            RERepartirLesCreancesViewBean vb = new RERepartirLesCreancesViewBean(viewBean.getISession(),
                    ((RECreancierViewBean) viewBean).getIdDemandeRente());
            vb.setIdTiers(request.getParameter("idTierRequerant"));

            viewBean = mainDispatcher.dispatch(vb, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            if (goesToSuccessDest) {
                destination = getRelativeURLwithoutClassPart(request, session) + "/repartirLesCreances"
                        + RECreancierAction.VERS_ECRAN_DE_UPD;
            } else {
                destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        return destination;
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
        RECreancierViewBean crViewBean = (RECreancierViewBean) this.loadViewBean(session);
        String idTierRequerant = request.getParameter("idTierRequerant");
        String noDemandeRente = request.getParameter("noDemandeRente");
        String csType = request.getParameter("csType");
        String montantRetroactif = request.getParameter("montantRetroactif");
        String menuOptionToLoad = request.getParameter("menuOptionToLoad");
        StringBuffer queryString = new StringBuffer();

        queryString.append(PRDefaultAction.USER_ACTION);
        queryString.append("=");
        queryString.append(IREActions.ACTION_CREANCIER);
        queryString.append(".");
        queryString.append(FWAction.ACTION_CHERCHER);
        queryString.append("&idCreancier=");
        queryString.append(crViewBean.getIdCreancier());
        queryString.append("&idTierRequerant=");
        queryString.append(idTierRequerant);
        queryString.append("&noDemandeRente=");
        queryString.append(noDemandeRente);
        queryString.append("&csType=");
        queryString.append(csType);
        queryString.append("&montantRetroactif=");
        queryString.append(montantRetroactif);
        queryString.append("&menuOptionToLoad=");
        queryString.append(menuOptionToLoad);

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
     * Méthode qui retourne le NNSS formaté
     * 
     * @param session
     * 
     * @return NNSS formaté
     */
    private String getNumeroAvsFormate(String idTierBeneficiaire, BSession session) {

        String result = "";

        if (!JadeStringUtil.isIntegerEmpty(idTierBeneficiaire)) {

            PRTiersWrapper tiers;
            try {
                tiers = PRTiersHelper.getTiersParId(session, idTierBeneficiaire);
                String nnss = tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                result = NSUtil.formatWithoutPrefixe(nnss, nnss.length() > 14 ? true : false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * inspecte le viewBean et retourne vrais si celui-ci indique que l'on revient de pyxis.
     * 
     * @param viewBean
     * 
     * @return
     */
    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof RECreancierViewBean) && ((RECreancierViewBean) viewBean)
                .isRetourDepuisPyxis());
    }

    /**
     * Add ou update des creances accordees.
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
    public String miseAJourCreancesAccordees(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        RERepartirLesCreancesViewBean rlcViewBean;

        try {
            rlcViewBean = (RERepartirLesCreancesViewBean) viewBean;
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(((BSession) mainDispatcher.getSession()).getLabel("JSP_RCR_D_ERREUR_UPDATE_CREANCES"));
            return FWDefaultServletAction.ERROR_PAGE;
        }

        mainDispatcher.dispatch(rlcViewBean, getAction());

        // on memorise si il y a eu une modification
        boolean isModified = false;

        // on retrouve tous les montants des differentes creances accordees
        for (int ra = 0; ra < rlcViewBean.getNombreRentesAccordees(); ra++) {

            for (int cr = 0; cr < rlcViewBean.getNombreCreanciers(); cr++) {

                RECreanceAccordee ca = rlcViewBean.getCreanceAccordee(ra, cr);

                // on cherche le montant retourne pour cette creance
                String nomMontant = "r" + ra + "m" + cr;
                String montant = request.getParameter(nomMontant);

                // mise a jour du montant de le viewBean
                if (!JadeStringUtil.isNull(montant)) {
                    montant = JadeStringUtil.removeChar(montant, '\'');

                    // une des valeurs a ete modifiee
                    if (!montant.equals(ca.getMontant())) {
                        isModified = true;
                    }

                    ca.setMontant(montant);
                }

                if (ca.isNew()) {
                    ca.add();
                } else {
                    ca.update();
                }
            }
        }

        // si les creances accordees on ete modifiee, on supprime les decisions
        if (isModified) {

            BITransaction transaction = ((BSession) mainDispatcher.getSession()).newTransaction();

            try {

                if (!transaction.isOpened()) {
                    transaction.openTransaction();
                }

                REDecisionsManager decisionsMgr = new REDecisionsManager();
                decisionsMgr.setSession((BSession) mainDispatcher.getSession());
                decisionsMgr.setForIdDemandeRente(rlcViewBean.getIdDemandeRente());
                decisionsMgr.find();

                for (Iterator iterator = decisionsMgr.iterator(); iterator.hasNext();) {
                    REDecisionEntity decision = (REDecisionEntity) iterator.next();

                    REDeleteCascadeDemandeAPrestationsDues.supprimerDecisionsCascade_noCommit(
                            (BSession) mainDispatcher.getSession(), transaction, decision,
                            IREValidationLevel.VALIDATION_LEVEL_NONE);
                }

            } catch (Exception e) {
                transaction.setRollbackOnly();

                viewBean.setMessage("Error " + e.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            } finally {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        if (transaction.hasErrors()) {
                            viewBean.setMessage(transaction.getErrors().toString());
                            viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        }
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        return _getDestAjouterSucces(session, request, response, rlcViewBean) + "&idTierRequerant="
                + rlcViewBean.getIdTiers() + "&noDemandeRente=" + rlcViewBean.getIdDemandeRente();
    }

}
