/*
 * Créé le 8 juin 05
 */
package globaz.apg.servlet;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.api.lots.IAPLot;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.prestation.APRepartitionJointPrestation;
import globaz.apg.db.prestation.APRepartitionJointPrestationManager;
import globaz.apg.itext.APListePrestations;
import globaz.apg.process.APListePrestationExcelProcess;
import globaz.apg.utils.APGUtils;
import globaz.apg.vb.droits.APDroitAPGDTO;
import globaz.apg.vb.droits.APDroitDTO;
import globaz.apg.vb.lots.APLotViewBean;
import globaz.apg.vb.prestation.APPrestationJointLotTiersDroitListViewBean;
import globaz.apg.vb.prestation.APPrestationJointLotTiersDroitViewBean;
import globaz.apg.vb.prestation.APPrestationParametresRCDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRDemande;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Iterator;

/**
 * Classe d'actions pour prestationJointLotTiersDroit
 * 
 * @author dvh
 */
public class APPrestationJointLotTiersDroitAction extends PRDefaultAction {

    private static final String VERS_DE_2 = "_de2.jsp?";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APPrestationJointLotTiersDroitAction.
     * 
     * @param servlet
     *            DOCUMENT ME!
     */
    public APPrestationJointLotTiersDroitAction(FWServlet servlet) {
        super(servlet);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

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

        APDroitDTO dto = new APDroitDTO();
        dto.setDateDebutDroit(((APPrestationJointLotTiersDroitViewBean) viewBean).getDateDebutDroit());
        dto.setGenreService(((APPrestationJointLotTiersDroitViewBean) viewBean).getGenreService());
        dto.setIdDroit(((APPrestationJointLotTiersDroitViewBean) viewBean).getIdDroit());
        dto.setNoAVS(((APPrestationJointLotTiersDroitViewBean) viewBean).getNoAVS());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, dto);

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
        try {
            if ((viewBean == null) || !(viewBean instanceof APPrestationJointLotTiersDroitViewBean)) {
                // Si on n'a pas de viewBean a ce moment là (ce qui peut arriver
                // si on arrive ici par le menu d'option
                // des
                // prestations, il faut en créer un
                String id = request.getParameter("selectedId");

                if (JadeStringUtil.isIntegerEmpty(id)) {
                    // on n'a pas de viewBean ni d'id pour le retrouver... C'est
                    // la cata.
                    return FWDefaultServletAction.ERROR_PAGE;
                }

                viewBean = new APPrestationJointLotTiersDroitViewBean();
                ((APPrestationJointLotTiersDroitViewBean) viewBean).setIdPrestationApg(id);
                viewBean.setISession(mainDispatcher.getSession());
                ((APPrestationJointLotTiersDroitViewBean) viewBean).retrieve();
            }

            // on ne veut pas que les repartitions soient mises a jour
            ((APPrestationJointLotTiersDroitViewBean) viewBean).wantMiseAJourRepartitions(false);

            this.saveViewBean(viewBean, session);

            // redirection vers la page d'ajout dans un lot
            return getRelativeURL(request, session) + APPrestationJointLotTiersDroitAction.VERS_DE_2;
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
        APPrestationJointLotTiersDroitListViewBean viewBean = new APPrestationJointLotTiersDroitListViewBean();

        viewBean.setForIdDroit(request.getParameter("forIdDroit"));

        if (!JadeStringUtil.isNull(viewBean.getForIdDroit())) {

            APPrestationParametresRCDTO dto = new APPrestationParametresRCDTO();
            dto.setNoDroit(viewBean.getForIdDroit());
            dto.setNoLot("");
            PRSessionDataContainerHelper.setData(session,
                    PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_RC_DTO, dto);

            // on met à jour le dto du droit pour l'affichage.
            APDroitLAPG droit = new APDroitLAPG();
            droit.setISession(mainDispatcher.getSession());
            droit.setIdDroit(viewBean.getForIdDroit());
            try {
                droit.retrieve();
            } catch (Exception e) {
               // on ne met pas à jour le dto.
            }
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO, new APDroitAPGDTO(droit));
        }

