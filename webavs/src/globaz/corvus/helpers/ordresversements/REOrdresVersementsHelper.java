package globaz.corvus.helpers.ordresversements;

import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRente;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRenteManager;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.RETiersForJspUtils;
import globaz.corvus.vb.ordresversements.REOrdresVersementsViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.prestation.helpers.PRHybridHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * @author PBA
 */
public class REOrdresVersementsHelper extends PRHybridHelper {

    @Override
    protected void _retrieve(final FWViewBeanInterface viewBean, final FWAction action, final BISession session)
            throws Exception {

        REOrdresVersementsViewBean ordreVersementViewBean = (REOrdresVersementsViewBean) viewBean;

        ordreVersementViewBean.setIdTierRequerant(getIdTiersBeneficiaireDecision((BSession) session,
                ordreVersementViewBean.getIdPrestation()));

        if (ordreVersementViewBean.getIdTierRequerant() == null) {
            throw new RETechnicalException("[idTierRequerant] can't be null");
        }

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, ordreVersementViewBean.getIdTierRequerant()
                .toString());
        ordreVersementViewBean.setTiers(tiers);
        ordreVersementViewBean
                .setDetailTiers(RETiersForJspUtils.getInstance((BSession) session).getDetailsTiers(tiers));

        ordreVersementViewBean.setValidationDecisionAuthorisee(REPmtMensuel
                .isValidationDecisionAuthorise((BSession) session));

        ordreVersementViewBean.setIdDemandeRente(chargerIdDemandeRenteDepuisDecision((BSession) session,
                ordreVersementViewBean.getIdDecision()));
    }

    private String chargerIdDemandeRenteDepuisDecision(final BSession session, final String idDecision)
            throws Exception {
        REDecisionJointDemandeRenteManager manager = new REDecisionJointDemandeRenteManager();
        manager.setSession(session);
        manager.setForIdDecision(idDecision);
        manager.find();

        if (manager.isEmpty()) {
            return null;
        } else {
            REDecisionJointDemandeRente decisionJointDemandeRente = (REDecisionJointDemandeRente) manager.get(0);
            return decisionJointDemandeRente.getIdDemandeRente();
        }
    }

    private Long getIdTiersBeneficiaireDecision(final BSession session, final Long idPrestation) {
        try {
            REPrestations prestation = new REPrestations();
            prestation.setSession(session);
            prestation.setId(idPrestation.toString());
            prestation.retrieve();

            REDecisionEntity decision = new REDecisionEntity();
            decision.setSession(session);
            decision.setIdDecision(prestation.getIdDecision());
            decision.retrieve();

            return Long.parseLong(decision.getIdTiersBeneficiairePrincipal());
        } catch (Exception ex) {
            throw new RETechnicalException(ex);
        }
    }
}
