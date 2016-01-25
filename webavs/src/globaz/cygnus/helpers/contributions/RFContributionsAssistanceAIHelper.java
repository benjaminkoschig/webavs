package globaz.cygnus.helpers.contributions;

import globaz.corvus.utils.RETiersForJspUtils;
import globaz.cygnus.db.contributions.RFContributionsAssistanceAI;
import globaz.cygnus.db.contributions.RFContributionsAssistanceAIManager;
import globaz.cygnus.db.dossiers.RFDossier;
import globaz.cygnus.exceptions.RFBusinessException;
import globaz.cygnus.vb.contributions.RFContributionsAssistanceAIDetailViewBean;
import globaz.cygnus.vb.contributions.RFContributionsAssistanceAIListViewBean;
import globaz.cygnus.vb.contributions.RFContributionsAssistanceAIViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BIPersistentObjectList;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRStringUtils;

/**
 * @author PBA
 */
public class RFContributionsAssistanceAIHelper extends PRAbstractHelper {

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RFContributionsAssistanceAIDetailViewBean detailViewBean = (RFContributionsAssistanceAIDetailViewBean) viewBean;

        if (validate((BSession) session, detailViewBean)) {
            RFContributionsAssistanceAI contribution = new RFContributionsAssistanceAI();
            contribution.setSession((BSession) session);

            copyViewBeanIntoEntity(detailViewBean, contribution);

            verifierChevauchement(session, contribution);

            contribution.add();
        }
    }

    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RFContributionsAssistanceAIViewBean contributionViewBean = (RFContributionsAssistanceAIViewBean) viewBean;

        if (!JadeStringUtil.isBlankOrZero(contributionViewBean.getIdTierRequerant())) {
            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, contributionViewBean.getIdTierRequerant());

            contributionViewBean.setDetailRequerant(RETiersForJspUtils.getInstance((BSession) session).getDetailsTiers(
                    tiers, true));
        }

        if (!JadeStringUtil.isBlankOrZero(contributionViewBean.getIdDossier())) {
            RFDossier dossier = new RFDossier();
            dossier.setSession((BSession) session);
            dossier.setIdDossier(contributionViewBean.getIdDossier());
            dossier.retrieve();

            contributionViewBean.setEtatDossier(JadeCodesSystemsUtil.getCodeLibelle((BSession) session,
                    dossier.getCsEtatDossier()));
            contributionViewBean.setPeriodeDossier(dossier.getDateDebut() + " - " + dossier.getDateFin());
        }
    }

    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        RFContributionsAssistanceAIDetailViewBean detailViewBean = (RFContributionsAssistanceAIDetailViewBean) viewBean;

        RFContributionsAssistanceAI contribution = new RFContributionsAssistanceAI();
        contribution.setIdContributionAssistanceAI(detailViewBean.getIdContributionAssistanceAI());
        contribution.setSession((BSession) session);
        contribution.retrieve();

        contribution.delete();
    }

    @Override
    protected void _find(BIPersistentObjectList persistentList, FWAction action, BISession session) throws Exception {
        RFContributionsAssistanceAIListViewBean viewBean = (RFContributionsAssistanceAIListViewBean) persistentList;
        viewBean.setOrdreDeTri(RFContributionsAssistanceAIManager.OrdreDeTri.DateDebutPeriode);

        super._find(persistentList, action, session);
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        RFContributionsAssistanceAIDetailViewBean detailViewBean = (RFContributionsAssistanceAIDetailViewBean) viewBean;

        RFContributionsAssistanceAIManager contributionManager = new RFContributionsAssistanceAIManager();
        contributionManager.setSession((BSession) session);
        contributionManager.setForIdDossierRFM(detailViewBean.getIdDossierRFM());
        contributionManager.setOrdreDeTri(RFContributionsAssistanceAIManager.OrdreDeTri.DateDebutPeriode);
        contributionManager.find();

        if (contributionManager.size() > 0) {
            RFContributionsAssistanceAI derniereContribution = (RFContributionsAssistanceAI) contributionManager
                    .get(contributionManager.size() - 1);
            if (JadeStringUtil.isBlankOrZero(derniereContribution.getDateFinPeriode())) {
                detailViewBean.setMessageAvertissementModification(PRStringUtils.replaceString(
                        ((BSession) session).getLabel("WARNING_DERNIERE_PERIODE_CAAI_OUVERTE"), "{DATE_DEBUT}",
                        derniereContribution.getDateDebutPeriode()));
                detailViewBean.setDernierePeriodeOuverte(true);
            }
        }

        if (!JadeStringUtil.isBlankOrZero(detailViewBean.getIdContributionAssistanceAI())) {
            detailViewBean.setSession((BSession) session);
            detailViewBean.retrieve();
        }
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        RFContributionsAssistanceAIDetailViewBean detailViewBean = (RFContributionsAssistanceAIDetailViewBean) viewBean;

        if (validate((BSession) session, detailViewBean)) {
            RFContributionsAssistanceAI contribution = new RFContributionsAssistanceAI();
            contribution.setIdContributionAssistanceAI(detailViewBean.getIdContributionAssistanceAI());
            contribution.setSession((BSession) session);
            contribution.retrieve();

            copyViewBeanIntoEntity(detailViewBean, contribution);
            verifierChevauchement(session, contribution);

            contribution.update();
        }
    }

    private void copyViewBeanIntoEntity(RFContributionsAssistanceAIDetailViewBean viewBean,
            RFContributionsAssistanceAI entity) {
        entity.setIdDossierRFM(viewBean.getIdDossierRFM());

        entity.setCodeAPI(viewBean.getCodeAPI());
        entity.setDateDebutPeriode(viewBean.getDateDebutPeriode());
        entity.setDateDebutRecours(viewBean.getDateDebutRecours());
        entity.setDateDecisionAI(viewBean.getDateDecisionAI());
        entity.setDateDepotDemandeCAAI(viewBean.getDateDepotDemandeCAAI());
        entity.setDateFinPeriode(viewBean.getDateFinPeriode());
        entity.setDateFinRecours(viewBean.getDateFinRecours());
        entity.setDateReceptionDecisionCAAI(viewBean.getDateReceptionDecisionCAAI());
        entity.setMontantAPI(viewBean.getMontantAPI());
        entity.setMontantContribution(viewBean.getMontantContribution());
        entity.setNombreHeures(viewBean.getNombreHeures());
        entity.setRemarque(viewBean.getRemarque());
    }

    private boolean validate(BSession session, RFContributionsAssistanceAIDetailViewBean viewBean) {
        if (!JadeDateUtil.isGlobazDate(viewBean.getDateDebutPeriode())) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(session.getLabel("ERROR_DATE_DEBUT_PERIODE_OBLIGATOIRE"));
            return false;
        }
        if (JadeDateUtil.isGlobazDate(viewBean.getDateFinPeriode())
                && JadeDateUtil.isDateBefore(viewBean.getDateFinPeriode(), viewBean.getDateDebutPeriode())) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(session.getLabel("ERROR_DATE_FIN_DOIT_ETRE_ULTERIEURE_A_DATE_DEBUT"));
            return false;
        }
        if (JadeDateUtil.isGlobazDate(viewBean.getDateDebutRecours())
                && JadeDateUtil.isGlobazDate(viewBean.getDateFinRecours())
                && JadeDateUtil.isDateBefore(viewBean.getDateFinRecours(), viewBean.getDateDebutRecours())) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(session.getLabel("ERROR_DATE_FIN_DOIT_ETRE_ULTERIEURE_A_DATE_DEBUT"));
            return false;
        }
        return true;
    }

    private void verifierChevauchement(BISession session, RFContributionsAssistanceAI nouvelleContribution)
            throws Exception {
        RFContributionsAssistanceAIManager contributionManager = new RFContributionsAssistanceAIManager();
        contributionManager.setSession((BSession) session);
        contributionManager.setForIdDossierRFM(nouvelleContribution.getIdDossierRFM());
        contributionManager.find();

        JadePeriodWrapper nouvellePeriode = new JadePeriodWrapper(nouvelleContribution.getDateDebutPeriode(),
                nouvelleContribution.getDateFinPeriode());

        for (int i = 0; i < contributionManager.size(); i++) {
            RFContributionsAssistanceAI uneContribution = (RFContributionsAssistanceAI) contributionManager.get(i);

            if (uneContribution.getIdContributionAssistanceAI().equals(
                    nouvelleContribution.getIdContributionAssistanceAI())) {
                continue;
            }

            JadePeriodWrapper periodeDeCetteContribution = new JadePeriodWrapper(uneContribution.getDateDebutPeriode(),
                    uneContribution.getDateFinPeriode());
            switch (nouvellePeriode.comparerChevauchement(periodeDeCetteContribution)) {
                case LesPeriodesSeChevauchent:
                    throw new RFBusinessException(
                            ((BSession) session).getLabel("ERROR_CHEVAUCHEMENT_PERIODES_INTERDITE"));
                default:
                    break;
            }
        }
    }
}