        if (!JadeStringUtil.isEmpty(request.getParameter("forIdLot"))) {
            viewBean.setForIdLot(request.getParameter("forIdLot"));

            // recherche du numero du lot
            APLotViewBean lot = new APLotViewBean();
            lot.setIdLot(viewBean.getForIdLot());
            lot.setISession(mainDispatcher.getSession());

            try {
                lot.retrieve();
            } catch (Exception e) {
                // tant pis, le numero du lot ne sera pas affiché.
            }

            viewBean.setForNoLot(lot.getNoLot());

            APPrestationParametresRCDTO dto = new APPrestationParametresRCDTO();
            dto.setNoLot(viewBean.getForNoLot());
            dto.setNoDroit("");
            PRSessionDataContainerHelper.setData(session,
                    PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_RC_DTO, dto);
        }

        // si on vient de calculer les prestations, on verifie que les toutes
        // les repartitions des prestations du droit
        // ont une adresse de paiement
        if (!JadeStringUtil.isNull(request.getParameter("checkRepartitionsDroit"))
                && "true".equals(request.getParameter("checkRepartitionsDroit"))
                && !JadeStringUtil.isIntegerEmpty(viewBean.getForIdDroit())) {

            APRepartitionJointPrestationManager rpManager = new APRepartitionJointPrestationManager();
            rpManager.setSession((BSession) mainDispatcher.getSession());
            rpManager.setForIdDroit(viewBean.getForIdDroit());
            try {
                rpManager.find(BManager.SIZE_NOLIMIT);

                Iterator iter = rpManager.iterator();
                APRepartitionJointPrestation rp = null;
                while (iter.hasNext()) {
                    rp = (APRepartitionJointPrestation) iter.next();

                    if (rp.loadAdressePaiement(null) == null) {
                        viewBean.setMessage(((BSession) mainDispatcher.getSession())
                                .getLabel("ERROR_REPARTITION_SANS_ADRESSE_PAIEMENT"));
                        viewBean.setMsgType(FWViewBeanInterface.ERROR);
                        break;
                    }
                }

            } catch (Exception e) {
                // on affiche qmmm la liste des prestations
            }
        }

        this.saveViewBean(viewBean, request);
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
        APPrestationJointLotTiersDroitViewBean plViewBean = (APPrestationJointLotTiersDroitViewBean) viewBean;
        String destination = FWDefaultServletAction.ERROR_PAGE;

        // si le viewBean n'a pas d'idLot, il faut en ajouter un et le mettre
        // dedans
        if (JadeStringUtil.isIntegerEmpty(plViewBean.getIdLot())) {
            // creation du lot
            APLotViewBean lot = new APLotViewBean();
            lot.setDescription(plViewBean.getDescriptionLot());
            lot.setDateCreation(JACalendar.todayJJsMMsAAAA());
            lot.setEtat(IAPLot.CS_OUVERT);

            String typePrestation = null;

            if (APGUtils.isTypeAllocationPandemie(plViewBean.getGenreService())) {
                typePrestation = IPRDemande.CS_TYPE_PANDEMIE;
            } else if (APGUtils.isTypeMaternite(plViewBean.getGenreService())) {
                typePrestation = IPRDemande.CS_TYPE_MATERNITE;
            } else if(APGUtils.isTypePaternite(plViewBean.getGenreService())) {
                typePrestation = IPRDemande.CS_TYPE_PATERNITE;
            }

            lot.setTypeLot(typePrestation);

            FWAction action = FWAction.newInstance("apg.lots.lot.ajouter");
            mainDispatcher.dispatch(lot, action);

            // pour prevenir les erreurs lors de la creation du lot
            if (!FWViewBeanInterface.ERROR.equals(lot.getMsgType())) {

                // Mise à jour de la prestation
                plViewBean.setIdLot(lot.getIdLot());
                plViewBean.setEtat(IAPPrestation.CS_ETAT_PRESTATION_MIS_LOT);
                action = FWAction.newInstance("apg.prestation.prestationJointLotTiersDroit.modifier");
                mainDispatcher.dispatch(viewBean, action);

                // redirection vers la page des lots

                action = FWAction.newInstance("apg.lots.lot.chercher");
                mainDispatcher.dispatch(lot, action);
                this.saveViewBean(lot, session);

                destination = this.getUserActionURL(request, action.toString());
            } else {
                viewBean.setMessage(lot.getMessage());
                viewBean.setMsgType(FWViewBeanInterface.ERROR);
            }

            // on a choisi un lot dans la liste
        } else {
            // update de la prestation
            plViewBean.setEtat(IAPPrestation.CS_ETAT_PRESTATION_MIS_LOT);

            FWAction action = FWAction.newInstance("apg.prestation.prestationJointLotTiersDroit.modifier");
            mainDispatcher.dispatch(viewBean, action);

            action = FWAction.newInstance("apg.lots.lot.chercher");

            APLotViewBean viewBean2 = new APLotViewBean();
            mainDispatcher.dispatch(viewBean2, action);

            // saveViewBean(viewBean2, session);
            destination = this.getUserActionURL(request, action.toString());
        }

