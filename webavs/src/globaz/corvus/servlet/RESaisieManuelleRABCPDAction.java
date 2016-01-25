/*
 * Créé le 2 juil. 07
 */

package globaz.corvus.servlet;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteSurvivant;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.demandes.REPeriodeInvalidite;
import globaz.corvus.db.demandes.REPeriodeInvaliditeManager;
import globaz.corvus.utils.survenance.REDateSurvenanceUtil;
import globaz.corvus.vb.basescalcul.RESaisieManuelleRABCPDViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.servlet.PRDefaultAction;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author HPE
 * 
 */

public class RESaisieManuelleRABCPDAction extends PRDefaultAction {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static final String VERS_ECRAN_DE_ADD = "_de.jsp?" + PRDefaultAction.METHOD_ADD;
    private String _destination = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe RESaisieDemandeRenteAction.
     * 
     * @param FWServlet
     *            servlet
     */
    public RESaisieManuelleRABCPDAction(FWServlet servlet) {
        super(servlet);
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

        FWViewBeanInterface vb = this.loadViewBean(session);

        if (isRetourDepuisPyxis(vb)) {

            try {

                // Ecran toujours utilisé pour l'ajout
                getAction().changeActionPart(FWAction.ACTION_NOUVEAU);

                // on revient depuis pyxis on se contente de forwarder car le
                // bon viewBean est deja en session
                ((RESaisieManuelleRABCPDViewBean) vb).setRetourDepuisPyxis(false); // pour
                // la
                // prochaine
                // fois

                mainDispatcher.dispatch(vb, getAction());

                forward(getRelativeURL(request, session) + RESaisieManuelleRABCPDAction.VERS_ECRAN_DE_ADD, request,
                        response);

            } catch (Exception e) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }

        } else {

            try {

                // Ecran toujours utilisé pour l'ajout
                getAction().changeActionPart(FWAction.ACTION_NOUVEAU);

                // Reprise des éléments passés en requête
                String noDemandeRente = request.getParameter("noDemandeRente");
                String idTierRequerant = request.getParameter("idTierRequerant");
                String idTierBeneficiaire = request.getParameter("idTiersBeneficiaire");

                // Reprise des éventuels paramètres des BC - RA - PD
                String idBasesCalcul = request.getParameter("idBasesCalcul");

                // Création du viewBean
                RESaisieManuelleRABCPDViewBean viewBean = new RESaisieManuelleRABCPDViewBean();

                // Mise par défaut de l'idTiersBeneficiaire de l'id_Requerant si
                // pas dans les paramètres (recharge)
                if (JadeStringUtil.isBlank(idTierBeneficiaire) || "null".equals(idTierBeneficiaire)) {
                    viewBean.setIdTiersBeneficiaire(idTierRequerant);
                } else {
                    viewBean.setIdTiersBeneficiaire(idTierBeneficiaire);
                }

                // Selon les paramètres éventuels, retrieve de la BC et de la
                // demande
                if (!JadeStringUtil.isIntegerEmpty(idBasesCalcul)) {

                    viewBean.setIsBasesCalculModifiable(Boolean.FALSE);

                    REBasesCalcul bc = new REBasesCalcul();
                    bc.setSession((BSession) mainDispatcher.getSession());
                    bc.setIdBasesCalcul(idBasesCalcul);
                    bc.retrieve();

                    viewBean.setRAMBasesCalcul(bc.getRevenuAnnuelMoyen());
                    viewBean.setEchelleBasesCalcul(bc.getEchelleRente());
                    viewBean.setAnneeTraitementBasesCalcul(bc.getAnneeTraitement());
                    viewBean.setDureeCotAv73BasesCalcul(bc.getDureeCotiAvant73());
                    viewBean.setDureeCotAp73BasesCalcul(bc.getDureeCotiDes73());
                    viewBean.setMoisAppAv73BasesCalcul(bc.getMoisAppointsAvant73());
                    viewBean.setMoisAppAp73BasesCalcul(bc.getMoisAppointsDes73());
                    viewBean.setDureeCotClasseBasesCalcul(bc.getAnneeCotiClasseAge());
                    viewBean.setAnneeNiveauBasesCalcul(bc.getAnneeDeNiveau());
                    viewBean.setDureeCotRAMBasesCalcul(bc.getDureeRevenuAnnuelMoyen());
                    viewBean.setInvaliditePrecoceBasesCalcul(bc.isInvaliditePrecoce());
                    viewBean.setDureeCotEtrAv73BasesCalcul(bc.getPeriodeAssEtrangerAv73());
                    viewBean.setDureeCotEtrAp73BasesCalcul(bc.getPeriodeAssEtrangerDes73());
                    viewBean.setSuppAIRAMBasesCalcul(bc.getSupplementCarriere());

                    if (bc.getDroitApplique().equals("10")) {
                        viewBean.setBonusEducatifBasesCalcul("");
                    } else {

                        REBasesCalculNeuviemeRevision bc9 = new REBasesCalculNeuviemeRevision();
                        bc9.setSession((BSession) mainDispatcher.getSession());
                        bc9.setIdBasesCalcul(bc.getIdBasesCalcul());
                        bc9.retrieve();

                        viewBean.setBonusEducatifBasesCalcul(bc9.getBonificationTacheEducative());

                    }

                    viewBean.setAnneesEducatifBasesCalcul(bc.getAnneeBonifTacheEduc());
                    viewBean.setAnneesAssistanceBasesCalcul(bc.getAnneeBonifTacheAssistance());
                    viewBean.setAnneesTransitionBasesCalcul(bc.getAnneeBonifTransitoire());
                    viewBean.setCodeRevenusSplittesBasesCalcul(bc.isRevenuSplitte());
                    viewBean.setCodeRevenu9emeBasesCalcul(bc.getRevenuPrisEnCompte());
                    viewBean.setDroitBasesCalcul(bc.getDroitApplique());
                    viewBean.setIdBasesCalcul(bc.getIdBasesCalcul());

                } else {

                    viewBean.setIsBasesCalculModifiable(Boolean.TRUE);

                }

                if (!JadeStringUtil.isIntegerEmpty(noDemandeRente)) {

                    REDemandeRenteJointDemande demande = new REDemandeRenteJointDemande();
                    demande.setSession((BSession) mainDispatcher.getSession());
                    demande.setIdDemandeRente(noDemandeRente);
                    demande.retrieve();

                    viewBean.setIdRenteCalculee(demande.getIdRenteCalculee());

                    // Retrieve des informations complémentaires
                    PRInfoCompl infoCompl = new PRInfoCompl();
                    if (!JadeStringUtil.isEmpty(demande.getIdInfoComplementaire())) {
                        try {
                            infoCompl.setSession((BSession) mainDispatcher.getSession());
                            infoCompl.setIdInfoCompl(demande.getIdInfoComplementaire());
                            infoCompl.retrieve();
                        } catch (Exception e) {
                            ((BSession) mainDispatcher.getSession())
                                    .addError("Erreur dans le retrieve des informations complémentaires (REFichierAssuresPrinter)");
                        }
                    }

                    if (demande.getCsTypeDemande().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API)) {

                        REDemandeRenteAPI renteAPI = new REDemandeRenteAPI();
                        renteAPI.setSession((BSession) mainDispatcher.getSession());
                        renteAPI.setIdDemandeRente(demande.getIdDemandeRente());
                        renteAPI.retrieve();

                        viewBean.setCleInfirmiteDemandeRente(((BSession) mainDispatcher.getSession()).getCode(renteAPI
                                .getCsInfirmite())
                                + ((BSession) mainDispatcher.getSession()).getCode(renteAPI.getCsAtteinte()));
                        // BZ 5494, si pas de date de survenance définie, recherche dans les périodes
                        if (JadeStringUtil.isBlank(renteAPI.getDateSuvenanceEvenementAssure())) {
                            viewBean.setSurvEvAssDemandeRente(REDateSurvenanceUtil.getSurvenancePeriodeAPI(
                                    viewBean.getSession(), renteAPI.getIdDemandeRente()));
                        } else {
                            if (JadeDateUtil.isGlobazDate(renteAPI.getDateSuvenanceEvenementAssure())) {
                                viewBean.setSurvEvAssDemandeRente(renteAPI.getDateSuvenanceEvenementAssure().substring(
                                        3));
                            } else {
                                viewBean.setSurvEvAssDemandeRente(renteAPI.getDateSuvenanceEvenementAssure());
                            }
                        }
                        viewBean.setCodeOfficeAIDemandeRente(renteAPI.getCodeOfficeAI());
                        viewBean.setIsTransfere(infoCompl.getIsTransfere());

                    } else if (demande.getCsTypeDemande().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE)) {

                        REDemandeRenteInvalidite renteInv = new REDemandeRenteInvalidite();
                        renteInv.setSession((BSession) mainDispatcher.getSession());
                        renteInv.setIdDemandeRente(demande.getIdDemandeRente());
                        renteInv.retrieve();

                        REPeriodeInvaliditeManager perInvMan = new REPeriodeInvaliditeManager();
                        perInvMan.setSession((BSession) mainDispatcher.getSession());
                        perInvMan.setForIdDemandeRente(demande.getIdDemandeRente());
                        perInvMan.find();

                        for (Iterator iter = perInvMan.iterator(); iter.hasNext();) {
                            REPeriodeInvalidite perInv = (REPeriodeInvalidite) iter.next();

                            // TODO VOIR SI C'EST LA BONNE !!! (PRENDRE LA
                            // DERNIERE PERIODE)
                            viewBean.setDegreInvaliditeDemandeRente(perInv.getDegreInvalidite());

                        }

                        viewBean.setCleInfirmiteDemandeRente(((BSession) mainDispatcher.getSession()).getCode(renteInv
                                .getCsInfirmite())
                                + ((BSession) mainDispatcher.getSession()).getCode(renteInv.getCsAtteinte()));
                        // BZ 5494, si pas de date de survenance définie, recherche dans les périodes
                        if (JadeStringUtil.isBlank(renteInv.getDateSuvenanceEvenementAssure())) {
                            viewBean.setSurvEvAssDemandeRente(REDateSurvenanceUtil.getSurvenancePeriodeInvalidite(
                                    mainDispatcher.getSession(), renteInv.getIdDemandeRente()));
                        } else {
                            if (JadeDateUtil.isGlobazDate(renteInv.getDateSuvenanceEvenementAssure())) {
                                viewBean.setSurvEvAssDemandeRente(renteInv.getDateSuvenanceEvenementAssure().substring(
                                        3));
                            } else {
                                viewBean.setSurvEvAssDemandeRente(renteInv.getDateSuvenanceEvenementAssure());
                            }
                        }

                        viewBean.setCodeOfficeAIDemandeRente(renteInv.getCodeOfficeAI());
                        viewBean.setIsTransfere(infoCompl.getIsTransfere());

                    } else if (demande.getCsTypeDemande().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT)) {

                        REDemandeRenteSurvivant renteSur = new REDemandeRenteSurvivant();
                        renteSur.setSession((BSession) mainDispatcher.getSession());
                        renteSur.setIdDemandeRente(demande.getIdDemandeRente());
                        renteSur.retrieve();

                        viewBean.setIsTransfere(infoCompl.getIsTransfere());

                    } else if (demande.getCsTypeDemande().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE)) {

                        REDemandeRenteVieillesse renteViel = new REDemandeRenteVieillesse();
                        renteViel.setSession((BSession) mainDispatcher.getSession());
                        renteViel.setIdDemandeRente(demande.getIdDemandeRente());
                        renteViel.retrieve();

                        viewBean.setAnneesAnticipationDemandeRente(renteViel.getCsAnneeAnticipation());

                        String dateRevocationDemandeRente;
                        if (JadeDateUtil.isGlobazDate(renteViel.getDateRevocationRequerant())) {
                            dateRevocationDemandeRente = JadeDateUtil.convertDateMonthYear(renteViel
                                    .getDateRevocationRequerant());
                        } else if (JadeDateUtil.isGlobazDateMonthYear(renteViel.getDateRevocationRequerant())) {
                            dateRevocationDemandeRente = renteViel.getDateRevocationRequerant();
                        } else {
                            dateRevocationDemandeRente = "";
                        }
                        viewBean.setDateRevocationDemandeRente(dateRevocationDemandeRente);

                        viewBean.setIsAjourneDemandeRente(renteViel.getIsAjournementRequerant());
                        viewBean.setIsTransfere(infoCompl.getIsTransfere());

                    }

                }

                if (getAction().getActionPart().equals(FWAction.ACTION_NOUVEAU)) {
                    viewBean = (RESaisieManuelleRABCPDViewBean) beforeNouveau(session, request, response, viewBean);
                }

                viewBean = (RESaisieManuelleRABCPDViewBean) mainDispatcher.dispatch(viewBean, getAction());
                session.removeAttribute("viewBean");
                session.setAttribute("viewBean", viewBean);
                request.setAttribute(FWServlet.VIEWBEAN, viewBean);

                if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                    _destination = FWDefaultServletAction.ERROR_PAGE;
                } else {
                    _destination = getRelativeURL(request, session) + "_de.jsp";
                }

            } catch (Exception e) {
                _destination = FWDefaultServletAction.ERROR_PAGE;
            }

            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        }

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

    public void ajouterRABCPD(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws ServletException, IOException {

        try {

            // getAction().setRight(FWSecureConstants.ADD);

            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            viewBean = beforeAjouter(session, request, response, viewBean);
            viewBean = mainDispatcher.dispatch(viewBean, getAction());

            session.setAttribute("viewBean", viewBean);
            request.setAttribute(FWServlet.VIEWBEAN, viewBean);

            boolean goesToSuccessDest = !viewBean.getMsgType().equals(FWViewBeanInterface.ERROR);

            RESaisieManuelleRABCPDViewBean vb = (RESaisieManuelleRABCPDViewBean) viewBean;

            if (goesToSuccessDest) {
                _destination = "/corvus?userAction=corvus.basescalcul.saisieManuelleRABCPD.afficher&noDemandeRente="
                        + vb.getNoDemandeRente() + "&idTierRequerant=" + vb.getIdTierRequerant() + "&idBasesCalcul="
                        + vb.getIdBasesCalcul();
            } else {
                _destination = _getDestAjouterEchec(session, request, response, viewBean);
            }
        } catch (Exception e) {
            _destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        // goSendRedirect(_destination, request, response);

    }

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

    private boolean isRetourDepuisPyxis(FWViewBeanInterface viewBean) {
        return ((viewBean != null) && (viewBean instanceof RESaisieManuelleRABCPDViewBean) && ((RESaisieManuelleRABCPDViewBean) viewBean)
                .isRetourDepuisPyxis());
    }

}
