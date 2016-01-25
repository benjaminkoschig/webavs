/*
 * Créé le 21 sept. 05
 */
package globaz.ij.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JACalendar;
import globaz.ij.api.lots.IIJLot;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.itext.IJListePrestations;
import globaz.ij.regles.IJPrestationRegles;
import globaz.ij.vb.lots.IJLotViewBean;
import globaz.ij.vb.prestations.IJPrestationJointLotPrononceListViewBean;
import globaz.ij.vb.prestations.IJPrestationJointLotPrononceViewBean;
import globaz.ij.vb.prestations.IJPrestationParametresRCDTO;
import globaz.ij.vb.prononces.IJNSSDTO;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJPrestationJointLotPrononceAction extends PRDefaultAction {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJPrestationJointLotPrononceAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public IJPrestationJointLotPrononceAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
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
        // on ne memorise pas le nss comme critere de recherche (pour avoir le
        // meme comportement que lors d'une suppression)
        IJNSSDTO ijnssdto = (IJNSSDTO) session.getAttribute("globaz.apg.util.PRSessionDataContainerHelper.NSS_DTO");
        ijnssdto.setNSS("");

        return super._getDestModifierSucces(session, request, response, viewBean);
    }

    /**
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

        return super._getDestSupprimerSucces(session, request, response, viewBean);
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

        super.actionAfficher(session, request, response, mainDispatcher);

        FWViewBeanInterface viewBean = this.loadViewBean(session);

        IJNSSDTO dto = new IJNSSDTO();

        if ("" != ((IJPrestationJointLotPrononceViewBean) viewBean).getNoAVS()) {
            dto.setNSS(((IJPrestationJointLotPrononceViewBean) viewBean).getNoAVS());
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
        }

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
     * 
     * @return DOCUMENT ME!
     */
    public String actionAjouterDansLot(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
        String id = request.getParameter("selectedId");

        try {
            // si on n'a pas de viewbean ou qu'il n'est pas de la bonne classe,
            // ou que son id n'est pas le même que
            // celui transmis dans la requête, il faut l'instancier
            if ((viewBean == null) || !(viewBean instanceof IJPrestationJointLotPrononceViewBean)
                    || !((IJPrestationJointLotPrononceViewBean) viewBean).getIdPrestation().equals(id)) {
                if (JadeStringUtil.isIntegerEmpty(id)) {
                    // on n'a pas de viewBean ni d'id pour le retrouver... C'est
                    // la cata.
                    return FWDefaultServletAction.ERROR_PAGE;
                }

                viewBean = new IJPrestationJointLotPrononceViewBean();
                ((IJPrestationJointLotPrononceViewBean) viewBean).setIdPrestation(id);
                viewBean.setISession(mainDispatcher.getSession());
                ((IJPrestationJointLotPrononceViewBean) viewBean).retrieve();
            }

            IJPrestationJointLotPrononceViewBean prestationJointLotPrononceViewBean = (IJPrestationJointLotPrononceViewBean) viewBean;

            if (!IJPrestationRegles.isMettableEnLot(prestationJointLotPrononceViewBean)) {
                prestationJointLotPrononceViewBean.setMsgType(FWViewBeanInterface.ERROR);
                prestationJointLotPrononceViewBean.setMessage(prestationJointLotPrononceViewBean.getSession().getLabel(
                        "PRESTATIONS_VALIDEES_SEULEMENT"));
            }

            this.saveViewBean(viewBean, session);
            // redirection vers la page d'ajout dans un lot

            return getRelativeURL(request, session) + "_de2.jsp?";
        } catch (Exception e) {
            e.printStackTrace();

            return FWDefaultServletAction.ERROR_PAGE;
        }
    }

    /**
     * Pour les cas ou l'identifiant d'un lot est transmis en parametres dans la requete, charge le lot pour cet
     * identifiant afin de trouver son numero et retourne ce resultat.
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
        IJPrestationJointLotPrononceListViewBean viewBean = new IJPrestationJointLotPrononceListViewBean();

        try {
            JSPUtils.setBeanProperties(request, viewBean);

            if (JadeStringUtil.isIntegerEmpty(viewBean.getForNoBaseIndemnisation())) {

                if (!JadeStringUtil.isIntegerEmpty(request.getParameter("selectedId"))) {
                    viewBean.setForNoBaseIndemnisation(request.getParameter("selectedId"));
                }
            }

            IJPrestationParametresRCDTO dto = new IJPrestationParametresRCDTO();

            viewBean.setForIdLot(request.getParameter("forIdLot"));

            if (!JadeStringUtil.isNull(viewBean.getForIdLot())) {
                dto.setNoIndemnisation("");
                dto.setNoLot(viewBean.getForIdLot());
            }

            viewBean.setForNoBaseIndemnisation(request.getParameter("forNoBaseIndemnisation"));

            if (!JadeStringUtil.isNull(viewBean.getForNoBaseIndemnisation())) {
                dto.setNoLot("");
                dto.setNoIndemnisation(viewBean.getForNoBaseIndemnisation());
            }

            if (JadeStringUtil.isEmpty(dto.getNoLot())) {
                viewBean.setForNoLot("");
                viewBean.setForIdLot("");
            }

            if (JadeStringUtil.isEmpty(dto.getNoIndemnisation())) {
                viewBean.setForNoBaseIndemnisation("");
            }

            PRSessionDataContainerHelper.setData(session,
                    PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO, dto);

            try {
                IJNSSDTO nssDto = (IJNSSDTO) session.getAttribute(PRSessionDataContainerHelper.KEY_NSS_DTO);

                if (nssDto != null) {
                    if (!JadeStringUtil.isBlankOrZero(request.getParameter("noAVS"))
                            && !request.getParameter("noAVS").equals(nssDto.getNSS())) {
                        nssDto.setNSS(request.getParameter("noAVS"));
                    }
                    PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
                }
            } catch (Exception e) {
                // Erreur vue globale, on ne va rien faire d'autre pour ne pas empecher le chargement de la page !
            }

        } catch (Exception e) {
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        mainDispatcher.dispatch(viewBean, getAction());
        this.saveViewBean(viewBean, request);
        this.saveViewBean(viewBean, session);
        forward(getRelativeURL(request, session) + "_rc.jsp", request, response);
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
     * 
     * @return DOCUMENT ME!
     */
    public String actionMettreDansLot(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
        IJPrestationJointLotPrononceViewBean plViewBean = (IJPrestationJointLotPrononceViewBean) viewBean;
        String destination = FWDefaultServletAction.ERROR_PAGE;

        // si le viewBean n'a pas d'idLot, il faut en ajouter un et le mettre
        // dedans
        if (JadeStringUtil.isIntegerEmpty(plViewBean.getIdLot())) {
            // creation du lot
            IJLotViewBean lot = new IJLotViewBean();

            lot.setDescription(plViewBean.getDescriptionLot());
            lot.setDateCreation(JACalendar.todayJJsMMsAAAA());
            lot.setCsEtat(IIJLot.CS_OUVERT);

            FWAction action = FWAction.newInstance("ij.lots.lot.ajouter");

            mainDispatcher.dispatch(lot, action);

            // pour prevenir les erreurs lors de la creation du lot
            if (!FWViewBeanInterface.ERROR.equals(lot.getMsgType())) {

                // Mise à jour de la prestation
                plViewBean.setIdLot(lot.getIdLot());
                plViewBean.setCsEtat(IIJPrestation.CS_MIS_EN_LOT);
                action = FWAction.newInstance("ij.prestations.prestationJointPrononce.modifier");
                mainDispatcher.dispatch(viewBean, action);

                // redirection vers la page des lots

                action = FWAction.newInstance("ij.lots.lot.chercher");
                mainDispatcher.dispatch(lot, action);
                this.saveViewBean(lot, session);
                destination = this.getUserActionURL(request, action.toString());

            } else {
                viewBean.setMessage(lot.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            // on a choisi un droit dans la liste
        } else {
            // update de la prestation
            plViewBean.setCsEtat(IIJPrestation.CS_MIS_EN_LOT);

            FWAction action = FWAction.newInstance("ij.prestation.prestationJointLotPrononce.modifier");

            mainDispatcher.dispatch(viewBean, action);

            action = FWAction.newInstance("ij.lots.lot.chercher");

            IJLotViewBean viewBean2 = new IJLotViewBean();

            mainDispatcher.dispatch(viewBean2, action);

            // saveViewBean(viewBean2, session);
            destination = this.getUserActionURL(request, action.toString());
        }

        return destination;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeLister(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        IJPrestationParametresRCDTO dto = new IJPrestationParametresRCDTO();

        dto.setDateDebut(((IJPrestationJointLotPrononceListViewBean) viewBean).getFromDateDebutPrononce());
        dto.setDatePaiement(((IJPrestationJointLotPrononceListViewBean) viewBean).getFromDatePaiement());
        dto.setEtatPrestation(((IJPrestationJointLotPrononceListViewBean) viewBean).getForCsEtat());
        dto.setNoIndemnisation(((IJPrestationJointLotPrononceListViewBean) viewBean).getForNoBaseIndemnisation());
        dto.setNoLot(((IJPrestationJointLotPrononceListViewBean) viewBean).getForNoLot());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO,
                dto);

        IJNSSDTO dtoNSS = new IJNSSDTO();

        dtoNSS.setNSS(((IJPrestationJointLotPrononceListViewBean) viewBean).getLikeNumeroAVS());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dtoNSS);

        return super.beforeLister(session, request, response, viewBean);
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
     * 
     * @return DOCUMENT ME!
     * 
     * @throws ServletException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public String imprimerListePrestations(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean)
            throws ServletException, IOException {

        // generer le document
        try {
            viewBean = new IJPrestationJointLotPrononceListViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            IJListePrestations listePrestatons = new IJListePrestations((BSession) mainDispatcher.getSession());
            listePrestatons.setForCsSexe(((IJPrestationJointLotPrononceListViewBean) viewBean).getForCsSexe());
            listePrestatons.setForDateNaissance(((IJPrestationJointLotPrononceListViewBean) viewBean)
                    .getForDateNaissance());
            listePrestatons.setForEtat(((IJPrestationJointLotPrononceListViewBean) viewBean).getForCsEtat());
            listePrestatons.setForNoLot(((IJPrestationJointLotPrononceListViewBean) viewBean).getForNoLot());
            listePrestatons.setForNoBaseIndemnisation(((IJPrestationJointLotPrononceListViewBean) viewBean)
                    .getForNoBaseIndemnisation());
            listePrestatons.setLikeNom(((IJPrestationJointLotPrononceListViewBean) viewBean).getLikeNom());
            listePrestatons.setLikePrenom(((IJPrestationJointLotPrononceListViewBean) viewBean).getLikePrenom());
            listePrestatons.setLikeNumeroAVS(((IJPrestationJointLotPrononceListViewBean) viewBean).getLikeNumeroAVS());
            listePrestatons.setLikeNumeroAVSNNSS(((IJPrestationJointLotPrononceListViewBean) viewBean)
                    .getLikeNumeroAVSNNSS());
            listePrestatons.setFromDateDebutPrononce(((IJPrestationJointLotPrononceListViewBean) viewBean)
                    .getFromDateDebutPrononce());
            listePrestatons.executeProcess();

            if ((listePrestatons.getAttachedDocuments() != null) && (listePrestatons.getAttachedDocuments().size() > 0)) {

                ((IJPrestationJointLotPrononceListViewBean) viewBean).setAttachedDocuments(listePrestatons
                        .getAttachedDocuments());
            } else {
                ((IJPrestationJointLotPrononceListViewBean) viewBean).setAttachedDocuments(null);
            }

            getAction().changeActionPart(FWAction.ACTION_CHERCHER);
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }

        getAction().setRight(FWSecureConstants.ADD);
        mainDispatcher.dispatch(viewBean, getAction());
        this.saveViewBean(viewBean, request);

        return getRelativeURL(request, session) + "_rc.jsp";
    }

}