        return destination;
    }

    /**
     * effectue les opération necessaires avant la modification effective d'une prestation. Mis dans les actions et non
     * dans le beforeUpdate du Bentity car on a besoin de savoir si cette modification provient d'une mise en lot.
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
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        try {
            APPrestationJointLotTiersDroitViewBean viewBean = (APPrestationJointLotTiersDroitViewBean) (this
                    .loadViewBean(session));
            JSPUtils.setBeanProperties(request, viewBean);

            // si la prestation est en etat contrôlé, elle doit repasser en
            // validé
            if (viewBean.getEtat().equals(IAPPrestation.CS_ETAT_PRESTATION_CONTROLE)) {
                viewBean.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
            }

            // le lot dans lequel elle se trouve doit être remis en état ouvert
            if (!JadeStringUtil.isIntegerEmpty(viewBean.getIdLot())) {
                FWAction actionModifLot = FWAction.newInstance("apg.lots.lot.modifier");
                APLotViewBean lot = (APLotViewBean) FWViewBeanActionFactory.newInstance(actionModifLot,
                        mainDispatcher.getPrefix());

                lot.setId(viewBean.getIdLot());
                lot.setSession(viewBean.getSession());
                lot.retrieve();
                lot.setEtat(IAPLot.CS_OUVERT);
                mainDispatcher.dispatch(lot, actionModifLot);
            }

            super.actionModifier(session, request, response, mainDispatcher);
        } catch (Exception e) {
            forward(FWDefaultServletAction.ERROR_PAGE, request, response);
        }
    }

    /**
     * action custom qui permet de rediriger sur la page de liste des repartitions pour la prestation qui se trouve
     * actuellement dans la session ou alors en utilisant les parametres de la requete.
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
    public String actionRepartitionPaiements(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) {
        StringBuffer action = new StringBuffer();

        action.append(IAPActions.ACTION_REPARTITION_PAIEMENTS);
        action.append(".");
        action.append(FWAction.ACTION_CHERCHER);
        action.append("&idPrestationCourante=");

        if (viewBean instanceof APPrestationJointLotTiersDroitViewBean) {
            APPrestationJointLotTiersDroitViewBean pViewBean = (APPrestationJointLotTiersDroitViewBean) viewBean;

            action.append(pViewBean.getIdPrestationApg());
            action.append("&idDroit=");
            action.append(pViewBean.getIdDroit());
            action.append("&genreService=");
            action.append(pViewBean.getGenreService());
        } else {
            action.append(request.getParameter("idPrestationCourante"));
            action.append("&idDroit=");
            action.append(request.getParameter("idDroit"));
            action.append("&genreService=");
            action.append(request.getParameter("genreService"));
        }

        return this.getUserActionURL(request, action.toString());
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

        APPrestationJointLotTiersDroitListViewBean listViewBean = (APPrestationJointLotTiersDroitListViewBean) viewBean;

        APPrestationParametresRCDTO dto = new APPrestationParametresRCDTO();
        dto.setPeriodeDateDebut(listViewBean.getFromDateDebut());
        dto.setPeriodeDateFin(listViewBean.getToDateFin());
        dto.setEtatPrestation(listViewBean.getForEtat());
        dto.setNoDroit(listViewBean.getForIdDroit());
        dto.setNoLot(listViewBean.getForNoLot());

        PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_RC_DTO,
                dto);

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
            viewBean = new APPrestationJointLotTiersDroitListViewBean();
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
            String isExcel = request.getParameter("isExcel");
            if(JadeStringUtil.isBlankOrZero(isExcel)){
            APListePrestations listePrestatons = new APListePrestations((BSession) mainDispatcher.getSession());
            listePrestatons.setSession((BSession) mainDispatcher.getSession());
            listePrestatons.setForCsSexe(((APPrestationJointLotTiersDroitListViewBean) viewBean).getForCsSexe());
            listePrestatons.setForDateNaissance(((APPrestationJointLotTiersDroitListViewBean) viewBean)
                    .getForDateNaissance());
            listePrestatons.setForEtat(((APPrestationJointLotTiersDroitListViewBean) viewBean).getForEtat());
            listePrestatons.setForIdDroit(((APPrestationJointLotTiersDroitListViewBean) viewBean).getForIdDroit());
            listePrestatons.setForNoLot(((APPrestationJointLotTiersDroitListViewBean) viewBean).getForNoLot());
            listePrestatons.setForTypeDroit(((APPrestationJointLotTiersDroitListViewBean) viewBean).getForTypeDroit());
            listePrestatons.setLikeNom(((APPrestationJointLotTiersDroitListViewBean) viewBean).getLikeNom());
            listePrestatons.setLikePrenom(((APPrestationJointLotTiersDroitListViewBean) viewBean).getLikePrenom());
            listePrestatons
                    .setLikeNumeroAVS(((APPrestationJointLotTiersDroitListViewBean) viewBean).getLikeNumeroAVS());
            listePrestatons.setLikeNumeroAVSNNSS(((APPrestationJointLotTiersDroitListViewBean) viewBean)
                    .getLikeNumeroAVSNNSS());
            listePrestatons
                    .setFromDateDebut(((APPrestationJointLotTiersDroitListViewBean) viewBean).getFromDateDebut());
            listePrestatons.setToDateFin(((APPrestationJointLotTiersDroitListViewBean) viewBean).getToDateFin());
            listePrestatons.executeProcess();

            if ((listePrestatons.getAttachedDocuments() != null) && (listePrestatons.getAttachedDocuments().size() > 0)) {

                ((APPrestationJointLotTiersDroitListViewBean) viewBean).setAttachedDocuments(listePrestatons
                        .getAttachedDocuments());
            } else {
                ((APPrestationJointLotTiersDroitListViewBean) viewBean).setAttachedDocuments(null);
            }
            }else{
                APListePrestationExcelProcess listePrestatons = new APListePrestationExcelProcess();
                listePrestatons.setSession((BSession) mainDispatcher.getSession());
                listePrestatons.setForCsSexe(((APPrestationJointLotTiersDroitListViewBean) viewBean).getForCsSexe());
                listePrestatons.setForDateNaissance(((APPrestationJointLotTiersDroitListViewBean) viewBean)
                        .getForDateNaissance());
                listePrestatons.setForEtat(((APPrestationJointLotTiersDroitListViewBean) viewBean).getForEtat());
                listePrestatons.setForIdDroit(((APPrestationJointLotTiersDroitListViewBean) viewBean).getForIdDroit());
                listePrestatons.setForNoLot(((APPrestationJointLotTiersDroitListViewBean) viewBean).getForNoLot());
                listePrestatons.setForTypeDroit(((APPrestationJointLotTiersDroitListViewBean) viewBean).getForTypeDroit());
                listePrestatons.setLikeNom(((APPrestationJointLotTiersDroitListViewBean) viewBean).getLikeNom());
                listePrestatons.setLikePrenom(((APPrestationJointLotTiersDroitListViewBean) viewBean).getLikePrenom());
                listePrestatons
                        .setLikeNumeroAVS(((APPrestationJointLotTiersDroitListViewBean) viewBean).getLikeNumeroAVS());
                listePrestatons.setLikeNumeroAVSNNSS(((APPrestationJointLotTiersDroitListViewBean) viewBean)
                        .getLikeNumeroAVSNNSS());
                listePrestatons
                        .setFromDateDebut(((APPrestationJointLotTiersDroitListViewBean) viewBean).getFromDateDebut());
                listePrestatons.setToDateFin(((APPrestationJointLotTiersDroitListViewBean) viewBean).getToDateFin());
                listePrestatons.executeProcess();
                if ((listePrestatons.getAttachedDocuments() != null) && (listePrestatons.getAttachedDocuments().size() > 0)) {
                    ((APPrestationJointLotTiersDroitListViewBean) viewBean).setAttachedDocuments(listePrestatons
                            .getAttachedDocuments());
                } else {
                    ((APPrestationJointLotTiersDroitListViewBean) viewBean).setAttachedDocuments(null);
                }
            }

            getAction().changeActionPart(FWAction.ACTION_CHERCHER);
        } catch (Exception e) {
            return FWDefaultServletAction.ERROR_PAGE;
        }

        // getAction().setRight(FWSecureConstants.ADD);
        mainDispatcher.dispatch(viewBean, getAction());
        this.saveViewBean(viewBean, request);

        return getRelativeURL(request, session) + "_rc.jsp";
    }

}
