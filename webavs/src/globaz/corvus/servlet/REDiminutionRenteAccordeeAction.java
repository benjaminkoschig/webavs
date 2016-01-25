package globaz.corvus.servlet;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.diminution.IREDiminution;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.retenues.RERetenuesJointPrestationAccordee;
import globaz.corvus.db.retenues.RERetenuesJointPrestationAccordeeManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.corvus.vb.process.REDiminutionRenteAccordeeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRDateValidator;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author HPE
 */
public class REDiminutionRenteAccordeeAction extends REDefaultProcessAction {

    /**
     * @param servlet
     */
    public REDiminutionRenteAccordeeAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {

            FWViewBeanInterface viewBean = new REDiminutionRenteAccordeeViewBean();

            ((REDiminutionRenteAccordeeViewBean) viewBean).setIdRenteAccordee(request.getParameter("idRenteAccordee"));
            ((REDiminutionRenteAccordeeViewBean) viewBean).setIdTiersBeneficiaire(request
                    .getParameter("idTierRequerant"));

            // BZ 4421 Affichage si date de décès dans la date de fin de droit
            String idTiersBeneficiaire = request.getParameter("idTiersBeneficiaire");
            if (JadeStringUtil.isEmpty(idTiersBeneficiaire)) {
                idTiersBeneficiaire = request.getParameter("idTierRequerant");
            }
            try {
                PRTiersWrapper tierswrap = PRTiersHelper
                        .getTiersParId(mainDispatcher.getSession(), idTiersBeneficiaire);
                String dateDeces = tierswrap.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES);

                if (!JadeStringUtil.isEmpty(dateDeces)) {
                    ((REDiminutionRenteAccordeeViewBean) viewBean).setDateFinDroit(PRDateFormater
                            .convertDate_JJxMMxAAAA_to_MMxAAAA(dateDeces));
                }
            } catch (Exception e) {
            }

            viewBean.setISession(mainDispatcher.getSession());

            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            chargementViewBean(session, (REDiminutionRenteAccordeeViewBean) viewBean);

            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(getNumeroAvsFormate(request.getParameter("idTierRequerant"),
                    (BSession) mainDispatcher.getSession()));

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

