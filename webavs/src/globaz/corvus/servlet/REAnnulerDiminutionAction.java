package globaz.corvus.servlet;

import globaz.commons.nss.NSUtil;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.process.REAnnulerDiminutionRAHandler;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.corvus.vb.process.REAnnulerDiminutionViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author SCR
 */
public class REAnnulerDiminutionAction extends REDefaultProcessAction {

    /**
     * @param servlet
     */
    public REAnnulerDiminutionAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {

            FWViewBeanInterface viewBean = new REAnnulerDiminutionViewBean();

            ((REAnnulerDiminutionViewBean) viewBean).setIdRenteAccordee(request.getParameter("idRenteAccordee"));
            ((REAnnulerDiminutionViewBean) viewBean).setIdTiersBeneficiaire(request.getParameter("idTierRequerant"));

            String idTiersBeneficiaire = request.getParameter("idTiersBeneficiaire");
            if (JadeStringUtil.isEmpty(idTiersBeneficiaire)) {
                idTiersBeneficiaire = request.getParameter("idTierRequerant");
            }

            viewBean.setISession(mainDispatcher.getSession());

            request.setAttribute(FWServlet.VIEWBEAN, viewBean);
            chargementViewBean(session, (REAnnulerDiminutionViewBean) viewBean);

            RENSSDTO dto = new RENSSDTO();
            dto.setNSS(getNSSFormate(request.getParameter("idTierRequerant"), (BSession) mainDispatcher.getSession()));

            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dto);
            PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO,
                    null);

            saveViewBean(viewBean, session);

        } catch (Exception e) {
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }

    @Override
    protected void actionCustom(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String userAction = request.getParameter("userAction");
        if (userAction.equalsIgnoreCase(IREActions.ACTION_ANNULER_DIMINUTION_RENTE_ACCORDEE
                + ".testAnnonceDiminutionError")) {
            REAnnulerDiminutionViewBean viewBean = new REAnnulerDiminutionViewBean();
            viewBean.setIdRenteAccordee(request.getParameter("idRenteAccordee"));
            viewBean.setIdTiersBeneficiaire(request.getParameter("idTiersBeneficiaire"));
            viewBean.setDateDebutDroitRA(request.getParameter("dateDebutDroit"));
            viewBean.setDateFinDroitRA(request.getParameter("dateFinDroit"));
            viewBean.setGenreRente(request.getParameter("genreRente"));
            viewBean.setCodeMutation(request.getParameter("codeMutation"));
            viewBean.setISession(mainDispatcher.getSession());

            try {

                BTransaction transaction = (BTransaction) ((BSession) mainDispatcher.getSession()).newTransaction();
                RERenteAccordee ra = new RERenteAccordee();
                ra.setSession((BSession) mainDispatcher.getSession());
                ra.setIdPrestationAccordee(viewBean.getIdRenteAccordee());
                ra.retrieve();

                REAnnulerDiminutionRAHandler h = new REAnnulerDiminutionRAHandler();
                String noRevision = h.getNoRevision((BSession) mainDispatcher.getSession(), transaction, ra);
                REAnnulerDiminutionRAHandler.AnnonceDiminutionContainer adc = h.getAnnonceDiminution(
                        (BSession) mainDispatcher.getSession(), transaction, ra, noRevision);

                // Aucune annonce de diminution trouvée
                if (adc == null) {
                    response.getOutputStream().print(
                            new String(((BSession) mainDispatcher.getSession()).getLabel(
                                    "ERREUR_ANNONCE_DIMINUTION_NON_TROUVEE").getBytes("UTF-8")));
                } else {
                    JADate dateDiminution = new JADate("01."
                            + PRDateFormater.convertDate_MMAA_to_MMxAAAA(adc.moisRapport));
                    JACalendar cal = new JACalendarGregorian();
                    dateDiminution = cal.addMonths(dateDiminution, -1);
                    JADate dateDernierPaiement = new JADate(REPmtMensuel.getDateDernierPmt((BSession) mainDispatcher
                            .getSession()));

                    // La date de la diminution est >= à la date du dernier pmt
                    if ((cal.compare(dateDiminution, dateDernierPaiement) == JACalendar.COMPARE_FIRSTLOWER)) {
                        response.getOutputStream().print(
                                new String(((BSession) mainDispatcher.getSession()).getLabel(
                                        "ERREUR_ANNONCE_DIMINUTION_DATE_ANT").getBytes("UTF-8")));

                    }
                }

            } catch (Exception e) {
                response.getOutputStream().print(e.toString());
            }
        }
    }

    /**
     * Chargement des informations du viewBean
     * 
     * @param session
     * @param viewBean
     */
    private void chargementViewBean(HttpSession session, REAnnulerDiminutionViewBean viewBean) {
        RERenteAccordee rente = new RERenteAccordee();
        try {
            rente = loadRenteAccordee((BSession) viewBean.getISession(), viewBean.getIdRenteAccordee());
            viewBean.setTiersBeneficiaireInfo(getInfoTiers(rente.getSession(), rente));
            viewBean.setMontant(rente.getMontantPrestation());
            viewBean.setDateDebutDroitRA(rente.getDateDebutDroit());
            viewBean.setDateFinDroitRA(rente.getDateFinDroit());
            viewBean.setCodeMutation(rente.getCodeMutation());
            viewBean.setGenreRente(rente.getCodePrestation());

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage("Erreur lors du chargement de la rente accordée no :".concat(rente
                    .getIdPrestationAccordee()));
        }
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

    private String getNSSFormate(String idTierBeneficiaire, BSession session) {

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

}