            saveViewBean(viewBean, session);

        } catch (Exception e) {
            destination = ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String userAction = request.getParameter("userAction");
        if (userAction.equalsIgnoreCase(IREActions.ACTION_DIMINUTION_RENTE_ACCORDEE + ".testRetenueError")) {
            REDiminutionRenteAccordeeViewBean viewBean = new REDiminutionRenteAccordeeViewBean();
            viewBean.setIdRenteAccordee(request.getParameter("idRenteAccordee"));
            viewBean.setIdTiersBeneficiaire(request.getParameter("idTiersBeneficiaire"));
            viewBean.setDateDebutDroit(request.getParameter("dateDebutDroit"));
            viewBean.setDateFinDroit(request.getParameter("dateFinDroit"));
            viewBean.setCsCodeTraitement(request.getParameter("csGenreDiminution"));
            viewBean.setISession(mainDispatcher.getSession());

            try {

                if (!PRDateValidator.isDateFormat_MMxAAAA(viewBean.getDateFinDroit())) {
                    if (!JadeStringUtil.isBlank(viewBean.getDateFinDroit())) {
                        throw new Exception(((BSession) mainDispatcher.getSession()).getLabel("JSP_WARN_DIM_R_A_002"));
                    }
                }

                String resultCheckTraitement = checkTraitement((BSession) mainDispatcher.getSession(), new JADate(
                        viewBean.getDateFinDroit()), new JADate(viewBean.getDateDebutDroit()),
                        viewBean.getCsCodeTraitement());
                if (resultCheckTraitement != null) {

                    response.getOutputStream().print(new String(resultCheckTraitement.getBytes("UTF-8")));
                }
                // Si la RA est bloquée et que le genre de diminution est RESTIT
                // PAR FACTURATION, afficher message d'erreur....
                else if (IREDiminution.CS_GENRE_TRATEMENT_DIM_RESTITUTION_PAR_FACTURATION.equals(request
                        .getParameter("csGenreDiminution"))) {
                    REPrestationsAccordees pr = new REPrestationsAccordees();
                    pr.setSession((BSession) mainDispatcher.getSession());
                    pr.setIdPrestationAccordee(request.getParameter("idRenteAccordee"));
                    pr.retrieve();
                    if (pr.getIsPrestationBloquee() != null && pr.getIsPrestationBloquee().booleanValue()) {
                        response.getOutputStream().print(
                                ((BSession) mainDispatcher.getSession()).getLabel("JSP_ERROR_DIM_R_A_007"));
                    }
                }
            } catch (Exception e) {
                response.getOutputStream().print(
                        ((BSession) mainDispatcher.getSession()).getLabel("JSP_WARN_DIM_R_A_002"));
            }
        } else if (userAction.equalsIgnoreCase(IREActions.ACTION_DIMINUTION_RENTE_ACCORDEE + ".testRetenueWarning")) {
            REDiminutionRenteAccordeeViewBean viewBean = new REDiminutionRenteAccordeeViewBean();
            viewBean.setIdRenteAccordee(request.getParameter("idRenteAccordee"));
            viewBean.setIdTiersBeneficiaire(request.getParameter("idTiersBeneficiaire"));
            viewBean.setDateDebutDroit(request.getParameter("dateDebutDroit"));
            viewBean.setDateFinDroit(request.getParameter("dateFinDroit"));
            viewBean.setCsCodeTraitement(request.getParameter("csGenreDiminution"));
            viewBean.setISession(mainDispatcher.getSession());
            try {

                String msg = testCodeMutation(loadRenteAccordee((BSession) viewBean.getISession(),
                        viewBean.getIdRenteAccordee()));

                if (JadeStringUtil.isBlankOrZero(msg)) {
                    msg = "";
                } else {
                    msg += ". ";
                }
                if (testRetenueEnCours(
                        loadRenteAccordee((BSession) viewBean.getISession(), viewBean.getIdRenteAccordee()),
                        new JADate(viewBean.getDateDebutDroit()), new JADate(viewBean.getDateFinDroit()))
                        .booleanValue()) {
                    msg += ((BSession) mainDispatcher.getSession()).getLabel("JSP_WARN_DIM_R_A_001");
                }

                if (!JadeStringUtil.isBlankOrZero(msg)) {
                    response.getOutputStream().print(msg);
                }

            } catch (Exception e) {
                response.getOutputStream().print(
                        ((BSession) mainDispatcher.getSession()).getLabel("JSP_WARN_DIM_R_A_002"));
            }
            // Ajouter pour le bz 4230
        } else if (userAction.equalsIgnoreCase(IREActions.ACTION_DIMINUTION_RENTE_ACCORDEE
                + ".afficheMontantArestituer")) {
            REDiminutionRenteAccordeeViewBean viewBean = new REDiminutionRenteAccordeeViewBean();
            viewBean.setIdRenteAccordee(request.getParameter("idRenteAccordee"));
            viewBean.setDateFinDroit(request.getParameter("dateFinDroit"));
            viewBean.setCsCodeTraitement(request.getParameter("csGenreDiminution"));
            viewBean.setISession(mainDispatcher.getSession());
            BSession bSession = (BSession) mainDispatcher.getSession();
            String dateDernierPMT = REPmtMensuel.getDateDernierPmt(bSession);
            JACalendarGregorian cal = new JACalendarGregorian();

            try {

                int i = cal.compare(dateDernierPMT, viewBean.getDateFinDroit());

                if (IREDiminution.CS_GENRE_TRATEMENT_DIM_RESTITUTION.equals(viewBean.getCsCodeTraitement())
                        && !JadeStringUtil.isEmpty(viewBean.getDateFinDroit()) && JACalendar.COMPARE_SECONDLOWER == i) {

                    BTransaction transaction = (BTransaction) ((BSession) mainDispatcher.getSession()).newTransaction();
                    RERenteAccordee ra = new RERenteAccordee();
                    ra.setSession(bSession);
                    ra.setId(viewBean.getIdRenteAccordee());
                    ra.retrieve();
                    JADate dateDernierPmt = new JADate(REPmtMensuel.getDateDernierPmt(bSession));
                    JADate dateFinRA = new JADate(viewBean.getDateFinDroit());
                    dateFinRA = cal.addMonths(dateFinRA, 1);

                    FWCurrency montantARestituer = getCumulDesMontantsDeA(bSession, transaction, ra,
                            PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateFinRA.toStrAMJ()),
                            PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dateDernierPmt.toStrAMJ()));

                    String montant = bSession.getLabel("JSP_DIM_R_A_MONTANT_RESTITUTION") + " : "
                            + montantARestituer.toStringFormat();
                    response.getOutputStream().print(montant);
                }
            } catch (Exception e) {
                response.getOutputStream().print(
                        ((BSession) mainDispatcher.getSession()).getLabel("JSP_WARN_DIM_R_A_002"));
            }
        }
    }

    /**
     * Chargement des informations du viewBean
     * 
     * @param session
     * @param viewBean
     */
    private void chargementViewBean(HttpSession session, REDiminutionRenteAccordeeViewBean viewBean) {
        RERenteAccordee rente = new RERenteAccordee();
        try {
            rente = loadRenteAccordee((BSession) viewBean.getISession(), viewBean.getIdRenteAccordee());
            viewBean.setTiersBeneficiaireInfo(getInfoTiers(rente.getSession(), rente));
            viewBean.setGenreDiminution(rente.getCodePrestation());
            viewBean.setMontant(rente.getMontantPrestation());
            viewBean.setDateDebutDroit(rente.getDateDebutDroit());
            viewBean.setIsPrestationBloquee(rente.getIsPrestationBloquee());
            viewBean.setTotalBloque(searchTotalBloque(rente));

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage("Erreur lors du chargement de la rente accordée no :".concat(rente
                    .getIdPrestationAccordee()));
        }
    }

    /**
     * 
     * @param rente
     * @param dateDebutDroit
     * @param dateFinDroit
     * @return null si aucun message d'erreur
     * @throws Exception
     */
    private String checkTraitement(BSession session, JADate dateFinRA, JADate dateDebut,
            String csGenreTraitementDiminution) throws Exception {

        if (JAUtil.isDateEmpty(dateFinRA.toString())) {
            return session.getLabel("ERRREUR_DATE_FIN_DROIT_OBLIGATOIRE");
        }

        JACalendarGregorian cal = new JACalendarGregorian();
        dateDebut = cal.addMonths(dateDebut, -1);

        int i = cal.compare(dateDebut, dateFinRA);

        JADate dateDernierPmt = new JADate(REPmtMensuel.getDateDernierPmt(session));
        int diff = PRDateFormater.nbrMoisEntreDates(dateDernierPmt, dateFinRA) - 1;

        // Matrice des cas possibles...
        // Dernier pmt effectué : 06.2008
        //
        // date
        // Retrait + Restit Retrait Restit diminution diff
        // ================ ======= ====== ========== ====
        // OK KO OK 03.08 -3
        // OK KO OK 04.08 -2
        // KO OK OK 05.08 -1
        // KO KO KO 06.08 0
        // KO KO KO 08.08 +1

        if (diff > 0) {
            return session.getLabel("JSP_ERROR_DIM_R_A_105");
        }

        // BZ 4421 si date fin plus petite que date debut - 1 mois, alors
        // message d'erreur
        if (i == JACalendar.COMPARE_SECONDLOWER) {
            return session.getLabel("JSP_ERROR_DIM_R_A_008");
        }

        if (diff >= 0) {

            if (!JadeStringUtil.isBlankOrZero(csGenreTraitementDiminution)) {
                return session.getLabel("JSP_ERROR_DIM_R_A_002");
            }
        } else {

            if (IREDiminution.CS_GENRE_TRATEMENT_DIM_RETRAIT.equals(csGenreTraitementDiminution)) {
                // Pour les cas de retrait, un seul mois de retard
                if (diff != -1) {
                    return session.getLabel("JSP_ERROR_DIM_R_A_101");
                }
            } else if (IREDiminution.CS_GENRE_TRATEMENT_DIM_RETRAIT_ET_RESTITUTION.equals(csGenreTraitementDiminution)
                    || IREDiminution.CS_GENRE_TRATEMENT_DIM_RETRAIT_ET_RESTITUTION_PAR_FACTURATION
                            .equals(csGenreTraitementDiminution)) {

                // Pour les cas de retrait et restitution, il doit y avoir plus
                // d'un mois de retard.
                if (diff == -1) {
                    return session.getLabel("JSP_ERROR_DIM_R_A_103");
                }

            } else if (JadeStringUtil.isBlankOrZero(csGenreTraitementDiminution)) {

                return session.getLabel("JSP_ERROR_DIM_R_A_104");

            }

        }

        return null;
    }

    public FWCurrency getCumulDesMontantsDeA(BSession session, BTransaction transaction, RERenteAccordee ra,
            String dateDebut, String dateFin) throws Exception {

        REPrestationsDuesManager mgr = new REPrestationsDuesManager();
        mgr.setSession(session);
        mgr.setForIdRenteAccordes(ra.getIdPrestationAccordee());
        mgr.setToDateDebut(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateFin));
        mgr.setFromDateFin(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateDebut));
        mgr.setForCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
        mgr.setOrderBy(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT + " ASC ");
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        JACalendar cal = new JACalendarGregorian();

        JADate df = new JADate(dateFin);

        FWCurrency result = new FWCurrency(0);
        for (int i = 0; i < mgr.size(); i++) {
            REPrestationDue pd = (REPrestationDue) mgr.getEntity(i);

            // Check si la date calIdx est incluse dans la prestation due.
            // Si ce n'est pas le cas, on passe à la suivante
            JADate ddPD = new JADate(pd.getDateDebutPaiement());

            JADate calIdx = new JADate(dateDebut);
            if (cal.compare(calIdx, ddPD) == JACalendar.COMPARE_FIRSTLOWER) {
                calIdx = new JADate(pd.getDateDebutPaiement());
            }

            JADate dfPD = null;
            if (JadeStringUtil.isBlankOrZero(pd.getDateFinPaiement())) {
                dfPD = new JADate("31.12.2999");
            } else {
                dfPD = new JADate(pd.getDateFinPaiement());
            }

            while ((cal.compare(calIdx, ddPD) == JACalendar.COMPARE_FIRSTUPPER || cal.compare(calIdx, ddPD) == JACalendar.COMPARE_EQUALS)
                    && (cal.compare(calIdx, dfPD) == JACalendar.COMPARE_FIRSTLOWER || cal.compare(calIdx, dfPD) == JACalendar.COMPARE_EQUALS)) {

                result.add(pd.getMontant());

                // On passe au mois suivant
                calIdx = cal.addMonths(calIdx, 1);

                if (cal.compare(calIdx, df) == JACalendar.COMPARE_FIRSTUPPER) {
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * Recherches les infos pout le tiers bénéficiaire
     * 
     * @param session
     * @param rente
     * @return
     * @throws Exception
     */
    private String getInfoTiers(BSession session, RERenteAccordee rente) throws Exception {
        PRTiersWrapper tiersBeneficiaire = loadTiersBeneficiaire(session, rente);
        if (tiersBeneficiaire != null) {
            return tiersBeneficiaire.getDescription(session);
        } else {
            throw new JAException(session.getLabel("ERREUR_IMPOSSIBLE_DE_RETROUVER_INFOS_BENEFICIAIRE"), "No :"
                    + rente.getIdTiersBeneficiaire());
        }
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
     * Chargement de l'enregistrement rente accordée
     * 
     * @param session
     * @param idRenteAccordee
     * @return
     * @throws Exception
     */
    private RERenteAccordee loadRenteAccordee(BSession session, String idRenteAccordee) throws Exception {
        RERenteAccordee rente = new RERenteAccordee();
        rente.setSession(session);
        rente.setIdPrestationAccordee(idRenteAccordee);
        rente.retrieve();
        return rente;
    }

    /**
     * Chargement du tiers bénéficiaire
     * 
     * @param session
     * @param rente
     * @return
     * @throws Exception
     */
    private PRTiersWrapper loadTiersBeneficiaire(BSession session, RERenteAccordee rente) throws Exception {
        PRTiersWrapper tiersBeneficiaire = null;
        if (tiersBeneficiaire == null) {
            if (!JadeStringUtil.isIntegerEmpty(rente.getIdTiersBeneficiaire())) {
                tiersBeneficiaire = PRTiersHelper.getTiersParId(session, rente.getIdTiersBeneficiaire());
            }
        }
        return tiersBeneficiaire;
    }

    /**
     * Recherche du montant total bloqué
     * 
     * @param rente
     * @return
     * @throws Exception
     */
    private FWCurrency searchTotalBloque(RERenteAccordee rente) throws Exception {

        if (JadeStringUtil.isBlankOrZero(rente.getIdEnteteBlocage())) {
            return new FWCurrency(0);
        }

        REEnteteBlocage entete = new REEnteteBlocage();
        entete.setSession(rente.getSession());
        entete.setIdEnteteBlocage(rente.getIdEnteteBlocage());
        entete.retrieve();

        FWCurrency montantADebloquer = new FWCurrency(0);
        if (!entete.isNew()) {
            montantADebloquer.add(entete.getMontantBloque());
            montantADebloquer.sub(entete.getMontantDebloque());
        }
        return montantADebloquer;
    }

    private String testCodeMutation(RERenteAccordee ra) {

        if ("02".equals(ra.getCodeCasSpeciaux1()) || "02".equals(ra.getCodeCasSpeciaux2())
                || "02".equals(ra.getCodeCasSpeciaux3()) || "02".equals(ra.getCodeCasSpeciaux4())
                || "02".equals(ra.getCodeCasSpeciaux5())) {

            return ra.getSession().getLabel("JSP_WARN_DIM_R_A_003");
        } else if ("05".equals(ra.getCodeCasSpeciaux1()) || "05".equals(ra.getCodeCasSpeciaux2())
                || "05".equals(ra.getCodeCasSpeciaux3()) || "05".equals(ra.getCodeCasSpeciaux4())
                || "05".equals(ra.getCodeCasSpeciaux5())) {

            return ra.getSession().getLabel("JSP_WARN_DIM_R_A_004");
        } else {
            return null;
        }
    }

    /**
     * Recherche si retenue en cours
     * 
     * @param rente
     * @param dateFinDroit
     * @return
     * @throws Exception
     */
    private Boolean testRetenueEnCours(RERenteAccordee rente, JADate dateDebutDroit, JADate dateFinDroit)
            throws Exception {
        Boolean diminutionActive = new Boolean(false);
        if (!JAUtil.isDateEmpty(dateFinDroit.toString())) {
            RERetenuesJointPrestationAccordeeManager manager = new RERetenuesJointPrestationAccordeeManager();
            RERetenuesJointPrestationAccordee entity = null;
            manager.setSession(rente.getSession());
            manager.addCsEtatRenteList(IREPrestationAccordee.CS_ETAT_PARTIEL);
            manager.addCsEtatRenteList(IREPrestationAccordee.CS_ETAT_VALIDE);
            manager.setForIdRenteAccordee(rente.getIdPrestationAccordee());
            manager.find();
            JACalendarGregorian cal = new JACalendarGregorian();
            for (int i = 0; i < manager.size(); i++) {
                entity = (RERetenuesJointPrestationAccordee) manager.getEntity(i);
                JADate dateDebutRetenue = new JADate(entity.getDateDebutRetenue());
                // si date de fin = 0
                // et date de début des retenues <= date de fin du droit
                if (JAUtil.isDateEmpty(entity.getDateFinRetenue())
                        && (cal.compare(dateDebutRetenue, dateFinDroit) == JACalendar.COMPARE_FIRSTLOWER || cal
                                .compare(dateDebutRetenue, dateFinDroit) == JACalendar.COMPARE_EQUALS)) {
                    diminutionActive = new Boolean(true);
                    break;
                }
                // si date de début des retenues >= date de fin du droit
                if (cal.compare(dateDebutRetenue, dateFinDroit) == JACalendar.COMPARE_FIRSTUPPER
                        || cal.compare(dateDebutRetenue, dateFinDroit) == JACalendar.COMPARE_EQUALS) {
                    diminutionActive = new Boolean(true);
                    break;
                }
                // si date de début des retenues >= date de début du droit
                // et date de début des retenues <= date de fin du droit
                if ((cal.compare(dateDebutRetenue, dateDebutDroit) == JACalendar.COMPARE_FIRSTUPPER || cal.compare(
                        dateDebutRetenue, dateDebutDroit) == JACalendar.COMPARE_EQUALS)
                        && (cal.compare(dateDebutRetenue, dateFinDroit) == JACalendar.COMPARE_FIRSTLOWER || cal
                                .compare(dateDebutRetenue, dateFinDroit) == JACalendar.COMPARE_EQUALS)) {
                    diminutionActive = new Boolean(true);
                    break;
                }

            }
        }
        return diminutionActive;
    }

}
